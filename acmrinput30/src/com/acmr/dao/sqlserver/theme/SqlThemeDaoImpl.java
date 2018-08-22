package com.acmr.dao.sqlserver.theme;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import acmr.data.DataQuery;
import acmr.data.DataQuickQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.theme.IThemeDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.security.Department;
import com.acmr.model.security.User;
import com.acmr.model.theme.Lanmu;
import com.acmr.model.theme.MenuRight;
import com.acmr.model.theme.RightModel;
import com.acmr.model.theme.ThemePo;
import com.acmr.model.theme.ThemeSon;
import com.acmr.service.security.DepartmentService;
import com.acmr.service.security.UserService;

public class SqlThemeDaoImpl implements IThemeDao {

	@Override
	public List<ThemePo> getThemeByProcode(String procode) {
		StringBuffer sql = new StringBuffer();
		ArrayList<Object> parms = new ArrayList<Object>();
		sql.append("select * from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU t where 1=1 ");
		if (StringUtil.isEmpty(procode)) {
			sql.append(" and procode is null or procode ='' ");
		} else {
			sql.append(" and procode = ?");
			parms.add(procode);
		}
		sql.append(" and ifclose = 0 order by sortcode ");

		ResultSet rs1 = null;
		DataQuery dataQuery = null;
		List<ThemePo> result = new ArrayList<ThemePo>();
		try {
			dataQuery = AcmrInputDPFactor.getDataQuery();
			rs1 = dataQuery.getResultSetSql(sql.toString(), parms.toArray());
			while (rs1.next()) {
				ThemePo theme = new ThemePo();
				theme.setCode(rs1.getString("code"));
				theme.setCname(rs1.getString("cname"));
				theme.setProcode(rs1.getString("procode"));
				theme.setHref(rs1.getString("href"));
				theme.setTarget(rs1.getString("target"));
				theme.setExp(rs1.getString("exp"));
				theme.setDef(rs1.getString("def"));
				theme.setSort(rs1.getString("sort"));
				theme.setPhoto(rs1.getString("photo"));
				theme.setVisible(rs1.getString("visible"));
				result.add(theme);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if (dataQuery != null) {
				dataQuery.releaseConnl();
			}
		}
		return result;
	}

	@Override
	public int checkCode(String code) {
		String sql = "select count(*) from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU t where code = ?";
		DataTable dt = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[] { code });
		int count = dt.getRows().get(0).getint(0);
		return count;
	}

