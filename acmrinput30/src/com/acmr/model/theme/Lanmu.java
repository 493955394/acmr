package com.acmr.model.theme;

public class Lanmu {

	private String code;
	private String dbcode;
	private String sort;
	private String name;

	public Lanmu() {
		// TODO Auto-generated constructor stub
	}

	public Lanmu(String code, String dbcode, String sort, String name) {
		super();
		this.code = code;
		this.dbcode = dbcode;
		this.sort = sort;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDbcode() {
		return dbcode;
	}

	public void setDbcode(String dbcode) {
		this.dbcode = dbcode;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
