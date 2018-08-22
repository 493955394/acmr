package com.acmr.model.FreeQuery;

public class CellItem {

	private String code;
	private String cname;

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

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	private boolean select;
	
	public CellItem(String code,String cname,boolean select){
		this.code=code;
		this.cname=cname;
		this.select=select;
	}
}
