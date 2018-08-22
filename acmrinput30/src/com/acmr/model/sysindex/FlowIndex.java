package com.acmr.model.sysindex;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * <p>@author ShenHaiOu
 *
 * <p>Company ACMR
 *
 * <p>FLOW_INDEX.java
 *
 * <p>2014上午9:45:14
 *
 * <p>方法描述：流程定义
 * 
 * <p>
 */
public class FlowIndex implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 流程编码 */
	private String flowcode;
	/** 流程名称 */
	private String cname;
	/** 制度内容 */
	private String flowdata;
	/** 创建时间 */
	private Date createtime;
	/** 备注 */
	private String memo;
	
	private int version;
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getFlowcode() {
		return flowcode;
	}
	public void setFlowcode(String flowcode) {
		this.flowcode = flowcode;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getFlowdata() {
		return flowdata;
	}
	public void setFlowdata(String flowdata) {
		this.flowdata = flowdata;
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
