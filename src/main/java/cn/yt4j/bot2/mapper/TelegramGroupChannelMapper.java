package cn.yt4j.bot2.mapper;

import cn.yt4j.bot2.entity.TelegramGroupChannel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 电报群组频道表(TelegramGroupChannel)表数据库访问层
 *
 * @author makejava
 * @since 2024-11-05 17:35:37
 */
@Mapper
public interface TelegramGroupChannelMapper extends BaseMapper<TelegramGroupChannel> {

}
