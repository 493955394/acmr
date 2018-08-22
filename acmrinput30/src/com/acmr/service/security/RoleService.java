package com.acmr.service.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import acmr.util.ListHashMap;

import com.acmr.dao.security.SecurityDao;
import com.acmr.model.pub.ZTreeNode;
import com.acmr.model.security.Right;
import com.acmr.model.security.Role;
import com.acmr.model.security.User;
import com.acmr.model.templatecenter.TreeNode;

public class RoleService {

	/**
	 * 查询单个角色信息
	 * @param code 部门编码
	 * @return
	 */
	public static Role getRole(String code) {
		ListHashMap<Role> lists = SecurityService.fator.getInstance().getRoles();
		if (lists.containsKey(code)) {
			return lists.get(code);
		}
		return null;
	}
	public static ListHashMap<Role> getRoles(){
		return SecurityService.fator.getInstance().getRoles();
	}
	public static List<Role> findRole(String value) {
		ListHashMap<Role> roles = SecurityService.fator.getInstance().getRoles();
		List<Role> roleList = new ArrayList<Role>();
		Role role=null;
		if (value.equals("")) {
			return roles;
		}
		for (int i = 0; i < roles.size(); i++) {
			role = roles.get(i);
			if (role.getCode().toLowerCase().indexOf(value.toLowerCase()) >= 0 || role.getCname().toLowerCase().indexOf(value.toLowerCase()) >= 0) {
				roleList.add(role);
			}
		}
		return roleList;
	}
	public static int switchState(String code){
		Role role = getRole(code);
		Role newRole = new Role();
		int result =0;
		if("启用".equals(role.getState())){
			role.setState("停用");
		}else{
			role.setState("启用");
		}
		newRole.setCode(code);
		newRole.setState(role.getState());
		newRole.setRights(role.getRights());
		result = SecurityService.fator.getInstance().updateRole(newRole);
		return result;
	}
	public static int updateRole(Role role){
		return SecurityService.fator.getInstance().updateRole(role);
	}
	public static int updateRoleInfo(Role role){
		return SecurityService.fator.getInstance().updateRoleInfo(role);
	}
	public static List<ZTreeNode> getRightTree(String roleCode) {
		ListHashMap<Role> roleList =  SecurityService.fator.getInstance().getRoles();
		ListHashMap<Right> rightList = SecurityService.fator.getInstance().getRights();
		List<ZTreeNode> result = new ArrayList<ZTreeNode>();
		Role role = roleList.get(roleCode);
		List<String> rights= role.getRights();
		ZTreeNode node = null;
		for(int i=0; i< rightList.size(); i++){
			if("".equals(rightList.get(i).getCode())){
				continue;
			}
			node = new ZTreeNode(rightList.get(i).getCode(),rightList.get(i).getParent(),rightList.get(i).getCname() , rightList.get(i).getChilds().size() > 0);
			for(int j=0 ;j<rights.size() ;j++){
				if(rights.get(j).equals(rightList.get(i).getCode())){
					node.setChecked(true);
					break;
				}
			}
			result.add(node);
		}
		return result;
	}
	public static int setRoleRight(String roleCode, String insertNodes) {
		Role role = new Role();
		role.setCode(roleCode);
		List<String> list = new ArrayList<String>();
		Collections.addAll(list, insertNodes.split(","));
		role.setRights(list);
		int result= SecurityService.fator.getInstance().updateRole(role);
		return result;
	}
	public static int addRole(Role role){
		return SecurityService.fator.getInstance().addRole(role);
	}
	/**
	 * 更新用户角色关系
	 * @param rolecode 角色编码
	 * @param deleteUserids 撤销用户编码
	 * @param insertUserids 新增用户编码
	 * @throws DaoException
	 */
	public static void updateUserToRole(String rolecode, String[] deleteUserids, String[] insertUserids)  {
		if (deleteUserids != null){
			//SecurityDao.Fator.getInstance().getSecurityDao().deleteUserFromRole(rolecode, deleteUserids);
			for(int i=0;i<deleteUserids.length;i++){
				User user1=UserService.getUserInfo(deleteUserids[i]);
			    List<String> roles = user1.getRoles();
			    for(int j=roles.size()-1;j>=0;j--){
			    	if(roles.get(j).equals(rolecode)){
			    		roles.remove(j);
			    	}
			    }
			    SecurityService.fator.getInstance().updateUser(user1);
			} 		
		}
		if (insertUserids != null){
			for (int i = 0; i < insertUserids.length; i++) {
				//SecurityDao.Fator.getInstance().getSecurityDao().insertUserToRole(rolecode, insertUserids[i]);
				 User user1=UserService.getUserInfo(insertUserids[i]);
				 List<String> roles = user1.getRoles();
				  for(int j=roles.size()-1;j>=0;j--){
				    	if(roles.get(j).equals(rolecode)){
				    		roles.remove(j);
				    	}
				    }
				  roles.add(rolecode);
				    SecurityService.fator.getInstance().updateUser(user1);
				
			}
		}
	}
	/**
	 * 查找机构人员树
	 * @param id
	 * @return
	 */
	public static List<TreeNode> findDepUserTree(String rolecode, String id){
		return SecurityDao.Fator.getInstance().getSecurityDao().findDepUserTree(rolecode, id);
	}
}
