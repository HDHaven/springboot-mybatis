package com.haven.framework.springboot.mybatis.interceptor;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 压缩拦截器
 * <p>将responseBody中的内容进行gzip压缩，减少网络带宽的消耗
 * 
 * @author Haven
 * @date 2018/03/10
 */
public class CompressInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(CompressInterceptor.class);
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.error("进入压缩拦截器的postHandle方法");
//		response.getOutputStream();
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
//		super.afterCompletion(request, response, handler, ex);
	}

}
