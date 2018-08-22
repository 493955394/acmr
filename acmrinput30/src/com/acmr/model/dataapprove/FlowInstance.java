package com.acmr.model.dataapprove;

import java.io.Serializable;

/**
 * 
 * <p>@author ShenHaiOu
 *
 * <p>Company ACMR
 *
 * <p>FLOW_INSTANCE.java
 *
 * <p>2014上午9:42:01
 *
 * <p>方法描述：流程实例
 * 
 * <p>
 */
public class FlowInstance implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 流程实例id */
	private String  instanceid;
	/** 流程编码 */
	private String flowcode;
	/** 业务主键编码 */
	private String code;
	/** 流程启动时间 */
	private String  btime;
	/** 流程状态 */
	private String flowstate;
	/** 流程创建人 */
	private String userid;
	/** 流程完成时间 */
	private String  etime;
	//当前步骤
	private String step;
	//版本
	private int version;
	// 一键办公code
	private String ycode;

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}

	public String getFlowcode() {
		return flowcode;
	}

	public void setFlowcode(String flowcode) {
		this.flowcode = flowcode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFlowstate() {
		return flowstate;
	}

	public void setFlowstate(String flowstate) {
		this.flowstate = flowstate;
	}

	

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getBtime() {
		return btime;
	}

	public void setBtime(String btime) {
		this.btime = btime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getYcode() {
		return ycode;
	}

	public void setYcode(String ycode) {
		this.ycode = ycode;
	}


}
