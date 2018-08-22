package com.acmr.dao.security;

import java.util.List;

import com.acmr.model.security.Department;
import com.acmr.model.security.Role;
import com.acmr.model.security.User;
import com.acmr.model.templatecenter.TreeNode;

import acmr.util.DataTable;

public interface ISecurityDao {

	public DataTable getDep();

	public DataTable getRole();

	public DataTable getRight();

	public DataTable getMenu();

	public int addDep(Department dep);

	public int updateDep(Department dep);

	public int moveDep(String depcode1,String depcode2,String sortcode1,String sortcode2);
	
	public DataTable getUser(String userid);
	
	public DataTable getUsers(String depcode);

	public int addUser(User user, String pwd);

	public int updateUser(User user);

	public int updateUserPwd(String userid, String pwd);

	public int addRole(Role role);

	public int updateRole(Role role);
	public int updateRoleInfo(Role role);
	
	public int delDep(String depcode);
	public int delUser(String userid);
	public int delRole(String code);

	public DataTable getUsersByEmail(String email);
	public int deleteUserFromRole(String rolecode, String[] userids);
	public int insertUserToRole(String rolecode, String userid) ;
	public List<TreeNode> findDepUserTree(String rolecode, String deptId);
	public int deleteUserRole(String userid);
}
