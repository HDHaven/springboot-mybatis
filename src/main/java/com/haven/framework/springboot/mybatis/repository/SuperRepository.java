package com.haven.framework.springboot.mybatis.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 数据访问接口
 * 
 * @author Haven
 * @date 2018/01/06 
 */
@Repository
public interface SuperRepository {

	Integer insertOne(@Param("tableName") String tableName, @Param("columnNames") List<String> columnNames, @Param("columnValues") List<Object> columnValues);

	Integer insertBatch(@Param("tableName") String tableName, @Param("columnNames") List<String> columnNames,  @Param("batchColumnValues") List<List<Object>> batchColumnValues);

	Integer deleteOne(@Param("tableName") String tableName, @Param("pk") String pk, @Param("id") Long id);
	
	Integer deleteBatch(@Param("tableName") String tableName, @Param("pk") String pk, @Param("ids") Long... ids);
	
	Integer update(@Param("tableName") String tableName, @Param("pk") String pk, @Param("id") Long id, @Param("updateColunms") Map<String, Object> updateColumns);

	Map<String, Object> selectOne(@Param("tableName") String tableName, @Param("pk") String pk, @Param("id") Long id, @Param("columnNames") List<String> columnNames);
	
	List<Map<String, Object>> selectList(@Param("tableName") String tableName, @Param("condition") Map<String, Object> searchMap, @Param("orderBy") String[] orderBy, @Param("offset") Long offset, @Param("rowCount") Long rowCount, @Param("queryColumns") String... selectColumns);
	
	Map<String, Object> selectCount(@Param("tableName") String tableName, @Param("condition") Map<String, Object> searchMap);
	
	List<Map<String, Object>> selectIn(@Param("tableName") String tableName, @Param("columnName") String columnName, @Param("condition") Object[] value, @Param("queryColumns") String... queryColumns);
	
}
