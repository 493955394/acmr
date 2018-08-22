package com.acmr.web.jsp.metadata;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

import acmr.cubeinput.MetaTableException;
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

import com.acmr.dao.metadata.IMetaDao;
import com.acmr.dao.metadata.MetaDao;
import com.acmr.helper.constants.Const;
import com.acmr.helper.util.SecureRandomUtil;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.AjaxPageBean;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.pub.PageBean;
import com.acmr.model.pub.TreeNode;
import com.acmr.model.search.sItem;
import com.acmr.model.search.searchList;
import com.acmr.model.security.User;
import com.acmr.service.LogService;
import com.acmr.service.metadata.DataDetails;
import com.acmr.service.metadata.DataFormat;
import com.acmr.service.metadata.MetaDataExport;
import com.acmr.service.metadata.MetaExcelColor;
import com.acmr.service.metadata.MetaService;
import com.acmr.service.metadata.MetaServiceManager;
import com.acmr.service.metadata.MultipleTree;
import com.acmr.service.metadata.Synonyms;
import com.acmr.service.metadata.Template;
import com.acmr.service.search.SearchService;
import com.acmr.service.security.UserService;
import com.acmr.web.jsp.log.LogConst;
import com.alibaba.fastjson.JSON;

/**
 * 模块：元数据管理 -> 指标管理 action层
 * 
 * @author chenyf
 */
public class Zbmgr extends BaseAction {

	@Override
	public boolean servletLoad() {

		return true;
	}
 
	/**
	 * 获取service层对象（cube）
	 * 
	 * @author chenyf
	 */
	private MetaServiceManager metaService = MetaService.getMetaService("zb");

	// 同义词表 metacode
	public static final String metacode = "tyc";
	// 同义词表 dbcode
	public static final String dbcode = Const.DB_CODE;

	/**
	 * 主界面跳转方法
	 * 
	 * @author chenyf
	 */
	public ModelAndView main() {
		this.getResponse().setContentType("application/json; charset=utf-8");
		PageBean<Map<String, Object>> page = new PageBean<Map<String, Object>>();
		StringBuffer sb = new StringBuffer();
		sb.append(this.getRequest().getRequestURI());
		sb.append("?m=query");
		String bottom = null;
		try {
			List<SqlWhere> where1 = new ArrayList<SqlWhere>();
			where1.add(new SqlWhere("procode", "NULL", 00));
			// 查询第一页数据
			List<Map<String, Object>> list = metaService.QueryData_ByPage(null, where1, "sortcode", page.getPageNum() - 1, page.getPageSize());
			int queryCount = metaService.QueryCount(where1);
			page.setData(list);
			page.setTotalRecorder(queryCount);
			page.setUrl(sb.toString());

			// 如果下一页没有数据，就说明是最后一页
			List<Map<String, Object>> queryData_ByPage = metaService.QueryData_ByPage(null, where1, "sortcode", page.getPageNum(), page.getPageSize());
			if (queryData_ByPage.size() >= 1) {
				Map<String, Object> map = queryData_ByPage.get(0);
				bottom = (String) map.get("code");
			}

		} catch (MetaTableException e) {
			e.printStackTrace();
		}
		return new ModelAndView("/WEB-INF/jsp/metadata/zbmgr/index").addObject("page", page).addObject("ismove", 1).addObject("bottom", bottom);
	}

	/**
	 * 
	 * 获取树形结构
	 * 
	 * @throws MetaException
	 * @throws IOException
	 * @author chenyf
	 */
	public void findZbTree() throws MetaTableException, IOException {
		List<SqlWhere> where1 = new ArrayList<SqlWhere>();
		String code = this.getRequest().getParameter("id");
		if (StringUtil.isEmpty(code)) {
			where1.add(new SqlWhere("procode", "NULL", 00));
		} else {
			where1.add(new SqlWhere("procode", code, 10));
		}
		DataTable queryData = metaService.getTree(null, where1, "sortcode");
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
		this.sendJson(list);
	}

	/**
	 * 查找
	 * 
	 * @throws IOException
	 * @author chenyf
	 */
	public ModelAndView find() throws IOException {
		HttpServletRequest req = this.getRequest();
		// 获取查询数据
		String code = StringUtil.toLowerString(req.getParameter("code"));
		String cname = StringUtil.toLowerString(req.getParameter("cname"));
		String procode = req.getParameter("id");
		String treeList = "";

		// 判断是否pjax 请求
		String pjax = req.getHeader("X-PJAX");

		PageBean<Map<String, Object>> page = new PageBean<Map<String, Object>>();
		List<SqlWhere> where1 = new ArrayList<SqlWhere>();

		String url = MetaService.getPageUrl(req);
		String searchPara = MetaService.getMetaPara(req);

		if (!StringUtil.isEmpty(procode)) {
			try {
				treeList = metaService.getTreePath(procode);
			} catch (MetaTableException e) {
				e.printStackTrace();
			}
		}
		if (!StringUtil.isEmpty(code)) {
			where1.add(new SqlWhere("lower(code)", "%" + SqlWhere.LikeEscapeEncode(code.toLowerCase()) + "%", 20));
		}
		if (!StringUtil.isEmpty(cname)) {
			where1.add(new SqlWhere("lower(cname)", "%" + SqlWhere.LikeEscapeEncode(cname.toLowerCase()) + "%", 20));
		}
		try {
			List<Map<String, Object>> list = null;
			int count = 0;

			if (!StringUtil.isEmpty(procode)) {
				list = metaService.QueryData_InTree_ByPage(null, where1, "code", procode, "procode", "sortcode", page.getPageNum() - 1, page.getPageSize());
				count = metaService.QueryCount_InTree(where1, "code", procode, "procode");
			} else if (StringUtil.isEmpty(procode)) {
				// if (StringUtil.isEmpty(code) && StringUtil.isEmpty(cname)) {
				// where1.clear();
				// where1.add(new CSqlWhere("procode", "NULL", 00));
				// }
				list = metaService.QueryData_ByPage(null, where1, "sortcode", page.getPageNum() - 1, page.getPageSize());
				count = metaService.QueryCount(where1);
			}
			page.setData(list);
			page.setTotalRecorder(count);
			page.setUrl(url.toString().replaceAll("%", "%25"));
			// 发送返回数据
			// this.sendJson(resp, new JSONReturnData(page));
			Map<String, String> codes = new HashMap<String, String>();
			codes.put("code", code);
			codes.put("cname", cname);
			codes.put("procode", procode);
			 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_ZBMGR, LogConst.EXCSEC_FIND, null, "" + count, codes, code, cname, procode, null, null, null, null, null, null, null);
		} catch (MetaTableException e) {
			e.printStackTrace();
		}

