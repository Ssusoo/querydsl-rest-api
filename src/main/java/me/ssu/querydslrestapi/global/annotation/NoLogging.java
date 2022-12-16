package me.ssu.querydslrestapi.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API Logging (ApiLoggingAspect)을 제외하고자 하는 컨트롤러 메서드에 붙인다.
 * 보통, return 데이터 형식이 JSON 이 아닌 경우 (e.g. 엑셀 다운로드 등) 사용 한다.
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoLogging {
}
