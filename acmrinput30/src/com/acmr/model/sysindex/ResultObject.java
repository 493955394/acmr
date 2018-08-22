package com.acmr.model.sysindex;

public class ResultObject {
	private boolean flag; // 状态
	private String code; // code
	private String err; // 错误信息

	public ResultObject() {
		// TODO Auto-generated constructor stub
	}

	public ResultObject(boolean flag, String code, String err) {
		this.flag = flag;
		this.code = code;
		this.err = err;
	}

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getErr() {
		return err;
	}

	public void setErr(String err) {
		this.err = err;
	}

}
