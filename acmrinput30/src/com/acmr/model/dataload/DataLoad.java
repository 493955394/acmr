package com.acmr.model.dataload;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <p>
 * 
 * @author 
 *
 *         <p>
 *         Company ACMR
 *
 *         <p>
 *         DataLoad.java
 *
 *         <p>
 *         2014下午2:38:54
 *
 *         <p>
 *         方法描述：数据加载VO类
 * 
 *         <p>
 */
public class DataLoad implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 任务代码/
	 * 
	 */
	
	private String deptCode;
	
	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	private String taskId;
	/** 报表代码 */
	private String reportcode;
	/** 报表名称 */
	private String reportname;
	/** 调查制度 */
	private String systemname;
	/** 报告期（报表期别）*/
	private String reportstage;
	/** 综合机关-部门名称 --可能是处室 */
	private String deptname;
	/** 报表期（制度类型） */
	private String sort;
	/** 上传截日期 */
	private Date createtime;
	/** 上传时间 */
	private Date inputtime;
	/** 报表状态 */
	private String reportstate;
	/** 综合机关 */
	private String compoffice;
	/** 制度期别类型 */
	private String atitysort;
	/** 选择标签名称:0待审批，1待填报 **/
	private String choosetabname;

	private String currentState;// 当前所处状态

	private String systemId;//调查制度id
	
	private String isForcePass;//是否是强制通过
	private Date pushDate;//推送至发布库时间
	
	private int intempcount;// 临时表录入数量
	
	
	public int getIntempcount() {
		return intempcount;
	}

	public void setIntempcount(int intempcount) {
		this.intempcount = intempcount;
	}

	public Date getPushDate() {
		return pushDate;
	}

	public void setPushDate(Date pushDate) {
		this.pushDate = pushDate;
	}

	public String getIsForcePass() {
		return isForcePass;
	}

	public void setIsForcePass(String isForcePass) {
		this.isForcePass = isForcePass;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	private Date etime;
	/**
	 * 操作时间
	 */
	private Date updatetime;

	private String procode;

	private String taskstate;

	private String flowcode; //流程编码
	//是否过期：0：未过期，1：已过期，-1：已删除
	private String isExpire;
	
	private String inputUserId; //提交人
	
	private String inputUserName; //提交人姓名
	
	
 
	public String getInputUserName() {
		return inputUserName;
	}

	public void setInputUserName(String inputUserName) {
		this.inputUserName = inputUserName;
	}

	public String getInputUserId() {
		return inputUserId;
	}

	public void setInputUserId(String inputUserId) {
		this.inputUserId = inputUserId;
	}

	public String getIsExpire() {
		return isExpire;
	}

	public void setIsExpire(String isExpire) {
		this.isExpire = isExpire;
	}

	public String getFlowcode() {
		return flowcode;
	}

	public void setFlowcode(String flowcode) {
		this.flowcode = flowcode;
	}

	/**
	 * 任务类型,0正式任务，1临时任务
	 */
	private String taskType;

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskstate() {
		return taskstate;
	}

	public void setTaskstate(String taskstate) {
		this.taskstate = taskstate;
	}

	public String getProcode() {
		return procode;
	}

	public void setProcode(String procode) {
		this.procode = procode;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Date getEtime() {
		return etime;
	}

	public void setEtime(Date etime) {
		this.etime = etime;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCurrentState() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}

	public String getChoosetabname() {
		return this.choosetabname;
	}

	public void setChoosetabname(String choosetabname) {
		this.choosetabname = choosetabname;
	}

	public String getAtitysort() {
		return atitysort;
	}

	public void setAtitysort(String atitysort) {
		this.atitysort = atitysort;
	}

	public String getCompoffice() {
		return compoffice;
	}

	public void setCompoffice(String compoffice) {
		this.compoffice = compoffice;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public String getReportcode() {
		return reportcode;
	}

	public void setReportcode(String reportcode) {
		this.reportcode = reportcode;
	}

	public String getReportname() {
		return reportname;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
	}

	public String getSystemname() {
		return systemname;
	}

	public void setSystemname(String systemname) {
		this.systemname = systemname;
	}

	public String getReportstage() {
		return reportstage;
	}

	public void setReportstage(String reportstage) {
		this.reportstage = reportstage;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getReportstate() {
		return reportstate;
	}

	public void setReportstate(String reportstate) {
		this.reportstate = reportstate;
	}

}
