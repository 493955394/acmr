package com.acmr.service.metadata;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

/**
 * 元数据数据格式化工具
 * 
 * @author chenyf
 *
 */
public class DataFormat {
	/**
	 * 获取单元格的值
	 * 
	 * @param cell
	 * @author chenyf
	 */
	public static String getCellValue(Cell cell) {
		String strCell = "";
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_FORMULA:
			try {
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					Date date = (Date) cell.getDateCellValue();
					strCell = formatDate(date);
					break;
				} else {
					try {
						strCell = String.valueOf(cell.getStringCellValue());
					} catch (IllegalStateException e) {
						strCell = String.valueOf(cell.getNumericCellValue());
						String substring = strCell.substring(strCell.indexOf("."));
						if (".0".equals(substring)) {
							strCell = strCell.substring(0, strCell.indexOf("."));
						}
					}
					break;
				}
			} catch (IllegalStateException e) {
				strCell = String.valueOf(cell.getRichStringCellValue());
			}
			break;
		case Cell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				break;
			} else {
				strCell = String.valueOf(cell.getNumericCellValue());
				String substring = strCell.substring(strCell.indexOf("."));
				if (".0".equals(substring)) {
					strCell = strCell.substring(0, strCell.indexOf("."));
				}
				break;
			}
		case Cell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		return strCell;
	}

	/**
	 * 格式化日期
	 * @author chenyf
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:ss:mm");
		return dateformat.format(date);
	}
}
