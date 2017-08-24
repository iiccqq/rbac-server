package com.rbac.model;

import java.io.Serializable;



/**
 * 系统配置信息表
 * 
 * @author admin
 * @email admin@163.com
 * @date 2017-08-22 15:05:00
 */
public class SysConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Integer id;
	//父id
	private Integer parentid;
	//值
	private String value;
	//文本
	private String text;
	//状态   0：正常  1：停用
	private Integer status;
	//备注
	private String remark;
	//
	private String code;

	/**
	 * 设置：id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：父id
	 */
	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}
	/**
	 * 获取：父id
	 */
	public Integer getParentid() {
		return parentid;
	}
	/**
	 * 设置：值
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 获取：值
	 */
	public String getValue() {
		return value;
	}
	/**
	 * 设置：文本
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * 获取：文本
	 */
	public String getText() {
		return text;
	}
	/**
	 * 设置：状态   0：正常  1：停用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态   0：正常  1：停用
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：
	 */
	public String getCode() {
		return code;
	}
}
