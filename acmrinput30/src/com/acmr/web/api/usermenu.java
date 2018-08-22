package com.acmr.web.api;

import java.io.IOException;

import com.acmr.model.security.Menu;
import com.acmr.model.security.User;
import com.acmr.service.security.SafeService;
import com.acmr.service.security.UserService;
import com.alibaba.fastjson.JSON;

import acmr.util.ListHashMap;
import acmr.util.PubInfo;
import acmr.util.returnData;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;

public class usermenu extends BaseAction {

	public void getmenus() throws IOException {
		this.getResponse().setContentType("text/json;charset=utf-8");

		String userid = PubInfo.getString(this.getRequest().getParameter("userid"));

		User user = UserService.getUserInfo(userid);
		ListHashMap<Menu> menus = SafeService.getUserMenu(user);
		returnData re = new returnData(JSON.toJSONString(menus));
		this.sendJson(re);

	}

}
