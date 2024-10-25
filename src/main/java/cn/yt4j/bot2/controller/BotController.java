package cn.yt4j.bot2.controller;

import cn.yt4j.bot2.manager.BlackGodBot;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@RequestMapping("bot")
public class BotController {

    private final BlackGodBot blackGodBot;

    @PostMapping
    public void sendMessage(@RequestBody String message) {
        blackGodBot.sendMessage(message);
    }
}
