package com.acmr.helper.util.download;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class WorkBookStruct {
	private Workbook workBook;
	private Sheet[] sheets = null;
	private String[] sheetNames = null;
	public WorkBookStruct(Workbook wb){
		if(wb == null){
			throw new RuntimeException("工作簿不能为空！");
		}
		this.workBook = wb;
		int sheetNum = workBook.getNumberOfSheets();
		sheets = new Sheet[sheetNum];
		sheetNames = new String[sheetNum];
		for(int i = 0; i < sheetNum; i++){
			sheets[i] = wb.getSheetAt(i);
			sheetNames[i] = sheets[i].getSheetName();
		}
	}
	public Workbook getWorkBook() {
		return workBook;
	}
	public void setWorkBook(Workbook workBook) {
		this.workBook = workBook;
	}
	public Sheet[] getSheets() {
		return sheets;
	}
	public void setSheets(Sheet[] sheets) {
		this.sheets = sheets;
	}
	public String[] getSheetNames() {
		return sheetNames;
	}
	public void setSheetNames(String[] sheetNames) {
		this.sheetNames = sheetNames;
	}
	
}