		if (StringUtil.isEmpty(pjax)) {
			return new ModelAndView("/WEB-INF/jsp/metadata/zbmgr/index").addObject("page", page).addObject("code", code.replaceAll("%25", "%")).addObject("cname", cname.replaceAll("%25", "%")).addObject("procode", procode).addObject("initTreePara", treeList).addObject("searchPara", searchPara).addObject("ismove", 0);
		} else {
			return new ModelAndView("/WEB-INF/jsp/metadata/zbmgr/tableList").addObject("page", page).addObject("ismove", 0);
		}
	}

	/***
	 * 获取子节点内容
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @author chenyf
	 */
	public ModelAndView query() throws IOException {
		HttpServletRequest req = CurrentContext.getRequest();
		// 获取查询数据
		String code = req.getParameter("id");
		// 判断是否pjax 请求
		String pjax = req.getHeader("X-PJAX");

		PageBean<Map<String, Object>> page = new PageBean<Map<String, Object>>();
		String top = null;
		String bottom = null;
		String pageUrl = MetaService.getPageUrl(req);
		String treeList = "";

		try {
			List<SqlWhere> where1 = new ArrayList<SqlWhere>();
			if (!StringUtil.isEmpty(code)) {
				treeList = metaService.getTreePath(code);
				where1.add(new SqlWhere("procode", code, 10));
			} else {
				where1.add(new SqlWhere("procode", "NULL", 00));
			}
			List<Map<String, Object>> list = metaService.QueryData_ByPage(null, where1, "sortcode", page.getPageNum() - 1, page.getPageSize());
			int queryCount = metaService.QueryCount(where1);
			// 说明不是第一页，就要取上一页的最后一条
			if (page.getPageNum() != 1) {
				List<Map<String, Object>> queryData_ByPage = metaService.QueryData_ByPage(null, where1, "sortcode", page.getPageNum() - 2, page.getPageSize());
				if (queryData_ByPage.size() >= 1) {
					Map<String, Object> map = queryData_ByPage.get(queryData_ByPage.size() - 1);
					top = (String) map.get("code");
				}
			}

			// 如果下一页没有数据，就说明是最后一页
			List<Map<String, Object>> queryData_ByPage = metaService.QueryData_ByPage(null, where1, "sortcode", page.getPageNum(), page.getPageSize());
			if (queryData_ByPage.size() >= 1) {
				Map<String, Object> map = queryData_ByPage.get(0);
				bottom = (String) map.get("code");
			}

			// end
			if (queryCount == 0 && !StringUtil.isEmpty(code)) {
				where1.clear();
				where1.add(new SqlWhere("code", code, 10));
				list = metaService.QueryData_ByPage(null, where1, "sortcode", page.getPageNum() - 1, page.getPageSize());
				queryCount = metaService.QueryCount(where1);
			}
			page.setData(list);
			page.setTotalRecorder(queryCount);
			page.setUrl(pageUrl);
			 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_ZBMGR, LogConst.EXC_QUERY, null, "" + queryCount, code, code, null, null, null, null, null, null, null, null, null);
			// 发送返回数据
		} catch (MetaTableException e) {
			e.printStackTrace();
		}

		if (StringUtil.isEmpty(pjax)) {
			return new ModelAndView("/WEB-INF/jsp/metadata/zbmgr/index").addObject("page", page).addObject("initTreePara", treeList).addObject("top", top).addObject("bottom", bottom).addObject("ismove", 1);
		} else {
			return new ModelAndView("/WEB-INF/jsp/metadata/zbmgr/tableList").addObject("page", page).addObject("top", top).addObject("bottom", bottom).addObject("ismove", 1);
		}
	}

	/**
	 * 跳转到新增页面
	 * 
	 * @param req
	 * @param resp
	 * @author chenyf
	 */
	public ModelAndView toAdd() {
		HttpServletRequest req = CurrentContext.getRequest();
		String procode = req.getParameter("procodeId");
		String procodeName = req.getParameter("procodeName");
		try {
			if (!StringUtil.isEmpty(procode)) {
				procode = URLDecoder.decode(procode, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ModelAndView("/WEB-INF/jsp/metadata/zbmgr/add").addObject("procode", procode).addObject("procodeName", procodeName).addObject("israndom", Const.ZB_RANDOM_NUMBER);
	}

	public void getCode() throws Exception {
		String code = "";
		while (true) {
			code = SecureRandomUtil.getSecureRandomNum(9);
			int queryCount = metaService.QueryCount("code='" + code + "'");
			if (queryCount == 0) {
				break;
			}
		}
		JSONReturnData data = new JSONReturnData(code);
		this.sendJson(data);
	}

	/**
	 * 保存数据
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 * 
	 * @author chenyf
	 */
	public void toSave() throws IOException {
		HttpServletRequest req = CurrentContext.getRequest();
		String id = req.getParameter("id"); // id
		String cname = req.getParameter("cname"); // 中文名称
		String ccname = req.getParameter("ccname");// 中文全称
		String cname_en = req.getParameter("cname_en"); // 英文名称
		String ccname_en = req.getParameter("ccname_en"); // 英文全称
		String cexp = req.getParameter("cexp"); // 中文解释
		String cexp_en = req.getParameter("cexp_en"); // 英文解释
		String cmemo = req.getParameter("cmemo"); // 中文备注
		String cmemo_en = req.getParameter("cmemo_en"); // 英文备注
		String code = req.getParameter("code"); // 指标代码
		String unitcode = req.getParameter("unitcode"); // 中文单位
		String dotcount = req.getParameter("dotcount"); // 小数点位数
		String ifdata = req.getParameter("ifdata"); // 指标分类
		String classprocodes = req.getParameter("classprocodes"); // 优先分组代码
		String nbscode = req.getParameter("nbscode"); // NBS代码
		String procode = req.getParameter("procode"); // 父节点code
		String zbsort = req.getParameter("zbsort"); // 时间类型
		String synonym = req.getParameter("synonym"); // 同义词

		JSONReturnData data = new JSONReturnData(500, "操作失败");

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
		codes.put("nbscode", nbscode);
		codes.put("zbsort", zbsort);
		java.util.Date now = new java.util.Date();
		java.sql.Timestamp tt = new java.sql.Timestamp(now.getTime());
		codes.put("updatetime", tt);
		codes.put("ifdata", ifdata);

		User currentUser = UserService.getCurrentUser();
		if (null != currentUser) {
			codes.put("updateby", currentUser.getUserid());
		}
		if (StringUtil.isEmpty(procode)) {
			codes.put("procode", "");
		} else {
			codes.put("procode", getCode(procode));
		}

		// 添加时的数据
		if (StringUtil.isEmpty(id)) {
			code = getCode(code);
			codes.put("code", code);
			codes.put("sortcode", code);
			codes.put("createtime", tt);
			codes.put("ifclose", "0");
			if (null != currentUser) {
				codes.put("createby", currentUser.getUserid());
			}
		}

		if (!StringUtil.isEmpty(classprocodes)) {
			codes.put("classprocodes", classprocodes.replace(',', '|'));
		} else {
			codes.put("classprocodes", "");
		}

		if (StringUtil.isEmpty(id)) {
			try {
				int insertRow = metaService.InsertRow(codes);
				/**
				 * 1、 req 2、 栏目 3、 类型 （增删改查） 4、 dbcCode 数据库 5、 条数 6、备注 (codes) 7、其他1 (code) ....
				 */
				 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_ZBMGR, LogConst.EXCSEC_INSERT, null, "" + insertRow, codes, code, procode, null, null, null, null, null, null, null, null);
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
				int updateRow = metaService.UpdateRow(keys, codes);
				 LogService.logOperation(req, UserService.getCurrentUser().getUserid(),LogConst.APPID,LogConst.COLUMN_ZBMGR, LogConst.EXCSEC_UPDATE, null, "" + updateRow, codes, code, procode, null, null, null, null, null, null, null, null);
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
			list.add(new Synonyms("zb", "1", cname, code, Zbmgr.dbcode));
		}
		if (!StringUtil.isEmpty(ccname)) {
			list.add(new Synonyms("zb", "2", ccname, code, Zbmgr.dbcode));
		}

		if (!StringUtil.isEmpty(synonym)) {
			String[] split = synonym.split("__");
			for (int i = 0; i < split.length; i++) {
				if (StringUtil.isEmpty(split[i])) {
					continue;
				}
				list.add(new Synonyms("zb", "3", split[i], code, Zbmgr.dbcode));
			}
		}

		// 元数据操作成功 添加同义词
		if (200 == data.getReturncode()) {
			metaService.addSynonyms(list, code, "zb");
		}

		this.sendJson(data);
	}

	/**
	 * 校验同义词名称
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	public void querySysnName() throws IOException {
		HttpServletRequest req = CurrentContext.getRequest();
		String synonym = req.getParameter("synonym");
		String code = req.getParameter("code"); // 有code 说明是编辑
		JSONReturnData data = new JSONReturnData(200, "可以使用");
		StringBuffer sb = new StringBuffer();
		if (!StringUtil.isEmpty(synonym)) {
			String[] split = synonym.split("__");
			for (int i = 0; i < split.length; i++) {
				if (StringUtil.isEmpty(split[i])) {
					continue;
				}
				int count = metaService.querySysnName(split[i], code);
				if (count > 0) {
					sb.append(split[i] + ",");
				}
			}
			if (sb.toString().length() > 0) {
				data.setReturncode(500);
				data.setReturndata("同义词：" + sb.toString() + "在数据中已存在");
			} else {
				data.setReturndata("可以使用");
			}
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
	public ModelAndView toEdit() {
		HttpServletRequest req = CurrentContext.getRequest();
		String code = req.getParameter("id");
		Map<String, Object> data = new HashMap<String, Object>();
		String procodeName = "指标树";
		String unicode = "";// 单位名称
		try {
			List<Map<String, Object>> queryData = metaService.QueryData(null, "code='" + code + "'", "sortcode", false);
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
				DataTable tree = metaService.getTree(codes, "code='" + procode + "'", "sortcode");
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
		// 查询同义词
		List<Synonyms> list = metaService.getSynonyms(code);
		return new ModelAndView("/WEB-INF/jsp/metadata/zbmgr/edit").addObject(data).addObject("procodeName", procodeName).addObject("unicode", unicode).addObject("list", list).addObject("tyc", metaService.getSynonString(list));
	}

	/**
	 * 查看详情
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @author chenyf
	 */
	public ModelAndView getDataById() {
		HttpServletRequest req = CurrentContext.getRequest();
		String code = req.getParameter("id");
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> classProcodes = null;
		String procodeName = "指标树";
		String unicode = "";// 单位名称
		int queryCount = 0;
		try {
			List<Map<String, Object>> queryData = metaService.QueryData(null, "code='" + code + "'", "sortcode", false);
			if (queryData.size() > 0) {
				queryCount = queryData.size();
				data = queryData.get(0);
			}
			List<String> codes = new ArrayList<String>();
			codes.add("code");
			codes.add("cname");
			String procode = (String) data.get("procode");
			String classprocodes = (String) data.get("classprocodes");
			String unitcode = (String) data.get("unitcode");

			// 获取procodeName
			if (!StringUtil.isEmpty(procode)) {
				DataTable tree = metaService.getTree(codes, "code='" + procode + "'", "sortcode");
				if (tree.getRows().size() > 0) {
					procodeName = tree.getRows().get(0).getString("cname");
				}
			}

			// 获取分类的Name
			if (!StringUtil.isEmpty(classprocodes)) {
				String[] cprocodes = classprocodes.split("\\|");
				StringBuffer sb = new StringBuffer();
				sb.append("code in (");
				for (int i = 0; i < cprocodes.length; i++) {
					sb.append("'").append(cprocodes[i]).append("'");
					if (i < cprocodes.length - 1) {
						sb.append(",");
					}
				}
				sb.append(")");
				classProcodes = MetaService.getMetaService("fl").QueryData(codes, sb.toString(), "sortcode");
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

		// 查询同义词
		List<Synonyms> list = metaService.getSynonyms(code);
		 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_ZBMGR, LogConst.EXC_QUERY_INFO, null, "" + queryCount, code, code, null, null, null, null, null, null, null, null, null);
		return new ModelAndView("/WEB-INF/jsp/metadata/zbmgr/detail").addObject(data).addObject("procodeName", procodeName).addObject("classProcodes", classProcodes).addObject("unitcode", unicode).addObject("list", list);
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

		// logger.info("删除指标开始");
		String id = req.getParameter("delId");
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
			int queryCount = metaService.QueryCount(where1);
			if (queryCount > 0) {
				data.setReturndata("该指标正在被其他指标使用");
				this.sendJson(data);
				return;
			}
			MetaServiceManager metaService2 = MetaService.getMetaService("data");
			int queryCount2 = metaService2.QueryCount("code = '" + id + "'");
			if (queryCount2 > 0) {
				data.setReturndata("该指标在底库中存在数据");
				this.sendJson(data);
				return;
			}
			// MetaDataDaoImpl metaDataDao = new MetaDataDaoImpl();
			// boolean queryTemplate = metaDataDao.queryTemplate("zb_" + id);
			// if (queryTemplate) {
			// data.setReturndata("该指标已经被模板使用");
			// this.sendJson(resp, data);
			// return;
			// }
			// boolean queryTask = metaDataDao.queryTask("zb_" + id);
			// if (queryTask) {
			// data.setReturndata("该指标正在任务表中使用");
			// this.sendJson(resp, data);
			// return;
			// }
			// boolean queryTmpTable = metaDataDao.queryTmpTable(id, "code");
			// if (queryTmpTable) {
			// data.setReturndata("该指标正在临时表中使用");
			// this.sendJson(data);
			// return;
			// }
			Map<String, Object> keys = new HashMap<String, Object>();
			keys.put("code", id);
			result = metaService.deleteRow(keys);
			if (result != 0) {
				data.setReturncode(200);
				data.setReturndata("删除成功");
			}
		} catch (MetaTableException e) {
			this.sendJson(data);
			// logger.info("删除指标失败");
			return;
		}
		this.sendJson(data);
		// logger.info("删除指标结束");
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
	public void toDeleteAll() throws IOException {
		HttpServletRequest req = CurrentContext.getRequest();

		// logger.info("删除指标开始");
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
				int queryCount = metaService.QueryCount(where1);
				if (queryCount > 0) {
					data.setReturndata("第" + (i + 1) + "条数据被其他指标使用");
					this.sendJson(data);
					return;
				}
				MetaServiceManager metaService2 = MetaService.getMetaService("data");
				int queryCount2 = metaService2.QueryCount("code = '" + ids[i] + "'");
				if (queryCount2 > 0) {
					data.setReturndata("第" + (i + 1) + "条数据在底库中存在数据");
					this.sendJson(data);
					return;
				}
				// MetaDataDaoImpl metaDataDao = new MetaDataDaoImpl();
				// boolean queryTemplate = metaDataDao.queryTemplate("zb_" + ids[i]);
				// if (queryTemplate) {
				// data.setReturndata("第" + (i + 1) + "条数据已经被模板使用");
				// this.sendJson(resp, data);
				// return;
				// }
				// boolean queryTask = metaDataDao.queryTask("zb_" + ids[i]);
				// if (queryTask) {
				// data.setReturndata("第" + (i + 1) + "条数据正在任务表中使用");
				// this.sendJson(resp, data);
				// return;
				// }
				// boolean queryTmpTable = metaDataDao.queryTmpTable(ids[i], "code");
				// if (queryTmpTable) {
				// data.setReturndata("第" + (i + 1) + "条数据正在临时表中使用");
				// this.sendJson(resp, data);
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
			int updateRows = metaService.deleteRows(keys);
			if (updateRows != 0) {
				data.setReturncode(200);
				data.setReturndata("删除成功！");
				this.sendJson(data);
			}
		} catch (MetaTableException e) {
			// logger.info("删除指标失败");
			MetaTableException metaException = new MetaTableException();
			int row = metaException.getRow();
			data.setReturndata("第" + row + "行错误！");
			this.sendJson(data);
		}
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

		// logger.info("移动开始");
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
			List<Map<String, Object>> queryData = metaService.QueryData(code, "code='" + currentCode + "'", null);
			if (queryData.size() > 0) {
				currentSortCode = (String) queryData.get(0).get("sortcode");
			}
			List<Map<String, Object>> queryData1 = metaService.QueryData(code, "code='" + siblingsCode + "'", null);
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
			int updateRows = metaService.UpdateRows(keys, codes);
			 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_ZBMGR, LogConst.EXCSEC_MOVE, null, "" + updateRows, cos, currentCode, siblingsCode, null, null, null, null, null, null, null, null);
			if (updateRows == 2) {
				data.setReturncode(200);
				data.setReturndata("移动成功");
				this.sendJson(data);
			}
		} catch (MetaTableException e) {
			this.sendJson(data);
		}
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
	 * 检查code是否已经存在
	 * 
	 * @author chenyf
	 */
	public void checkCode() throws IOException {
		HttpServletRequest req = CurrentContext.getRequest();

		String code = req.getParameter("code");
		String procode = req.getParameter("procode");
		String field = req.getParameter("field");
		// 构造返回对象
		JSONReturnData data = new JSONReturnData(501, "code已存在");
		try {
			// 检查其它唯一码
			if (!StringUtil.isEmpty(field)) {
				int queryCount = metaService.QueryCount(field + "='" + code + "'");
				if (queryCount == 0) {
					data.setReturncode(200);
					data.setReturndata("可以使用");
				}
			} else if (!StringUtil.isEmpty(code)) {
				String clearCode = getCode(code);
				boolean flag = true;
				if (!StringUtil.isEmpty(procode)) {
					String procods = getCode(procode);
					int codelen = metaService.getCodelen();
					if (codelen > 0) {
						try {
							if (!procods.substring(0, codelen).equals(clearCode.substring(0, codelen))) {
								flag = false;
								data.setReturndata("代码和父级代码前" + codelen + "位必须相同");
							}
						} catch (Exception e) {
							flag = false;
							data.setReturndata("代码和父级代码前" + codelen + "位必须相同");
						}
					}
				}
				if (flag) {
					// 检查code
					int queryCount = metaService.QueryCount("code='" + clearCode + "'");
					if (queryCount == 0) {
						data.setReturncode(200);
						data.setReturndata("可以使用");
					}
				}

			}
		} catch (MetaTableException e) {
			this.sendJson(data);
			return;
		}
		this.sendJson(data);
	}

	/**
	 * 检查sql是否正确
	 * 
	 * @author chenyf
	 */
	public void checkSql() throws IOException {
		HttpServletRequest req = CurrentContext.getRequest();

		String sql = req.getParameter("searchSQL");
		if (!StringUtil.isEmpty(sql)) {
			sql = sql.trim();
			sql = sql.replaceAll(" +", " ");
		}

		// 指标分类
		if (sql.contains("ifdata")) {
			if (sql.contains("指标")) {
				sql = sql.replaceAll("指标", "1");
			} else if (sql.contains("分类")) {
				sql = sql.replaceAll("分类", "0");
			}
		}

		// 起于止于
		if (sql.contains("createtime") && sql.contains("between") && sql.contains("and")) {
			String[] str = sql.trim().split(" ");
			int index1 = 0;
			int index2 = 0;
			for (int i = 0; i < str.length; i++) {
				if ("between".equals(str[i])) {
					index1 = i;
				}
				if ("and".equals(str[i])) {
					index2 = i;
				}
			}
			if (!sql.contains("to_date")) {
				sql = "createtime between " + "to_date(" + str[index1 + 1] + ",'yyyy-MM-dd')" + " and " + "to_date(" + str[index2 + 1] + ",'yyyy-MM-dd')";
			}
		} else if (sql.contains("createtime") && sql.contains("between")) {
			// 起于
			String[] str = sql.trim().split(" ");
			int index1 = 0;
			for (int i = 0; i < str.length; i++) {
				if ("between".equals(str[i])) {
					index1 = i;
				}
			}
			if (!sql.contains("to_date")) {
				sql = "createtime >= " + "to_date(" + str[index1 + 1] + ",'yyyy-MM-dd')";
			}
		} else if (sql.contains("createtime") && sql.contains("and")) {
			// 止于
			String[] str = sql.trim().split(" ");
			int index2 = 0;
			for (int i = 0; i < str.length; i++) {
				if ("and".equals(str[i])) {
					index2 = i;
				}
			}
			if (!sql.contains("to_date")) {
				sql = "createtime <=" + "to_date(" + str[index2 + 1] + ",'yyyy-MM-dd')";
			}
		}
		// 构造返回对象
		JSONReturnData data = new JSONReturnData(501, "sql语句错误");
		try {
			metaService.QueryCount(sql);
		} catch (MetaTableException e) {
			this.sendJson(data);
			return;
		}
		data.setReturncode(200);
		data.setReturndata("sql正确");
		this.sendJson(data);
	}

	/**
	 * 导出当前节点下的子类
	 * 
	 * @throws MetaException
	 * @author chenyf
	 */
	public void exportData() throws IOException, MetaTableException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();

		// 获取查询数据
		String code = req.getParameter("id");
		String procodeName = req.getParameter("procodeName");
		List<SqlWhere> where1 = new ArrayList<SqlWhere>();
		if (!StringUtil.isEmpty(code)) {
			where1.add(new SqlWhere("procode", code, 10));
		} else {
			where1.add(new SqlWhere("procode", "NULL", 00));
		}
		List<String> codes = null;
		if (getField().size() > 0) {
			codes = getField();
		}
		List<Map<String, Object>> list = metaService.QueryData(codes, where1, "sortcode");
		int queryCount = metaService.QueryCount(where1);
		if (queryCount == 0) {
			where1.clear();
			if (!StringUtil.isEmpty(code)) {
				where1.add(new SqlWhere("code", code, 10));
			}
			list = metaService.QueryData(codes, where1, "sortcode");
		}
		 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_ZBMGR, LogConst.EXC_EXPORT2, null, "" + list.size(), code, procodeName, null, null, null, null, null, null, null, null, null);
		MetaDataExport.export(req, resp, list, getTitle(), getField(), procodeName, null, Const.ZB_FILE_NAME);
	}

	/**
	 * 导出搜索的数据
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws MetaException
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
			sb.append("指标代码=").append(code);
			where1.add(new SqlWhere("code", "%" + code + "%", 20));
		}
		if (!StringUtil.isEmpty(cname)) {
			sb.append("指标名称=").append(cname);
			where1.add(new SqlWhere("cname", "%" + cname + "%", 20));
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<String> codes = null;
		if (getField().size() > 0) {
			codes = getField();
		}
		if (!StringUtil.isEmpty(procode)) {
			list = metaService.QueryData_InTree(codes, where1, "code", procode, "procode", "sortcode");
		} else if (StringUtil.isEmpty(procode)) {
			list = metaService.QueryData(codes, where1, "sortcode");
		}

		Map<String, String> cod = new HashMap<String, String>();
		cod.put("code", code);
		cod.put("cname", cname);
		cod.put("procode", procode);
		cod.put("procodeName", procodeName);
		 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_ZBMGR, LogConst.EXC_EXPORT3, null, "" + list.size(), codes, code, cname, procode, procodeName, null, null, null, null, null, null);
		MetaDataExport.export(req, resp, list, getTitle(), getField(), procodeName, sb.toString(), Const.ZB_FILE_NAME);
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
	 * 格式化日期
	 * 
	 * @param Date
	 *            日期
	 * @author chenyf
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return dateformat.format(date);
	}

	/**
	 * 导出高级查询的数据
	 * 
	 * @throws MetaException
	 * @author chenyf
	 */
	public void exportAdvQueryData() throws IOException, MetaTableException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();

		// 获取查询数据
		String condition = req.getParameter("searchSQL");

		if (!StringUtil.isEmpty(condition)) {
			// 解码
			condition = URLDecoder.decode(condition, "utf-8").replaceAll("%25", "%");
			condition = condition.replaceAll("%20", " ");
			condition = condition.replaceAll("%25", "%");
			condition = condition.trim();
			condition = condition.replaceAll(" +", " ");
		}

		String procodeName = req.getParameter("procodeName");
		List<String> codes = null;
		if (getField().size() > 0) {
			codes = getField();
		}
		List<Map<String, Object>> list = metaService.QueryData(codes, condition, "sortcode");
		 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_ZBMGR, LogConst.EXC_EXPORT4, null, "" + list.size(), condition, null, null, null, null, null, null, null, null, null, null);
		MetaDataExport.export(req, resp, list, getTitle(), getField(), procodeName, null, Const.ZB_FILE_NAME);
	}

	/**
	 * 高级查找分页查找
	 * 
	 * @param req
	 * @param resp
	 * @throws MetaException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @author chenyf
	 */
	public ModelAndView advQuery() throws MetaTableException, FileNotFoundException, IOException {
		HttpServletRequest req = CurrentContext.getRequest();

		String condition = MetaService.getAdvParameter(req, "searchSQL");
		// 判断是否pjax 请求
		String pjax = req.getHeader("X-PJAX");

		if (!StringUtil.isEmpty(condition)) {
			condition = condition.trim();
			// condition = condition.replaceAll(" +", " ");
		}

		// 指标分类
		if (condition.contains("ifdata")) {
			if (condition.contains("指标")) {
				condition = condition.replaceAll("指标", "1");
			} else if (condition.contains("分类")) {
				condition = condition.replaceAll("分类", "0");
			}
		}

		// 起于止于
		if (condition.contains("createtime") && condition.contains("between") && condition.contains("and")) {
			String[] str = condition.split(" ");
			int index1 = 0;
			int index2 = 0;
			for (int i = 0; i < str.length; i++) {
				if ("between".equals(str[i])) {
					index1 = i;
				}
				if ("and".equals(str[i])) {
					index2 = i;
				}
			}
			if (!condition.contains("to_date")) {
				condition = "createtime between " + "to_date(" + str[index1 + 1] + ",'yyyy-MM-dd')" + " and " + "to_date(" + str[index2 + 1] + ",'yyyy-MM-dd')";
			}
		} else if (condition.contains("createtime") && condition.contains("between")) {
			// 起于
			String[] str = condition.split(" ");
			int index1 = 0;
			for (int i = 0; i < str.length; i++) {
				if ("between".equals(str[i])) {
					index1 = i;
				}
			}
			if (!condition.contains("to_date")) {
				condition = "createtime >= " + "to_date(" + str[index1 + 1] + ",'yyyy-MM-dd')";
			}
		} else if (condition.contains("createtime") && condition.contains("and")) {
			// 止于
			String[] str = condition.split(" ");
			int index2 = 0;
			for (int i = 0; i < str.length; i++) {
				if ("and".equals(str[i])) {
					index2 = i;
				}
			}
			if (condition.contains("to_date")) {
				condition = "createtime <=" + "to_date(" + str[index2 + 1] + ",'yyyy-MM-dd')";
			}
		}

		int count = 0;
		PageBean<Map<String, Object>> page = new PageBean<Map<String, Object>>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = metaService.QueryData_ByPage(null, condition, "sortcode", page.getPageNum() - 1, page.getPageSize());
		count = metaService.QueryCount(condition);
		 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_ZBMGR, LogConst.EXC_ADV_QUERY, null, "" + count, condition.toUpperCase(), null, null, null, null, null, null, null, null, null, null);
		try {
			StringBuffer sb = new StringBuffer();
			condition = condition.replaceAll(" ", "%20");
			condition = condition.replaceAll("%", "%25");
			condition = condition.replaceAll("'", "%27");
			sb.append(req.getRequestURI());
			sb.append("?m=advQuery");
			sb.append("&searchSQL=" + condition);
			page.setData(list);
			page.setTotalRecorder(count);
			page.setUrl(sb.toString());
			// 发送返回数据
			// this.sendJson(resp, new JSONReturnData(page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (StringUtil.isEmpty(pjax)) {
			return new ModelAndView("/WEB-INF/jsp/metadata/zbmgr/index").addObject("page", page).addObject("ismove", 0);
		} else {
			return new ModelAndView("/WEB-INF/jsp/metadata/zbmgr/tableList").addObject("page", page).addObject("ismove", 0);
		}
	}

	/**
	 * 代码导入
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @author chenyf
	 */
	public void upload() throws FileNotFoundException, IOException {
		JSONReturnData data = new JSONReturnData("");
		List<String> list = new ArrayList<String>();
		String filedname = "";
		ServletFileUpload uploader = new ServletFileUpload(new DiskFileItemFactory());
		uploader.setHeaderEncoding("utf-8");
		try {
			ArrayList<FileItem> files = (ArrayList<FileItem>) uploader.parseRequest(this.getRequest());
			if (files.size() > 0) {
				FileItem file = files.get(0);
				InputStream inputStream = file.getInputStream();
				HSSFWorkbook workBook = null;
				try {
					workBook = new HSSFWorkbook(inputStream);
				} catch (Exception e) {
					data.setReturncode(500);
					data.setReturndata("上传文件错误");
					this.sendJson(data);
					return;
				}
				HSSFSheet sheet = workBook.getSheetAt(0);
				int totalRow = sheet.getPhysicalNumberOfRows();
				for (int i = 0; i < totalRow; i++) {
					int totalCell = sheet.getRow(i).getPhysicalNumberOfCells();
					for (int j = 0; j < totalCell; j++) {
						// 获取每一行的所有列
						HSSFCell cell = sheet.getRow(i).getCell(j);
						String cellValue = DataFormat.getCellValue(cell);
						if (i == 0) {
							filedname = cellValue;
						} else {
							list.add(cellValue);
						}
					}
				}
				String codesCondition = "";
				if (list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						if (i == 0) {
							codesCondition += filedname + " = " + "'" + list.get(i) + "'";
						} else {
							codesCondition += " or " + filedname + " = " + "'" + list.get(i) + "'";
						}
					}
				}
				data.setReturncode(200);
				data.setReturndata(codesCondition);
			} else {
				data.setReturncode(500);
				data.setReturndata("没有发现上传的文件");
			}
			this.sendJson(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
					queryCount += metaService.QueryCount(where1);
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
	 * @param 批量匹配详情数据导出
	 * @throws MetaException
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
			list.addAll(metaService.QueryData(getField(), where1, null));
		}
		 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_ZBMGR, LogConst.EXC_PLPP, null, "" + list.size(), sb.toString(), "" + list.size(), null, null, null, null, null, null, null, null, null);
		MetaDataExport.export(req, resp, list, getTitle(), getField(), null, null, "指标批量匹配数据结果", id);
	}

	/**
	 * @param 批量导入
	 * @author chenyf
	 */
	public void importData() {
		JSONReturnData data = new JSONReturnData("");
		ServletFileUpload uploader = new ServletFileUpload(new DiskFileItemFactory());
		uploader.setHeaderEncoding("utf-8");
		try {
			ArrayList<FileItem> files = (ArrayList<FileItem>) uploader.parseRequest(this.getRequest());
			if (files.size() > 0) {
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

						List<String> code = new ArrayList<String>(); // 所有的原始code
						List<String> ycode = new ArrayList<String>(); // 所有的补全后code
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
								String currentCode = null;
								// 处理必填项列
								for (Iterator<Integer> iterator = mkey.keySet().iterator(); iterator.hasNext();) {
									Integer integer = iterator.next();
									String key = mkey.get(integer);
									ExcelCell cell = row.getCells().get(integer);
									if (cell == null) {
										cell = new ExcelCell();
										// row.getCells().set(integer, cell);
										// cell = row.createCell(integer);
									}
									String value = metaService.getValue(cell.getText() + "");
									if (StringUtil.isEmpty(value)) {
										cell.setCellstyle(MetaExcelColor.getColor("red"));
										flag = false;
									} else if ("code".equals(key)) {
										orgCode = value;
										String clearCode = getCode(value);
										codes.put(key, clearCode);
										codes.put("sortcode", clearCode); // 排序码
										if (!checkMustValue(key, clearCode)) { // 校验必填项数据格式是否正确
											cell.setCellstyle(MetaExcelColor.getColor("pink"));
											checkData = false;
										}
										currentCode = clearCode;
									} else if (!checkMustValue(key, value)) { // 校验必填项数据格式是否正确
										cell.setCellstyle(MetaExcelColor.getColor("pink"));
										checkData = false;
									} else {
										codes.put(key, value);
									}
									// 添加重复的code
									if (index == integer) {
										if (ycode.contains(getCode(value))) {
											cqCode.add(value);
										} else {
											ycode.add(getCode(value));
											code.add(value); // 原始code
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
										// cell = row.createCell(integer);
									}
									String value = metaService.getValue(cell.getText() + "");
									if (!StringUtil.isEmpty(value)) {
										if ("procode".equals(key)) {
											value = getCode(value);
											try {
												int codelen = metaService.getCodelen();
												if (codelen > 0) {
													if (!currentCode.substring(0, codelen).equals(value.substring(0, codelen))) {
														cell.setCellstyle(MetaExcelColor.getColor("pink"));
														checkData = false;
													}
												}
											} catch (Exception e) {
												cell.setCellstyle(MetaExcelColor.getColor("pink"));
												checkData = false;
											}
										}
										if (!checkOtherValue(key, value)) { // 校验非必填项数据格式是否正确
											cell.setCellstyle(MetaExcelColor.getColor("pink"));
											checkData = false;
										} else {
											if ("classprocodes".equals(key)) {
												value = value.replace(',', '|');
											}
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
									String cd = code.get(i);
									String clearCode = getCode(cd);
									// 检查code
									int queryCount = metaService.QueryCount("code='" + clearCode + "'");
									if (queryCount > 0) {
										cqCode.add(code.get(i));
									}
								}
								if (cqCode.size() > 0) {// 标记code有重复的数据
									checkImportCode(data, name, book1, sheet, rows, MetaExcelColor.getColor("green"), index, cqCode, null, xlstype);
								} else if (errRow.size() > 0) { // 标记数据有死循环的情况
									checkImportCode(data, name, book1, sheet, rows, MetaExcelColor.getColor("blue"), index, errRow, procodeIndex, xlstype);
								} else if (needCheckProcode.size() > 0) {// 底库校验procode
									List<String> checkProcode = new ArrayList<String>();
									Set<String> keySet = needCheckProcode.keySet();
									for (String key : keySet) {
										// 检查code
										int queryCount = metaService.QueryCount("code='" + needCheckProcode.get(key) + "'");
										if (queryCount == 0) {
											checkProcode.add(key);
										}
									}
									if (checkProcode.size() > 0) {
										checkImportCode(data, name, book1, sheet, rows, MetaExcelColor.getColor("blue"), index, checkProcode, procodeIndex, xlstype);
									} else {
										int insertRows = metaService.InsertRows(sql);
										data.setParam1(insertRows);
										data.setReturncode(200);
										data.setReturndata("数据文件上传成功");
									}
								} else { // 表示所有数据都符合规则（入库）
									int insertRows = metaService.InsertRows(sql);
									data.setParam1(insertRows);
									data.setReturncode(200);
									data.setReturndata("数据文件上传成功");
								}
							} else if (!checkData) {// 校验数据格式是否正确
								// 写入字节流
								ByteArrayOutputStream bos = new ByteArrayOutputStream();
								book1.saveExcel(bos, xlstype);
								bos.flush();
								bos.close();
								String uuid = StringUtil.getUUID();
								acmr.cache.acmrCache.Factor.getInstance().Add(uuid, bos, 30); // 文件
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
		} catch (Exception e) {
			e.printStackTrace();
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
				// cell = row.createCell(index);
			}
			String value = cell.getText() + "";
			if (cqCode.contains(value)) {
				if (procodeIndex != null) {
					ExcelCell cell2 = row.getCells().get(procodeIndex);
					if (cell2 == null) {
						cell2 = new ExcelCell();
						// cell2 = row.createCell(procodeIndex);
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
	 * @throws MetaException
	 * @author chenyf
	 */
	public static void impErrInfoExport() throws MetaTableException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();

		String uuid = req.getParameter("uuid");
		Object item = acmr.cache.acmrCache.Factor.getInstance().get(uuid);
		String filename = (String) acmr.cache.acmrCache.Factor.getInstance().get(uuid + "fn");
		if (item == null || filename == null) {
			return;
		}
		try {
			// 定义输出类型
			resp.reset();
			resp.setContentType("application/vnd.ms-excel");
			resp.setHeader("Pragma", "public");
			resp.setHeader("Cache-Control", "max-age=30");
			filename = new String(filename.getBytes("gb2312"), "iso8859-1").replace(" ", "");
			resp.setHeader("Content-disposition", "attachment;filename=" + filename);
			// 生成Excel并响应客户端
			ServletOutputStream out = resp.getOutputStream();
			ByteArrayOutputStream bos = (ByteArrayOutputStream) item;
			resp.setContentLength(bos.size());
			bos.writeTo(out);
			out.close();
			out.flush();
			bos.close();
			bos.flush();
		} catch (IOException e) {
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
		hashMap.put("code", "code");// 指标代码
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
		hashMap.put("unitcode", "unitcode");// 中文单位
		hashMap.put("dotcount", "dotcount");// 小数点位数
		hashMap.put("classprocodes", "classprocodes"); // 优先分组代码
		hashMap.put("nbscode", "nbscode"); // NBS代码
		hashMap.put("zbsort", "zbsort");// 时间类型
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
			if (isContainChinese(value) || value.length() > 20 || value.length() < 1) {
				return false;
			}
			return true;
		} else if ("cname".equals(code)) {
			return checkLength(value, 100);
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
			if (isContainChinese(value) || value.length() > 20 || value.length() < 1) {
				return false;
			}
			return true;
		} else if ("ccname".equals(code)) {
			return checkLength(value, 100);
		} else if ("cname_en".equals(code)) {
			return checkLength(value, 250);
		} else if ("ccname_en".equals(code)) {
			return checkLength(value, 250);
		} else if ("cexp".equals(code)) {
			return checkLength(value, 2000);
		} else if ("cexp_en".equals(code)) {
			return checkLength(value, 2000);
		} else if ("cmemo".equals(code)) {
			return checkLength(value, 2000);
		} else if ("cmemo_en".equals(code)) {
			return checkLength(value, 2000);
		} else if ("unitcode".equals(code)) {
			if (isContainChinese(value) || value.length() > 20) {
				return false;
			}
			return true;
		} else if ("classprocodes".equals(code)) {
			if (isContainChinese(value) || value.length() > 2000) {
				return false;
			}
			String[] str = value.split(",");
			for (int i = 0; i < str.length; i++) {
				if (str[i].length() > 20) {
					return false;
				}
			}
			return true;
		} else if ("dotcount".equals(code)) {
			Pattern p = Pattern.compile("^[0-4]$");
			Matcher m = p.matcher(value);
			if (m.find()) {
				return true;
			}
			return false;
		} else if ("nbscode".equals(code)) {
			if (isContainChinese(value) || value.length() > 10) {
				return false;
			}
			return true;
		} else if ("zbsort".equals(code)) {
			return checkIfdata(value);
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
	 * @throws MetaException
	 * @author chenyf
	 */
	public void exportAllData() throws IOException, MetaTableException {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();

		// 获取查询数据
		String codes = req.getParameter("ids");
		// 构造返回对象
		if (StringUtil.isEmpty(codes)) {
			MetaDataExport.export(req, resp, new ArrayList<Map<String, Object>>(), getTitle(), getField(), null, null, Const.ZB_FILE_NAME);
			return;
		}
		String[] ids = codes.split(",");
		// 存放数据
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < ids.length; i++) { // 最多遍历10次，1w条数据
			List<SqlWhere> where1 = new ArrayList<SqlWhere>();
			// 最多1000条数据
			List<Map<String, Object>> dt = metaService.QueryData_InTree(getField(), where1, "code", ids[i], "procode", "sortcode");
			for (int j = 0; j < dt.size(); j++) {
				list.add(dt.get(j));
			}
		}
		 LogService.logOperation(req,UserService.getCurrentUser().getUserid(),LogConst.APPID, LogConst.COLUMN_ZBMGR, LogConst.EXC_EXPORT1, null, "" + ids.length, ids, "" + list.size(), null, null, null, null, null, null, null, null, null);
		MetaDataExport.export(req, resp, list, getTitle(), getField(), null, null, Const.ZB_FILE_NAME);
	}

	/**
	 * 检查code是否已经存在
	 * 
	 * @author chenyf
	 */
	public void checkProCode() throws IOException {
		HttpServletRequest req = CurrentContext.getRequest();

		String code = req.getParameter("code");
		String procode = req.getParameter("procode");
		// 构造返回对象
		JSONReturnData data = new JSONReturnData(501, "code不存在");
		try {
			if (!StringUtil.isEmpty(procode)) {
				String clearCode = getCode(procode);
				// 检查code 是否存在
				int queryCount = metaService.QueryCount("code='" + clearCode + "'");
				if (queryCount != 0) {
					List<Map<String, Object>> queryData_TreePath = metaService.QueryData_TreePath(null, "code", clearCode, "procode");
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
							int codelen = metaService.getCodelen();
							try {
								List<Map<String, Object>> queryData = metaService.QueryData(null, "code='" + clearCode + "'", "sortcode");
								Map<String, Object> map = queryData.get(0);
								String cname = (String) map.get("cname");
								if (codelen > 0) {
									String ss = code.substring(0, codelen);
									String ss2 = clearCode.substring(0, codelen);
									if (ss2.equals(ss)) {
										data.setReturncode(200);
										data.setParam1(cname);
										data.setReturndata("可以使用");
									} else {
										data.setReturndata("代码和父级代码前" + codelen + "位必须相同");
									}
								} else {
									data.setParam1(cname);
									data.setReturncode(200);
									data.setReturndata("可以使用");
								}

							} catch (Exception e) {
								data.setReturndata("代码和父级代码前" + codelen + "位必须相同");
							}
						}
					}
				}
			} else {
				data.setReturncode(200);
				data.setParam1("指标树");
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
	 * @throws MetaException
	 * @throws IOException
	 * @author chenyf
	 */
	public void checkHasParent() throws MetaTableException, IOException {
		HttpServletRequest req = CurrentContext.getRequest();

		String code = req.getParameter("code");
		JSONReturnData data = new JSONReturnData(501, "没有下级");
		if (!StringUtil.isEmpty(code)) {
			List<SqlWhere> where1 = new ArrayList<SqlWhere>();
			where1.add(new SqlWhere("procode", code, 10));
			int queryCount = metaService.QueryCount(where1);
			if (queryCount > 0) {
				data.setReturncode(200);
				data.setReturndata("有下级");
			}
		}
		this.sendJson(data);
	}

	/**
	 * 获取补填后的code
	 * 
	 * @author chenyf
	 */
	private String getCode(String code) {
		if (StringUtil.isEmpty(code)) {
			return code;
		}
		code = code.replaceAll("\\s*", "");
		String defaultCode = metaService.getPrecode();
		if (!StringUtil.isEmpty(defaultCode) && !code.startsWith(defaultCode)) {
			code = defaultCode + code;
		}
		return code;
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

	/**
	 * 批量修改模板下载
	 * 
	 * @param req
	 * @param resp
	 * @author chenyf
	 */
	public void templateDownload3() {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();
		String realPath = req.getServletContext().getRealPath("/template");
		String file = "批量修改模板." + Const.DOWNLOAD_TYPE;
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
	 * 模板详情
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @return
	 * @author chenyf
	 */
	public ModelAndView template() {
		HttpServletRequest req = this.getRequest();
		AjaxPageBean<Template> page = new AjaxPageBean<Template>();
		String code = req.getParameter("code");
		String type = req.getParameter("type");
		IMetaDao dao = MetaDao.Fator.getInstance().getDao();
		int count = dao.queryTemplate(code, type, "zb_");
		List<Template> data = dao.getTemplateData(code, type, page, "zb_");
		page.setData(data);
		page.setTotalRecorder(count);
		return new ModelAndView("/WEB-INF/jsp/metadata/zbmgr/template").addObject("page", page).addObject("code", code).addObject("type", type);
	}

	/**
	 * getPage模板详情
	 * 
	 * @param req
	 * @param resp
	 * @author chenyf
	 */
	public void getTemplateData() {
		HttpServletRequest req = this.getRequest();
		AjaxPageBean<Template> page = new AjaxPageBean<Template>();
		String code = req.getParameter("code");
		String type = req.getParameter("type");
		StringBuffer sb = new StringBuffer();
		sb.append(req.getRequestURI());
		sb.append("?m=getTemplateData");
		if (!StringUtil.isEmpty(code)) {
			sb.append("&code=" + code);
		}
		if (!StringUtil.isEmpty(type)) {
			sb.append("&type=" + type);
		}
		IMetaDao dao = MetaDao.Fator.getInstance().getDao();

		int count = dao.queryTemplate(code, type, "zb_");
		List<Template> templateData = dao.getTemplateData(code, type, page, "zb_");
		try {
			page.setTotalRecorder(count);
			page.setData(templateData);
			page.setUrl(sb.toString());
			this.sendJson(new JSONReturnData(page));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 数据详情
	 * 
	 * @param req
	 * @param resp
	 * @throws MetaException
	 * @throws UnsupportedEncodingException
	 * @author chenyf
	 */
	public ModelAndView datadetails() throws MetaTableException, UnsupportedEncodingException {
		MetaServiceManager metaService = MetaService.getMetaService("data");
		MetaServiceManager regService = MetaService.getMetaService("reg");
		MetaServiceManager dsService = MetaService.getMetaService("ds");
		HttpServletRequest req = this.getRequest();
		AjaxPageBean<DataDetails> page = new AjaxPageBean<DataDetails>();
		String code = req.getParameter("code");
		String cname = req.getParameter("cname");
		Object[] parm = { code };
		// 查询分组下拉
		String zbClassSql = "select vw_data.*,tb_class.cname as cname from (select CLASSCODE from TB_BASIC_DATA where code = ? GROUP BY CLASSCODE) vw_data left join TB_BASIC_CLASS tb_class on  tb_class.code = vw_data.classcode";
		DataTable zbClassData = metaService.queryDataSql(zbClassSql, parm);
		List<Map<String, Object>> queryFlData = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < zbClassData.getRows().size(); i++) {
			DataTableRow row = zbClassData.getRows().get(i);
			String ccode = row.getString("classcode");
			String ccname = row.getString("cname");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("code", ccode);
			map.put("cname", ccname);
			queryFlData.add(map);
		}

		List<String> codes = new ArrayList<String>();
		List<SqlWhere> where1 = new ArrayList<SqlWhere>();

		// 查询地区下拉
		String regSql = "select REGCODE from TB_BASIC_DATA where code = ? GROUP BY REGCODE";
		String regData = toData(metaService.queryDataSql(regSql, parm), "regcode");
		where1.clear();
		where1.add(new SqlWhere("code", regData, 16));
		List<Map<String, Object>> queryRegData = regService.QueryData(codes, where1, null);

		// 数据来源下拉
		String dsSql = "select DATASOURCE from TB_BASIC_DATA where code = ? GROUP BY DATASOURCE";
		String dsData = toData(metaService.queryDataSql(dsSql, parm), "datasource");
		where1.clear();
		where1.add(new SqlWhere("code", dsData, 16));
		List<Map<String, Object>> queryDsData = dsService.QueryData(codes, where1, null);

		try {
			MetaDao.Fator.getInstance().getDao().getAllData(metaService, page, code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("/WEB-INF/jsp/metadata/zbmgr/datadetails").addObject("queryRegData", queryRegData).addObject("queryFlData", queryFlData).addObject("queryDsData", queryDsData).addObject("page", page).addObject("code", code).addObject("cname", cname);
	}

	/**
	 * 数据详情分页
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @return
	 * @throws MetaException
	 * @throws UnsupportedEncodingException
	 * @author chenyf
	 */
	public void toDatadetails() throws MetaTableException, UnsupportedEncodingException {
		HttpServletRequest req = this.getRequest();
		String code = req.getParameter("code");
		String flcode = req.getParameter("flcode");
		String regcode = req.getParameter("regcode");
		String datasource = req.getParameter("datasource");

		AjaxPageBean<DataDetails> page = new AjaxPageBean<DataDetails>();
		StringBuffer url = new StringBuffer();
		url.append(req.getRequestURI());
		url.append("?m=toDatadetails&code=" + code);

		try {
			MetaDao.Fator.getInstance().getDao().toDatadetails(metaService, page, code, flcode, regcode, datasource, url);
			this.sendJson(new JSONReturnData(page));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 数据详情数据导出
	 * 
	 * @author chenyf
	 */
	public void exportDatadetails() throws MetaTableException {
		List<Map<String, String>> par = new ArrayList<Map<String, String>>();
		HttpServletRequest req = this.getRequest();
		HttpServletResponse resp = this.getResponse();
		String code = req.getParameter("code");
		String dbcode = req.getParameter("dbcode");
		String flcode = req.getParameter("flcode");
		String regcode = req.getParameter("regcode");
		String datasource = req.getParameter("datasource");
		String codename = req.getParameter("codename");
		String dbcodename = req.getParameter("dbcodename");
		String flcodename = req.getParameter("flcodename");
		String regcodename = req.getParameter("regcodename");
		String datasourcename = req.getParameter("datasourcename");
		HashMap<String, String> db = new HashMap<String, String>();
		db.put("code", dbcode);
		db.put("name", dbcodename);
		db.put("cname", "数据库");
		HashMap<String, String> codes = new HashMap<String, String>();
		codes.put("code", code);
		codes.put("name", codename);
		codes.put("cname", "指标");
		HashMap<String, String> fl = new HashMap<String, String>();
		fl.put("code", flcode);
		fl.put("name", flcodename);
		fl.put("cname", "指标分组");
		HashMap<String, String> reg = new HashMap<String, String>();
		reg.put("code", regcode);
		reg.put("name", regcodename);
		reg.put("cname", "地区");
		HashMap<String, String> ds = new HashMap<String, String>();
		ds.put("code", datasource);
		ds.put("name", datasourcename);
		ds.put("cname", "数据来源");
		par.add(db);
		par.add(codes);
		par.add(fl);
		par.add(reg);
		par.add(ds);

		StringBuffer sb = new StringBuffer();
		sb.append("select vw_data.*,tb_class.cname as flname,tb_reg.cname as regname,tb_ds.cname as dsname from (");
		sb.append("select CLASSCODE,REGCODE,DATASOURCE,count(*) as count,max(updatetime) as updatetime, max(AYEARMON) as maxnum,min(AYEARMON) as minnum from TB_BASIC_DATA where code =? ");
		List<Object> parm1 = new ArrayList<Object>();
		parm1.add(code);
		if (!StringUtil.isEmpty(flcode)) {
			sb.append("and '&' || CLASSCODE || '&' like ?");
			parm1.add("%&" + flcode + "&%");
		}
		if (!StringUtil.isEmpty(regcode)) {
			sb.append("and regcode = ?");
			parm1.add(regcode);
		}
		if (!StringUtil.isEmpty(datasource)) {
			sb.append("and datasource = ?");
			parm1.add(datasource);
		}
		sb.append("GROUP BY CLASSCODE,REGCODE,DATASOURCE");
		sb.append(") vw_data");
		sb.append(" left join TB_BASIC_CLASS tb_class on tb_class.code = vw_data.classcode");
		sb.append(" left join TB_BASIC_REGION tb_reg on vw_data.regcode = tb_reg.code");
		sb.append(" left join TB_BASIC_DATASOURCE tb_ds on vw_data.datasource = tb_ds.code");
		DataTable queryDataSql = metaService.queryDataSql(sb.toString(), parm1.toArray());
		try {
			// 定义输出类型
			resp.reset();
			resp.setContentType("application/vnd.ms-excel");
			resp.setHeader("Pragma", "public");
			resp.setHeader("Cache-Control", "max-age=30");
			if ("xls".equals(Const.DOWNLOAD_TYPE)) {
				String filename = code + "数据详情.xls";
				filename = new String(filename.getBytes("gb2312"), "iso8859-1").replace(" ", "");
				resp.setHeader("Content-disposition", "attachment;filename=" + filename);
				// 生成Excel并响应客户端
				ServletOutputStream out = resp.getOutputStream();
				ByteArrayOutputStream bos = getDatadetailsStream(code + "数据详情", queryDataSql, par);
				resp.setContentLength(bos.size());
				bos.writeTo(out);
				bos.flush();
				bos.close();
				out.flush();
				out.close();
			} else {
				String filename = code + "数据详情.xlsx";
				filename = new String(filename.getBytes("gb2312"), "iso8859-1").replace(" ", "");
				resp.setHeader("Content-disposition", "attachment;filename=" + filename);
				// 生成Excel并响应客户端
				ServletOutputStream out = resp.getOutputStream();
				try {
					ByteArrayOutputStream bos = getDatadetailsStream(code + "数据详情", queryDataSql, par);
					Workbook get07ExcelByte = MetaService.get07ExcelByte(bos.toByteArray(), "xls");
					bos.reset();
					get07ExcelByte.write(bos);
					resp.setContentLength(bos.size());
					bos.writeTo(out);
					bos.flush();
					bos.close();
					out.flush();
					out.close();
				} catch (InvalidFormatException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 数据详情导出文件流
	 * 
	 * @param sheetName
	 * @param queryDataSql
	 * @param par
	 * @param search
	 * @return
	 * @throws IOException
	 * @author chenyf
	 */
	public ByteArrayOutputStream getDatadetailsStream(String sheetName, DataTable queryDataSql, List<Map<String, String>> par) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建sheet
		HSSFSheet sheet = workbook.createSheet(sheetName);
		sheet.setDefaultColumnWidth(1 * 15);
		sheet.setDefaultRowHeight((short) (20 * 20));

		// 创建表头样式
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		// 文本位置(居中)
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 设置字体
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);// 字体大小
		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		// font.setColor(HSSFColor.BLUE.index);//字体颜色
		cellStyle.setFont(font);
		// thAlign居中
		HSSFCellStyle thAlign = workbook.createCellStyle();
		thAlign.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 垂直居中加粗
		HSSFCellStyle style = workbook.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font theadFont = workbook.createFont();
		theadFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		style.setFont(theadFont);

		int index = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (int i = 0; i < par.size(); i++) {
			Map<String, String> map = par.get(i);
			// String code = map.get("code");
			String name = map.get("name");
			String cname = map.get("cname");
			HSSFRow th = sheet.createRow(index++);
			HSSFCell td = th.createCell(0);
			String value = cname + "：" + name;
			// if (!StringUtil.isEmpty(code)) {
			// value = value + "(" + code + ")";
			// }
			td.setCellValue(value);
			td.setCellStyle(thAlign);
		}

		ArrayList<String> title = new ArrayList<String>();
		title.add("数据库");
		title.add("指标分组");
		title.add("地区");
		title.add("开始时间");
		title.add("结束时间");
		title.add("更新时间	");
		title.add("数据量");
		title.add("数据来源");

		// 创建表头
		HSSFRow th = sheet.createRow(index++);
		for (int i = 0; i < title.size(); i++) {
			HSSFCell td = th.createCell(i);
			td.setCellValue(title.get(i));
			td.setCellStyle(style);
		}

		for (int i = 0; i < queryDataSql.getRows().size(); i++) {
			DataTableRow row = queryDataSql.getRows().get(i);
			HSSFRow tr = sheet.createRow(index++);
			HSSFCell td0 = tr.createCell(0);
			td0.setCellValue("");

			HSSFCell td1 = tr.createCell(1);
			String flcode = row.getString("classcode");
			String flname = row.getString("flname");
			td1.setCellValue(flname + "(" + flcode + ")");

			HSSFCell td2 = tr.createCell(2);
			String regcode = row.getString("regcode");
			String regname = row.getString("regname");
			td2.setCellValue(regname + "(" + regcode + ")");

			HSSFCell td3 = tr.createCell(3);
			String beginTime = row.getString("minnum");
			td3.setCellValue(beginTime);

			HSSFCell td4 = tr.createCell(4);
			String endTime = row.getString("maxnum");
			td4.setCellValue(endTime);

			HSSFCell td5 = tr.createCell(5);
			java.util.Date date = row.getDate("updatetime");
			String updatetime = "";
			if (null != date) {
				updatetime = sdf.format(date);
			}
			td5.setCellValue(updatetime);

			HSSFCell td6 = tr.createCell(6);
			String count = row.getString("count");
			td6.setCellValue(count);

			HSSFCell td7 = tr.createCell(7);
			String datasource = row.getString("datasource");
			String dsname = row.getString("dsname");
			td7.setCellValue(dsname + "(" + datasource + ")");
		}

		// 写入字节流
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		return bos;
	}

	/**
	 * 数据详情联动下拉框
	 * 
	 * @param req
	 * @param resp
	 * @throws MetaException
	 * @author chenyf
	 */
	@SuppressWarnings("unchecked")
	public void getOtherSel() throws MetaTableException {
		HttpServletRequest req = this.getRequest();
		MetaServiceManager regService = MetaService.getMetaService("reg");
		MetaServiceManager dsService = MetaService.getMetaService("ds");
		String flag = req.getParameter("flag");
		String code = req.getParameter("code");
		String flcode = req.getParameter("flcode");
		String regcode = req.getParameter("regcode");
		List<Map<String, Object>> queryFlData = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> queryRegData = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> queryDsData = new ArrayList<Map<String, Object>>();
		@SuppressWarnings("rawtypes")
		Map list = new HashMap();
		// 构造返回对象
		JSONReturnData data = new JSONReturnData(501, "出现错误");
		boolean next = false;
		// 操作项 数据库
		if ("1".equals(flag)) {
			StringBuffer flsql = new StringBuffer();
			List<Object> parm1 = new ArrayList<Object>();
			parm1.add(code);
			flsql.append("select vw_data.*,tb_class.cname as cname from (select CLASSCODE,count(CLASSCODE) as count from TB_BASIC_DATA where code = ?");
			flsql.append(" GROUP BY CLASSCODE) vw_data left join TB_BASIC_CLASS tb_class on  tb_class.code = vw_data.classcode");
			DataTable zbClassData = metaService.queryDataSql(flsql.toString(), parm1.toArray());
			for (int i = 0; i < zbClassData.getRows().size(); i++) {
				DataTableRow row = zbClassData.getRows().get(i);
				String ccode = row.getString("classcode");
				String ccname = row.getString("cname");
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("code", ccode);
				map.put("cname", ccname);
				queryFlData.add(map);
			}
			next = true;
			list.put("fldata", queryFlData);
		}

		List<String> codes = new ArrayList<String>();
		List<SqlWhere> where1 = new ArrayList<SqlWhere>();
		// 查询地区下拉
		if ("2".equals(flag) || next == true) {
			StringBuffer regsql = new StringBuffer();
			List<Object> parm1 = new ArrayList<Object>();
			parm1.add(code);
			regsql.append("select REGCODE,count(REGCODE) from TB_BASIC_DATA where code = ? ");
			if (!StringUtil.isEmpty(flcode)) {
				regsql.append(" and '&' || CLASSCODE || '&' like ? ");
				parm1.add("%&" + flcode + "&%");
			}
			regsql.append(" GROUP BY REGCODE");
			String regData = toData(metaService.queryDataSql(regsql.toString(), parm1.toArray()), "regcode");
			where1.clear();
			where1.add(new SqlWhere("code", regData, 16));
			queryRegData = regService.QueryData(codes, where1, null);
			list.put("regdata", queryRegData);
			next = true;

		}
		// 数据来源下拉
		if ("3".equals(flag) || next == true) {
			StringBuffer dssql = new StringBuffer();
			List<Object> parm1 = new ArrayList<Object>();
			parm1.add(code);
			dssql.append("select DATASOURCE,count(DATASOURCE) from TB_BASIC_DATA where code = ? ");

			if (!StringUtil.isEmpty(flcode)) {
				dssql.append(" and '&' || CLASSCODE || '&' like ? ");
				parm1.add("%&" + flcode + "&%");
			}

			if (!StringUtil.isEmpty(regcode)) {
				dssql.append(" and regcode = ? ");
				parm1.add(regcode);
			}
			dssql.append("GROUP BY DATASOURCE");
			String dsData = toData(metaService.queryDataSql(dssql.toString(), parm1.toArray()), "datasource");
			where1.clear();
			where1.add(new SqlWhere("code", dsData, 16));
			queryDsData = dsService.QueryData(codes, where1, null);
			list.put("dsdata", queryDsData);
		}
		data.setReturncode(200);
		data.setReturndata(list);
		try {
			this.sendJson(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 数据详情值转换
	 * 
	 * @param data
	 * @param col
	 * @return
	 * @author chenyf
	 */
	public String toData(DataTable data, String col) {
		StringBuffer sb = new StringBuffer();
		List<DataTableRow> rows = data.getRows();
		for (int i = 0; i < rows.size(); i++) {
			String object = rows.get(i).getString(0);
			sb.append(object + ";");
		}
		return sb.toString();
	}

	/**
	 * 批量修改
	 * 
	 * @author chenyf
	 */
	public void importEditData() {
		JSONReturnData data = new JSONReturnData("");
		ServletFileUpload uploader = new ServletFileUpload(new DiskFileItemFactory());
		uploader.setHeaderEncoding("utf-8");
		try {
			ArrayList<FileItem> files = (ArrayList<FileItem>) uploader.parseRequest(this.getRequest());
			if (files.size() > 0) {
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
											mkey.put(i, value);
										} else if (isOther(value) && !okey.containsValue(value)) {
											okey.put(i, value);
										}
									}
								}
							}
						}

						boolean checkid = true;
						boolean checkData = true;

						List<Map<String, Object>> sql = new ArrayList<Map<String, Object>>();
						List<Map<String, Object>> ikey = new ArrayList<Map<String, Object>>();

						// 如果标识列不为空，则遍历内容
						if (mkey.size() + okey.size() > 1) {
							// 遍历所有值
							for (int i = 2; i < rows; i++) {
								Map<String, Object> codes = new HashMap<String, Object>();
								Map<String, Object> keys = new HashMap<String, Object>();
								ExcelRow row = sheet.getRows().get(i);
								if (row == null || row.isNull()) {
									continue;
								}
								Boolean ifcontinue = false;
								String code = null;
								// 处理必填项列
								for (Iterator<Integer> iterator = mkey.keySet().iterator(); iterator.hasNext();) {
									Integer integer = iterator.next();
									String key = mkey.get(integer);
									ExcelCell cell = row.getCells().get(integer);
									if (cell == null) {
										cell = new ExcelCell();
									}
									String value = metaService.getValue(cell.getText() + "");
									if (StringUtil.isEmpty(value) && !"code".equals(key)) {
										continue;
									}
									if ("code".equals(key)) {
										int queryCount = metaService.QueryCount("code='" + value + "'");
										code = value;
										if (StringUtil.isEmpty(value) || code.length() < 1) {
											ifcontinue = true;
											break;
										}
										keys.put("code", value);
										// 数据库校验code是否存在
										if (queryCount == 0) {
											// 表示不存在，无法修改
											cell.setCellstyle(MetaExcelColor.getColor("yellow"));
											checkid = false;
										}
									} else if ("###".equals(value)) {
										// 表示不能置为空
										checkid = false;
										cell.setCellstyle(MetaExcelColor.getColor("red"));
									} else if (!checkMustValue(key, value)) { // 校验必填项数据格式是否正确
										checkid = false;
										cell.setCellstyle(MetaExcelColor.getColor("pink"));
									} else {
										if (!StringUtil.isEmpty(value)) {
											codes.put(key, value);
										}
									}
								}
								// 是否终止此行
								if (ifcontinue) {
									continue;
								}
								// 处理非必填项列
								for (Iterator<Integer> iterator = okey.keySet().iterator(); iterator.hasNext();) {
									Integer integer = iterator.next();
									String key = okey.get(integer);
									ExcelCell cell = row.getCells().get(integer);
									if (cell == null) {
										cell = new ExcelCell();
									}
									String value = metaService.getValue(cell.getText() + "");
									if (!StringUtil.isEmpty(value)) {
										if ("###".equals(value)) {
											codes.put(key, "");
										} else {
											if ("procode".equals(key)) {
												String clearCode = value;
												int codelen = metaService.getCodelen();
												if (codelen > 0) {
													try {
														if (clearCode.length() < 1 || !code.substring(0, codelen).equals(clearCode.substring(0, codelen))) {
															cell.setCellstyle(MetaExcelColor.getColor("blue"));
															checkData = false;
														}
													} catch (Exception e) {
														cell.setCellstyle(MetaExcelColor.getColor("blue"));
														checkData = false;
													}
												}

												// 检查code 是否存在，如果存在，则可以使用
												int queryCount = metaService.QueryCount("code='" + clearCode + "'");
												if (queryCount != 0) {
													List<Map<String, Object>> queryData_TreePath = metaService.QueryData_TreePath(null, "code", clearCode, "procode");
													ArrayList<String> list = new ArrayList<String>();
													if (queryData_TreePath.size() > 0) {
														for (int k = 0; k < queryData_TreePath.size(); k++) {
															Map<String, Object> map = queryData_TreePath.get(k);
															String cd = (String) map.get("code");
															list.add(cd);
														}
														boolean contains = list.contains(code);
														if (contains) {
															cell.setCellstyle(MetaExcelColor.getColor("blue"));
															checkData = false;
														} else {
															codes.put(key, value);
														}
													}
												} else {
													cell.setCellstyle(MetaExcelColor.getColor("blue"));
													checkData = false;
												}

											} else if (!checkOtherValue(key, value)) { // 校验非必填项数据格式是否正确
												cell.setCellstyle(MetaExcelColor.getColor("pink"));
												checkData = false;
											} else {
												codes.put(key, value);
											}
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
								// 内容
								sql.add(codes);
								// 主键
								ikey.add(keys);

								count++;
							}

							// 如果修改的数据量大于10000条，则提示用户数量超标
							if (count >= 10000) {
								data.setReturncode(400);
								data.setReturndata("修改的数据不能超过10000行，目前有" + count + "行");
								return;
							}

							// 通过校验
							if (checkid && checkData) {
								int updateRows = metaService.UpdateRows(ikey, sql);
								data.setParam1(updateRows);
								data.setReturncode(200);
								data.setReturndata("数据修改成功");
							} else {
								// 未通过校验 写入字节流
								ByteArrayOutputStream bos = new ByteArrayOutputStream();
								book1.saveExcel(bos, xlstype);
								bos.flush();
								bos.close();
								String uuid = StringUtil.getUUID();
								acmr.cache.acmrCache.Factor.getInstance().Add(uuid, bos, 30); // 文件
								acmr.cache.acmrCache.Factor.getInstance().Add(uuid + "fn", name, 30);// 文件名
								data.setReturncode(300);
								data.setParam1(uuid);
								data.setReturndata("数据格式不合法");
							}
						} else {
							data.setReturncode(400);
							data.setReturndata("模板格式不正确，第二行数据必须包含：code及至少一个要修改的字段");
						}
					} catch (Exception e) {
						e.printStackTrace();
						data.setReturncode(500);
						data.setReturndata("数据修改失败");
					}
				}
			} else {
				data.setReturncode(500);
				data.setReturndata("没有发现上传的文件");
			}
			this.sendJson(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void findUnit() throws IOException, MetaTableException {
		// 接收参数
		HttpServletRequest req = this.getRequest();
		HttpServletResponse resp = this.getResponse();
		String wdcode = req.getParameter("wd");
		String text = req.getParameter("text");
		String dbcode = Const.DB_CODE;
		ArrayList<sItem> list = null;
		list = (ArrayList<sItem>) SearchService.factor.getInstance().find(text, wdcode, dbcode);
		// 发送返回数据
		searchList l = new searchList();
		if (list != null) {
			l.setSuccess("true");
			l.setResults(list);;
		}
		// System.out.print(JSON.toJSONString(l));
		resp.setContentType("application/json; charset=utf-8");
		resp.getWriter().print(JSON.toJSONString(l));		
	}
}
