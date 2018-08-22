package com.acmr.service.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import acmr.cubeinput.MetaTableException;
import acmr.cubeinput.service.CubeInputSev;
import acmr.cubeinput.service.CubeMetaTbSev;
import acmr.cubeinput.service.CubeSearchSev;
import acmr.cubeinput.service.CubeUnitManager;
import acmr.cubeinput.service.CubeUnitManager.CubeUnitManagerFactor;
import acmr.cubeinput.service.cubeinput.entity.CubeUnit;
import acmr.cubeinput.service.metatable.entity.CubeMetaTable;
import acmr.cubeinput.service.metatable.entity.SqlWhere;
import acmr.util.DataTable;
import acmr.util.DataTableColumns;
import acmr.util.DataTableRow;

import com.acmr.helper.constants.Const;
import com.acmr.helper.util.StringUtil;
import com.acmr.web.jsp.metadata.Zbmgr;

/**
 * 元数据cube封装层
 * 
 * @author chenyf
 *
 */
public class MetaServiceManager {
	/**
	 * meta表代码
	 * 
	 * @author chenyf
	 */
	private String tbcode;

	private String precode;

	private int codelen;

	private CubeMetaTbSev metaDao;

	private CubeInputSev cube = CubeInputSev.CubeInputSevFactor.getInstance(Const.DB_CODE);

	private CubeUnitManager unitCube = CubeUnitManagerFactor.getInstance("");

	public MetaServiceManager(String tbcode) {
		this.tbcode = tbcode;
		this.metaDao = MetaManager.getMetaDao(tbcode);
		this.precode = metaDao.getMetaTable().getPrecode();
		this.codelen = metaDao.getMetaTable().getCodelen();
	}

	public String getTbcode() {
		return tbcode;
	}

	public void setTbcode(String tbcode) {
		this.tbcode = tbcode;
	}

	public String getPrecode() {
		return precode;
	}

	public void setPrecode(String precode) {
		this.precode = precode;
	}

	public int getCodelen() {
		return codelen;
	}

	public void setCodelen(int codelen) {
		this.codelen = codelen;
	}

	/**
	 * 查询数据，最多返回1000条记录
	 * 
	 * @param codes
	 *            需要返回的字段列表
	 * @param where1
	 *            查询的条件列表
	 * @see CSqlWhere
	 * @param sortcode
	 *            排序字段
	 * @return 数据list
	 * @see DataTable
	 * @throws MetaException
	 * @author chenyf
	 */
	public List<Map<String, Object>> QueryData(List<String> codes, List<SqlWhere> where1, String sortcode) throws MetaTableException {
		DataTable queryData = metaDao.QueryData(codes, where1, sortcode);
		List<Map<String, Object>> list = getResult(queryData);
		return list;
	}

	public List<Map<String, Object>> QueryData(List<String> codes, List<SqlWhere> where1, String sortcode, boolean unitName) throws MetaTableException {
		DataTable queryData = metaDao.QueryData(codes, where1, sortcode);
		List<Map<String, Object>> list = getResult(queryData, unitName);
		return list;
	}

	/**
	 * 获取默认的DataTable
	 * 
	 * @param codes
	 * @param where1
	 * @param sortcode
	 * @return
	 * @throws MetaException
	 * @author chenyf
	 */
	public DataTable getTree(List<String> codes, List<SqlWhere> where1, String sortcode) throws MetaTableException {
		return this.metaDao.QueryData(codes, where1, sortcode);
	}

	/**
	 * 获取默认的DataTable
	 * 
	 * @param codes
	 * @param where1
	 * @param sortcode
	 * @return
	 * @throws MetaException
	 * @author chenyf
	 */
	public DataTable getTree(List<String> codes, String where1, String sortcode) throws MetaTableException {
		return this.metaDao.QueryData(codes, where1, sortcode);
	}

	/***
	 * 获取到处理后的结果
	 * 
	 * @param queryData
	 * @author chenyf
	 */
	private List<Map<String, Object>> getResult(DataTable queryData) {
		return getResult(queryData, true);
	}

