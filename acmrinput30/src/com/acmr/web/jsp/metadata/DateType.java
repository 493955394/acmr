package com.acmr.web.jsp.metadata;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import acmr.cubeinput.MetaTableException;
import acmr.cubeinput.service.metatable.entity.CubeMetaTable;
import acmr.cubeinput.service.metatable.entity.SqlWhere;
import acmr.excel.ExcelException;
import acmr.excel.pojo.Constants.XLSTYPE;
import acmr.excel.pojo.ExcelBook;
import acmr.excel.pojo.ExcelCell;
import acmr.excel.pojo.ExcelCellStyle;
import acmr.excel.pojo.ExcelRow;
import acmr.excel.pojo.ExcelSheet;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.web.control.BaseAction;
import acmr.web.core.CurrentContext;
import acmr.web.entity.ModelAndView;

import com.acmr.helper.constants.Const;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.pub.PageBean;
import com.acmr.model.pub.TreeNode;
import com.acmr.model.security.User;
import com.acmr.service.LogService;
import com.acmr.service.metadata.MetaDataExport;
import com.acmr.service.metadata.MetaExcelColor;
import com.acmr.service.metadata.MetaService;
import com.acmr.service.metadata.MetaServiceManager;
import com.acmr.service.metadata.MultipleTree;
import com.acmr.service.metadata.Synonyms;
import com.acmr.service.security.UserService;
import com.acmr.web.jsp.log.LogConst;

/**
 * 模块：元数据管理 -> 时间类型 action层
 * 
 */
public class DateType extends BaseAction {
	/**
	 * 获取service层对象（cube）
	 * 
	 * @author chenyf
	 */
	private MetaServiceManager dtService = MetaService.getMetaService("tt");

