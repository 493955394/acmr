package com.acmr.service.metadata;

import java.util.HashMap;
import java.util.Map;

import acmr.excel.pojo.ExcelCellStyle;
import acmr.excel.pojo.ExcelColor;

public class MetaExcelColor {

	private static Map<String, ExcelCellStyle> color = new HashMap<String, ExcelCellStyle>();

	static {
		// 特殊背景 红色不能为空
		ExcelCellStyle red = new ExcelCellStyle();
		red.setFgcolor(new ExcelColor("FF0000"));
		red.setBgcolor(new ExcelColor("FF0000"));
		red.setPattern((short) 1);
		color.put("red", red);

		// 特殊背景 黄色表示已存在
		ExcelCellStyle yellow = new ExcelCellStyle();
		yellow.setFgcolor(new ExcelColor("FFFF00"));
		yellow.setBgcolor(new ExcelColor("FFFF00"));
		yellow.setPattern((short) 1);
		color.put("yellow", yellow);

		// 特殊背景 绿色表示在底库中已存在
		ExcelCellStyle green = new ExcelCellStyle();
		green.setFgcolor(new ExcelColor("006600"));
		green.setBgcolor(new ExcelColor("006600"));
		green.setPattern((short) 1);
		color.put("green", green);

		// 特殊背景 粉色表示在格式不正确
		ExcelCellStyle pink = new ExcelCellStyle();
		pink.setFgcolor(new ExcelColor("EE6EC0"));
		pink.setBgcolor(new ExcelColor("EE6EC0"));
		pink.setPattern((short) 1);
		color.put("pink", pink);

		// 特殊背景蓝色表示在有死循环，或者procode不存在
		ExcelCellStyle blue = new ExcelCellStyle();
		blue.setFgcolor(new ExcelColor("0070C0"));
		blue.setBgcolor(new ExcelColor("0070C0"));
		blue.setPattern((short) 1);
		color.put("blue", blue);
	}

	public static ExcelCellStyle getColor(String c) {
		return color.get(c);
	}

}
