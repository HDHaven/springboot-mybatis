package com.haven.framework.springboot.mybatis.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haven.framework.springboot.mybatis.exception.FieldNotFoundException;
import com.haven.framework.springboot.mybatis.exception.PKNotFoundException;
import com.haven.framework.springboot.mybatis.exception.ParamNullPointException;
import com.haven.framework.springboot.mybatis.exception.QueryException;
import com.haven.framework.springboot.mybatis.exception.SaveDomainException;
import com.haven.framework.springboot.mybatis.exception.UpdateDomainException;
import com.haven.framework.springboot.mybatis.repository.SuperRepository;
import com.haven.framework.springboot.mybatis.service.Page;
import com.haven.framework.springboot.mybatis.service.SuperService;
import com.haven.framework.springboot.mybatis.service.WhereCondition;
import com.haven.framework.springboot.mybatis.service.annotation.Column;
import com.haven.framework.springboot.mybatis.service.annotation.Ignore;
import com.haven.framework.springboot.mybatis.service.annotation.PrimaryKey;
import com.haven.framework.springboot.mybatis.service.annotation.QueryColumns;
import com.haven.framework.springboot.mybatis.service.annotation.Table;

@Service
public class DefaultSuperService<T> implements SuperService<T> {
	private static final Logger log = LoggerFactory.getLogger(DefaultSuperService.class);
	
	@Autowired
	private SuperRepository superRepository;
	
	// 获取表名
	private String getTableName(Class<?> clazz) {
		// 默认类名
		String tableName = clazz.getName().substring(clazz.getName().lastIndexOf('.')+1);
		Table t = clazz.getAnnotation(Table.class);
		if(t != null) {
			// 使用注入的表名
			tableName = t.value();
		}
		return tableName;
	}
	
	// 获取主键
	private String getPK(Class<?> clazz) {
		String pk = null;
		Field[] fields = clazz.getDeclaredFields();
		boolean flag = false;
		for (Field field : fields) {
			if(field.isAnnotationPresent(PrimaryKey.class)) {
				pk = field.getAnnotation(PrimaryKey.class).value();
				flag = true;
				break;
			}
		}
		// 主键不存在
		if(!flag) throw new PKNotFoundException(clazz);
		return pk;
	}
	
