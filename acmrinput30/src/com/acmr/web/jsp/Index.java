package com.acmr.web.jsp;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import acmr.util.ListHashMap;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;

import com.acmr.helper.util.StringUtil;
import com.acmr.model.dataapprove.FlowInstanceNode;
import com.acmr.model.dataload.DataLoad;
import com.acmr.model.pub.PageBean;
import com.acmr.model.security.Right;

public class Index extends BaseAction {

	@Override
	public ModelAndView main() throws IOException {
		HttpSession session = this.getSession();
		String pjax = this.getRequest().getHeader("X-PJAX");
		ListHashMap<Right> rights = (ListHashMap<Right>) session
				.getAttribute("userright");
		boolean permissionDataManager = rights.get("jz") != null ? true : false;
		boolean permissionDataReview = rights.get("sp") != null ? true : false;
		ResultList result = new ResultList();
		// DataLoadService dataloadService =
		// DataLoadService.factor.getInstance();
		// if(permissionDataManager){
		// PageBean<DataLoad> page = new PageBean<DataLoad>();
		// DataLoad dataLoad = new DataLoad();
		// dataLoad.setChoosetabname("4");
		// result.setWritePage(dataloadService.findBypage(page, dataLoad));
		// }
		// if(permissionDataReview){
		// PageBean<FlowInstanceNode> page = new PageBean();
		// FlowInstanceNode node = new FlowInstanceNode();
		// node.setUserId(UserService.getCurrentUser().getUserid());
		// node.setCurrentState("1");
		// DataApproveService dataApproveService =
		// DataApproveService.factor.getInstance();
		// page = dataApproveService.findBypage(page, node);
		// result.setReadPage(page);
		// }
		PageBean<DataLoad> page = new PageBean();
		DataLoad dataLoad = new DataLoad();
		dataLoad.setChoosetabname("5");
		// result.setAllPage(dataloadService.findBypage(page, dataLoad));
		if (StringUtil.isEmpty(pjax)) {
			return new ModelAndView("/WEB-INF/jsp/index")
					.addObject("list", result)
					.addObject("permission1", permissionDataManager)
					.addObject("permission2", permissionDataReview)
					.addObject("pageType", "index");
		} else {
			return new ModelAndView("/WEB-INF/jsp/tabList")
					.addObject("list", result)
					.addObject("permission2", permissionDataManager)
					.addObject("pageType", "index");
		}

	}

	public class ResultList {
		// 待审批任务
		private PageBean<FlowInstanceNode> readPage;
		// 待填报任务
		private PageBean<DataLoad> writePage;
		// 全部任务
		private PageBean<DataLoad> allPage;

		public PageBean<FlowInstanceNode> getReadPage() {
			return readPage;
		}

		public void setReadPage(PageBean<FlowInstanceNode> readPage) {
			this.readPage = readPage;
		}

		public PageBean<DataLoad> getWritePage() {
			return writePage;
		}

		public void setWritePage(PageBean<DataLoad> writePage) {
			this.writePage = writePage;
		}

		public PageBean<DataLoad> getAllPage() {
			return allPage;
		}

		public void setAllPage(PageBean<DataLoad> allPage) {
			this.allPage = allPage;
		}
	}

}
