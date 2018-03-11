package com.haven.framework.springboot.mybatis.service;

import java.util.List;
import java.util.Map;

public interface Page<T> {

	public Long getOffset();

	public Long getRowCount();

	public Long getPageSize();

	public void setPageSize(Long pageSize);

	public Long getTotalCount();

	public void setTotalCount(Long totalCount);

	public void setOffset(Long offset);

	public void setRowCount(Long rowCount);

	public void setPageNum(Long pageNum);

	public Long getPageNum();

	public Long getCurrentPage();

	public void setCurrentPage(Long currentPage);
	
	public List<T> getData();

	public void setData(List<T> data);

	public Map<String, Object> getSearchMap();

	public void setSearchMap(Map<String, Object> searchMap);

	public String[] getOrderBy();

	public void setOrderBy(String[] orderBy);
	
}
