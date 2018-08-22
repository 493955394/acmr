package com.acmr.model.marker;

import com.acmr.model.range.Pinfo;

/**
 * 标记区对象 前台使用
 * 
 * @author chenyf
 *
 */
public class MarkerInfoB {

	private Pinfo title; // 标题

	private Pinfo zhu; // 主栏

	private Pinfo bin; // 宾栏

	private Pinfo unit; // 单位

	private Pinfo exps; // 备注

	public MarkerInfoB() {
		// TODO Auto-generated constructor stub
	}

	public MarkerInfoB(Pinfo title, Pinfo zhu, Pinfo bin, Pinfo unit, Pinfo exps) {
		super();
		this.title = title;
		this.zhu = zhu;
		this.bin = bin;
		this.unit = unit;
		this.exps = exps;
	}

	public Pinfo getZhu() {
		return zhu;
	}

	public void setZhu(Pinfo zhu) {
		this.zhu = zhu;
	}

	public Pinfo getBin() {
		return bin;
	}

	public void setBin(Pinfo bin) {
		this.bin = bin;
	}

	public Pinfo getTitle() {
		return title;
	}

	public void setTitle(Pinfo title) {
		this.title = title;
	}

	public Pinfo getUnit() {
		return unit;
	}

	public void setUnit(Pinfo unit) {
		this.unit = unit;
	}

	public Pinfo getExps() {
		return exps;
	}

	public void setExps(Pinfo exps) {
		this.exps = exps;
	}

}
