package me.ssu.querydslrestapi.infra.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.ssu.querydslrestapi.global.dto.ApiInfo;

import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class ApiLogRequest {
	private final ApiInfo apiInfo;
	private final int httpStatus;
	private final String requestMethod;
	private final String requestUrl;
	private final Map<String, Object> requestHeaders;
	private final String requestQueryString;
	private final Map<String, Object> requestBodyData;
	private final Map<String, Object> responseText;
	private final String refererUrl;
}