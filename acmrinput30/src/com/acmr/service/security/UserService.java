package com.acmr.service.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.acmr.model.pub.ZTreeNode;
import com.acmr.model.security.Department;
import com.acmr.model.security.Role;
import com.acmr.model.security.User;

public class UserService {
	public static List<User> getDepUsers(String depcode) {
		return SecurityService.fator.getInstance().getDepUsers(depcode);
	}

	public static User getUserInfo(String name) {
		return SecurityService.fator.getInstance().getUserInfo(name);
	}

	public static List<ZTreeNode> getSubUsers(String pcode, boolean selectall) {
		List<ZTreeNode> lists = SecurityService.fator.getInstance().getsubDepartMent(pcode);
		List<User> users = SecurityService.fator.getInstance().getDepUsers(pcode);
		if (selectall) {
			for (int i = 0; i < lists.size(); i++) {
				lists.get(i).setCanselect(true);
				lists.get(i).setChkDisabled(false);
			}
		}
		for (int i = 0; i < lists.size(); i++) {
			ZTreeNode node1 = lists.get(i);
			List<User> Depusers = SecurityService.fator.getInstance().getDepUsers(node1.getId());
			if ((SecurityService.fator.getInstance().getsubDepartMent(node1.getId()) != null && SecurityService.fator.getInstance().getsubDepartMent(node1.getId()).size() != 0) || (Depusers != null && Depusers.size() != 0)) {
				node1.setIsParent(true);
				node1.setChkDisabled(true);
			}
		}
		for (int i = 0; i < users.size(); i++) {
			User user1 = users.get(i);
			ZTreeNode node1 = new ZTreeNode(user1.getUserid(), pcode, user1.getUsername(), false);
			node1.setCanselect(true);
			node1.setChkDisabled(false);
			lists.add(node1);
		}
		return lists;
	}

	public static List<ZTreeNode> getSubUsersRole(String roleCode) {
		List<Department> depList = SecurityService.fator.getInstance().getDeps();
		List<User> users = SecurityService.fator.getInstance().getDepUsers(null);
		List<ZTreeNode> result = new ArrayList<ZTreeNode>();
		ZTreeNode node = null;
		for (int i = 0; i < depList.size(); i++) {
			if (!"".equals(depList.get(i).getCode())) {
				node = new ZTreeNode(depList.get(i).getCode(), depList.get(i).getParent(), depList.get(i).getCname(), true);
				node.setCanselect(true);
				result.add(node);
			}
		}

		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			node = new ZTreeNode(user.getUserid(), user.getDepcode(), user.getUsername(), false);
			List<String> roleCodeList = user.getRoles();
			for (int j = 0; j < roleCodeList.size(); j++) {
				if (roleCodeList.get(j).equals(roleCode)) {
					node.setChecked(true);
					break;
				}
			}
			node.setCanselect(true);
			result.add(node);
		}
		return result;
	}

	public static User getCurrentUser() {
		return SecurityService.fator.getInstance().getCurrentUser();
	}

	public static List<User> Finduser(String depcode, String findvalue) {
		List<User> lists = new ArrayList<User>();
		boolean isfind = false;
		if (!findvalue.equals("")) {
			isfind = true;
		}
		List<User> users = getDepUsers(depcode);
		findvalue = findvalue.toLowerCase();
		if (!isfind) {
			lists.addAll(users);
			return lists;
		} else {
			for (int i = 0; i < users.size(); i++) {
				User user1 = users.get(i);
				if (user1.getUserid().toLowerCase().indexOf(findvalue) == 0 || user1.getUsername().toLowerCase().indexOf(findvalue) >= 0) {
					lists.add(user1);
				}
			}
			Department deps = DepartmentService.getDepartment(depcode);
			for (int i = 0; i < deps.getChilds().size(); i++) {
				String depcode1 = deps.getChilds().get(i);
				lists.addAll(Finduser(depcode1, findvalue));
			}
		}
		return lists;
	}

	public static int addUser(User user, String pwd) {
		return SecurityService.fator.getInstance().addUser(user, pwd);
	}

	public static int updateUser(User user) {
		return SecurityService.fator.getInstance().updateUser(user);
	}

	public static int delUser(String userid) {
		return SecurityService.fator.getInstance().delUser(userid);
	}

	public static int updateUserPwd(String userid, String pwd) {
		return SecurityService.fator.getInstance().updateUserPwd(userid, pwd);
	}

	public static List<User> findUserByEmail(String email) {
		List<User> userList = SecurityService.fator.getInstance().getUserByEmail(email);
		return userList;
	}

	public static List<ZTreeNode> getRoleTree(String id) {
		List<ZTreeNode> result = new ArrayList<ZTreeNode>();
		User user = SecurityService.fator.getInstance().getUserInfo(id);
		List<Role> roleList = SecurityService.fator.getInstance().getRoles();
		ZTreeNode node = null;
		for (int i = 0; i < roleList.size(); i++) {
			node = new ZTreeNode(roleList.get(i).getCode(), "", roleList.get(i).getCname(), false);
			node.setCanselect(true);
			if (user.getRoles().indexOf(roleList.get(i).getCode()) != -1) {
				node.setChecked(true);
			}
			result.add(node);
		}
		return result;
	}

	public static Integer updateUserToRole(String userId, String roleValue) {
		User user = new User();
		List<String> list = new ArrayList<String>();
		Collections.addAll(list, roleValue.split(","));
		user.setUserid(userId);
		user.setRoles(list);
		int result = SecurityService.fator.getInstance().updateUser(user);
		// SecurityDao.Fator.getInstance().getSecurityDao().deleteUserRole(userId);
		// for(int i=0;i<list.size();i++){
		//
		// SecurityDao.Fator.getInstance().getSecurityDao().insertUserToRole(list.get(i), userId);
		// }
		// if (result > 0) {
		// SecurityService.fator.getInstance().loadRole();
		// }
		return result;
	}
}
