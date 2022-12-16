package me.ssu.querydslrestapi.domains.common.controller;

import lombok.extern.slf4j.Slf4j;
import me.ssu.querydslrestapi.global.dto.ApiResponse;

@Slf4j
@SuppressWarnings("unchecked")
public abstract class BaseController {

	/**
	 * 정상 (200) 응답 (결과 데이터 없음)
	 */
	public ApiResponse<Object> responseJson() {
		return responseJson(null);
	}

	/**
	 * 정상 (200) 응답 (결과 데이터 포함)
	 * @param data : API 결과 데이터 컬렉션, 모델 ...
	 */
	public <T> ApiResponse<T> responseJson(T data) {
		return (ApiResponse<T>) ApiResponse.builder()
				.data(data)
				.build();
	}
}