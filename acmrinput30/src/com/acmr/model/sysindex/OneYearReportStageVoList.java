package com.acmr.model.sysindex;

import java.util.ArrayList;
import java.util.List;

import com.acmr.helper.constants.Const;

/**
 * 一年的报告期生成类
 * 
 * @author caosl
 */
public class OneYearReportStageVoList {
	private List<ReportStageVo> OneYearReportStageList;// 报告期
	private String sort;// 制度类型
	private String year;// 年份
	private String lastDay;// 任务持续时间
	private String pushTime;// 推送时间
	private String inputTimeType;
	private String pushTimeType;

	public String getInputTimeType() {
		return inputTimeType;
	}

	public void setInputTimeType(String inputTimeType) {
		this.inputTimeType = inputTimeType;
	}

	public String getPushTimeType() {
		return pushTimeType;
	}

	public void setPushTimeType(String pushTimeType) {
		this.pushTimeType = pushTimeType;
	}

	public OneYearReportStageVoList() {
	}

	public OneYearReportStageVoList(String sort, String year, String lastDay, String pushTime) {
		this.sort = sort;
		this.year = year;
		this.lastDay = lastDay;
		this.pushTime = pushTime;
		List<ReportStageVo> reportStage = new ArrayList<>();
		if (Const.SYSTEM_SORT_M.equals(sort)) {
			for (int i = 1; i < 13; i++) {
				ReportStageVo qa = new ReportStageVo();
				qa.setIsClose("0");
				qa.setIsCreateTask("0");
				String reportStr = i + "";
				String monthStr = i + 1 + "";
				if (i < 9) {
					monthStr = "0" + (i + 1);
				}
				if (i <= 9) {
					reportStr = "0" + i;
				}
				qa.setReportStage(year + reportStr);
				qa.setTaskBeginTime(year + "-" + monthStr + "-15");
				if (i == 12) {
					qa.setTaskBeginTime((Integer.parseInt(year) + 1) + "-01-15");
				}
				reportStage.add(qa);
			}
		}
		if (Const.SYSTEM_SORT_Q.equals(sort)) {// 季度任务
			ReportStageVo qa = new ReportStageVo();
			qa.setIsClose("0");
			qa.setIsCreateTask("0");
			qa.setReportStage(year + "A");
			qa.setTaskBeginTime(year + "-04-15");
			reportStage.add(qa);
			ReportStageVo qb = new ReportStageVo();
			qb.setIsClose("0");
			qb.setIsCreateTask("0");
			qb.setReportStage(year + "B");
			qb.setTaskBeginTime(year + "-07-15");
			reportStage.add(qb);
			ReportStageVo qc = new ReportStageVo();
			qc.setIsClose("0");
			qc.setIsCreateTask("0");
			qc.setReportStage(year + "C");
			qc.setTaskBeginTime(year + "-10-15");
			reportStage.add(qc);
			ReportStageVo qd = new ReportStageVo();
			qd.setIsClose("0");
			qd.setIsCreateTask("0");
			qd.setReportStage(year + "D");
			qd.setTaskBeginTime((Integer.parseInt(year) + 1) + "-01-15");
			reportStage.add(qd);
		}
		if (Const.SYSTEM_SORT_Y.equals(sort)) {
			ReportStageVo yvo = new ReportStageVo();
			yvo.setIsClose("0");
			yvo.setIsCreateTask("0");
			yvo.setReportStage(year);
			yvo.setTaskBeginTime((Integer.parseInt(year) + 1) + "-01-15");
			reportStage.add(yvo);
		}
		this.OneYearReportStageList = reportStage;
	}

	public List<ReportStageVo> getOneYearReportStageList() {
		return OneYearReportStageList;
	}

	public void setOneYearReportStageList(List<ReportStageVo> oneYearReportStageList) {
		OneYearReportStageList = oneYearReportStageList;
	}

	public String getLastDay() {
		return lastDay;
	}

	public void setLastDay(String lastDay) {
		this.lastDay = lastDay;
	}

	public String getPushTime() {
		return pushTime;
	}

	public void setPushTime(String pushTime) {
		this.pushTime = pushTime;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public static void main(String[] args) {
		// OneYearReportStageVoList one = new OneYearReportStageVoList("M",
		// "2014", "1", "2");
		// System.out.println(JSON.toJSON(one));
	}
}
