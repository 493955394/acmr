package com.acmr.helper.util.download;

public class CellCoordinate {
	private int rowIndex;
	private int colIndex;

	public CellCoordinate(int rowIndex, int colIndex) {
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getColIndex() {
		return colIndex;
	}

	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CellCoordinate) {
			CellCoordinate o = (CellCoordinate) obj;
			if (o.colIndex == colIndex && o.rowIndex == rowIndex) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Integer.parseInt(rowIndex + "0" + colIndex);
	}
}