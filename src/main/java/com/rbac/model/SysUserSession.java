package com.rbac.model;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author admin
 * @email admin@163.com
 * @date 2017-08-22 16:16:58
 */
public class SysUserSession implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String sessionid;
	//
	private String captcha;
	//
	private Date expireTime;
	//
	private Date updateTime;

	/**
	 * 设置：
	 */
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	/**
	 * 获取：
	 */
	public String getSessionid() {
		return sessionid;
	}
	/**
	 * 设置：
	 */
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	/**
	 * 获取：
	 */
	public String getCaptcha() {
		return captcha;
	}
	/**
	 * 设置：
	 */
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	/**
	 * 获取：
	 */
	public Date getExpireTime() {
		return expireTime;
	}
	/**
	 * 设置：
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
