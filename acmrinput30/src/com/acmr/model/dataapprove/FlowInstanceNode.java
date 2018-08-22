package com.acmr.model.dataapprove;

import java.io.Serializable;
import java.util.Date;

import com.acmr.model.dataload.DataLoad;


/**
 * 
 * <p>@author ShenHaiOu
 *
 * <p>Company ACMR
 *
 * <p>FlowInstanceNode.java
 *
 * <p>2014上午9:51:06
 *
 * <p>方法描述：流程结点实例表
 * 
 * <p>
 */
public class FlowInstanceNode extends DataLoad implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** id */
	private String  id;
	/** 流程实例id */
	private String  instanceid;
	/** 流程结点id */
	private String  nodeid;
	/** 创建时间 */
	private Date btime;
	/** 结束时间 */
	private Date  etime;
	/** 审核结果 */
	private String auditresult;
	/**状态*/
	private String state;
	/**版本*/
	private   String version;
	private String code;
	private String taskId;//任务id
	private String userId;//当前登陆用户id
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public String getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public Date getBtime() {
		return btime;
	}

	public void setBtime(Date btime) {
		this.btime = btime;
	}

	public Date getEtime() {
		return etime;
	}

	public void setEtime(Date etime) {
		this.etime = etime;
	}

	public String getAuditresult() {
		return auditresult;
	}

	public void setAuditresult(String auditresult) {
		this.auditresult = auditresult;
	}

	
}
