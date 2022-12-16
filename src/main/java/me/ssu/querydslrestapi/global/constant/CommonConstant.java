package me.ssu.querydslrestapi.global.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonConstant {
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Jwt {
		public static final long TOKEN_EXPIRED_MINUTES = 20L; // 인증토큰 유효 시간 (분)
		public static final long REFRESH_TOKEN_EXTENSION_HOURS = 1L; // refreshToken 연장 기준 시간
		public static final long REFRESH_TOKEN_EXPIRE_HOURS = 4L; // refreshToken 유효 시간
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Yn {
		public static final String Y = "Y";
		public static final String N = "N";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ActiveProfile {
		public static final String LOCAL = "local";
		public static final String DEV = "dev";
		public static final String ALPHA = "alpha";
		public static final String PROD = "prod";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class BatchFlag {
		public static final String COMPLETE = "C";
		public static final String REGISTER = "R";
	}
}