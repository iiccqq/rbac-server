package com.rbac.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;

import com.alibaba.fastjson.JSON;
import com.rbac.dao.SysUserTokenrDao;
import com.rbac.model.R;
import com.rbac.model.SysUserToken;

public class SecurityFilter implements Filter {

	private SysUserTokenrDao sysUseTokenrDao;

	public SecurityFilter() {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		String token = getRequestToken((HttpServletRequest) request);
		if (StringUtils.isNotBlank(token)) {
			SysUserToken sysUserToken = sysUseTokenrDao.queryUserByToken(token);
			if (sysUserToken != null && sysUserToken.getExpireTime() != null
					&& sysUserToken.getExpireTime().getTime() > new Date().getTime()) {
				chain.doFilter(request, response);
				return;
			}
		}
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String json = JSON.toJSONString(R.error(HttpStatus.SC_UNAUTHORIZED, "invalid token"));
		httpResponse.getWriter().print(json);
		return;
	}

	public void init(FilterConfig fConfig) throws ServletException {

	}

	/**
	 * 获取请求的token
	 */
	private String getRequestToken(HttpServletRequest httpRequest) {
		String token = httpRequest.getHeader("token");
		if (StringUtils.isBlank(token)) {
			token = httpRequest.getParameter("token");
		}
		return token;
	}

	public SysUserTokenrDao getSysUseTokenrDao() {
		return sysUseTokenrDao;
	}

	public void setSysUseTokenrDao(SysUserTokenrDao sysUseTokenrDao) {
		this.sysUseTokenrDao = sysUseTokenrDao;
	}

}
