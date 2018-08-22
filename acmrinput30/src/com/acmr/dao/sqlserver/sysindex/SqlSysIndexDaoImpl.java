package com.acmr.dao.sqlserver.sysindex;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import acmr.data.DataQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.ListHashMap;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.data.AcmrDataPageDao;
import com.acmr.dao.sysindex.ISysIndexDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.PageBean;
import com.acmr.model.pub.RightDepartment;
import com.acmr.model.pub.TreeNode;
import com.acmr.model.security.Department;
import com.acmr.model.sysindex.OneYearReportStageVoList;
import com.acmr.model.sysindex.SystemIndex;
import com.acmr.model.sysindex.SystemPlan;
import com.acmr.model.templatecenter.TemplateIndex;
import com.acmr.service.security.DepartmentService;
import com.acmr.service.security.UserService;

public class SqlSysIndexDaoImpl implements ISysIndexDao {

	@Override
	public List<TreeNode> getSysIndexTreeByDeptCode(String deptCode, String proCode) {
		// 定义查询sql
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sql.append("select t.code as id,                   ");
	    sql.append("   t.procode as pid,                   ");
	    sql.append("   t.deptcode as deptcode,             ");
	    sql.append("   t.cname as name,                    ");
	    sql.append("   t.ModCode as templateId,            ");
	    sql.append("   t.isTable as isTable,               ");
	    sql.append("   (case (select count(*)              ");
	    sql.append("        from TB_SYSTEM_INDEX p         ");
	    sql.append("       where p.procode = t.code        ");
	    sql.append("         and p.istable = 0)            ");
	    sql.append("     when 0 then                       ");
	    sql.append("      'false'                          ");
	    sql.append("     else                              ");
	    sql.append("      'true'                           ");
	    sql.append("   end) isParent,                      ");
	    sql.append("   (case (select count(1) from TB_RIGHT_DEPARTMENT ");
		sql.append("    where t.deptcode in(?");
		List<Department> dep1 = DepartmentService.findDepartment(deptCode, "");
		params.add(deptCode);
		for (Department department : dep1) {
			sql.append(",?");
			params.add(department.getCode());
		}
		sql.append("	)) ");
		sql.append("     when 0 then                              ");
		sql.append("      'false'                                 ");
		sql.append("     else                                     ");
		sql.append("      'true'                                  ");
		sql.append("   end) chkDisabled                           ");
		sql.append(" from tb_system_index t where state != '-1' and t.istable='0'");
		if (StringUtil.isEmpty(proCode)) {
			sql.append(" and t.procode is null");
		} else {
			sql.append(" and t.procode=?");
			params.add(proCode);
		}
		sql.append(" and (t.code in (?");
		params.add("");
		List<String> currentDepSys = getCurrentDepSys(proCode, deptCode);
		for (int i = 0; i < currentDepSys.size(); i++) {
			sql.append(",?");
			params.add(currentDepSys.get(i));
		}
		sql.append(")) order by t.createtime");
		List<TreeNode> result = new ArrayList<TreeNode>();
		DataQuery dataQuery = AcmrInputDPFactor.getDataPool().getDataQuery();
		try {
			DataTable dt = dataQuery.getDataTableSql(sql.toString(), params.toArray());
			if (dt.getRows() == null && dt.getRows().size() == 0) {
				return null;
			}
			for (DataTableRow row : dt.getRows()) {
				TreeNode treeNode = new TreeNode();
				treeNode.setId(row.getString("id"));
				treeNode.setPId(row.getString("pid"));
				treeNode.setName(row.getString("name"));
				treeNode.setTemplateId(row.getString("templateId"));
				if ("false".equals(row.getString("isParent"))) {
					treeNode.setIsParent(false);
				} else {
					treeNode.setIsParent(true);
				}
				if (!StringUtil.isEmpty(deptCode)) {
					List<String> children = getAllChildDept(deptCode);
					// 确定是否可以点击
					if (children.contains(row.getString("deptcode"))) {
						treeNode.setChkDisabled(false);
					} else {
						treeNode.setChkDisabled(true);
					}
					// 还需要判断其下面有没有制度报表，如果有制度报表，则它的节点为可以点击状态
					String childrenSystemSql = "select code from tb_system_index where procode=? and state!='-1' and istable='1'";
					params.clear();
					params.add(row.getString("id"));
					DataTable dtChildren = dataQuery.getDataTableSql(childrenSystemSql, params.toArray());
					if (dtChildren.getRows() != null && dtChildren.getRows().size() > 0) {// 有数据查出
						treeNode.setChkDisabled(false);
					}
				}
				treeNode.setIsTable(row.getString("isTable"));
				result.add(treeNode);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (dataQuery != null) {
				dataQuery.releaseConnl();
			}
		}
		return result;
	}

	@Override
	public int insert(SystemIndex systemIndex) {
		String sql = "insert into TB_SYSTEM_INDEX(CODE, CNAME, CCNAME, PROCODE, MODCODE, FLOWCODE, " + "SORT, CREATETIME, STATE, MEMO,SORTCODE,DEPTCODE," + "TABLENUM,TABLENAME,DOCNUM,CREATEOFFICE,COMPOFFICE,SYSTYPE,ISTABLE,TaskSort,planId,flowType,PROVIDEOFFICE) " + "values(?, ?, ?, ?, ?,?, ?, getdate(), ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object> params = new ArrayList<Object>();
		params.add(systemIndex.getCode());
		params.add(systemIndex.getCname());
		params.add(systemIndex.getCcName());
		params.add(systemIndex.getProCode());
		params.add(systemIndex.getModCode());
		params.add(systemIndex.getFlowCode());
		params.add(systemIndex.getSort());
		params.add(systemIndex.getState());
		params.add(systemIndex.getMemo());
		params.add(systemIndex.getSortCode());
		params.add(systemIndex.getDeptCode());
		params.add(systemIndex.getTableNum());
		params.add(systemIndex.getTableName());
		params.add(systemIndex.getDocNum());
		params.add(systemIndex.getCreateOffice());
		params.add(systemIndex.getCompoffice());
		params.add(systemIndex.getSysType());
		params.add(systemIndex.getIsTable());
		params.add(systemIndex.getTaskSort());
		params.add(systemIndex.getPlanId());
		params.add(systemIndex.getFlowType());
		params.add(systemIndex.getProvideOffice());
		DataQuery dataQuery = null;
		int result = 0;
		try {
			dataQuery = AcmrInputDPFactor.getDataQuery();
			dataQuery.beginTranse();
			result = dataQuery.executeSql(sql, params.toArray());
			dataQuery.commit();
		} catch (SQLException e) {
			if (null != dataQuery) {
				dataQuery.rollback();
			}
			e.printStackTrace();
		} finally {
			if (null != dataQuery) {
				dataQuery.releaseConnl();
			}
		}
		return result;
	}

	@Override
	public int update(SystemIndex systemIndex) {
		String sql = "update TB_SYSTEM_INDEX set CNAME=?,CCNAME=?, PROCODE=?, MODCODE=?," + "FLOWCODE=?, SORT=?,STATE=?,MEMO=?,SORTCODE=?,DEPTCODE=?," + "TABLENAME=?,DOCNUM=?,CREATEOFFICE=?,COMPOFFICE=?,SYSTYPE=?,ISTABLE=?,taskSort=? ,planId=?,flowType=?,PROVIDEOFFICE=?" + " where CODE=?";
		List<Object> params = new ArrayList<Object>();
		params.add(systemIndex.getCname());
		params.add(systemIndex.getCcName());
		params.add(systemIndex.getProCode());
		params.add(systemIndex.getModCode());
		params.add(systemIndex.getFlowCode());
		params.add(systemIndex.getSort());
		params.add(systemIndex.getState());
		params.add(systemIndex.getMemo());
		params.add(systemIndex.getSortCode());
		params.add(systemIndex.getDeptCode());
		params.add(systemIndex.getTableName());
		params.add(systemIndex.getDocNum());
		params.add(systemIndex.getCreateOffice());
		params.add(systemIndex.getCompoffice());
		params.add(systemIndex.getSysType());
		params.add(systemIndex.getIsTable());
		params.add(systemIndex.getTaskSort());
		params.add(systemIndex.getPlanId());
		params.add(systemIndex.getFlowType());
		params.add(systemIndex.getProvideOffice());
		params.add(systemIndex.getCode());
		DataQuery dataQuery = null;
		int result = 0;
		try {
			dataQuery = AcmrInputDPFactor.getDataQuery();
			dataQuery.beginTranse();
			result = dataQuery.executeSql(sql, params.toArray());
			dataQuery.commit();
		} catch (SQLException e) {
			if (null != dataQuery) {
				dataQuery.rollback();
			}
			e.printStackTrace();
		} finally {
			if (null != dataQuery) {
				dataQuery.releaseConnl();
			}
		}
		return result;
	}

	@Override
	public int delete(String code) {
		String sql = "delete from TB_SYSTEM_INDEX  where code=?";
		List<Object> params = new ArrayList<Object>();
		params.add(code);
		DataQuery dataQuery = null;
		int result = 0;
		try {
			dataQuery = AcmrInputDPFactor.getDataQuery();
			dataQuery.beginTranse();
			result = dataQuery.executeSql(sql, params.toArray());
			dataQuery.commit();
		} catch (SQLException e) {
			if (null != dataQuery) {
				dataQuery.rollback();
			}
			e.printStackTrace();
		} finally {
			if (null != dataQuery) {
				dataQuery.releaseConnl();
			}
		}
		return result;
	}

	@Override
	public PageBean<SystemIndex> findSystemIndex(String isTable, String systemStageType, PageBean<SystemIndex> page, SystemIndex systemIndex,String type) {
		StringBuffer sbf = new StringBuffer();
		List<Object> objs = new ArrayList<Object>();
		sbf.append("select TOP 100 PERCENT * from TB_SYSTEM_INDEX t where 1=1 ");
		if (!StringUtil.isEmpty(systemIndex.getCode())) {
			sbf.append(" and t.CODE like ? ");
			objs.add("%" + systemIndex.getCode() + "%");
		}

		if (!StringUtil.isEmpty(systemIndex.getCname())) {
			sbf.append(" and t.CNAME like ? ");
			objs.add("%" + systemIndex.getCname() + "%");
		}
		if (!StringUtil.isEmpty(systemIndex.getTableNum())) {
			sbf.append(" and t.code like ? ");
			objs.add("%" + systemIndex.getTableNum() + "%");
		}

		if (!StringUtil.isEmpty(systemIndex.getTableName())) {
			sbf.append(" and t.CNAME like ? ");
			objs.add("%" + systemIndex.getTableName() + "%");
		}

		if(type.equals("0")){
			if (!StringUtil.isEmpty(systemIndex.getProCode())) {
				sbf.append(" and t.PROCODE=? ");
				objs.add(systemIndex.getProCode());
			}else{
				sbf.append(" and (t.PROCODE is NULL or  t.PROCODE='') ");
			}
			
		}
		else{
			if (!StringUtil.isEmpty(systemIndex.getProCode())) {
				sbf.append(" and t.PROCODE=? ");
				objs.add(systemIndex.getProCode());
			}
		}
		
		if (!StringUtil.isEmpty(isTable)) {
			sbf.append(" and t.isTable=? ");
			objs.add(isTable);
		}

		String deptCode = UserService.getCurrentUser().getDepcode();
		if (!StringUtil.isEmpty(deptCode)) {
			List<Department> findDepartment = DepartmentService.findDepartment(deptCode, "");
			sbf.append(" and t.deptcode in (?");
			objs.add(deptCode);
			for (Department department : findDepartment) {
				sbf.append(",?");
				objs.add(department.getCode());
			}
			sbf.append(") ");
		}

		// 搜索
		if (!StringUtil.isEmpty(systemIndex.getTableNum()) && !StringUtil.isEmpty(systemIndex.getProCode())) {
			List<SystemIndex> list = findSystemIndexs(systemIndex.getProCode(), systemIndex.getTableNum());
			page = new AcmrDataPageDao<SystemIndex>().findPage(list);
			return page;
		} else if (!StringUtil.isEmpty(systemIndex.getTableName()) && !StringUtil.isEmpty(systemIndex.getProCode())) {
			List<SystemIndex> list = findSystemIndexs(systemIndex.getProCode(), systemIndex.getTableName());
			page = new AcmrDataPageDao<SystemIndex>().findPage(list);
			return page;
		}

		if (!StringUtil.isEmpty(systemStageType)) {
			sbf.append(" and t.systype=? ");
			objs.add(systemStageType);
		}
		sbf.append(" order by createtime asc ");

		page = new AcmrDataPageDao<SystemIndex>().findPage(sbf.toString(), "createtime", objs.toArray());
		page.setData(this.dataToSysIndex(page.getDataTable()));
		return page;
	}

	@Override
	public List<SystemIndex> findAllSystemIndex() {
		String sql = "select * from TB_SYSTEM_INDEX t where t.state = 1 and t.istable = 1";
		DataTable dataTable = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[] {});
		return this.dataToSysIndex(dataTable);
	}

