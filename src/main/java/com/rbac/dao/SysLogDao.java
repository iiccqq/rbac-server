package com.rbac.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.rbac.model.Page;
import com.rbac.model.SysLog;

@Repository
public class SysLogDao extends QueryDao<SysLog> {


	public Page<SysLog> queryList(Map<String, Object> params, Integer start, Integer limit) {
		Page<SysLog> p = queryList("sys_log", SysLog.class, params, start, limit);
		return p;
	}

	public SysLog queryObject(Long   id ) {
		String sql = "select * from sys_log where  id  = ?";
		List<SysLog> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<SysLog>(SysLog.class),id);
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	public void save(SysLog sysLog) {
		String sql = "insert into sys_log (username,operation,method,params,time,ip,create_date) " +
		" values  (?,?,?,?,?,?,?) ";
		jdbcTemplate.update(sql,new Object[]{sysLog.getUsername(),sysLog.getOperation(),sysLog.getMethod(),sysLog.getParams(),sysLog.getTime(),sysLog.getIp(),sysLog.getCreateDate()});	
	}	
	
	public void update(SysLog sysLog) {
		String sql = "update sys_log set username = ?,operation = ?,method = ?,params = ?,time = ?,ip = ?,create_date = ?" +
		" where id = ?";
		jdbcTemplate.update(sql, new Object[]{sysLog.getUsername(),sysLog.getOperation(),sysLog.getMethod(),sysLog.getParams(),sysLog.getTime(),sysLog.getIp(),sysLog.getCreateDate(),sysLog.getId()});		
	}
	
	public void deleteBatch(Long[]  ids) {
		StringBuffer sb = new StringBuffer();
		for (Long id :  ids) {
			if (sb.length() > 0)
				sb.append(",");
			sb.append(id);
		}
		String sql = String.format("delete from sys_log where id in (%s)", sb.toString());
		jdbcTemplate.update(sql);
	}
	
}
