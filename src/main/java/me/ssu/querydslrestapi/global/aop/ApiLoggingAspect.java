package me.ssu.querydslrestapi.global.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ssu.querydslrestapi.global.constant.CommonConstant;
import me.ssu.querydslrestapi.global.dto.ApiInfo;
import me.ssu.querydslrestapi.global.properties.EnvProperties;
import me.ssu.querydslrestapi.global.util.ConverterUtil;
import me.ssu.querydslrestapi.global.util.HttpUtil;
import me.ssu.querydslrestapi.infra.application.ApiLogService;
import me.ssu.querydslrestapi.infra.dto.ApiLogRequest;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * API Request & Response Logging
 */
@SuppressWarnings({"EmptyMethod", "unused"})
@Aspect
@Component
@RequiredArgsConstructor
@Profile(value = { CommonConstant.ActiveProfile.DEV, CommonConstant.ActiveProfile.ALPHA })
@Slf4j
public class ApiLoggingAspect {
	private final Environment environment;
	private final ApiLogService apiLogService;
	private final EnvProperties env;

	@Pointcut("execution(* me.ssu.*.domains.*.controller.*Controller.*(..))")
	public void api() {
	}

	@Pointcut("execution(* me.ssu.*.global.error.GlobalExceptionHandler.handle*(..))")
	protected void exceptions() {
	}

	@Pointcut("execution(public * *(..))")
	public void publicMethods() {
	}

	@Pointcut("execution(protected * *(..))")
	public void protectedMethods() {
	}

	@Pointcut("!@annotation(me.ssu.querydslrestapi.global.annotation.NoLogging)")
	public void noLogging() {

	}

	/**
	 * ApiInfo 객체 생성 후 Request Attribute 에 저장
	 */
	@Before("(api() && publicMethods() && noLogging()) || (exceptions() && protectedMethods()) && args(..)")
	public void makeApiInfo() {
		var request = HttpUtil.getHttpServletRequest();
		assert request != null;

		HttpUtil.setRequestAttribute(request, "apiInfo", ApiInfo.makeRequestApiInfo(env, request));
	}

	/**
	 * API 결과 반환 후 로그 적재
	 */
	@AfterReturning(pointcut = "(api() && publicMethods() && noLogging()) || (exceptions() && protectedMethods())",
			returning = "result")
	public void saveApiLog(final Object result) {
		if (result.getClass().getName().contains("ModelAndView")) {
			return;
		}

		try {
			var request = HttpUtil.getHttpServletRequest();
			var response = ConverterUtil.convertJsonToMap(ConverterUtil.convertObjectToJson(result));
			assert request != null;
			assert response != null;

			var httpStatus = Integer.parseInt(response.getOrDefault("statusCodeValue", 200).toString());

			// API 메타정보
			ApiInfo apiInfo;
			try {
				apiInfo = ApiInfo.makeResponseApiInfo((ApiInfo) HttpUtil.getRequestAttribute(request, "apiInfo"));
			} catch (Exception e) {
				e.printStackTrace();
				apiInfo = ApiInfo.makeRequestApiInfo(env);
			}

			var requestBody = ConverterUtil.convertJsonToMap(HttpUtil.getRequestBody(request));
			var responseBody = ConverterUtil.convertJsonToMap(ConverterUtil.convertObjectToJson(result));

			// 개인정보 관련된 API 는 Parameter 및 responseBody 의 data 를 제거한 후 로깅한다.
			if (request.getRequestURI().contains("/manager/auth")
					|| request.getRequestURI().contains("/manager/auth/refresh")) {
				requestBody = null;
			}

			// Http Status 가 200 인 경우 resultData 는 제거, 그 외는 포함 (StackTrace 정보가 있기 때문..)
			//if (httpStatus == HttpStatus.SC_OK) {
			//	assert responseBody != null;
			//	responseBody.remove("data");
			//}
			for (String profileName : environment.getActiveProfiles()) {
				if (CommonConstant.ActiveProfile.PROD.equals(profileName)) {
					assert responseBody != null;
					responseBody.remove("data");
				}
			}

			apiLogService.apiLogSave(ApiLogRequest.builder()
					.httpStatus(httpStatus)
					.apiInfo(apiInfo)
					.requestMethod(request.getMethod())
					.requestUrl(request.getRequestURI())
					.requestHeaders(HttpUtil.getHeaderToMap(request))
					.requestQueryString(request.getQueryString())
					.requestBodyData(requestBody == null || requestBody.isEmpty() ? null : requestBody)
					.responseText(responseBody)
					.refererUrl(HttpUtil.getRefererURL(request))
					.build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}