	/**
	 * 查询所有
	 * 
	 * @author chenyf
	 */
	public ModelAndView main() {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		// 获取查询数据
		PageBean<Map<String, Object>> page = new PageBean<Map<String, Object>>();
		List<SqlWhere> where1 = new ArrayList<SqlWhere>();

		StringBuffer sb = new StringBuffer();
		sb.append(req.getRequestURI());
		sb.append("?m=find");
		String bottom = null;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			// 根节点
			where1.add(new SqlWhere("procode", "NULL", 00));
			list = dtService.QueryData_ByPage(null, where1, "sortcode", page.getPageNum() - 1, page.getPageSize());
			page.setData(list);
			int count = dtService.QueryCount(where1);
			page.setTotalRecorder(count);
			page.setUrl(sb.toString());

			// 如果下一页没有数据，就说明是最后一页
			List<Map<String, Object>> queryData_ByPage = dtService.QueryData_ByPage(null, where1, "sortcode", page.getPageNum(), page.getPageSize());
			if (queryData_ByPage.size() >= 1) {
				Map<String, Object> map = queryData_ByPage.get(0);
				bottom = (String) map.get("code");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("/WEB-INF/jsp/metadata/ttmgr/index").addObject("page", page).addObject("ismove", 1).addObject("bottom", bottom);
	}

	/***
	 * 获取子节点内容
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @author chenyf
	 */
	public void query() throws IOException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		// 获取查询数据
		String code = req.getParameter("id");
		PageBean<Map<String, Object>> page = new PageBean<Map<String, Object>>();
		StringBuffer sb = new StringBuffer();
		sb.append(req.getRequestURI());
		sb.append("?m=query");

		try {
			List<SqlWhere> where1 = new ArrayList<SqlWhere>();
			if (!StringUtil.isEmpty(code)) {
				sb.append("&id=").append(code);
				where1.add(new SqlWhere("procode", code, 10));
			} else {
				where1.add(new SqlWhere("procode", "NULL", 00));
			}
			List<Map<String, Object>> list = dtService.QueryData_ByPage(null, where1, "sortcode", page.getPageNum() - 1, page.getPageSize());
			int queryCount = dtService.QueryCount(where1);
			if (queryCount == 0) {
				List<SqlWhere> where2 = new ArrayList<SqlWhere>();
				if (!StringUtil.isEmpty(code)) {
					where2.add(new SqlWhere("code", code, 10));
				}
				list = dtService.QueryData_ByPage(null, where2, "sortcode", page.getPageNum() - 1, page.getPageSize());
				queryCount = dtService.QueryCount(where2);
			}
			page.setData(list);
			page.setTotalRecorder(queryCount);
			page.setUrl(sb.toString());
			// 发送返回数据
			this.sendJson(new JSONReturnData(page));
		} catch (MetaTableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 获取时间类型树
	 * 
	 * @throws MetaTableException
	 * 
	 * @throws IOException
	 * @author chenyf
	 * 
	 */
	public void findDtTree() throws MetaTableException, IOException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		List<SqlWhere> where1 = new ArrayList<SqlWhere>();
		String code = req.getParameter("id");

		if (StringUtil.isEmpty(code)) {
			where1.add(new SqlWhere("procode", "NULL", 00));
		} else {
			where1.add(new SqlWhere("procode", code, 10));
		}
		// 取出状态为不删除的数据
		DataTable queryData = dtService.getTree(null, where1, "sortcode");
		List<DataTableRow> rows = queryData.getRows();
		List<TreeNode> list = new ArrayList<TreeNode>();
		for (int i = 0; i < rows.size(); i++) {
			DataTableRow row = rows.get(i);
			TreeNode treeNode = new TreeNode();
			treeNode.setId(row.getString("code"));
			String ifdata = row.getString("ifdata");
			Boolean ifd = true;
			if (Const.IFDATA.equals(ifdata)) {
				where1.clear();
				where1.add(new SqlWhere("procode", row.getString("code"), 10));
				int queryCount = dtService.QueryCount(where1);
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
		this.sendJson(list);
	}

	/**
	 * 查找
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws MetaTableException
	 * @author chenyf
	 */
	public ModelAndView find() throws IOException, MetaTableException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		// 获取查询数据
		String procode = req.getParameter("id");
		// 判断是否pjax 请求
		String pjax = req.getHeader("X-PJAX");
		String treeList = "";

		String pageUrl = MetaService.getPageUrl(req);
		PageBean<Map<String, Object>> page = new PageBean<Map<String, Object>>();
		List<SqlWhere> where1 = new ArrayList<SqlWhere>();
		String top = null;
		String bottom = null;

		if (!StringUtil.isEmpty(procode)) {
			treeList = dtService.getTreePath(procode);
			where1.add(new SqlWhere("procode", procode, 10));
		} else {
			where1.add(new SqlWhere("procode", "NULL", 00));
		}

		List<Map<String, Object>> list = dtService.QueryData_ByPage(null, where1, "sortcode", page.getPageNum() - 1, page.getPageSize());
		// 说明不是第一页，就要取上一页的最后一条
		if (page.getPageNum() != 1) {
			List<Map<String, Object>> queryData_ByPage = dtService.QueryData_ByPage(null, where1, "sortcode", page.getPageNum() - 2, page.getPageSize());
			if (queryData_ByPage.size() >= 1) {
				Map<String, Object> map = queryData_ByPage.get(queryData_ByPage.size() - 1);
				top = (String) map.get("code");
			}
		}

		// 如果下一页没有数据，就说明是最后一页
		List<Map<String, Object>> queryData_ByPage = dtService.QueryData_ByPage(null, where1, "sortcode", page.getPageNum(), page.getPageSize());
		if (queryData_ByPage.size() >= 1) {
			Map<String, Object> map = queryData_ByPage.get(0);
			bottom = (String) map.get("code");
		}

		// end
		int queryCount = dtService.QueryCount(where1);
		if (queryCount == 0 && !StringUtil.isEmpty(procode)) {
			where1.clear();
			where1.add(new SqlWhere("code", procode, 10));
			list = dtService.QueryData_ByPage(null, where1, "sortcode", page.getPageNum() - 1, page.getPageSize());
			queryCount = dtService.QueryCount(where1);
		}
		try {
			page.setData(list);
			page.setTotalRecorder(queryCount);
			page.setUrl(pageUrl);
			 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_DATETYPE, LogConst.EXC_QUERY, null, "" + queryCount, procode, procode, null, null, null, null, null, null, null, null, null);
			// 发送返回数据
			// this.sendJson(resp, new JSONReturnData(page));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (StringUtil.isEmpty(pjax)) {
			return new ModelAndView("/WEB-INF/jsp/metadata/ttmgr/index").addObject("page", page).addObject("initTreePara", treeList).addObject("ismove", 1).addObject("top", top).addObject("bottom", bottom);
		} else {
			return new ModelAndView("/WEB-INF/jsp/metadata/ttmgr/tableList").addObject("page", page).addObject("ismove", 1).addObject("top", top).addObject("bottom", bottom);
		}
	}

	/**
	 * 快速查找
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws MetaTableException
	 * @author chenyf
	 */
	public ModelAndView quickFind() throws IOException, MetaTableException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		// 获取查询数据
		String code = StringUtil.toLowerString(req.getParameter("code"));
		String cname = StringUtil.toLowerString(req.getParameter("cname"));
		String procode = req.getParameter("id");
		// 判断是否pjax 请求
		String pjax = req.getHeader("X-PJAX");
		String treeList = "";

		PageBean<Map<String, Object>> page = new PageBean<Map<String, Object>>();
		List<SqlWhere> where1 = new ArrayList<SqlWhere>();

		String url = MetaService.getPageUrl(req);
		String searchPara = MetaService.getMetaPara(req);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 添加查找条件
		try {
			treeList = dtService.getTreePath(procode);
		} catch (MetaTableException e) {
			e.printStackTrace();
		}
		if (!StringUtil.isEmpty(code)) {
			where1.add(new SqlWhere("lower(code)", "%" + MetaService.getSearchReplace(code) + "%", 20));
		}
		if (!StringUtil.isEmpty(cname)) {
			where1.add(new SqlWhere("lower(cname)", "%" + MetaService.getSearchReplace(cname) + "%", 20));
		}

		int count = 0;
		if (StringUtil.isEmpty(procode)) {
			list = dtService.QueryData_ByPage(null, where1, "sortcode", page.getPageNum() - 1, page.getPageSize());
			count = dtService.QueryCount(where1);
		} else {
			list = dtService.QueryData_InTree_ByPage(null, where1, "code", procode, "procode", "sortcode", page.getPageNum() - 1, page.getPageSize());
			count = dtService.QueryCount_InTree(where1, "code", procode, "procode");
		}
		try {
			page.setData(list);
			page.setTotalRecorder(count);
			page.setUrl(url.toString().replaceAll("%", "%25"));

			Map<String, String> codes = new HashMap<String, String>();
			codes.put("code", code);
			codes.put("cname", cname);
			codes.put("procode", procode);

			 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_DATETYPE, LogConst.EXCSEC_FIND, null, "" + count, codes, code, cname, procode, null, null, null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (StringUtil.isEmpty(pjax)) {
			return new ModelAndView("/WEB-INF/jsp/metadata/ttmgr/index").addObject("page", page).addObject("code", code.replaceAll("%25", "%")).addObject("cname", cname.replaceAll("%25", "%")).addObject("initTreePara", treeList).addObject("searchPara", searchPara).addObject("ismove", 0);
		} else {
			return new ModelAndView("/WEB-INF/jsp/metadata/ttmgr/tableList").addObject("page", page).addObject("ismove", 0);
		}
	}

	/**
	 * 跳转到新增页面
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @author chenyf
	 */
	public ModelAndView toAdd() {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		String code = req.getParameter("code");
		String proname = req.getParameter("cname");
		CubeMetaTable cubeMetaTable = dtService.getMetaTable();
		if(cubeMetaTable != null){
			int codeLen = cubeMetaTable.getCodelen();
			if(codeLen == 0 ||codeLen>20){
				codeLen = 20;
			}
			req.setAttribute("codeLen", codeLen);
		}
		try {
			if (!StringUtil.isEmpty(code)) {
				code = URLDecoder.decode(code, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ModelAndView("/WEB-INF/jsp/metadata/ttmgr/add").addObject("code", code).addObject("proname", proname);
	}

	/**
	 * 添加数据
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 * @author chenyf
	 */
	public void add() throws IOException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		String id = req.getParameter("id");
		String cname = req.getParameter("cname"); // 中文名称
		String ccname = req.getParameter("ccname");// 中文全称
		String cname_en = req.getParameter("cname_en"); // 英文名称
		String ccname_en = req.getParameter("ccname_en"); // 英文全称
		String cexp = req.getParameter("cexp"); // 中文解释
		String cexp_en = req.getParameter("cexp_en"); // 英文解释
		String cmemo = req.getParameter("cmemo"); // 中文备注
		String cmemo_en = req.getParameter("cmemo_en"); // 英文备注
		String code = req.getParameter("code"); // 时间类型代码
		String unitcode = req.getParameter("unitcode");// 中文单位
		String procode = req.getParameter("procode");// 上级节点
		String dotcount = req.getParameter("dotcount");// 小数点位数
		String ifdata = req.getParameter("ifdata"); // 事件类型分类
		String synonym = req.getParameter("synonym"); // 同义词

		JSONReturnData data = new JSONReturnData(500, "添加失败");

		Map<String, Object> codes = new HashMap<String, Object>();

		codes.put("cname", cname);
		codes.put("ccname", ccname);
		codes.put("cname_en", cname_en);
		codes.put("ccname_en", ccname_en);
		codes.put("cexp", cexp);
		codes.put("cexp_en", cexp_en);
		codes.put("cmemo", cmemo);
		codes.put("cmemo_en", cmemo_en);
		codes.put("unitcode", unitcode);
		codes.put("dotcount", dotcount);
		codes.put("code", code);

		Date date = new Date();
		Timestamp tt = new Timestamp(date.getTime());
		codes.put("updatetime", tt);
		if (!StringUtil.isEmpty(ifdata)) {
			codes.put("ifdata", ifdata);
		}

		User currentUser = UserService.getCurrentUser();
		if (null != currentUser) {
			codes.put("updateby", currentUser.getUserid());
		}

		if (StringUtil.isEmpty(procode)) {
			codes.put("procode", "");
		} else {
			String clearCode = procode.replaceAll("\\s*", "");
			codes.put("procode", clearCode);
		}

		// 添加时的数据
		if (StringUtil.isEmpty(id)) {
			code = code.replaceAll("\\s*", "");
			codes.put("code", code);
			codes.put("sortcode", code);
			codes.put("createtime", tt);
			codes.put("ifclose", "0");
			if (null != currentUser) {
				codes.put("createby", currentUser.getUserid());
			}

		}

		if (StringUtil.isEmpty(id)) {
			try {
				int insertRow = dtService.InsertRow(codes);
				 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_DATETYPE, LogConst.EXCSEC_INSERT, null, "" + insertRow, codes, code, procode, null, null, null, null, null, null, null, null);
				if (insertRow == 1) {
					data.setReturncode(200);
					data.setParam1(code);
					data.setReturndata("操作成功");
				}
			} catch (MetaTableException e) {
				this.sendJson(data);
				return;
			}
		} else {
			Map<String, Object> keys = new HashMap<String, Object>();
			keys.put("code", code);
			try {
				int updateRow = dtService.UpdateRow(keys, codes);
				 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_DATETYPE, LogConst.EXCSEC_UPDATE, null, "" + updateRow, codes, code, procode, null, null, null, null, null, null, null, null);
				if (updateRow == 1) {
					data.setReturncode(200);
					data.setParam1(code);
					data.setReturndata("操作成功");
				}
			} catch (MetaTableException e) {
				this.sendJson(data);
				return;
			}
		}

		// 同义词
		ArrayList<Synonyms> list = new ArrayList<Synonyms>();
		if (!StringUtil.isEmpty(cname)) {
			list.add(new Synonyms("tt", "1", cname, code, Zbmgr.dbcode));
		}
		if (!StringUtil.isEmpty(ccname)) {
			list.add(new Synonyms("tt", "2", ccname, code, Zbmgr.dbcode));
		}

		if (!StringUtil.isEmpty(synonym)) {
			String[] split = synonym.split("__");
			for (int i = 0; i < split.length; i++) {
				if (StringUtil.isEmpty(split[i])) {
					continue;
				}
				list.add(new Synonyms("tt", "3", split[i], code, Zbmgr.dbcode));
			}
		}

		// 元数据操作成功 添加同义词
		if (200 == data.getReturncode()) {
			dtService.addSynonyms(list, code, "tt");
		}

		this.sendJson(data);
	}

	/**
	 * 跳转到修改界面
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @author chenyf
	 */
	public ModelAndView turnToUpdate() {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		String code = req.getParameter("id");
		Map<String, Object> data = new HashMap<String, Object>();
		String procodeName = "时间类型树";
		String unicode = "";// 单位名称
		try {
			List<Map<String, Object>> queryData = dtService.QueryData(null, "code='" + code + "'", "sortcode", false);
			if (queryData.size() > 0) {
				data = queryData.get(0);
			}
			List<String> codes = new ArrayList<String>();
			codes.add("code");
			codes.add("cname");

			String procode = (String) data.get("procode");
			String unitcode = (String) data.get("unitcode");

			// 获取父节点名称
			if (!StringUtil.isEmpty(procode)) {
				DataTable tree = dtService.getTree(codes, "code='" + procode + "'", "sortcode");
				if (tree.getRows().size() > 0) {
					procodeName = tree.getRows().get(0).getString("cname");
				}
			}

			// 获取单位的名称
			if (!StringUtil.isEmpty(unitcode)) {
				List<Map<String, Object>> queryData2 = MetaService.getMetaService("unit").QueryData(codes, "code='" + unitcode + "'", "sortcode");
				if (queryData2.size() > 0) {
					unicode = (String) queryData2.get(0).get("cname");
				}
			}

		} catch (MetaTableException e) {
			e.printStackTrace();
		}
		// ModelMap modelMap = new ModelMap();
		// modelMap.putAll(data);
		// 查询同义词
		List<Synonyms> list = dtService.getSynonyms(code);
		CubeMetaTable cubeMetaTable = dtService.getMetaTable();
		if(cubeMetaTable != null){
			int codeLen = cubeMetaTable.getCodelen();
			if(codeLen == 0 ||codeLen>20){
				codeLen = 20;
			}
			req.setAttribute("codeLen", codeLen);
		}
		return new ModelAndView("/WEB-INF/jsp/metadata/ttmgr/edit").addObject("procodeName", procodeName).addObject("unicode", unicode).addObject("list", list).addObject("tyc", dtService.getSynonString(list)).addObject(data);
	}

	/**
	 * 进行单个数据的删除
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 * @author chenyf
	 */
	public void toDelete() throws IOException {
		HttpServletRequest req = CurrentContext.getRequest();
		String id = req.getParameter("delId");
		id = id.trim();
		int result = 0;
		// 构造返回对象
		JSONReturnData data = new JSONReturnData(501, "删除失败");
		if (StringUtil.isEmpty(id)) {
			this.sendJson(data);
			return;
		}
		try {
			List<SqlWhere> where1 = new ArrayList<SqlWhere>();
			where1.add(new SqlWhere("procode", id, 10));
			int queryCount = dtService.QueryCount(where1);
			if (queryCount > 0) {
				data.setReturncode(501);
				data.setReturndata("该指标正在被其他指标使用");
				this.sendJson(data);
				return;
			}
			// 底库
			MetaServiceManager metaService3 = MetaService.getMetaService("data");
			int queryCount2 = metaService3.QueryCount("timetype = '" + id + "'");
			if (queryCount2 > 0) {
				data.setReturndata("该指标在底库中存在数据");
				this.sendJson(data);
				return;
			}
			// 模板表
			// MetaDataDaoImpl metaDataDao = new MetaDataDaoImpl();
			// boolean queryTemplate = metaDataDao.queryTemplate("tt_" + id);
			// if (queryTemplate) {
			// data.setReturndata("该指标已经被模板使用");
			// this.sendJson(resp, data);
			// return;
			// }
			// 任务表
			// boolean queryTask = metaDataDao.queryTask("tt_" + id);
			// if (queryTask) {
			// data.setReturndata("该指标正在任务表中使用");
			// this.sendJson(resp, data);
			// return;
			// }
			// 临时表
			// boolean queryTmpTable = metaDataDao.queryTmpTable(id, "timetype");
			// if (queryTmpTable) {
			// data.setReturndata("该指标正在临时表中使用");
			// this.sendJson(data);
			// return;
			// }

			Map<String, Object> keys = new HashMap<String, Object>();
			keys.put("code", id);
			result = dtService.deleteRow(keys);
			if (result != 0) {
				data.setReturncode(200);
				data.setReturndata("删除成功");
			}

		} catch (MetaTableException e) {
			this.sendJson(data);
			// logger.info("删除时间类型失败");
			return;
		}
		this.sendJson(data);
		// logger.info("删除时间类型结束");
	}

	/**
	 * 进行多个数据的删除
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 * @author chenyf
	 */
	public void deleteAll() throws IOException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		String cods = req.getParameter("ids");
		// 构造返回对象
		JSONReturnData data = new JSONReturnData(501, "删除失败");
		if (StringUtil.isEmpty(cods)) {
			this.sendJson(data);
			return;
		}
		String[] ids = cods.split(",");
		try {
			for (int i = 0; i < ids.length; i++) {
				List<SqlWhere> where1 = new ArrayList<SqlWhere>();
				where1.add(new SqlWhere("procode", ids[i], 10));
				int queryCount = dtService.QueryCount(where1);
				if (queryCount > 0) {
					data.setReturndata("第" + (i + 1) + "条数据正在被其他指标使用");
					this.sendJson(data);
					return;
				}
				// 底库
				MetaServiceManager metaService3 = MetaService.getMetaService("data");
				int queryCount2 = metaService3.QueryCount("timetype = '" + ids[i] + "'");
				if (queryCount2 > 0) {
					data.setReturndata("第" + (i + 1) + "条数据在底库中存在数据");
					this.sendJson(data);
					return;
				}
				// 模板表
				// MetaDataDaoImpl metaDataDao = new MetaDataDaoImpl();
				// boolean queryTemplate = metaDataDao.queryTemplate("tt_" + ids[i]);
				// if (queryTemplate) {
				// data.setReturndata("第" + (i + 1) + "条数据已经被模板使用");
				// this.sendJson(resp, data);
				// return;
				// }
				// 任务表
				// boolean queryTask = metaDataDao.queryTask("tt_" + ids[i]);
				// if (queryTask) {
				// data.setReturndata("第" + (i + 1) + "条数据正在任务表中使用");
				// this.sendJson(resp, data);
				// return;
				// }
				// 临时表
				// boolean queryTmpTable = metaDataDao.queryTmpTable(ids[i], "timetype");
				// if (queryTmpTable) {
				// data.setReturndata("第" + (i + 1) + "条数据正在临时表中使用");
				// this.sendJson(data);
				// return;
				// }
			}
			List<Map<String, Object>> keys = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				Map<String, Object> key = new HashMap<String, Object>();
				key.put("code", id);
				keys.add(key);
			}
			int updateRows = dtService.deleteRows(keys);
			if (updateRows != 0) {
				data.setReturncode(200);
				data.setReturndata("删除成功！");
				this.sendJson(data);
			}
		} catch (MetaTableException e) {
			MetaTableException metaException = new MetaTableException();
			int row = metaException.getRow();
			data.setReturndata("第" + row + "行错误！");
			this.sendJson(data);
		}
	}

	/**
	 * 检查code是否已经存在
	 * 
	 * @author chenyf
	 */
	public void checkCode() throws IOException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		String code = req.getParameter("code");
		// 构造返回对象
		JSONReturnData data = new JSONReturnData(501, "code已存在");

		try {
			int queryCount = dtService.QueryCount("code='" + code + "'");
			if (queryCount == 0) {
				data.setReturncode(200);
				data.setReturndata("可以使用");
			}
		} catch (MetaTableException e) {
			this.sendJson(data);
		}
		this.sendJson(data);
	}

	/**
	 * 上移下移
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @author chenyf
	 */
	public void move() throws IOException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		// 构造返回对象
		JSONReturnData data = new JSONReturnData(501, "移动失败");

		String currentCode = req.getParameter("currentId");
		String currentSortCode = "";
		String siblingsCode = req.getParameter("siblingsId");
		String siblingsiSortCode = "";

		List<Map<String, Object>> keys = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> codes = new ArrayList<Map<String, Object>>();

		List<String> code = new ArrayList<String>();
		code.add("sortcode");
		try {
			List<Map<String, Object>> queryData = dtService.QueryData(code, "code='" + currentCode + "'", null);
			if (queryData.size() > 0) {
				currentSortCode = (String) queryData.get(0).get("sortcode");
			}
			List<Map<String, Object>> queryData1 = dtService.QueryData(code, "code='" + siblingsCode + "'", null);
			if (queryData1.size() > 0) {
				siblingsiSortCode = (String) queryData1.get(0).get("sortcode");
			}
		} catch (MetaTableException e1) {
			e1.printStackTrace();
		}

		keys.add(getMapObj("code", currentCode));
		codes.add(getMapObj("sortcode", siblingsiSortCode));

		keys.add(getMapObj("code", siblingsCode));
		codes.add(getMapObj("sortcode", currentSortCode));
		Map<String, String> cos = new HashMap<String, String>();
		cos.put("currentCode", currentCode);
		cos.put("currentSortCode", siblingsiSortCode);
		cos.put("siblingsCode", siblingsCode);
		cos.put("siblingsiSortCode", currentSortCode);

		try {
			int updateRows = dtService.UpdateRows(keys, codes);
			 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_DATETYPE, LogConst.EXCSEC_MOVE, null, "" + updateRows, cos, currentCode, siblingsCode, null, null, null, null, null, null, null, null);
			if (updateRows == 2) {
				data.setReturncode(200);
				data.setReturndata("移动成功");
			}
		} catch (MetaTableException e) {
			this.sendJson(data);
		}
		this.sendJson(data);
	}

	/**
	 * 根据id获取单位数据
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws MetaTableException
	 * @author chenyf
	 */
	public ModelAndView getById() throws MetaTableException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		String code = req.getParameter("id");
		Map<String, Object> data = new HashMap<String, Object>();
		String proname = "时间类型树";
		String unicode = "";
		int queryCount = 0;
		try {
			List<Map<String, Object>> queryData = dtService.QueryData(null, "code='" + code + "'", "sortcode", false);
			if (queryData.size() > 0) {
				queryCount = queryData.size();
				data = queryData.get(0);
			}
			List<String> codes = new ArrayList<String>();
			codes.add("code");
			codes.add("cname");
			String procode = (String) data.get("procode");
			String unitcode = (String) data.get("unitcode");

			// 获取procodeName
			if (!StringUtil.isEmpty(procode)) {
				DataTable tree = dtService.getTree(codes, "code='" + procode + "'", "sortcode");
				if (tree.getRows().size() > 0) {
					proname = tree.getRows().get(0).getString("cname");
				}
			}

			// 获取单位的名称
			if (!StringUtil.isEmpty(unitcode)) {
				List<Map<String, Object>> queryData2 = MetaService.getMetaService("unit").QueryData(codes, "code='" + unitcode + "'", "sortcode");
				if (queryData2.size() > 0) {
					unicode = (String) queryData2.get(0).get("cname");
				}
			}

		} catch (MetaTableException e) {
			e.printStackTrace();
		}
		// ModelMap modelMap = new ModelMap();
		// modelMap.putAll(data);
		// 查询同义词
		List<Synonyms> list = dtService.getSynonyms(code);
		LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_DATETYPE, LogConst.EXC_QUERY_INFO, null, "" + queryCount, code, code, null, null, null, null, null, null, null, null, null);
		return new ModelAndView("/WEB-INF/jsp/metadata/ttmgr/detail").addObject("proname", proname).addObject(data).addObject("unitcode", unicode).addObject("list", list);
	}

	/**
	 * 获取map对象
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @author chenyf
	 */
	public Map<String, Object> getMapObj(String key, String value) {
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put(key, value);
		return obj;
	}

	/**
	 * 导出当前节点下的子类
	 * 
	 * @throws MetaTableException
	 * @author chenyf
	 */
	public void exportData() throws IOException, MetaTableException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		// 获取查询数据
		String code = req.getParameter("id");
		String procodeName = req.getParameter("procodeName");

		List<SqlWhere> where1 = new ArrayList<SqlWhere>();
		if (StringUtil.isEmpty(code)) {
			where1.add(new SqlWhere("procode", "NULL", 00));
		} else {
			where1.add(new SqlWhere("procode", code, 10));
		}
		List<String> codes = null;
		if (getField().size() > 0) {
			codes = getField();
		}
		List<Map<String, Object>> list = dtService.QueryData(codes, where1, "sortcode");
		int queryCount = dtService.QueryCount(where1);
		if (queryCount == 0) {
			where1.clear();
			if (!StringUtil.isEmpty(code)) {
				where1.add(new SqlWhere("code", code, 10));
			}
			list = dtService.QueryData(codes, where1, "sortcode");
		}
		 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_DATETYPE, LogConst.EXC_EXPORT2, null, "" + list.size(), code, procodeName, null, null, null, null, null, null, null, null, null);
		MetaDataExport.export(req, resp, list, getTitle(), getField(), procodeName, null, Const.DATETYPE_FILE_NAME);
	}

	/**
	 * 导出搜索的数据
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws MetaTableException
	 * @author chenyf
	 */
	public void exportSearchData() throws IOException, MetaTableException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		// 获取查询数据
		String code = req.getParameter("code");
		String cname = req.getParameter("cname");
		String procode = req.getParameter("id");
		String procodeName = req.getParameter("procodeName");

		List<SqlWhere> where1 = new ArrayList<SqlWhere>();
		StringBuffer sb = new StringBuffer();
		if (!StringUtil.isEmpty(code)) {
			sb.append("时间类型代码=").append(code);
			where1.add(new SqlWhere("code", "%" + code + "%", 20));
		}
		if (!StringUtil.isEmpty(cname)) {
			sb.append("时间类型名称=").append(cname);
			where1.add(new SqlWhere("cname", "%" + cname + "%", 20));
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<String> codes = null;
		if (getField().size() > 0) {
			codes = getField();
		}
		if (!StringUtil.isEmpty(procode)) {
			list = dtService.QueryData_InTree(codes, where1, "code", procode, "procode", "sortcode");
		} else if (StringUtil.isEmpty(procode)) {
			list = dtService.QueryData(codes, where1, "sortcode");
		}
		Map<String, String> cod = new HashMap<String, String>();
		cod.put("code", code);
		cod.put("cname", cname);
		cod.put("procode", procode);
		cod.put("procodeName", procodeName);
		 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_DATETYPE, LogConst.EXC_EXPORT3, null, "" + list.size(), codes, code, cname, procode, procodeName, null, null, null, null, null, null);
		MetaDataExport.export(req, resp, list, getTitle(), getField(), procodeName, sb.toString(), Const.DATETYPE_FILE_NAME);
	}

	/**
	 * 导出的文件列
	 * 
	 * @return
	 * @author chenyf
	 */
	private static List<String> getField() {
		List<String> field = new ArrayList<String>();
		field.add("code"); // 代码
		field.add("cname"); // 中文名称
		field.add("cname_en"); // 英文名称
		field.add("ccname"); // 中文全称
		field.add("ccname_en"); // 英文全称
		field.add("ifdata"); // 分类类型
		field.add("unitcode"); // 中文单位
		field.add("dotcount"); // 小数点位数
		field.add("sortcode");// 排序码
		field.add("cexp");// 中文指标解释
		field.add("cexp_en");// 英文指标解释
		field.add("cmemo");// 中文注释
		field.add("cmemo_en");// 英文注释
		field.add("createby");// 生成者
		field.add("createtime");// 生成时间
		field.add("updateby");// 最后修改者
		field.add("updatetime");// 最后修改时间
		return field;
	}

	/**
	 * 导出的文件Title
	 * 
	 * @return
	 * @author chenyf
	 */
	private static List<String> getTitle() {
		List<String> title = new ArrayList<String>();
		title.add("序号");
		title.add("代码");
		title.add("中文名称");
		title.add("英文名称 ");
		title.add("中文全称");
		title.add("英文全称");
		title.add("分类类型");
		title.add("中文单位");
		title.add("小数点位数");
		title.add("排序码");
		title.add("中文解释");
		title.add("英文解释");
		title.add("中文注释");
		title.add("英文注释");
		title.add("生成者");
		title.add("生成时间");
		title.add("最后修改者");
		title.add("最后修改时间");
		return title;
	}

	/**
	 * @param 批量匹配详情
	 * @author chenyf
	 */
	public void matchingDetails() {
		HttpServletRequest req = CurrentContext.getRequest();
		JSONReturnData data = new JSONReturnData("");
		ServletFileUpload uploader = new ServletFileUpload(new DiskFileItemFactory());
		uploader.setHeaderEncoding("utf-8");
		try {
			ArrayList<FileItem> list = (ArrayList<FileItem>) uploader.parseRequest(req);
			if (list.size() > 0) {
				FileItem file = list.get(0);
				XLSTYPE xlstype = XLSTYPE.XLS;
				if (file.getName().endsWith("xlsx")) {
					xlstype = XLSTYPE.XLSX;
				}
				data.setReturncode(200);
				data.setReturndata("数据文件上传成功");

				ExcelBook book1 = new ExcelBook();
				book1.LoadExcel(file.getInputStream(), xlstype);

				// /ExcelWorkBook workbook = ExcelUtil.getExcelWorkBook(in, null, xlstype);
				ExcelSheet sheet = book1.getSheets().get(0);
				if (sheet == null) {
					data.setReturncode(500);
					data.setReturndata("没有发现上传的文件");
					this.sendJson(data);
					return;
				}
				List<ExcelRow> rows = sheet.getRows();
				String id = null;
				int index = 0;
				StringBuffer sb = new StringBuffer();
				// 遍历标识列
				if (rows != null && rows.get(0) != null) {
					ExcelRow firstRow = rows.get(0);
					List<ExcelCell> cells = firstRow.getCells();
					for (int i = 0; i < cells.size(); i++) {
						ExcelCell cell = cells.get(i);
						if (cell != null && !StringUtil.isEmpty(cell.getText())) {
							id = "" + cell.getText();
							index = i;
							break;
						}
					}
				}
				ArrayList<String> sqlList = new ArrayList<String>();
				// 遍历数据
				for (int i = 1; i < rows.size() && id != null; i++) {
					ExcelRow row = rows.get(i);
					if (row == null || row.isNull()) {
						continue;
					}
					ExcelCell cell = row.getCells().get(index);
					if (cell != null && !StringUtil.isEmpty(cell.getText())) {
						String value = cell.getText() + "".replaceAll("\\s*", "");
						if (value.length() > 0) {
							sb.append(value + ";");
						}
					}
					if (i % 1000 == 0) {
						sqlList.add(sb.toString());
						sb = new StringBuffer("");
					}
				}
				if (!StringUtil.isEmpty(sb.toString())) {
					sqlList.add(sb.toString());
				}
				int queryCount = 0;
				for (int i = 0; i < sqlList.size(); i++) {
					String string = sqlList.get(i);
					if (string.length() > 0) {
						string = string.substring(0, string.length() - 1);
					}
					List<SqlWhere> where1 = new ArrayList<SqlWhere>();
					where1.add(new SqlWhere(id, string, 16));
					queryCount += dtService.QueryCount(where1);
				}
				String uuid = StringUtil.getUUID();
				acmr.cache.acmrCache.Factor.getInstance().Add(uuid, sqlList, 30);
				data.setParam1(queryCount);
				data.setParam2(uuid);
				data.setParam3(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			data.setReturncode(500);
			data.setReturndata(e.getMessage());
		}

		try {
			this.sendJson(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param 批量匹配详情
	 *            数据导出
	 * @throws MetaTableException
	 * @author chenyf
	 */
	public void mdExport() throws MetaTableException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		String id = req.getParameter("id");
		String uuid = req.getParameter("uuid");
		ArrayList<String> sqlList = (ArrayList<String>) acmr.cache.acmrCache.Factor.getInstance().get(uuid);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < sqlList.size(); i++) {
			String string = sqlList.get(i);
			if (string.length() > 0) {
				string = string.substring(0, string.length() - 1);
				if (sqlList.size() - 1 != i) {
					sb.append(string + ";");
				}
			}
			List<SqlWhere> where1 = new ArrayList<SqlWhere>();
			where1.add(new SqlWhere(id, string, 16));
			list.addAll(dtService.QueryData(getField(), where1, null));
		}
		 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_DATETYPE, LogConst.EXC_PLPP, null, "" + list.size(), sb.toString(), "" + list.size(), null, null, null, null, null, null, null, null, null);
		MetaDataExport.export(req, resp, list, getTitle(), getField(), null, null, "时间类型批量匹配数据结果", id);

	}

	/**
	 * @param 批量导入
	 * @author chenyf
	 */
	public void importData() {
		JSONReturnData data = new JSONReturnData("");
		ServletFileUpload uploader = new ServletFileUpload(new DiskFileItemFactory());
		uploader.setHeaderEncoding("utf-8");
		ArrayList<FileItem> files;
		try {
			files = (ArrayList<FileItem>) uploader.parseRequest(this.getRequest());
			if (files != null && files.size() > 0) {
				FileItem file = files.get(0);
				String name = file.getName();
				if (file != null) {
					try {
						XLSTYPE xlstype = XLSTYPE.XLS;
						if (name.endsWith("xlsx")) {
							xlstype = XLSTYPE.XLSX;
						}
						ExcelBook book1 = new ExcelBook();
						book1.LoadExcel(file.getInputStream(), xlstype);
						ExcelSheet sheet = book1.getSheets().get(0);
						if (sheet == null) {
							data.setReturncode(500);
							data.setReturndata("没有发现上传的文件");
							this.sendJson(data);
							return;
						}
						int rows = sheet.getRows().size();
						java.util.Date now = new java.util.Date();

						// 必填项
						Map<Integer, String> mkey = new HashMap<Integer, String>();
						// 非必填项
						Map<Integer, String> okey = new HashMap<Integer, String>();
						// 唯一标识列
						Integer index = null;
						// procode列
						Integer procodeIndex = null;
						// 数据量
						int count = 0;
						// 遍历标识列
						if (rows >= 1 && sheet.getRows().get(1) != null) {
							ExcelRow firstRow = sheet.getRows().get(1);
							if (firstRow != null) {
								int cells = firstRow.getCells().size();
								for (int i = 0; i < cells; i++) {
									ExcelCell cell = firstRow.getCells().get(i);
									if (cell != null) {
										String value = cell.getText() + "";
										if (StringUtil.isEmpty(value)) {
											continue;
										}
										if (isMust(value) && !mkey.containsValue(value)) {
											if ("code".equals(value)) {
												index = i;
											}
											mkey.put(i, value);
										} else if (isOther(value) && !okey.containsValue(value)) {
											if ("procode".equals(value)) {
												procodeIndex = i;
											}
											okey.put(i, value);
										}
									}
								}
							}
						}
						boolean flag = true; // 模板是否正确，默认正确
						boolean checkData = true; // 检查数据格式是否正确
						List<Map<String, Object>> sql = new ArrayList<Map<String, Object>>();
						List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
						List<String> code = new ArrayList<String>(); // 所有的code
						List<String> cqCode = new ArrayList<String>(); // 重复的code

						// 如果标识列不为空，则遍历内容
						if (mkey.size() == getMust().size()) {
							// 遍历所有值
							for (int i = 2; i < rows; i++) {
								Map<String, Object> codes = new HashMap<String, Object>();
								String orgCode = ""; // 原始code
								ExcelRow row = sheet.getRows().get(i);
								if (row == null || row.isNull()) {
									continue;
								}
								// 处理必填项列
								for (Iterator<Integer> iterator = mkey.keySet().iterator(); iterator.hasNext();) {
									Integer integer = iterator.next();
									String key = mkey.get(integer);
									ExcelCell cell = row.getCells().get(integer);
									if (cell == null) {
										cell = new ExcelCell();
									}
									String value = dtService.getValue(cell.getText() + "");
									if (StringUtil.isEmpty(value)) {
										cell.setCellstyle(MetaExcelColor.getColor("red"));
										flag = false;
									} else if ("code".equals(key)) {
										orgCode = value;
										String codeValue = value.replaceAll("\\s*", "");
										codes.put(key, codeValue);
										codes.put("sortcode", codeValue); // 排序码
										if (!checkMustValue(key, value)) { // 校验必填项数据格式是否正确
											cell.setCellstyle(MetaExcelColor.getColor("pink"));
											checkData = false;
										}
									} else if (!checkMustValue(key, value)) { // 校验必填项数据格式是否正确
										cell.setCellstyle(MetaExcelColor.getColor("pink"));
										checkData = false;
									} else {
										codes.put(key, value);
									}
									// 添加重复的code
									if (index == integer) {
										if (code.contains(value)) {
											cqCode.add(value);
										} else {
											code.add(value);
										}
									}
								}
								// 处理非必填项列
								for (Iterator<Integer> iterator = okey.keySet().iterator(); iterator.hasNext();) {
									Integer integer = iterator.next();
									String key = okey.get(integer);
									ExcelCell cell = row.getCells().get(integer);
									if (cell == null) {
										cell = new ExcelCell();
									}
									String value = dtService.getValue(cell.getText() + "");
									if (!StringUtil.isEmpty(value)) {
										if (!checkOtherValue(key, value)) { // 校验非必填项数据格式是否正确
											cell.setCellstyle(MetaExcelColor.getColor("pink"));
											checkData = false;
										} else {
											codes.put(key, value);
										}
									}
								}
								// 添加其他字段 开始

								java.sql.Timestamp tt = new java.sql.Timestamp(now.getTime());
								codes.put("updatetime", tt);
								User currentUser = UserService.getCurrentUser();
								if (null != currentUser) {
									codes.put("updateby", currentUser.getUserid());
									codes.put("createby", currentUser.getUserid());
								}
								codes.put("createtime", tt);
								// 添加其他字段 结束
								sql.add(codes);
								Map<String, Object> hm = new HashMap<String, Object>(codes);
								hm.put("orgCode", orgCode);
								nodeList.add(hm);
								count++;
							}

							// 如果插入的数据量大于10000条，则提示用户数量超标
							if (count >= 10000) {
								data.setReturncode(400);
								data.setReturndata("导入的数据不能超过10000行，目前有" + count + "行");
								return;
							}

							MultipleTree multipleTree = new MultipleTree(nodeList);

							List<String> errRow = multipleTree.getErrRow();
							HashMap<String, String> needCheckProcode = multipleTree.getNeedCheckProcode();

							// 必填项没有空数据，且code暂时不重复
							if (flag && checkData && !(cqCode.size() > 0)) {
								// 检查底库中code是否存在
								for (int i = 0; i < code.size(); i++) {
									String cd = code.get(i).replaceAll("\\s*", "");
									// 检查code
									int queryCount = dtService.QueryCount("code='" + cd + "'");
									if (queryCount > 0) {
										cqCode.add(code.get(i));
									}
								}
								if (cqCode.size() > 0) {
									checkImportCode(data, name, book1, sheet, rows, MetaExcelColor.getColor("green"), index, cqCode, null, xlstype);
								} else if (errRow.size() > 0) { // 标记数据有死循环的情况
									checkImportCode(data, name, book1, sheet, rows, MetaExcelColor.getColor("blue"), index, errRow, procodeIndex, xlstype);
								} else if (needCheckProcode.size() > 0) {// 底库校验procode
									List<String> checkProcode = new ArrayList<String>();
									Set<String> keySet = needCheckProcode.keySet();
									for (String key : keySet) {
										// 检查code
										int queryCount = dtService.QueryCount("code='" + needCheckProcode.get(key) + "'");
										if (queryCount == 0) {
											checkProcode.add(key);
										}
									}
									if (checkProcode.size() > 0) {
										checkImportCode(data, name, book1, sheet, rows, MetaExcelColor.getColor("blue"), index, checkProcode, procodeIndex, xlstype);
									} else {
										int insertRows = dtService.InsertRows(sql);
										data.setParam1(insertRows);
										data.setReturncode(200);
										data.setReturndata("数据文件上传成功");
									}
								} else {
									int insertRows = dtService.InsertRows(sql);
									data.setParam1(insertRows);
									data.setReturncode(200);
									data.setReturndata("数据文件上传成功");
								}
							} else if (!checkData) {// 校验数据格式是否正确
								// 写入字节流
//								ByteArrayOutputStream bos = new ByteArrayOutputStream();
//								book1.saveExcel(bos, xlstype);
//								bos.flush();
//								bos.close();
								String uuid = StringUtil.getUUID();
								acmr.cache.acmrCache.Factor.getInstance().Add(uuid, book1, 30); // 文件
								acmr.cache.acmrCache.Factor.getInstance().Add(uuid + "fn", name, 30);// 文件名
								data.setReturncode(300);
								data.setParam1(uuid);
								data.setReturndata("数据格式不合法");
							} else {
								// 处理code是否重复
								checkImportCode(data, name, book1, sheet, rows, MetaExcelColor.getColor("yellow"), index, cqCode, null, xlstype);
							}
						} else {
							data.setReturncode(400);
							Set<String> must = getMust().keySet();
							data.setReturndata("模板格式不正确，第二行数据必须包含：" + must.toString());
						}
					} catch (Exception e) {
						e.printStackTrace();
						data.setReturncode(500);
						data.setReturndata("数据导入失败");
					}
				}
			} else {
				data.setReturncode(500);
				data.setReturndata("没有发现上传的文件");
			}
			this.sendJson(data);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * 检查模板中code是否重复
	 * 
	 * @author chenyf
	 */
	public static void checkImportCode(JSONReturnData data, String name, ExcelBook workbook, ExcelSheet sheet, int rows, ExcelCellStyle bgColor, Integer index, List<String> cqCode, Integer procodeIndex, XLSTYPE xlstype) throws IOException {
		for (int i = 2; i < rows && cqCode.size() > 0; i++) {
			ExcelRow row = sheet.getRows().get(i);
			if (row == null || row.isNull()) {
				continue;
			}
			ExcelCell cell = row.getCells().get(index);
			if (cell == null) {
				cell = new ExcelCell();
			}
			String value = cell.getText() + "";
			if (cqCode.contains(value)) {
				if (procodeIndex != null) {
					ExcelCell cell2 = row.getCells().get(procodeIndex);
					if (cell2 == null) {
						cell2 = new ExcelCell();
					}
					cell2.setCellstyle(bgColor);
				} else {
					cell.setCellstyle(bgColor);
				}
			}
		}
		// 写入字节流
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			workbook.saveExcel(bos, xlstype);
		} catch (ExcelException e) {
			e.printStackTrace();
		}
		bos.flush();
		bos.close();
		String uuid = StringUtil.getUUID();
		acmr.cache.acmrCache.Factor.getInstance().Add(uuid, bos, 30); // 文件
		acmr.cache.acmrCache.Factor.getInstance().Add(uuid + "fn", name, 30);// 文件名
		data.setReturncode(300);
		data.setParam1(uuid);
		data.setReturndata("数据模板有错误");
	}

	/**
	 * @param 批量匹配详情数据导出
	 * @throws MetaTableException
	 * @author chenyf
	 */
//	public static void impErrInfoExport() throws MetaTableException {
//		HttpServletRequest req = CurrentContext.getRequest();
//		HttpServletResponse resp = CurrentContext.getResponse();
//		String uuid = req.getParameter("uuid");
//		Object item = acmr.cache.acmrCache.Factor.getInstance().get(uuid);
//		String filename = (String) acmr.cache.acmrCache.Factor.getInstance().get(uuid + "fn");
//		if (item == null || filename == null) {
//			return;
//		}
//		try {
//			// 定义输出类型
//			resp.reset();
//			resp.setContentType("application/vnd.ms-excel");
//			resp.setHeader("Pragma", "public");
//			resp.setHeader("Cache-Control", "max-age=30");
//			filename = new String(filename.getBytes("gb2312"), "iso8859-1").replace(" ", "");
//			resp.setHeader("Content-disposition", "attachment;filename="+URLEncoder.encode("错误信息.xlsx", "utf-8"));
//			// 生成Excel并响应客户端
//			ServletOutputStream out = resp.getOutputStream();
//			ByteArrayOutputStream bos = (ByteArrayOutputStream) item;
//			resp.setContentLength(bos.size());
//			bos.writeTo(out);
//			out.close();
//			out.flush();
//			bos.close();
//			bos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	/**
	 * @param 批量匹配详情数据导出
	 * @throws MetaTableException
	 * @author chenyf
	 */
	public void impErrInfoExport() throws MetaTableException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		String uuid = req.getParameter("uuid");
		ExcelBook item = (ExcelBook)acmr.cache.acmrCache.Factor.getInstance().get(uuid);
		String filename = (String) acmr.cache.acmrCache.Factor.getInstance().get(uuid + "fn");
		if (item == null || filename == null) {
			return;
		}
		
		try {
			OutputStream out = resp.getOutputStream();
			resp.setContentType("application/octet-stream");
			resp.setHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode("错误信息.xlsx", "utf-8"));
			item.saveExcel(out, XLSTYPE.XLSX);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 判断模板中的字段是否为必填项
	 * 
	 * @param code
	 * @return
	 * @author chenyf
	 */
	public static boolean isMust(String code) {
		Map<String, String> map = getMust();
		String value = map.get(code);
		if (value != null) {
			return true;
		}
		return false;
	}

	/**
	 * 判断模板中的字段是否为非必填项
	 * 
	 * @param code
	 * @return
	 * @author chenyf
	 */
	public static boolean isOther(String code) {
		Map<String, String> map = getOther();
		String value = map.get(code);
		if (value != null) {
			return true;
		}
		return false;
	}

	/**
	 * 必填项
	 * 
	 * @return
	 * @author chenyf
	 */
	public static Map<String, String> getMust() {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("code", "code");// 指标组代码
		hashMap.put("cname", "cname");// 中文名称
		hashMap.put("ifdata", "ifdata"); // 指标分类
		return hashMap;
	}

	/**
	 * 非必填项
	 * 
	 * @return
	 * @author chenyf
	 */
	public static Map<String, String> getOther() {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("procode", "procode");// 父节点code
		hashMap.put("ccname", "ccname");// 中文全称
		hashMap.put("ccname_en", "ccname_en");// 英文全称
		hashMap.put("cexp", "cexp");// 中文解释
		hashMap.put("cexp_en", "cexp_en");// 英文解释
		hashMap.put("cmemo", "cmemo");// 中文备注
		hashMap.put("cmemo_en", "cmemo_en");// 英文备注
		hashMap.put("unitcode", "unitcode");// 单位码
		hashMap.put("dotcount", "dotcount");// 小数点位数
		hashMap.put("cname_en", "cname_en");// 英文名称
		return hashMap;
	}

	/**
	 * 检查必填项值是否合法
	 * 
	 * @return
	 * @author chenyf
	 */
	public static boolean checkMustValue(String code, String value) {
		if ("code".equals(code)) {
			if (isContainChinese(value) || value.length() > 20) {
				return false;
			}
			return true;
		} else if ("cname".equals(code)) {
			return checkLength(value, 50);
		} else if ("ifdata".equals(code)) {
			return checkIfdata(value);
		}
		return false;
	}

	/**
	 * 检查非必填项值是否合法
	 * 
	 * @return
	 * @author chenyf
	 */
	public static boolean checkOtherValue(String code, String value) {
		if ("procode".equals(code)) {
			if (isContainChinese(value) || value.length() > 20) {
				return false;
			}
			return true;
		} else if ("ccname".equals(code)) {
			return checkLength(value, 100);
		} else if ("ccname_en".equals(code)) {
			return checkLength(value, 250);
		} else if ("cexp".equals(code)) {
			return checkLength(value, 4000);
		} else if ("cexp_en".equals(code)) {
			return checkLength(value, 4000);
		} else if ("cmemo".equals(code)) {
			return checkLength(value, 4000);
		} else if ("cmemo_en".equals(code)) {
			return checkLength(value, 4000);
		} else if ("cname_en".equals(code)) {
			return checkLength(value, 50);
		} else if ("procode".equals(code)) {
			return checkLength(value, 500);
		} else if ("sortcode".equals(code)) {
			return checkLength(value, 20);
		} else if ("createby".equals(code)) {
			return checkLength(value, 50);
		} else if ("createtime".equals(code)) {
			return checkLength(value, 7);
		} else if ("updateby".equals(code)) {
			return checkLength(value, 50);
		} else if ("updatetime".equals(code)) {
			return checkLength(value, 7);
		} else if ("dotcount".equals(code)) {
			Pattern p = Pattern.compile("^[0-4]$");
			Matcher m = p.matcher(value);
			if (m.find()) {
				return true;
			}
			return false;
		} else if ("unitcode".equals(code)) {
			if (isContainChinese(value) || value.length() > 20) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * 校验是否包含中文
	 * 
	 * @param str
	 * @return
	 * @author chenyf
	 */
	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 检查数据的长度
	 * 
	 * @param value
	 * @param len
	 * @return
	 * @author chenyf
	 */
	public static boolean checkLength(String value, int len) {
		if (value.length() > len) {
			return false;
		}
		return true;
	}

	/**
	 * 校验是否包含中文
	 * 
	 * @param str
	 * @return
	 * @author chenyf
	 */
	public static boolean checkIfdata(String str) {
		Pattern p = Pattern.compile("^[01]$");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 导出当前节点下的所有节点
	 * 
	 * @throws MetaTableException
	 * @author chenyf
	 */
	public void exportAllData() throws IOException, MetaTableException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		// 获取查询数据
		String codes = req.getParameter("ids");
		// 构造返回对象
		if (StringUtil.isEmpty(codes)) {
			MetaDataExport.export(req, resp, new ArrayList<Map<String, Object>>(), getTitle(), getField(), null, null, Const.DATETYPE_FILE_NAME);
			return;
		}
		String[] ids = codes.split(",");
		// 存放数据
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < ids.length; i++) { // 最多遍历10次，1w条数据
			List<SqlWhere> where1 = new ArrayList<SqlWhere>();
			// 最多1000条数据
			List<Map<String, Object>> dt = dtService.QueryData_InTree(getField(), where1, "code", ids[i], "procode", "sortcode");
			for (int j = 0; j < dt.size(); j++) {
				list.add(dt.get(j));
			}
		}
		 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_DATETYPE, LogConst.EXC_EXPORT1, null, "" + ids.length, ids, "" + list.size(), null, null, null, null, null, null, null, null, null);
		MetaDataExport.export(req, resp, list, getTitle(), getField(), null, null, Const.DATETYPE_FILE_NAME);
	}

	/**
	 * 检查code是否已经存在
	 * 
	 * @author chenyf
	 */
	public void checkProCode() throws IOException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		String code = req.getParameter("code");
		String procode = req.getParameter("procode");
		// 构造返回对象
		JSONReturnData data = new JSONReturnData(501, "code不存在");
		try {
			if (!StringUtil.isEmpty(procode)) {
				String clearCode = procode.replaceAll("\\s*", "");
				// 检查code 是否存在，如果存在，则可以使用
				int queryCount = dtService.QueryCount("code='" + clearCode + "'");
				if (queryCount != 0) {
					List<Map<String, Object>> queryData_TreePath = dtService.QueryData_TreePath(null, "code", clearCode, "procode");
					ArrayList<String> list = new ArrayList<String>();
					if (queryData_TreePath.size() > 0) {
						for (int i = 0; i < queryData_TreePath.size(); i++) {
							Map<String, Object> map = queryData_TreePath.get(i);
							String cd = (String) map.get("code");
							list.add(cd);
						}
						boolean contains = list.contains(code);
						if (contains) {
							data.setReturndata("不能修改为子集");
						} else {
							data.setReturncode(200);
							List<Map<String, Object>> queryData = dtService.QueryData(null, "code='" + clearCode + "'", "sortcode");
							Map<String, Object> map = queryData.get(0);
							String cname = (String) map.get("cname");
							data.setParam1(cname);
							data.setReturndata("可以使用");
						}
					}
				}
			} else {
				data.setReturncode(200);
				data.setParam1("时间类型树");
				data.setReturndata("可以使用");
			}
		} catch (MetaTableException e) {
			this.sendJson(data);
			return;
		}
		this.sendJson(data);
	}

	/**
	 * 检查当前当前节点是否有下级
	 * 
	 * @param req
	 * @param resp
	 * @throws MetaTableException
	 * @throws IOException
	 * @author chenyf
	 */
	public void checkHasParent() throws MetaTableException, IOException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		String code = req.getParameter("code");
		JSONReturnData data = new JSONReturnData(501, "没有下级");
		if (!StringUtil.isEmpty(code)) {
			List<SqlWhere> where1 = new ArrayList<SqlWhere>();
			where1.add(new SqlWhere("procode", code, 10));
			// 取出状态为不删除的数据
			int queryCount = dtService.QueryCount(where1);
			if (queryCount > 0) {
				data.setReturncode(200);
				data.setReturndata("有下级");
			}
		}
		this.sendJson(data);
	}

	/**
	 * 批量匹配模板下载
	 * 
	 * @param req
	 * @param resp
	 * @author chenyf
	 */
	public void templateDownload() {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		String realPath = req.getServletContext().getRealPath("/template");
		String file = "批量匹配模板." + Const.DOWNLOAD_TYPE;
		try {
			// 定义输出类型
			resp.reset();
			resp.setContentType("application/vnd.ms-excel");
			resp.setHeader("Pragma", "public");
			resp.setHeader("Cache-Control", "max-age=30");
			String filename = new String(file.getBytes("gb2312"), "iso8859-1").replace(" ", "");
			resp.setHeader("Content-disposition", "attachment;filename=" + filename);
			// 生成Excel并响应客户端
			ServletOutputStream out = resp.getOutputStream();
			InputStream inputStream = new FileInputStream(realPath + "\\" + file);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			int b1 = 0;
			while ((b1 = inputStream.read()) != -1) {
				outStream.write(b1);
			}
			inputStream.close();
			resp.setContentLength(outStream.size());
			outStream.writeTo(out);
			out.flush();
			out.close();
			outStream.flush();
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 批量导入模板下载
	 * 
	 * @param req
	 * @param resp
	 * @author chenyf
	 */
	public void templateDownload2() {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		String realPath = req.getServletContext().getRealPath("/template");
		String file = "批量导入模板." + Const.DOWNLOAD_TYPE;
		try {
			// 定义输出类型
			resp.reset();
			resp.setContentType("application/vnd.ms-excel");
			resp.setHeader("Pragma", "public");
			resp.setHeader("Cache-Control", "max-age=30");
			String filename = new String(file.getBytes("gb2312"), "iso8859-1").replace(" ", "");
			resp.setHeader("Content-disposition", "attachment;filename=" + filename);
			// 生成Excel并响应客户端
			ServletOutputStream out = resp.getOutputStream();
			InputStream inputStream = new FileInputStream(realPath + "\\" + file);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			int b1 = 0;
			while ((b1 = inputStream.read()) != -1) {
				outStream.write(b1);
			}
			inputStream.close();
			resp.setContentLength(outStream.size());
			outStream.writeTo(out);
			out.flush();
			out.close();
			outStream.flush();
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