	@Override
	public SystemIndex getSystemIndex(String code) {
		String sql = "select * from TB_SYSTEM_INDEX t where t.CODE = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(code);
		DataTable dataTable = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, params.toArray());
		List<SystemIndex> list = this.dataToSysIndex(dataTable);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public List<TreeNode> findSysTree(String code, String isTable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TreeNode> findAllSystemTree(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TreeNode> findSysTableTree(String code, String sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TreeNode> findSysTabTree(String code, String sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteOrUse(String id) {
		// 查询制度
		SystemIndex sysIndex = getSystemIndex(id);
		// 更新制度计划
		String sql = "update tb_system_index set state = (case when state=1 then 0 else 1 end) where code = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		DataQuery dataQuery = null;
		try {
			dataQuery = AcmrInputDPFactor.getDataQuery();
			dataQuery.beginTranse();
			dataQuery.executeSql(sql, params.toArray());
			dataQuery.commit();
		} catch (SQLException e) {
			if (dataQuery != null) {
				dataQuery.rollback();
			}
			e.printStackTrace();
		} finally {
			if (dataQuery != null) {
				dataQuery.releaseConnl();
			}
		}
		if ("0".equals(sysIndex.getState())) {
			return 1;
		}
		if ("1".equals(sysIndex.getState())) {
			return 2;
		}
		return 0;
	}

	@Override
	public List<RightDepartment> listAllDept() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RightDepartment> listAllDept(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TemplateIndex> findTemplateListBySysCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateSysPlan(SystemIndex systemIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public int isParent(String code) {
		String sql = "select count(code) from tb_system_index where PROCODE = ?";
		DataTable dataTables = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[] { code });
		String count = dataTables.getRows().get(0).getString(0);
		int c = Integer.valueOf(count);
		return c;
	}

	@Override
	public int getCountByCode(String code) {
		String sql = "select count(code) from TB_SYSTEM_INDEX where code = ?";
		DataTable dataTables = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{code});
		return dataTables.getRows().get(0).getint(0);
	}

	@Override
	public int getCountByTableNum(String tableNum) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> getCDeptCodeByPDeptCode(String pDeptCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTable(String sysCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<TreeNode> getParentTree(String code, String deptCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TreeNode> getParentTreeFromSys(String code, String deptCode) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		if (!StringUtil.isEmpty(deptCode)) {// && !isSuper){
			// 不是管理员，需要加权限
			sql.append("select t.code as id,                               ");
			sql.append("   t.procode as pid,                               ");
			sql.append("   t.cname as name,                                ");
			sql.append("   (case (select count(*)  from TB_SYSTEM_INDEX p  ");
			sql.append("       where p.procode = t.code                    ");
			sql.append("         and p.istable = 0)                        ");
			sql.append("     		 when 0 then 'false'                   ");
			sql.append("     		 	else  'true'                       ");
			sql.append("  			 end) isParent,	                       ");
			sql.append("   (case (select count(1) from TB_RIGHT_DEPARTMENT ");
			sql.append("     where t.deptcode in(?");
			List<Department> dep1 = DepartmentService.findDepartment(deptCode, "");
			params.add(deptCode);
			for (Department department : dep1) {
				sql.append(",?");
				params.add(department.getCode());
			}
			sql.append("	)) ");
			sql.append("     when 0 then                              ");
			sql.append("      'false'                                 ");
			sql.append("     else                                     ");
			sql.append("      'true'                                  ");
			sql.append("   end) chkDisabled                          ");
			sql.append("   from TB_SYSTEM_INDEX t                     ");
			sql.append(" where istable = '0' and state = 1 ");

//			sql.append(" and (t.procode in (?");
//			params.add(code);
//			String pCode = code;
//			while (true) {
//				SystemIndex systemIndex = getSystemIndex(pCode);
//				if (null == systemIndex) {
//					break;
//				}
//				pCode = systemIndex.getProCode();
//				if (StringUtil.isEmpty(pCode)) {
//					break;
//				}
//				sql.append(",?");
//				params.add(pCode);
//			}
//			sql.append(") or procode is null )");
//			sql.append(" and (t.code in (?");
			sql.append(" and (t.code in (?");
			// ----
			params.add("");
			List<String> currentDepSys = getCurrentDepSys(code, deptCode);
			for (int i = 0; i < currentDepSys.size(); i++) {
				sql.append(",?");
				params.add(currentDepSys.get(i));
			}
			sql.append(")) order by t.createtime");
		}
		DataQuery dataQuery = AcmrInputDPFactor.getDataPool().getDataQuery();
		DataTable dt = null;
		try {
			dt = dataQuery.getDataTableSql(sql.toString(), params.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (dataQuery != null) {
				dataQuery.releaseConnl();
			}
		}
		List<TreeNode> result = new ArrayList<TreeNode>();
		if (dt.getRows() == null && dt.getRows().size() == 0) {
			return null;
		}
		for (DataTableRow row : dt.getRows()) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(row.getString("id"));
			treeNode.setPId(row.getString("pid"));
			treeNode.setName(row.getString("name"));
			// if ("false".equals(row.getString("open"))) {
			// treeNode.setOpen("false");
			// } else {
			treeNode.setOpen("false");
			// }

			if ("false".equals(row.getString("isParent"))) {
				treeNode.setIsParent(false);
			} else {
				treeNode.setIsParent(true);
			}
			if (!StringUtil.isEmpty(deptCode)) {
				treeNode.setChkDisabled(!Boolean.parseBoolean(row.getString("chkDisabled")));
			}
			result.add(treeNode);
		}
		return result;
	}

	@Override
	public int getPageNum(SystemIndex systemIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SystemPlan getSystemPlan(String syscode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemIndex> getChildren(String sysCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAllChildrenDeption(SystemIndex systemIndex, String deptCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<SystemIndex> getAllChildren(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemIndex> getAllChildrenIndex(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteSysIndexByBatch(String codes) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteSysTableByBatch(String codes) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateAllChildFlow(String code, String flowCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<TreeNode> getParentSystemTree(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TreeNode> getSysTreeByDeptCode(String deptCode, String proCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemIndex> getCanCopySysList(String sort, String deptCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileSuffix(String templateId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getBLOBBycode(String code, String field) {
		DataQuery dataQuery = AcmrInputDPFactor.getDataPool().getDataQuery();
		String sql = "select " + field + " from TB_TEMPLATE_Content where templateCode = ?";
		try {
			ResultSet resultSet = dataQuery.getResultSetSql(sql, new Object[] { code });
			byte[] b = null;
			if (resultSet.next()) {
				try {
					b = resultSet.getString(field).getBytes("utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return b;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataQuery.releaseConnl();
		}
		return null;
	}

	@Override
	public List<String> getYears(String sysCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OneYearReportStageVoList getOneYearPlan(String sysCode, String year) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TreeNode> getSysIndexTree(String deptCode, String proCode, String sysType) {
		// 定义查询sql
		StringBuffer sqlBuff = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sqlBuff.append("select t.code as id, t.procode as pid,t.deptcode as deptcode,t.cname as name,t.ModCode as templateId,t.isTable as isTable, (case (select count(*) from  TB_SYSTEM_INDEX p where p.procode = t.code and p.istable=0) when 0 then 'false' else 'true' end) isParent, ");
		sqlBuff.append(" (case (select count(1) from TB_RIGHT_DEPARTMENT where t.deptcode in (?");
		List<Department> findDepartment = DepartmentService.findDepartment(deptCode, "");
		params.add(deptCode);
		for (Department department : findDepartment) {
			sqlBuff.append(",?");
			params.add(department.getCode());
		}
		sqlBuff.append("))");
		sqlBuff.append(" when 0 then 'false' else 'true' end) chkDisabled ");

		sqlBuff.append(" from tb_system_index t where state != '-1' and t.istable='0'");
		if (StringUtil.isEmpty(proCode)) {
			sqlBuff.append(" and t.procode='' ");
		} else {
			sqlBuff.append(" and t.procode=?");
			params.add(proCode);
		}

		List<TreeNode> result = new ArrayList<TreeNode>();
		DataQuery dataQuery = AcmrInputDPFactor.getDataPool().getDataQuery();
		try {
			DataTable dt = dataQuery.getDataTableSql(sqlBuff.toString(), params.toArray());
			if (dt.getRows() == null && dt.getRows().size() == 0) {
				return null;
			}
			for (DataTableRow row : dt.getRows()) {
				TreeNode treeNode = new TreeNode();
				treeNode.setId(row.getString("id"));
				treeNode.setPId(row.getString("pid"));
				treeNode.setName(row.getString("name"));
				treeNode.setTemplateId(row.getString("templateId"));
				if ("false".equals(row.getString("isParent"))) {
					treeNode.setIsParent(false);
				} else {
					treeNode.setIsParent(true);
				}
				if (!StringUtil.isEmpty(deptCode)) {
					List<String> children = getAllChildDept(deptCode);
					// 确定是否可以点击
					if (children.contains(row.getString("deptcode"))) {
						treeNode.setChkDisabled(false);
					} else {
						treeNode.setChkDisabled(true);
					}
					// 还需要判断其下面有没有制度报表，如果有制度报表，则它的节点为可以点击状态
					String childrenSystemSql = "select code from tb_system_index where procode=? and state!='-1' and istable='1'";
					params.clear();
					params.add(row.getString("id"));
					DataTable dtChildren = dataQuery.getDataTableSql(childrenSystemSql, params.toArray());
					if (dtChildren.getRows() != null && dtChildren.getRows().size() > 0) {// 有数据查出
						treeNode.setChkDisabled(false);
					}
				}
				String isTable = row.getString("isTable");
				// 如果是制度  类型不匹配  移除
				if ("1".equals(isTable) && !StringUtil.isEmpty(sysType) && !sysType.equals(row.getString("systype"))) {
					continue;
				}
				treeNode.setIsTable(isTable);
				result.add(treeNode);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (dataQuery != null) {
				dataQuery.releaseConnl();
			}
		}
		return result;
	}

	public List<String> getAllChildDept(String proCode) {
		List<Department> findDepartment = DepartmentService.findDepartment(proCode, "");
		List<String> result = new ArrayList<String>();
		for (Department department : findDepartment) {
			result.add(department.getCode());
		}
		return result;
	}

	@Override
	public List<TreeNode> findModTree(String id) {
		List<TreeNode> result = new ArrayList<TreeNode>();
		StringBuffer sbf = new StringBuffer("select t.id as id,t.code as code,t.procode as pid,t.cname as name, (case (select count(*) from TB_TEMPLATE_INDEX p where p.procode = t.id) when 0 then 'false' else 'true' end) isParent,t.isMod as p1 from TB_TEMPLATE_INDEX t where t.state=1");
		List<Object> params = new ArrayList<Object>();
		if (StringUtil.isEmpty(id)) {
			sbf.append(" and t.procode is null ");
		} else {
			sbf.append(" and t.procode = ? ");
			params.add(id);
		}
		DataTable dataTable = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sbf.toString(), params.toArray());
		if (dataTable == null || dataTable.getRows().isEmpty()) { // 如果没有查询到数据返回
			return result;
		}
		List<DataTableRow> dataTableRows = dataTable.getRows();
		for (DataTableRow row : dataTableRows) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(row.getString("id"));
			treeNode.setPId(row.getString("pid"));
			treeNode.setP2(row.getString("name"));
			if ("1".equals(row.getString("p1"))) {
				treeNode.setName(row.getString("code") + "_" + row.getString("name"));
				treeNode.setIsTable("1");
			} else {
				treeNode.setName(row.getString("name"));
				treeNode.setIsTable("0");
			}
			treeNode.setIsParent(Boolean.parseBoolean(row.getString("isParent")));
			treeNode.setP1(row.getString("p1"));// 是否是模板
			result.add(treeNode);
		}
		return result;
	}

	/**
	 * 数据转对象
	 * 
	 * @param dataTable
	 * @return
	 */
	private List<SystemIndex> dataToSysIndex(DataTable dataTable) {
		List<SystemIndex> result = new ArrayList<SystemIndex>();
		if (dataTable == null || dataTable.getRows().size() == 0) { // 如果没有查询到数据返回
			return result;
		}
		List<DataTableRow> dataTableRows = dataTable.getRows();
		for (DataTableRow row : dataTableRows) {
			SystemIndex systemIndex = new SystemIndex();
			systemIndex.setCode(row.getString("CODE"));
			systemIndex.setCname(row.getString("CNAME"));
			systemIndex.setCcName(row.getString("CCNAME"));
			systemIndex.setProCode(row.getString("PROCODE"));
			systemIndex.setModCode(row.getString("MODCODE"));
			systemIndex.setFlowCode(row.getString("FLOWCODE"));
			systemIndex.setSort(row.getString("SORT"));
			systemIndex.setState(row.getString("STATE"));
			systemIndex.setMemo(row.getString("MEMO"));
			systemIndex.setSortCode(row.getString("SORTCODE"));
			systemIndex.setCreateTime(row.getDate("CREATETIME"));
			systemIndex.setDeptCode(row.getString("deptCode"));
			systemIndex.setCompoffice(row.getString("compoffice"));
			systemIndex.setTableNum(row.getString("tableNum"));
			systemIndex.setTableName(row.getString("tableName"));
			systemIndex.setDocNum(row.getString("docNum"));
			systemIndex.setCreateOffice(row.getString("createOffice"));
			systemIndex.setSysType(row.getString("sysType"));
			systemIndex.setIsTable(row.getString("isTable"));
			systemIndex.setTaskSort(row.getString("TaskSort"));
			systemIndex.setPlanId(row.getString("planid"));
			systemIndex.setFlowType(row.getString("flowtype"));
			systemIndex.setProvideOffice(row.getString("PROVIDEOFFICE"));
			result.add(systemIndex);
		}
		return result;
	}

	public ListHashMap<SystemIndex> loadSysIndexs() {
		ListHashMap<SystemIndex> indexs = new ListHashMap<SystemIndex>();
		SystemIndex sys = new SystemIndex();
		sys.setCode("");
		indexs.add(sys);
		String sql = "SELECT * FROM TB_SYSTEM_INDEX WHERE STATE = 1 ORDER BY CREATETIME asc";
		DataTable dt1 = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql);
		for (int i = 0; i < dt1.getRows().size(); i++) {
			DataTableRow dr1 = dt1.getRows().get(i);
			sys = new SystemIndex();
			sys.setCode(dr1.getString("code"));
			sys.setProCode(dr1.getString("procode"));
			sys.setCname(dr1.getString("CNAME"));
			sys.setCcName(dr1.getString("CCNAME"));
			sys.setModCode(dr1.getString("MODCODE"));
			sys.setFlowCode(dr1.getString("FLOWCODE"));
			sys.setSort(dr1.getString("SORT"));
			sys.setState(dr1.getString("STATE"));
			sys.setMemo(dr1.getString("MEMO"));
			sys.setSortCode(dr1.getString("SORTCODE"));
			sys.setCreateTime(dr1.getDate("CREATETIME"));
			sys.setDeptCode(dr1.getString("deptCode"));
			sys.setCompoffice(dr1.getString("compoffice"));
			sys.setTableNum(dr1.getString("tableNum"));
			sys.setTableName(dr1.getString("tableName"));
			sys.setDocNum(dr1.getString("docNum"));
			sys.setCreateOffice(dr1.getString("createOffice"));
			sys.setSysType(dr1.getString("sysType"));
			sys.setIsTable(dr1.getString("isTable"));
			sys.setTaskSort(dr1.getString("TaskSort"));
			sys.setPlanId(dr1.getString("planid"));
			sys.setFlowType(dr1.getString("flowtype"));
			sys.setProvideOffice(dr1.getString("PROVIDEOFFICE"));
			indexs.add(sys);
		}
		for (int i = 0; i < indexs.size(); i++) {
			sys = indexs.get(i);
			String code = sys.getCode();
			if (code.equals("")) {
				continue;
			}
			String pcode = sys.getProCode();
			SystemIndex pdep = indexs.get(pcode);
			pdep.addChild(code);
		}
		return indexs;
	}

	public List<SystemIndex> findSystemIndexs(String pcode, String value) {
		ListHashMap<SystemIndex> indexs = this.loadSysIndexs();
		List<SystemIndex> lists = new ArrayList<SystemIndex>();
		if (!indexs.containsKey(pcode)) {
			return lists;
		}
		boolean isfind = false;
		if (!value.equals("")) {
			isfind = true;
		}

		SystemIndex deproot = indexs.get(pcode);
		if (!isfind && deproot.getChilds().size() == 0) {
			lists.add(deproot);
			return lists;
		}
		for (int i = 0; i < deproot.getChilds().size(); i++) {
			SystemIndex sys = indexs.get(deproot.getChilds().get(i));
			if (isfind) {
				if (sys.getCode().toLowerCase().indexOf(value.toLowerCase()) >= 0 || sys.getCname().toLowerCase().indexOf(value.toLowerCase()) >= 0) {
					lists.add(sys);
				}
				lists.addAll(findSystemIndexs(sys.getCode(), value));
			} else {
				lists.add(sys);
			}
		}
		return lists;
	}

	/**
	 * 查询当前部门下能看的所有制度分类
	 * 
	 * @param code
	 * @param deptCode
	 * @return
	 */
	public List<String> getCurrentDepSys(String code, String deptCode) {
		// 查询出当前用户的部门及下级部门
		List<Department> dep1 = DepartmentService.findDepartment(deptCode, "");

		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		List<String> fl = new ArrayList<String>();
		// 所有能看的分类列表
		sql.append("select code from tb_system_index t where t.istable = '0' and t.deptcode in(? ");
		params.add(deptCode);
		for (Department department : dep1) {
			sql.append(",?");
			params.add(department.getCode());
		}
		sql.append(")");
		DataTable dt = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql.toString(), params.toArray());
		if (dt != null && dt.getRows().size() > 0) {
			List<DataTableRow> rows = dt.getRows();
			for (int i = 0; i < rows.size(); i++) {
				fl.add(rows.get(i).getString(0));
			}
		}

		sql.setLength(0);
		params.clear();

		// 查询出能看的第一层目录
		List<String> first = new ArrayList<String>();
		for (int i = 0; i < fl.size(); i++) {
			String pCode = fl.get(i);
			while (true) {
				SystemIndex systemIndex = getSystemIndex(pCode);
				if (null == systemIndex) {
					break;
				}
				first.add(systemIndex.getCode());
				pCode = systemIndex.getProCode();
				if (StringUtil.isEmpty(pCode)) {
					break;
				}
			}
		}
		return first;
	}
}
