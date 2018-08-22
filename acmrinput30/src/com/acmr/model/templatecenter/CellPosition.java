package com.acmr.model.templatecenter;

/**
 * 单元格对象
 * 
 * @author chenyf
 *
 */
public class CellPosition {
	private String row; // 行
	private String col; // 列
	private String value; // 值

	public CellPosition() {
		// TODO Auto-generated constructor stub
	}

	public CellPosition(String row, String col, String value) {
		this.row = row;
		this.col = col;
		this.value = value;
	}

	@Override
	public String toString() {
		return "CellPosition [row=" + row + ", col=" + col + ", value=" + value + "]";
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public String getCol() {
		return col;
	}

	public void setCol(String col) {
		this.col = col;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
