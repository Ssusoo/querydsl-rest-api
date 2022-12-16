package me.ssu.querydslrestapi.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ssu.querydslrestapi.global.constant.ClientHeaderKey;
import me.ssu.querydslrestapi.global.error.exception.JwtTokenMissingException;
import me.ssu.querydslrestapi.infra.dto.ClientHeader;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * HTTP Settings-3(HttpUtil 클래스 만들기)
 *  HttpUtil 클래스 정의
 *      HTTP 요청을 보내고 URI로 식별된 리소스에서 HTTP 응답을 수신하는 클래스
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class HttpUtil {

	/**
	 * Gets the http servlet request.
	 *
	 * @return the http servlet request
	 */
	public static HttpServletRequest getHttpServletRequest() {
		var servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (servletRequestAttributes != null) {
			return servletRequestAttributes.getRequest();
		} else {
			return null;
		}
	}

	public static HttpServletResponse getHttpServletResponse() {
		var servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (servletRequestAttributes != null) {
			return servletRequestAttributes.getResponse();
		} else {
			return null;
		}
	}

	public static void setRequestAttribute(String name, Object value) {
		var request = getHttpServletRequest();
		assert request != null;

		setRequestAttribute(request, name, value);
	}

	public static void setRequestAttribute(HttpServletRequest request, String name, Object value) {
		request.setAttribute(name, value);
	}

	public static Object getRequestAttribute(String name) {
		var request = getHttpServletRequest();
		assert request != null;

		return getRequestAttribute(request, name);
	}

	public static Object getRequestAttribute(HttpServletRequest request, String name) {
		return request.getAttribute(name);
	}

	public static ClientHeader getClientHeader() {
		var request = getHttpServletRequest();
		assert request != null;

		return getClientHeader(request);
	}

	public static ClientHeader getClientHeader(HttpServletRequest request) {
		return ClientHeader.builder()
				.authorization(request.getHeader(ClientHeaderKey.AUTHORIZATION.getKey()))
				.build();
	}

	public static String getHeaderToken() {
		return getHeaderToken(getHttpServletRequest());
	}

	public static String getHeaderToken(HttpServletRequest request) {
		String jwtToken = HttpUtil.getClientHeader(request).getAuthorization();

		if (StringUtils.hasText(jwtToken)) {
			if (!jwtToken.startsWith("Bearer ")) {
				throw new JwtTokenMissingException();
			}

			return jwtToken.substring(7);
		} else {
			return null;
		}
	}

	public static Map<String, Object> getRequestParams(HttpServletRequest request) {
		Map<String, Object> requestParams = new HashMap<>();
		while (request.getParameterNames().hasMoreElements()) {
			var parameterName = request.getParameterNames().nextElement();
			requestParams.put(parameterName, request.getParameter(parameterName));
		}

		if (requestParams.isEmpty()) {
			return null;
		}

		return requestParams;
	}

	public static String getRequestBody(HttpServletRequest request) {
		if (!("application/x-www-form-urlencoded".equalsIgnoreCase(request.getContentType()))
				&& !request.getMethod().equalsIgnoreCase("GET")) {
			ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
			if (wrapper != null) {
				byte[] buf = wrapper.getContentAsByteArray();
				if (buf.length > 0) {
					try {
						return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
					} catch (UnsupportedEncodingException e) {
						return null;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets the ip addr.
	 */
	public static String getClientIpAddress() {
		var request = getHttpServletRequest();
		if (request == null) {
			return "0.0.0.0";
		}
		return getClientIpAddress(request);
	}
	public static String getClientIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		var unknown = "unknown";
		if (StringUtil.isNotEmpty(ip) && !unknown.equalsIgnoreCase(ip)) {
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("Proxy-Client-IP");
		if (StringUtil.isNotEmpty(ip) && !unknown.equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("WL-Proxy-Client-IP");
		if (StringUtil.isNotEmpty(ip) && !unknown.equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("HTTP_CLIENT_IP");
		if (StringUtil.isNotEmpty(ip) && !unknown.equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (StringUtil.isNotEmpty(ip) && !unknown.equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}




	/**
	 * Gets the local ip addr.
	 *
	 * @return the local ip addr
	 */
	public static String getLocalIpAddress() {
		String ip = null;
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();

			while (en.hasMoreElements()) {
				NetworkInterface ni = en.nextElement();

				var isContinue = false;

				Enumeration<InetAddress> inetAddresses = null;

				if (ni == null || !ni.isUp() || ni.isLoopback() || ni.isVirtual()) {
					isContinue = true;
				} else {
					inetAddresses = ni.getInetAddresses();
				}

				while (!isContinue && inetAddresses.hasMoreElements()) {
					var ia = inetAddresses.nextElement();

					isContinue = isLoopbackOrLinkLocalAddress(ia);

					if ((!isContinue && ia.isSiteLocalAddress())
							|| (!isContinue && ia.getHostAddress() != null && ia.getHostAddress().indexOf('.') != -1)) {
						return ia.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			log.error("error : {}", e.getMessage());
		}

		return ip;
	}

	/**
	 * Loopback 또는 LinkLocal Address 여부 확인
	 *
	 * @param ia
	 * @return
	 */
	private static boolean isLoopbackOrLinkLocalAddress(InetAddress ia) {
		return ia.isLoopbackAddress() || ia.isLinkLocalAddress();
	}

	/**
	 * Gets the header to map.
	 *
	 * @param request the request
	 * @return the header to map
	 */
	public static Map<String, Object> getHeaderToMap(HttpServletRequest request) {
		Map<String, Object> headerMap = null;
		if (request != null) {
			headerMap = new HashMap<>();
			Enumeration<String> headerNames = request.getHeaderNames();
			String key = null;
			String value = null;

			while (headerNames.hasMoreElements()) {
				key = headerNames.nextElement();
				value = request.getHeader(key);
				if (StringUtil.isNotEmpty(value)) {
					headerMap.put(key.toLowerCase(), value);
				}
			}
		}
		return headerMap;
	}

	/**
	 * Gets the header to map.
	 *
	 * @param response the response
	 * @return the header to map
	 */
	public static Map<String, Object> getHeaderToMap(HttpServletResponse response) {
		Map<String, Object> headerMap = null;
		if (response != null) {
			headerMap = new HashMap<>();
			Iterator<String> iter = response.getHeaderNames().iterator();
			String key = null;
			String value = null;

			while (iter.hasNext()) {
				key = iter.next();
				value = response.getHeader(key);
				if (StringUtil.isNotEmpty(value)) {
					headerMap.put(key.toLowerCase(), value);
				}
			}
			String contentType = response.getContentType();
			if (StringUtil.isNotEmpty(contentType)) {
				headerMap.put("Content-type", contentType);
			}
		}
		return headerMap;
	}

	/**
	 * Gets the referer URL.
	 *
	 * @param request the request
	 * @return the referer URL
	 */
	public static String getRefererURL(HttpServletRequest request) {
		return request.getHeader("referer");
	}
}