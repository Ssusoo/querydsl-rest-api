package me.ssu.querydslrestapi.infra.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * HTTP Settings-1(Header)
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ClientHeader {
	private final String authorization;
}