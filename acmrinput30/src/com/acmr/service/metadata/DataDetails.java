package com.acmr.service.metadata;

/**
 * 元数据数据详情实体对象
 * 
 * @author chenyf
 *
 */
public class DataDetails {
	private String dbcode;
	private String fl;
	private String flName;
	private String reg;
	private String regName;
	private String beginTime;
	private String endTime;
	private String updateTime;
	private String count;
	private String datasource;
	private String datasourceName;

	public DataDetails() {
		// TODO Auto-generated constructor stub
	}

	public DataDetails(String dbcode, String fl, String flName, String reg, String regName, String beginTime, String endTime, String updateTime, String count, String datasource, String datasourceName) {
		this.dbcode = dbcode;
		this.fl = fl;
		this.flName = flName;
		this.reg = reg;
		this.regName = regName;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.updateTime = updateTime;
		this.count = count;
		this.datasource = datasource;
		this.datasourceName = datasourceName;
	}

	public String getDbcode() {
		if ("y".equals(dbcode)) {
			return "年度";
		} else if ("m".equals(dbcode)) {
			return "月度";
		} else if ("q".equals(dbcode)) {
			return "季度";
		}
		return dbcode;
	}

	public void setDbcode(String dbcode) {
		this.dbcode = dbcode;
	}

	public String getFl() {
		return fl;
	}

	public void setFl(String fl) {
		this.fl = fl;
	}

	public String getReg() {
		return reg;
	}

	public void setReg(String reg) {
		this.reg = reg;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getFlName() {
		return flName;
	}

	public void setFlName(String flName) {
		this.flName = flName;
	}

	public String getRegName() {
		return regName;
	}

	public void setRegName(String regName) {
		this.regName = regName;
	}

	public String getDatasourceName() {
		return datasourceName;
	}

	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}

}
