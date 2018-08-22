package com.acmr.model.pub;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 人员模型，该模型包括该人的人员信息，角色信息，部门信息
 * 
 * @author xufeng
 *
 */
public class UserModel extends User implements Serializable {
	private static final long serialVersionUID = 1284689996926139651L;
	private RightDepartment department; // 该人所属部门
	private List<RightRole> roles; // 所属角色
	private List<MenuVo> menus; // 所拥有菜单

	private Map<String, Integer> righthrefs;// 能进入的页面地址列表

	public RightDepartment getDepartment() {
		return department;
	}

	public void setDepartment(RightDepartment department) {
		this.department = department;
	}

	public List<RightRole> getRoles() {
		return roles;
	}

	public void setRoles(List<RightRole> roles) {
		this.roles = roles;
	}

	public List<MenuVo> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuVo> menus) {
		this.menus = menus;
		righthrefs = new HashMap<String, Integer>();
		doRightHrefs(this.menus);
	}

	public Map<String, Integer> getRighthrefs() {
		return righthrefs;
	}

	private void doRightHrefs(List<MenuVo> ms) {
		if (ms == null) {
			return;
		}
		for (int i = 0; i < ms.size(); i++) {
			MenuVo m1 = ms.get(i);
			String subhrefs = m1.getSubhrefs() + "," + m1.getHref();
			String s[] = subhrefs.split(",");
			for (int j = 0; j < s.length; j++) {
				String href1 = s[j];
				if (href1 != null && !href1.equals("") && !href1.equals("/")) {
					href1 = href1.toLowerCase();
					righthrefs.put(href1, 1);
				}
			}
			doRightHrefs(m1.getChildren());
		}
	}

}
