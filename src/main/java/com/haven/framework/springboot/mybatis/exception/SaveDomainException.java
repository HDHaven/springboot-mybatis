package com.haven.framework.springboot.mybatis.exception;

public class SaveDomainException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SaveDomainException(String msg) {
		super(msg);
	}
	
	public SaveDomainException(String msg, Class<?> clazz) {
		super("save [ "+ clazz.getName() +" ] exception: "+ msg);
	}
	
}
