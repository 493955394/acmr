package com.acmr.model.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import acmr.util.IKeyible;

public class User implements IKeyible, Comparable<User> {
	private String userid;
	private String username;
	private String depcode;
	private String sex;
	private String duty;
	private String tel;
	private String email;
	private String flowcode;
	private String flowtype;

	private String sortcode;

	private List<String> roles;

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	private Date updatetime;
	private String memo;
	private String updateuserid;

	public User() {
		userid = "";
		roles = new ArrayList<String>();
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getUpdateuserid() {
		return updateuserid;
	}

	public void setUpdateuserid(String updateuserid) {
		this.updateuserid = updateuserid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDepcode() {
		return depcode;
	}

	public void setDepcode(String depcode) {
		this.depcode = depcode;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public List<String> getRoles() {
		return roles;
	}

	public void addRole(String v) {
		if (!roles.contains(v)) {
			roles.add(v);
		}
	}

	public void addRoles(String v, String sp) {
		String s[] = v.split(sp);
		for (int i = 0; i < s.length; i++) {
			addRole(s[i]);
		}
	}

	@Override
	public int compareTo(User o) {
		if (o == null) {
			return -1;
		}
		return this.sortcode.compareTo(o.sortcode);
	}

	@Override
	public String Key() {
		return userid;
	}

}
