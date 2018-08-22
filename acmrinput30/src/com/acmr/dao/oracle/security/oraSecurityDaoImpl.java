package com.acmr.dao.oracle.security;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import acmr.data.DataSql;
import acmr.util.DataTable;
import acmr.util.DataTableRow;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.security.ISecurityDao;
import com.acmr.model.security.Department;
import com.acmr.model.security.Role;
import com.acmr.model.security.User;
import com.acmr.model.templatecenter.TreeNode;

public class oraSecurityDaoImpl implements ISecurityDao {

	@Override
	public DataTable getDep() {
		String sql1 = "select * from tb_right_department where ifclose='0' order by sortcode";
		return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql1);
	}

	@Override
	public DataTable getUser(String userid) {
		String sql1 = "select * from tb_right_user where userid=?";
		return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql1, new String[] { userid });
	}

	@Override
	public DataTable getRole() {
		String sql1 = "select * from tb_right_role order by sortcode";
		return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql1);
	}

	@Override
	public DataTable getRight() {
		String sql1 = "select * from tb_right_right  order by sortcode";
		return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql1);
	}

	@Override
	public DataTable getMenu() {
		String sql1 = "select * from tb_web_menu  where ifclose='0' order by sortcode";
		return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql1);

	}

	@Override
	public int addDep(Department dep) {
		String sql1 = "insert into tb_right_department (code,cname,procode,ifclose,updatetime,updateuserid,memo,sortcode,flowcode,flowtype) values(?,?,?,?,?,?,?,?,?,?)";
		List<Object> parms = new ArrayList<Object>();
		parms.add(dep.getCode());
		parms.add(dep.getCname());
		parms.add(dep.getParent());
		parms.add("0");
		parms.add(new Date(System.currentTimeMillis()));
		parms.add(dep.getUpdateuserid());
		parms.add(dep.getMemo());
		parms.add(dep.getSortcode());
		parms.add(dep.getFlowcode());
		parms.add(dep.getFlowtype());
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, parms.toArray());
	}

	@Override
	public int updateDep(Department dep) {
		String sql1 = "";
		List<Object> parms = new ArrayList<Object>();
		if (dep.getCname() != null) {
			sql1 += ",cname=?";
			parms.add(dep.getCname());
		}
		if (dep.getParent() != null) {
			sql1 += ",procode=?";
			parms.add(dep.getParent());
		}
		
		if (dep.getMemo() != null) {
			sql1 += ",memo=?";

			parms.add(dep.getMemo());
		}
		if (dep.getSortcode() != null) {
			sql1 += ",sortcode=?";

			parms.add(dep.getSortcode());
		}
		if (dep.getFlowcode() != null) {
			sql1 += ",flowcode=?";

			parms.add(dep.getFlowcode());
		}
		if (dep.getFlowtype() != null) {
			sql1 += ",flowtype=?";
			parms.add(dep.getFlowtype());
		}
		if (sql1.length() > 0) {
			sql1 += ",updatetime=?";
			parms.add(new Date(System.currentTimeMillis()));

		}
		if (sql1.equals("")) {
			return 0;
		}

		sql1 = "update tb_right_department set " + sql1.substring(1) + " where code=?";
		parms.add(dep.getCode());
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, parms.toArray());
	}

	@Override
	public int addUser(User user, String pwd) {
		String sql1 = "insert into tb_right_user (userid,pwd,depcode,sex,duty,tel,updatetime,ifclose,memo,email,cname,updateuserid,flowcode,flowtype,roles) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object> parms = new ArrayList<Object>();
		parms.add(user.getUserid());
		parms.add(pwd);
		parms.add(user.getDepcode());
		parms.add(user.getSex());
		parms.add(user.getDuty());
		parms.add(user.getTel());
		parms.add(new java.sql.Date(System.currentTimeMillis()));
		parms.add("0");
		parms.add(user.getMemo());
		parms.add(user.getEmail());
		parms.add(user.getUsername());
		parms.add(user.getUpdateuserid());
		parms.add(user.getFlowcode());
		parms.add(user.getFlowtype());
		parms.add(user.getRoles().toString());
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, parms.toArray());
	}

	@Override
	public int updateUser(User user) {
		String sql1 = "";
		List<Object> parms = new ArrayList<Object>();
		if (user.getDepcode() != null) {
			sql1 += ",depcode=?";
			parms.add(user.getDepcode());
		}
		if (user.getDuty() != null) {
			sql1 += ",duty=?";
			parms.add(user.getDuty());
		}
		 if (user.getEmail() != null) {
			 sql1 += ",email=?";
			 parms.add(user.getEmail());
		 }
		 
		if (user.getFlowcode() != null) {
			sql1 += ",flowcode=?";
			parms.add(user.getFlowcode());
		}
		if (user.getFlowtype() != null) {
			sql1 += ",flowtype=?";
			parms.add(user.getFlowtype());
		}
		if (user.getMemo() != null) {
			sql1 += ",memo=?";
			parms.add(user.getMemo());
		}
		if (user.getSex() != null) {
			sql1 += ",sex=?";
			parms.add(user.getSex());
		}

		if (user.getTel() != null) {
			sql1 += ",tel=?";
			parms.add(user.getTel());
		}
		if (user.getUsername() != null) {
			sql1 += ",cname=?";
			parms.add(user.getUsername());
		}			
		if(user.getRoles()!= null){
			sql1+=",roles=?";
			StringBuffer sbf=new StringBuffer("");
			for(int i=0;i<user.getRoles().size();i++){
				if(i==user.getRoles().size()-1){
					sbf.append(user.getRoles().get(i));
				}else{
					sbf.append(user.getRoles().get(i)+",");
				}
			}
			parms.add(sbf.toString());
		}
		if (sql1.length() > 0) {
			sql1 += ",updatetime=?";
			parms.add(new Date(System.currentTimeMillis()));

		}
		if (sql1.equals("")) {
			return 0;
		}
		sql1 = "update tb_right_user set " + sql1.substring(1) + " where userid=?";
		parms.add(user.getUserid());
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, parms.toArray());

	}

	@Override
	public int updateUserPwd(String userid, String pwd) {
		String sql1 = "update tb_right_user set pwd=? where userid=?";
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, new String[] { pwd, userid });
	}

	@Override
	public int addRole(Role role) {
		String sql1 = "insert into tb_right_role (code,cname,updatetime,ifclose,memo,updateuserid,rights,sortcode) values (?,?,?,?,?,?,?,?) ";
		List<Object> parms = new ArrayList<Object>();
		parms.add(role.getCode());
		parms.add(role.getCname());
		parms.add(new java.sql.Date(System.currentTimeMillis()));
		parms.add("0");
		parms.add(role.getMemo());
		parms.add(role.getUpdateuserid());
		parms.add(role.getRights().toString());
		parms.add(role.getSortcode());
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, parms.toArray());
	}

	

	public int updateRoleInfo(Role role) {
		String sql1 = "";
		List<Object> parms = new ArrayList<Object>();
		if (role.getCname() != null) {
			sql1 += ",cname=?";
			parms.add(role.getCname());
		}
		if(role.getMemo()!=null){
			sql1+=",memo=?";
			parms.add(role.getMemo());
		}	
		if (sql1.length() > 0) {
			sql1 += ",updatetime=?";
			parms.add(new Date(System.currentTimeMillis()));

		}
		if (sql1.equals("")) {
			return 0;
		}
		sql1 = "update tb_right_role set " + sql1.substring(1) + " where code=?";
		parms.add(role.getCode());
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, parms.toArray());
	}
	
	@Override
	public int updateRole(Role role) {
		String sql1 = "";
		List<Object> parms = new ArrayList<Object>();
		if (role.getCname() != null) {
			sql1 += ",cname=?";
			parms.add(role.getCname());
		}
		if(role.getMemo()!=null){
			sql1+=",memo=?";
			parms.add(role.getMemo());
		}
		if(role.getState()!=null){
			sql1+=",ifclose=?";
			if("启用".equals(role.getState())){
				parms.add("0");
			}else{
				parms.add("1");
			}
		}
		if(role.getRights()!=null){
			sql1+=",rights=?";
			StringBuffer sbf=new StringBuffer("");
			for(int i=0;i<role.getRights().size();i++){
				if(i==role.getRights().size()-1){
					sbf.append(role.getRights().get(i));
				}else{
					sbf.append(role.getRights().get(i)+",");
				}
			}
			parms.add(sbf.toString());
		}
		if (sql1.length() > 0) {
			sql1 += ",updatetime=?";
			parms.add(new Date(System.currentTimeMillis()));

		}
		if (sql1.equals("")) {
			return 0;
		}
		sql1 = "update tb_right_role set " + sql1.substring(1) + " where code=?";
		parms.add(role.getCode());
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, parms.toArray());
	}

	@Override
	public DataTable getUsers(String depcode) {
		List<Object> parms = new ArrayList<Object>();
		String sql1 = "select * from tb_right_user where ifclose='0' ";
		if(depcode!= null){
			sql1 += " and  depcode=? ";
			parms.add(depcode);
		}
		sql1 += "order by userid";
		return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql1, parms.toArray());
	}

	@Override
	public int delDep(String depcode) {
		String sql1="update tb_right_department set ifclose='1' where code=?";
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, new String[]{depcode});
	}

	@Override
	public int delUser(String userid) {
		String sql1="update tb_right_user set ifclose='1' where userid=?";
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, new String[]{userid});
	}

	@Override
	public int delRole(String code) {
		String sql1="update tb_right_role set ifclose='1' where code=?";
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, new String[]{code});

	}

	@Override
	public int moveDep(String depcode1, String depcode2, String sortcode1, String sortcode2) {
		List<DataSql> lists=new ArrayList<DataSql>();
		String sql1="update tb_right_department set sortcode=? where code=?";
		lists.add(new DataSql(sql1,new String[]{sortcode1,depcode1}));
		lists.add(new DataSql(sql1,new String[]{sortcode2,depcode2}));
		return AcmrInputDPFactor.getQuickQuery().executeSql(lists); 
	}

	@Override
	public DataTable getUsersByEmail(String email) {
		String sql1 = "select * from tb_right_user where email=?";
		return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql1, new String[] { email });
	}
	
	/**
	 * 从角色关系表中根据角色删除相应人员
	 * @param rolecode 角色编码
	 * @param userids 用户Id 数组 
	 * @return 操作条数
	 * @throws DaoException
	 */
	public int deleteUserFromRole(String rolecode, String[] userids)  {
		StringBuffer sbf = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sbf.append("delete from tb_right_user_role t where t.rolecode = ? and t.userid in ( ");
		params.add(rolecode);
		for (int i = 0; i < userids.length; i++) {
			sbf.append("?");
			if (i != userids.length - 1) {
				sbf.append(",");
			}
			params.add(userids[i]);
		}
		sbf.append(")");
		
		return AcmrInputDPFactor.getQuickQuery().executeSql(sbf.toString(), params.toArray());

	}
	
	public int deleteUserRole(String userid)  {
		StringBuffer sbf = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sbf.append("delete from tb_right_user_role t where t.userid =?");
		params.add(userid);	
		return AcmrInputDPFactor.getQuickQuery().executeSql(sbf.toString(), params.toArray());

	}
	
	/**
	 * 插入角色用户关系
	 * @param rolecode 角色编码
	 * @param userid 用户Id
	 * @return 操作条数
	 * @throws DaoException
	 */
	public int insertUserToRole(String rolecode, String userid)  {
		String sql = "insert into tb_right_user_role(ID, USERID, ROLECODE,state) values(sq_user_role.nextval,?,?,'1')";
		List<Object> params = new ArrayList<Object>();
		params.add(userid);
		params.add(rolecode);	
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql, params.toArray());

	}
	
