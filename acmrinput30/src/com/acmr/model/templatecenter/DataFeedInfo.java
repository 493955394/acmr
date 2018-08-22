package com.acmr.model.templatecenter;

import java.sql.Blob;
import java.util.Date;

public class DataFeedInfo {
	private String id;
	private String title;
	private Double currentValue;
	private int cForecast;
	private Double proposeValue;
	private int pForecast;
	private String unit;
	private byte[] uploadFile;
	private String memo;
	private String approvalContent;
	private int state;
	private String submitter;
	private String userId;
	private String updateTime;
	private Long dataId;
	private Date sendTime;
	private String approveUserId;
	private String fileName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Double currentValue) {
		this.currentValue = currentValue;
	}

	public int getcForecast() {
		return cForecast;
	}

	public void setcForecast(int cForecast) {
		this.cForecast = cForecast;
	}

	public Double getProposeValue() {
		return proposeValue;
	}

	public void setProposeValue(Double proposeValue) {
		this.proposeValue = proposeValue;
	}

	public int getpForecast() {
		return pForecast;
	}

	public void setpForecast(int pForecast) {
		this.pForecast = pForecast;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public byte[] getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(byte[] uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getApprovalContent() {
		return approvalContent;
	}

	public void setApprovalContent(String approvalContent) {
		this.approvalContent = approvalContent;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Long getDataId() {
		return dataId;
	}

	public void setDataId(Long dataId) {
		this.dataId = dataId;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getApproveUserId() {
		return approveUserId;
	}

	public void setApproveUserId(String approveUserId) {
		this.approveUserId = approveUserId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
