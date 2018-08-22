package com.acmr.model.templatecenter;

public class PositionInfo {
	/**
	 * 位置类型 cell，行，列
	 */
	private String positionType;//cell,c,r

	/**
	 * 起始行坐标
	 */
	private int rowStartIndex;
	/**
	 * 结束行坐标
	 */
	private int rowEndIndex;
	/**
	 * 开始列坐标
	 */
	private int colStartIndex;
	/**
	 * 结束列坐标
	 */
	private int colEndIndex;
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
	 * 前台表达式
	 */
	private String expression;
	
	/**
	 * 别名位置信息
	 */
	private IndexPositionInfo indexPos;
	
	public IndexPositionInfo getIndexPos() {
		return indexPos;
	}

	public void setIndexPos(IndexPositionInfo indexPos) {
		this.indexPos = indexPos;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
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

	
	public int getRowStartIndex() {
		return rowStartIndex;
	}

	public void setRowStartIndex(int rowStartIndex) {
		this.rowStartIndex = rowStartIndex;
	}

	public int getRowEndIndex() {
		return rowEndIndex;
	}

	public void setRowEndIndex(int rowEndIndex) {
		this.rowEndIndex = rowEndIndex;
	}

	public int getColStartIndex() {
		return colStartIndex;
	}

	public void setColStartIndex(int colStartIndex) {
		this.colStartIndex = colStartIndex;
	}

	public int getColEndIndex() {
		return colEndIndex;
	}

	public void setColEndIndex(int colEndIndex) {
		this.colEndIndex = colEndIndex;
	}

	public String getPositionType() {
		return positionType;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}
}
