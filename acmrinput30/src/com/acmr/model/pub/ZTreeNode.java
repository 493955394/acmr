package com.acmr.model.pub;

public class ZTreeNode {

	private String id;
	private String pId;
	private String name;
	private boolean isParent; // 是否是父节点
	private boolean canselect;// 是否能选中
	private boolean chkDisabled; // 是否不可选
	private boolean checked; // check状态
	private boolean candrag; // 是否能拖拽
	private String iconfile; // 图标文件

	private Object data;

	public ZTreeNode() {
		id = "";
		pId = "";
		name = "";
		isParent = false;
		checked = false;
		candrag = false;
		canselect = false;
		chkDisabled = false;
		iconfile = "";
	}

	public ZTreeNode(String id1, String pid1, String name1, boolean isparent1) {
		this();
		id = id1;
		pId = pid1;
		name = name1;
		isParent = isparent1;
	}

	public boolean isChkDisabled() {
		return chkDisabled;
	}

	public void setChkDisabled(boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPId() {
		return pId;
	}

	public void setPId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isparent) {
		this.isParent = isparent;
	}

	public boolean isCanselect() {
		return canselect;
	}

	public void setCanselect(boolean canselect) {
		this.canselect = canselect;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isCandrag() {
		return candrag;
	}

	public void setCandrag(boolean candrag) {
		this.candrag = candrag;
	}

	public String getIconfile() {
		return iconfile;
	}

	public void setIconfile(String iconfile) {
		this.iconfile = iconfile;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
