package com.rbac.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SysRoleMenuDao {

	@Resource
	private JdbcTemplate jdbcTemplate;
	public List<Long> queryMenuIdList(Long roleId) {
		String sql = "select menu_id from sys_role_menu where role_id = ?";
		List<Long> list = jdbcTemplate.queryForList(sql, Long.class,roleId);
		return list;
	}

}
