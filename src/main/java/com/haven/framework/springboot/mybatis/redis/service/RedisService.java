package com.haven.framework.springboot.mybatis.redis.service;

import java.util.List;
import java.util.Set;

public interface RedisService {

	/**
	 * 写入缓存
	 * @param key
	 * @param value
	 * @return
	 */
	public Boolean set(final String key, Object value);
	
	/**
	 * 写入缓存，设置key的存活时间
	 * @param key
	 * @param value
	 * @param expireTime 存活时间（单位/秒)
	 * @return
	 */
	public Boolean set(final String key, Object value, Long expireTime);
	
	/**
	 * 删除缓存
	 * @param key
	 */
	public void remove(final String key);
	
	/**
	 * 批量删除缓存
	 * @param keys
	 */
	public void remove(final String... keys);
	
	/**
	 * 批量删除缓存，按模式删除
	 * @param pattern 模式，例如：[user*]表示删除以user开头的所有key
	 */
	public void removeByPattern(final String pattern);
	
	/**
	 * 判断key是否在缓存中
	 * @param key
	 * @return
	 */
	public Boolean exists(final String key);
	
	/**
	 * 读取缓存
	 * @param key
	 * @return
	 */
	public Object get(final String key);
	
	/**
	 * 哈希写入缓存
	 * @param key
	 * @param hashKey
	 * @param value
	 */
	public void hmSet(final String key, Object hashKey, Object value);
	
	/**
	 * 哈希获取缓存
	 * @param key
	 * @param hashKey
	 */
	public Object hmGet(final String key, Object hashKey);
	
	/**
	 * 列表添加
	 * @param key
	 * @param value
	 */
	public void lPush(final String key, Object value);
	
	/**
	 * 列表获取
	 * @param key
	 * @param start 
	 * @param end 
	 * @return
	 */
	public List<Object> lRange(final String key, Long start, Long end);

	/**
	 * 集合添加
	 * @param key
	 * @param value
	 */
	public void add(final String key, Object value);
	
	/**
	 * 集合获取
	 * @param key
	 * @return
	 */
	public Set<Object> members(final String key);
	
	/**
	 * 有序集合添加
	 * @param key
	 * @param value
	 * @param scoure
	 */
	public void sortAdd(final String key, Object value, Double score);
	
	/**
	 * 有序集合获取
	 * @param key
	 * @param minScore
	 * @param maxScore
	 * @return
	 */
	public Set<Object> rangeByScore(final String key, Double minScore, Double maxScore);
	
}
