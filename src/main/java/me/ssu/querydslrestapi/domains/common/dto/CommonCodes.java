package me.ssu.querydslrestapi.domains.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Schema(hidden = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonCodes {

	@Getter
	@Setter
	@AllArgsConstructor(access = AccessLevel.PUBLIC)
	public static class Request {
		@Schema(description = "공통 마스터 코드")
		@NotEmpty
		private String[] commonMasterCode;
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PUBLIC)
	public static class Result {
		@Schema(description = "공통 코드 목록")
		private final List<CommonCode> codes;
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PUBLIC)
	@Builder
	public static class CommonCode {
		@Schema(description = "공통 마스터 코드")
		private final String commonMasterCode;

		@Schema(description = "공통 코드")
		private final String commonCode;

		@Schema(description = "공통 코드명")
		private final String commonCodeName;

		@Schema(description = "비고")
		private final String remark;

		@Schema(description = "사용 여부")
		private final String useYn;
	}
}