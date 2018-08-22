package com.acmr.model.FreeQuery;

import java.util.List;

public class FilterItem {

	private String level;
	private String name;
	private String fid;
	private String currid;
	private List<SelItem> list;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getCurrid() {
		return currid;
	}

	public void setCurrid(String currid) {
		this.currid = currid;
	}

	public List<SelItem> getList() {
		return list;
	}

	public void setList(List<SelItem> list) {
		this.list = list;
	}
}
