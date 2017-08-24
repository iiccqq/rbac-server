package com.rbac.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.rbac.dao.SysUserDao;
import com.rbac.dao.SysUserRoleDao;
import com.rbac.dao.SysUserTokenrDao;
import com.rbac.model.Page;
import com.rbac.model.R;
import com.rbac.model.SysUser;
import com.rbac.util.ValidatorUtils;

@RestController
@RequestMapping("/sys/users")
public class SysUserController {
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Resource
	private SysUserTokenrDao sysUserTokenrDao;

	/**
	 * 所有用户列表
	 */
	@GetMapping("/{userid}/list")
	public R list(@RequestParam String params, @PathVariable Long userid, Integer start, Integer limit) {
		@SuppressWarnings("unchecked")
		Map<String, Object> para = JSON.parseObject(params, Map.class);
		// 只有超级管理员，才能查看所有管理员列表
		if (userid.intValue() != Constant.SUPER_ADMIN) {
			para.put("createUserId", userid);
		}
		Page<SysUser> p = sysUserDao.queryList(para, start, limit);
		return R.ok().put("list", p.getList()).put("total", p.getTotal());
	}

	/**
	 * 获取登录的用户信息
	 */
	@GetMapping("/{userid}")
	public R info(@PathVariable Integer userid) {
		SysUser su = sysUserDao.queryUserById(userid);
		List<Long> roleIdList = sysUserRoleDao.queryRoleByUserId(userid);
		su.setRoleIdList(roleIdList);
		return R.ok().put("user", su);
	}

	@PostMapping("/{userid}")
	@SysLogInfo("保存用户")
	public R save(@PathVariable Long userid, @RequestBody SysUser user) {
		if (user.getPassword() == null)
			return R.error("密码不能为空");
		ValidatorUtils.validateEntity(user);
		user.setCreateUserId(userid);
		sysUserDao.save(user);
		return R.ok();
	}

	@PutMapping("/{userid}")
	@SysLogInfo("修改用户")
	public R update(@PathVariable Long userid, @RequestBody SysUser sysUser) {
		ValidatorUtils.validateEntity(sysUser);
		sysUserDao.update(sysUser);
		return R.ok();
	}

	@DeleteMapping("/{userid}/users")
	@SysLogInfo("删除用户")
	public R delete(@PathVariable Long userid, @RequestBody Long[] userIds) {
		sysUserDao.deleteBatch(userIds);
		return R.ok();
	}

}
