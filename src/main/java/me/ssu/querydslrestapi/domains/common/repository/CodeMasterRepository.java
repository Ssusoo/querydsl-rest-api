package me.ssu.querydslrestapi.domains.common.repository;

import me.ssu.querydslrestapi.base.repository.BaseRepository;
import me.ssu.querydslrestapi.domains.common.domain.CodeMaster;
import me.ssu.querydslrestapi.domains.common.domain.QCodeDetail;
import me.ssu.querydslrestapi.domains.common.domain.QCodeMaster;
import me.ssu.querydslrestapi.domains.common.dto.CommonCodes;
import me.ssu.querydslrestapi.global.constant.CommonConstant;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CodeMasterRepository extends BaseRepository<CodeMaster, String> {
	private static final QCodeMaster codeMaster = QCodeMaster.codeMaster;
	private static final QCodeDetail codeDetail = QCodeDetail.codeDetail;

	public List<CommonCodes.CommonCode> findAll() {
		return select(
				CommonCodes.CommonCode.class,
				codeMaster.commonMasterCode,
				codeDetail.commonCode,
				codeDetail.commonCodeName,
				codeDetail.remark,
				codeDetail.useYn)
				.from(codeMaster)
				.innerJoin(codeDetail)
				.on(codeMaster.commonMasterCode.eq(codeDetail.codeMaster.commonMasterCode))
				.where(codeMaster.useYn.eq(CommonConstant.Yn.Y)
						.and(codeDetail.useYn.eq(CommonConstant.Yn.Y)))
				.orderBy(codeMaster.commonMasterCode.asc(),
						codeMaster.codeSortNo.asc(),
						codeDetail.codeSortNo.asc())
				.fetch();
	}
}