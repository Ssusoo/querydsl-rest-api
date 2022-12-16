package me.ssu.querydslrestapi.global.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import me.ssu.querydslrestapi.global.error.exception.ExternalApiUnavailableException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String method, Response response) {
		if (method.contains("similarTextPost")) {
			switch (response.status()) {
				case 429:
					return new ExternalApiUnavailableException("Too Many Requests");
				case 401:
					return new ExternalApiUnavailableException("UnAuthorized access");
				case 400:
					return new ExternalApiUnavailableException("Validation error");
				case 500:
					return new ExternalApiUnavailableException("Unknown error occurred");
				default:
					return new Exception(response.reason());
			}
		}
		return null;
	}
}