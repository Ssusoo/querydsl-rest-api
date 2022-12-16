package me.ssu.querydslrestapi.global.annotation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Swagger 에서 Pageable 객체의 출력 형태를 커스트마이징 한 클래스
 */
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Parameter(in = ParameterIn.QUERY
		, description = "페이지 번호"
		, name = "page"
		, schema = @Schema(type = "integer", defaultValue = "1"))
@Parameter(in = ParameterIn.QUERY
		, description = "페이지 사이즈"
		, name = "pageSize"
		, schema = @Schema(type = "integer", defaultValue = "15"))
public @interface
PageableAsQueryParam {}