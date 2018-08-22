package com.acmr.service.search;

import java.util.ArrayList;
import java.util.List;

import acmr.cubeinput.MetaTableException;
import acmr.cubeinput.service.CubeMetaTbSev;
import acmr.cubeinput.service.metatable.entity.SqlWhere;
import acmr.util.DataTable;
import acmr.util.DataTableRow;

import com.acmr.helper.constants.Const;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.TreeNode;
import com.acmr.model.search.sItem;
import com.acmr.service.metadata.MetaService;
import com.acmr.service.metadata.MetaServiceManager;

public class SearchService {

	private static String quanguo = "GX000000";
	
	public static class factor {
		private static SearchService ds = new SearchService();

		public static SearchService getInstance() {
			return ds;
		}
	}

	/**
	 * 获取树形结构第一层 code , wdcode 维度吗 zb,fl,unit,reg
	 * 
	 * @throws MetaTableException
	 */
	public List<TreeNode> getTree(String code, String wdcode) throws MetaTableException {
		List<TreeNode> list = new ArrayList<TreeNode>();
		if (StringUtil.isEmpty(wdcode)) {
			return list;
		}
		List<SqlWhere> where1 = new ArrayList<SqlWhere>();
		if (StringUtil.isEmpty(code)) {
			where1.add(new SqlWhere("procode", "NULL", 00));
		} else {
			where1.add(new SqlWhere("procode", code, 10));
		}
		MetaServiceManager metaService = MetaService.getMetaService(wdcode);
		DataTable queryData = metaService.getTree(null, where1, "sortcode");
		List<DataTableRow> rows = queryData.getRows();
		boolean flag = false;
		for (int i = 0; i < rows.size(); i++) {
			DataTableRow row = rows.get(i);
			TreeNode treeNode = new TreeNode();
			treeNode.setId(row.getString("code"));
			String ifdata = row.getString("ifdata");
			Boolean ifd = true;
			if (Const.IFDATA.equals(ifdata)) {
				where1.clear();
				where1.add(new SqlWhere("procode", row.getString("code"), 10));
				int queryCount = metaService.QueryCount(where1);
				if (queryCount > 0) {
					treeNode.setIconSkin("icon01");
				} else {
					ifd = false;
				}
			}
			if (StringUtil.isEmpty(code)) {
				treeNode.setIsParent(ifd);
			}else{
				treeNode.setIsParent(false);
			}
			treeNode.setName(row.getString("cname"));
			treeNode.setPid(row.getString("procode"));
			// 如果是地区 并且有全国
			if ("reg".equals(wdcode) && quanguo.equals(row.getString("code"))) {
				flag = true;
			}
			list.add(treeNode);
		}
		// 如果是地区并且有全国
		if (flag) {
			getRegTree(quanguo, list);
		}
		return list;
	}

	public void getRegTree(String code, List<TreeNode> list) throws MetaTableException {
		List<SqlWhere> where1 = new ArrayList<SqlWhere>();
		if (StringUtil.isEmpty(code)) {
			where1.add(new SqlWhere("procode", "NULL", 00));
		} else {
			where1.add(new SqlWhere("procode", code, 10));
		}
		MetaServiceManager metaService = MetaService.getMetaService("reg");
		DataTable queryData = metaService.getTree(null, where1, "sortcode");
		List<DataTableRow> rows = queryData.getRows();
		for (int i = 0; i < rows.size(); i++) {
			DataTableRow row = rows.get(i);
			TreeNode treeNode = new TreeNode();
			treeNode.setId(row.getString("code"));
			String ifdata = row.getString("ifdata");
			Boolean ifd = true;
			if (Const.IFDATA.equals(ifdata)) {
				where1.clear();
				where1.add(new SqlWhere("procode", row.getString("code"), 10));
				int queryCount = metaService.QueryCount(where1);
				if (queryCount > 0) {
					treeNode.setIconSkin("icon01");
				} else {
					ifd = false;
				}
			}
			treeNode.setIsParent(ifd);
			treeNode.setName(row.getString("cname"));
			treeNode.setPid(row.getString("procode"));
			list.add(treeNode);
		}
	}
	
