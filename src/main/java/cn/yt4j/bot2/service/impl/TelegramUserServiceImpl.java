package cn.yt4j.bot2.service.impl;

import cn.yt4j.bot2.entity.TelegramUser;
import cn.yt4j.bot2.mapper.TelegramUserMapper;
import cn.yt4j.bot2.service.TelegramUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 电报用户表(TelegramUser)表服务实现类
 *
 * @author makejava
 * @since 2024-11-05 16:32:39
 */
@Service
public class TelegramUserServiceImpl extends ServiceImpl<TelegramUserMapper, TelegramUser>
		implements TelegramUserService {

}
