package com.rbac.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.rbac.constant.Constant;
import com.rbac.model.Page;

@Repository
public class QueryDao<T> {

	@Resource
	JdbcTemplate jdbcTemplate;

	public Page<T> queryList(String tableName, Class<T> t, Map<String, Object> params, Integer start, Integer limit,
			String select) {
		StringBuffer sb = new StringBuffer();
		List<Object> values = Lists.newArrayList();
		for (Entry<String, Object> en : params.entrySet()) {
			String key = en.getKey();
			String field = key.split("__")[0];
			Integer intOper = Constant.SEARCH_OPER_LIKE;
			if (key.split("__").length > 1) {
				intOper = Integer.parseInt(key.split("__")[1]);
			}
			Object v = en.getValue();
			if (v == null)
				continue;
			if (sb.length() > 0)
				sb.append(" and ");
			String oper = " like ";
			if (intOper == Constant.SEARCH_OPER_LIKE) {
				oper = " like ";
				v = "%" + v + "%";
			} else if (intOper == Constant.SEARCH_OPER_EQU) {
				oper = " = ";
			} else if (intOper == Constant.SEARCH_OPER_GTE) {
				oper = " >= ";
				v = new Date((long)v);
			} else if (intOper == Constant.SEARCH_OPER_LT) {
				oper = " < ";
				v = new Date((long)v);
			}
			sb.append(String.format("%s %s ?", field, oper));
			values.add(v);
		}
		String selectSql = String.format("select %s from %s", select == null ? "*" : select, tableName);
		if (sb.length() > 0)
			selectSql += " where ";

		selectSql += sb;
		if (start != null && limit != null)
			selectSql += String.format(" limit %d,%d", start, limit);

		List<T> list = jdbcTemplate.query(selectSql, new BeanPropertyRowMapper<T>(t), values.toArray());
		Page<T> p = new Page<T>();
		p.setList(list);
		String totalSql = "select count(1) from " + tableName;
		if (sb.length() > 0)
			totalSql += " where ";
		totalSql += sb;
		int total = jdbcTemplate.queryForObject(totalSql, Integer.class, values.toArray());
		p.setTotal(total);
		return p;
	}


	public Page<T> queryList(String tableName, Class<T> t, Map<String, Object> params, Integer start, Integer limit) {
		return queryList(tableName, t, params, start, limit, null);
	}

	public List<T> queryList(String tableName, Class<T> t, Map<String, Object> params, String select) {
		StringBuffer sb = new StringBuffer();
		List<Object> values = Lists.newArrayList();
		for (Entry<String, Object> en : params.entrySet()) {
			String key = en.getKey();
			Object v = en.getValue();
			if (v == null)
				continue;
			if (sb.length() > 0)
				sb.append(" and ");
			String oper = "";
			if (v instanceof String) {
				oper = " like ";
				v = "%" + v + "%";
			} else if (v instanceof Integer) {
				oper = " = ";
			} else if (v instanceof Long) {
				oper = " = ";
			}

			sb.append(String.format("%s %s ?", key, oper));
			values.add(v);
		}
		String selectSql = String.format("select %s from %s", select == null ? "*" : select, tableName);
		if (sb.length() > 0)
			selectSql += " where ";

		selectSql += sb;

		List<T> list = jdbcTemplate.query(selectSql, new BeanPropertyRowMapper<T>(t), values.toArray());

		return list;
	}

	public List<T> queryList(String tableName, Class<T> t, Map<String, Object> params) {
		return queryList(tableName, t, params, null);
	}

}
