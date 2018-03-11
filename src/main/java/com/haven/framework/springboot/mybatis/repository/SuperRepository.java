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

	/**
	 * 单条新增
	 * 
	 * @param tableName 数据库表名
	 * @param columnNames 表列名
	 * @param columnValues 表列值
	 * @return 成功返回新增的数目
	 */
	Integer insertOne(@Param("tableName") String tableName, @Param("columnNames") List<String> columnNames, @Param("columnValues") List<Object> columnValues);

	/**
	 * 批量新增
	 * 
	 * @param tableName 数据库表名
	 * @param columnNames 表列名
	 * @param batchColumnValues 表列值列表
	 * @return 成功返回新增的数目
	 */
	Integer insertBatch(@Param("tableName") String tableName, @Param("columnNames") List<String> columnNames,  @Param("batchColumnValues") List<List<Object>> batchColumnValues);

	/**
	 * 单条删除
	 * 
	 * @param tableName 数据库表名
	 * @param pk 主键列名(通常设为自增的id)
	 * @param id 主键对应的值
	 * @return 成功返回删除的数目
	 */
	Integer deleteOne(@Param("tableName") String tableName, @Param("pk") String pk, @Param("id") Long id);
	
	/**
	 * 批量删除
	 * 
	 * @param tableName 数据库表名
	 * @param pk 主键名称(通常设为自增的id)
	 * @param ids 主键对应的值列表
	 * @return 成功返回删除的数目
	 */
	Integer deleteBatch(@Param("tableName") String tableName, @Param("pk") String pk, @Param("ids") Long... ids);
	
	/**
	 * 根据主键更新数据
	 * 
	 * @param tableName 表名
	 * @param pk 主键名
	 * @param id 主键值
	 * @param updateColumns 需要更新的列
	 * @return 成功返回更新的数目
	 */
	Integer update(@Param("tableName") String tableName, @Param("pk") String pk, @Param("id") Long id, @Param("updateColunms") Map<String, Object> updateColumns);

	/**
	 * 根据主键查询单条记录
	 * 
	 * @param tableName 表名
	 * @param pk 主键名
	 * @param id 主键值
	 * @param columnNames 需要查询的列
	 * @return 成功返回查询结果的map集合
	 */
	Map<String, Object> selectOne(@Param("tableName") String tableName, @Param("pk") String pk, @Param("id") Long id, @Param("columnNames") List<String> columnNames);
	
	/**
	 * 分页查询，如果offset、rowCount为null，则不做分页处理
	 * 
	 * @param tableName 表名
	 * @param searchMap 查询条件
	 * @param orderBy 排序条件
	 * @param offset 位移量
	 * @param rowCount 查询条数
	 * @param selectColumns 查询列名
	 * @return 成功返回查询结果
	 */
	List<Map<String, Object>> selectList(@Param("tableName") String tableName, @Param("condition") Map<String, Object> searchMap, @Param("orderBy") String[] orderBy, @Param("offset") Long offset, @Param("rowCount") Long rowCount, @Param("queryColumns") String... selectColumns);
	
	/**
	 * 查询总条数
	 * @param tableName 表名
	 * @param searchMap 查询条件
	 * @return 成功返回查询总数目
	 */
	Map<String, Object> selectCount(@Param("tableName") String tableName, @Param("condition") Map<String, Object> searchMap);
	
	/**
	 * in查询
	 * eg: select queryColumns[0], queryColumns[1], ..., queryColumns[queryColumns.length-1]
	 *  	from tableName where columnName in (value[0], value[1], ..., value[value.length-1])
	 * @param tableName
	 * @param columnName
	 * @param value
	 * @param queryColumns
	 * @return 成功返回查询map结果集
	 */
	List<Map<String, Object>> selectIn(@Param("tableName") String tableName, @Param("columnName") String columnName, @Param("condition") Object[] value, @Param("queryColumns") String... queryColumns);
	
}
