package com.acmr.web.jsp.system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acmr.dao.data.AcmrDataPageDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.pub.ZTreeNode;
import com.acmr.model.security.Department;
import com.acmr.model.sysindex.FlowIndex;
import com.acmr.model.sysindex.Stream;
import com.acmr.service.security.DepartmentService;
import com.acmr.service.security.SafeService;
import com.acmr.service.security.UserService;
import com.alibaba.fastjson.JSON;

import acmr.excel.ExcelException;
import acmr.excel.pojo.Constants.XLSTYPE;
import acmr.excel.pojo.ExcelBook;
import acmr.excel.pojo.ExcelCell;
import acmr.excel.pojo.ExcelRow;
import acmr.excel.pojo.ExcelSheet;
import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;

public class dep extends BaseAction {

	@Override
	public boolean servletLoad() throws IOException {
		return SafeService.checkUserRight("");
	}

	@Override
	public ModelAndView main() throws IOException {
		String pjax = PubInfo.getString(this.getRequest().getHeader("X-PJAX"));
		AcmrDataPageDao<Department> page = new AcmrDataPageDao<Department>();
		List<Department> listdata = DepartmentService.findDepartment("", "");
		page.findPage(listdata);
		String url = this.getUrl(this.getRequest());
		url +="?m=find";
		page.getPagebean().setUrl(url.toString());
		if (pjax.equals("")) {
			return new ModelAndView("/WEB-INF/jsp/management/department/index").addObject("page", page.getPagebean()).addObject("url", url);
		} else {
			return new ModelAndView("/WEB-INF/jsp/management/department/tabList").addObject("page", page.getPagebean()).addObject("url", url);

		}
	}
	/**
	 * 跳转审批流程界面
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView addFlow(){
		HttpServletRequest req=this.getRequest();
		String code = req.getParameter("code");
		if (StringUtil.isEmpty(code)) {
			return null;
		}
		Department department = DepartmentService.getDepartment(code);
		String flowContent = "";
		if(department!=null && "1".equals(department.getFlowtype()) && !StringUtil.isEmpty(department.getFlowcode())){
//			FlowIndexService flowIndexService =FlowIndexService.factor.getInstance();
//			FlowIndex flowindex = flowIndexService.getFlowIndexByCode(department.getFlowcode());
//			if(null != flowindex){
//				flowContent = toFlowContent(flowindex.getFlowdata());
//			}
		}
		return new ModelAndView("/WEB-INF/jsp/management/department/review").addObject("code", code).addObject("user",department).addObject("flowContent",flowContent);
	}
	private String toFlowContent(String content){
		ArrayList<Stream> list = new ArrayList<Stream>();
		if(!StringUtil.isEmpty(content)){
			String[] split = content.split(">");
			for (String str : split) {
				Stream stream = new Stream();
				String mark = "all";
				switch (str.substring(0, 1)) {
				case "|":
					mark = "single";
					break;
				case "&":
					mark = "all";
					break;
				default:
					break;
				}
				stream.setVittedno(mark);
				String substring = str.substring(1);
				
				String[] split2 = substring.substring(1).replaceAll("@", ",").split(",");
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < split2.length; i++) {
					sb.append(DepartmentService.getNameByCode(split2[i]) + ",");
				}
				stream.setName(sb.toString().substring(0,sb.toString().length()-1));
				stream.setPerson(substring.substring(1).replaceAll("@", ","));
				list.add(stream);
			}
		}
		return JSON.toJSONString(list);
	}
	/**
	 * 根据条件查找部门
	 * @param code 组织编码
	 * @param selId 选择Id
	 * @throws IOException
	 */
	public ModelAndView find() throws IOException {
		HttpServletRequest request = this.getRequest();
		// 判断是否为pjax请求
		String pjax = PubInfo.getString(request.getHeader("X-PJAX"));
		String proCode = PubInfo.getString(request.getParameter("depcode"));
		String findValue = PubInfo.getString(this.getParameter(request, "findvalue"));
		AcmrDataPageDao<Department> page = new AcmrDataPageDao<Department>();
		List<Department> listdata = DepartmentService.findDepartment(proCode, findValue);
		page.findPage(listdata);
		// 执行查询
		String url = this.getUrl(request);
		page.getPagebean().setUrl(url);
		if (StringUtil.isEmpty(pjax)) {
			return new ModelAndView("/WEB-INF/jsp/management/department/index").addObject("page", page.getPagebean()).addObject("depcode", proCode).addObject("findvalue", findValue).addObject("url", url);
		} else {
			return new ModelAndView("/WEB-INF/jsp/management/department/tabList").addObject("page", page.getPagebean()).addObject("depcode", proCode).addObject("findvalue", findValue).addObject("url", url);
		}
	}
	/**
	 * 获取decode解码后的key参数值，常用于获取获取get模式提交的参数，防止出现乱码
	 * @param req	servlet的request请求参数类
	 * @param key	请求参数的键
	 * @return	key请求参数值
	 */
	private String getParameter(HttpServletRequest req, String key) {
		String value = req.getParameter(key);
		if (StringUtil.isEmpty(value)) {
			return "";
		}
		if (req.getMethod().toLowerCase().equals("get")) {
			try {
				value = java.net.URLDecoder.decode(value.replaceAll("%", "###"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return value.replaceAll("###", "%25");
	}
	/**
	 * 获取操作后的url
	 * 
	 * @param request
	 *            请求类
	 * @return 生成url
	 */
	private String getUrl(HttpServletRequest req) {
		Enumeration<String> keys = req.getParameterNames();
		int v = 0;
		StringBuffer urlpara = new StringBuffer();
		urlpara.append(req.getRequestURI());
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if (!key.equals("_pjax") && !key.equals("pageNum")) {
				String value = this.getParameter(req, key);
				if (v++ == 0) {
					urlpara.append("?").append(key).append("=").append(value);
				} else {
					urlpara.append("&").append(key).append("=").append(value);
				}
			}
		}
		return urlpara.toString();
	}
	public void getchilds() throws IOException {
		String procode = PubInfo.getString(this.getRequest().getParameter("id"));
		List<ZTreeNode> childs = DepartmentService.getSubDepartments(procode);
		String re = JSON.toJSONString(childs);
		this.getResponse().getWriter().print(re);
	}

	public void delete() throws IOException {
		// 构造返回对象
		JSONReturnData data = new JSONReturnData("");
		data.setReturncode(200);
		String code = PubInfo.getString(this.getRequest().getParameter("id"));
		if (DepartmentService.getSubDepartments(code).size() != 0) {
			data.setReturncode(320);
			this.sendJson(data);
			return;
		}
		if (UserService.getDepUsers(code).size() != 0) {
			data.setReturncode(300);
			this.sendJson(data);
			return;
		}

		int int1 = DepartmentService.delDepartment(code);
		if (int1 == -1) {
			data.setReturncode(501);
			data.setReturndata("fail");
			this.sendJson(data);
			return;
		}
		data.setReturncode(200);
		data.setReturndata("");
		this.sendJson(data);
	}

	public ModelAndView toAdd() {
		String depcode = PubInfo.getString(this.getRequest().getParameter("depcode"));
		String depname = "";
		depname = DepartmentService.getDepartment(depcode).getCname();
		return new ModelAndView("/WEB-INF/jsp/management/department/add").addObject("deptcode", depcode).addObject("deptName", depname);
	}

	public void insert() throws IOException {
		HttpServletRequest req = this.getRequest();
		String code = PubInfo.getString(req.getParameter("code"));
		JSONReturnData data = new JSONReturnData("");
		if (DepartmentService.getDepartment(code) != null) {
			data.setReturncode(300);
			data.setReturndata("fail");
			this.sendJson(data);
			return;
		}

		String name = PubInfo.getString(req.getParameter("name"));
		String memo = PubInfo.getString(req.getParameter("memo"));
		String proCode = PubInfo.getString(req.getParameter("deptcode"));
		Department dep = new Department();

		dep.setCode(code);
		dep.setCname(name);
		dep.setParent(proCode);
		dep.setMemo(memo);
		dep.setUpdateuserid(UserService.getCurrentUser().getUserid());
		int int1 = DepartmentService.addDepartment(dep);
		if (int1 == -1) {
			data.setReturncode(501);
			data.setReturndata("fail");
			this.sendJson(data);
			return;
		}
		data.setReturndata(dep);
		this.sendJson(data);
	}

	public void checkCode() throws IOException {
		HttpServletRequest req = this.getRequest();
		String code = PubInfo.getString(req.getParameter("code"));
		JSONReturnData data = new JSONReturnData("");
		if (DepartmentService.getDepartment(code) != null) {
			data.setReturncode(300);
		} else {
			data.setReturncode(200);
		}
		this.sendJson(data);
	}

	public ModelAndView toedit(){
		String code = PubInfo.getString(this.getRequest().getParameter("code"));
		Department department = DepartmentService.getDepartment(code);
		return new ModelAndView("/WEB-INF/jsp/management/department/edit").addObject("department", department);
	}
	
	public void move() throws IOException {
		HttpServletRequest req = this.getRequest();
		String currentId = PubInfo.getString(req.getParameter("currentId"));
		String siblingsId = PubInfo.getString(req.getParameter("siblingsId"));
		JSONReturnData data = new JSONReturnData("");
		int int1 = DepartmentService.moveDepartment(currentId, siblingsId);
		if (int1 == -1) {
			data.setReturncode(500);
			data.setReturndata("fail");
		}
		this.sendJson(data);
	}

	public void toExcel() throws IOException {
		String depcode = PubInfo.getString(this.getRequest().getParameter("depcode"));
		String findvalue = PubInfo.getString(this.getRequest().getParameter("code"));
		List<Department> listdata = DepartmentService.findDepartment(depcode, findvalue);
		ExcelBook book = new ExcelBook();
		ExcelSheet sheet1 = new ExcelSheet();
		sheet1.setName("sheet1");
		sheet1.addColumn();
		sheet1.addColumn();
		ExcelCell cell1 = new ExcelCell();
		ExcelRow dr1 = sheet1.addRow();
		ExcelCell cell2 = cell1.clone();
		cell2.setCellValue("代码");
		dr1.set(0, cell2);
		cell2 = cell1.clone();
		cell2.setCellValue("名称");
		dr1.set(1, cell2);

		cell1.getCellstyle().getFont().setBoldweight((short) 10);
		for (int i = 0; i < listdata.size(); i++) {
			Department dep = listdata.get(i);
			dr1 = sheet1.addRow();
			cell2 = cell1.clone();
			cell2.setCellValue(dep.getCode());
			dr1.set(0, cell2);
			cell2 = cell1.clone();
			cell2.setCellValue(dep.getCname());
			dr1.set(1, cell2);
		}
		book.getSheets().add(sheet1);
		HttpServletResponse resp = this.getResponse();
		resp.reset();
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Pragma", "public");
		resp.setHeader("Cache-Control", "max-age=30");
		try {
			book.saveExcel(resp.getOutputStream(), XLSTYPE.XLSX);
		} catch (ExcelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update() throws IOException {
		HttpServletRequest req = this.getRequest();
		String code = PubInfo.getString(req.getParameter("code"));
		JSONReturnData data = new JSONReturnData("");
		Department dep = DepartmentService.getDepartment(code);
		if (dep == null) {
			data.setReturncode(300);
			data.setReturndata("fail");
			this.sendJson(data);
			return;
		}

		String name = PubInfo.getString(this.getParameter(req, "cname"));
		String memo = PubInfo.getString(this.getParameter(req, "memo"));
        dep.setCname(name);
        dep.setMemo(memo);
       int int1= DepartmentService.updateDepartment(dep);
        if(int1==-1){
			data.setReturncode(501);
			data.setReturndata("fail");
			this.sendJson(data);
			return;
        }
        data.setReturndata(dep);
        this.sendJson(data);
	}
}
