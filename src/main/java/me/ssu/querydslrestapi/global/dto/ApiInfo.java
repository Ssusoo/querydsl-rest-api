package me.ssu.querydslrestapi.global.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.ssu.querydslrestapi.global.properties.EnvProperties;
import me.ssu.querydslrestapi.global.util.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
@JsonPropertyOrder({ "requestTime", "responseTime", "transactionId", "serverInstanceId", "clientIp" })
public class ApiInfo {

	private final String requestTime;
	private String responseTime;
	private final String clientIp;
	private final String serverInstanceId;
	private final String transactionId;

	public static ApiInfo makeRequestApiInfo(EnvProperties env) {
		var request = HttpUtil.getHttpServletRequest();
		return makeRequestApiInfo(env, request);
	}

	public static ApiInfo makeRequestApiInfo(EnvProperties env, HttpServletRequest request) {
		StringBuilder instanceId = new StringBuilder().append(env.getServerInstanceId());
		String localIpAddress = HttpUtil.getLocalIpAddress();

		if (localIpAddress != null && localIpAddress.contains(".")) {
			String[] ip = localIpAddress.split("\\.");

			instanceId.append("-");
			instanceId.append(ip[ip.length - 1]);
		}

		return ApiInfo.builder()
				.requestTime(getDateTime(new Timestamp(System.currentTimeMillis())))
				.clientIp((request != null) ? HttpUtil.getClientIpAddress(request) : "0.0.0.0")
				.serverInstanceId(instanceId.toString())
				.transactionId(UUID.randomUUID().toString())
				.build();
	}

	public static ApiInfo makeResponseApiInfo(ApiInfo apiInfo) {
		apiInfo.setResponseTime(new Timestamp(System.currentTimeMillis()));

		return apiInfo;
	}

	private void setResponseTime(final Timestamp time) {
		this.responseTime = getDateTime(time);
	}

	private static String getDateTime(final Timestamp time) {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

		return ZonedDateTime.ofInstant(
				Instant.ofEpochMilli(time.getTime()),
				TimeZone.getDefault().toZoneId()).format(formatter);
	}
}