package com.acmr.model.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import acmr.util.IKeyible;

public class Department implements IKeyible, Comparable<Department> {

	private String code;
	private String cname;
	private Date updatetime;
	private String updateuserid;
	private String memo;

	private String flowcode;
	private String flowtype;

	private String sortcode;
	private String parent;
	private List<String> childs;

	public Department() {
		code="";
		parent="";			
		childs = new ArrayList<String>();
	}

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

	public String getFlowcode() {
		return flowcode;
	}

	public void setFlowcode(String flowcode) {
		this.flowcode = flowcode;
	}

	public String getFlowtype() {
		return flowtype;
	}

	public void setFlowtype(String flowtype) {
		this.flowtype = flowtype;
	}

	public String getSortcode() {
		return sortcode;
	}

	public void setSortcode(String sortcode) {
		this.sortcode = sortcode;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getUpdateuserid() {
		return updateuserid;
	}

	public void setUpdateuserid(String updateuserid) {
		this.updateuserid = updateuserid;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<String> getChilds() {
		return childs;
	}

	public void addChild(String v) {
		if (!childs.contains(v)) {
			childs.add(v);
		}
	}

	@Override
	public String Key() {
		return code;
	}

	@Override
	public int compareTo(Department o) {
		if (o == null) {
			return -1;
		}
		return this.sortcode.compareTo(o.sortcode);
	}

}
