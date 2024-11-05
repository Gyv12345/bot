package cn.yt4j.bot2.manager;

import cn.yt4j.bot2.entity.MuteContent;
import cn.yt4j.bot2.entity.TelegramGroupChannel;
import cn.yt4j.bot2.entity.TelegramUser;
import cn.yt4j.bot2.service.MuteContentService;
import cn.yt4j.bot2.service.TelegramGroupChannelService;
import cn.yt4j.bot2.service.TelegramUserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class BlackGodBotManager {

	@Autowired
	private TelegramClient telegramClient;

	@Autowired
	private TelegramGroupChannelService telegramGroupChannelService;

	@Autowired
	private TelegramUserService telegramUserService;

	@Autowired
	private MuteContentService muteContentService;

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

	@Async
	public void saveMember(List<User> users) {
		List<TelegramUser> list = new ArrayList<>();
		for (User user : users) {
			TelegramUser one = telegramUserService
				.getOne(Wrappers.<TelegramUser>lambdaQuery().eq(TelegramUser::getTelegramUserId, user.getId()));
			if (one != null) {
				continue;
			}
			TelegramUser telegramUser = new TelegramUser();
			telegramUser.setTelegramUserId(user.getId());
			telegramUser.setUsername(user.getUserName());
			telegramUser.setDisplayName(user.getFirstName() + user.getLastName());
			telegramUser.setCreateTime(LocalDateTime.now());

			list.add(telegramUser);
		}
		telegramUserService.saveBatch(list);
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
		groupChannel.setCreateTime(LocalDateTime.now());
		telegramGroupChannelService.save(groupChannel);
	}

	@Async
	public void storeMuteInfo(String text, String nickName) {
		MuteContent muteContent = new MuteContent();
		muteContent.setContent(text);
		muteContent.setCreateTime(LocalDateTime.now());
		this.muteContentService.save(muteContent);
	}

}
