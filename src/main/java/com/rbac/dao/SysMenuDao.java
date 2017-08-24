package com.rbac.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.rbac.model.SysMenu;
@Repository
public class SysMenuDao {

	@Resource
	JdbcTemplate jdbcTemplate;

	public List<Long> getUserMenuList(long userid) {
		String sql = "select distinct rm.menu_id from sys_user_role ur 	LEFT JOIN sys_role_menu rm on ur.role_id = rm.role_id  where ur.user_id = ?";
		List<Long> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Long>(Long.class), userid);
		return list;
	}

	public List<SysMenu> queryListParentId(Long parentId) {
		String sql = "select * from sys_menu where parent_id = ? order by order_num asc ";
		List<SysMenu> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<SysMenu>(SysMenu.class), parentId);
		return list;
	}

	public List<SysMenu> queryList() {
		String sql = "select * from sys_menu order by order_num asc ";
		List<SysMenu> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<SysMenu>(SysMenu.class));
		return list;
	}

	public void save(SysMenu sysMenu) {
		String sql = "insert into sys_menu (parent_id,name,url,perms,type,icon,order_num) values(?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, sysMenu.getParentId(), sysMenu.getName(), sysMenu.getUrl(), sysMenu.getPerms(),
				sysMenu.getType(), sysMenu.getIcon(), sysMenu.getOrderNum());

	}

	public void update(SysMenu sysMenu) {
		String sql = "update sys_menu set parent_id = ?, name=?,url=?,perms=?,type=?,icon=?,order_num=? where menu_id=?";
		jdbcTemplate.update(sql, sysMenu.getParentId(), sysMenu.getName(), sysMenu.getUrl(), sysMenu.getPerms(),
				sysMenu.getType(), sysMenu.getIcon(), sysMenu.getOrderNum(),sysMenu.getMenuId());
	}

	public void delete(Long menuid) {
		String sql = "delete from  sys_menu  where menu_id=?";
		jdbcTemplate.update(sql,menuid);		
	}

	public int queryChildrenMenuCount(Long menuid) {
		String sql = "select count(1) from sys_menu where parent_id = ?";
		int count = jdbcTemplate.queryForObject(sql, Integer.class, menuid);
		return count;
	}

	public SysMenu queryObject(Long menuid) {
		String sql = "select * from sys_menu where menu_id = ?  ";
		List<SysMenu> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<SysMenu>(SysMenu.class), menuid);
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	public List<SysMenu> queryNotButtonList() {
		String sql = "select * from sys_menu where type != 2 order by order_num asc ";
		List<SysMenu> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<SysMenu>(SysMenu.class));
		return list;
	}

}
