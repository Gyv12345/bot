package cn.yt4j.bot2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "yt4j.telegram")
public class TelegramProperty {

	private String token;

	private String botUsername;

}
