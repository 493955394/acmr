package com.acmr.model.dataright;

import java.util.Date;

public class DataGroup {

	private String code;
	private String cname;
	private Date createtime;


	public String getCname() {
		return cname;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	
}
