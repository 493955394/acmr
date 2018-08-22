package com.acmr.model.property;

import com.acmr.model.templatecenter.FilterInfo;

public class ProEdit {

	private String keycode;

	private String attrType;//固定值 filter
	public String getKeycode() {
		return keycode;
	}
	public void setKeycode(String keycode) {
		this.keycode = keycode;
	}
	private String fixedName;//固定值
	private FilterInfo filterInfo;//filter
	public String getAttrType() {
		return attrType;
	}
	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}
	public String getFixedName() {
		return fixedName;
	}
	public void setFixedName(String fixedName) {
		this.fixedName = fixedName;
	}
	public FilterInfo getFilterInfo() {
		return filterInfo;
	}
	public void setFilterInfo(FilterInfo filterInfo) {
		this.filterInfo = filterInfo;
	}
}
