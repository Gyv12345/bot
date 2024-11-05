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
 * (MuteContent)表实体类
 *
 * @author makejava
 * @since 2024-11-05 21:12:48
 */
@Data
public class MuteContent implements Serializable {

	@Serial
	private static final long serialVersionUID = 600464501092628908L;

	/**
	 * 禁言内容唯一标识符
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 禁言内容
	 */
	private String content;

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
