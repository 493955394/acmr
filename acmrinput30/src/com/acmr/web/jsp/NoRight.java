package com.acmr.web.jsp;

import java.io.IOException;

import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;

public class NoRight extends BaseAction {

	@Override
	public ModelAndView main() throws IOException {
		String str1 = "no right";
		this.getResponse().getWriter().print(str1);
		return null;
	}

}
