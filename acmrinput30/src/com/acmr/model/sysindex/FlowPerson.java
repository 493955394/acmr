package com.acmr.model.sysindex;

import java.io.Serializable;

/**
 * 
 * <p>@author ShenHaiOu
 *
 * <p>Company ACMR
 *
 * <p>FlowPerson.java
 *
 * <p>2014上午9:58:39
 *
 * <p>方法描述：流程人员
 * 
 * <p>
 */
public class FlowPerson implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**id*/
	private String id;
	/**流程结点id*/
	private String nodeid;
	/**审核人类型(角色、人员)*/
	private String type;
	/**角色/人员编码*/
	private String proleid;

	private int version;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProleid() {
		return proleid;
	}

	public void setProleid(String proleid) {
		this.proleid = proleid;
	}

}