	// 获取传入的对象
	private T getInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			log.error("获取对象实例失败："+ e.getMessage(), e);
			throw new QueryException(e.getMessage());
		}
	}
	
	// 将查询结果map转换为泛型T对象
	private T setValue(Field[] fields, Class<T> clazz, Map<String, Object> resultMap) {
		if(resultMap == null || resultMap.isEmpty()) return null;
		T obj = this.getInstance(clazz);
		if(fields == null)
			fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if(field.getName().equals("serialVersionUID")) continue;
			if(field.isAnnotationPresent(Ignore.class)) continue;
			String columnName = "";
			if(field.isAnnotationPresent(PrimaryKey.class)) {
				columnName = field.getAnnotation(PrimaryKey.class).value();
			} else if(field.isAnnotationPresent(Column.class)) {
				columnName = field.getAnnotation(Column.class).value();
			} else {
				columnName = field.getName();
			}
			field.setAccessible(true);
			Object value = resultMap.get(columnName);
			try {
				field.set(obj, value);
			} catch (Exception e) {
				log.error("格式转换失败："+ e.getMessage(), e);
				throw new QueryException("格式转换失败："+ e.getMessage());
			}
		}
		return obj;
	}
	
	// 获取查询的行
	private String[] getQueryColumns(Class<T> clazz) {
		QueryColumns queryColumns = clazz.getAnnotation(QueryColumns.class);
		String[] selectColumns = null;
		if(queryColumns != null) {
			selectColumns = queryColumns.value().split(",");
		} else {
			Field[] fields = clazz.getDeclaredFields();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < fields.length; i++) {
				if(fields[i].isAnnotationPresent(Ignore.class)) continue;
				if(fields[i].getName().equals("serialVersionUID")) continue;
				Field field = fields[i];
				String columnName = "";
				if(field.isAnnotationPresent(PrimaryKey.class)) {
					columnName = field.getAnnotation(PrimaryKey.class).value();
				} else {
					columnName = field.isAnnotationPresent(Column.class) ? field.getAnnotation(Column.class).value() : field.getName();
				}
				sb.append(columnName +",");
			}
			selectColumns = sb.toString().split(",");
		}
		return selectColumns;
	}
	
	// 获取查询条件
	private Map<String, Object> getSearchMap(Class<T> clazz, Map<String, Object> rawSearchMap) {
		if(rawSearchMap == null || rawSearchMap.isEmpty()) return null;
		Map<String, Object> searchMap = new HashMap<String, Object>();
		for(String key : rawSearchMap.keySet()) {
			Object value = rawSearchMap.get(key);
			if(key.contains("#")) key = key.replace("#", "");
			String[] tempArr = key.split(",");// temp[0]=fieldName, temp[1]=operation(>,<,etc)
			try {
				Field field = clazz.getDeclaredField(tempArr[0]);
				String columnName = null;
				if(field.isAnnotationPresent(Ignore.class)) continue;
				if(field.isAnnotationPresent(PrimaryKey.class)) {
					columnName = field.getAnnotation(PrimaryKey.class).value();
				} else {
					Column column = field.getAnnotation(Column.class);
					columnName = column==null ? tempArr[0] : column.value();
				}
				searchMap.put(columnName+tempArr[1], value);
			} catch (Exception e) {
				log.error("查询条件有误："+ e.getMessage(), e);
				throw new FieldNotFoundException(clazz, tempArr[0]);
			}
		}
		return searchMap;
	}
	
	// 获取排序条件
	private String[] getOrderBy(Class<T> clazz, List<String> rawOrderBy) {
		if(rawOrderBy == null) return null;
		String[] orderBy = new String[rawOrderBy.size()];
		for(int i = 0; i < rawOrderBy.size(); i++) {
			String[] tempArr = rawOrderBy.get(i).split(",");// tempArr[0]=fieldName, tempArr[1]=asc|desc
			try {
				Field field = clazz.getDeclaredField(tempArr[0]);
				if(field.isAnnotationPresent(Ignore.class)) continue;
				String columnName = "";
				if(field.isAnnotationPresent(PrimaryKey.class)) {
					columnName = field.getAnnotation(PrimaryKey.class).value();
				} else {
					Column column = field.getAnnotation(Column.class);
					columnName = column==null ? tempArr[0] : column.value();
				}
				orderBy[i] = columnName +" "+ tempArr[1];
			} catch (Exception e) {
				log.error("排序条件有误："+ e.getMessage(), e);
				throw new FieldNotFoundException(clazz, tempArr[0]);
			}
		}
		return orderBy;
	}
	
	// 将查询结果List<map>转换为List<T>
	private List<T> getResultList(Class<T> clazz, List<Map<String, Object>> list) {
		if(list == null || list.isEmpty()) return null;
		List<T> resultList = new ArrayList<T>();
		for(int i = 0; i < list.size(); i++) {
			T obj = this.setValue(null, clazz, list.get(i));
			resultList.add(obj);
		}
		return resultList;
	}
	
	@Transactional 
	@Override
	public T saveOne(T obj) {
		if(obj == null) throw new ParamNullPointException(DefaultSuperService.class, "saveOne(T obj)");
		// 1.获取表名
		Class<?> clazz = obj.getClass();
		String tableName = this.getTableName(clazz);
		// 2.获取列名及对应的值
		Field[] fields = clazz.getDeclaredFields();
		if(fields == null || fields.length == 0) throw new FieldNotFoundException(clazz);
		List<String> columnNames = new ArrayList<String>();
		List<Object> columnValues = new ArrayList<Object>();
		for (Field field : fields) {
			if(field.getName().equals("serialVersionUID")) continue;
			if(field.isAnnotationPresent(PrimaryKey.class)) continue;
			if(field.isAnnotationPresent(Ignore.class)) continue;
			Column column = field.getAnnotation(Column.class);
			String columnName = column==null ? field.getName() : column.value();
			columnNames.add(columnName);
			field.setAccessible(true);
			try {
				Object value = field.get(obj);
				columnValues.add(value);
			} catch (Exception e) {
				log.error("保存对象失败："+ e.getMessage(), e);
				throw new SaveDomainException(e.getMessage(), clazz);
			}
		}
		if(columnNames.size() == 0) throw new FieldNotFoundException(clazz);
		// 执行插入操作
		Integer result = superRepository.insertOne(tableName, columnNames, columnValues);
		return 1==result ? obj : null;
	}

	@Transactional
	@Override
	public Integer saveBatch(List<T> list) {
		if(list == null || list.isEmpty()) throw new ParamNullPointException(DefaultSuperService.class, "saveBatch(List<T> list)");
		// 1.获取表名
		Class<?> clazz = list.get(0).getClass();
		String tableName = this.getTableName(clazz);
		// 2.获取列名
		List<String> columnNames = new ArrayList<String>();
		Field[] fields = clazz.getDeclaredFields();
		if(fields == null || fields.length == 0) throw new FieldNotFoundException(clazz);
		for (Field field : fields) {
			if(field.getName().equals("serialVersionUID")) continue;
			if(field.isAnnotationPresent(PrimaryKey.class)) continue;
			if(field.isAnnotationPresent(Ignore.class)) continue;
			Column column = field.getAnnotation(Column.class);
			String columnName = column==null ? field.getName() : column.value();
			columnNames.add(columnName);
		}
		if(columnNames.size() == 0) throw new FieldNotFoundException(clazz);
		// 3.获取插入数据
		List<List<Object>> batchColumnValues = new ArrayList<List<Object>>();
		for(T obj1 : list) {
			List<Object> temp = new ArrayList<Object>();
			for(Field field : fields) {
				if(field.getName().equals("serialVersionUID")) continue;
				if(field.isAnnotationPresent(Ignore.class)) continue;
				if(field.isAnnotationPresent(PrimaryKey.class)) continue;
				field.setAccessible(true);
				try {
					Object fieldValue = field.get(obj1);
					temp.add(fieldValue);
				} catch (Exception e) {
					log.error("批量插入数据失败："+ e.getMessage(), e);
					throw new SaveDomainException(e.getMessage(), clazz);
				}
			}
			batchColumnValues.add(temp);
		}
		// 4.执行插入操作
		Integer result = superRepository.insertBatch(tableName, columnNames, batchColumnValues);
		return list.size()==result ? result : null;
	}

	@Transactional
	@Override
	public Boolean deleteOne(Class<T> clazz, Long id) {
		if(clazz == null || id == null) throw new ParamNullPointException(DefaultSuperService.class, "deleteOne(Class<T>, Long)");
		// 获取表名及主键名
		String tableName = this.getTableName(clazz);
		String pk = this.getPK(clazz);
		// 执行删除操作
		Integer result = superRepository.deleteOne(tableName, pk, id);
		return result==1 ? Boolean.TRUE : Boolean.FALSE;
	}

	@Transactional
	@Override
	public Boolean deleteBatch(Class<T> clazz, Long... ids) {
		if(clazz == null || ids == null) throw new ParamNullPointException(DefaultSuperService.class, "deleteBatch(Class<T>, Long...)");
		// 批量删除
		String tableName = this.getTableName(clazz);
		String pk = this.getPK(clazz);
		Integer result = superRepository.deleteBatch(tableName, pk, ids);
		return result==ids.length ? Boolean.TRUE : Boolean.FALSE;
	}

	@Transactional
	@Override
	public Boolean update(Long id, T obj) {
		if(id == null || obj == null) throw new ParamNullPointException(DefaultSuperService.class, "update(Long, T)");
		// 1.获取表名及主键
		Class<?> clazz = obj.getClass();
		String tableName = this.getTableName(clazz);
		String pk = this.getPK(clazz);
		// 将对象转换成Map<columnName, columnValue>的形式
		Map<String, Object> updateColumns = new HashMap<String, Object>();
		Field[] fields = clazz.getDeclaredFields();
		if(fields == null || fields.length == 0) throw new FieldNotFoundException(clazz);
		for (Field field : fields) {
			if(field.getName().equals("serialVersionUID")) continue;
			if(field.isAnnotationPresent(Ignore.class)) continue;
			if(field.isAnnotationPresent(PrimaryKey.class)) continue;
			Column column = field.getAnnotation(Column.class);
			String columnName = column==null ? field.getName() : column.value();
			try {
				field.setAccessible(true);
				Object columnValue = field.get(obj);
				if(columnValue != null) {
					updateColumns.put(columnName, columnValue);
				}
			} catch (Exception e) {
				log.error("更新数据失败："+ e.getMessage(), e);
				throw new UpdateDomainException(clazz, e.getMessage());
			}
		}
		if(updateColumns.isEmpty() || updateColumns.size() == 0) throw new FieldNotFoundException(clazz);
		Integer result = superRepository.update(tableName, pk, id, updateColumns);
		return result==1 ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	public T findById(Class<T> clazz, Long id) {
		if(clazz == null || id == null) throw new ParamNullPointException(DefaultSuperService.class, "findById(Class<T>, Long)");
		// 1.获取表名和主键名
		String tableName = this.getTableName(clazz);
		String pk = this.getPK(clazz);
		// 2.获取列名
		List<String> columnNames = new ArrayList<String>();
		Field[] fields = clazz.getDeclaredFields();
		if(fields == null || fields.length == 0) throw new FieldNotFoundException(clazz);
		for (Field field : fields) {
			if(field.isAnnotationPresent(Ignore.class)) continue;
			if(field.getName().equals("serialVersionUID")) continue;
			String columnName = "";
			if(field.isAnnotationPresent(PrimaryKey.class)) {
				columnName = field.getAnnotation(PrimaryKey.class).value();
			} else {
				Column column = field.getAnnotation(Column.class);
				columnName = column==null ? field.getName() : column.value();
			}
			columnNames.add(columnName);
		}
		if(columnNames.isEmpty() || columnNames.size() == 0) throw new FieldNotFoundException(clazz);
		// 3.执行查询
		Map<String, Object> resultMap = superRepository.selectOne(tableName, pk, id, columnNames);
		return this.setValue(fields, clazz, resultMap);
	}

	@Override
	public List<T> findByList(Class<T> clazz, WhereCondition condition) {
		if(clazz == null) throw new ParamNullPointException(DefaultSuperService.class, "findByList(Class<T>, WhereCondition)");
		// 1.获取表名
		String tableName = this.getTableName(clazz);
		// 2.获取查询条件和排序条件
		Map<String, Object> searchMap = condition==null ? null : this.getSearchMap(clazz, condition.getSearchMap());
		String[] orderBy = condition==null ? null : this.getOrderBy(clazz, condition.getOrderBy());
		// 3.获取查询的列
		String[] queryColumns = this.getQueryColumns(clazz);
		// 4.执行查询操作
		List<Map<String, Object>> list = superRepository.selectList(tableName, searchMap, orderBy, null, null, queryColumns);
		// 5.将查询结果从List<map>转换成List<T>
		List<T> resultList = this.getResultList(clazz, list);
		return resultList;
	}

	@Override
	public Page<T> findByPage(Class<T> clazz, WhereCondition condition, Long pageSize) {
		if(clazz == null) throw new ParamNullPointException(DefaultSuperService.class, "findByPage(Class<T>, WhereCondition, Long)");
		// 1.获取表名
		String tableName = this.getTableName(clazz);
		// 2.获取总行数
		Long totalCount = 0L;
		Map<String, Object> searchMap = null;
		String[] orderBy = null;
		if(condition == null || condition.getSearchMap() == null 
				|| condition.getSearchMap().isEmpty()) {
			totalCount = (Long) superRepository.selectCount(tableName, null).get("totalCount");
		} else {
			// 查询条件
			searchMap = this.getSearchMap(clazz, condition.getSearchMap());
			totalCount = (Long) superRepository.selectCount(tableName, searchMap).get("totalCount");
		}
		if(condition.getOrderBy() != null && !condition.getOrderBy().isEmpty()) {
			// 排序条件
			orderBy = this.getOrderBy(clazz, condition.getOrderBy());
		}
		DefaultPage<T> page = new DefaultPage<T>();
		if(pageSize != null && pageSize > 0) page.setPageSize(pageSize);// 设置查询条数
		page.setTotalCount(totalCount);// 设置总行数
		// 3.获取查询列名
		String[] selectColumns = this.getQueryColumns(clazz);
		// 4.执行分页查询
		List<Map<String,Object>> list = superRepository.selectList(tableName, searchMap, orderBy, page.getOffset(), page.getRowCount(), selectColumns);
		// 5.将查询结果从List<map>转换成List<T>
		List<T> resultList = this.getResultList(clazz, list);
		
		page.setData(resultList);
		page.setSearchMap(searchMap);// 保存查询条件
		page.setOrderBy(orderBy);// 保存排序条件
		return page;
	}

	@Override
	public Page<T> findByPage(Class<T> clazz, Page<T> page) {
		if(clazz == null || page == null) throw new ParamNullPointException(DefaultSuperService.class, "findByPage(Class<T>, Page<T>)");
		// 1.获取表名
		String tableName = this.getTableName(clazz);
		// 2.获取查询列名
		String[] queryColumns = this.getQueryColumns(clazz);
		// 3.执行查询
		List<Map<String, Object>> list = superRepository.selectList(tableName, page.getSearchMap(), page.getOrderBy(), page.getOffset(), page.getRowCount(), queryColumns);
		// 4.将List<map>转换成List<T>
		List<T> resultList = this.getResultList(clazz, list);
		// 5.保存数据
		page.setData(resultList);
		
		return page;
	}

	@Override
	public List<T> findByList(Class<T> clazz, String fieldName, Boolean isIn, Object[] value) {
		if(clazz == null || value == null) throw new ParamNullPointException(DefaultSuperService.class, "findByList(Class<T>, Boolean, Object[])");
		if(isIn == null) isIn = Boolean.TRUE;// 默认In查询
		// 1.获取表名
		String tableName = this.getTableName(clazz);
		// 2.获取列名
		Field field = null;
		String columnName = "";
		try {
			field = clazz.getDeclaredField(fieldName);
			if(field.isAnnotationPresent(PrimaryKey.class)) {
				columnName = field.getAnnotation(PrimaryKey.class).value();
			} else {
				columnName = field.isAnnotationPresent(Column.class) ? field.getAnnotation(Column.class).value() : fieldName;
			}
			columnName += isIn ? " IN " : " NOT IN ";
		} catch (Exception e) {
			log.error("In查询失败："+ e.getMessage(), e);
			throw new FieldNotFoundException(clazz, fieldName);
		}
		// 3.获取查询的列
		String[] queryColumns = this.getQueryColumns(clazz);
		List<Map<String,Object>> list = superRepository.selectIn(tableName, columnName, value, queryColumns);
		// 4.将List<map>转换成List<T>
		List<T> resultList = this.getResultList(clazz, list);
		return resultList;
	}

}
