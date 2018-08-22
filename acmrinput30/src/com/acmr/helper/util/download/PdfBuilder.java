package com.acmr.helper.util.download;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfBuilder {
	private OutputStream os;
	private Document doc;
	private PdfWriter writer;
	private float totolwidth = 0;
	private List<PdfPTable> tables = new ArrayList<PdfPTable>();
	private PdfPageSetting setting = new PdfPageSetting();;

	public PdfBuilder(OutputStream os) {
		if (os == null) {
			throw new RuntimeException("输出流os不能为空");
		}
		this.os = os;
	}

	public PdfBuilder buildDocument() {
		doc = new Document();
		try {
			writer = PdfWriter.getInstance(doc, os);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return this;
	}

	public PdfBuilder buildTable(SheetStruct sheet) {
		// 取出表的列
		int colNum = sheet.colNum() + 1;
		float[] ratio = new float[colNum];
		for (int i = 0; i < ratio.length; i++) {
			ratio[i] = 1f / colNum;
			// 计算宽度
			int number = widthUnits2Pixel(sheet.getSheet().getColumnWidth(i));
			if (number < 50) {
				number *= 1.5;
			}
			totolwidth += number;
		}
		if (totolwidth < 595F) {
			totolwidth = 595F;
		}
		ratio[colNum - 1] = 0.01f;
		// 创x列的表格
		PdfPTable table = new PdfPTable(ratio);
		int cellNum = colNum * sheet.rowNum();
		for (int i = 0; i < cellNum; i++) {
			PdfPCell pdfcell = new PdfPCell(ParaFactory.size8(" "));
			pdfcell.setBorder(0);// border
			table.addCell(pdfcell);
		}
		Iterator<Row> rows = sheet.getSheet().rowIterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			Iterator<Cell> cells = row.cellIterator();
			while (cells.hasNext()) {
				Cell cell = cells.next();
				if (sheet.isBeMeger(cell)) {
					table.getRow(cell.getRowIndex()).getCells()[cell.getColumnIndex()] = null;
					continue;
				}
				CellCoordinate index = new CellCoordinate(cell.getRowIndex(), cell.getColumnIndex());
				// cell.setCellType(Cell.CELL_TYPE_STRING);
				boolean flag = false;
				Cell startCell = null;
				Cell endCell = null;
				if (sheet.getMergeCell().containsKey(index)) {
					CellRangeAddress cra = sheet.getMergeCell().get(index);
					int colSpan = cra.getLastColumn() - cra.getFirstColumn() + 1;
					int rowSpan = cra.getLastRow() - cra.getFirstRow() + 1;
					PdfPCell pdfcell = table.getRow(cra.getFirstRow()).getCells()[cra.getFirstColumn()];
					startCell = sheet.getSheet().getRow(cra.getFirstRow()).getCell(cra.getFirstColumn());
					endCell = sheet.getSheet().getRow(cra.getLastRow()).getCell(cra.getLastColumn());
					pdfcell.setColspan(colSpan);
					pdfcell.setRowspan(rowSpan);
					flag = true;
					// pdfcell.setBorder(0);//border
					// buildBorders(pdfcell, cell);
				}
				// String val = cell.getStringCellValue();
				String val = ParaFactory.getCellValue(cell);
				PdfPCell pdfcell = table.getRow(cell.getRowIndex()).getCells()[cell.getColumnIndex()];
				pdfcell.setPhrase(ParaFactory.size8(val));
				buildBorders(pdfcell, cell, sheet.getSheet(), flag, startCell, endCell);
				StyleSyncer.syncStyle(cell, pdfcell);
			}
		}
		// for (PdfPRow row : table.getRows()) {
		// PdfPCell pdfcell = row.getCells()[table.getNumberOfColumns() - 1];
		// pdfcell.setBorder(PdfPCell.NO_BORDER);
		// }
		// 表格宽度百分比
		table.setWidthPercentage(100);
		tables.add(table);
		return this;
	}

	private void buildBorders(PdfPCell pdfcell, Cell cell, Sheet sheet, boolean flag, Cell startCell, Cell endCell) {
		CellStyle style = cell.getCellStyle();
		// 合并单元格
		if (flag) {
			// System.out.print(DataFormat.getCellValue(cell));
			CellStyle s = startCell.getCellStyle();

			short borderTop = s.getBorderTop();
			borderTop = (borderTop != 0 && borderTop != 1) ? 1 : borderTop;
			pdfcell.setBorderWidthTop(borderTop);

			short borderLeft = s.getBorderLeft();
			borderLeft = (borderLeft != 0 && borderLeft != 1) ? 1 : borderLeft;
			pdfcell.setBorderWidthLeft(borderLeft);
			CellStyle e = cell.getCellStyle();
			if (null != endCell) {
				e = endCell.getCellStyle();
			}
			short borderRight = e.getBorderRight();
			borderRight = (borderRight != 0 && borderRight != 1) ? 1 : borderRight;
			pdfcell.setBorderWidthRight(borderRight);

			short borderBottom = e.getBorderBottom();
			borderBottom = (borderBottom != 0 && borderBottom != 1) ? 1 : borderBottom;
			pdfcell.setBorderWidthBottom(borderBottom);
			// System.out.println(" " + borderTop + "-" + borderBottom + "-" + borderLeft + "-" + borderRight);
		} else {
			short borderTop = style.getBorderTop();
			borderTop = (borderTop != 0 && borderTop != 1) ? 1 : borderTop;
			pdfcell.setBorderWidthTop(borderTop);

			short borderLeft = style.getBorderLeft();
			borderLeft = (borderLeft != 0 && borderLeft != 1) ? 1 : borderLeft;
			pdfcell.setBorderWidthLeft(borderLeft);

			short borderRight = style.getBorderRight();
			borderRight = (borderRight != 0 && borderRight != 1) ? 1 : borderRight;
			pdfcell.setBorderWidthRight(borderRight);

			short borderBottom = style.getBorderBottom();
			borderBottom = (borderBottom != 0 && borderBottom != 1) ? 1 : borderBottom;
			pdfcell.setBorderWidthBottom(borderBottom);
		}
		// if (!flag) {
		// pdfcell.disableBorderSide(2); // 隐藏下右边框
		// pdfcell.disableBorderSide(8);
		// }
		// System.out.print(DataFormat.getCellValue(cell));
		// System.out.println(" " + borderTop + "-" + borderBottom + "-" + borderLeft + "-" + borderRight);
	}

	public PdfBuilder buildPageSetting(float marginLeft, float marginRight, float marginTop, float marginBottom) {
		setting.setMarginLeft(marginLeft);
		setting.setMarginRight(marginRight);
		setting.setMarginTop(marginTop);
		setting.setMarginBottom(marginBottom);
		return this;
	}

	public PdfBuilder buildPageSetting(Rectangle pageSize) {
		setting.setPageSize(pageSize);
		return this;
	}

	protected int widthUnits2Pixel(int widthUnits) {
		int pixels = (widthUnits / 256) * 7;
		int offsetWidthUnits = widthUnits % 256;
		pixels += Math.round((float) offsetWidthUnits / 36.57143F);
		return pixels;
	}

	public PdfBuilder build(ByteArrayOutputStream photo) {
		// doc.setPageSize(setting.getPageSize());
		doc.setPageSize(new Rectangle(totolwidth, 842F));

		// 将多出的一行多出的0.3毫米转化为像素
		float left = (float) (0.3 * 72 / 2.54);
		doc.setMargins(setting.getMarginLeft(), setting.getMarginRight() - left, setting.getMarginTop(), setting.getMarginBottom());
		doc.setPageCount(20);
		doc.open();
		try {
			// 添加图片
			if (photo != null && photo.size() > 0) {
				Image img = Image.getInstance(photo.toByteArray());
				img.setAlignment(1); // 居中
				img.scalePercent(80);// 图片比例
				// img.scaleAbsolute(100, 100);// 控制图片大小
				// img.setAbsolutePosition(0, 200);// 控制图片位置
				doc.add(img);
			}
			for (PdfPTable table : tables) {
				doc.add(table);
				doc.newPage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		doc.close();
		return this;
	}

	public OutputStream getOs() {
		return os;
	}

	public void setOs(OutputStream os) {
		this.os = os;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public PdfWriter getWriter() {
		return writer;
	}

	public void setWriter(PdfWriter writer) {
		this.writer = writer;
	}

	public List<PdfPTable> getTables() {
		return tables;
	}

	public void setTables(List<PdfPTable> tables) {
		this.tables = tables;
	}

	public PdfPageSetting getSetting() {
		return setting;
	}

	public void setSetting(PdfPageSetting setting) {
		this.setting = setting;
	}

	public float getTotolwidth() {
		return totolwidth;
	}

	public void setTotolwidth(float totolwidth) {
		this.totolwidth = totolwidth;
	}

}
