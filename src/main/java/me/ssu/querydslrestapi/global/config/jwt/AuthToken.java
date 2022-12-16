package me.ssu.querydslrestapi.global.config.jwt;

/**
 * Jwt Token-1
 * @param <T>
 */
public interface AuthToken<T> {

	boolean validate();
	T getData();
}