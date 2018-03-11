package com.haven.framework.springboot.mybatis.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.haven.framework.springboot.mybatis.service.WhereCondition;

public class DefaultWhereCondition implements WhereCondition {

	private Map<String, Object> searchMap = null;
	private List<String> orderBy = null;
	
	public DefaultWhereCondition() {
		searchMap = new HashMap<String, Object>();
	}

	@Override
	public void orderByDesc(String fieldName) {
		if(this.isNullEmpty(fieldName, "")) return;
		if(orderBy == null)
			orderBy = new ArrayList<String>();
		orderBy.add(fieldName +",desc");
	}

	@Override
	public void orderByAsc(String fieldName) {
		if(this.isNullEmpty(fieldName, "")) return;
		if(orderBy == null)
			orderBy = new ArrayList<String>();
		orderBy.add(fieldName +",asc");
	}

	@Override
	public void gt(String fieldName, Object value) {
		if(this.isNullEmpty(fieldName, value)) return;
		searchMap.put(fieldName +",>", value);
	}

	@Override
	public void eq(String fieldName, Object value) {
		if(this.isNullEmpty(fieldName, value)) return;
		searchMap.put(fieldName +",=", value);
	}

	@Override
	public void lt(String fieldName, Object value) {
		if(this.isNullEmpty(fieldName, value)) return;
		searchMap.put(fieldName +",<", value);
	}

	@Override
	public void ge(String fieldName, Object value) {
		if(this.isNullEmpty(fieldName, value)) return;
		searchMap.put(fieldName +",>=", value);
	}

	@Override
	public void le(String fieldName, Object value) {
		if(this.isNullEmpty(fieldName, value)) return;
		searchMap.put(fieldName +",<=", value);
	}

	@Override
	public void notEq(String fieldName, Object value) {
		if(this.isNullEmpty(fieldName, value)) return;
		searchMap.put(fieldName +",!=", value);
		// TODO [!=]不能使用索引，将来优化为"[>value] and [<value]"
	}

	@Override
	public void like(String fieldName, Object value) {
		if(this.isNullEmpty(fieldName, value)) return;
		searchMap.put(fieldName +", like ", "%"+value+"%");
	}

	@Override
	public void notLike(String fieldName, Object value) {
		if(this.isNullEmpty(fieldName, value)) return;
		searchMap.put(fieldName +", not like ", "%"+value+"%");
	}

	@Override
	public void isNull(String fieldName) {
		if(this.isNullEmpty(fieldName, "")) return;
		searchMap.put(fieldName +", is null", "");
	}

	@Override
	public void isNotNull(String fieldName) {
		if(this.isNullEmpty(fieldName, "")) return;
		searchMap.put(fieldName +", is not null", "");
	}

	@Override
	public void betweenAnd(String fieldName, Object floorValue, Object ceilValue) {
		if(this.isNullEmpty(fieldName, "")) return;
		if(floorValue != null)
			searchMap.put(fieldName +"#, >=", floorValue);
		if(ceilValue != null)
			searchMap.put(fieldName +"#, <=", ceilValue);
	}
	
	@Override
	public Map<String, Object> getSearchMap() {
		return searchMap;
	}

	@Override
	public List<String> getOrderBy() {
		return orderBy;
	}

	private boolean isNullEmpty(String fieldName, Object value) {
		if(fieldName == null || "".equals(fieldName.trim()) || value == null)
			return true;
		return false;
	}

}
