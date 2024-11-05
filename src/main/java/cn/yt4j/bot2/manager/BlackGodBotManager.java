package cn.yt4j.bot2.manager;

import cn.yt4j.bot2.config.TelegramProperty;
import cn.yt4j.bot2.entity.TelegramGroupChannel;
import cn.yt4j.bot2.service.TelegramGroupChannelService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class BlackGodBotManager {

	@Autowired
	private TelegramClient telegramClient;

	@Autowired
	private TelegramProperty telegramProperty;

	@Autowired
	private TelegramGroupChannelService telegramGroupChannelService;

	@Async
	public void sendMessage(String messageText, Long chatId, Boolean isMd) {

	}

	@Async
	public void forwardMessage(String messageText, Long chatId, Long fromChatId, Boolean isMd) {

	}

	@Async
	@SneakyThrows
	public void muteUser(Long chatId, Long userId) {
		// 禁止发送消息
		ChatPermissions permissions = new ChatPermissions();
		permissions.setCanSendMessages(false);

		// 限制原消息发送者的权限
		RestrictChatMember restrictChatMember = new RestrictChatMember(chatId.toString(), userId, permissions);
		restrictChatMember.setChatId(chatId.toString());
		restrictChatMember.setUserId(userId);
		restrictChatMember.setUntilDate((int) (System.currentTimeMillis() / 1000) + 3600); // 设置限制时长（1小时）

		telegramClient.execute(restrictChatMember);
	}

	@Async
	public void processSpamInfo(Update update) {

	}

	public void saveMember() {

	}

	@Async
	public void saveGroup(Chat chat) {
		TelegramGroupChannel one = this.telegramGroupChannelService.getOne(Wrappers.<TelegramGroupChannel>lambdaQuery()
			.eq(TelegramGroupChannel::getTelegramGroupId, chat.getId()));
		if (one != null) {
			return;
		}
		TelegramGroupChannel groupChannel = new TelegramGroupChannel();
		groupChannel.setGroupName(chat.getTitle());
		groupChannel.setTelegramGroupId(chat.getId());
		groupChannel.setType(chat.getType());
		telegramGroupChannelService.save(groupChannel);
	}

}
