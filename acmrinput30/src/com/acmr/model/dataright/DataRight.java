package com.acmr.model.dataright;

import java.util.Date;

public class DataRight {

	private String code;
	private String cname;
	private String groupcode;
	private String groupname;
	private String memo;
	private String logictype;
	private String departments;
	private String usercodes;
	private String departmentsname;
	private String usercodesname;
	private Date createtime;


	public String getDepartmentsname() {
		return departmentsname;
	}

	public void setDepartmentsname(String departmentsname) {
		this.departmentsname = departmentsname;
	}

	public String getUsercodesname() {
		return usercodesname;
	}

	public void setUsercodesname(String usercodesname) {
		this.usercodesname = usercodesname;
	}

	public String getCname() {
		return cname;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getDepartments() {
		return departments;
	}

	public void setDepartments(String departments) {
		this.departments = departments;
	}

	public String getUsercodes() {
		return usercodes;
	}

	public void setUsercodes(String usercodes) {
		this.usercodes = usercodes;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGroupcode() {
		return groupcode;
	}

	public void setGroupcode(String groupcode) {
		this.groupcode = groupcode;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}


	public String getLogictype() {
		return logictype;
	}

	public void setLogictype(String logictype) {
		this.logictype = logictype;
	}


}
