package me.ssu.querydslrestapi.domains.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ssu.querydslrestapi.base.controller.BaseController;
import me.ssu.querydslrestapi.domains.common.application.CommonCodesService;
import me.ssu.querydslrestapi.domains.common.dto.CommonCodes;
import me.ssu.querydslrestapi.global.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "공통-코드")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/common/codes")
@Slf4j
public class CommonCodesController extends BaseController {
	private final CommonCodesService commonCodesService;

	@Operation(summary = "공통 코드 목록")
	@GetMapping
	public ApiResponse<CommonCodes.Result> getCodes(@Valid CommonCodes.Request request) {
		return responseJson(CommonCodesService.getCommonCodes(request));
	}

	@Operation(summary = "공통 코드 목록 - 전체")
	@GetMapping("/all")
	public ApiResponse<CommonCodes.Result> getAllCodes() {
		return responseJson(CommonCodesService.getCommonCodes());
	}

	@Operation(summary = "공통 코드 목록 갱신")
	@PostMapping("/reload")
	public ApiResponse<Object> postReload() {
		commonCodesService.fetchCodeList();
		return responseJson(null);
	}
}