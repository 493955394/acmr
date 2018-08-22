package com.acmr.model.sysindex;

import java.io.Serializable;
import java.util.Date;

/**
 * 制度计划表
 * @author caosl
 */
public class SystemPlan implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;//计划编码
	private String cname;//计划名
	private String sysCode;//制度编码
	private String sort;//期别类型
	private Date btime;//计划开始时间
	private Date etime;//计划结束时间
	private int interval;//间隔期
	private int rpdelay;//延后填报时间
	private int lastDay;//过期天数
	private Date lastTime;//上次任务时间（实际时间）
	private Date nextTime;//下次任务时间（实际时间）
	private String nextTimeCode;//下次任务时间（逻辑时间）
	private Date createTime;//计划创建时间
	private String state;//状态
	private String memo;//说明
	private String sorta;
	private String irregular;//是否为规律的制度计划
	
	public String getSorta() {
		return sorta;
	}
	public void setSorta(String sorta) {
		this.sorta = sorta;
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
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
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
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public int getRpdelay() {
		return rpdelay;
	}
	public void setRpdelay(int rpdelay) {
		this.rpdelay = rpdelay;
	}
	public int getLastDay() {
		return lastDay;
	}
	public void setLastDay(int lastDay) {
		this.lastDay = lastDay;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	public Date getNextTime() {
		return nextTime;
	}
	public void setNextTime(Date nextTime) {
		this.nextTime = nextTime;
	}
	public String getNextTimeCode() {
		return nextTimeCode;
	}
	public void setNextTimeCode(String nextTimeCode) {
		this.nextTimeCode = nextTimeCode;
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
	public String getIrregular() {
		return irregular;
	}
	public void setIrregular(String irregular) {
		this.irregular = irregular;
	}
	
}
