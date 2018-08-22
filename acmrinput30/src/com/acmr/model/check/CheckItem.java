package com.acmr.model.check;

import java.util.HashMap;
import java.util.Map;


public class CheckItem {

	private Map<String,CheckWdCodeName> wds;
	private String celladdr;
	private String unitcode;
	private String unitname;
	private double data;

	private String dbunitcode;
	private String dbunitname;
	private double dbdata;
	private String result;

	private String c1;
	private String c2;
	private String c3;
	private String c4;
	private String c5;

	public CheckItem() {
		wds = new HashMap<String,CheckWdCodeName>();
	}

	public Map<String, CheckWdCodeName>  getWds() {
		return wds;
	}

	public void addwd(String code,CheckWdCodeName e) {
		this.wds.put(code, e);
	}

	public String getUnitcode() {
		return unitcode;
	}

	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public double getData() {
		return data;
	}

	public void setData(double data) {
		this.data = data;
	}

	public String getDbunitcode() {
		return dbunitcode;
	}

	public void setDbunitcode(String dbunitcode) {
		this.dbunitcode = dbunitcode;
	}

	public String getDbunitname() {
		return dbunitname;
	}

	public void setDbunitname(String dbunitname) {
		this.dbunitname = dbunitname;
	}

	public double getDbdata() {
		return dbdata;
	}

	public void setDbdata(double dbdata) {
		this.dbdata = dbdata;
	}

	public String getC1() {
		return c1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}

	public String getC2() {
		return c2;
	}

	public void setC2(String c2) {
		this.c2 = c2;
	}

	public String getC3() {
		return c3;
	}

	public void setC3(String c3) {
		this.c3 = c3;
	}

	public String getC4() {
		return c4;
	}

	public void setC4(String c4) {
		this.c4 = c4;
	}

	public String getC5() {
		return c5;
	}

	public void setC5(String c5) {
		this.c5 = c5;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getCelladdr() {
		return celladdr;
	}

	public void setCelladdr(String celladdr) {
		this.celladdr = celladdr;
	}

}
