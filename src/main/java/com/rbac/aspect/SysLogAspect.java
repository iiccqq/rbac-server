package com.rbac.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.rbac.annotation.SysLogInfo;
import com.rbac.dao.SysLogDao;
import com.rbac.dao.SysUserDao;
import com.rbac.model.SysLog;
import com.rbac.model.SysUser;
import com.rbac.util.HttpContextUtils;
import com.rbac.util.IPUtils;

@Aspect
@Component
public class SysLogAspect {
	@Autowired
	private SysLogDao sysLogDao;
	@Resource
	private SysUserDao sysUserDao;

	@Pointcut("@annotation(com.rbac.annotation.SysLogInfo)")
	public void logPointCut() {

	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		// 执行方法
		Object result = point.proceed();
		// 执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;
		// 保存日志
		saveSysLog(point, time);
		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		SysLog sysLog = new SysLog();
		SysLogInfo rbacSysLog = method.getAnnotation(SysLogInfo.class);
		if (rbacSysLog != null) {
			// 注解上的描述
			sysLog.setOperation(rbacSysLog.value());
		}

		// 请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");

		// 请求的参数
		Object[] args = joinPoint.getArgs();
		try {
			String params = new Gson().toJson(args);
			sysLog.setParams(params);
		} catch (Exception e) {

		}

		// 获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		// 设置IP地址
		sysLog.setIp(IPUtils.getIpAddr(request));

		Long userid = (Long) args[0];
		// 用户名
		SysUser su = sysUserDao.queryUserById(userid);
		String username = su.getUsername();
		sysLog.setUsername(username);

		sysLog.setTime(time);
		sysLog.setCreateDate(new Date());
		// 保存系统日志
		sysLogDao.save(sysLog);
	}

}
