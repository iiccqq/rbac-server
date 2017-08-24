package com.rbac.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.rbac.model.SysUserSession;

@Repository
public class SysUserSessionDao  {

	@Resource
	JdbcTemplate jdbcTemplate;


	public SysUserSession queryObject(String   sessionid ) {
		String sql = "select * from sys_user_session where  sessionid  = ? ";
		List<SysUserSession> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<SysUserSession>(SysUserSession.class),sessionid);
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	public void save(SysUserSession sysUserSession) {
		String sql = "replace into sys_user_session (sessionid,captcha,expire_time,update_time) " +
		" values  (?,?,?,?) ";
		jdbcTemplate.update(sql,new Object[]{sysUserSession.getSessionid(),sysUserSession.getCaptcha(),sysUserSession.getExpireTime(),sysUserSession.getUpdateTime()});	
	}	
	
	
	
}
