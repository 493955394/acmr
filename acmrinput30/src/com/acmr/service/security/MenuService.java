package com.acmr.service.security;

import java.util.List;

import acmr.util.ListHashMap;
import acmr.util.WebConfig;
import acmr.web.core.CurrentContext;

import com.acmr.model.security.Menu;

public class MenuService {

	public static String getUserMenu(ListHashMap<Menu> menus) {
		String restr = "\n";
		List<String> strmenus = menus.get("").getChilds();
		for (String code : strmenus) {
			Menu menu = menus.get(code);
			if (menu.getSort().equals("1")) {

				if (menu.getChilds().size() > 0) {
					restr += "<li class=\"dropdown\"><a href=\"javascript:;\" class=\"dropdown-toggle\" " + getHref(menu.getHref(), menu.getTarget()) + " data-toggle=\"dropdown\" aria-expanded=\"false\">" + menu.getCname() + "<span class=\"caret\"></span>";
				} else {
					restr += "<li><a " + getHref(menu.getHref(), menu.getTarget()) + ">" + menu.getCname();
				}
				restr += "</a>\n";

				if (menu.getChilds().size() > 0) {
					restr += "						<ul class=\"dropdown-menu multi-level\">\n";
					// 二级菜单
					for (String code1 : menu.getChilds()) {
						// response.getWriter().print(code1);
						Menu menu1 = menus.get(code1);
						if (menu1 == null) {
							continue;
						}
						if (menu1.getChilds().size() > 0) {
							restr += "							<li class=\"dropdown-submenu\">\n";
						} else {
							restr += "							<li>";
						}
						if (menu1.getHref().equals("")) {
							restr += " <a href=\"#\">" + menu1.getCname() + "</a>";
						} else {
							restr += " <a";
							restr += " " + getHref(menu1.getHref(), menu1.getTarget()) + ">" + menu1.getCname() + "</a>";
						}
						if (menu1.getChilds().size() > 0) {
							restr += "							<ul class=\"dropdown-menu\">";
							// 三级菜单
							for (String code2 : menu1.getChilds()) {
								Menu menu2 = menus.get(code2);
								if (menu2.getHref().equals("")) {
									restr += "									<li><a tabindex=\"-1\"";
									restr += " href=\"#\">" + menu2.getCname() + "</a></li>";
								} else {
									restr += " 									<li><a tabindex=\"-1\"";
									restr += " " + getHref(menu2.getHref(), menu2.getTarget()) + ">" + menu2.getCname() + "</a></li>";
								}
							}
							restr += "						</ul>\n";
						}
						restr += "					</li>\n";
					}
					restr += "				</ul>\n";
				}
				restr += "				</li>\n";

			}
		}
		return restr;
	}

	public static String getSystemMenu(ListHashMap<Menu> menuList) {
		StringBuffer strMenu = new StringBuffer("");
		// strMenu.append("<div class=\"col-sm-12\">");
		for (int i = 0; i < menuList.size(); i++) {
			Menu menu = menuList.get(i);
			if ("2".equals(menu.getSort()) && "".equals(menu.getParent())) {
				strMenu.append("<div class=\"findTitle\"><span>");
				strMenu.append(menu.getCname());
				strMenu.append("</span></div><div class=\"gap4\"><div class=\"findContent theme" + i + " clearfix\">");
				for (int j = 0; j < menu.getChilds().size(); j++) {
					Menu child = menuList.get(menu.getChilds().get(j));
 					strMenu.append("<a "+getHref(child.getHref(),child.getTarget())+ ">");
					strMenu.append("<img src=\"" + CurrentContext.getContextPath() + "/images/systemicon/" + child.getCode() + ".png\">");
					strMenu.append("<span>" + child.getCname() + "</span></a>");
					strMenu.append("\n");
				}
				strMenu.append("</div></div>");
			}
		}
		 
		return strMenu.toString();
	}

	private static String getHref(String href1, String target) {
		if (href1 == null || href1.equals("")) {
			return "";
		}
		String str1 = "";
		if (href1.toLowerCase().indexOf("http://") == 0) {
			str1 = href1;
		} else {
			String input = CurrentContext.getContextPath();
			String pub = WebConfig.factor.getInstance().getPropertie("login.properties", "puburl");
			str1 = href1.replace("[input]", input).replace("[pub]", pub);
			if(str1.equals(href1)){
				str1=input + str1;
			}

		}
		str1 = "href=\"" + str1 + "\" ";

		if (!target.equals("")) {
			str1 += " target=\"" + target + "\"";
		}
		return str1;
	}

}
