package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.zhzs.IndexCategory;
import com.acmr.model.zhzs.IndexList;
import com.acmr.service.zhzs.IndexListService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

public class indexlist extends BaseAction {
    public ModelAndView main() throws IOException {
        String test="this is the listjps";
        ArrayList<IndexList> indexlist= new IndexListService().getIndexList();
        return new ModelAndView("/WEB-INF/jsp/zhzs/indexlist").addObject("test",test).addObject("indexlist",indexlist);
    }



}
