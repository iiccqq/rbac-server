package com.rbac.controller;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.rbac.annotation.SysLogInfo;
import com.rbac.constant.Constant;
import com.rbac.constant.MenuType;
import com.rbac.dao.SysMenuDao;
import com.rbac.dao.SysUserDao;
import com.rbac.dao.SysUserRoleDao;
import com.rbac.dao.SysUserTokenrDao;
import com.rbac.model.R;
import com.rbac.model.SysMenu;

@RestController
@RequestMapping("/sys")
public class SysMenuController {
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;

	@Resource
	private SysUserTokenrDao sysUserTokenrDao;
	@Resource
	private SysMenuDao sysMenuDao;

	@GetMapping("/users/{userid}/menusnav")
	public R menusNav(@PathVariable Long userid) {

		List<SysMenu> menuList = null;
		// 系统管理员，拥有最高权限
		if (userid == Constant.SUPER_ADMIN) {
			menuList = getAllMenuList(null);
		} else {
			// 用户菜单列表
			List<Long> menuids = sysMenuDao.getUserMenuList(userid);
			menuList = getAllMenuList(menuids);
		}

		Set<String> permissions = Sets.newHashSet();

		return R.ok().put("menuList", menuList).put("permissions", permissions);
	}

	/**
	 * 所有菜单列表
	 */
	@GetMapping("/users/{userid}/menus")
	public List<SysMenu> list(@PathVariable Long userid) {
		List<SysMenu> menuList = sysMenuDao.queryList();
		return menuList;
	}
	/**
	 * 选择菜单(添加、修改菜单)
	 */
	@GetMapping("/users/{userid}/menustree")
	public R menusTree(){
		//查询列表数据
		List<SysMenu> menuList = sysMenuDao.queryNotButtonList();
		
		//添加顶级菜单
		SysMenu root = new SysMenu();
		root.setMenuId(0L);
		root.setName("一级菜单");
		root.setParentId(-1L);
		root.setOpen(true);
		menuList.add(root);
		
		return R.ok().put("menuList", menuList);
	}
	/**
	 * 所有菜单列表
	 */
	@GetMapping("/users/{userid}/menus/{menuid}")
	public R select(@PathVariable Long userid, @PathVariable Long menuid) {
		SysMenu menu = sysMenuDao.queryObject(menuid);
		return R.ok().put("menu", menu);
	}
	@PostMapping("/users/{userid}/menu")
	@SysLogInfo("增加菜单")
	public R add(@RequestBody SysMenu sysMenu,@PathVariable  Long userid) {
		sysMenuDao.save(sysMenu);
		return R.ok();
	}

	@PutMapping("/users/{userid}/menu")
	@SysLogInfo("修改菜单")
	public R update(@RequestBody SysMenu sysMenu,@PathVariable  Long userid) {
		sysMenuDao.update(sysMenu);
		return R.ok();
	}

	@DeleteMapping("/users/{userid}/menus/{menuid}")
	@SysLogInfo("删除菜单")
	public R delete(@PathVariable Long userid, @PathVariable Long menuid) {
		int count = sysMenuDao.queryChildrenMenuCount(menuid);
		if (count > 0)
			return R.error("请先删除子节点");
		sysMenuDao.delete(menuid);
		return R.ok();
	}

	/**
	 * 获取所有菜单列表
	 */
	private List<SysMenu> getAllMenuList(List<Long> menuIdList) {
		// 查询根菜单列表
		List<SysMenu> menuList = queryListParentId(0L, menuIdList);
		// 递归获取子菜单
		getMenuTreeList(menuList, menuIdList);

		return menuList;
	}

	// @Override
	public List<SysMenu> queryListParentId(Long parentId, List<Long> menuIdList) {
		List<SysMenu> menuList = queryListParentId(parentId);
		if (menuIdList == null) {
			return menuList;
		}

		List<SysMenu> userMenuList = Lists.newArrayList();
		for (SysMenu menu : menuList) {
			if (menuIdList.contains(menu.getMenuId())) {
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	/**
	 * 递归
	 */
	private List<SysMenu> getMenuTreeList(List<SysMenu> menuList, List<Long> menuIdList) {
		List<SysMenu> subMenuList = Lists.newArrayList();

		for (SysMenu entity : menuList) {
			if (entity.getType() == MenuType.CATALOG.getValue()) {// 目录
				entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
			}
			subMenuList.add(entity);
		}

		return subMenuList;
	}

	public List<SysMenu> queryListParentId(Long parentId) {
		return sysMenuDao.queryListParentId(parentId);
	}

}
