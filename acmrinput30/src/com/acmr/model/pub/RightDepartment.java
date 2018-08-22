package com.acmr.model.pub;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <p>@author ShenHaiOu
 *
 * <p> Company ACMR
 *
 * <p>Department.java
 *
 * <p> 2014上午9:07:43
 *
 * <p>方法描述：部门
 * 
 * <p>
 */
public class RightDepartment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 00001L;
	/** 部门编码 */
	private String code;
	/** 部门名称 */
	private String cname;
	/** 父部门编码 */
	private String procode;
	/** 状态 */
	private String state;
	/** 创建人 */
	private String createuserid;
	/** 创建时间 */
	private Date createtime;
	/** 备注 */
	private String memo;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getProcode() {
		return procode;
	}

	public void setProcode(String procode) {
		this.procode = procode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreateuserid() {
		return createuserid;
	}

	public void setCreateuserid(String createuserid) {
		this.createuserid = createuserid;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
