package com.acmr.web.jsp.zbdata;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.zhzs.IndexCategory;
import com.acmr.model.zhzs.IndexList;
import com.acmr.service.zhzs.AddService;
import com.acmr.service.zhzs.IndexListService;
import com.sun.org.apache.xml.internal.resolver.Catalog;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class indexlist extends BaseAction {
    public ModelAndView main() throws IOException {
        String test="this is the listjps";
        ArrayList<IndexList> indexlist= new IndexListService().getIndexList();
        String code = PubInfo.getString(this.getRequest().getParameter("code"));
        String cname = PubInfo.getString(this.getRequest().getParameter("cname"));
        return new ModelAndView("/WEB-INF/jsp/zhzs/indexlist").addObject("test",test).addObject("indexlist",indexlist).addObject("code",code).addObject("cname",cname);
    }
    public void insert() throws IOException {
        IndexListService indexListService=new IndexListService();
        AddService addService = new AddService();
        HttpServletRequest req = this.getRequest();
        String code = PubInfo.getString(req.getParameter("code"));
        JSONReturnData data = new JSONReturnData("");
        if (indexListService.getData(code).getCode() != null) {
            data.setReturncode(300);
            data.setReturndata("fail");
            this.sendJson(data);
            return;
        }

        String name = PubInfo.getString(req.getParameter("name"));
        String ccname = PubInfo.getString(req.getParameter("ccname"));
        IndexCategory inCata = new IndexCategory();

        inCata.setCode(code);
        inCata.setCname(name);
        int int1 = addService.addCata(inCata);
        if (int1 == -1) {
            data.setReturncode(501);
            data.setReturndata("fail");
            this.sendJson(data);
            return;
        }
        data.setReturndata(inCata);
        this.sendJson(data);
    }


}
