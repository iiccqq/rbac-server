package com.rbac.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SysUserRoleDao {

	@Resource
	JdbcTemplate jdbcTemplate;
	public List<Long> queryRoleByUserId(Integer userid) {
		String sql = "select role_id from sys_user_role where user_id = ? ";
		List<Long> roleList = jdbcTemplate.queryForList(sql ,Long.class, userid);
		return roleList;
	}

}
