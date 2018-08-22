package com.acmr.model.dataload;

public class BatchResult {
	private String key;
	private String value;
	private String flag = "0";
	private String state = "";

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public BatchResult() {

	}

	public BatchResult(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public BatchResult(String key, String value, String flag) {
		this.key = key;
		this.value = value;
		this.flag = flag;
	}

}
