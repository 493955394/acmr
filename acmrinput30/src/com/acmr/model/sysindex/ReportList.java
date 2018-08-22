package com.acmr.model.sysindex;

import java.util.ArrayList;
import java.util.List;

public class ReportList {
	private List<OneYearReportStageVoList> list;

	public List<OneYearReportStageVoList> getList() {
		return list;
	}

	public void setList(List<OneYearReportStageVoList> list) {
		this.list = list;
	}
	
	public ReportList() {
		// TODO Auto-generated constructor stub
	}
	
	public ReportList(List<OneYearReportStageVoList> list) {
		this.list = list;
	}

	public static void main(String[] args) {
		ReportList list = new ReportList();
		List<OneYearReportStageVoList> onelist = new ArrayList<>();
		OneYearReportStageVoList one = new OneYearReportStageVoList("M","2015","2","7");
		OneYearReportStageVoList two = new OneYearReportStageVoList("M","2014","2","7");
		onelist.add(one);
		onelist.add(two);
		list.setList(onelist);
		//System.out.println(JSON.toJSON(list));
	}
}
