package com.acmr.helper.util.download;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;

public class StyleSyncer {
	public static void syncStyle(Cell cell, PdfPCell pdfcell){
		syncAlign(cell,pdfcell);
		//background(cell,pdfcell);
		font(cell, pdfcell);
	}
	public static void syncAlign(Cell cell, PdfPCell pdfcell){
		CellStyle style = cell.getCellStyle();
		switch (style.getAlignment()) {
		case CellStyle.ALIGN_GENERAL:
			pdfcell.setHorizontalAlignment(PdfPCell.ALIGN_UNDEFINED);
			break;
		case CellStyle.ALIGN_LEFT:
			pdfcell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			break;
		case CellStyle.ALIGN_CENTER:
			pdfcell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			break;
		case CellStyle.ALIGN_RIGHT:
			pdfcell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			break;
		case CellStyle.ALIGN_FILL:
			pdfcell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED_ALL);
			break;
		case CellStyle.ALIGN_JUSTIFY:
			pdfcell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
			break;
		case CellStyle.ALIGN_CENTER_SELECTION:
			pdfcell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			break;
		default:
			break;
		}
		switch (style.getVerticalAlignment()) {
		case CellStyle.VERTICAL_BOTTOM:
			pdfcell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
			break;
		case CellStyle.VERTICAL_CENTER:
			pdfcell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			break;
		case CellStyle.VERTICAL_JUSTIFY:
			pdfcell.setVerticalAlignment(PdfPCell.ALIGN_BASELINE);
			break;
		case CellStyle.VERTICAL_TOP:
			pdfcell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
			break;
		default:
			break;
		}
	}
	
	public static void background(Cell cell, PdfPCell pdfcell){
		CellStyle style = cell.getCellStyle();
		HSSFColor color= (HSSFColor)style.getFillForegroundColorColor();
		short[] hex = color.getTriplet();
		if("0:0:0".equals(color.getHexString())){
			hex[0]=255;
			hex[1]=255;
			hex[2]=255;
		}
		BaseColor baseColor = new BaseColor(hex[0], hex[1], hex[2]);
		pdfcell.setBackgroundColor(baseColor);
	}
	public static void font(Cell cell, PdfPCell pdfcell){
		CellStyle style = cell.getCellStyle();
		org.apache.poi.ss.usermodel.Font font = cell.getSheet().getWorkbook().getFontAt(style.getFontIndex());
		Font pdfFont = pdfcell.getPhrase().getFont();
		//set color
		pdfFont.setColor(createdColor(font));
		//set style
		pdfFont.setStyle(createdFontStyle(font));
		//set size
		pdfFont.setSize(font.getFontHeightInPoints());
	}
	
	//根据font转换为pdf的颜色
	private static BaseColor createdColor(org.apache.poi.ss.usermodel.Font font){
		//int colorIndex = font.getColor();
		//HSSFColor color	= HSSFColor.getIndexHash().get(colorIndex);
		//short[] hex = color.getTriplet();
		BaseColor baseColor = new BaseColor(0, 0, 0);
		return baseColor;
	}
	//根据font转换为pdf的font
	private static int createdFontStyle(org.apache.poi.ss.usermodel.Font font){
		int style = Font.NORMAL;
		switch (font.getBoldweight()) {
		case org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD:
			style = Font.BOLD;
			if(font.getItalic()){
				style |= Font.ITALIC;
			}
			if(font.getStrikeout()){
				style |= Font.STRIKETHRU;
			}
			if(font.getUnderline() != org.apache.poi.ss.usermodel.Font.U_NONE){
				style |= Font.UNDERLINE;
			}
		case org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_NORMAL:
			style = Font.NORMAL;
			if(font.getItalic()){
				style |= Font.ITALIC;
			}
			if(font.getStrikeout()){
				style |= Font.STRIKETHRU;
			}
			if(font.getUnderline() != org.apache.poi.ss.usermodel.Font.U_NONE){
				style |= Font.UNDERLINE;
			}
		}
		return style;
	} 
}