	@Override
	public int save(ThemePo themePo) {
		String sql = "insert into ACMRNDATA_PUB_V2.TB_WEB_TOPMENU(code,cname,href,sortcode,ifclose, procode,photo,exp,def,sort,visible) values(?,?,?,?,?,?,?,?,?,?,?)";
		ArrayList<Object> parms = new ArrayList<Object>();
		parms.add(themePo.getCode());
		parms.add(themePo.getCname());
		parms.add(themePo.getHref());
		parms.add(themePo.getCode());
		parms.add(0);
		parms.add(themePo.getProcode());
		parms.add(themePo.getPhoto());
		parms.add(themePo.getExp());
		parms.add(themePo.getDef());
		parms.add(themePo.getSort());
		parms.add(themePo.getVisible());
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql, parms.toArray());
	}

	@Override
	public int deleteByCode(String code) {
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU where code in(");
		sql.append("select code from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU t where code = ? union all    ");
		sql.append("select code from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU t where procode = ? union all  ");
		sql.append("select code from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU t where procode in (select code from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU t where procode =?)");
		sql.append(")");
		deleteRight(code);
		String sb = "delete from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU_RIGHT where menucode in (select code from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU t where procode = ?)";
		AcmrInputDPFactor.getQuickQuery().executeSql(sb, new Object[] { code });
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql.toString(), new Object[] { code, code, code });
	}

	@Override
	public ThemePo getByCode(String code) {
		String sql = "select * from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU where code = ?";
		ResultSet rs1 = null;
		DataQuery dataQuery = null;
		List<ThemePo> result = new ArrayList<ThemePo>();
		try {
			dataQuery = AcmrInputDPFactor.getDataQuery();
			rs1 = dataQuery.getResultSetSql(sql, new Object[] { code });
			while (rs1.next()) {
				ThemePo theme = new ThemePo();
				theme.setCode(rs1.getString("code"));
				theme.setCname(rs1.getString("cname"));
				theme.setProcode(rs1.getString("procode"));
				theme.setHref(rs1.getString("href"));
				theme.setTarget(rs1.getString("target"));
				theme.setExp(rs1.getString("exp"));
				theme.setDef(rs1.getString("def"));
				theme.setSort(rs1.getString("sort"));
				theme.setPhoto(rs1.getString("photo"));
				theme.setVisible(rs1.getString("visible"));
				result.add(theme);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if (dataQuery != null) {
				dataQuery.releaseConnl();
			}
		}
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public int updatefl(ThemePo themePo) {
		String sql = "update ACMRNDATA_PUB_V2.TB_WEB_TOPMENU set cname=? where code=?";
		ArrayList<Object> parms = new ArrayList<Object>();
		parms.add(themePo.getCname());
		parms.add(themePo.getCode());
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql, parms.toArray());
	}

	@Override
	public List<Lanmu> getLanmu() {
		String sql = "select distinct dbcode,t.code,t2.cname from ACMRNDATA_PUB_V2.TB_WEB_LANMUDATA_DB t left join ACMR_PUB_V2.TB_DATA_DATABASE t2 on  t2.code = t.dbcode where sort='jd'";
		DataTable dataTable = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql);
		List<Lanmu> result = new ArrayList<Lanmu>();
		if (dataTable == null || dataTable.getRows().size() == 0) { // 如果没有查询到数据返回
			return result;
		}
		List<DataTableRow> dataTableRows = dataTable.getRows();
		for (DataTableRow row : dataTableRows) {
			Lanmu lanmu = new Lanmu();
			lanmu.setDbcode(row.getString("dbcode"));
			lanmu.setCode(row.getString("code"));
			lanmu.setName(row.getString("cname"));
			result.add(lanmu);
		}
		return result;
	}

	@Override
	public int saveMenu(List<ThemeSon> sons, String code) {
		// return值暂时无用
		if (sons == null || sons.size() == 0 || StringUtil.isEmpty(code)) {
			return 0;
		}
		DataQuery dataQuery = null;
		try {
			dataQuery = AcmrInputDPFactor.getDataQuery();
			dataQuery.beginTranse();
			// 删除旧的
			String delold = "delete from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU where procode = ? and sort=3";
			dataQuery.executeSql(delold, new Object[] { code });
			// 添加新的
			String sql = "insert into ACMRNDATA_PUB_V2.TB_WEB_TOPMENU(code,cname,href,sortcode,ifclose, procode,photo,exp,def,sort) values(?,?,?,?,?,?,?,?,?,?)";
			for (int i = 0; i < sons.size(); i++) {
				ThemeSon themeSon = sons.get(i);
				if (code.equals(themeSon.getId())) {
					// 说明和父级code相同了 死循环
					continue;
				}
				ArrayList<Object> parms = new ArrayList<Object>();
				parms.add(themeSon.getId());
				parms.add(themeSon.getName());
				parms.add("/easyquery.htm?cn=" + themeSon.getId());
				parms.add(themeSon.getId());
				parms.add(0);
				parms.add(code);
				parms.add("");
				parms.add("");
				parms.add(themeSon.getDef());
				parms.add("3");
				dataQuery.executeSql(sql, parms.toArray());
			}
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
		return 0;
	}

	@Override
	public int update(ThemePo themePo) {
		StringBuffer sql = new StringBuffer();
		ArrayList<Object> parms = new ArrayList<Object>();
		sql.append("update ACMRNDATA_PUB_V2.TB_WEB_TOPMENU set ");
		sql.append("cname=?,procode=?,exp=?,visible=?");
		parms.add(themePo.getCname());
		parms.add(themePo.getProcode());
		parms.add(themePo.getExp());
		parms.add(themePo.getVisible());
		if (!StringUtil.isEmpty(themePo.getPhoto())) {
			sql.append(",photo=?");
			parms.add(themePo.getPhoto());
		}
		sql.append(" where code=?");
		parms.add(themePo.getCode());
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql.toString(), parms.toArray());
	}

	@Override
	public int insertRight(MenuRight menuright) {
		String sql = "insert into ACMRNDATA_PUB_V2.TB_WEB_TOPMENU_RIGHT(id,menucode,departments,users,roles) values(ACMRNDATA_PUB_V2.SEQ_TOPMENU_RIGHT.nextval,?,?,?,?)";
		ArrayList<Object> parms = new ArrayList<Object>();
		parms.add(menuright.getMenucode());
		parms.add(menuright.getDept());
		parms.add(menuright.getUser());
		parms.add(menuright.getRols());
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql, parms.toArray());
	}

	@Override
	public int deleteRight(String menucode) {
		String sql = "delete from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU_RIGHT where menucode = ? ";
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql, new Object[] { menucode });
	}

	@Override
	public List<MenuRight> findMenuRight(String menucode) {
		String sql = "select * from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU_RIGHT where menucode = ? order by id";
		List<Object> parm = new ArrayList<Object>();
		parm.add(menucode);
		DataQuickQuery quickQuery = AcmrInputDPFactor.getQuickQuery();
		DataTable dataTable = quickQuery.getDataTableSql(sql, parm.toArray());
		return dataToMenuRight(dataTable);
	}

	private List<MenuRight> dataToMenuRight(DataTable dataTable) {
		ArrayList<MenuRight> list = new ArrayList<MenuRight>();
		if (dataTable == null || dataTable.getRows().size() == 0) { // 如果没有查询到数据返回
			return list;
		}
		List<DataTableRow> dataTableRows = dataTable.getRows();
		for (DataTableRow row : dataTableRows) {
			ArrayList<RightModel> childs = new ArrayList<RightModel>();
			MenuRight right = new MenuRight();
			right.setId(row.getString("ID"));
			right.setMenucode(row.getString("menucode"));
			String depts = row.getString("departments");
			String users = row.getString("users");
			// 部门
			if (!StringUtil.isEmpty(depts)) {
				String[] split = depts.split(",");
				for (String string : split) {
					if (StringUtil.isEmpty(string)) {
						continue;
					}
					Department department = DepartmentService.getDepartment(string);
					childs.add(new RightModel(string, department.getCname(), "0"));
				}
			}
			// 用户
			if (!StringUtil.isEmpty(users)) {
				String[] split = users.split(",");
				for (String string : split) {
					if (StringUtil.isEmpty(string)) {
						continue;
					}
					User user = UserService.getUserInfo(string);
					childs.add(new RightModel(string, user.getUsername(), "1"));
				}
			}

			right.setDept(depts);
			right.setUser(users);
			right.setRols(row.getString("roles"));
			right.setChilds(childs);
			list.add(right);
		}
		return list;
	}

	@Override
	public List<String> getMenuCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllTopmenuRight(User currentUser) {
		List<String> list = new ArrayList<String>();

		String sql = "select * from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU_RIGHT";
		DataQuickQuery quickQuery = AcmrInputDPFactor.getQuickQuery();
		DataTable dataTable = quickQuery.getDataTableSql(sql, new Object[] {});

		// 当前用户所有上级部门
		String depcode = currentUser.getDepcode();
		ArrayList<String> deps = new ArrayList<String>();
		Department dep = DepartmentService.getDepartment(depcode);
		int count = 0; // 200层以内循环
		while (!StringUtil.isEmpty(depcode) && dep != null && count < 200) {
			count++;
			deps.add(dep.getCode());
			String parent = dep.getParent();
			if (StringUtil.isEmpty(parent)) {
				break;
			}
			dep = DepartmentService.getDepartment(parent);
		}
		// 当前用户所有角色
		List<String> roles = currentUser.getRoles();
		List<MenuRight> menuRights = dataToMenuRight(dataTable);
		for (int i = 0; i < menuRights.size(); i++) {
			MenuRight menuRight = menuRights.get(i);
			String depts = menuRight.getDept();
			String rols = menuRight.getRols();
			String users = menuRight.getUser();

			// 处理用户
			if (null != users && users.contains(currentUser.getUserid() + ",")) {
				if (!list.contains(menuRight.getMenucode())) {
					list.add(menuRight.getMenucode());
				}
				continue;
			}

			// 处理部门和角色并集
			if (!StringUtil.isEmpty(rols) && !StringUtil.isEmpty(depts)) {
				for (int j = 0; j < deps.size(); j++) {
					String dept = deps.get(j);
					if (depts.contains(dept) && roles.contains(rols)) {
						if (!list.contains(menuRight.getMenucode())) {
							list.add(menuRight.getMenucode());
						}
						break;
					}
				}
			}

			// 角色为空 看部门
			if (!StringUtil.isEmpty(depts) && StringUtil.isEmpty(rols)) {
				for (int j = 0; j < deps.size(); j++) {
					String dept = deps.get(j);
					if (depts.contains(dept)) {
						if (!list.contains(menuRight.getMenucode())) {
							list.add(menuRight.getMenucode());
						}
						break;
					}
				}
			}

			// 部门为空 看角色
			if (!StringUtil.isEmpty(rols) && StringUtil.isEmpty(depts)) {
				if (roles.contains(rols)) {
					if (!list.contains(menuRight.getMenucode())) {
						list.add(menuRight.getMenucode());
					}
					continue;
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i) + ",");
		}
		return sb.toString();
	}

	@Override
	public int move(String currentCode, String nextCode) {
		ArrayList<Object> parms = new ArrayList<Object>();
		ThemePo current = getByCode(currentCode);
		ThemePo next = getByCode(nextCode);
		StringBuffer sql = new StringBuffer();
		sql.append("update ACMRNDATA_PUB_V2.TB_WEB_TOPMENU set sortcode = ? where code = ?");
		DataQuery dataQuery = AcmrInputDPFactor.getDataQuery();
		int count = 0;
		try {
			dataQuery.beginTranse();
			parms.add(current.getSortcode());
			parms.add(next.getCode());
			count += dataQuery.executeSql(sql.toString(), parms.toArray());
			parms.clear();
			parms.add(next.getSortcode());
			parms.add(current.getCode());
			count += dataQuery.executeSql(sql.toString(), parms.toArray());
			dataQuery.commit();
		} catch (SQLException e) {
			dataQuery.rollback();
			e.printStackTrace();
		} finally {
			dataQuery.releaseConnl();
		}
		return count;
	}

	@Override
	public String cansee(String code, User currentUser) {
		String sql = "select * from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU_RIGHT where menucode in (select procode from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU where code = ?)";
		DataQuickQuery quickQuery = AcmrInputDPFactor.getQuickQuery();
		DataTable dataTable = quickQuery.getDataTableSql(sql, new Object[] { code });

		List<String> list = new ArrayList<String>();

		// 当前用户所有上级部门
		String depcode = currentUser.getDepcode();
		ArrayList<String> deps = new ArrayList<String>();
		Department dep = DepartmentService.getDepartment(depcode);
		int count = 0; // 200层以内循环
		while (!StringUtil.isEmpty(depcode) && dep != null && count < 200) {
			count++;
			deps.add(dep.getCode());
			String parent = dep.getParent();
			if (StringUtil.isEmpty(parent)) {
				break;
			}
			dep = DepartmentService.getDepartment(parent);
		}
		// 当前用户所有角色
		List<String> roles = currentUser.getRoles();
		List<MenuRight> menuRights = dataToMenuRight(dataTable);
		for (int i = 0; i < menuRights.size(); i++) {
			MenuRight menuRight = menuRights.get(i);
			String depts = menuRight.getDept();
			String rols = menuRight.getRols();
			String users = menuRight.getUser();

			// 处理用户
			if (null != users && users.contains(currentUser.getUserid() + ",")) {
				if (!list.contains(menuRight.getMenucode())) {
					list.add(menuRight.getMenucode());
				}
				continue;
			}

			// 处理部门和角色并集
			if (!StringUtil.isEmpty(rols) && !StringUtil.isEmpty(depts)) {
				for (int j = 0; j < deps.size(); j++) {
					String dept = deps.get(j);
					if (depts.contains(dept) && roles.contains(rols)) {
						if (!list.contains(menuRight.getMenucode())) {
							list.add(menuRight.getMenucode());
						}
						break;
					}
				}
			}

			// 角色为空 看部门
			if (!StringUtil.isEmpty(depts) && StringUtil.isEmpty(rols)) {
				for (int j = 0; j < deps.size(); j++) {
					String dept = deps.get(j);
					if (depts.contains(dept)) {
						if (!list.contains(menuRight.getMenucode())) {
							list.add(menuRight.getMenucode());
						}
						break;
					}
				}
			}

			// 部门为空 看角色
			if (!StringUtil.isEmpty(rols) && StringUtil.isEmpty(depts)) {
				if (roles.contains(rols)) {
					if (!list.contains(menuRight.getMenucode())) {
						list.add(menuRight.getMenucode());
					}
					continue;
				}
			}
		}

		StringBuffer sb = new StringBuffer();
		sb.append("select t.*                                                          ");
		sb.append("  from ACMRNDATA_PUB_V2.TB_WEB_TOPMENU t,                           ");
		sb.append("       ACMRNDATA_PUB_V2.TB_WEB_TOPMENU t2                           ");
		sb.append(" where t2.code = ?                                                  ");
		sb.append("   and (t.visible is null or t.visible = '' or t.visible = 0)       ");
		sb.append("   and t.code = t2.procode                                          ");

		DataTable dt = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sb.toString(), new Object[] { code });

		if (dt.getRows().size() > 0) {
			list.add(dt.getRows().get(0).getString("code"));
		}

		System.out.println(list);
		return list.size() + "";
	}

	@Override
	public List<Lanmu> searchLanmu(String key) {
		StringBuffer sb = new StringBuffer();
		sb.append("select *  from (                                        ");
		sb.append("        select distinct dbcode, t.code, t2.cname        ");
		sb.append("          from ACMRNDATA_PUB_V2.TB_WEB_LANMUDATA_DB t   ");
		sb.append("          left join ACMR_PUB_V2.TB_DATA_DATABASE t2     ");
		sb.append("            on t2.code = t.dbcode  where sort = 'jd' )  ");
		sb.append(" where dbcode like ?   or code like ?   or cname like ? ");

		DataTable dataTable = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sb.toString(), new Object[] { "%" + key + "%", "%" + key + "%", "%" + key + "%" });
		List<Lanmu> result = new ArrayList<Lanmu>();
		if (dataTable == null || dataTable.getRows().size() == 0) { // 如果没有查询到数据返回
			return result;
		}
		List<DataTableRow> dataTableRows = dataTable.getRows();
		for (DataTableRow row : dataTableRows) {
			Lanmu lanmu = new Lanmu();
			lanmu.setDbcode(row.getString("dbcode"));
			lanmu.setCode(row.getString("code"));
			lanmu.setName(row.getString("cname") + "/" + row.getString("dbcode") + "/" + row.getString("code"));
			result.add(lanmu);
		}
		return result;
	}
}
