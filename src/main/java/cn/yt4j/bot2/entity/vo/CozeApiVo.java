package cn.yt4j.bot2.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CozeApiVo<T> {

	@JsonProperty("data")
	private T data;

	@JsonProperty("code")
	private Integer code;

	@JsonProperty("msg")
	private String msg;

}
