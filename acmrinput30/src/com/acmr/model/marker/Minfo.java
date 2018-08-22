package com.acmr.model.marker;

import com.acmr.helper.util.StringUtil;
import com.acmr.model.range.Pinfo;

public class Minfo {

	private String title; // 标题

	private String zhu; // 主栏

	private String bin; // 宾栏

	private String unit; // 单位

	private String exps; // 备注

	public String getTitle() {
		return title;
	}

	public void setTitle(Pinfo ainfo) {
		StringBuffer sb = new StringBuffer();
		sb.append("CELL[");
		sb.append(ainfo.getColStartIndex());
		sb.append(ainfo.getRowStartIndex());
		sb.append(":");
		sb.append(ainfo.getColEndIndex());
		sb.append(ainfo.getRowEndIndex());
		sb.append("]");
		this.title = sb.toString(); 
				//+ "==A1";
	}

	public String getZhu() {
		return zhu;
	}

	public void setZhu(Pinfo ainfo) {
		StringBuffer sb = new StringBuffer();
		sb.append("R[");
		sb.append(ainfo.getRowStartIndex() + ":" + ainfo.getRowEndIndex());
		if (!StringUtil.isEmpty(ainfo.getColStartIndex())) {
			sb.append(",STARTPOS:" + ainfo.getColStartIndex());
		}
		if (!StringUtil.isEmpty(ainfo.getStartColText())) {
			sb.append(",STARTTEXT:" + ainfo.getStartColText());
		}
		if (!StringUtil.isEmpty(ainfo.getColEndIndex())) {
			sb.append(",ENDPOS:" + ainfo.getColEndIndex());
		}
		if (!StringUtil.isEmpty(ainfo.getEndColText())) {
			sb.append(",ENDTEXT:" + ainfo.getEndColText());
		}
		sb.append("]");
		this.zhu = sb.toString();
				//+ "==B1";
	}

	public String getBin() {
		return bin;
	}

	public void setBin(Pinfo ainfo) {
		StringBuffer sb = new StringBuffer();
		sb.append("C[");
		sb.append(ainfo.getColStartIndex() + ":" + ainfo.getColEndIndex());
		if (!StringUtil.isEmpty(ainfo.getRowStartIndex())) {
			sb.append(",STARTPOS:" + ainfo.getRowStartIndex());
		}
		if (!StringUtil.isEmpty(ainfo.getStartRowText())) {
			sb.append(",STARTTEXT:" + ainfo.getStartRowText());
		}
		if (!StringUtil.isEmpty(ainfo.getRowEndIndex())) {
			sb.append(",ENDPOS:" + ainfo.getRowEndIndex());
		}
		if (!StringUtil.isEmpty(ainfo.getEndRowText())) {
			sb.append(",ENDTEXT:" + ainfo.getEndRowText());
		}
		sb.append("]");
		this.bin = sb.toString() ;
		//+ "==C1";
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(Pinfo ainfo) {
		StringBuffer sb = new StringBuffer();
		sb.append("CELL[");
		sb.append(ainfo.getColStartIndex());
		sb.append(ainfo.getRowStartIndex());
		sb.append(":");
		sb.append(ainfo.getColEndIndex());
		sb.append(ainfo.getRowEndIndex());
		sb.append("]");
		this.unit = sb.toString() ;
		//+ "==D1";
	}

	public String getExps() {
		return exps;
	}

	public void setExps(Pinfo ainfo) {
		StringBuffer sb = new StringBuffer();
		sb.append("CELL[");
		sb.append(ainfo.getColStartIndex());
		sb.append(ainfo.getRowStartIndex());
		sb.append(":");
		sb.append(ainfo.getColEndIndex());
		sb.append(ainfo.getRowEndIndex());
		sb.append("]");
		this.exps = sb.toString() ;
		//+ "==G1";
	}

}