	private List<Map<String, Object>> getResult(DataTable queryData, boolean hasUnitName) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		// 所包含的字段
		List<String> keyList = getColumnName(queryData.getColumns());
		List<DataTableRow> rows = queryData.getRows();
		CubeUnitManager ut1 = CubeUnitManager.CubeUnitManagerFactor.getInstance("");
		for (int i = 0; i < rows.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			DataTableRow row = rows.get(i);
			for (int j = 0; j < keyList.size(); j++) {
				String code = keyList.get(j);
				String value = row.getString(code);
				if ("unitcode".equals(code) && !StringUtil.isEmpty(value)) {
					CubeUnit dd = ut1.getUnit(value);
					if (hasUnitName && dd != null) {
						value += " : " + dd.getName();
					}
				} else {
					List<String> date = isDate();
					if (date.contains(code)) {
						if (!StringUtil.isEmpty(value) && value.length() >= 11) {
							value = value.substring(0, value.length() - 11);
						}
					} else {
						String formatValue = formatDate(code, value);
						if (null != formatValue) {
							value = formatValue;
						}
					}
				}
				map.put(code, value);
			}
			result.add(map);
		}
		return result;
	}

	/**
	 * 获取到字段名称
	 * 
	 * @author chenyf
	 */
	private List<String> getColumnName(DataTableColumns columns) {
		List<String> list = new ArrayList<String>();
		for (int j = 0; j < columns.size(); j++) {
			list.add(columns.get(j).getColumnName().toLowerCase());
		}
		return list;
	}

	/**
	 * 得到此表的meta相关信息
	 * 
	 * @return 表的meta相关信息
	 * @see CMetaTable
	 * @author chenyf
	 */
	public CubeMetaTable getMetaTable() {
		return this.metaDao.getMetaTable();
	}

	/**
	 * 查询记录数据量
	 * 
	 * @param where1
	 *            查询的条件列表
	 * @return 记录总条数
	 * @throws MetaException
	 * @author chenyf
	 */
	public int QueryCount(List<SqlWhere> where1) throws MetaTableException {
		return this.metaDao.QueryCount(where1);
	}

	/**
	 * 查询记录数据量
	 * 
	 * @param where1
	 *            查询的条件
	 * @return 记录总条数
	 * @throws MetaException
	 * @author chenyf
	 */
	public int QueryCount(String where1) throws MetaTableException {
		return this.metaDao.QueryCount(where1);
	}

	/**
	 * 查询记录数据量，根据某个节点开始递归查询整个树
	 * 
	 * @param where1
	 *            查询的条件列表
	 * @see CSqlWhere
	 * @param code
	 *            树节点的字段名
	 * @param bvalue
	 *            树节点开始的值
	 * @param procode
	 *            树关系节点的字段名
	 * @return 记录总条数
	 * @throws MetaException
	 * @author chenyf
	 */
	public int QueryCount_InTree(List<SqlWhere> where1, String code, String bvalue, String procode) throws MetaTableException {
		return this.metaDao.QueryCount_InTree(where1, code, bvalue, procode);
	}

	/**
	 * 查询数据，最多返回1000条记录
	 * 
	 * @param codes
	 *            需要返回的字段列表
	 * @param where1
	 *            查询的条件
	 * @param sortcode
	 *            排序字段
	 * @return 数据表格
	 * @see DataTable
	 * @throws MetaException
	 * @author chenyf
	 */
	public List<Map<String, Object>> QueryData(List<String> codes, String where1, String sortcode) throws MetaTableException {
		DataTable queryData = this.metaDao.QueryData(codes, where1, sortcode);
		List<Map<String, Object>> list = getResult(queryData);
		return list;
	}

	public List<Map<String, Object>> QueryData(List<String> codes, String where1, String sortcode, boolean hasUnitName) throws MetaTableException {
		DataTable queryData = this.metaDao.QueryData(codes, where1, sortcode);
		List<Map<String, Object>> list = getResult(queryData, hasUnitName);
		return list;
	}

	/**
	 * 查询数据，根据某个节点开始递归查询整个树，最多返回1000条记录
	 * 
	 * @param codes
	 *            需要返回的字段列表
	 * @param where1
	 *            查询的条件列表
	 * @see CSqlWhere
	 * @param code
	 *            树节点的字段名
	 * @param bvalue
	 *            树节点开始的值
	 * @param procode
	 *            树关系节点的字段名
	 * @param sortcode
	 *            排序字段
	 * @return 数据表格
	 * @see DataTable
	 * @throws MetaException
	 * @author chenyf
	 */
	public List<Map<String, Object>> QueryData_InTree(List<String> codes, List<SqlWhere> where1, String code, String bvalue, String procode, String sortcode) throws MetaTableException {
		DataTable queryData = this.metaDao.QueryData_InTree(codes, where1, code, bvalue, procode, sortcode);
		List<Map<String, Object>> list = getResult(queryData);
		return list;
	}

	/**
	 * 查询树的路径
	 * 
	 * @param codes
	 * @param code
	 * @param bvalue
	 * @param procode
	 * @return
	 * @throws MetaException
	 * @author chenyf
	 */
	public List<Map<String, Object>> QueryData_TreePath(List<String> codes, String code, String bvalue, String procode) throws MetaTableException {
		DataTable queryData = this.metaDao.QueryData_TreePath(codes, code, bvalue, procode);
		List<Map<String, Object>> list = getResult(queryData);
		return list;
	}

	/**
	 * 获取treepath
	 * 
	 * @param code
	 * @return
	 * @throws MetaException
	 */
	public String getTreePath(String code) throws MetaTableException {
		DataTable queryData = this.metaDao.QueryData_TreePath(null, "code", code, "procode");
		List<Map<String, Object>> result = getResult(queryData);
		String str = "";
		for (int i = result.size() - 1; i >= 0; i--) {
			Map<String, Object> map = result.get(i);
			String object = (String) map.get("code");
			if (StringUtil.isEmpty(object)) {
				continue;
			}
			str = str + "/" + object;
		}
		return str;
	}

	/**
	 * 查询数据，每次返回一页记录
	 * 
	 * @param codes
	 *            需要返回的字段列表
	 * @param where1
	 *            查询的条件列表
	 * @see CSqlWhere
	 * @param sortcode
	 *            排序字段
	 * @param page
	 *            第几页，从0开始
	 * @param pagesize
	 *            每页的记录数
	 * @return 数据表格
	 * @see DataTable
	 * @throws MetaException
	 * @author chenyf
	 */
	public List<Map<String, Object>> QueryData_ByPage(List<String> codes, List<SqlWhere> where1, String sortcode, int page, int pagesize) throws MetaTableException {
		DataTable queryData = metaDao.QueryData_ByPage(codes, where1, sortcode, page, pagesize);
		List<Map<String, Object>> list = getResult(queryData);
		return list;
	}

	/**
	 * 查询数据， 每次返回一页记录
	 * 
	 * @param codes
	 *            需要返回的字段列表
	 * @param where1
	 *            查询的条件列表
	 * @see CSqlWhere
	 * @param sortcode
	 *            排序字段
	 * @param page
	 *            第几页，从0开始
	 * @param pagesize
	 *            每页的记录数
	 * @return 数据表格
	 * @see DataTable
	 * @throws MetaException
	 * @author chenyf
	 */
	public List<Map<String, Object>> QueryData_ByPage(List<String> codes, String where1, String sortcode, int page, int pagesize) throws MetaTableException {
		DataTable queryData = this.metaDao.QueryData_ByPage(codes, where1, sortcode, page, pagesize);
		List<Map<String, Object>> list = getResult(queryData);
		return list;
	}

	/**
	 * 查询数据，根据某个节点开始递归查询整个树，每次返回一页记录
	 * 
	 * @param codes
	 *            需要返回的字段列表
	 * @param where1
	 *            查询的条件列表
	 * @see CSqlWhere
	 * @param code
	 *            树节点的字段名
	 * @param bvalue
	 *            树节点开始的值
	 * @param procode
	 *            树关系节点的字段名
	 * @param sortcode
	 *            排序字段
	 * @param page
	 *            第几页，从0开始
	 * @param pagesize
	 *            每页的记录数
	 * @return 数据表格
	 * @see DataTable
	 * @throws MetaException
	 * @author chenyf
	 */
	public List<Map<String, Object>> QueryData_InTree_ByPage(List<String> codes, List<SqlWhere> where1, String code, String bvalue, String procode, String sortcode, int page, int pagesize) throws MetaTableException {
		DataTable queryData_InTree_ByPage = this.metaDao.QueryData_InTree_ByPage(codes, where1, code, bvalue, procode, sortcode, page, pagesize);
		return getResult(queryData_InTree_ByPage);
	}

	/**
	 * 新增一条记录
	 * 
	 * @param codes
	 *            新增记录列表
	 * @return 更新记录条数
	 * @throws MetaException
	 * @author chenyf
	 */
	public int InsertRow(Map<String, Object> codes) throws MetaTableException {
		int insertRow = this.metaDao.InsertRow(codes);
		// 更新缓存
		if ("unit".equals(tbcode)) {
			unitCube.updateUnit((String) codes.get("code"));
		} else if ("hv".equals(tbcode)) {
			//
		} else {
			cube.updateWdNode(getDbcode(tbcode), (String) codes.get("code"));
		}
		return insertRow;
	}

	/**
	 * 批量新增记录
	 * 
	 * @param codes
	 *            新增记录列表
	 * @return 新增记录条数
	 * @throws MetaException
	 * @author chenyf
	 */
	public int InsertRows(List<Map<String, Object>> codes) throws MetaTableException {
		int insertRows = this.metaDao.InsertRows(codes);
		// 更新缓存
		for (Map<String, Object> map : codes) {
			if ("unit".equals(tbcode)) {
				unitCube.updateUnit((String) map.get("code"));
			} else if ("hv".equals(tbcode)) {
				//
			} else {
				cube.updateWdNode(getDbcode(tbcode), (String) map.get("code"));
			}
		}
		return insertRows;
	}

	/**
	 * 更新一行
	 * 
	 * @param keys
	 *            更新的关键字信息
	 * @param codes
	 *            需要更新字段的内容，如果需要清空某字段则为NULL
	 * @return 更新记录条数
	 * @throws MetaException
	 * @author chenyf
	 */
	public int UpdateRow(Map<String, Object> keys, Map<String, Object> codes) throws MetaTableException {
		int updateRow = this.metaDao.UpdateRow(keys, codes);
		// 更新缓存
		if ("unit".equals(tbcode)) {
			unitCube.updateUnit((String) keys.get("code"));
		} else if ("hv".equals(tbcode)) {
			//
		} else {
			cube.updateWdNode(getDbcode(tbcode), (String) keys.get("code"));
		}
		return updateRow;
	}

	/**
	 * 批量更新数据接口
	 * 
	 * @param keys
	 *            关键字列表
	 * @param codes
	 *            需要更新的字段列表
	 * @return 更新的条数
	 * @throws MetaException
	 * @author chenyf
	 */
	public int UpdateRows(List<Map<String, Object>> keys, List<Map<String, Object>> codes) throws MetaTableException {
		int updateRows = this.metaDao.UpdateRows(keys, codes);
		// 更新缓存
		for (Map<String, Object> map : keys) {
			if ("unit".equals(tbcode)) {
				unitCube.updateUnit((String) map.get("code"));
			} else if ("hv".equals(tbcode)) {
				//
			} else {
				cube.updateWdNode(getDbcode(tbcode), (String) map.get("code"));
			}
		}
		return updateRows;
	}

	/**
	 * 去除date类型的毫秒数
	 * 
	 * @param code
	 * @param value
	 * @return
	 * @author chenyf
	 */
	public String formatDate(String code, String value) {
		if (("createtime".equals(code) || "updatetime".equals(code)) && !StringUtil.isEmpty(value) && value.length() >= 2) {
			return value.substring(0, value.length() - 2);
		}
		return null;
	}

	/**
	 * 处理时间数据
	 * 
	 * @author chenyf
	 */
	public static List<String> isDate() {
		List<String> list = new ArrayList<String>();
		list.add("btime");
		list.add("etime");
		return list;
	}

	/**
	 * 删除单条记录
	 * 
	 * @param keys
	 * @return
	 * @throws MetaException
	 * @author chenyf
	 */
	public int deleteRow(Map<String, Object> keys) throws MetaTableException {
		int deleteRow = this.metaDao.DeleteRow(keys);
		// 更新缓存
		if ("unit".equals(tbcode)) {
			unitCube.deleteUnit((String) keys.get("code"));
		} else if ("hv".equals(tbcode)) {
			//
		} else {
			cube.deleteWdNode(getDbcode(tbcode), (String) keys.get("code"));
		}
		return deleteRow;
	}

	/**
	 * 删除多条记录
	 * 
	 * @param keys
	 * @return
	 * @throws MetaException
	 * @author chenyf
	 */
	public int deleteRows(List<Map<String, Object>> keys) throws MetaTableException {
		int deleteRows = this.metaDao.DeleteRows(keys);
		// 更新缓存
		for (Map<String, Object> map : keys) {
			if ("unit".equals(tbcode)) {
				unitCube.deleteUnit((String) map.get("code"));
			} else if ("hv".equals(tbcode)) {
				//
			} else {
				cube.deleteWdNode(getDbcode(tbcode), (String) map.get("code"));
			}
		}
		return deleteRows;
	}

	/**
	 * 自定义sql
	 * 
	 * @return
	 * @throws MetaException
	 * @author chenyf
	 */
	public DataTable queryDataSql(String sql, Object[] parm) throws MetaTableException {
		return this.metaDao.QueryDataSql(sql, parm);
	}

	/**
	 * 查询同义词
	 * 
	 * @return
	 */
	public List<Synonyms> getSynonyms(String code) {
		// 查询同义词
		String dbSql = "select * from TB_SEARCH_SYNONYMS where sort = '3' and code = ?";
		Object[] parm = { code };
		ArrayList<Synonyms> list = new ArrayList<Synonyms>();
		try {
			DataTable dt = queryDataSql(dbSql, parm);
			if (null != dt && dt.getRows().size() > 0) {
				for (int i = 0; i < dt.getRows().size(); i++) {
					DataTableRow row = dt.getRows().get(i);
					Synonyms synonyms = new Synonyms();
					synonyms.setWdcode(row.getString("wdcode"));
					synonyms.setSort(row.getString("sort"));
					synonyms.setDbcode(row.getString("dbcode"));
					synonyms.setCname(row.getString("cname"));
					synonyms.setCode(row.getString("code"));
					list.add(synonyms);
				}
			}
		} catch (MetaTableException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 查询同义词名称是否存在
	 * 
	 * @return
	 */
	public int querySysnName(String cname, String code) {
		// 查询同义词
		String dbSql = "select count(*) from TB_SEARCH_SYNONYMS where cname = ?";
		ArrayList<Object> parm = new ArrayList<Object>();
		parm.add(cname);
		if (!StringUtil.isEmpty(code)) {
			dbSql += " and code!=?";
			parm.add(code);
		}
		try {
			DataTable dt = queryDataSql(dbSql, parm.toArray());
			if (null != dt && dt.getRows().size() > 0) {
				return dt.getRows().get(0).getint(0);
			}
		} catch (MetaTableException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 拼接同义词
	 * 
	 * @param list
	 * @return
	 */
	public String getSynonString(List<Synonyms> list) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (i != 0) {
				sb.append("__");
			}
			sb.append(list.get(i).getCname());
		}
		return sb.toString();
	}

	/**
	 * 增加同义词
	 * 
	 * @param req
	 * @param resp
	 */
	public int addSynonyms(List<Synonyms> list, String code, String wdcode) {
		CubeMetaTbSev metaDao = MetaManager.getMetaDao(Zbmgr.metacode);
		// 先删除之前的所有
		Map<String, Object> keys = new HashMap<String, Object>();
		keys.put("code", code);
		keys.put("wdcode", wdcode);
		try {
			metaDao.DeleteRow(keys);
		} catch (MetaTableException e1) {
			e1.printStackTrace();
		}
		CubeSearchSev s1 = CubeSearchSev.CubeSearchAPIFactor.getInstance();
		// 新增数据
		List<Map<String, Object>> syns = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Synonyms synonym = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("wdcode", synonym.getWdcode());
			map.put("sort", synonym.getSort());
			map.put("cname", synonym.getCname());
			map.put("code", synonym.getCode());
			map.put("dbcode", synonym.getDbcode());
			String path1 = s1.getTreePath(Const.DB_CODE, wdcode, code);
			String ceng0 = "";
			String ceng1 = "";
			String s[] = path1.split("/");
			if (s.length > 0) {
				ceng0 = s[0];
				ceng1 = ceng0;
			}
			if (s.length > 1) {
				ceng1 = s[1];
			}
			if (!ceng0.equals("")) {
				map.put("ceng0", ceng0);
			}
			if (!ceng1.equals("")) {
				map.put("ceng1", ceng1);
			}
			syns.add(map);
		}
		int count = 0;
		try {
			count = metaDao.InsertRows(syns);
		} catch (MetaTableException e) {
			e.printStackTrace();
		}
		// 增加索引
		if (count > 0) {
			s1.CreateIndex(Zbmgr.dbcode, wdcode, code);
		}
		return count;
	}

	public String getValue(String str) {
		if (StringUtil.isEmpty(str)) {
			return str;
		}
		Pattern p = Pattern.compile("^\\d+\\.0$");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return str.substring(0, str.length() - 2);
		}
		return str;
	}

	public String getDbcode(String code) {
		return code;
	}

}
