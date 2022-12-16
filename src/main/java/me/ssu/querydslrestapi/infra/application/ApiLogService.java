package me.ssu.querydslrestapi.infra.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ssu.querydslrestapi.infra.dto.ApiLogRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiLogService {

	@Async
	public void apiLogSave(ApiLogRequest apiLog) {
		log.debug("[API LOG] Request URL : ({}) {}", apiLog.getRequestMethod(), apiLog.getRequestUrl());
		log.debug("[API LOG] Request Headers : {}", apiLog.getRequestHeaders());
		log.debug("[API LOG] Request QueryString : {}", apiLog.getRequestQueryString());
		log.debug("[API LOG] Request BodyData : {}", apiLog.getRequestBodyData());
		log.debug("[API LOG] Referer URL : {}", apiLog.getRefererUrl());
		log.debug("[API LOG] Response Body : {}", apiLog.getResponseText());
	}
}