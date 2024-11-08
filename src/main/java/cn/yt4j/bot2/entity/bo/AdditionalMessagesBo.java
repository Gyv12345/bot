package cn.yt4j.bot2.entity.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdditionalMessagesBo {

	@JsonProperty("role")
	private String role = "user";

	@JsonProperty("content")
	private String content;

	@JsonProperty("content_type")
	private String contentType = "text";

}
