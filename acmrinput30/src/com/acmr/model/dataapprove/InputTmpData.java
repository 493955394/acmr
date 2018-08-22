package com.acmr.model.dataapprove;

import java.util.Date;

import acmr.templatecenter.entity.CellSignItems;

public class InputTmpData {

	private long id;
	private String taskCode;
	private String state;
	private String cellLoc;
	private String updateType;
	private CellSignItems wds;
	private double data;
	private String ifForceCast;
	private Date updateTime;
	private String memo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCellLoc() {
		return cellLoc;
	}

	public void setCellLoc(String cellLoc) {
		this.cellLoc = cellLoc;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public CellSignItems getWds() {
		return wds;
	}

	public void setWds(CellSignItems wds) {
		this.wds = wds;
	}

	public double getData() {
		return data;
	}

	public void setData(double data) {
		this.data = data;
	}

	public String getIfForceCast() {
		return ifForceCast;
	}

	public void setIfForceCast(String ifForceCast) {
		this.ifForceCast = ifForceCast;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
