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
import com.google.common.collect.Maps;
import com.rbac.annotation.SysLogInfo;
import com.rbac.constant.Constant;
import com.rbac.dao.SysRoleDao;
import com.rbac.dao.SysRoleMenuDao;
import com.rbac.model.Page;
import com.rbac.model.R;
import com.rbac.model.SysRole;
import com.rbac.util.ValidatorUtils;

@RestController
@RequestMapping("/sys/users")
public class SysRoleController {
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;

	@GetMapping("/{userid}/rolespage")
	public R list(@RequestParam String params, @PathVariable Long userid, Integer start, Integer limit, Integer type) {
		@SuppressWarnings("unchecked")
		Map<String, Object> para = JSON.parseObject(params, Map.class);
		// 如果不是超级管理员，则只查询自己创建的角色列表
		if (userid != Constant.SUPER_ADMIN)
			para.put("createUserId", userid);
		Page<SysRole> p = sysRoleDao.queryList(para, start, limit);
		return R.ok().put("list", p.getList()).put("total", p.getTotal());

	}

	@GetMapping("/{userid}/roles")
	public R list(@PathVariable Long userid) {
		Map<String, Object> para = Maps.newHashMap();
		if (userid != Constant.SUPER_ADMIN)
			para.put("createUserId", userid);
		List<SysRole> list = sysRoleDao.queryList(para);

		return R.ok().put("list", list);

	}

	@GetMapping("/{userid}/roles/{roleId}")
	public R select(@PathVariable("roleId") Long roleId) {
		SysRole role = sysRoleDao.queryObject(roleId);
		// 查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuDao.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);
		return R.ok().put("role", role);
	}

	@PostMapping("/{userid}/role")
	@SysLogInfo("增加角色")
	public R save(@RequestBody SysRole role, Long userid) {
		ValidatorUtils.validateEntity(role);
		role.setCreateUserId(userid);
		sysRoleDao.save(role);
		return R.ok();
	}
	@SysLogInfo("修改角色")
	@PutMapping("/{userid}/role")
	public R update(@RequestBody SysRole role, Long userid) {
		ValidatorUtils.validateEntity(role);

		role.setCreateUserId(userid);
		sysRoleDao.update(role);

		return R.ok();
	}
	@SysLogInfo("删除角色")
	@DeleteMapping("/{userid}/roles")
	public R delete(@RequestBody Long[] roleIds) {
		sysRoleDao.deleteBatch(roleIds);
		return R.ok();
	}
}
