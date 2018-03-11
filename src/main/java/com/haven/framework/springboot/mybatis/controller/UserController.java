//package com.haven.framework.springboot.mybatis.controller;
//
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.haven.framework.springboot.mybatis.domain.User;
//import com.haven.framework.springboot.mybatis.service.Page;
//import com.haven.framework.springboot.mybatis.service.SuperService;
//import com.haven.framework.springboot.mybatis.service.WhereCondition;
//import com.haven.framework.springboot.mybatis.service.annotation.Table;
//import com.haven.framework.springboot.mybatis.service.impl.DefaultPage;
//import com.haven.framework.springboot.mybatis.service.impl.DefaultWhereCondition;
//
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//
///**
// * 用户控制层
// * 
// * @author Haven
// * @date 2018/01/28 17:12
// */
//@RestController
//@RequestMapping("/user")
//public class UserController {
//	
//	@Autowired
//	private SuperService<User> userService;
//	
////	@ApiOperation(value="redis#set", notes="redis设值")
//////	@ApiImplicitParam(name="u", value="用户实体user", required=true, dataType="User")
////	@GetMapping("/redis/{key}/{value}")
////	public String setValue(@PathVariable("key")String key, @PathVariable("value")String value) {
////		redisService.push(key, value);
////		return key +":"+ value;
////	}
////	
////	@ApiOperation(value="redis#set", notes="redis设值")
//////	@ApiImplicitParam(name="u", value="用户实体user", required=true, dataType="User")
////	@GetMapping("/redis/{key}")
////	public String setValue(@PathVariable("key")String key) {
////		String value = redisService.put(key);
////		return key +":"+ value;
////	}
////	@ApiOperation(value="redis#del", notes="redis删值")
////@ApiImplicitParam(name="u", value="用户实体user", required=true, dataType="User")
//	@DeleteMapping("/redis/{key}")
////	public String delValue(@PathVariable("key")String key) {
////		redisService.del(key);
////		return key;
////	}
////	
////	@ApiOperation(value="redis#delBatch", notes="redis前缀删值")
////	//@ApiImplicitParam(name="u", value="用户实体user", required=true, dataType="User")
////	@DeleteMapping("/redis/batch/{prefix}")
////	public String delBatchValue(@PathVariable("prefix")String prefix) {
////		redisService.delWithPrefix(prefix);
////		return prefix;
////	}
//	
//	
//	
//	@ApiOperation(value="注册用户", notes="根据User对象注册用户")
//	@ApiImplicitParam(name="u", value="用户实体user", required=true, dataType="User")
//	@PostMapping
//	public User saveOne(@RequestBody User u) {
//		u.setRegistryTime(new Date());
//		return userService.saveOne(u);
//	}
//	
//	@ApiOperation(value="批量插入用户", notes="根据User对象列表批量注册用户")
//	@ApiImplicitParam(name="list", value="用户列表List<User>", required=true, dataType="List<User>")
//	@PostMapping("/batch")
//	public Integer saveBatch(@RequestBody List<User> list) {
//		return userService.saveBatch(list);
//	}
//	
//	@ApiOperation(value="获取用户详情", notes="根据url的id来获取用户详细信息")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name="id", value="用户ID", required=true, dataType="Long", paramType="path"),
//		@ApiImplicitParam(name="useCache", value="是否使用缓存，0/1", required=true, dataType="Integer", paramType="path")
//	})
//	@GetMapping("/{id}/{useCache}")
//	public User findById(@PathVariable("id") Long id, @PathVariable("useCache") Integer useCache) {
//		User user = null;
//		if(useCache == 0) {
//			// 不使用缓存
//			user = userService.findById(User.class, id);
//		} else if(useCache == 1) {
//			// 使用缓存
//			String key = User.class.getAnnotation(Table.class).value() +":"+ id;
//			user = redisService.put(key, User.class);
//			if(user == null) {
//				user = userService.findById(User.class, id);
//				redisService.push(key, user);
//			}
//		}
//		
//		return user;
//	}
//	
////	@ApiOperation(value="从缓存中获取用户详情", notes="根据url的id来获取用户详细信息")
////	@ApiImplicitParam(name="id", value="用户ID", required=true, dataType="Long", paramType="path")
////	@GetMapping("/cache/{id}")
////	public User findByIdWithCache(@PathVariable("id") Long id) {
////		String key = User.class.getAnnotation(Table.class).value() +":"+ String.valueOf(id);
////		User user = redisService.put(key, User.class);
////		if(user == null) {
////			System.err.println("DEBUG ==> 第一次从数据库中查询");
////			user = userService.findById(User.class, id);
////			redisService.push(key, user);
////		}
////		return user;
////	}
//	
//	@ApiOperation(value="更新用户信息", notes="根据url的id指定更新的对象，根据用户对象u指定更新的信息")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name="id", value="用户ID", required=true, dataType="Long", paramType="path"),
//		@ApiImplicitParam(name="u", value="用户实体对象u", required=true, dataType="User")
//	})
//	@PutMapping("/{id}")
//	public Boolean update(@PathVariable("id") Long id, @RequestBody User u) {
//		return userService.update(id, u);
//	}
//	
//	@ApiOperation(value="删除用户", notes="根据url的id删除指定用户")
//	@ApiImplicitParam(name="id", value="用户编号", required=true, dataType="Long", paramType="path")
//	@DeleteMapping("/{id}")
//	public Boolean deleteOne(@PathVariable("id") Long id) {
//		return userService.deleteOne(User.class, id);
//	}
//	
//	@ApiOperation(value="批量删除用户", notes="根据用户编号数组批量删除用户")
//	@ApiImplicitParam(name="ids", value="用户编号数组", required=true, dataType="Long[]")
//	@DeleteMapping("/batch")
//	public Boolean deleteBatch(@RequestBody Long... ids) {
//		return userService.deleteBatch(User.class, ids);
//	}
//	
//	@ApiOperation(value="分页查询用户信息", notes="根据用户对象封装的条件查询用户信息")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name="u", value="用户实体对象u", required=false, dataType="User"),
//		@ApiImplicitParam(name="useCache", value="是否使用缓存，0/1", required=true, dataType="Integer", paramType="path")
//	})
//	@PostMapping("/page/{useCache}")
//	public Page<User> findByPage(@RequestBody User u, @PathVariable("useCache") Integer useCache) {
//		WhereCondition condition = new DefaultWhereCondition();
//		condition.like("userName", u.getUserName());
//		condition.orderByDesc("registryTime");
//		if(useCache == 0) return userService.findByPage(User.class, condition, 5L);
//		String key = u.getClass().getAnnotation(Table.class).value()
//				+":"+ u.getUserName();
//		DefaultPage<User> page = pageService.put(key, DefaultPage.class);
//		if(page == null) {
//			System.err.println("DEBUG ==> 没有使用缓存");
//			page = (DefaultPage<User>) userService.findByPage(User.class, condition, 5L);
//			pageService.push(key, page);
//		}
//		return page;
//	}
//	
//	@ApiOperation(value="分页查询用户信息", notes="根据分页对象Page分页获取用户信息，通常在第一次分页查询后调用")
//	@ApiImplicitParam(name="page", value="分页对象page", required=true, dataType="Page<User>")
//	@PostMapping("/list")
//	public Page<User> findByPage(@RequestBody DefaultPage<User> page) {
//		return userService.findByPage(User.class, page);
//	}
//	
//	@ApiOperation(value="获取所有用户信息", notes="返回所有用户信息，不分页处理")
//	@GetMapping("/all")
//	public List<User> findAll() {
//		return userService.findByList(User.class, null);
//	}
//	
//}
