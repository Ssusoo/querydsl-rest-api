package me.ssu.querydslrestapi.domains.common.application;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ssu.querydslrestapi.domains.common.dto.CommonCodes;
import me.ssu.querydslrestapi.domains.common.repository.CodeMasterRepository;
import me.ssu.querydslrestapi.global.constant.CommonCodeMaster;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommonCodesService {
	private final CodeMasterRepository codeMasterRepository;

	private static final List<CommonCodes.CommonCode> commonCodes = new ArrayList<>();

	public void fetch() {
		synchronized (commonCodes) {
			commonCodes.clear();
			commonCodes.addAll(codeMasterRepository.findAll());
		}
	}

	/**
	 * 서버 초기 구동 시 전체 코드를 가져온다.
	 */
//	@PostConstruct
	public void fetchCodeList() {
		fetch();
	}

	/**
	 * 코드 목록 가져오기
	 */
	public static CommonCodes.Result getCommonCodes() {
		return new CommonCodes.Result(commonCodes);
	}

	public static CommonCodes.Result getCommonCodes(CommonCodes.Request request) {
		var codes = commonCodes.stream()
				.filter((v) -> Arrays.asList(request.getCommonMasterCode()).contains(v.getCommonMasterCode()))
				.collect(Collectors.toList());

		return new CommonCodes.Result(codes);
	}

	public static List<CommonCodes.CommonCode> getCommonCodes(CommonCodeMaster[] commonCodeMasters) {
		return getCommonCodes(new CommonCodes.Request(Arrays.stream(commonCodeMasters)
				.map(CommonCodeMaster::getCode).toArray(String[]::new)))
				.getCodes();
	}

	/**
	 * 지정된 코드목록 - 코드 이름 반환
	 * @param codes
	 * @param commonCodeMaster
	 * @param commonCode
	 * @return
	 */
	public static String getCodeName(List<CommonCodes.CommonCode> codes,
	                                 CommonCodeMaster commonCodeMaster, String commonCode) {
		for (CommonCodes.CommonCode code : codes) {
			if (code.getCommonMasterCode().equals(commonCodeMaster.getCode()) &&
					code.getCommonCode().equals(commonCode)) {
				return code.getCommonCodeName();
			}
		}
		return null;
	}

	public static String getCodeName(List<CommonCodes.CommonCode> codes,
	                                 CommonCodeMaster[] commonCodeMasters, String commonCode) {
		String codeName;
		for (CommonCodeMaster commonCodeMaster : commonCodeMasters) {
			codeName = getCodeName(codes, commonCodeMaster, commonCode);
			if (!StringUtils.isEmpty(codeName)) {
				return codeName;
			}
		}
		return null;
	}

	public static String getCodeName(CommonCodeMaster[] commonCodeMasters, String commonCode) {
		String codeName;
		for (CommonCodeMaster commonCodeMaster : commonCodeMasters) {
			codeName = getCodeName(getCommonCodes(commonCodeMasters), commonCodeMaster, commonCode);
			if (!StringUtils.isEmpty(codeName)) {
				return codeName;
			}
		}
		return null;
	}

	/**
	 * 코드를 코드명으로 매핑해준다.
	 *  1. CommonCodeMaster 에 정의 된 "codeField" 값을 기준으로 "codeField" 값에 "Name" 을 추가 한 변수에 자동으로 매핑된다.
	 *  2. 예를들어, erpSubjectDomainCode 의 코드명은 erpSubjectDomainCodeName 에 매핑된다.
	 *  3. 따라서 코드명 멤버 변수는 반드시 "codeField" 값에 "Name" 을 붙인 형태로 작성해야 한다.
	 * @param dto : 코드명을 매핑할 DTO 객체
	 * @param commonCodeMasters : 매핑 대상 코드 목록
	 * @param <T>
	 */
	public static <T> void commonCodeToCodeName(T dto, CommonCodeMaster[] commonCodeMasters) {
		// 매핑할 코드 대상을 가져온다.
		var commonCodes = getCommonCodes(commonCodeMasters);

		// SpEl 을 이용하여 DTO 에 매핑한다.
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext resultContext = new StandardEvaluationContext(dto);
		for (CommonCodeMaster commonCodeMaster : commonCodeMasters) {
			try {
				var commonCode = parser.parseExpression(commonCodeMaster.getCodeField()).getValue(resultContext, String.class);
				if (!StringUtils.isEmpty(commonCode)) {
					var codeName = getCodeName(commonCodes, commonCodeMaster, commonCode);
					if (!StringUtils.isEmpty(codeName)) {
						parser.parseExpression(commonCodeMaster.getCodeField().concat("Name")).setValue(resultContext, codeName);
					}
				}
			} catch (Exception e) {
				log.error("CommonCodesService.commonCodeToCodeName", e);
			}
		}
	}
}
