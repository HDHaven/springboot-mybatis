package com.haven.framework.springboot.mybatis.service.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.haven.framework.springboot.mybatis.service.Page;

public class DefaultPage<T> implements Page<T>, Serializable {
	private static final long serialVersionUID = 1L;

	private Long offset;// 位移量
	private Long rowCount;// 查询条数
	private Long pageSize = 10L;// 页面大小
	private Long totalCount;// 查询总行数
	private Long pageNum;// 页数
	private Long currentPage = 1L;// 当前页
	private List<T> data;
	private Map<String, Object> searchMap;
	private String[] orderBy;
	
	public DefaultPage() {}

	public Long getOffset() {
		offset = (currentPage - 1) * pageSize;
		return offset;
	}

	public Long getRowCount() {
		rowCount = currentPage*pageSize <= totalCount ? pageSize : totalCount-(currentPage-1)*pageSize;
		return rowCount;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		// 根据总条数计算处总页数
		pageNum = totalCount%pageSize==0 ? totalCount/pageSize : totalCount/pageSize+1;
		this.totalCount = totalCount;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	public void setPageNum(Long pageNum) {
		this.pageNum = pageNum;
	}

	public Long getPageNum() {
		return pageNum;
	}

	public Long getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Long currentPage) {
		currentPage = currentPage<1 ? 1 : currentPage;
		currentPage = currentPage>pageNum ? pageNum : currentPage;
		this.currentPage = currentPage;
	}
	
	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Map<String, Object> getSearchMap() {
		return searchMap;
	}

	public void setSearchMap(Map<String, Object> searchMap) {
		this.searchMap = searchMap;
	}

	public String[] getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String[] orderBy) {
		this.orderBy = orderBy;
	}

	@Override
	public String toString() {
		return "DefaultPage [offset=" + offset + ", rowCount=" + rowCount + ", pageSize=" + pageSize + ", totalCount="
				+ totalCount + ", pageNum=" + pageNum + ", currentPage=" + currentPage + ", data=" + data
				+ ", searchMap=" + searchMap + ", orderBy=" + Arrays.toString(orderBy) + "]";
	}

}
