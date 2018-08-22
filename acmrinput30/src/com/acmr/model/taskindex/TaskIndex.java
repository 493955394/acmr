package com.acmr.model.taskindex;

import java.io.Serializable;
import java.util.Date;

import com.acmr.helper.constants.Const;

/**
 * 任务表po
 * 
 */
public final class TaskIndex implements Serializable {
	private static final long serialVersionUID = 1L;
	private String code;// 任务实例编码
	private String cname;// 任务实例名称
	private String sysCode;// 制度编码
	private String modCode;// 模板编码
	private Date btime;// 开始时间
	private Date etime;// 结束时间
	private String sort;// 期别类型（年月日季）
	private String state;// 状态
	private Date createTime;// 创建时间
	private Date inputTime;// 录入时间
	private Date updateTime;// 更新时间
	private Date indbTime;// 入库时间
	private String memo;// 说明
	private String taskState;// 添报状态，0未添报，1已添报
	private String reportstage;// 报告期
	private String taskcode;// 任务代码
	private String ayearmon;
	private String tableNum;// 表号
	private String tableName;// 表名
	private String taskType;// 任务类型（系统任务，临时任务）
	private String isexpire;// 是否过期,0：false，未过期,1:true,已过期
	private String deptCode;// 部门编码
	private String dataChange; //查询变更
	private String inputUserId; //上传人
	private Date pushDate;// 推送时间
	private String isForcePass;//是否是强制通过0：不是1：是
	public TaskIndex() {}
	public TaskIndex(String code, String cname, String sysCode, String modCode,
			Date btime, Date etime, String sort, String state, Date createTime,
			Date inputTime, Date updateTime, Date indbTime, String memo,
			String taskState, String reportstage, String taskcode,
			String ayearmon, String tableNum, String tableName,
			String taskType, String isexpire,String deptCode,
			String isForcePass,String inputUserId) {
		this.code = code;
		this.cname = cname;
		this.sysCode = sysCode;
		this.modCode = modCode;
		this.btime = btime;
		this.etime = etime;
		this.sort = sort;
		this.state = state;
		this.createTime = createTime;
		this.inputTime = inputTime;
		this.updateTime = updateTime;
		this.indbTime = indbTime;
		this.memo = memo;
		this.taskState = taskState;
		this.reportstage = reportstage;
		this.taskcode = taskcode;
		this.ayearmon = ayearmon;
		this.tableNum = tableNum;
		this.tableName = tableName;
		this.taskType = taskType;
		this.isexpire = isexpire;
		this.deptCode = deptCode;
		this.isForcePass = isForcePass;
		this.inputUserId = inputUserId;
	}

	public String getInputUserId() {
		return inputUserId;
	}
	
	public void setInputUserId(String inputUserId) {
		this.inputUserId = inputUserId;
	}

	public String getDataChange() {
		return dataChange;
	}
	
