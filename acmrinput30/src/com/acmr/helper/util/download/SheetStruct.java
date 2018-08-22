package com.acmr.helper.util.download;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class SheetStruct {
	private Sheet sheet = null;
	private Map<CellCoordinate, CellRangeAddress> mergeCell;

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public Map<CellCoordinate, CellRangeAddress> getMergeCell() {
		return mergeCell;
	}

	public void setMergeCell(Map<CellCoordinate, CellRangeAddress> mergeCell) {
		this.mergeCell = mergeCell;
	}

	public SheetStruct(Sheet sheet) {
		if (sheet == null) {
			throw new RuntimeException("工作表不能为空！");
		}
		this.sheet = sheet;
		mergeCell = initMegerCell(sheet);
	}

	private Map<CellCoordinate, CellRangeAddress> initMegerCell(Sheet sheet) {
		mergeCell = new HashMap<CellCoordinate, CellRangeAddress>();
		int num = sheet.getNumMergedRegions();
		for (int i = 0; i < num; i++) {
			CellRangeAddress cra = sheet.getMergedRegion(i);
			CellCoordinate index = new CellCoordinate(cra.getFirstRow(), cra.getFirstColumn());
			mergeCell.put(index, cra);
		}
		return mergeCell;
	}

	public boolean isBeMeger(Cell cell) {
		Set<CellCoordinate> indexs = mergeCell.keySet();
		for (CellCoordinate index : indexs) {
			CellRangeAddress cra = mergeCell.get(index);
			int cellRow = cell.getRowIndex();
			int cellCol = cell.getColumnIndex();
			int craFirstRow = cra.getFirstRow();
			int craLastRow = cra.getLastRow();
			int craFirstCol = cra.getFirstColumn();
			int craLastCol = cra.getLastColumn();
			if (cellRow >= craFirstRow && cellRow <= craLastRow && cellCol >= craFirstCol && cellCol <= craLastCol) {
				if (cellRow == craFirstRow && cellCol == craFirstCol) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	public boolean isRowBeMeger(Cell cell) {
		Set<CellCoordinate> indexs = mergeCell.keySet();
		for (CellCoordinate index : indexs) {
			CellRangeAddress cra = mergeCell.get(index);
			int cellRow = cell.getRowIndex();
			int cellCol = cell.getColumnIndex();
			int craFirstRow = cra.getFirstRow();
			int craLastRow = cra.getLastRow();
			int craFirstCol = cra.getFirstColumn();
			int craLastCol = cra.getLastColumn();
			if (cellRow >= craFirstRow && cellRow <= craLastRow && cellCol >= craFirstCol && cellCol <= craLastCol) {
				if (cellRow == craFirstRow && cellCol == craFirstCol) {
					return false;
				}
				if (cellRow >= craFirstRow && cellRow <= craLastRow) {
					return true;
				}
				return false;
			}
		}
		return false;
	}

	public boolean isColBeMeger(Cell cell) {
		Set<CellCoordinate> indexs = mergeCell.keySet();
		for (CellCoordinate index : indexs) {
			CellRangeAddress cra = mergeCell.get(index);
			int cellRow = cell.getRowIndex();
			int cellCol = cell.getColumnIndex();
			int craFirstRow = cra.getFirstRow();
			int craLastRow = cra.getLastRow();
			int craFirstCol = cra.getFirstColumn();
			int craLastCol = cra.getLastColumn();
			if (cellRow >= craFirstRow && cellRow <= craLastRow && cellCol >= craFirstCol && cellCol <= craLastCol) {
				if (cellRow == craFirstRow && cellCol == craFirstCol) {
					return false;
				}
				if (cellCol >= craFirstCol && cellCol <= craLastCol) {
					return true;
				}
				return false;
			}
		}
		return false;
	}

	public int colNum() {
		Iterator<Row> rows = sheet.iterator();
		int colNum = 0;
		while (rows.hasNext()) {
			Row row = rows.next();
			int rowColNum = row.getLastCellNum();
			if (rowColNum > colNum) {
				colNum = rowColNum;
			}
		//	System.out.println(colNum);
			// int rowColNum = 0;
			// Row row = rows.next();
			// Iterator<Cell> cols = row.iterator();
			// // System.out.println(lastCellNum);
			// // System.out.println(lastCellNum);
			// // System.out.println(lastCellNum);
			// while(cols.hasNext()){
			// // short lastCellNum = row.getLastCellNum();
			// cols.next();
			// rowColNum++;
			// }
			// if(rowColNum > colNum){
			// colNum = rowColNum;
			// }
		}
		return colNum;
	}

	public int rowNum() {
		return sheet.getLastRowNum() + 1;
		//return sheet.getLastRowNum() - sheet.getFirstRowNum() + 1;
	}
}
