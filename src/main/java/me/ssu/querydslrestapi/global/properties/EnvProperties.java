package me.ssu.querydslrestapi.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "env")
@Setter
@Getter
public class EnvProperties {
	private String serverInstanceId;
	private byte[] kisaSeedCbcSecretKey;
	private byte[] kisaSeedCbcIv;
}
