package com.acmr.model.templatecenter;

import acmr.excel.pojo.ExcelSheet;

public class ExcelResult {

	private int code;
	private ExcelSheet sheet;

	public ExcelResult() {
		// TODO Auto-generated constructor stub
	}

	public ExcelResult(int code) {
		this.code = code;
	}

	public ExcelResult(int code, ExcelSheet sheet) {
		super();
		this.code = code;
		this.sheet = sheet;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ExcelSheet getSheet() {
		return sheet;
	}

	public void setSheet(ExcelSheet sheet) {
		this.sheet = sheet;
	}

}