	public void setDataChange(String dataChange) {
		this.dataChange = dataChange;
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

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
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

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getModCode() {
		return modCode;
	}

	public void setModCode(String modCode) {
		this.modCode = modCode;
	}

	public Date getBtime() {
		return btime;
	}

	public void setBtime(Date btime) {
		this.btime = btime;
	}

	public Date getEtime() {
		return etime;
	}

	public void setEtime(Date etime) {
		this.etime = etime;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getInputTime() {
		return inputTime;
	}

	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getIndbTime() {
		return indbTime;
	}

	public void setIndbTime(Date indbTime) {
		this.indbTime = indbTime;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public String getReportstage() {
		return reportstage;
	}

	public void setReportstage(String reportstage) {
		this.reportstage = reportstage;
	}

	public String getTaskcode() {
		return taskcode;
	}

	public void setTaskcode(String taskcode) {
		this.taskcode = taskcode;
	}

	public String getAyearmon() {
		return ayearmon;
	}

	public void setAyearmon(String ayearmon) {
		this.ayearmon = ayearmon;
	}

	public String getIsexpire() {
		return isexpire;
	}

	public void setIsexpire(String isexpire) {
		this.isexpire = isexpire;
	}

	public boolean isExpire() {
		return Const.BOOLEAN_TRUE_STRING.equals(isexpire);
	}

	public String getSortLabel() {
		if (sort == null) {
			return "";
		}
		else if (Const.TEMPLATE_TYPE_Y.equals(sort)) {
			return "年报";
		} else if (Const.TEMPLATE_TYPE_Q.equals(sort)) {
			return "季报";
		} else if (Const.TEMPLATE_TYPE_M.equals(sort)) {
			return "月报";
		} else if (Const.TEMPLATE_TYPE_X.equals(sort)) {
			return "旬报";
		} else if (Const.TEMPLATE_TYPE_W.equals(sort)) {
			return "周报";
		} else if (Const.TEMPLATE_TYPE_D.equals(sort)) {
			return "日报";
		} else {
			return "临时";
		}
	}

	public String getTaskStateLabel() {
		if (taskState == null) {
			return "";
		}
		if (Const.TASK_STATE_02.equals(taskState)) {
			return "草稿";
//		} else if (Const.TASK_STATE_06.equals(taskState)) {
//			return "已校验,待提交";
		} else if (Const.TASK_STATE_03.equals(taskState)) {
			return "待审批";
		} else if (Const.TASK_STATE_04.equals(taskState)) {
			return "已入库";
		} else if (Const.TASK_STATE_05.equals(taskState)) {
			return "退回重报";
		} else {
			return "未报";
		}
	}

	public String getTaskTypeLabel() {
		if (taskType == null) {
			return "";
		}
		if (Const.TASK_TYPE_1.equals(taskType)) {
			return "临时任务";
		} else {
			return "常规任务";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ayearmon == null) ? 0 : ayearmon.hashCode());
		result = prime * result + ((btime == null) ? 0 : btime.hashCode());
		result = prime * result + ((cname == null) ? 0 : cname.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result
				+ ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((etime == null) ? 0 : etime.hashCode());
		result = prime * result
				+ ((indbTime == null) ? 0 : indbTime.hashCode());
		result = prime * result
				+ ((inputTime == null) ? 0 : inputTime.hashCode());
		result = prime * result
				+ ((isexpire == null) ? 0 : isexpire.hashCode());
		result = prime * result + ((memo == null) ? 0 : memo.hashCode());
		result = prime * result + ((modCode == null) ? 0 : modCode.hashCode());
		result = prime * result
				+ ((reportstage == null) ? 0 : reportstage.hashCode());
		result = prime * result + ((sort == null) ? 0 : sort.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((sysCode == null) ? 0 : sysCode.hashCode());
		result = prime * result
				+ ((tableName == null) ? 0 : tableName.hashCode());
		result = prime * result
				+ ((tableNum == null) ? 0 : tableNum.hashCode());
		result = prime * result
				+ ((taskState == null) ? 0 : taskState.hashCode());
		result = prime * result
				+ ((taskType == null) ? 0 : taskType.hashCode());
		result = prime * result
				+ ((taskcode == null) ? 0 : taskcode.hashCode());
		result = prime * result
				+ ((updateTime == null) ? 0 : updateTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskIndex other = (TaskIndex) obj;
		if (ayearmon == null) {
			if (other.ayearmon != null)
				return false;
		} else if (!ayearmon.equals(other.ayearmon))
			return false;
		if (btime == null) {
			if (other.btime != null)
				return false;
		} else if (!btime.equals(other.btime))
			return false;
		if (cname == null) {
			if (other.cname != null)
				return false;
		} else if (!cname.equals(other.cname))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (etime == null) {
			if (other.etime != null)
				return false;
		} else if (!etime.equals(other.etime))
			return false;
		if (indbTime == null) {
			if (other.indbTime != null)
				return false;
		} else if (!indbTime.equals(other.indbTime))
			return false;
		if (inputTime == null) {
			if (other.inputTime != null)
				return false;
		} else if (!inputTime.equals(other.inputTime))
			return false;
		if (isexpire == null) {
			if (other.isexpire != null)
				return false;
		} else if (!isexpire.equals(other.isexpire))
			return false;
		if (memo == null) {
			if (other.memo != null)
				return false;
		} else if (!memo.equals(other.memo))
			return false;
		if (modCode == null) {
			if (other.modCode != null)
				return false;
		} else if (!modCode.equals(other.modCode))
			return false;
		if (reportstage == null) {
			if (other.reportstage != null)
				return false;
		} else if (!reportstage.equals(other.reportstage))
			return false;
		if (sort == null) {
			if (other.sort != null)
				return false;
		} else if (!sort.equals(other.sort))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (sysCode == null) {
			if (other.sysCode != null)
				return false;
		} else if (!sysCode.equals(other.sysCode))
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		if (tableNum == null) {
			if (other.tableNum != null)
				return false;
		} else if (!tableNum.equals(other.tableNum))
			return false;
		if (taskState == null) {
			if (other.taskState != null)
				return false;
		} else if (!taskState.equals(other.taskState))
			return false;
		if (taskType == null) {
			if (other.taskType != null)
				return false;
		} else if (!taskType.equals(other.taskType))
			return false;
		if (taskcode == null) {
			if (other.taskcode != null)
				return false;
		} else if (!taskcode.equals(other.taskcode))
			return false;
		if (updateTime == null) {
			if (other.updateTime != null)
				return false;
		} else if (!updateTime.equals(other.updateTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaskIndex [code=" + code + ", cname=" + cname + ", sysCode="
				+ sysCode + ", modCode=" + modCode + ", btime=" + btime
				+ ", etime=" + etime + ", sort=" + sort + ", state=" + state
				+ ", createTime=" + createTime + ", inputTime=" + inputTime
				+ ", updateTime=" + updateTime + ", indbTime=" + indbTime
				+ ", memo=" + memo + ", taskState=" + taskState
				+ ", reportstage=" + reportstage + ", taskcode=" + taskcode
				+ ", ayearmon=" + ayearmon + ", tableNum=" + tableNum
				+ ", tableName=" + tableName + ", taskType=" + taskType
				+ ", isexpire=" + isexpire + "]";
	}

}
