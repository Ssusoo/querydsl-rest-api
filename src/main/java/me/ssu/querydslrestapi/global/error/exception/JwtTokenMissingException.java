package me.ssu.querydslrestapi.global.error.exception;

import me.ssu.querydslrestapi.global.constant.ApiResponseCode;

public class JwtTokenMissingException extends BusinessException {

	public JwtTokenMissingException() {
		super(ApiResponseCode.INVALID_TOKEN);
	}
}