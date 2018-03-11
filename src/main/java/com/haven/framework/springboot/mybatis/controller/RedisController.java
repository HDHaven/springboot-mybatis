package com.haven.framework.springboot.mybatis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haven.framework.springboot.mybatis.domain.User;
import com.haven.framework.springboot.mybatis.redis.service.RedisService;
import com.haven.framework.springboot.mybatis.service.Page;
import com.haven.framework.springboot.mybatis.service.SuperService;
import com.haven.framework.springboot.mybatis.service.WhereCondition;
import com.haven.framework.springboot.mybatis.service.annotation.Table;
import com.haven.framework.springboot.mybatis.service.impl.DefaultWhereCondition;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/redis")
public class RedisController {
	private static final Logger logger = LoggerFactory.getLogger(RedisController.class);
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private SuperService<User> userService;
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value="分页查询用户信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name="useCache", value="是否使用缓存，0/1", required=true, dataType="Integer", paramType="path"),
		@ApiImplicitParam(name="user", value="用户实体对象user", required=false, dataType="User")
	})
	@PostMapping("/page/{useCache}")
	public Page<User> findByPage(@RequestBody User user, @PathVariable("useCache") Integer useCache) {
		WhereCondition condition = new DefaultWhereCondition();
		condition.like("userName", user.getUserName());
		condition.orderByDesc("registryTime");
		if(useCache == 0) {
			logger.debug("==》 直接从数据库查询，不使用redis缓存");
			return userService.findByPage(User.class, condition, 5L);
		}
		String key = user.getClass().getAnnotation(Table.class).value()
				+":"+ user.getUserName();
		if(redisService.exists(key)) {
			logger.debug("==》  从redis数据库中获取缓存数据");
			return (Page<User>) redisService.get(key);
		} else {
			Page<User> page = userService.findByPage(User.class, condition, 5L);
			redisService.set(key, page);
			logger.debug("==》 第一次访问，缓存没有，先从数据库中获取再设值到redis库中");
			return page;
		}
	}
	
}
