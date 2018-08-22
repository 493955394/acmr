package com.acmr.model.security;

import acmr.util.IKeyible;

public class SortedCode implements IKeyible, Comparable<SortedCode> {

	private String code;
	private String sortcode;

	public SortedCode() {
		code = "";
		sortcode = "";
	}

	public SortedCode(String c, String s) {
		code = c;
		sortcode = s;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	@Override
	public int compareTo(SortedCode o) {
		if (o == null) {
			return -1;
		}
		if (this.sortcode == null || o.sortcode == null) {
			return -1;
		}
		return this.sortcode.compareTo(o.sortcode);
	}

}
