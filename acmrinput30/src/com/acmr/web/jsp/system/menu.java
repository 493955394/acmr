package com.acmr.web.jsp.system;

import java.io.IOException;

import acmr.util.PubInfo;
import com.acmr.model.security.Menu;
import com.acmr.service.security.MenuService;

import acmr.util.ListHashMap;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;

public class menu extends BaseAction {
	public ModelAndView main() throws IOException {
		@SuppressWarnings("unchecked")
		ListHashMap<Menu> menus = (ListHashMap<Menu>) this.getSession().getAttribute("usermenu");
		String menuList = MenuService.getSystemMenu(menus);
		return new ModelAndView("/WEB-INF/jsp/management/menu/index").addObject("menulist",menuList);
	}
}
