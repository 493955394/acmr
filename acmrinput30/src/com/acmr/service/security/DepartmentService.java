package com.acmr.service.security;

import java.util.ArrayList;
import java.util.List;

import acmr.util.ListHashMap;

import com.acmr.model.pub.ZTreeNode;
import com.acmr.model.security.Department;

public class DepartmentService {
	public static List<ZTreeNode> getSubDepartments(String pcode) {
		return SecurityService.fator.getInstance().getsubDepartMent(pcode);
	}
	/**
	 * 查询单个部分信息
	 * @param code 部门编码
	 * @return
	 */
	public static Department getDepartment(String code) {
		ListHashMap<Department> lists = SecurityService.fator.getInstance().getDeps();
		if (lists.containsKey(code)) {
			return lists.get(code);
		}
		return null;
	}

	public static List<Department> findDepartment(String pcode, String value) {
		ListHashMap<Department> deps = SecurityService.fator.getInstance().getDeps();
		List<Department> lists = new ArrayList<Department>();
		if (!deps.containsKey(pcode)) {
			return lists;
		}
		boolean isfind = false;
		if (!value.equals("")) {
			isfind = true;
		}

		Department deproot = deps.get(pcode);
		if (!isfind && deproot.getChilds().size() == 0) {
			lists.add(deproot);
			return lists;
		}
		for (int i = 0; i < deproot.getChilds().size(); i++) {
			Department dep = deps.get(deproot.getChilds().get(i));
			if (isfind) {
				if (dep.getCode().toLowerCase().indexOf(value.toLowerCase()) >= 0 || dep.getCname().toLowerCase().indexOf(value.toLowerCase()) >= 0) {
					lists.add(dep);
				}
				lists.addAll(findDepartment(dep.getCode(), value));
			} else {
				lists.add(dep);
			}
		}
		return lists;
	}
	
	public static int delDepartment(String depcode){
		return SecurityService.fator.getInstance().delDepartment(depcode);
	}
	public static int addDepartment(Department dep){
		return SecurityService.fator.getInstance().addDepartment(dep);
	}
	public static int updateDepartment(Department dep){
		return SecurityService.fator.getInstance().updateDepartment(dep);
	}
	public static int moveDepartment(String code1,String code2){
		return SecurityService.fator.getInstance().moveDepartment(code1, code2);
	}
	public static String getNameByCode(String code) {
		ListHashMap<Department> deps = SecurityService.fator.getInstance().getDeps();
		Department dep = deps.get(code);
		if(dep!=null){
			return dep.getCname();
		}else{
			return "";
		}
	}

}
