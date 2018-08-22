package com.acmr.model.templatecenter;

import java.util.ArrayList;
import java.util.List;

import com.acmr.model.calculate.CalBean;
import com.acmr.model.calculate.CheBean;
import com.acmr.model.calculate.MemoBean;
import com.acmr.model.property.ProEdit;
import com.acmr.model.range.Distinguish;
import com.acmr.model.range.Pinfo;

/**
 * excel属性集合
 * @author caosl
 */
public class ExcelProperty {
	
	private Pinfo titleinfo; // 报表标题  --- Pinfo
	private Pinfo mainbarinfo; // 报表主栏 --- Pinfo
	private Pinfo guestbarinfo; // 报表宾栏 --- Pinfo
	private Pinfo unitinfo; // 报表单位 --- Pinfo
	private Pinfo expsinfo; // 报表备注 --- Pinfo
	private Tnode zlnode;//拖拽主栏
	private Tnode blnode;//拖拽宾栏
	public Tnode getZlnode() {
		return zlnode;
	}
	public void setZlnode(Tnode zlnode) {
		this.zlnode = zlnode;
	}
	public Tnode getBlnode() {
		return blnode;
	}
	public void setBlnode(Tnode blnode) {
		this.blnode = blnode;
	}
	//private MarkerModel markerPos; // 前后台交互对象
	private List<ProEdit> propertyList=new ArrayList<ProEdit>();//单元格属性
	private List<CalBean> calculateInfo=new ArrayList<CalBean>(); //计算信息
	private List<CheBean> checkinfo=new ArrayList<CheBean>();//校验信息
	private List<MemoBean> memoinfo=new ArrayList<MemoBean>();//备注信息
	private List<Distinguish> rangeList=new ArrayList<Distinguish>(); //range列表，对应range对象（还没编写）
	private List<FilterInfo> filterList=new ArrayList<FilterInfo>();//筛选集合
	
//	private List<MultivariateCell> showCode=new ArrayList<MultivariateCell>();//显示代码
//	private List<MultivariateCell> showColor=new ArrayList<MultivariateCell>(); //显示
//	private List<MultivariateCell> hideCode=new ArrayList<MultivariateCell>();//隐藏代码
//	private List<MultivariateCell> hideColor=new ArrayList<MultivariateCell>();//隐藏颜色
	
	
	
	public List<MemoBean> getMemoinfo() {
		return memoinfo;
	}
	public void setMemoinfo(List<MemoBean> memoinfo) {
		this.memoinfo = memoinfo;
	}
//	public List<MultivariateCell> getShowCode() {
//		return showCode;
//	}
//	public void setShowCode(List<MultivariateCell> showCode) {
//		this.showCode = showCode;
//	}
//	public List<MultivariateCell> getShowColor() {
//		return showColor;
//	}
//	public void setShowColor(List<MultivariateCell> showColor) {
//		this.showColor = showColor;
//	}
//	public List<MultivariateCell> getHideCode() {
//		return hideCode;
//	}
//	public void setHideCode(List<MultivariateCell> hideCode) {
//		this.hideCode = hideCode;
//	}
//	public List<MultivariateCell> getHideColor() {
//		return hideColor;
//	}
//	public void setHideColor(List<MultivariateCell> hideColor) {
//		this.hideColor = hideColor;
//	}
	public List<ProEdit> getPropertyList() {
		return propertyList;
	}
	public void setPropertyList(List<ProEdit> propertyList) {
		this.propertyList = propertyList;
	}
//	public MarkerModel getMarkerPos() {
//		return markerPos;
//	}
//	public void setMarkerPos(MarkerModel markerPos) {
//		this.markerPos = markerPos;
//	}
	public Pinfo getTitleinfo() {
		return titleinfo;
	}
	public void setTitleinfo(Pinfo titleinfo) {
		this.titleinfo = titleinfo;
	}
	public Pinfo getMainbarinfo() {
		return mainbarinfo;
	}
	public void setMainbarinfo(Pinfo mainbarinfo) {
		this.mainbarinfo = mainbarinfo;
	}
	public Pinfo getGuestbarinfo() {
		return guestbarinfo;
	}
	public void setGuestbarinfo(Pinfo guestbarinfo) {
		this.guestbarinfo = guestbarinfo;
	}
	public Pinfo getUnitinfo() {
		return unitinfo;
	}
	public void setUnitinfo(Pinfo unitinfo) {
		this.unitinfo = unitinfo;
	}
	public Pinfo getExpsinfo() {
		return expsinfo;
	}
	public void setExpsinfo(Pinfo expsinfo) {
		this.expsinfo = expsinfo;
	}
	public List<CalBean> getCalculateInfo() {
		return calculateInfo;
	}
	public void setCalculateInfo(List<CalBean> calculateInfo) {
		this.calculateInfo = calculateInfo;
	}
	public List<CheBean> getCheckinfo() {
		return checkinfo;
	}
	public void setCheckinfo(List<CheBean> checkinfo) {
		this.checkinfo = checkinfo;
	}
	public List<Distinguish> getRangeList() {
		return rangeList;
	}
	public void setRangeList(List<Distinguish> rangeList) {
		this.rangeList = rangeList;
	}
	public List<FilterInfo> getFilterList() {
		return filterList;
	}
	public void setFilterList(List<FilterInfo> filterList) {
		this.filterList = filterList;
	}

}
