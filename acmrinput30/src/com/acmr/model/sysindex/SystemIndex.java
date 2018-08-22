package com.acmr.model.sysindex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import acmr.util.IKeyible;

/**
 * 用户类
 * 
 * @author jinhr
 *
 */
public class SystemIndex implements Serializable,IKeyible, Comparable<SystemIndex> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 制度编码
	 */
	private String code;
	/**
	 * 制度名称
	 */
	private String cname;
	/**
	 * 制度长名称
	 */
	private String ccName;
	/**
	 * 父制度编码
	 */
	private String proCode;
	/**
	 * 模板id
	 */
	private String modCode;
	/**
	 * 流程code
	 */
	private String flowCode;
	/**
	 * 任务频度
	 */
	private String sort;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 状态
	 */
	private String state;
	/**
	 * 备注说明
	 */
	private String memo;
	/**
	 * 排序码
	 */
	private String sortCode;
	/**
	 * 部门编码
	 */
	private String deptCode;
	/**
	 * 综合机关
	 */
	private String compoffice;
	/**
	 * 表号
	 */
	private String tableNum;
	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * 文号
	 */
	private String docNum;
	/**
	 * 制表机关
	 */
	private String createOffice;
	/**
	 * 制度类型
	 */
	private String sysType;
	/**
	 * 是否是报表
	 */
	private String isTable;
	/**
	 * 任务期别
	 */
	private String taskSort;
	/**
	 * 任务id
	 */
	private String planId;

	private String flowType;

	/**
	 * 管理机关
	 */
	private String AdminOffice;

	/**
	 * 填报机关
	 */
	private String provideOffice;

	private String hasReportPeriod;
	
	private List<String> childs = new ArrayList<String>();


	public String getHasReportPeriod() {
		return hasReportPeriod;
	}

	public void setHasReportPeriod(String hasReportPeriod) {
		this.hasReportPeriod = hasReportPeriod;
	}

	public String getFlowType() {
		return flowType;
	}

	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getCcName() {
		return ccName;
	}

	public void setCcName(String ccName) {
		this.ccName = ccName;
	}

	public String getProCode() {
		return proCode;
	}

	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	public String getModCode() {
		return modCode;
	}

	public void setModCode(String modCode) {
		this.modCode = modCode;
	}

	public String getFlowCode() {
		return flowCode;
	}

	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getSortCode() {
		return sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getCompoffice() {
		return compoffice;
	}

	public void setCompoffice(String compoffice) {
		this.compoffice = compoffice;
	}

	public String getTableNum() {
		return tableNum;
	}

	public void setTableNum(String tableNum) {
		this.tableNum = tableNum;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDocNum() {
		return docNum;
	}

	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}

	public String getCreateOffice() {
		return createOffice;
	}

	public void setCreateOffice(String createOffice) {
		this.createOffice = createOffice;
	}

	public String getSysType() {
		return sysType;
	}

	public void setSysType(String sysType) {
		this.sysType = sysType;
	}

	public String getIsTable() {
		return isTable;
	}

	public void setIsTable(String isTable) {
		this.isTable = isTable;
	}

	public String getTaskSort() {
		return taskSort;
	}

	public void setTaskSort(String taskSort) {
		this.taskSort = taskSort;
	}

	public String getAdminOffice() {
		return AdminOffice;
	}

	public void setAdminOffice(String adminOffice) {
		AdminOffice = adminOffice;
	}

	public String getProvideOffice() {
		return provideOffice;
	}

	public void setProvideOffice(String provideOffice) {
		this.provideOffice = provideOffice;
	}

	public int compareTo(SystemIndex o) {
		if (o == null) {
			return -1;
		}
		return this.createTime.compareTo(o.createTime);
	}


	public List<String> getChilds() {
		return childs;
	}

	public void addChild(String v) {
		if (!childs.contains(v)) {
			childs.add(v);
		}
	}

	@Override
	public String Key() {
		return code;
	}

}
