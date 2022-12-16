package me.ssu.querydslrestapi.global.config.jwt;

public interface AuthToken<T> {

	boolean validate();
	T getData();
}