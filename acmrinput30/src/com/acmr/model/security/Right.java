package com.acmr.model.security;

import java.util.ArrayList;
import java.util.List;

import acmr.util.IKeyible;

public class Right implements IKeyible, Comparable<Right> {
	private String code;
	private String cname;
	private List<String> menus;
	private String parent;
	private List<String> childs;

	private String sortcode;

	public Right() {
		code = "";
		cname = "";
		parent = "";
		sortcode = "";
		menus = new ArrayList<String>();
		childs = new ArrayList<String>();
	}

	public Right Copy() {
		Right o = new Right();
		o.code = code;
		o.cname = cname;
		o.parent = parent;
		o.sortcode = sortcode;
		for (int i = 0; i < menus.size(); i++) {
			o.addMenu(menus.get(i));
		}
		return o;
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

	public List<String> getMenus() {
		return menus;
	}

	public String getSortcode() {
		return sortcode;
	}

	public void setSortcode(String sortcode) {
		this.sortcode = sortcode;
	}

	@Override
	public String Key() {
		return code;
	}

	public void addChild(String v) {
		if (!childs.contains(v)) {
			childs.add(v);
		}
	}

	public void addMenu(String v) {
		if (!v.equals("") && !menus.contains(v)) {
			menus.add(v);
		}
	}

	public void addMenus(String v, String sp) {
		String s[] = v.split(sp);
		for (int i = 0; i < s.length; i++) {
			addMenu(s[i]);
		}
	}

	@Override
	public int compareTo(Right o) {
		if (o == null) {
			return -1;
		}
		return this.sortcode.compareTo(o.sortcode);
	}
}
