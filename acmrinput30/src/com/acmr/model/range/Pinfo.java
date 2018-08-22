package com.acmr.model.range;

public class Pinfo {
	/**
	 * 位置类型 cell，行，列
	 */
	private String positionType;// cell,c,r

	/**
	 * 起始行坐标
	 */
	private String rowStartIndex;
	/**
	 * 结束行坐标
	 */
	private String rowEndIndex;
	/**
	 * 开始列坐标
	 */
	private String colStartIndex;
	/**
	 * 结束列坐标
	 */
	private String colEndIndex;
	/**
	 * 行开始文本
	 */
	private String startRowText;
	/**
	 * 行结束文本
	 */
	private String endRowText;
	/**
	 * 列开始文本
	 */
	private String startColText;
	/**
	 * 列结束文本
	 */
	private String endColText;

	/**
	 * 开始类型 0 1
	 */
	private String stype;

	private String svalue;

	/**
	 * 结束类型 0 1
	 */
	private String etype;

	private String evalue;

	/**
	 * 前台表达式
	 */
	private String expression;

	public String getPositionType() {
		return positionType;
	}

	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	public String getSvalue() {
		return svalue;
	}

	public void setSvalue(String svalue) {
		this.svalue = svalue;
	}

	public String getEtype() {
		return etype;
	}

	public void setEtype(String etype) {
		this.etype = etype;
	}

	public String getEvalue() {
		return evalue;
	}

	public void setEvalue(String evalue) {
		this.evalue = evalue;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	public String getRowStartIndex() {
		return rowStartIndex;
	}

	public void setRowStartIndex(String rowStartIndex) {
		this.rowStartIndex = rowStartIndex;
	}

	public String getRowEndIndex() {
		return rowEndIndex;
	}

	public void setRowEndIndex(String rowEndIndex) {
		this.rowEndIndex = rowEndIndex;
	}

	public String getColStartIndex() {
		return colStartIndex;
	}

	public void setColStartIndex(String colStartIndex) {
		this.colStartIndex = colStartIndex;
	}

	public String getColEndIndex() {
		return colEndIndex;
	}

	public void setColEndIndex(String colEndIndex) {
		this.colEndIndex = colEndIndex;
	}

	public String getStartRowText() {
		return startRowText;
	}

	public void setStartRowText(String startRowText) {
		this.startRowText = startRowText;
	}

	public String getEndRowText() {
		return endRowText;
	}

	public void setEndRowText(String endRowText) {
		this.endRowText = endRowText;
	}

	public String getStartColText() {
		return startColText;
	}

	public void setStartColText(String startColText) {
		this.startColText = startColText;
	}

	public String getEndColText() {
		return endColText;
	}

	public void setEndColText(String endColText) {
		this.endColText = endColText;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

}
