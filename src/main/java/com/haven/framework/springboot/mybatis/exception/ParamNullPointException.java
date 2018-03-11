package com.haven.framework.springboot.mybatis.exception;

public class ParamNullPointException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ParamNullPointException(Class<?> clazz, String method) {
		super("The parameter of method [ "+ clazz.getName() +"#"+ method +" ] is not allowed null!" );
	}
	
}
