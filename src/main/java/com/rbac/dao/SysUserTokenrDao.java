package com.rbac.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.rbac.model.SysUserToken;

@Repository
public class SysUserTokenrDao {

	@Resource
	JdbcTemplate jdbcTemplate;

	public void saveOrUpdate(SysUserToken sut) {
		String sql = "replace into sys_user_token(user_id,token,update_time,expire_time) values (?,?,?,?)";
		jdbcTemplate.update(sql,
				new Object[] { sut.getUserId(), sut.getToken(), sut.getUpdateTime(), sut.getExpireTime() });

	}

	public void deleteTokenById(long userId) {
		String sql = "delete from  sys_user_token where user_id = ?";
		jdbcTemplate.update(sql, userId);
	}

	public SysUserToken queryUserByToken(String token) {
		String sql = "select * from sys_user_token where token = ?";
		List<SysUserToken> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<SysUserToken>(SysUserToken.class),
				token);
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}

}
