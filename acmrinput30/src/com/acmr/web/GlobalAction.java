package com.acmr.web;

import java.io.IOException;

import acmr.web.core.BaseGlobal;

import com.acmr.service.security.SafeService;

public class GlobalAction extends BaseGlobal {

	@Override
	public boolean actionLoad() {
		String path1 = this.getRequest().getRequestURI();
		path1 = path1.substring(this.getRequest().getContextPath().length());
		if (path1.toLowerCase().indexOf("/api/") == 0||path1.toLowerCase().indexOf("/ssologin.htm") == 0||path1.toLowerCase().indexOf("/login.htm") == 0) {
			return true;
		}
		boolean mark1 = false;
		try {
			mark1 = SafeService.checkUserRight("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mark1;
	}

	@Override
	public void context_Created() {
		super.context_Created();
	}

	
	
}
