package com.acmr.model.pub;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单Vo
 * 
 * @author xufeng
 *
 */
public class MenuVo extends WebMenu {

	private List<MenuVo> children; // 子菜单列表
	/** 展示类型 */
	private String showtype;
	/** 根节点 */
	private String rootCode;

	private String subhrefs;

	public String getRootCode() {
		return rootCode;
	}

	public void setRootCode(String rootCode) {
		this.rootCode = rootCode;
	}

	public String getShowtype() {
		return showtype;
	}

	public void setShowtype(String showtype) {
		this.showtype = showtype;
	}

	public List<MenuVo> getChildren() {
		if (children == null) {
			children = new ArrayList<MenuVo>();
		}
		return children;
	}

	public void setChildren(List<MenuVo> children) {
		this.children = children;
	}

	public String getSubhrefs() {
		return subhrefs;
	}

	public void setSubhrefs(String subhrefs) {
		this.subhrefs = subhrefs;
	}

}
