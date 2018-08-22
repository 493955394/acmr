package com.acmr.model.adv;

import java.io.Serializable;

import acmr.cubeinput.service.cubeinput.entity.CubeNode;


public class AdvTreeNode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2304483452602388154L;
	private String id = "";
	private String pid = "";
	private String name = "";
	private String wd = ""; // 所属维度
	private String dbcode = ""; // 所属数据库
	private boolean isParent = false;
	private String iconSkin = ""; // 图标
	private CubeNode cubenode = new CubeNode();

	// add by wjp
	private boolean open;
	private String unit;

	private String exp; // 解释

	// caosl 添加
	private String ifData;// 是否是数据

	public String getIfData() {
		return ifData;
	}

	public void setIfData(String ifData) {
		this.ifData = ifData;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	public String getWd() {
		return wd;
	}

	public void setWd(String wd) {
		this.wd = wd;
	}

	public String getDbcode() {
		return dbcode;
	}

	public void setDbcode(String dbcode) {
		this.dbcode = dbcode;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getIconSkin() {
		return iconSkin;
	}

	public void setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
	}

	public CubeNode getCubenode() {
		return cubenode;
	}

	public void setCubenode(CubeNode cubenode) {
		this.cubenode = cubenode;
	}

	public AdvTreeNode(String id, String pid, String name, String wd, String dbcode, String unit, boolean isParent, boolean open) {
		super();
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.wd = wd;
		this.unit = unit;
		this.dbcode = dbcode;
		this.isParent = isParent;
		this.open = open;
	}

	public AdvTreeNode() {
	}

}
