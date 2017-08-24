package com.rbac.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.rbac.model.Page;
import com.rbac.model.SysConfig;

@Repository
public class SysConfigDao extends QueryDao<SysConfig> {


	public Page<SysConfig> queryList(Map<String, Object> params, Integer start, Integer limit) {
		Page<SysConfig> p = queryList("sys_config", SysConfig.class, params, start, limit);
		return p;
	}

	public SysConfig queryObject(Long   id ) {
		String sql = "select * from sys_config where  id  = ? ";
		List<SysConfig> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<SysConfig>(SysConfig.class),id);
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	public void save(SysConfig sysConfig) {
		String sql = "insert into sys_config (parentid,value,text,status,remark,code) " +
		" values  (?,?,?,?,?,?) ";
		jdbcTemplate.update(sql,new Object[]{sysConfig.getParentid(),sysConfig.getValue(),sysConfig.getText(),sysConfig.getStatus(),sysConfig.getRemark(),sysConfig.getCode()});	
	}	
	
	public void update(SysConfig sysConfig) {
		String sql = "update sys_config  set parentid = ?,value = ?,text = ?,status = ?,remark = ?,code = ?" +
		" where id = ?";
		jdbcTemplate.update(sql, new Object[]{sysConfig.getParentid(),sysConfig.getValue(),sysConfig.getText(),sysConfig.getStatus(),sysConfig.getRemark(),sysConfig.getCode(),sysConfig.getId()});		
	}
	
	public void deleteBatch(Long[]  ids) {
		StringBuffer sb = new StringBuffer();
		for (Long id :  ids) {
			if (sb.length() > 0)
				sb.append(",");
			sb.append(id);
		}
		String sql = String.format("delete from sys_config where id in (%s)", sb.toString());
		jdbcTemplate.update(sql);
	}

	public List<SysConfig> getConfigsByCode(String code) {
		List<SysConfig> r = Lists.newArrayList();
		String sql = "select * from sys_config where  code  = ? ";
		List<SysConfig> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<SysConfig>(SysConfig.class), code);
		if (list.size() > 0) {
			SysConfig parentConfig = list.get(0);
			r.add(parentConfig);
			Integer parentId = parentConfig.getId();
			String childrenConfigListSql = "select * from sys_config where  parentid  = ?";
			List<SysConfig> childrenList = jdbcTemplate.query(childrenConfigListSql,
					new BeanPropertyRowMapper<SysConfig>(SysConfig.class), parentId);
			r.addAll(childrenList);
		}
		return r;
	}
	
}
