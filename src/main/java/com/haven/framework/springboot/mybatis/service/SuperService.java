package com.haven.framework.springboot.mybatis.service;

import java.util.List;

public interface SuperService<T> {

	/**
	 * 保存一条记录
	 * 
	 * @param obj 需要保存的对象
	 * @return
	 */
	T saveOne(T obj);
	
	/**
	 * 批量保存记录
	 * 
	 * @param list 需要保存的对象列表
	 * @return
	 */
	Integer saveBatch(List<T> list);
	
	/**
	 * 删除一条记录
	 * 
	 * @param clazz 删除对象的类型
	 * @param id 删除对象的id值
	 * @return
	 */
	Boolean deleteOne(Class<T> clazz, Long id);
	
	/**
	 * 批量删除记录
	 * 
	 * @param clazz 删除对象的类型
	 * @param ids 删除对象的id值列表
	 * @return
	 */
	Boolean deleteBatch(Class<T> clazz, Long... ids);
	
	/**
	 * 更新一条记录
	 * 
	 * @param id 更新对象的id值
	 * @param obj 更新的对象
	 * @return
	 */
	Boolean update(Long id, T obj);
	
	/**
	 * 查询一条记录
	 * 
	 * @param clazz 查询的对象类型
	 * @param id 查询id值
	 * @return
	 */
	T findById(Class<T> clazz, Long id);
	
	/**
	 * 分页查询，第一次查询
	 * 
	 * @param clazz 查询对象类型
	 * @param condition 查询条件以及排序条件
	 * @param pageSize 查询条数，默认10条
	 * @return
	 * @see #findByPage(Class, Page)
	 */
	Page<T> findByPage(Class<T> clazz, WhereCondition condition, Long pageSize);

	/**
	 * 分页查询，在第一次查询后保存了分页信息，就不需要在查询总条数了
	 * 
	 * @param clazz 查询对象类型
	 * @param page 第一次查询后保存的分页信息
	 * @return
	 */
	Page<T> findByPage(Class<T> clazz, Page<T> page);
	
	/**
	 * 列表查询，不做分页处理
	 * 
	 * @param clazz 需要查询的对象
	 * @param condition 查询条件
	 * @return
	 */
	List<T> findByList(Class<T> clazz, WhereCondition condition);
	
	/**
	 * [in]或者[not in]查询，即范围查询
	 * 
	 * @param clazz 查询对象类型
	 * @param fieldName 条件字段名
	 * @param isIn true表示in，false表示not in，默认为true，即in
	 * @param value 范围列表
	 * @return
	 */
	List<T> findByList(Class<T> clazz, String fieldName, Boolean isIn, Object[] value);
	
}
