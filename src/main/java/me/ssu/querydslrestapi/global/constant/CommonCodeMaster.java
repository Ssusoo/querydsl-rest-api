package me.ssu.querydslrestapi.global.constant;

import lombok.Getter;

/**
 * 공통_코드_마스터
 */
@Getter
public enum CommonCodeMaster {

	SAMPLE_CODE("SP01", "sampleCommonCode", ""), // 샘플_코드
	;

	private final String code; // 마스터 코드
	private final String codeField; // 코드 필드 명 (=해당 코드를 사용하는 entity 의 멤버 변수 명)
	private final String parentCode; // 부모 마스터 코드

	CommonCodeMaster(String code, String codeField, String parentCode) {
		this.code = code;
		this.codeField = codeField;
		this.parentCode = parentCode;
	}
}
