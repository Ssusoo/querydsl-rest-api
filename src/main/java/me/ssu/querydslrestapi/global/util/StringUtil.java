package me.ssu.querydslrestapi.global.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	/**
	 * Instantiates a new string util.
	 */
	private StringUtil() {
	}

	/**
	 * Checks if is empty.
	 *
	 * @param value the value
	 * @return true, if is empty
	 */
	public static boolean isEmpty(String value) {
		return (value == null || value.length() == 0);
	}

	/**
	 * Checks if is not empty.
	 *
	 * @param value the value
	 * @return true, if is not empty
	 */
	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}

	/**
	 * HTML 태그 제거
	 * @param value
	 * @return
	 */
	public static String removeHTMLTags(String value) {
		String result = "";

		// [문자열 데이터 널 판단 수행 실시]
		if (value != null
				&& value.length() > 0
				&& !value.trim().equals("")
				&& !value.trim().equals("null")
				&& !value.trim().equals("undefined")) {

			try {
				// [html 태그를 제거 하기 위한 패턴 정의 실시]
				String tagPattern = "<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>"; // [<p> 등 태그 제거]
				Pattern numRegEntityPattern = Pattern.compile("&#[0-9]+;"); // [&#09; 형태 제거]
				Pattern charRegEntityPattern = Pattern.compile("&[a-zA-Z]+;"); // [&amp; 형태 제거]
				Pattern charNormalEntityPattern = Pattern.compile(" [a-zA-Z]+;"); // [amp; 형태 제거]

				// [html 태그 1차 제거 실시]
				result = value.replaceAll(tagPattern, " ");

				// [html 태그 2차 제거 실시]
				Matcher numRegMat = numRegEntityPattern.matcher(result);
				result = numRegMat.replaceAll("");

				Matcher charRegMat = charRegEntityPattern.matcher(result);
				result = charRegMat.replaceAll("");

				Matcher charNormalMat = charNormalEntityPattern.matcher(result);
				result = charNormalMat.replaceAll("");

				// [html 태그 연속 공백 제거 실시]
				result = result.replaceAll("\\s+", " ");

				// [문자열 양쪽 끝 공백 제거 실시]
				result = result.trim();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}