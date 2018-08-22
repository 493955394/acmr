package com.acmr.model.sysindex;

public class UniteTime {
	private String id;
	private String timeplanId;
	/**
	 * 月
	 */
	private int month;
	/**
	 * 日
	 */
	private int day;
	/**
	 * 起始报告期
	 */
	private String startReportPeriod;
	/**
	 * 结束报告期
	 */
	private String endReportPeriod;
	/**
	录入时间设定
	*/
	private int inputTimeSet;
	/**
	 * 录入时间设定工作日
	 */
	private String inputTimeType;
	/**
	 * 推送时间
	 */
	private int examineOrPushTime;
	/**
	 * 推送时间工作日
	 */
	private String examineOrPushTimeType;

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

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getStartReportPeriod() {
		return startReportPeriod;
	}

	public void setStartReportPeriod(String startReportPeriod) {
		this.startReportPeriod = startReportPeriod;
	}

	public String getEndReportPeriod() {
		return endReportPeriod;
	}

	public void setEndReportPeriod(String endReportPeriod) {
		this.endReportPeriod = endReportPeriod;
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

}
