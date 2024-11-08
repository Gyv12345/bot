package cn.yt4j.bot2.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataVo {

	@JsonProperty("id")
	private String id;

	@JsonProperty("conversation_id")
	private String conversationId;

	@JsonProperty("bot_id")
	private String botId;

	@JsonProperty("created_at")
	private Integer createdAt;

	@JsonProperty("status")
	private String status;

}
