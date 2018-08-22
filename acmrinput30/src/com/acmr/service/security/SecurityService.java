package com.acmr.service.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.ListHashMap;
import acmr.util.PubInfo;
import acmr.util.returnData;
import acmr.web.core.CurrentContext;

import com.acmr.dao.security.SecurityDao;
import com.acmr.model.pub.ZTreeNode;
import com.acmr.model.security.Department;
import com.acmr.model.security.Menu;
import com.acmr.model.security.Right;
import com.acmr.model.security.Role;
import com.acmr.model.security.User;
import com.acmr.service.ssologin.SSOService;

class SecurityService {

	public static class fator {
		private static SecurityService ss = new SecurityService();

		public static SecurityService getInstance() {
			return ss;
		}
	}

	private ListHashMap<Department> deps;
	private ListHashMap<Role> roles;
	private ListHashMap<Right> rights;
	private ListHashMap<Menu> menus;

	private SecurityService() {
		this.loadDeps();
		this.loadRole();
		this.loadRight();
		this.loadMenu();
	}

	public ListHashMap<Department> getDeps() {
		return deps;
	}

	public ListHashMap<Role> getRoles() {
		return roles;
	}

	public ListHashMap<Right> getRights() {
		return rights;
	}

	public ListHashMap<Menu> getMenus() {
		return menus;
	}

	public int moveDepartment(String code1, String code2) {
		if (!deps.containsKey(code1) || !deps.containsKey(code2)) {
			return -1;
		}
		String scode1 = deps.get(code2).getSortcode();
		String scode2 = deps.get(code1).getSortcode();
		int int1 = SecurityDao.Fator.getInstance().getSecurityDao().moveDep(code1, code2, scode1, scode2);
		if (int1 > 0) {
			this.loadDeps();
		}
		return int1;
	}

	public int updateDepartment(Department dep) {
		int int1 = SecurityDao.Fator.getInstance().getSecurityDao().updateDep(dep);
		if (int1 > 0) {
			this.loadDeps();
		}
		return int1;
	}

	public int updateUser(User user) {
		return SecurityDao.Fator.getInstance().getSecurityDao().updateUser(user);
	}

	public int updateUserPwd(String userid, String pwd) {
		return SecurityDao.Fator.getInstance().getSecurityDao().updateUserPwd(userid, pwd);
	}

	public int updateRole(Role role) {
		int int1 = SecurityDao.Fator.getInstance().getSecurityDao().updateRole(role);
		if (int1 > 0) {
			this.loadRole();
		}
		return int1;
	}
	
	public int updateRoleInfo(Role role){
		int int1 = SecurityDao.Fator.getInstance().getSecurityDao().updateRoleInfo(role);
		if (int1 > 0) {
			this.loadRole();
		}
		return int1;
	}

	public int addDepartment(Department dep) {
		String sortcode = "00000" + deps.size();
		sortcode = sortcode.substring(sortcode.length() - 5);
		dep.setSortcode(sortcode);
		int int1 = SecurityDao.Fator.getInstance().getSecurityDao().addDep(dep);
		if (int1 > 0) {
			this.loadDeps();
		}
		return int1;
	}

	public int addRole(Role role) {
		String sortcode = "00000" + roles.size();
		sortcode = sortcode.substring(sortcode.length() - 5);
		role.setSortcode(sortcode);
		int int1 = SecurityDao.Fator.getInstance().getSecurityDao().addRole(role);
		if (int1 > 0) {
			this.loadRole();
		}
		return int1;
	}

	public int addUser(User user, String pwd) {
		return SecurityDao.Fator.getInstance().getSecurityDao().addUser(user, pwd);
	}

	public int delDepartment(String depcode) {
		int int1 = SecurityDao.Fator.getInstance().getSecurityDao().delDep(depcode);
		if (int1 > 0) {
			this.loadDeps();
		}
		return int1;
	}

	public int delUser(String userid) {
		return SecurityDao.Fator.getInstance().getSecurityDao().delUser(userid);
	}

	public int delRole(String code) {
		int int1 = SecurityDao.Fator.getInstance().getSecurityDao().delRole(code);
		if (int1 > 0) {
			this.loadRole();
		}
		return int1;
	}

	public User getCurrentUser() {
		return (User) CurrentContext.getSession().getAttribute("loginuser");
	}

