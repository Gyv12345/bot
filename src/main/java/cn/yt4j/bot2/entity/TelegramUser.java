package cn.yt4j.bot2.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 电报用户表(TelegramUser)表实体类
 *
 * @author makejava
 * @since 2024-11-05 17:24:16
 */
@Data
public class TelegramUser implements Serializable {

	@Serial
	private static final long serialVersionUID = -16460404609609349L;

	/**
	 * 用户唯一标识符
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

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
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	private LocalDateTime updateTime;

}
