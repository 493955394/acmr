package com.acmr.model.pub;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <p>@author ShenHaiOu
 *
 * <p>Company ACMR
 *
 * <p>RightRole.java
 *
 * <p>2014上午9:16:01
 *
 * <p>方法描述：角色表
 * 
 * <p>
 */
public class RightRole implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**角色编码*/
	private String code;
	/**角色名称*/
	private String cname;
	/**创建时间*/
	private Date createtime;
	/**状态*/
	private String state;
	/**说明*/
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
	
}                

