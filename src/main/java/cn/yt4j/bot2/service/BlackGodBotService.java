package cn.yt4j.bot2.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;

public interface BlackGodBotService {

	/**
	 * 发送消息
	 * @param messageText
	 * @param chatId
	 * @param isMd
	 */
	void sendMessage(String messageText, Long chatId, Boolean isMd);

	/**
	 * 转发消息
	 * @param messageText
	 * @param chatId
	 * @param fromChatId
	 * @param isMd
	 */
	void forwardMessage(String messageText, Long chatId, Long fromChatId, Boolean isMd);

	/**
	 * 禁言用户
	 * @param chatId
	 * @param userId
	 */
	void muteUser(Long chatId, Long userId);

	/**
	 * 处理垃圾信息
	 * @param update
	 */
	void processSpamInfo(Update update);

	/**
	 *
	 */
	void saveMember();

	/**
	 * 保存群组信息
	 * @param chat
	 */
	void saveGroup(Chat chat);

}
