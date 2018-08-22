package com.acmr.model.range;

import java.util.List;

import acmr.templatecenter.entity.Position;

import com.acmr.model.templatecenter.FilterInfo;
import com.acmr.model.templatecenter.WdInfo;

/**
 * 识别区对象
 * 
 * @author chenyf
 *
 */
public class Distinguish {

	private String name; // 名称

	private String oldName; // 名称

	private Position areainfo; // 区域 后台用

	private Pinfo ainfo;// 区域.前台用

	private String wdflag; // 维度信息



	private Integer ifdiy; // 是否是自定义

	private List<WdInfo> wds; // 维度信息

	private List<WdInfo> afterFun; // 解析之后维度信息

	private String liketype; // 匹配类型

	private FilterInfo filterInfo; // 是否筛选

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public List<WdInfo> getAfterFun() {
		return afterFun;
	}

	public void setAfterFun(List<WdInfo> afterFun) {
		this.afterFun = afterFun;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Position getAreainfo() {
		return areainfo;
	}

	public void setAreainfo(Position areainfo) {
		this.areainfo = areainfo;
	}

	public Pinfo getAinfo() {
		return ainfo;
	}

	public void setAinfo(Pinfo ainfo) {
		this.ainfo = ainfo;
	}

	public String getWdflag() {
		return wdflag;
	}

	public void setWdflag(String wdflag) {
		this.wdflag = wdflag;
	}

	public Integer getIfdiy() {
		return ifdiy;
	}

	public void setIfdiy(Integer ifdiy) {
		this.ifdiy = ifdiy;
	}

	public List<WdInfo> getWds() {
		return wds;
	}

	public void setWds(List<WdInfo> wds) {
		this.wds = wds;
	}

	public String getLiketype() {
		return liketype;
	}

	public void setLiketype(String liketype) {
		this.liketype = liketype;
	}

	public FilterInfo getFilterInfo() {
		return filterInfo;
	}

	public void setFilterInfo(FilterInfo filterInfo) {
		this.filterInfo = filterInfo;
	}

}
