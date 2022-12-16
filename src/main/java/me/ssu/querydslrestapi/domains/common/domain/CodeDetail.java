package me.ssu.querydslrestapi.domains.common.domain;

import lombok.*;
import me.ssu.querydslrestapi.base.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "cd_dtl")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class CodeDetail extends BaseEntity {
	@Id
	@Column(name = "comm_cd", length = 8)
	private String commonCode; // 공통_코드

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "comm_mst_cd", insertable = false, updatable = false,
			foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private CodeMaster codeMaster; // 공통_코드_마스터

	@Column(name = "comm_cd_nm", length = 100)
	private String commonCodeName; // 공통_코드_이름

	@Column(name = "use_yn", length = 1)
	private String useYn; // 사용_여부

	@Column(name = "cd_sort_no")
	private Integer codeSortNo; // 코드_정렬번호

	@Column(name = "remark", length = 2000)
	private String remark; // 비고
}