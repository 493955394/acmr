package com.acmr.model.dataapprove;

import java.io.Serializable;
/**
 * @author anzhao
 * 描述：审批意见实体类
 *
 */
public class ApproveView implements Serializable{
	private String datetime;//审批时间
	private String approveResult;//审批结果
	private String approver;//审批人
	private String approveOpinion;//审批意见
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getApproveResult() {
		return approveResult;
	}
	public void setApproveResult(String approveResult) {
		this.approveResult = approveResult;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public String getApproveOpinion() {
		return approveOpinion;
	}
	public void setApproveOpinion(String approveOpinion) {
		this.approveOpinion = approveOpinion;
	}
	
	
	

}
