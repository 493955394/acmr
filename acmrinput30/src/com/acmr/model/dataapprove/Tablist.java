package com.acmr.model.dataapprove;

import com.acmr.model.pub.PageBean;

public class Tablist {
	
	private PageBean<FlowInstanceNode> allPage;
	
	private PageBean<FlowInstanceNode> updatePage;

	public PageBean<FlowInstanceNode> getAllPage() {
		return allPage;
	}

	public void setAllPage(PageBean<FlowInstanceNode> allPage) {
		this.allPage = allPage;
	}

	public PageBean<FlowInstanceNode> getUpdatePage() {
		return updatePage;
	}

	public void setUpdatePage(PageBean<FlowInstanceNode> updatePage) {
		this.updatePage = updatePage;
	}

	
}
	
