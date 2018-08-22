package com.acmr.web.jsp.metadata;


import java.io.IOException;

import acmr.cubeinput.service.MetaMulIndexService;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;

import com.acmr.helper.constants.Const;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.service.metadata.MulIndexThread;

public class MulIndex extends BaseAction{
	private MetaMulIndexService mulIndexService = MetaMulIndexService.factor.getInstance();
	public ModelAndView main() {
		int status = Const.customQueryStatus;
		return new ModelAndView("/WEB-INF/jsp/mulindex/mul").addObject("status", status);
	}
	
	public void createCustomTable(){
		new Thread(new MulIndexThread(mulIndexService)).start();
		JSONReturnData data = new JSONReturnData(200);
		try {
			this.sendJson(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
