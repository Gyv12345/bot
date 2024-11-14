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
	private BlackGodBotConsumer consumer;

	@Autowired
	private TelegramProperty telegramProperty;

	@Override
	public String getBotToken() {
		return telegramProperty.getToken();
	}

	@Override
	public LongPollingUpdateConsumer getUpdatesConsumer() {
		return consumer;
	}

}
