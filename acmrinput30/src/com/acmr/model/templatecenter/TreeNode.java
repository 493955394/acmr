package com.acmr.model.templatecenter;

import java.io.Serializable;

/**
 * 通用树结点信息
 * 
 * @author xufeng
 *
 */
public class TreeNode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String isvar;// 是否使用变量
	private String varopt;// 变量位置
	private String id; // 树结点id
	private String pid; // 父结点id
	private String scope;//变量范围
	private boolean drag;
	private String name; // 树结点名称
	private boolean isParent; // 是否有子结点，有子结点为true，否则为false
	private boolean chkDisabled;// 判断节点是否可被选择,可以被选择为true,不可以被选择为false
	private boolean checked = false; // 是否勾选，默认true
	private String iconSkin; // 图标
	private String p1; // 扩展字段
	private String p2;
	private String p3;
	private String p4;
	private String p5;
	private Object obj; // 各业务需要的其它对象
	private String open;
	private String unitCode;// 节点-指标单位代码
	private String unitName;// 节点-指标单位名称
	private String ifData;// 是否是数据
	private String classify;
	private boolean childOuter;
	private String templateId;
	private String isTable;
	private String path;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isDrag() {
		return drag;
	}

	public void setDrag(boolean drag) {
		this.drag = drag;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getIsvar() {
		return isvar;
	}

	public void setIsvar(String isvar) {
		this.isvar = isvar;
	}

	public String getVaropt() {
		return varopt;
	}

	public void setVaropt(String varopt) {
		this.varopt = varopt;
	}

	public String getIsTable() {
		return isTable;
	}

	public void setIsTable(String isTable) {
		this.isTable = isTable;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getIfData() {
		return ifData;
	}

	public void setIfData(String ifData) {
		this.ifData = ifData;
	}

	public String getClassify() {
		return classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
	}

	public boolean isChildOuter() {
		return childOuter;
	}

	public void setChildOuter(boolean childOuter) {
		this.childOuter = childOuter;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPId() {
		return pid;
	}

	public void setPId(String pId) {
		this.pid = pId;
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

	public boolean isChkDisabled() {
		return chkDisabled;
	}

	public void setChkDisabled(boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

	public String getP3() {
		return p3;
	}

	public void setP3(String p3) {
		this.p3 = p3;
	}

	public String getP4() {
		return p4;
	}

	public void setP4(String p4) {
		this.p4 = p4;
	}

	public String getP5() {
		return p5;
	}

	public void setP5(String p5) {
		this.p5 = p5;
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getIconSkin() {
		return iconSkin;
	}

	public void setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
	}
}
