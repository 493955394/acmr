package com.acmr.dao.oracle.sysindex;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import acmr.data.DataQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.data.AcmrDataPageDao;
import com.acmr.dao.sysindex.ISysIndexDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.PageBean;
import com.acmr.model.pub.RightDepartment;
import com.acmr.model.pub.TreeNode;
import com.acmr.model.sysindex.OneYearReportStageVoList;
import com.acmr.model.sysindex.SystemIndex;
import com.acmr.model.sysindex.SystemPlan;
import com.acmr.model.templatecenter.TemplateIndex;
import com.acmr.service.security.UserService;

public class OraSysIndexDaoImpl implements ISysIndexDao {

	@Override
	public List<TreeNode> getSysIndexTreeByDeptCode(String deptCode, String proCode) {
		// 定义查询sql
		StringBuffer sqlBuff = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sqlBuff.append("select t.code as id, t.procode as pid,t.deptcode as deptcode,t.cname as name,t.ModCode as templateId,t.isTable as isTable, (case (select count(*) from  TB_SYSTEM_INDEX p where p.procode = t.code and p.istable=0) when 0 then 'false' else 'true' end) isParent, ");
		sqlBuff.append(" (case (select count(t.deptcode) from TB_RIGHT_DEPARTMENT where t.deptcode in (select t2.code from TB_RIGHT_DEPARTMENT t2 start with t2.code = '" + deptCode + "' connect by prior t2.code = t2.procode))");
		sqlBuff.append(" when 0 then 'false' else 'true' end) chkDisabled ");

		sqlBuff.append(" from tb_system_index t where state != '-1' and t.istable='0'");
		if (StringUtil.isEmpty(proCode)) {
			sqlBuff.append(" and t.procode is null");
		} else {
			sqlBuff.append(" and t.procode=?");
			params.add(proCode);
		}
		sqlBuff.append(" and t.code in (select code from tb_system_index t3 where t3.istable='0'");
		sqlBuff.append(" start with t3.code in (select code from tb_system_index t4 where t4.istable='0' and t4.deptcode in");
		sqlBuff.append(" (select t5.code");
		sqlBuff.append(" from tb_right_department t5");
		sqlBuff.append(" start with t5.code = ?");
		sqlBuff.append(" connect by prior t5.code = t5.procode))");
		sqlBuff.append(" connect by prior t3.procode = t3.code) order by t.createtime");
		params.add(deptCode);
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

	public List<String> getAllChildDept(String proCode) {
		List<String> result = new ArrayList<String>();
		result.add(proCode);
		String sql = "select code from tb_right_department t start with code = ? connect by prior code = procode";
		List<Object> params = new ArrayList<Object>();
		params.add(proCode);
		DataQuery dataQuery = AcmrInputDPFactor.getDataPool().getDataQuery();
		DataTable dt = null;
		try {
			dt = dataQuery.getDataTableSql(sql, params.toArray());
			if (dt.getRows() != null && dt.getRows().size() > 0) {
				for (DataTableRow d : dt.getRows()) {
					result.add(d.getString("code"));
				}
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

	/**
	 * 查找制度
	 * 
	 * @param SystemIndex
	 *            制度对象
	 * @return 制度分页对象
	 */
	public PageBean<SystemIndex> findSystemIndex(String isTable, String systemStageType, PageBean<SystemIndex> page, SystemIndex systemIndex,String type) {
		StringBuffer sbf = new StringBuffer();
		List<Object> objs = new ArrayList<Object>();
		sbf.append("select * from TB_SYSTEM_INDEX t where 1=1 ");
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
			sbf.append(" and t.deptcode in (select t2.code from TB_RIGHT_DEPARTMENT t2 start with t2.code=? connect by prior t2.code = t2.procode) ");
			objs.add(deptCode);
		}

		// 搜索
		if (!StringUtil.isEmpty(systemIndex.getTableNum()) && !StringUtil.isEmpty(systemIndex.getProCode())) {
			sbf.setLength(0);
			sbf.append("select * from (select * from TB_SYSTEM_INDEX where 1=1  START WITH code=? CONNECT BY PRIOR  code = procode) t  where t.code like ?");
			objs.clear();
			objs.add(systemIndex.getProCode());
			objs.add("%" + systemIndex.getTableNum() + "%");
		} else if (!StringUtil.isEmpty(systemIndex.getTableName()) && !StringUtil.isEmpty(systemIndex.getProCode())) {
			sbf.setLength(0);
			sbf.append("select * from (select * from TB_SYSTEM_INDEX where 1=1  START WITH code=? CONNECT BY PRIOR  code = procode) t  where t.cname like ?");
			objs.clear();
			objs.add(systemIndex.getProCode());
			objs.add("%" + systemIndex.getTableName() + "%");
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

	@Override
	public int insert(SystemIndex systemIndex) {
		String sql = "insert into TB_SYSTEM_INDEX(CODE, CNAME, CCNAME, PROCODE, MODCODE, FLOWCODE, " + "SORT, CREATETIME, STATE, MEMO,SORTCODE,DEPTCODE," + "TABLENUM,TABLENAME,DOCNUM,CREATEOFFICE,COMPOFFICE,SYSTYPE,ISTABLE,TaskSort,planId,flowType,PROVIDEOFFICE) " + "values(?, ?, ?, ?, ?,?, ?,sysdate, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
		String sql = "update TB_SYSTEM_INDEX t set t.CNAME=?,t.CCNAME=?, t.PROCODE=?, t.MODCODE=?," + "t.FLOWCODE=?, t.SORT=?,t.STATE=?,t.MEMO=?,t.SORTCODE=?,t.DEPTCODE=?," + "t.TABLENAME=?,t.DOCNUM=?,t.CREATEOFFICE=?,t.COMPOFFICE=?,t.SYSTYPE=?,t.ISTABLE=?,t.taskSort=? ,planId=?,flowType=?,PROVIDEOFFICE=?" + " where t.CODE=?";
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
		String sql = "delete from TB_SYSTEM_INDEX t where t.code=?";
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
	public List<SystemIndex> findAllSystemIndex() {
		String sql = "select * from TB_SYSTEM_INDEX t where t.state = 1 and t.istable = 1";
		DataTable dataTable = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[0]);
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
		String sql = "update tb_system_index t set t.state = (case when t.state=1 then 0 else 1 end) where t.code = ?";
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
		String count = dataTables.getRows().get(0).getString("count(code)");
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
		String sql = "";
		if (!StringUtil.isEmpty(deptCode)) {// && !isSuper){
			// 不是管理员，需要加权限
			sql = "select t.code as id, t.procode as pid, t.cname as name, " + "(case (select count(*) from TB_SYSTEM_INDEX p where p.procode = t.code and p.istable = 0) " + "when 0 then 'false' else 'true' end) isParent," + " (case (select count(t.deptcode) from TB_RIGHT_DEPARTMENT " + "where t.deptcode in (select t2.code from TB_RIGHT_DEPARTMENT t2 start with t2.code = ? " + "connect by prior t2.code = t2.procode)) when 0 then 'false' else 'true' end) chkDisabled," + " (case (select count(*) from tb_system_index t2 where t2.code = t.code and " + "t2.code <> ? start with t2.code = ? connect by prior t2.procode = t2.code) when 0 then 'false' else 'true' end) " + "open from TB_SYSTEM_INDEX t" + " where istable='0'  and state = 1 "
					+ " and (t.procode in(select code from tb_system_index t2 start with t2.code = ?" + " connect by prior t2.procode = t2.code) and procode <> ? or procode is null)" + "and (t.code in (select code from tb_system_index t2 WHERE procode <> ? or procode is null " + "start with t2.code in(select code from tb_system_index t where t.istable='0' and t.deptcode in (select code from tb_right_department t start with code = ? connect by prior code = procode)) connect by prior t2.procode = t2.code)) order by t.createTime"; // and"
			params.add(deptCode);
			params.add(code);
			params.add(code);
			params.add(code);
			params.add(code);
			params.add(code);
			params.add(deptCode);
		}
		DataQuery dataQuery = AcmrInputDPFactor.getDataPool().getDataQuery();
		DataTable dt = null;
		try {
			dt = dataQuery.getDataTableSql(sql, params.toArray());
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
			if ("false".equals(row.getString("open"))) {
				treeNode.setOpen("false");
			} else {
				treeNode.setOpen("true");
			}

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
		String sql = "select " +field+ " from TB_TEMPLATE_Content where templateCode = ?";
		byte[] b = null;
		try {
			ResultSet resultSet = dataQuery.getResultSetSql(sql, new Object[]{code});
			if(resultSet.next()){
				 b = resultSet.getString(field).getBytes("utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dataQuery.releaseConnl();
		}
		return b;
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
	public List<TreeNode> getSysIndexTree(String deptCode, String proCode,String sysType) {
		// 定义查询sql
		StringBuffer sqlBuff = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sqlBuff.append("select t.code as id, t.procode as pid,t.deptcode as deptcode,t.cname as name,t.ModCode as templateId,t.isTable as isTable, t.systype as systype, (case (select count(*) from  TB_SYSTEM_INDEX p where p.procode = t.code) when 0 then 'false' else 'true' end) isParent, ");
		sqlBuff.append(" (case (select count(t.deptcode) from TB_RIGHT_DEPARTMENT where t.deptcode in (select t2.code from TB_RIGHT_DEPARTMENT t2 start with t2.code = '" + deptCode + "' connect by prior t2.code = t2.procode))");
		sqlBuff.append(" when 0 then 'false' else 'true' end) chkDisabled ");

		sqlBuff.append(" from tb_system_index t where state != '-1' ");
		if (StringUtil.isEmpty(proCode)) {
			sqlBuff.append(" and t.procode is null");
		} else {
			sqlBuff.append(" and t.procode=?");
			params.add(proCode);
		}
		sqlBuff.append(" and (t.planid is null or t.planid not in (select id from TB_TIMEPLAN_INDEX where plantype='2')) ");
		sqlBuff.append(" and t.code in (select code from tb_system_index t3 ");
		sqlBuff.append(" start with t3.code in (select code from tb_system_index t4 where t4.deptcode in");
		sqlBuff.append(" (select t5.code");
		sqlBuff.append(" from tb_right_department t5");
		sqlBuff.append(" start with t5.code = ?");
		sqlBuff.append(" connect by prior t5.code = t5.procode))");
		sqlBuff.append(" connect by prior t3.procode = t3.code) order by t.createtime");
		params.add(deptCode);

		DataTable dt = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sqlBuff.toString(), params.toArray());
		List<TreeNode> result = new ArrayList<TreeNode>();
		if (dt.getRows() == null && dt.getRows().size() == 0) {
			return null;
		}
		for (DataTableRow row : dt.getRows()) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(row.getString("id"));
			treeNode.setPId(row.getString("pid"));
			treeNode.setName(row.getString("name"));
			treeNode.setTemplateId(row.getString("templateId"));
			/*if ("false".equals(row.getString("isParent"))) {
				treeNode.setIsParent(false);
			} else {
				treeNode.setIsParent(true);
			}*/
//			if ("false".equals(row.getString("isParent"))) {
			if ("0".equals(row.getString("isTable"))) {
				treeNode.setIsParent(true);
			} else {
				treeNode.setIsParent(false);
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
				DataTable dtChildren = AcmrInputDPFactor.getQuickQuery().getDataTableSql(childrenSystemSql, params.toArray());
				if (dtChildren.getRows() != null && dtChildren.getRows().size() > 0) {// 有数据查出
					treeNode.setChkDisabled(false);
				}
			}
			treeNode.setIsTable(row.getString("isTable"));
			String isTable = row.getString("isTable");
			// 如果是制度  类型不匹配  移除
			if ("1".equals(isTable) && !StringUtil.isEmpty(sysType) && !sysType.equals(row.getString("systype"))) {
				continue;
			}
			treeNode.setIsTable(isTable);
			result.add(treeNode);
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

}