	private boolean ssocheckIsLogin() throws IOException {
		String aid = (String) CurrentContext.getSession().getAttribute("aid");
		User user = (User) CurrentContext.getSession().getAttribute("loginuser");
		if (user != null) {
			returnData rd = SSOService.getInstance().checklogin(aid);
			if (rd.getCode() == 200) {
				return true;
			}
		}

		HttpServletRequest req = CurrentContext.getRequest();
		String returnurl = req.getRequestURI();
		String qstr = req.getQueryString();
		if (qstr != null) {
			returnurl += "?" + qstr;
		}
		String reurl = req.getContextPath() + "/ssologin.htm?returnurl=" + acmr.util.PubInfo.UrlEncode(returnurl);
		CurrentContext.getResponse().sendRedirect(reurl);

		return false;

	}

	@SuppressWarnings("unchecked")
	public boolean ssocheckUserRight(String rights) throws IOException {
		
		if(ssocheckIsLogin()==false){
			return false;
		}
		HttpSession session = CurrentContext.getSession();
		HttpServletResponse resq = CurrentContext.getResponse();
 		User user = (User) session.getAttribute("loginuser");
		ListHashMap<Right> rlists = (ListHashMap<Right>) session.getAttribute("userright");
		if (rlists == null) {
			rlists = this.getUserRight(user);
			session.setAttribute("userright", rlists);
		}
		String s[] = rights.split(",");
		boolean hasright = false;
		for (int i = 0; i < s.length; i++) {
			String right = s[i];
			if (rlists.containsKey(right)) {
				hasright = true;
				break;
			}
		}
		if (hasright == false) {
			// no right
			String url = CurrentContext.getContextPath() + "/norigh.htm";
			resq.sendRedirect(url);
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public int ssocheckUserRightMethod(String rights) throws IOException {

		if(ssocheckIsLogin()==false){
			return 501;
		}
		HttpSession session = CurrentContext.getSession();
		User user = (User) session.getAttribute("loginuser");
		ListHashMap<Right> rlists = (ListHashMap<Right>) session.getAttribute("userright");
		if (rlists == null) {
			rlists = this.getUserRight(user);
			session.setAttribute("userright", rlists);
		}
		String s[] = rights.split(",");
		boolean hasright = false;
		for (int i = 0; i < s.length; i++) {
			String right = s[i];
			if (rlists.containsKey(right)) {
				hasright = true;
				break;
			}
		}
		if (hasright == false) {
			// no right
			return 502;
		}
		return 200;
	}

	@SuppressWarnings("unchecked")
	public boolean checkUserRight(String rights) throws IOException {
		HttpSession session = CurrentContext.getSession();
		HttpServletResponse resq = CurrentContext.getResponse();
		HttpServletRequest req = CurrentContext.getRequest();
		String returnurl = req.getRequestURI();
		User user = (User) session.getAttribute("loginuser");
		if (user == null) {
			// no login
			String url = CurrentContext.getContextPath() + "/login.htm?returnurl=" + PubInfo.UrlEncode(returnurl);
			resq.sendRedirect(url);
			return false;
		}
		ListHashMap<Right> rlists = (ListHashMap<Right>) session.getAttribute("userright");
		if (rlists == null) {
			rlists = this.getUserRight(user);
			session.setAttribute("userright", rlists);
		}
		String s[] = rights.split(",");
		boolean hasright = false;
		for (int i = 0; i < s.length; i++) {
			String right = s[i];
			if (rlists.containsKey(right)) {
				hasright = true;
				break;
			}
		}
		if (hasright == false) {
			// no right
			String url = CurrentContext.getContextPath() + "/noright.htm";
			resq.sendRedirect(url);
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public int checkUserRightMethod(String rights) throws IOException {
		HttpSession session = CurrentContext.getSession();
		User user = (User) session.getAttribute("loginuser");
		if (user == null) {
			// no login
			return 501;
		}
		ListHashMap<Right> rlists = (ListHashMap<Right>) session.getAttribute("userright");
		if (rlists == null) {
			rlists = this.getUserRight(user);
			session.setAttribute("userright", rlists);
		}
		String s[] = rights.split(",");
		boolean hasright = false;
		for (int i = 0; i < s.length; i++) {
			String right = s[i];
			if (rlists.containsKey(right)) {
				hasright = true;
				break;
			}
		}
		if (hasright == false) {
			// no right
			return 502;
		}
		return 200;
	}

	public List<ZTreeNode> getsubDepartMent(String pcode) {
		List<ZTreeNode> list1 = new ArrayList<ZTreeNode>();
		if (deps.containsKey(pcode)) {
			Department dep = deps.get(pcode);
			for (int i = 0; i < dep.getChilds().size(); i++) {
				Department dep1 = deps.get(dep.getChilds().get(i));
				list1.add(new ZTreeNode(dep1.getCode(), dep1.getParent(), dep1.getCname(), dep1.getChilds().size() > 0));
			}
		}
		return list1;
	}

	public List<User> getDepUsers(String depcode) {
		List<User> lists = new ArrayList<User>();
		DataTable dt1 = SecurityDao.Fator.getInstance().getSecurityDao().getUsers(depcode);
		if (dt1 != null) {
			for (int i = 0; i < dt1.getRows().size(); i++) {
				DataTableRow dr1 = dt1.getRows().get(i);
				lists.add(this.getUser(dr1));
			}
		}
		return lists;
	}

	public List<User> getUserByEmail(String email) {
		List<User> lists = new ArrayList<User>();
		DataTable dt1 = SecurityDao.Fator.getInstance().getSecurityDao().getUsersByEmail(email);
		if (dt1 != null) {
			for (int i = 0; i < dt1.getRows().size(); i++) {
				DataTableRow dr1 = dt1.getRows().get(i);
				lists.add(this.getUser(dr1));
			}
		}
		return lists;
	}

	public User getUserInfo(String name) {
		DataTable dt1 = SecurityDao.Fator.getInstance().getSecurityDao().getUser(name);
		if (dt1 == null || dt1.getRows().size() == 0) {
			return null;
		}
		return getUser(dt1.getRows().get(0));
	}

	public int UserLogin(String name, String pwd) {
		int state = 200;
		DataTable dt1 = SecurityDao.Fator.getInstance().getSecurityDao().getUser(name);
		if (dt1 == null || dt1.getRows().size() == 0) {
			state = 501;
		}
		if (state == 200) {
			String ifclose = dt1.getRows().get(0).getString("ifclose");
			if (!ifclose.equals("0")) {
				state = 502;
			}
		}

		if (state == 200) {
			String pwd1 = dt1.getRows().get(0).getString("pwd");
			if (!PubInfo.getHASHcode(pwd).equals(pwd1)) {
				state = 502;
			}
		}
		return state;
	}

	public ListHashMap<Right> getUserRight(User user) {
		ListHashMap<Right> lists = new ListHashMap<Right>();
		lists.add(rights.get("").Copy());
		List<String> rs = user.getRoles();
		for (int i = 0; i < rs.size(); i++) {
			Role role = roles.get(rs.get(i));
			if (role != null) {
				for (int j = 0; j < role.getRights().size(); j++) {
					Right right = rights.get(role.getRights().get(j));
					addRight(lists, right.Copy());
				}
			}
		}
		return lists;
	}

	public ListHashMap<Menu> getUserMenu(ListHashMap<Right> rights) {
		ListHashMap<Menu> lists = new ListHashMap<Menu>();
		lists.add(menus.get("").Copy());
		for (int i = 0; i < rights.size(); i++) {
			List<String> ms1 = rights.get(i).getMenus();
			for (int j = 0; j < ms1.size(); j++) {
				Menu menu1 = menus.get(ms1.get(j));
				addMenu(lists, menu1.Copy());
			}
		}
		return lists;
	}

	private void addMenu(ListHashMap<Menu> lists, Menu r) {
		if (lists.containsKey(r.getCode())) {
			return;
		}
		lists.add(r);
		if (r.getCode().equals("")) {
			return;
		}
		String pcode = r.getParent();
		if (!lists.containsKey(pcode)) {
			addMenu(lists, menus.get(pcode).Copy());
		}
		lists.get(pcode).addChild(r.getCode(),r.getSortcode());
	}

	public ListHashMap<Menu> getUserMenu(User user) {
		ListHashMap<Right> right = getUserRight(user);
		return getUserMenu(right);
	}

	private void addRight(ListHashMap<Right> lists, Right r) {
		if (lists.containsKey(r.getCode())) {
			return;
		}
		lists.add(r);
		if (r.getCode().equals("")) {
			return;
		}
		String pcode = r.getParent();
		if (!lists.containsKey(pcode)) {
			addRight(lists, rights.get(pcode).Copy());
		}
		lists.get(pcode).addChild(r.getCode());
	}

	private User getUser(DataTableRow dr1) {
		if (dr1 == null) {
			return null;
		}
		User user = new User();
		user.setDepcode(dr1.getString("depcode"));
		user.setDuty(dr1.getString("duty"));
		user.setEmail(dr1.getString("email"));
		user.setFlowcode(dr1.getString("flowcode"));
		user.setFlowtype(dr1.getString("flowtype"));
		user.setSex(dr1.getString("sex"));
		user.setSortcode("0");
		user.setTel(dr1.getString("tel"));
		user.setUserid(dr1.getString("userid"));
		user.setUsername(dr1.getString("cname"));
		user.addRoles(dr1.getString("roles"), ",");
		user.setUpdatetime(dr1.getDate("updatetime"));
		user.setMemo(dr1.getString("memo"));
		user.setUpdateuserid(dr1.getString("updateuserid"));
		return user;
	}

	private void loadDeps() {
		deps = new ListHashMap<Department>();
		Department dep = new Department();
		dep.setCode("");
		deps.add(dep);
		DataTable dt1 = SecurityDao.Fator.getInstance().getSecurityDao().getDep();
		for (int i = 0; i < dt1.getRows().size(); i++) {
			DataTableRow dr1 = dt1.getRows().get(i);
			dep = new Department();
			dep.setCname(dr1.getString("cname"));
			dep.setCode(dr1.getString("code"));
			dep.setUpdateuserid(dr1.getString("updateuserid"));
			dep.setFlowcode(dr1.getString("flowcode"));
			dep.setFlowtype(dr1.getString("flowtype"));
			dep.setMemo(dr1.getString("memo"));
			dep.setParent(dr1.getString("procode"));
			dep.setSortcode(dr1.getString("sortcode"));
			dep.setUpdatetime(dr1.getDate("updatetime"));
			deps.add(dep);
		}
		for (int i = 0; i < deps.size(); i++) {
			dep = deps.get(i);
			String code = dep.getCode();
			if (code.equals("")) {
				continue;
			}
			String pcode = dep.getParent();
			Department pdep = deps.get(pcode);
			pdep.addChild(code);
		}
	}

	private void loadRole() {
		roles = new ListHashMap<Role>();
		Role r = new Role();
		DataTable dt1 = SecurityDao.Fator.getInstance().getSecurityDao().getRole();
		String state;
		for (int i = 0; i < dt1.getRows().size(); i++) {
			DataTableRow dr1 = dt1.getRows().get(i);
			r = new Role();
			r.setCname(dr1.getString("cname"));
			r.setCode(dr1.getString("code"));
			r.setSortcode(dr1.getString("sortcode"));
			r.addRights(dr1.getString("rights"), ",");
			if ("0".equals(dr1.getString("ifclose"))) {
				state = "启用";
			} else {
				state = "停用";
			}
			r.setState(state);
			r.setParent("");
			r.setMemo(dr1.getString("memo"));
			r.setUpdatetime(dr1.getDate("updatetime"));
			r.setUpdateuserid(dr1.getString("updateuserid"));
			roles.add(r);
		}
	}

	private void loadRight() {
		rights = new ListHashMap<Right>();
		Right r = new Right();
		r.setCode("");
		rights.add(r);
		DataTable dt1 = SecurityDao.Fator.getInstance().getSecurityDao().getRight();
		for (int i = 0; i < dt1.getRows().size(); i++) {
			DataTableRow dr1 = dt1.getRows().get(i);
			r = new Right();
			r.setCname(dr1.getString("cname"));
			r.setCode(dr1.getString("code"));
			r.setParent(dr1.getString("procode"));
			r.setSortcode(dr1.getString("sortcode"));
			r.addMenus(dr1.getString("menus"), ",");
			rights.add(r);
		}
		for (int i = 0; i < rights.size(); i++) {
			r = rights.get(i);
			String code = r.getCode();
			if (code.equals("")) {
				continue;
			}
			String pcode = r.getParent();
			Right pr = rights.get(pcode);
			pr.addChild(code);
		}
	}

	private void loadMenu() {
		menus = new ListHashMap<Menu>();
		Menu m = new Menu();
		m.setCode("");
		menus.add(m);
		DataTable dt1 = SecurityDao.Fator.getInstance().getSecurityDao().getMenu();
		for (int i = 0; i < dt1.getRows().size(); i++) {
			DataTableRow dr1 = dt1.getRows().get(i);
			m = new Menu();
			m.setCname(dr1.getString("cname"));
			m.setCode(dr1.getString("code"));
			m.setHref(dr1.getString("href"));
			m.setParent(dr1.getString("procode"));
			m.setSort(dr1.getString("sort"));
			m.setSortcode(dr1.getString("sortcode"));
			m.setTarget(dr1.getString("target"));
			menus.add(m);
		}
		for (int i = 0; i < menus.size(); i++) {
			m = menus.get(i);
			String code = m.getCode();
			if (code.equals("")) {
				continue;
			}
			String pcode = m.getParent();
			Menu pm = menus.get(pcode);
			pm.addChild(code,m.getSortcode());
		}
	}
}
