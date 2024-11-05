package cn.yt4j.bot2.service.impl;

import cn.yt4j.bot2.service.BlackGodBotService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
@Service
public class BlackGodBotServiceImpl implements BlackGodBotService {

    @Override
    public void sendMessage(String messageText, Long chatId, Boolean isMd) {

    }

    @Override
    public void forwardMessage(String messageText, Long chatId, Long fromChatId, Boolean isMd) {

    }

    @Override
    public void muteUser(Long chatId, Long userId) {

    }

    @Async
    @Override
    public void processSpamInfo(Update update) {

    }
}
