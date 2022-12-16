package me.ssu.querydslrestapi.global.constant;

import lombok.Getter;

@Getter
public enum JwtPayloadKey {
	MANAGER_ACCOUNT_SERIAL_NO("managerAccountSerialNo"),
	ACCOUNT_ID("accountId"),
	ACCOUNT_NAME("accountName"),
	ERP_BRANCH_CODE("erpBranchCode"),
	TA_SERIAL_NO("taSerialNo"),
	ROLES("roles"),
	MANAGER_MENU_GROUP_ID("managerMenuGroupId");

	private final String key;

	JwtPayloadKey(final String key) {
		this.key = key;
	}
}