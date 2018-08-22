package com.acmr.model.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import acmr.util.IKeyible;

public class Role implements IKeyible, Comparable<Role> {
	private String code;
	private String cname;
	private List<String> rights;
	private String sortcode;
	private Date updatetime;
	private String updateuserid;
	private String memo;
	private String state;

	private String parent;

	private List<String> childs;

	public Role() {
		code = "";
		parent = "";
		rights = new ArrayList<String>();
		childs = new ArrayList<String>();
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

	public List<String> getRights() {
		return rights;
	}

	public String getSortcode() {
		return sortcode;
	}

	public void setSortcode(String sortcode) {
		this.sortcode = sortcode;
	}

	public void addRight(String v) {
		if (!v.equals("") && !rights.contains(v)) {
			rights.add(v);
		}
	}

	public void setRights(List<String> rights) {
		this.rights = rights;
	}

	public void addRights(String v, String sp) {
		String s[] = v.split(sp);
		for (int i = 0; i < s.length; i++) {
			addRight(s[i]);
		}
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
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
	public int compareTo(Role o) {
		if (o == null) {
			return -1;
		}
		return this.sortcode.compareTo(o.sortcode);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
