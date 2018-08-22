package com.acmr.model.security;

import java.util.ArrayList;
import java.util.List;

import acmr.util.IKeyible;
import acmr.util.ListHashMap;
import acmr.util.SortedList;

public class Menu implements IKeyible, Comparable<Menu> {

	private String code;
	private String cname;
	private String sort;
	private String href;
	private String target;

	private String sortcode;

	private String parent;
	private ListHashMap<SortedCode> childs;

	public Menu() {
		code = "";
		cname = "";
		sort = "";
		href = "";
		target = "";
		sortcode = "";
		parent = "";
		childs = new ListHashMap<SortedCode>();

	}

	public Menu Copy() {
		Menu o = new Menu();
		o.code = code;
		o.cname = cname;
		o.sort = sort;
		o.href = href;
		o.target = target;
		o.sortcode = sortcode;
		o.parent = parent;
		return o;
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

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
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

	public List<String> getChilds() {
		SortedList<SortedCode> list1 = new SortedList<SortedCode>();
		list1.addAll(childs);
		List<String> strlist1 = new ArrayList<String>();
		for (int i = 0; i < list1.size(); i++) {
			strlist1.add(list1.get(i).getCode());
		}
		return strlist1;
	}

	public void addChild(String code, String sortcode) {
		if (!childs.containsKey(code)) {
			childs.add(new SortedCode(code, sortcode));
		}
	}

	@Override
	public String Key() {
		return code;
	}

	@Override
	public int compareTo(Menu o) {
		if (o == null) {
			return -1;
		}
		return this.sortcode.compareTo(o.sortcode);
	}

}
