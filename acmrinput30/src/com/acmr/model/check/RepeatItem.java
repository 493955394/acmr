package com.acmr.model.check;

public class RepeatItem implements Comparable<RepeatItem> {
	private String code;
	private int rowid;

	public RepeatItem(String code1, int id1) {
		code = code1;
		rowid = id1;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getRowid() {
		return rowid;
	}

	public void setRowid(int rowid) {
		this.rowid = rowid;
	}

	@Override
	public int compareTo(RepeatItem o) {
		int int1= this.code.compareTo(o.code);
		if(int1==0){
			int1=this.rowid-o.rowid;
		}
		return int1;
	}

}
