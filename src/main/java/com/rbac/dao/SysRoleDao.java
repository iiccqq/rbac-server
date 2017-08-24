package com.rbac.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.rbac.model.Page;
import com.rbac.model.SysRole;

@Repository
public class SysRoleDao extends QueryDao<SysRole> {


	public Page<SysRole> queryList(Map<String, Object> params, Integer start, Integer limit) {
		Page<SysRole> p = queryList("sys_role", SysRole.class, params, start, limit);
		return p;
	}

	public SysRole queryObject(Long roleId) {
		String sql = "select * from sys_role ";
		List<SysRole> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<SysRole>(SysRole.class));
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	public void save(SysRole role) {
		String sql = "insert into sys_role (role_id,role_name,remark,create_user_id,create_time) values(?,?,?,?,?)";
		jdbcTemplate.update(sql, role.getRoleId(), role.getRoleName(), role.getRemark(), role.getCreateUserId(),
				role.getCreateTime());
		int roleId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
		String roleMenuDelSql = "delete from sys_role_menu where role_id = ?";
		jdbcTemplate.update(roleMenuDelSql, roleId);
		String roleMenuAddSql = "insert into sys_role_menu (role_id,menu_id) values(?,?)";
		List<Object[]> arg = Lists.newArrayList();
		for (Long m : role.getMenuIdList()) {
			Object[] o = new Object[] { roleId, m };
			arg.add(o);
		}
		jdbcTemplate.batchUpdate(roleMenuAddSql, arg);
	}

	public void update(SysRole role) {
		String sql = "update sys_role set role_name = ?,remark = ?,create_user_id = ?,create_time = ? where role_id = ?";
		jdbcTemplate.update(sql, role.getRoleName(), role.getRemark(), role.getCreateUserId(), role.getCreateTime(),
				role.getRoleId());
		String roleMenuDelSql = "delete from sys_role_menu where role_id=?";
		jdbcTemplate.update(roleMenuDelSql, role.getRoleId());
		String roleMenuAddSql = "insert into sys_role_menu (role_id,menu_id) values(?,?)";
		List<Object[]> arg = Lists.newArrayList();
		for (Long m : role.getMenuIdList()) {
			Object[] o = new Object[] { role.getRoleId(), m };
			arg.add(o);
		}
		jdbcTemplate.batchUpdate(roleMenuAddSql, arg);
	}

	public void deleteBatch(Long[] roleIds) {
		StringBuffer sb = new StringBuffer();
		for (Long r : roleIds) {
			if (sb.length() > 0)
				sb.append(",");
			sb.append(r);
		}
		String sql = String.format("delete from sys_role where role_id in (%s)", sb.toString());
		jdbcTemplate.update(sql);
	}

	public List<SysRole> queryList() {
		String sql = "select * from sys_role ";
		List<SysRole> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<SysRole>(SysRole.class));		
		return list;
	}

	public List<SysRole> queryList(Map<String, Object> para) {
		return queryList("sys_role", SysRole.class, para);
	}

}
