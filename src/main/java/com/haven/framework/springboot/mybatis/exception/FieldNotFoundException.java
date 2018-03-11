package com.haven.framework.springboot.mybatis.exception;

public class FieldNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FieldNotFoundException(Class<?> clazz) {
		super("No enable field found in domain [ "+ clazz.getName() +" ] ");
	}
	
	public FieldNotFoundException(Class<?> clazz, String fieldName) {
		super("no field named '"+ fieldName +"' in domain [ "+ clazz.getName() +" ]");
	}
	
}
