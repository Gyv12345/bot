package cn.yt4j.bot2.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@Slf4j
public class BlackGodBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    private final String chatId = "-1002205757052";

    private final String baogaoChatId="-1002152980524";

    private final String qunzuChatId="-1002298376382";

    public BlackGodBot() {
        telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    @Override
    public void consume(Update update) {
        if (update.hasChannelPost() && update.getChannelPost().hasText()) {
            // Set variables
            String message_text = update.getChannelPost().getText();
            long chat_id = update.getChannelPost().getChatId();
            log.info("Received from " + chat_id + ": " + message_text);
        }

        if (update.hasMessage() && update.getMessage().isSuperGroupMessage()) {
            String groupMessage = update.getMessage().getText(); // 获取群组消息内容
            long chatId = update.getMessage().getChatId(); // 获取群组的 chatId
            log.info("Received from " + chatId + ": " + groupMessage);
        }
    }

    @Override
    public String getBotToken() {
        return "8090102155:AAHGtKI_rie3E2PdT6W2Wxkzuxl-pmLiddY";
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    public void sendMessage(String messageText) {
        SendMessage message = SendMessage // Create a message object
                .builder()
                .chatId(chatId)
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
                .parseMode("MarkdownV2")
                .build();

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        SendMessage qunzuMessage = SendMessage // Create a message object
                .builder()
                .chatId(qunzuChatId)
                .text(messageText)
                .parseMode("MarkdownV2")
                .build();

        try {
            telegramClient.execute(qunzuMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
