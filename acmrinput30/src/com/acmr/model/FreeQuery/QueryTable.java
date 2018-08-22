package com.acmr.model.FreeQuery;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;

public class QueryTable {
	
	
	private List<CellItem> rows;
	private List<CellItem> cols;
	private String[][] data;
	public List<CellItem> getRows() {
		return rows;
	}
	public void setRows(List<CellItem> rows) {
		this.rows = rows;
	}
	public List<CellItem> getCols() {
		return cols;
	}
	public void setCols(List<CellItem> cols) {
		this.cols = cols;
	}
	public String[][] getData() {
		return data;
	}
	public void setData(String[][] data) {
		this.data = data;
	}
	public static void main(String[] args) {
		QueryTable vo=new QueryTable();
		List<CellItem> rows = new ArrayList<CellItem>();
		List<CellItem> cols =  new ArrayList<CellItem>();
		cols.add(new CellItem("2010","2010年",true));
		cols.add(new CellItem("2011","2011年",true));
		cols.add(new CellItem("2012","2012年",true));
		rows.add(new CellItem("bj","北京",true));
		rows.add(new CellItem("tj","天津",true));
		String[][] data = {{"1","2","3"},{"4","5","6"}};
		vo.setRows(rows);
		vo.setCols(cols);
		vo.setData(data);
		System.out.println(JSON.toJSONString(vo));
	}
}
