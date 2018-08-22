package com.acmr.model.dataapprove;

import java.io.Serializable;
/**
 * 
 * @author anzhao
 * 描述：审批进度实体类
 *
 */
public class ApproveProgress implements Serializable{
	private String approveNum;//审批序列号,对应节点
	private String approveRalative;//审批关系
	private String approver;//审批人
	private String state;//审批状态
	private String approveTime;//审批时间
	private String optionPerson;//操作人
	public String getApproveNum() {
		return approveNum;
	}
	public void setApproveNum(String approveNum) {
		this.approveNum = approveNum;
	}
	
	public String getApproveRalative() {
		return approveRalative;
	}
	public void setApproveRalative(String approveRalative) {
		this.approveRalative = approveRalative;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getApproveTime() {
		return approveTime;
	}
	public void setApproveTime(String approveTime) {
		this.approveTime = approveTime;
	}
	public String getOptionPerson() {
		return optionPerson;
	}
	public void setOptionPerson(String optionPerson) {
		this.optionPerson = optionPerson;
	}
	
	

}