	public List<sItem> find(String text, String wdtype, String dbcode) throws MetaTableException {
		CubeMetaTbSev cMetaManage = new CubeMetaTbSev(wdtype);
		DataTable dt1 = null;
		Object[] param = {};
		String tbname = getTbname(wdtype, dbcode);
		if (tbname == null) {
			return null;
		}
		param = new Object[1];
		param[0] = "%" + text + "%";
		dt1 = cMetaManage.QueryDataSql("select code,cname from " + tbname + " where lower(cname) like ?", param);
		ArrayList<sItem> list = new ArrayList<sItem>();
		if (dt1.getRows().size() > 0) {
			for (int i = 0; i < dt1.getRows().size(); i++) {
				sItem sitem = new sItem();
				sitem.setName(dt1.getRows().get(i).getString("cname").toString());
				sitem.setValue(dt1.getRows().get(i).getString("code").toString());
				sitem.setDatavalue(dt1.getRows().get(i).getString("code").toString());
				sitem.setPath(findPathName(dt1.getRows().get(i).getString("code").toString(), wdtype, dbcode));
				list.add(sitem);
			}
		}
		return list;
	}
	
	private String getTbname(String wdtype, String dbcode) throws MetaTableException {
		if ("DW".equals(wdtype.toUpperCase()) || "UNIT".equals(wdtype.toUpperCase()) || "U".equals(wdtype.toUpperCase())) {
			return "tb_basic_unit";
		}
		switch (wdtype.toUpperCase()) {
		case "I":
		case "ZB":
			wdtype = "zb";
			break;
		case "S":
		case "FL":
			wdtype = "fl";
			break;
		case "T":
		case "SJ":
		 
			wdtype = "sj";
			break;
		case "TT":
			wdtype = "tt";
			break;
		case "R":
		case "DQ":
		case "REG":
			wdtype = "reg";
			break;
		case "LY":
		case "DS":
			wdtype = "ds";
			break;
		}
		DataTable dt1 = null;
		CubeMetaTbSev cMetaManage = new CubeMetaTbSev(wdtype);
		Object[] param = { dbcode, wdtype.toLowerCase() };
		dt1 = cMetaManage.QueryDataSql("select ftable from tb_data_weidu_input  where dbcode=? and code=?", param);
		if (dt1.getRows().size() == 0) {
			return null;
		}
		return dt1.getRows().get(0).toString();
	}
	
	public String findPath(String code, String wdtype, String dbcode) throws MetaTableException {
		CubeMetaTbSev cMetaManage = new CubeMetaTbSev(wdtype);
		DataTable dt1 = null;
		Object[] param = {};
		String tbname = getTbname(wdtype, dbcode);
		if (tbname == null) {
			return null;
		}
		param = new Object[1];
		param[0] = code;
		dt1 = cMetaManage.QueryDataSql("select  code,LEVEL  from  " + tbname + " start with code =?  connect by prior PROCODE = code  order by level desc", param);
		String result = "";
		if (dt1.getRows().size() > 0) {
			for (int i = 0; i < dt1.getRows().size(); i++) {
				result += " > " + dt1.getRows().get(i).getString("code").toString();
			}
		}
		return result.substring(3, result.length());
	}
	
	public String findPathName(String code, String wdtype, String dbcode) throws MetaTableException {
		CubeMetaTbSev cMetaManage = new CubeMetaTbSev(wdtype);
		DataTable dt1 = null;
		Object[] param = {};
		String tbname = getTbname(wdtype, dbcode);
		if (tbname == null) {
			return null;
		}
		param = new Object[1];
		param[0] = code;
		dt1 = cMetaManage.QueryDataSql("select  code,cname,LEVEL  from  " + tbname + " start with code =?  connect by prior PROCODE = code  order by level desc", param);
		String result = "";
		if (dt1.getRows().size() > 0) {
			for (int i = 0; i < dt1.getRows().size(); i++) {
				result += " > " + dt1.getRows().get(i).getString("cname").toString();
			}
		}
		return result.substring(3, result.length());
	}

}
