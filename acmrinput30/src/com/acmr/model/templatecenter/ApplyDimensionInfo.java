package com.acmr.model.templatecenter;

import java.util.Date;
import java.util.List;
/**
 * 申请提交维度
 * @author cjl
 *
 */
public class ApplyDimensionInfo {
	private String id;
	private String name;
	private String memo;
	private String excel;
	private String state;
	private String userId;
	private String userName;
	private Date createTime;
	private String replyUserId;
	private String replyUserName;
	private String replyMemo;
	private Date replyTime;
	private ApplyDimensionList applyList;
	private ApplyDimensionList replyList;
	private byte[] fileContent;
	private String fileName;
	public String getReplyMemo() {
		return replyMemo;
	}
	public void setReplyMemo(String replyMemo) {
		this.replyMemo = replyMemo;
	}
	public ApplyDimensionList getApplyList() {
		return applyList;
	}
	public void setApplyList(ApplyDimensionList applyList) {
		this.applyList = applyList;
	}
	public ApplyDimensionList getReplyList() {
		return replyList;
	}
	public void setReplyList(ApplyDimensionList replyList) {
		this.replyList = replyList;
	}
	public String getReplyUserId() {
		return replyUserId;
	}
	public void setReplyUserId(String replyUserId) {
		this.replyUserId = replyUserId;
	}
	public String getReplyUserName() {
		return replyUserName;
	}
	public void setReplyUserName(String replyUserName) {
		this.replyUserName = replyUserName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getExcel() {
		return excel;
	}
	public void setExcel(String excel) {
		this.excel = excel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}
	public byte[] getFileContent() {
		return fileContent;
	}
	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
