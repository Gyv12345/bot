package cn.yt4j.bot2.mapper;

import cn.yt4j.bot2.entity.TelegramUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 电报用户表(TelegramUser)表数据库访问层
 *
 * @author makejava
 * @since 2024-11-05 16:32:39
 */
@Mapper
public interface TelegramUserMapper extends BaseMapper<TelegramUser> {

}

