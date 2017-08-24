package com.rbac.controller;

import java.util.List;
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
import com.rbac.annotation.SysLogInfo;
import com.rbac.constant.Constant;
import com.rbac.dao.SysConfigDao;
import com.rbac.model.Page;
import com.rbac.model.R;
import com.rbac.model.SysConfig;
import com.rbac.util.ValidatorUtils;

@RestController
@RequestMapping("sys/users")
public class SysConfigController {
	@Autowired
	private SysConfigDao sysConfigDao;
	
	
	@GetMapping("/{userid}/configspage")
	public R list(@RequestParam String params, @PathVariable Long userid, Integer start, Integer limit) {
		@SuppressWarnings("unchecked")
		Map<String, Object> para = JSON.parseObject(params, Map.class);
		
		if (userid != Constant.SUPER_ADMIN)
			para.put("createUserId", userid);
		Page<SysConfig> p = sysConfigDao.queryList(para, start, limit);
		return R.ok().put("list", p.getList()).put("total", p.getTotal());
	}
	
	@GetMapping("/{userid}/configs")
	public R getConfigs(String code) {
		List<SysConfig> list = sysConfigDao.getConfigsByCode(code);
		return R.ok().put("list", list);
	}
	
	@GetMapping("/{userid}/configs/{configId}")
	public R select(@PathVariable("configId") Long configId) {	
		SysConfig config = sysConfigDao.queryObject(configId);		
		return R.ok().put("item", config);
	}
	
	@PostMapping("/{userid}/config")
	@SysLogInfo("增加配置")
	public R save(@RequestBody SysConfig config, Long userid) {
		ValidatorUtils.validateEntity(config);		
		sysConfigDao.save(config);
		return R.ok();
	}

	@PutMapping("/{userid}/config")
	@SysLogInfo("修改配置")
	public R update(@RequestBody SysConfig config, Long userid) {
		ValidatorUtils.validateEntity(config);

		sysConfigDao.update(config);

		return R.ok();
	}

	@DeleteMapping("/{userid}/configs")
	@SysLogInfo("删除配置")
	public R delete(@RequestBody Long[] configIds) {
		sysConfigDao.deleteBatch(configIds);
		return R.ok();
	}
	
}
