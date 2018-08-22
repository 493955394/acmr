package com.acmr.model.dataload;

import java.io.Serializable;


public class TemplateTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id; // id
	private String templateCode; // 模板代码
	private String taskCode; // 报表代码
	private String taskName; // 报表名称
	private String taskType; // 报表类型
	private String reportstage; // 报告期
	private String state; // 状态
	private String result; // 结果
	private String isSuccess = "0"; // 0 不合法 1合法
	private String excelContent;// excel大字段

	public TemplateTask() {

	}


	public String getExcelContent() {
		return excelContent;
	}


	public void setExcelContent(String excelContent) {
		this.excelContent = excelContent;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getTaskCode() {
		if ("null" == this.taskCode) {
			return "";
		}
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getTaskName() {
		if ("null" == this.taskName) {
			return "";
		}
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public boolean isEmpty(String s) {
		if (null == s || s.trim().equals("") || s.trim().equals("null") || s.trim().equals("undefined")) {
			return true;
		}
		return false;
	}
	public String getTaskType() {
		if (!isEmpty(this.reportstage)) {
			if (this.reportstage.length() == 4) {
				return "年报";
			} else if (this.reportstage.length() == 5) {
				return "季报";
			} else if (this.reportstage.length() == 6) {
				return "月报";
			}
		}
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getReportstage() {
		if ("null" == this.reportstage) {
			return "";
		}
		return reportstage;
	}

	public void setReportstage(String reportstage) {
		this.reportstage = reportstage;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
