package com.acmr.model.templatecenter;

import java.util.List;

/**
 * 筛选对象信息
 * 
 * @author chenyf
 *
 */
public class FilterInfo {

	private String farea; // 筛选范围
	
	private String isRangeFilter; //0,1


	public String getIsRangeFilter() {
		return isRangeFilter;
	}

	public void setIsRangeFilter(String isRangeFilter) {
		this.isRangeFilter = isRangeFilter;
	}

	private String fname;// 名称
	
	private String funStr;//filter函数串

	private String wdtype;// 维度类型

	private List<WdInfo> fwd; // 筛选维度

	// ---输入
	private String itype; // 输入项类型 0 1 2

	private String ival; // 输入值

	private List<WdInfo> inwd; // 筛选维度2

	// ---输出
	private String otype;// 输出项类型 0 1 2
    private String sjtype;
	private String oval;// 值 1;2
	private String sjy;
	public String getSjy() {
		return sjy;
	}

	public void setSjy(String sjy) {
		this.sjy = sjy;
	}

	public String getSjq() {
		return sjq;
	}

	public void setSjq(String sjq) {
		this.sjq = sjq;
	}

	public String getSjm() {
		return sjm;
	}

	public void setSjm(String sjm) {
		this.sjm = sjm;
	}

	private String sjq;
	private String sjm;
	
	public String getSjtype() {
		return sjtype;
	}

	public void setSjtype(String sjtype) {
		this.sjtype = sjtype;
	}

	private String oname;//固定值名称
	public String getFunStr() {
		return funStr;
	}

	public void setFunStr(String funStr) {
		this.funStr = funStr;
	}
	public String getOname() {
		return oname;
	}

	public void setOname(String oname) {
		this.oname = oname;
	}

	private List<WdInfo> outwd; // 筛选维度

	private List<WdInfo> outwds; // 筛选维度
	
	public List<WdInfo> getInwd() {
		return inwd;
	}

	public void setInwd(List<WdInfo> inwd) {
		this.inwd = inwd;
	}

	public List<WdInfo> getOutwds() {
		return outwds;
	}

	public void setOutwds(List<WdInfo> outwds) {
		this.outwds = outwds;
	}

	public String getWdtype() {
		return wdtype;
	}

	public void setWdtype(String wdtype) {
		this.wdtype = wdtype;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public List<WdInfo> getOutwd() {
		return outwd;
	}

	public void setOutwd(List<WdInfo> outwd) {
		this.outwd = outwd;
	}

	public String getFarea() {
		return farea;
	}

	public void setFarea(String farea) {
		this.farea = farea;
	}

	public List<WdInfo> getFwd() {
		return fwd;
	}

	public void setFwd(List<WdInfo> fwd) {
		this.fwd = fwd;
	}

	public String getItype() {
		return itype;
	}

	public void setItype(String itype) {
		this.itype = itype;
	}

	public String getIval() {
		return ival;
	}

	public void setIval(String ival) {
		this.ival = ival;
	}

	public String getOtype() {
		return otype;
	}

	public void setOtype(String otype) {
		this.otype = otype;
	}

	public String getOval() {
		return oval;
	}

	public void setOval(String oval) {
		this.oval = oval;
	}

}
