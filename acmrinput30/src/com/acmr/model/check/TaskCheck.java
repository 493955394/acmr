package com.acmr.model.check;

import java.util.Date;

/**
 * 数据校验model
 */
public class TaskCheck {
	private String taskCode;
	private String checkState;
	private String checkStep;
	private Date updateTime;

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

	public String getCheckStep() {
		return checkStep;
	}

	public void setCheckStep(String checkStep) {
		this.checkStep = checkStep;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
