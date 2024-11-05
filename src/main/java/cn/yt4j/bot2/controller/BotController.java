package cn.yt4j.bot2.controller;

import cn.yt4j.bot2.service.BlackGodBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("bot")
public class BotController {

	private final BlackGodBotService blackGodBotService;

}
