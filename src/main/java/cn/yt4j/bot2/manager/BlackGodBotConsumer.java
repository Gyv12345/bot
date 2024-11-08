package cn.yt4j.bot2.manager;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.yt4j.bot2.config.TelegramProperty;
import cn.yt4j.bot2.entity.bo.AdditionalMessagesBo;
import cn.yt4j.bot2.entity.bo.CozeApiBo;
import cn.yt4j.bot2.entity.vo.CozeApiVo;
import cn.yt4j.bot2.entity.vo.CozeListVo;
import cn.yt4j.bot2.entity.vo.DataVo;
import cn.yt4j.bot2.util.CozeApiUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class BlackGodBotConsumer implements LongPollingSingleThreadUpdateConsumer {

	@Autowired
	private TelegramClient telegramClient;

	@Autowired
	private TelegramProperty telegramProperty;

	@Autowired
	private BlackGodBotManager blackGodBotManager;

	@Autowired
	private ScheduledExecutorService scheduledExecutorService;

	@Autowired
	private CozeApiUtil cozeApiUtil;

	@Autowired
	private ObjectMapper objectMapper;

	private static final String GONG_BANG_CHAT_ID = "-1002205757052";

	private static final String BAO_GAO_CHAT_ID = "-1002152980524";

	private static final String QUN_ZU_CHAT_ID = "-1002298376382";

	private static final int MESSAGE_DELETE_THRESHOLD = 6;

	@Override
	public void consume(Update update) {
		if (update == null)
			return;

		// 处理频道消息
		if (update.hasChannelPost() && update.getChannelPost().hasText()) {
			handleChannelPost(update);
		}

		// 处理成员更新
		if (update.hasMyChatMember()) {
			handleChatMemberUpdate(update);
		}

		// 处理群组消息
		if (update.hasMessage()) {
			handleGroupMessage(update);
		}
	}

	private void handleChannelPost(Update update) {
		String messageText = update.getChannelPost().getText();
		Long chatId = update.getChannelPost().getChatId();
		log.info("Received from " + chatId + ": " + messageText);

		if (ObjectUtil.equals(chatId.toString(), BAO_GAO_CHAT_ID)) {
			forwardMessage(BAO_GAO_CHAT_ID, QUN_ZU_CHAT_ID, update.getChannelPost().getMessageId());
		}
	}

	private void handleChatMemberUpdate(Update update) {
		ChatMemberUpdated myChatMember = update.getMyChatMember();
		String status = myChatMember.getNewChatMember().getStatus();
		if ("member".equals(status) || "administrator".equals(status)) {
			blackGodBotManager.saveGroup(myChatMember.getChat());
		}
	}

	private void handleGroupMessage(Update update) {
		Message message = update.getMessage();
		log.info("Received from " + message.getChatId() + ": " + message.getText());

		if (CollectionUtil.isNotEmpty(message.getNewChatMembers())) {
			// 可处理新成员加入逻辑
		}

		// 检查并限制回复消息
		checkAndRestrict(message);

		// 处理垃圾信息
		processSpamInfo(update);
	}

	@Async
	@SneakyThrows
	public void processSpamInfo(Update update) {
		Message message = update.getMessage();
		Long chatId = message.getChatId();
		Long userId = message.getFrom().getId();

		GetChatMember getChatMember = new GetChatMember(chatId.toString(), userId);
		ChatMember chatMember = telegramClient.execute(getChatMember);
		String status = chatMember.getStatus();

		if (isAdminOrBot(status, chatMember)) {
			return;
		}

		String userContent = chatMember.getUser().getFirstName() + " " + chatMember.getUser().getLastName() + ":"
				+ message.getText();
		CozeApiVo<DataVo> cozeApiVo = sendCozeApiRequest(userContent);

		scheduledExecutorService.schedule(() -> {
			handleCozeApiResponse(chatId, message, cozeApiVo);
		}, 3, TimeUnit.SECONDS);
	}

	private CozeApiVo<DataVo> sendCozeApiRequest(String content) throws Exception {
		CozeApiBo cozeApiBo = new CozeApiBo();
		cozeApiBo.setBotId(telegramProperty.getCozeBotId());
		cozeApiBo.setAdditionalMessages(ListUtil.of(new AdditionalMessagesBo(content)));
		String result = cozeApiUtil.question(cozeApiBo);
		return objectMapper.readValue(result, new TypeReference<CozeApiVo<DataVo>>() {
		});
	}

	@SneakyThrows
	private void handleCozeApiResponse(Long chatId, Message message, CozeApiVo<DataVo> cozeApiVo) {
		String answer = cozeApiUtil.getAnswer(cozeApiVo.getData().getId(), cozeApiVo.getData().getConversationId());
		CozeApiVo<List<CozeListVo>> cozeApiList = objectMapper.readValue(answer,
				new TypeReference<CozeApiVo<List<CozeListVo>>>() {
				});

		cozeApiList.getData()
			.stream()
			.filter(datum -> "answer".equals(datum.getType())
					&& Integer.parseInt(datum.getContent()) > MESSAGE_DELETE_THRESHOLD)
			.forEach(datum -> deleteMessage(chatId, message.getMessageId()));
	}

	private boolean isAdminOrBot(String status, ChatMember chatMember) {
		return "administrator".equals(status) || "creator".equals(status) || chatMember.getUser().getIsBot();
	}

	@Async
	public void checkAndRestrict(Message message) {
		if (message.isReply() && message.getReplyToMessage() != null) {
			restrictUserIfNeeded(message);
		}
	}

	private void restrictUserIfNeeded(Message message) {
		Long chatId = message.getChatId();
		String text = message.getText();

		try {
			GetChatMember getChatMember = new GetChatMember(chatId.toString(), message.getFrom().getId());
			ChatMember chatMember = telegramClient.execute(getChatMember);

			if (("GroupAnonymousBot".equals(chatMember.getUser().getUserName()) && "D".equals(text))
					|| ("creator".equals(chatMember.getStatus()) && "D".equals(text))) {
				deleteMessage(chatId, message.getReplyToMessage().getMessageId());
				muteUser(chatId, message.getReplyToMessage().getFrom().getId());
			}
		}
		catch (TelegramApiException e) {
			log.error("Error in restrictUserIfNeeded: ", e);
		}
	}

	private void muteUser(Long chatId, Long userId) throws TelegramApiException {
		ChatPermissions permissions = new ChatPermissions();
		permissions.setCanSendMessages(false);
		RestrictChatMember restrictChatMember = new RestrictChatMember(chatId.toString(), userId, permissions);
		restrictChatMember.setUntilDate((int) (System.currentTimeMillis() / 1000) + 3600);
		telegramClient.execute(restrictChatMember);
	}

	private void forwardMessage(String fromChatId, String toChatId, Integer messageId) {
		try {
			telegramClient
				.execute(ForwardMessage.builder().chatId(toChatId).fromChatId(fromChatId).messageId(messageId).build());
		}
		catch (TelegramApiException e) {
			log.error("Error in forwardMessage: ", e);
		}
	}

	private void deleteMessage(Long chatId, Integer messageId) {
		try {
			telegramClient.execute(new DeleteMessage(chatId.toString(), messageId));
		}
		catch (TelegramApiException e) {
			log.error("Error in deleteMessage: ", e);
		}
	}

}
