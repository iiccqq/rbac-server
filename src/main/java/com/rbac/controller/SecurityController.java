package com.rbac.controller;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rbac.dao.SysUserTokenrDao;
import com.rbac.model.R;
import com.rbac.model.SysUserToken;

@RestController
public class SecurityController {

	@Resource
	private SysUserTokenrDao sysUseTokenrDao;

	@RequestMapping(value = "/validateToken")
	public R validateToken(String token) throws IOException {
		SysUserToken sysUserToken = sysUseTokenrDao.queryUserByToken(token);
		if (sysUserToken == null || sysUserToken.getExpireTime().getTime() <= new Date().getTime()) {
			R.error(HttpStatus.SC_UNAUTHORIZED, "invalid token");
		}
		return R.ok();

	}

}
