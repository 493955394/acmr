package com.acmr.web.jsp;

import java.io.IOException;

import com.acmr.model.security.Menu;
import com.acmr.model.security.Right;
import com.acmr.model.security.User;
import com.acmr.service.security.SafeService;
import com.acmr.service.security.UserService;
import com.acmr.service.ssologin.SSOService;
import com.alibaba.fastjson.JSONObject;

import acmr.util.ListHashMap;
import acmr.util.PubInfo;
import acmr.util.WebConfig;
import acmr.util.returnData;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;

public class ssoLogin extends BaseAction {

	@Override
	public ModelAndView main() {
		String reurl = PubInfo.getString(this.getRequest().getParameter("returnurl"));
		String tid = PubInfo.getString(this.getRequest().getParameter("tid"));

		if (reurl.equals("") || reurl.equals(this.getContextPath() + "/")) {
			reurl = this.getRequest().getContextPath() + "/index.htm";
		}

		if (!tid.equals("")) {
			returnData rd = SSOService.getInstance().getUserAid(tid);
			if (rd.getCode() == 200) {
				String aid = (String) rd.getData();
				this.getSession().setAttribute("aid", aid);
			}
		}
		String aid = PubInfo.getString(this.getSession().getAttribute("aid"));
		if (!aid.equals("")) {
			returnData rd = SSOService.getInstance().checklogin(aid);
			if (rd.getCode() == 200) {
				JSONObject data1 = (JSONObject) rd.getData();
				this.getSession().setAttribute("userid", data1.getString("userid"));
				saveUser(data1.getString("userid"));
				try {
					this.getResponse().sendRedirect(reurl);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}
		reurl = this.getRequest().getRequestURL().toString() + "?returnurl=" + reurl;
		String url = WebConfig.factor.getInstance().getPropertie("login.properties", "ssologinurl") + "login.aspx?returnurl=" + acmr.util.PubInfo.UrlEncode(reurl);
		try {
			this.getResponse().sendRedirect(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private void saveUser(String userid) {
		User user = UserService.getUserInfo(userid);
		this.getSession().setAttribute("loginuser", user);
		ListHashMap<Right> rights = SafeService.getUserRight(user);
		ListHashMap<Menu> menus = SafeService.getUserMenu(rights);
		this.getSession().setAttribute("userright", rights);
		this.getSession().setAttribute("usermenu", menus);
	}

	public void LogOut() throws IOException {
		String reurl = PubInfo.getString(this.getRequest().getParameter("returnurl"));
		if (reurl.equals("") || reurl.equals(this.getContextPath() + "/")) {
			reurl = this.getRequest().getContextPath() + "/index.htm";
		}
		String server = this.getRequest().getScheme() + "://" + this.getRequest().getServerName();
		if (this.getRequest().getServerPort() != 80) {
			server += ":" + this.getRequest().getServerPort();
		}
		reurl = server + reurl;
		this.getSession().invalidate();
		String url = WebConfig.factor.getInstance().getPropertie("login.properties", "ssologinurl") + "loginout.aspx?returnurl=" + acmr.util.PubInfo.UrlEncode(reurl);
		PubInfo.printStr(url);
		this.getResponse().sendRedirect(url);
	}
}
