package me.ssu.querydslrestapi.global.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ssu.querydslrestapi.global.constant.ApiResponseCode;

@Getter
@JsonPropertyOrder({ "code", "message", "data" })
@Slf4j
public class ApiResponse<T> {
	private final String code;
	private final String message;
	private final T data;

	@Builder
	public ApiResponse(final String code, final String message, final T data) {
		this.code = code == null ? ApiResponseCode.OK.getCode() : code;
		this.message = message == null ? ApiResponseCode.OK.getMessage() : message;
		this.data = data;
	}
}