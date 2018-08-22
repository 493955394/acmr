package com.acmr.model.marker;

import com.acmr.model.templatecenter.CellPosition;
import com.acmr.model.templatecenter.PositionInfo;

/**
 * 标记区对象
 * 
 * @author chenyf
 *
 */
public class MarkerInfo {

	private CellPosition title; // 标题

	private PositionInfo zhul; // 主栏

	private PositionInfo binl; // 宾栏

	private PositionInfo unit; // 单位

	private PositionInfo exps; // 备注

	public MarkerInfo() {
		// TODO Auto-generated constructor stub
	}

	public CellPosition getTitle() {
		return title;
	}

	public void setTitle(CellPosition title) {
		this.title = title;
	}

	public PositionInfo getZhul() {
		return zhul;
	}

	public void setZhul(PositionInfo zhul) {
		this.zhul = zhul;
	}

	public PositionInfo getBinl() {
		return binl;
	}

	public void setBinl(PositionInfo binl) {
		this.binl = binl;
	}

	public PositionInfo getUnit() {
		return unit;
	}

	public void setUnit(PositionInfo unit) {
		this.unit = unit;
	}

	public PositionInfo getExps() {
		return exps;
	}

	public void setExps(PositionInfo exps) {
		this.exps = exps;
	}

}
