package me.ssu.querydslrestapi.global.config.jpa;

import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StringType;

/**
 * 기본 Mysql Dialect 에 추가할 함수가 있다면 이곳에 정의한다.
 */
public class MySQLDialect extends MySQL8Dialect {
	public MySQLDialect() {
		super();
		this.registerFunction("group_concat",
				new StandardSQLFunction("group_concat", new StringType()));
		this.registerFunction("TIMESTAMPDIFF",
				new StandardSQLFunction("TIMESTAMPDIFF", new StringType()));
		this.registerFunction("DATE_FORMAT",
				new StandardSQLFunction("DATE_FORMAT", new StringType()));
	}
}