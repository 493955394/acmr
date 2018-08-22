package com.acmr.web.jsp.system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

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
import com.acmr.model.security.Role;
import com.acmr.model.security.User;
import com.acmr.model.templatecenter.TreeNode;
import com.acmr.service.security.RoleService;
import com.acmr.service.security.UserService;

public class role extends BaseAction {
	@Override
	public ModelAndView main(){
		String pjax = PubInfo.getString(this.getRequest().getHeader("X-PJAX"));
		AcmrDataPageDao<Role> page = new AcmrDataPageDao<Role>();
		List<Role> listdata = RoleService.findRole("");
		page.findPage(listdata);
		String url = this.getUrl(this.getRequest());
		url+= "?m=find";
		page.getPagebean().setUrl(url);
		if (pjax.equals("")) {
			return new ModelAndView("/WEB-INF/jsp/management/role/index").addObject("page", page.getPagebean()).addObject("url", url);
		} else {
			return new ModelAndView("/WEB-INF/jsp/management/role/tabList").addObject("page", page.getPagebean()).addObject("url", url);

		}
	}
	/**
	 * 根据条件查找角色信息
	 * @param code 组织编码
	 * @param selId 选择Id
	 * @throws IOException
	 */
	public ModelAndView find() throws IOException {
		HttpServletRequest request = this.getRequest();
		// 判断是否为pjax请求
		String pjax = PubInfo.getString(request.getHeader("X-PJAX"));
		String findValue = PubInfo.getString(this.getParameter(request, "findvalue"));
		AcmrDataPageDao<Role> page = new AcmrDataPageDao<Role>();
		List<Role> listdata = RoleService.findRole(findValue);
		page.findPage(listdata);
		
		// 执行查询
		String url = this.getUrl(this.getRequest());
		page.getPagebean().setUrl(url);
		if (pjax.equals("")) {
			return new ModelAndView("/WEB-INF/jsp/management/role/index").addObject("page", page.getPagebean()).addObject("url", url);
		} else {
			return new ModelAndView("/WEB-INF/jsp/management/role/tabList").addObject("page", page.getPagebean()).addObject("url", url);
		}
	}
	public void toExcel() throws IOException {
		HttpServletRequest request = this.getRequest();
		String findValue = PubInfo.getString(this.getParameter(request, "findvalue"));
		List<Role> listdata = RoleService.findRole(findValue);
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
			Role role = listdata.get(i);
			dr1 = sheet1.addRow();
			cell2 = cell1.clone();
			cell2.setCellValue(role.getCode());
			dr1.set(0, cell2);
			cell2 = cell1.clone();
			cell2.setCellValue(role.getCname());
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
	/**
	 * 编辑角色界面跳转
	 * @param code 角色编码
	 */
	public ModelAndView toEdit() {
		String code = PubInfo.getString(this.getParameter(this.getRequest(), "code"));
		Role role = RoleService.getRole(code);
		return new ModelAndView("/WEB-INF/jsp/management/role/edit").addObject("role",role);
	}
	/**
	 * 添加角色界面跳转
	 */
	public ModelAndView toAdd() {
		return new ModelAndView("/WEB-INF/jsp/management/role/add");
	}
	/**
	 * 修改角色状态
	 * @param code 角色编码
	 * @throws IOException 
	 */
	public void delete() throws IOException {
		HttpServletRequest request = this.getRequest();
		String code = request.getParameter("code");
		Integer result = 0;
		JSONReturnData data = new JSONReturnData("");
		result = RoleService.switchState(code);
		if(result > 0){
			data.setReturncode(200);
		}else{
			data.setReturncode(300);
		}
		this.sendJson(data);
	}
	/**
	 * 获取权限树
	 * @param roleCode 角色编码
	 */
	public void getRightTree()throws IOException{
		HttpServletRequest request = this.getRequest();
		String roleCode=request.getParameter("rolecode");
		List<ZTreeNode> menus =RoleService.getRightTree(roleCode);		
		// 构造返回对象
		this.sendJson(menus);
	}
	/**
	 * 设置权限树
	 * @param roleCode 角色编码
	 */
	public void setRightTree()throws IOException{
		HttpServletRequest request = this.getRequest();
		String roleCode=request.getParameter("rolecode");
		String rights =request.getParameter("rightnodes");
		RoleService.setRoleRight(roleCode, rights);
		JSONReturnData data = new JSONReturnData("");
		data.setReturncode(200);
		// 构造返回对象
		this.sendJson(data);
	}
	/**
	 * 取得机构人员树
	 * @param deptId 部门编码
	 * @param rolecode 角色编码
	 */
	public void getPersonTree() throws IOException{
		HttpServletRequest request = this.getRequest();
//		String deptId = request.getParameter("id");
		String roleCode = request.getParameter("rolecode");
		List<ZTreeNode> users = UserService.getSubUsersRole(roleCode);

		this.sendJson(users);
	}
	
	/**
	 * 取得机构人员树
	 * @param deptId 部门编码
	 * @param rolecode 角色编码
	 */
	public void getRolePersonTree() throws IOException{
		HttpServletRequest req = this.getRequest();
		String deptId = req.getParameter("id");
		String rolecode = req.getParameter("rolecode");
		
		List<TreeNode> users = RoleService.findDepUserTree(rolecode, deptId);
		this.sendJson(users);
	}
	
	/**
	 * 插入角色
	 */
	public void insert()throws IOException {		
		// 构造插入数据
		HttpServletRequest request = this.getRequest();
		Role role=new Role();
		User user = new User();
		Map map = request.getParameterMap();
		try {
			BeanUtils.populate(role, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 构造返回对象
		JSONReturnData data = new JSONReturnData("");
		role.setUpdatetime(new Date(System.currentTimeMillis()));
		role.setUpdateuserid(UserService.getCurrentUser().getUserid());
		role.setState("0");
		// 执行插入
		Integer result = RoleService.addRole(role);
		if (result == 0) {
			data.setReturncode(501);
			data.setReturndata("fail");
			return;
		}
		data.setReturncode(200);
		data.setParam1(role.getCode());
		this.sendJson(data);
	}
	/**
	 * 更新角色
	 */
	public void update()throws IOException {
		// 构造插入数据
		// 构造插入数据
		HttpServletRequest request = this.getRequest();
		Role role=new Role();
		Map map = request.getParameterMap();
		try {
			BeanUtils.populate(role, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 构造返回对象
		JSONReturnData data = new JSONReturnData("");
		role.setUpdatetime(new Date(System.currentTimeMillis()));
		role.setUpdateuserid(UserService.getCurrentUser().getUserid());
		role.setState("0");
		// 执行插入
		Integer result = RoleService.updateRoleInfo(role);
		if (result == 0) {
			data.setReturncode(501);
			data.setReturndata("fail");
			return;
		}
		data.setReturncode(200);
		data.setParam1(role.getCode());
		this.sendJson(data);
	}
	/**
	 * 绑定菜单
	 * @param rolecode 角色编码
	 * @param insertNodes 选中菜单编码数组
	 */
	public void setMenuTree()throws IOException{
		HttpServletRequest request = this.getRequest();
		String roleCode = request.getParameter("rolecode");
		String insertNodes = request.getParameter("insertMenuNodes");
		JSONReturnData data = new JSONReturnData("");
		int result =RoleService.setRoleRight(roleCode,insertNodes);
		if(result>0){
			data.setReturncode(200);
		}else{
			data.setReturncode(300);
		}
		this.sendJson(data);
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
	/**
	 * 新增 校验
	 * @param code 角色编码
	 * @throws IOException 
	 */
	public void checkCode() throws IOException{
		HttpServletRequest reqquest = this.getRequest();
		String code= reqquest.getParameter("code");
		JSONReturnData data = new JSONReturnData("");
		Role roleTest= RoleService.getRole(code);
		if(roleTest!=null){
			data.setReturncode(300);
			data.setReturndata("fail");
			this.sendJson(data);
			return;
		}
		data.setReturncode(200);
		this.sendJson(data);
	}
	
	/**
	 * 更新用户角色关系
	 * @param rolecode 角色编码
	 * @param insertNodes 新增用户编码数组
	 * @param delNodes 删除用户编码数组
	 * @throws IOException
	 */
	public void updateUserToRole() throws IOException{
		HttpServletRequest req = this.getRequest();
		String rolecode = req.getParameter("rolecode");
		String insertNodes = req.getParameter("insertNodes");
		String delNodes = req.getParameter("delNodes");
		
		String[] deleteUserids = null; 
		String[] insertUserids = null;
		Integer result=0;
		if (!StringUtil.isEmpty(insertNodes)) {
			insertUserids = insertNodes.split(",");
			result+=insertNodes.length();
		}
		if (!StringUtil.isEmpty(delNodes)) {
			deleteUserids = delNodes.split(",");
			result+=delNodes.length();
		}
		
		JSONReturnData data = new JSONReturnData("");
		try {
			List<Object> list =new ArrayList<Object>();
			Role role=new Role();
			role.setCode(rolecode);
			list.add(role);
			list.add(insertNodes);
			list.add(delNodes);
			RoleService.updateUserToRole(rolecode, deleteUserids, insertUserids);
			//LogService.logOperation(req, LogConst.COLUMN_ROLE,LogConst.EXCSEC_BIND, null,result.toString(), list, rolecode, insertNodes, delNodes, "UserToRole", "true", null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			data.setReturncode(501);
			List<Object> list =new ArrayList<Object>();
			Role role=new Role();
			role.setCode(rolecode);
			list.add(role);
			list.add(insertNodes);
			list.add(delNodes);
			//LogService.logOperation(req, LogConst.COLUMN_ROLE,LogConst.EXCSEC_BIND, null,"0", list, rolecode, insertNodes, delNodes, "UserToRole", "false", null, null, null, null, null);
		}
		this.sendJson(data);
	}
}
