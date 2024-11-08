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
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
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

	/**
	 * 公榜
	 */
	private final String gongBangChatId = "-1002205757052";

	/**
	 * 报告频道
	 */
	private final String baogaoChatId = "-1002152980524";

	/**
	 * 群
	 */
	private final String qunzuChatId = "-1002298376382";

	@Override
	public void consume(Update update) {
		if (update.hasChannelPost() && update.getChannelPost().hasText()) {
			// Set variables
			String messageText = update.getChannelPost().getText();
			Long chatId = update.getChannelPost().getChatId();
			log.info("Received from " + chatId + ": " + messageText);

			if (ObjectUtil.equals(chatId.toString(), baogaoChatId)) {
				ForwardMessage forwardMessage = ForwardMessage.builder()
					.chatId(qunzuChatId)
					.fromChatId(baogaoChatId)
					.messageId(update.getChannelPost().getMessageId())
					.build();
				try {
					telegramClient.execute(forwardMessage);
				}
				catch (TelegramApiException e) {
					throw new RuntimeException(e);
				}
			}

		}

		if (update.hasMyChatMember()) {
			ChatMemberUpdated myChatMember = update.getMyChatMember();
			String status = myChatMember.getNewChatMember().getStatus();
			if ("member".equals(status) || "administrator".equals(status)) {
				// 机器人被添加进了群组或频道
				Chat chat = myChatMember.getChat();
				blackGodBotManager.saveGroup(chat);
			}
		}

		if (update.hasMessage()) {
			String groupMessage = update.getMessage().getText(); // 获取群组消息内容
			long chatId = update.getMessage().getChatId(); // 获取群组的 chatId
			log.info("Received from " + chatId + ": " + groupMessage);

			if (CollectionUtil.isNotEmpty(update.getMessage().getNewChatMembers())) {

			}
			// 检查是否为回复消息，如果是，则判断是否为禁止发送消息
			checkAndRestrict(update.getMessage());

			processSpamInfo(update);
		}
	}

	/**
	 * AI处理垃圾信息
	 */
	@Async
	@SneakyThrows
	public void processSpamInfo(Update update) {
		Message message = update.getMessage();
		Long chatId = message.getChatId();
		Long userId = message.getFrom().getId();
		// 获取用户在群组中的角色
		GetChatMember getChatMember = new GetChatMember(chatId.toString(), userId);

		ChatMember chatMember = telegramClient.execute(getChatMember);
		// 用户状态: creator, administrator, member,restricted, left, kicked
		String status = chatMember.getStatus();

		if (isAdminOrBot(status, chatMember)) {
			return;
		}
		User user = chatMember.getUser();
		CozeApiBo cozeApiBo = new CozeApiBo();
		cozeApiBo.setBotId(telegramProperty.getCozeBotId());
		AdditionalMessagesBo additionalMessagesDTO = new AdditionalMessagesBo();
		additionalMessagesDTO.setContent(user.getFirstName() + user.getLastName() + ":" + message.getText());
		cozeApiBo.setAdditionalMessages(ListUtil.of(additionalMessagesDTO));
		String result = cozeApiUtil.question(cozeApiBo);
		CozeApiVo<DataVo> cozeApiVo = objectMapper.readValue(result, new TypeReference<CozeApiVo<DataVo>>() {
		});

		scheduledExecutorService.schedule(() -> {
			CozeApiVo<DataVo> cozeVo = cozeApiVo;
			String answer = cozeApiUtil.getAnswer(cozeVo.getData().getId(), cozeVo.getData().getConversationId());
			try {
				CozeApiVo<List<CozeListVo>> cozeApiList = objectMapper.readValue(answer,
						new TypeReference<CozeApiVo<List<CozeListVo>>>() {
						});
				for (CozeListVo datum : cozeApiList.getData()) {
					if (datum.getType().equals("answer")) {
						String content = datum.getContent();
						log.info("AI:" + content);
						if (Integer.valueOf(content) > 8) {
							DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), message.getMessageId());
							telegramClient.execute(deleteMessage);
						}
					}
				}
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}

		}, 2, TimeUnit.SECONDS);

	}

	/**
	 * 判断用户是否为管理员或机器人
	 */
	private boolean isAdminOrBot(String status, ChatMember chatMember) {
		// 检查用户状态
		return status.equals("administrator") || status.equals("creator") || chatMember.getUser().getIsBot();
	}

	@Async
	public void checkAndRestrict(Message message) {
		// 确保消息是回复消息
		if (message.isReply() && message.getReplyToMessage() != null) {
			Long chatId = message.getChatId();
			User replyUser = message.getFrom(); // 获取回复人
			User originalUser = message.getReplyToMessage().getFrom(); // 获取被回复的原始消息发送者
			// 禁止发送消息
			String text = message.getText();
			String replayText = message.getReplyToMessage().getText();
			try {
				// 获取回复人在群组中的身份
				GetChatMember getChatMember = new GetChatMember(chatId.toString(), replyUser.getId());
				ChatMember chatMember = telegramClient.execute(getChatMember);
				// GroupAnonymousBot
				// 判断回复人是否为群主，如果是群组的身份发送信息，username应该是GroupAnonymousBot，那么最好也把创建者加上
				if (("GroupAnonymousBot".equals(chatMember.getUser().getUserName()) && "D".equals(text))
						|| ("creator".equals(chatMember.getStatus()) && "D".equals(text))) {
					// 删除被回复的消息
					DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(),
							message.getReplyToMessage().getMessageId());
					telegramClient.execute(deleteMessage);

					// 禁止发送消息
					ChatPermissions permissions = new ChatPermissions();
					permissions.setCanSendMessages(false);

					// 限制原消息发送者的权限
					RestrictChatMember restrictChatMember = new RestrictChatMember(chatId.toString(),
							originalUser.getId(), permissions);
					restrictChatMember.setChatId(chatId.toString());
					restrictChatMember.setUserId(originalUser.getId());
					restrictChatMember.setUntilDate((int) (System.currentTimeMillis() / 1000) + 3600); // 设置限制时长（1小时）

					telegramClient.execute(restrictChatMember);

					DeleteMessage myMessage = new DeleteMessage(chatId.toString(), message.getMessageId());
					telegramClient.execute(myMessage);

					this.blackGodBotManager.storeMuteInfo(replayText,
							originalUser.getFirstName() + originalUser.getLastName());
				}
			}
			catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}

}
