package cn.yt4j.bot2.entity.dto;

import cn.yt4j.bot2.entity.TelegramUser;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 电报用户表(TelegramUser)表实体类
 *
 * @author makejava
 * @since 2024-11-05 16:32:40
 */
@Data
@AutoMapper(target = TelegramUser.class, reverseConvertGenerate = false)
public class TelegramUserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -30914784067040324L;

    /**
     * 用户唯一标识符
     */
    private Long id;

    /**
     * 租户标识符
     */
    private Long tenantId;

    /**
     * 电报用户唯一ID
     */
    private String telegramUserId;

    /**
     * 电报用户名
     */
    private String username;

    /**
     * 用户显示名称
     */
    private String displayName;

    /**
     * 用户手机号
     */
    private String phoneNumber;

    /**
     * 用户头像URL
     */
    private String profilePhoto;

    /**
     * 用户状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}

