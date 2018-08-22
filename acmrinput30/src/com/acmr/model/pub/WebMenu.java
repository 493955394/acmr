package com.acmr.model.pub;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <p>@author ShenHaiOu
 *
 * <p>Company ACMR
 *
 * <p>WebMenu.java
 *
 * <p>2014上午9:24:00
 *
 * <p>方法描述：菜单表
 * 
 * <p>
 */
public class WebMenu implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 菜单编码 */
	private String code;
	/** 菜单名称 */
	private String cname;
	/** 父菜单编码 */
	private String procode;
	/** 链接 */
	private String href;
	/** 排序码 */
	private String sortcode;
	/** 创建时间 */
	private Date createtime;
	/** 状态 */
	private String state;
	/** 说明 */
	private String memo;
	/** 目标 */
	private String target;

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

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getSortcode() {
		return sortcode;
	}

	public void setSortcode(String sortcode) {
		this.sortcode = sortcode;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
