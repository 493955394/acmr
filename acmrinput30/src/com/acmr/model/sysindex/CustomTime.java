package com.acmr.model.sysindex;

import java.util.Date;

public class CustomTime {
	private String id;
	private String timeplanId;
	private String reportPeriod;
	private Date taskStartTime;
	private int inputTimeSet;
	private String inputTimeType;
	private int examineOrPushTime;
	private String examineOrPushTimeType;
	private String isNoReport;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTimeplanId() {
		return timeplanId;
	}

	public void setTimeplanId(String timeplanId) {
		this.timeplanId = timeplanId;
	}

	public String getReportPeriod() {
		return reportPeriod;
	}

	public void setReportPeriod(String reportPeriod) {
		this.reportPeriod = reportPeriod;
	}

	public Date getTaskStartTime() {
		return taskStartTime;
	}

	public void setTaskStartTime(Date taskStartTime) {
		this.taskStartTime = taskStartTime;
	}

	public int getInputTimeSet() {
		return inputTimeSet;
	}

	public void setInputTimeSet(int inputTimeSet) {
		this.inputTimeSet = inputTimeSet;
	}

	public String getInputTimeType() {
		return inputTimeType;
	}

	public void setInputTimeType(String inputTimeType) {
		this.inputTimeType = inputTimeType;
	}

	public int getExamineOrPushTime() {
		return examineOrPushTime;
	}

	public void setExamineOrPushTime(int examineOrPushTime) {
		this.examineOrPushTime = examineOrPushTime;
	}

	public String getExamineOrPushTimeType() {
		return examineOrPushTimeType;
	}

	public void setExamineOrPushTimeType(String examineOrPushTimeType) {
		this.examineOrPushTimeType = examineOrPushTimeType;
	}

	public String getIsNoReport() {
		return isNoReport;
	}

	public void setIsNoReport(String isNoReport) {
		this.isNoReport = isNoReport;
	}

}
