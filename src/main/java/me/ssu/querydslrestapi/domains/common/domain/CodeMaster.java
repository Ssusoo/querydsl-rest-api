package me.ssu.querydslrestapi.domains.common.domain;

import lombok.*;
import me.ssu.querydslrestapi.base.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cd_mst")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class CodeMaster extends BaseEntity {
	@Id
	@Column(name = "comm_mst_cd", length = 4)
	private String commonMasterCode; // 공통_코드_마스터

	@Column(name = "comm_mst_cd_nm", length = 100)
	private String commonMasterCodeName; // 공통_마스터코드_이름

	@Column(name = "use_yn", length = 1)
	private String useYn; // 사용_여부

	@Column(name = "cd_sort_no")
	private Integer codeSortNo; // 코드_정렬번호

	@Column(name = "remark", length = 2000)
	private String remark; // 비고
}