package com.rbac.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.rbac.constant.Constant;
import com.rbac.dao.SysLogDao;
import com.rbac.model.Page;
import com.rbac.model.R;
import com.rbac.model.SysLog;
import com.rbac.util.ValidatorUtils;

@RestController
@RequestMapping("sys/users")
public class SysLogController {
	@Autowired
	private SysLogDao sysLogDao;
	
	
	@GetMapping("/{userid}/logspage")
	public R list(@RequestParam String params, @PathVariable Long userid, Integer start, Integer limit) {
		@SuppressWarnings("unchecked")
		Map<String, Object> para = JSON.parseObject(params, Map.class);
		
		if (userid != Constant.SUPER_ADMIN)
			para.put("createUserId", userid);
		Page<SysLog> p = sysLogDao.queryList(para, start, limit);
		return R.ok().put("list", p.getList()).put("total", p.getTotal());
	}
	
	
	@GetMapping("/{userid}/logs/{logId}")
	public R select(@PathVariable("logId") Long logId) {	
		SysLog log = sysLogDao.queryObject(logId);		
		return R.ok().put("item", log);
	}
	
	@PostMapping("/{userid}/log")
	public R save(@RequestBody SysLog log, Long userid) {
		ValidatorUtils.validateEntity(log);		
		sysLogDao.save(log);
		return R.ok();
	}

	@PutMapping("/{userid}/log")
	public R update(@RequestBody SysLog log, Long userid) {
		ValidatorUtils.validateEntity(log);

		sysLogDao.update(log);

		return R.ok();
	}

	@DeleteMapping("/{userid}/logs")
	public R delete(@RequestBody Long[] logIds) {
		sysLogDao.deleteBatch(logIds);
		return R.ok();
	}
	
}
