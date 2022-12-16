package me.ssu.querydslrestapi.global.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ssu.querydslrestapi.global.constant.CommonConstant;
import me.ssu.querydslrestapi.global.constant.JwtPayloadKey;
import me.ssu.querydslrestapi.global.dto.ApiInfo;
import me.ssu.querydslrestapi.global.util.HttpUtil;
import me.ssu.querydslrestapi.global.util.StringUtil;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Jwt Token-2
 *  Jwt의 생김새(.을 구분자로 3가지의 문자열로 되어 있다.)
 *      1) aaaaaa[헤더(header)]
 *      2) bbbbbb[내용(payload)]
 *      3) cccccc[서명(signature)]
 *      * aaaaa
 */
@Slf4j
public class JwtAuthToken implements AuthToken<Claims> {

	@Getter
	private final String token;
	private final Key key;

	JwtAuthToken(String token, Key key) {
		this.token = token;
		this.key = key;
	}

	JwtAuthToken(TokenPayload dto, Key key) {
		this.key = key;

		Optional<String> value = createJwtAuthToken(dto);
		this.token = value.orElse(null);
	}

	@Override
	public boolean validate() {
		var jwtClaims = getData();
		return jwtClaims != null;
	}

	@Override
	public Claims getData() {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	private Optional<String> createJwtAuthToken(TokenPayload dto) {
		var expiredDate = Date.from(LocalDateTime.now()
				.plusMinutes(CommonConstant.Jwt.TOKEN_EXPIRED_MINUTES)
				.atZone(ZoneId.systemDefault()).toInstant());
		var host = "";
		var req = HttpUtil.getHttpServletRequest();
		if (req != null) {
			host = req.getHeader("host");
		}

		// API 메타정보
		String serverInstanceId = null;
		try {
			var apiInfo = ApiInfo.makeResponseApiInfo((ApiInfo) HttpUtil.getRequestAttribute("apiInfo"));
			if (StringUtil.isNotEmpty(apiInfo.getServerInstanceId())) {
				serverInstanceId = apiInfo.getServerInstanceId();
			}
		} catch (Exception e) {
			serverInstanceId = host;
		}

		var claims = Jwts.claims();
		claims.put(JwtPayloadKey.MANAGER_ACCOUNT_SERIAL_NO.getKey(), dto.getManagerAccountSerialNo());
		claims.put(JwtPayloadKey.ACCOUNT_ID.getKey(), dto.getAccountId());
		claims.put(JwtPayloadKey.ACCOUNT_NAME.getKey(),  URLEncoder.encode(dto.getAccountName(), StandardCharsets.UTF_8));
		claims.put(JwtPayloadKey.ERP_BRANCH_CODE.getKey(), dto.getErpBranchCode());
		claims.put(JwtPayloadKey.TA_SERIAL_NO.getKey(), dto.getTaSerialNo());
		claims.put(JwtPayloadKey.ROLES.getKey(), dto.getRoles());
		claims.put(JwtPayloadKey.MANAGER_MENU_GROUP_ID.getKey(), dto.getManagerMenuGroupId());

		return Optional.ofNullable(Jwts.builder()
				.setIssuer(serverInstanceId)
				.setAudience(dto.getAccountId())
				.addClaims(claims)
				.setIssuedAt(new Date())
				.setExpiration(expiredDate)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact());
	}

	/**
	 * JWT claims payload
	 */
	@AllArgsConstructor
	@Getter
	@Builder
	public static class TokenPayload {
		private final Long managerAccountSerialNo;
		private final String accountId;
		private final String accountName;
		private final String erpBranchCode;
		private final Long taSerialNo;
		private final List<String> roles;
		private final String managerMenuGroupId;

		// JWT Claims 를 TokenPayload 객체로 변환
		public static TokenPayload of(Claims claims) {
			var erpBranchCode = claims.get(JwtPayloadKey.ERP_BRANCH_CODE.getKey());
			var taSerialNo = claims.get(JwtPayloadKey.TA_SERIAL_NO.getKey());
			var roles = convertObjectToList(claims.get(JwtPayloadKey.ROLES.getKey()));
			var managerMenuGroupId = claims.get(JwtPayloadKey.MANAGER_MENU_GROUP_ID.getKey());

			return TokenPayload.builder()
					.managerAccountSerialNo(((Number) claims.get(JwtPayloadKey.MANAGER_ACCOUNT_SERIAL_NO.getKey())).longValue())
					.accountId(claims.get(JwtPayloadKey.ACCOUNT_ID.getKey()).toString())
					.accountName(claims.get(JwtPayloadKey.ACCOUNT_NAME.getKey()).toString())
					.erpBranchCode((erpBranchCode != null) ? erpBranchCode.toString() : null)
					.taSerialNo((taSerialNo != null) ? ((Number) taSerialNo).longValue() : null)
					.roles(roles)
					.managerMenuGroupId((managerMenuGroupId != null) ? managerMenuGroupId.toString() : null)
					.build();
		}

		private static List<String> convertObjectToList(Object obj) {
			List<String> list = new ArrayList<>();
			if (obj.getClass().isArray()) {
				list = Arrays.asList((String[])obj);
			}
			return list;
		}
	}
}