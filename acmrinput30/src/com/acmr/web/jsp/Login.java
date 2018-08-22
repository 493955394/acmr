package com.acmr.web.jsp;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import acmr.util.ListHashMap;
import acmr.util.PubInfo;
import acmr.util.WebConfig;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;

import com.acmr.model.security.Menu;
import com.acmr.model.security.Right;
import com.acmr.model.security.User;
import com.acmr.service.LogService;
import com.acmr.service.security.SafeService;
import com.acmr.service.security.UserService;

public class Login extends BaseAction {

	@Override
	public ModelAndView main() throws IOException {
		String returnurl = PubInfo.getString(this.getRequest().getParameter("returnurl"));
		if (returnurl.equals("") || returnurl.equals(this.getContextPath() + "/")) {
			returnurl = this.getContextPath() + "/system/menu.htm";
		}
		User user = (User) this.getSession().getAttribute("loginuser");
		if (user != null) {
			this.getResponse().sendRedirect(returnurl);
			return null;
		}
		if (this.getRequest().getMethod().equalsIgnoreCase("post")) {
			return login(returnurl);
		}
		return new ModelAndView("/WEB-INF/jsp/login");
	}

	private ModelAndView login(String returnurl) throws IOException {
		HttpServletRequest request = this.getRequest();
		String email = PubInfo.getString(request.getParameter("email"));
		String pwd = PubInfo.getString(request.getParameter("pwd"));
		// String screen
		// =PubInfo.getString(this.getRequest().getParameter("screen"));
		int state = 200;
		String msg = returnurl;
		if (email.equals("") || pwd.equals("")) {
			state = 401;
			msg = "用户名或密码不能为空!";
		}
		if (state == 200) {
			state = SafeService.checkUserLogin(email, pwd);
			if (state != 200) {
				msg = "用户或密码不正确!";
			}
		}
		if (state == 200) {
			saveUser(email);
		}

		ModelAndView mv = null;
		if (state == 200) {
		
			LogService.logSessionStart(request, "", "访问");
			this.getResponse().sendRedirect(returnurl);
		} else {
			mv = new ModelAndView("/WEB-INF/jsp/login").addObject("msg", msg);
		}
		return mv;
	}

	private void saveUser(String userid) {
		User user = UserService.getUserInfo(userid);
		this.getSession().setAttribute("loginuser", user);
		ListHashMap<Right> rights = SafeService.getUserRight(user);
		ListHashMap<Menu> menus = SafeService.getUserMenu(rights);
		this.getSession().setAttribute("userright", rights);
		this.getSession().setAttribute("usermenu", menus);
	}

	public void logout() throws IOException {
		this.getSession().setAttribute("loginuser", null);
		this.getSession().invalidate();
		if(WebConfig.factor.getInstance().getPropertie("login.properties", "ssologin").equalsIgnoreCase("yes")){
			this.getResponse().sendRedirect("ssologin.htm?m=logout");
			return ;
		}
		String url = "login.htm";
		this.getResponse().sendRedirect(url);
	}
}
