package com.acmr.web.jsp.system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import acmr.excel.ExcelException;
import acmr.excel.pojo.Constants.XLSTYPE;
import acmr.excel.pojo.ExcelBook;
import acmr.excel.pojo.ExcelCell;
import acmr.excel.pojo.ExcelRow;
import acmr.excel.pojo.ExcelSheet;
import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;

import com.acmr.dao.data.AcmrDataPageDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.pub.ZTreeNode;
import com.acmr.model.security.Department;
import com.acmr.model.security.User;
import com.acmr.model.sysindex.Stream;
import com.acmr.service.security.DepartmentService;
import com.acmr.service.security.UserService;
import com.alibaba.fastjson.JSON;

public class person extends BaseAction {

	@Override
	public boolean servletLoad() throws IOException {
		return true;
	}

	@Override
	public ModelAndView main() throws IOException {
		String pjax = PubInfo.getString(this.getRequest().getHeader("X-PJAX"));
		AcmrDataPageDao<User> page = new AcmrDataPageDao<User>();
		List<User> listdata = UserService.Finduser("", "");
		page.findPage(listdata);
		String url = this.getUrl(this.getRequest());
		url += "?m=find";
		page.getPagebean().setUrl(url);
		if (pjax.equals("")) {
			return new ModelAndView("/WEB-INF/jsp/management/person/index").addObject("page", page.getPagebean()).addObject("url",url);
		} else {
			return new ModelAndView("/WEB-INF/jsp/management/person/tabList").addObject("page", page.getPagebean()).addObject("url",url);
		}
	}
	public void getSubUsers() throws IOException{
		
		JSONReturnData data = new JSONReturnData("");
		data.setReturndata(UserService.getSubUsers("",true));
		this.sendJson(data);
	}
	/**
	 * 跳转审批流程界面
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView addFlow() {
		HttpServletRequest req = this.getRequest();
		String code = req.getParameter("code");
		if (StringUtil.isEmpty(code)) {
			return null;
		}
		User user = UserService.getUserInfo(code);
		String flowContent = "";
		if (user != null && "1".equals(user.getFlowtype()) && !StringUtil.isEmpty(user.getFlowcode())) {
//			FlowIndexService flowIndexService =FlowIndexService.factor.getInstance();
//			FlowIndex flowindex = flowIndexService.getFlowIndexByCode(user.getFlowcode());
//			if (null != flowindex) {
//				flowContent = toFlowContent(flowindex.getFlowdata());
//			}
		}
		return new ModelAndView("/WEB-INF/jsp/management/person/review").addObject("code", code).addObject("user", user).addObject("flowContent", flowContent);
	}
	private String toFlowContent(String content) {
		ArrayList<Stream> list = new ArrayList<Stream>();
		if (!StringUtil.isEmpty(content)) {
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
				stream.setName(sb.toString().substring(0, sb.toString().length() - 1));
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
		String findValue = PubInfo.getString(this.getParameter(request, "findValue"));
		
		AcmrDataPageDao<User> page = new AcmrDataPageDao<User>();
		List<User> listdata = UserService.Finduser(proCode, findValue);
		page.findPage(listdata);
		// 执行查询
		String url = this.getUrl(request);
		page.getPagebean().setUrl(url);
		if (StringUtil.isEmpty(pjax)) {
			return new ModelAndView("/WEB-INF/jsp/management/person/index").addObject("page", page.getPagebean()).addObject("depcode", proCode).addObject("findvalue", findValue).addObject("url", url);
		} else {
			return new ModelAndView("/WEB-INF/jsp/management/person/tabList").addObject("page", page.getPagebean()).addObject("depcode", proCode).addObject("findvalue", findValue).addObject("url", url);
		}
	}

	public ModelAndView toAdd() {
		// 取得部门信息
		String depcode = PubInfo.getString(this.getRequest().getParameter("depcode"));
		String depName;
		depName = DepartmentService.getDepartment(depcode).getCname();
		// 查询所有角色信息
		return new ModelAndView("/WEB-INF/jsp/management/person/add").addObject("depcode", depcode).addObject("depName", depName);
	}

	public void insert() throws IOException {
		JSONReturnData data = new JSONReturnData("");
		User user = new User();
		try {
			BeanUtils.populate(user, this.getRequest().getParameterMap());
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user.setUpdateuserid(UserService.getCurrentUser().getUserid());
		String pwd = PubInfo.getHASHcode(this.getRequest().getParameter("pwd"));
		int int1 = UserService.addUser(user, pwd);
		if (int1 == -1) {
			data.setReturncode(500);
			data.setReturndata("fail");
			this.sendJson(data);
			return;
		}
		data.setReturndata(user);
		this.sendJson(data);
	}

	public void checkuser() throws IOException {
		String name = PubInfo.getString(this.getRequest().getParameter("name"));
		User user = UserService.getUserInfo(name);
		JSONReturnData data = new JSONReturnData("");
		if (user == null) {
			data.setReturncode(300);
			data.setReturndata("no data");
		} else {
			if (user.getUserid().equals(name)) {
				data.setReturncode(201);
				data.setReturndata("is userid");
			} else {
				data.setReturncode(202);
				data.setReturndata("is email");
			}
		}
		this.sendJson(data);
	}

	public void update() throws IOException {
		JSONReturnData data = new JSONReturnData("");
		User user = new User();
		try {
			BeanUtils.populate(user, this.getRequest().getParameterMap());
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int int1 = UserService.updateUser(user);
		if (int1 == -1) {
			data.setReturncode(500);
			data.setReturndata("fail");
			this.sendJson(data);
		}
		data.setReturncode(200);
		data.setReturndata(user);
		this.sendJson(data);
	}
	public void delete() throws IOException {
		JSONReturnData data = new JSONReturnData("");
		String id = PubInfo.getString(this.getRequest().getParameter("id"));
		int int1 = UserService.delUser(id);
		if (int1 == -1) {
			data.setReturncode(500);
			data.setReturndata("fail");
			this.sendJson(data);
			return;
		}
		data.setReturncode(200);
		data.setReturndata(id);
		this.sendJson(data);
	}

	public ModelAndView toEdit() {
		String userid = PubInfo.getString(this.getRequest().getParameter("userid"));
		User user = UserService.getUserInfo(userid);
		Department dep = DepartmentService.getDepartment(user.getDepcode());
		return new ModelAndView("/WEB-INF/jsp/management/person/edit").addObject("user", user).addObject("department",dep.getCname());
	}
	/**
	 * 校验用户id是否存在
	 * @throws IOException
	 */
	public void checkUserId() throws IOException {
		String userid = PubInfo.getString(this.getRequest().getParameter("userId"));
		User user = UserService.getUserInfo(userid);
		JSONReturnData data = new JSONReturnData("");		
		if (user == null) {
			data.setReturncode(200);
			this.sendJson(data);
			return;
		} else {
			data.setReturncode(300);
			this.sendJson(data);
			return;
		}
	}
	/**
	 * 校验邮箱是否存在
	 * @param userId 用户Id
	 * @param email email
	 * @throws IOException
	 */
	public void checkEmail() throws IOException {
		String userid = PubInfo.getString(this.getRequest().getParameter("userid"));
		String email = PubInfo.getString(this.getParameter(this.getRequest(), "email"));
		List<User> userList = UserService.findUserByEmail(email);
		JSONReturnData data = new JSONReturnData("");		
		if (userList.size()==0 ||(userList.size()==1 && userList.get(0).getUserid().equals(userid))){
			data.setReturncode(200);
			this.sendJson(data);
			return;
		} else {
			data.setReturncode(300);
			this.sendJson(data);
			return;
		}
	}
	/**
	 * 用户角色绑定
	 * 
	 * @param userId
	 *            用户Id
	 * @param values
	 *            角色编码数组
	 * @throws IOException
	 */
	public void updateRoletoUser() throws IOException {
		HttpServletRequest req = this.getRequest();
		String userId = req.getParameter("userId");
		String values = req.getParameter("values");
		JSONReturnData data = new JSONReturnData("");
		try {
			Integer result = UserService.updateUserToRole(userId, values);
		} catch (Exception e) {
			e.printStackTrace();
			data.setReturncode(501);
		}
		this.sendJson(data);
	}
	public ModelAndView toeditme() {
		User user = UserService.getCurrentUser();
		return new ModelAndView("").addObject("user", user);
	}

