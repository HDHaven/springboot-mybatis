package com.haven.framework.springboot.mybatis.service;

import java.util.List;

public interface SuperService<T> {

	T saveOne(T obj);
	
	Integer saveBatch(List<T> list);
	
	Boolean deleteOne(Class<T> clazz, Long id);
	
	Boolean deleteBatch(Class<T> clazz, Long... ids);
	
	Boolean update(Long id, T obj);
	
	T findById(Class<T> clazz, Long id);
	
	Page<T> findByPage(Class<T> clazz, WhereCondition condition, Long pageSize);

	Page<T> findByPage(Class<T> clazz, Page<T> page);
	
	List<T> findByList(Class<T> clazz, WhereCondition condition);
	
	List<T> findByList(Class<T> clazz, String fieldName, Boolean isIn, Object[] value);
	
}
