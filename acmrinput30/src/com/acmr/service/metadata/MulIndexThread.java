package com.acmr.service.metadata;

import acmr.cubeinput.service.MetaMulIndexService;

import com.acmr.helper.constants.Const;

public class MulIndexThread implements Runnable {

	private MetaMulIndexService mulIndexService;

	public MulIndexThread(MetaMulIndexService mulIndexService) {
		this.mulIndexService = mulIndexService;
	}

	@Override
	public void run() {
		Const.customQueryStatus = 1;
		mulIndexService.createTable();
		Const.customQueryStatus = 2;
	}

}
