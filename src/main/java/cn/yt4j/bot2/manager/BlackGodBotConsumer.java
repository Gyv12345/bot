package cn.yt4j.bot2.manager;

import cn.hutool.core.util.ObjectUtil;
import cn.yt4j.bot2.config.TelegramProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
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
        }
    }
}
