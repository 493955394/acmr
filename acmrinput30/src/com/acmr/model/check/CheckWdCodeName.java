package com.acmr.model.check;

/**
 * 每个维度值的信息，有维度代码，值代码，值名称
 * @author zengqu
 *
 */
public class CheckWdCodeName {

	private String wdcode;
	private String code;
	private String name;

	public CheckWdCodeName() {
		wdcode = "";
		code = "";
		name = "";
	}

	public CheckWdCodeName(String wdcode1, String code1, String name1) {
		wdcode = wdcode1;
		code = code1;
		name = name1;
	}

	public String getWdcode() {
		return wdcode;
	}

	public void setWdcode(String wdcode) {
		this.wdcode = wdcode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
