package com.acmr.model.sysindex;

import java.io.Serializable;

/**
 * 
 * <p>@author ShenHaiOu
 *
 * <p>Company ACMR
 *
 * <p>FLOW_NODE.java
 *
 * <p>2014上午9:47:53
 *
 * <p>方法描述：流程结点表
 * 
 * <p>
 */
public class FlowNode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 流程结点id */
	private String  nodeid;
	/** 流程编码 */
	private String flowcode;
	/** 结点内容 */
	private String nodedata;
	/** 顺序码 */
	private String sort;
	/** 审核类型(多人审核单人通过/多人通过) */
	private String type;
	private String  version;
	
	

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public String getFlowcode() {
		return flowcode;
	}

	public void setFlowcode(String flowcode) {
		this.flowcode = flowcode;
	}

	public String getNodedata() {
		return nodedata;
	}

	public void setNodedata(String nodedata) {
		this.nodedata = nodedata;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
