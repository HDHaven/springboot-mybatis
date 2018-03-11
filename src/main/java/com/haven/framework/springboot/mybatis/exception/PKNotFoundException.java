package com.haven.framework.springboot.mybatis.exception;

import com.haven.framework.springboot.mybatis.service.annotation.PrimaryKey;

public class PKNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PKNotFoundException(Class<?> clazz) {
		super("There is no annotation [ "+ PrimaryKey.class.getName() +" ] be used in [ "+ clazz.getName() +" ] 's field!");
	}
	
}
