package com.acmr.helper.util;

import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelUtils {
	public static boolean isExcel2003(InputStream is) {
	    try {
	      new HSSFWorkbook(is);
	    } catch (Exception e) {
	      return false;
	    }
	    return true;
	  }
	public static int getPageHeight(int height) {
		return Math.round(height / 20) + 7;
	}
	public static int getPageWidth(int widthUnits) {
		int pixels = widthUnits / 256 * 7;

		int offsetWidthUnits = widthUnits % 256;

		pixels = pixels + Math.round(offsetWidthUnits / 36.57143F);

		return pixels;
	}
}
