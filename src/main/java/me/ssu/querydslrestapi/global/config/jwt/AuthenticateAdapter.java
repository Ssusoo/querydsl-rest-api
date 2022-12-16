package me.ssu.querydslrestapi.global.config.jwt;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component("authenticateAdapter")
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Slf4j
public class AuthenticateAdapter {

	private JwtAuthToken.TokenPayload tokenPayload;

	public AuthenticateAdapter() {
		this.tokenPayload = JwtAuthToken.TokenPayload.builder().build();
	}

	public void setTokenPayload(Claims claims) {
		this.tokenPayload = JwtAuthToken.TokenPayload.of(claims);
	}

}