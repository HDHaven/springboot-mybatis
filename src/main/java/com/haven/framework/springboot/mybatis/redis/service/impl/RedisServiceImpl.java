package com.haven.framework.springboot.mybatis.redis.service.impl;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.haven.framework.springboot.mybatis.redis.service.RedisService;

@Service
public class RedisServiceImpl implements RedisService {
	private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	private boolean isNullEmpty(String key) {
		if(key == null || key.isEmpty())
			return true;
		return false;
	}
	
	@Override
	public Boolean set(String key, Object value) {
		if(this.isNullEmpty(key)) {
			logger.debug("写入缓存失败，key不能为空！");
			return false;
		}
		Boolean success;
		try {
			ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
			opsForValue.set(key, value);
			logger.debug("写入缓存成功  ========》  {key: "+ key +"}");
			success = true;
		} catch(Exception e) {
			logger.error("写入缓存失败  ========》 {key: "+ key +", errMsg: "+ e.getMessage() +"}", e);
			success = false;
		}
		return success;
	}

	@Override
	public Boolean set(String key, Object value, Long expireTime) {
		if(this.isNullEmpty(key)) {
			logger.debug("写入缓存失败，key不能为空！");
			return false;
		}
		Boolean success;
		try {
			ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
			opsForValue.set(key, value);
			redisTemplate.expire(key, expireTime.longValue(), TimeUnit.SECONDS);
			logger.debug("写入缓存成功  ========》  {key: "+ key +"}");
			success = true;
		} catch(Exception e) {
			logger.error("写入缓存失败  ========》 {key: "+ key +", errMsg: "+ e.getMessage() +"}", e);
			success = false;
		}
		return success;
	}

	@Override
	public void remove(String key) {
		if(this.exists(key)) {
			try {
				redisTemplate.delete(key);
				logger.debug("删除缓存成功  ========》  {key: "+ key +"}");
			} catch(Exception e) {
				logger.error("删除缓存失败  ========》  {key: "+ key +", errMsg: "+ e.getMessage() +"}", e);
			}
		}
	}

	@Override
	public void remove(String... keys) {
		if(keys != null && keys.length > 0) {
			for (String key : keys) {
				this.remove(key);
			}
		}
	}

	@Override
	public void removeByPattern(String pattern) {
		if(!this.isNullEmpty(pattern)) {
			Set<String> keys = redisTemplate.keys(pattern);
			if(keys.size() > 0) 
				redisTemplate.delete(keys);
		}
	}

	@Override
	public Boolean exists(String key) {
		if(this.isNullEmpty(key)) {
			logger.debug("写入缓存失败，key不能为空！");
			return false;
		}
		return redisTemplate.hasKey(key);
	}

	@Override
	public Object get(String key) {
		Object result = null;
		try {
			ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
			result = opsForValue.get(key);
			logger.debug("读取缓存成功  ========》  {key: "+ key +"}");
		} catch(Exception e) {
			logger.error("读取缓存失败  ========》  {key: "+ key +", errMsg: "+ e.getMessage() +"}", e);
		}
		return result;
	}

	@Override
	public void hmSet(String key, Object hashKey, Object value) {
		HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
		opsForHash.put(key, hashKey, value);
	}

	@Override
	public Object hmGet(String key, Object hashKey) {
		HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
		return opsForHash.get(key, hashKey);
	}

	@Override
	public void lPush(String key, Object value) {
		ListOperations<String, Object> list = redisTemplate.opsForList();
	    list.rightPush(key, value);
	}

	@Override
	public List<Object> lRange(String key, Long start, Long end) {
		ListOperations<String, Object> list = redisTemplate.opsForList();
	    return list.range(key, start, end);
	}

	@Override
	public void add(String key, Object value) {
		SetOperations<String, Object> set = redisTemplate.opsForSet();
	    set.add(key, value);
	}

	@Override
	public Set<Object> members(String key) {
		SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
	}

	@Override
	public void sortAdd(String key, Object value, Double score) {
		ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, score);
		
	}

	@Override
	public Set<Object> rangeByScore(String key, Double minScore, Double maxScore) {
		ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, minScore, maxScore);
	}

}
