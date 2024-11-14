package cn.yt4j.bot2.controller;

import cn.yt4j.bot2.config.TelegramProperty;
import cn.yt4j.bot2.manager.BlackGodBotManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("bot")
public class BotController {

	private final TelegramProperty telegramProperty;

	private final BlackGodBotManager blackGodBotManager;

	@PostMapping("send")
	public void sendMessage(@RequestBody String message) {
		blackGodBotManager.sendMessage(message, telegramProperty.getChannelId(), true);
	}

	@GetMapping("del")
	public void del(Integer messageId) {
		blackGodBotManager.del(messageId, telegramProperty.getGroupId());
	}

	@PostMapping("sendQun")
	public void sendQunMessage(@RequestBody String message) {
		blackGodBotManager.sendMessage(message, telegramProperty.getGroupId(), true);
	}

}