	public void updatePwd() throws IOException {
		JSONReturnData data = new JSONReturnData("");
		String userid = PubInfo.getString(this.getRequest().getParameter("userid"));
		String pwd = PubInfo.getString(this.getRequest().getParameter("newpwd"));
		
        pwd=PubInfo.getHASHcode(pwd);
		int int1=UserService.updateUserPwd(userid, pwd);
		if (int1 == -1) {
			data.setReturncode(500);
			data.setReturndata("fail");
			this.sendJson(data);
			return;
		}
		data.setReturncode(200);
		data.setReturndata(userid);
		this.sendJson(data);
	}
	/**
	 * 查询用户角色树
	 * @param userId  用户Id
	 * @throws IOException
	 */
	public void findRoletoUser() throws IOException {
		String id = this.getRequest().getParameter("userId");
		List<ZTreeNode> roleList =UserService.getRoleTree(id);
		this.sendJson(roleList);
	}
	public void toExcel() throws IOException{
		String depcode = PubInfo.getString(this.getRequest().getParameter("depcode"));
		String findvalue = PubInfo.getString(this.getRequest().getParameter("code"));
		List<User> listdata = UserService.Finduser(depcode, findvalue);
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
			User user = listdata.get(i);
			dr1 = sheet1.addRow();
			cell2 = cell1.clone();
			cell2.setCellValue(user.getUserid());
			dr1.set(0, cell2);
			cell2 = cell1.clone();
			cell2.setCellValue(user.getUsername());
			dr1.set(1, cell2);
		}
		book.getSheets().add(sheet1);
		HttpServletResponse resp = this.getResponse();
		resp.reset();
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Pragma", "public");
		resp.setHeader("Cache-Control", "max-age=30");
		resp.addHeader("Content-Disposition", "attachment; filename=" + "person.xlsx");
		try {
			book.saveExcel(resp.getOutputStream(), XLSTYPE.XLSX);
		} catch (ExcelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	 * @param request 请求类
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
}
