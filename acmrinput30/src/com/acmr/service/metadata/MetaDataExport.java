package com.acmr.service.metadata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

import com.acmr.helper.constants.Const;
import com.acmr.helper.util.StringUtil;

/**
 * 元数据管理通用数据导出工具
 * 
 * @author chenyf
 *
 */
public class MetaDataExport {

	/**
	 * 
	 * @param req
	 * @param resp
	 * @param list
	 *            数据
	 * @param title
	 *            中文名称
	 * @param field
	 *            导出的列
	 * @param procodeName
	 *            属于哪个分组下的数据
	 * @param searchData
	 *            查询的条件
	 * @param fileName
	 *            文件名
	 * @param hasBgColor
	 *            包含背景色的字段名
	 * @author chenyf
	 */
	public static void export(HttpServletRequest req, HttpServletResponse resp, List<Map<String, Object>> list, List<String> title, List<String> field, String procodeName, String searchData, String fileName) {
		export(req, resp, list, title, field, procodeName, searchData, fileName, null);
	}

	public static void export(HttpServletRequest req, HttpServletResponse resp, List<Map<String, Object>> list, List<String> title, List<String> field, String procodeName, String searchData, String fileName, String hasBgColor) {
		String sheetName = fileName;
		String filename = sheetName + "." + Const.DOWNLOAD_TYPE;
		try {
			// 定义输出类型
			resp.reset();
			resp.setContentType("application/vnd.ms-excel");
			resp.setHeader("Pragma", "public");
			resp.setHeader("Cache-Control", "max-age=30");
			filename = new String(filename.getBytes("gb2312"), "iso8859-1").replace(" ", "");
			resp.setHeader("Content-disposition", "attachment;filename=" + filename);
			// 生成Excel并响应客户端
			if ("xlsx".equals(Const.DOWNLOAD_TYPE)) {
				ServletOutputStream out = resp.getOutputStream();
				ByteArrayOutputStream bos = (ByteArrayOutputStream) getStream(list, title, field, procodeName, searchData, sheetName, hasBgColor);
				Workbook get07ExcelByte = MetaService.get07ExcelByte(bos.toByteArray(), "xls");
				bos.reset();
				get07ExcelByte.write(bos);
				resp.setContentLength(bos.size());
				bos.writeTo(out);
				out.close();
				out.flush();
				bos.close();
				bos.flush();
			} else {
				ServletOutputStream out = resp.getOutputStream();
				ByteArrayOutputStream bos = (ByteArrayOutputStream) getStream(list, title, field, procodeName, searchData, sheetName, hasBgColor);
				resp.setContentLength(bos.size());
				bos.writeTo(out);
				out.close();
				out.flush();
				bos.close();
				bos.flush();
			}
		} catch (IOException | InvalidFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param list
	 *            数据
	 * @param title
	 *            中文名称
	 * @param field
	 *            导出的列
	 * @param procodeName
	 *            属于哪个分组下的数据
	 * @param searchData
	 *            查询的条件
	 * @param fileName
	 *            文件名
	 * @return 背景色
	 * @throws IOException
	 * @author chenyf
	 */
	private static ByteArrayOutputStream getStream(List<Map<String, Object>> list, List<String> title, List<String> field, String procodeName, String searchData, String fileName, String hasBgColor) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建sheet
		HSSFSheet sheet = workbook.createSheet(fileName);
		sheet.setDefaultColumnWidth(1 * 15);
		sheet.setDefaultRowHeight((short) (20 * 20));

		// 创建表头样式
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		// 文本位置(居中)
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 设置字体
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);// 字体大小
		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		// font.setColor(HSSFColor.BLUE.index);//字体颜色
		cellStyle.setFont(font);

		int index = 0;
		// 垂直居中加粗
		HSSFCellStyle style = workbook.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font theadFont = workbook.createFont();
		theadFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		style.setFont(theadFont);
		/*
		 * HSSFCell tTitle = thead.createCell(0, Cell.CELL_TYPE_STRING); HSSFCellStyle tTitleStyle = workbook.createCellStyle(); tTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); tTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); HSSFFont tTitleFont = workbook.createFont(); tTitleFont.setFontHeightInPoints((short) 16); tTitle.setCellStyle(tTitleStyle); tTitleStyle.setFont(tTitleFont); tTitle.setCellValue(title); thead.setHeight((short) 40);
		 */
		// thAlign居中
		HSSFCellStyle thAlign = workbook.createCellStyle();
		thAlign.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 特殊背景
		HSSFCellStyle bg = workbook.createCellStyle();
		bg.setFillForegroundColor(HSSFColor.RED.index);
		bg.setFillPattern(CellStyle.SOLID_FOREGROUND);
		bg.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		/*
		 * // tdFirst居中 HSSFCellStyle firstTdAlign = workbook.createCellStyle(); firstTdAlign.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		 */

		// 数据来源
		if (!StringUtil.isEmpty(procodeName)) {
			HSSFRow th = sheet.createRow(index++);
			HSSFCell td = th.createCell(0);
			td.setCellValue("数据范围：" + procodeName);
			td.setCellStyle(thAlign);
		}
		// 查询条件
		if (!StringUtil.isEmpty(searchData)) {
			HSSFRow th = sheet.createRow(index++);
			HSSFCell td = th.createCell(0);
			td.setCellValue(searchData);
			td.setCellStyle(thAlign);
		}

		HSSFRow th = sheet.createRow(index++);
		for (int i = 0; i < title.size(); i++) {
			HSSFCell td = th.createCell(i);
			td.setCellValue(title.get(i));
			td.setCellStyle(style);
		}

		// 创建tr
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			HSSFRow tr = sheet.createRow(index++);
			for (int j = 0; j < field.size(); j++) {
				// 序号列
				if (j == 0) {
					HSSFCell td = tr.createCell(0);
					td.setCellValue(i + 1);
					td.setCellStyle(thAlign);
				}
				HSSFCell td = tr.createCell(j + 1);
				String f = field.get(j);
				Object value = map.get(f);
				// 判断是否是分类
				if ("ifdata".equals(f)) {
					if (Const.IFDATA.equals(value)) {
						td.setCellValue("指标");
					} else {
						td.setCellValue("分类");
					}
				} else {
					td.setCellValue((String) value);
				}
				if (!StringUtil.isEmpty(hasBgColor) && f.equals(hasBgColor)) {
					td.setCellStyle(bg);
				} else {
					td.setCellStyle(thAlign);
				}
			}
		}
		// 写入字节流
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		return bos;
	}

}
