package com.acmr.web.api;

import java.io.IOException;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;

import com.acmr.model.security.User;
import com.acmr.service.security.UserService;
import com.acmr.service.theme.ThemeService;

public class lanmu extends BaseAction {

	private ThemeService themeService = ThemeService.factor.getInstance();

	public void getCurrentRight() throws IOException {
		String userid = PubInfo.getString(this.getRequest().getParameter("userid"));
		User user = UserService.getUserInfo(userid);
		this.sendJson(themeService.getAllTopmenuRight(user));
	}

	public void cansee() throws IOException {
		String code = PubInfo.getString(this.getRequest().getParameter("code"));
		String userid = PubInfo.getString(this.getRequest().getParameter("userid"));
		User user = UserService.getUserInfo(userid);
		this.sendJson(themeService.cansee(code, user));
	}

	// 获取人员树
	public void findPersonTree() throws IOException {
		// 获取查询数据
		String id = PubInfo.getString(this.getRequest().getParameter("id"));
		// 发送返回数据
		this.sendJson(UserService.getSubUsers(PubInfo.getString(id), false));
	}
}
