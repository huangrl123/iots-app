package com.dahuangit.water.app.util;

import java.io.Serializable;

public class Response implements Serializable {
	private Boolean success = true;

	private String msg = null;

	/** 用户主键id */
	private Integer userId = null;

	/** 用户名称 */
	private String userName = null;

	/** 用户昵称 */
	private String userAbbr = null;

	/** 用户登录密码 */
	private String password = null;

	/** 备注 */
	private String remark = null;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAbbr() {
		return userAbbr;
	}

	public void setUserAbbr(String userAbbr) {
		this.userAbbr = userAbbr;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
