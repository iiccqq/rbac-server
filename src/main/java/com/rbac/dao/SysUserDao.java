package com.rbac.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.rbac.model.Page;
import com.rbac.model.SysUser;

@Repository
public class SysUserDao extends QueryDao<SysUser> {

	public SysUser queryByUserName(String username) {
		String sql = "select * from sys_user where username = ?";
		List<SysUser> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<SysUser>(SysUser.class), username);
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	public SysUser queryUserById(long userid) {
		String sql = "select * from sys_user where user_id = ?";
		List<SysUser> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<SysUser>(SysUser.class), userid);
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	public Page<SysUser> queryList(Map<String, Object> para, Integer start, Integer limit) {
		return this.queryList("sys_user", SysUser.class, para, start, limit);

	}

	public void save(SysUser sysUser) {
		String sql = "insert into sys_user(username,password,salt,email,mobile,status,create_user_id,create_time) values(?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, sysUser.getUsername(), sysUser.getPassword(), sysUser.getSalt(), sysUser.getEmail(),
				sysUser.getMobile(), sysUser.getStatus(), sysUser.getCreateUserId(), sysUser.getCreateTime());
		int userId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
		String roleDelSql = "delete from sys_user_role where user_id = ?";
		jdbcTemplate.update(roleDelSql, userId);
		String roleAddSql = "insert into sys_user_role (user_id,role_id) values(?,?)";
		List<Object[]> arg = Lists.newArrayList();
		for (Long m : sysUser.getRoleIdList()) {
			Object[] o = new Object[] { userId, m };
			arg.add(o);
		}
		jdbcTemplate.batchUpdate(roleAddSql, arg);
		

	}

	public void update(SysUser sysUser) {
		String sql = "update sys_user set username = ?,salt = ?,email = ?,mobile = ?,status = ?,create_user_id = ?,create_time = ? where user_id = ? ";
		jdbcTemplate.update(sql, sysUser.getUsername(),  sysUser.getSalt(), sysUser.getEmail(),
				sysUser.getMobile(), sysUser.getStatus(), sysUser.getCreateUserId(), sysUser.getCreateTime(),
				sysUser.getUserId());
		Long userId = sysUser.getUserId();
		String roleDelSql = "delete from sys_user_role where user_id = ?";
		jdbcTemplate.update(roleDelSql, userId);
		String roleAddSql = "insert into sys_user_role (user_id,role_id) values(?,?)";
		List<Object[]> arg = Lists.newArrayList();
		for (Long m : sysUser.getRoleIdList()) {
			Object[] o = new Object[] { userId, m };
			arg.add(o);
		}
		jdbcTemplate.batchUpdate(roleAddSql, arg);

	}

	public void deleteBatch(Long[] userIds) {
		StringBuffer sb = new StringBuffer();
		for (Long r : userIds) {
			if (sb.length() > 0)
				sb.append(",");
			sb.append(r);
		}
		String sql = String.format("delete from sys_user where user_id in (%s)", sb.toString());
		jdbcTemplate.update(sql);
	}

}
