package cn.yt4j.bot2.manager;

import cn.hutool.core.util.ObjectUtil;
import cn.yt4j.bot2.config.TelegramProperty;
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
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@Slf4j
public class BlackGodBotConsumer implements LongPollingSingleThreadUpdateConsumer {

    @Autowired
    private TelegramClient telegramClient;

    @Autowired
    private TelegramProperty telegramProperty;

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
                ForwardMessage forwardMessage = ForwardMessage // Create a forward message object
                        .builder()
                        .chatId(qunzuChatId)
                        .fromChatId(baogaoChatId)
                        .messageId(update.getChannelPost().getMessageId())
                        .build();
                try {
                    telegramClient.execute(forwardMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        if (update.hasMessage()) {
            String groupMessage = update.getMessage().getText(); // 获取群组消息内容
            long chatId = update.getMessage().getChatId(); // 获取群组的 chatId
            log.info("Received from " + chatId + ": " + groupMessage);

            if (update.getMessage().getNewChatMembers() != null) {
                for (User newChatMember : update.getMessage().getNewChatMembers()) {
                    if (newChatMember.getUserName().equals(telegramProperty.getBotUsername()) && newChatMember.getIsBot()) {
                        // 如果是机器人的用户名称，那么判断为机器人，记录群组的 chatId
                        log.info("New chat member: {}, chatId: {}", newChatMember.getUserName(), chatId);
                        // 获取群组的信息
                        Chat chat = update.getMessage().getChat();
                        // 新的一个群组 或者 频道，将其信息保存到数据库
                        log.info("New chat: {}, chatId: {}", chat.getTitle(), chatId);
                    }
                }
            }
            // 检查是否为回复消息，如果是，则判断是否为禁止发送消息
            checkAndRestrict(update.getMessage());
        }
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
            try {
                // 获取回复人在群组中的身份
                GetChatMember getChatMember = new GetChatMember(chatId.toString(),replyUser.getId());
                ChatMember chatMember = telegramClient.execute(getChatMember);
                // GroupAnonymousBot
                // 判断回复人是否为群主，如果是群组的身份发送信息，username应该是GroupAnonymousBot，那么最好也把创建者加上
                if (("GroupAnonymousBot".equals(chatMember.getUser().getUserName()) && "D".equals(text))
                        || ("creator".equals(chatMember.getStatus()) && "D".equals(text))) {
                    // 删除被回复的消息
                    DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), message.getReplyToMessage().getMessageId());
                    telegramClient.execute(deleteMessage);

                    // 禁止发送消息
                    ChatPermissions permissions = new ChatPermissions();
                    permissions.setCanSendMessages(false);

                    // 限制原消息发送者的权限
                    RestrictChatMember restrictChatMember = new RestrictChatMember(chatId.toString(),originalUser.getId(),permissions);
                    restrictChatMember.setChatId(chatId.toString());
                    restrictChatMember.setUserId(originalUser.getId());
                    restrictChatMember.setUntilDate((int) (System.currentTimeMillis() / 1000) + 3600); // 设置限制时长（1小时）

                    telegramClient.execute(restrictChatMember);
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
