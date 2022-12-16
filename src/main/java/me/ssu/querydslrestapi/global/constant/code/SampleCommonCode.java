package me.ssu.querydslrestapi.global.constant.code;

import lombok.Getter;

/**
 * 코드_상세
 */
@Getter
public enum SampleCommonCode {

	TERMS_ESSENTIAL("SM010001", "필수");

	private final String code;
	private final String codeField;

	SampleCommonCode(String code, String codeField) {
		this.code = code;
		this.codeField = codeField;
	}
}