//	/**
//	 * 当前角色下，机构人员信息树查询
//	 * @param deptId 组织编码
//	 * @param rolecode 角色编码
//	 * @return 机构人员
//	 */
//
//	public List<TreeNode> findDepUserTree(String rolecode, String deptId) {
//		List<TreeNode> result = new ArrayList<TreeNode>();	
//			String userSql="select t.userid as id ,t.cname  as name,t.depcode  as depcode, (case (select count(*) from tb_right_user_role r where r.rolecode = ? and r.userid = t.userid and state='1') when 0 then 'false' else 'true' end) as checked from tb_right_user t where t.ifclose = '0'";
//			List<Object> params = new ArrayList<Object>();
//			params.add(rolecode);
//
//			DataTable tbUser=AcmrInputDPFactor.getQuickQuery().getDataTableSql(userSql, params.toArray());
//			List<DataTableRow> UserRows = tbUser.getRows();
//			for (DataTableRow row:UserRows) {
//				TreeNode treeNode = new TreeNode();
//				treeNode.setId(row.getString("id"));
//				treeNode.setPId(row.getString("depcode"));
//				treeNode.setName(row.getString("name"));
//				treeNode.setIsParent(false);
//				treeNode.setChecked(Boolean.valueOf(row.getString("checked")));
//				treeNode.setP1("user");
//				result.add(treeNode);
//			}	
//		// 查询组织
//		StringBuffer sbf = new StringBuffer(
//				"select t.code as id, t.procode as pid,t.cname as name  ,(case ( select count(*) from (select * from tb_right_user t1 inner join tb_right_user_role t2 on t1.userid=t2.userid where t2.rolecode=? and t1.ifclose='1') r where r.DEPCODE =t.code ) when 0 then 'false' else 'true' end )as checked from tb_right_department t where t.ifclose = '0'");
//		DataTable tbDep =AcmrInputDPFactor.getQuickQuery().getDataTableSql(sbf.toString(), params.toArray());		
//		List<DataTableRow> DepRows = tbDep.getRows();
//		for (DataTableRow row:DepRows) {
//			TreeNode treeNode = new TreeNode();
//			treeNode.setId(row.getString("id"));
//			treeNode.setPId(row.getString("pid"));
//			treeNode.setName(row.getString("name"));
//			treeNode.setIsParent(true);		
//			treeNode.setChecked(Boolean.valueOf(row.getString("checked")));
//			result.add(treeNode);
//		}
//		
//		return result;
//	}
	
	/**
	 * 当前角色下，机构人员信息树查询
	 * @param deptId 组织编码
	 * @param rolecode 角色编码
	 * @return 机构人员
	 */

	public List<TreeNode> findDepUserTree(String rolecode, String deptId) {
		List<TreeNode> result = new ArrayList<TreeNode>();	
			String userSql="select t.userid as id ,t.cname  as name,t.depcode  as depcode, (case  when (','||t.roles||','  like ?) then 'true' else 'false' end) as checked  from tb_right_user t where t.ifclose = '0'";
			List<Object> params = new ArrayList<Object>();
			params.add("%,"+rolecode+",%");

			DataTable tbUser=AcmrInputDPFactor.getQuickQuery().getDataTableSql(userSql, params.toArray());
			List<DataTableRow> UserRows = tbUser.getRows();
			for (DataTableRow row:UserRows) {
				TreeNode treeNode = new TreeNode();
				treeNode.setId(row.getString("id"));
				treeNode.setPId(row.getString("depcode"));
				treeNode.setName(row.getString("name"));
				treeNode.setIsParent(false);
				treeNode.setChecked(Boolean.valueOf(row.getString("checked")));
				treeNode.setP1("user");
				result.add(treeNode);
			}	
		// 查询组织
		StringBuffer sbf = new StringBuffer(
				"select t.code as id, t.procode as pid,t.cname as name  ,(case ( select count(*) from  tb_right_user t1 where ','||t1.ROLES||',' LIKE ? and t1.DEPCODE=t.code and t1.ifclose='0' ) when 0 then 'false' else 'true' end )as checked from tb_right_department t where t.ifclose = '0'");
		DataTable tbDep =AcmrInputDPFactor.getQuickQuery().getDataTableSql(sbf.toString(), params.toArray());		
		List<DataTableRow> DepRows = tbDep.getRows();
		for (DataTableRow row:DepRows) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(row.getString("id"));
			treeNode.setPId(row.getString("pid"));
			treeNode.setName(row.getString("name"));
			treeNode.setIsParent(true);		
			treeNode.setChecked(Boolean.valueOf(row.getString("checked")));
			result.add(treeNode);
		}
		
		return result;
	}

}
