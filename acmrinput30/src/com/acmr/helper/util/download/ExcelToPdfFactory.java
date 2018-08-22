package com.acmr.helper.util.download;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;

public class ExcelToPdfFactory {

	public static void execute(Workbook wb, OutputStream os, ByteArrayOutputStream photo) {
		new ExcelToPdfFactory().convert(wb, os, photo);
	}

	public void convert(Workbook wb, OutputStream os, ByteArrayOutputStream photo) {
		// Workbook wb = createdWorkBook(srcPath);
		WorkBookStruct wbStruct = new WorkBookStruct(wb);
		PdfBuilder builder = new PdfBuilder(os);
		builder.buildDocument();
		// 只处理第一个sheet
		// for(Sheet sheet : wbStruct.getSheets()){
		SheetStruct sheetStruct = new SheetStruct(wbStruct.getSheets()[0]);
		builder.buildTable(sheetStruct);
		builder.build(photo);
	}
}
