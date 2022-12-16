package me.ssu.querydslrestapi.global.error.exception;

import me.ssu.querydslrestapi.global.constant.ApiResponseCode;

public class ExternalApiUnavailableException  extends BusinessException {

	public ExternalApiUnavailableException() {
		super(ApiResponseCode.EXTERNAL_API_UNAVAILABLE);
	}

	public ExternalApiUnavailableException(String message) {
		super(ApiResponseCode.EXTERNAL_API_UNAVAILABLE, message);
	}
}