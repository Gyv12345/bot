package cn.yt4j.bot2.entity.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class CozeApiBo {

	@JsonProperty("bot_id")
	private String botId;

	@JsonProperty("user_id")
	private String userId = "BlackGodBot";

	@JsonProperty("stream")
	private Boolean stream = false;

	@JsonProperty("auto_save_history")
	private Boolean autoSaveHistory = true;

	@JsonProperty("additional_messages")
	private List<AdditionalMessagesBo> additionalMessages;

}
