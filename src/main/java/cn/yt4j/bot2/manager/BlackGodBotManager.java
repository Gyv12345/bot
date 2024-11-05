package cn.yt4j.bot2.manager;

import cn.yt4j.bot2.config.TelegramProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class BlackGodBotManager {

	@Autowired
	private TelegramClient telegramClient;

	@Autowired
	private TelegramProperty telegramProperty;

	public void sendMessage(String messageText, Long chatId, Boolean isMd) {

	}

	public void forwardMessage(String messageText, Long chatId, Long fromChatId, Boolean isMd) {

	}

	public void muteUser(Long chatId, Long userId) {

	}

	@Async

	public void processSpamInfo(Update update) {

	}

	public void saveMember() {

	}

	public void saveGroup(Chat chat) {

	}

}
