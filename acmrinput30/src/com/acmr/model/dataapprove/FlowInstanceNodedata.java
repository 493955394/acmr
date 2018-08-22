package com.acmr.model.dataapprove;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <p>@author ShenHaiOu
 *
 * <p>Company ACMR
 *
 * <p>FlowInstanceNodedata.java
 *
 * <p>2014上午9:55:22
 *
 * <p>方法描述：流程结点实例项
 * 
 * <p>
 */
public class FlowInstanceNodedata implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** id */
	private String id;
	/** 流程结点实例id */
	private Integer instancenodeid;
	/** 审核人id */
	private String audituserid;
	/** 审核时间 */
	private Date audittime;
	/** 审批意见 */
	private String auditexp;
	/** 审核结果 */
	private String auditresult;
	/**审核人*/
	private String username;
	/**审核状态*/
	private String state;
	/**操作人*/
	private String userid;
	
	private  String cname;
	
	
	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Date getAudittime() {
		return audittime;
	}

	public void setAudittime(Date audittime) {
		this.audittime = audittime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public Integer getInstancenodeid() {
		return instancenodeid;
	}

	public void setInstancenodeid(Integer instancenodeid) {
		this.instancenodeid = instancenodeid;
	}

	public String getAudituserid() {
		return audituserid;
	}

	public void setAudituserid(String audituserid) {
		this.audituserid = audituserid;
	}

	

	public String getAuditexp() {
		return auditexp;
	}

	public void setAuditexp(String auditexp) {
		this.auditexp = auditexp;
	}

	public String getAuditresult() {
		return auditresult;
	}

	public void setAuditresult(String auditresult) {
		this.auditresult = auditresult;
	}

}
