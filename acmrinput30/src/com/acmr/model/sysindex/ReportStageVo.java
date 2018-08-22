package com.acmr.model.sysindex;
/**
 * 任务生成封装类
 * @author caosl
 */
public class ReportStageVo {
	private String reportStage;//报告期
	private String isClose;//1，此任务被关闭；0，此任务要生成
	private String taskBeginTime;//生成任务时间，精确到天
	private String isCreateTask;//是否已经生成了任务
	public ReportStageVo(){}
	public String getReportStage() {
		return reportStage;
	}
	public void setReportStage(String reportStage) {
		this.reportStage = reportStage;
	}
	public String getIsClose() {
		return isClose;
	}
	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}
	public String getTaskBeginTime() {
		return taskBeginTime;
	}
	public void setTaskBeginTime(String taskBeginTime) {
		this.taskBeginTime = taskBeginTime;
	}
	public String getIsCreateTask() {
		return isCreateTask;
	}
	public void setIsCreateTask(String isCreateTask) {
		this.isCreateTask = isCreateTask;
	}
	
}
