package com.haven.framework.springboot.mybatis.exception;

public class QueryException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public QueryException(String msg) {
		super(msg);
	}
	
}
