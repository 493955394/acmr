package com.acmr.model.theme;

import java.util.List;

import acmr.util.IKeyible;

public class MenuRight implements IKeyible {

	private String id;
	private String menucode;
	private String dept;
	private String user;
	private String rols;
	private List<RightModel> childs;

	public MenuRight() {
		// TODO Auto-generated constructor stub
	}

	public MenuRight(String id, String menucode, String dept, String user, String rols) {
		this.id = id;
		this.menucode = menucode;
		this.dept = dept;
		this.user = user;
		this.rols = rols;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMenucode() {
		return menucode;
	}

	public void setMenucode(String menucode) {
		this.menucode = menucode;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getRols() {
		return rols;
	}

	public void setRols(String rols) {
		this.rols = rols;
	}

	public List<RightModel> getChilds() {
		return childs;
	}

	public void setChilds(List<RightModel> childs) {
		this.childs = childs;
	}

	@Override
	public String Key() {
		return this.id;
	}

}
