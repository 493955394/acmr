package com.acmr.service.metadata;

import java.util.List;

/**
 * 树节点对象
 * 
 * @author chenyf
 *
 */
public class Node {

	public String id;
	public String text;
	public String parentId;
	public String orgCode;
	public boolean isExc = false;
	private Children children = new Children();

	// 先序遍历，拼接JSON字符串
	public String toString() {
		String result = "{" + "id : '" + id + "'" + ", text : '" + text + "'";

		if (children != null && children.getSize() != 0) {
			result += ", children : " + children.toString();
		} else {
			result += ", leaf : true";
		}

		return result + "}";
	}

	public void addChild(Node node) {
		this.children.addChild(node);
	}

	public List<Node> getChilds() {
		return this.children.getChilds();
	}

	public boolean isExc() {
		return isExc;
	}

	public void setExc(boolean isExc) {
		this.isExc = isExc;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

}
