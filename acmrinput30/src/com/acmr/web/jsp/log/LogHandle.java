package com.acmr.web.jsp.log;

import com.acmr.helper.constants.Const;

import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;

public class LogHandle extends BaseAction {

	
	public ModelAndView main() {

		return new ModelAndView("/WEB-INF/jsp/log/index").addObject("url", Const.WEBLOGURL);
	}
}
