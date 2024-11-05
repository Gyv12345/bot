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
 * 电报群组频道表(TelegramGroupChannel)表实体类
 *
 * @author makejava
 * @since 2024-11-05 17:42:57
 */
@Data
public class TelegramGroupChannel implements Serializable {

	@Serial
	private static final long serialVersionUID = 412009258178105175L;

	/**
	 * 群组或频道唯一标识符
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 电报群组或频道ID
	 */
	private Long telegramGroupId;

	/**
	 * 群组或频道名称
	 */
	private String groupName;

	/**
	 * 群组或频道描述
	 */
	private String description;

	/**
	 * 类型（G表示群组，C表示频道）
	 */
	private String type;

	/**
	 * 成员数量
	 */
	private Integer memberCount;

	/**
	 * 状态（0正常 1停用）
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
