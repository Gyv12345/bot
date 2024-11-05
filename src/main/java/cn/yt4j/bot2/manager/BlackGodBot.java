package cn.yt4j.bot2.manager;

import cn.yt4j.bot2.config.TelegramProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@Slf4j
public class BlackGodBot implements SpringLongPollingBot {

    @Autowired
    private TelegramClient telegramClient;

    @Autowired
    private BlackGodBotConsumer consumer;

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
    public String getBotToken() {
        return telegramProperty.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return consumer;
    }

    public void sendMessage(String messageText) {
        SendMessage message = SendMessage // Create a message object
                .builder()
                .chatId(gongBangChatId)
                .text(messageText)
                .parseMode("MarkdownV2")
                .build();

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendBaoGaoMessage(String messageText) {
        SendMessage message = SendMessage // Create a message object
                .builder()
                .chatId(baogaoChatId)
                .text(messageText)
                .build();

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}
