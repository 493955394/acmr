package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.model.pub.JSONReturnData;
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

    public void insert() throws IOException {
        IndexListService indexListService = new IndexListService();
        HttpServletRequest req = this.getRequest();
        String code = PubInfo.getString(req.getParameter("code"));
        JSONReturnData data = new JSONReturnData("");
        data.setReturndata(200);
        /*if (indexListService.getData(code).getCode() != null) {
            data.setReturncode(300);
            data.setReturndata("fail");
            this.sendJson(data);
            return;
        }*/
        String ifdata1 = PubInfo.getString(req.getParameter("ifdata"));
        int ifdata = Integer.parseInt(ifdata1);
        String cname = PubInfo.getString(req.getParameter("cname"));
        String procode = PubInfo.getString(req.getParameter("idcata"));
        String procode1 = PubInfo.getString(req.getParameter("idplan"));
        String sort = PubInfo.getString(req.getParameter("sort"));
        String createuser = "usercode01";
        String state = "0";
        IndexList indexList = new IndexList();
        indexList.setCode(code);
        indexList.setCname(cname);
        indexList.setProcode(procode);
        indexList.setIfdata(ifdata1);
        indexList.setCreateuser(createuser);
        if(ifdata == 0){
            indexList.setProcode(procode);
        }else {
            indexList.setProcode(procode1);
            indexList.setSort(sort);
            indexList.setState(state);
        }
        int int1 = indexListService.addCp(indexList);
        /*if (int1 == -1) {
            data.setReturncode(501);
            data.setReturndata("fail");
            this.sendJson(data);
            return;
        }*/

        data.setReturndata(indexList);
        this.sendJson(data);
        //this.getResponse().sendRedirect("indexlist.htm");

    }
  //  public void insert() throws IOException {
//        IndexListService indexListService = new IndexListService();
//        HttpServletRequest req = this.getRequest();
//        String code = PubInfo.getString(req.getParameter("code"));
//        JSONReturnData data = new JSONReturnData("");
//        if (indexListService.getData(code).getCode() != null) {
//            data.setReturncode(300);
//            data.setReturndata("fail");
//            this.sendJson(data);
//            return;
//        }
//        String ifdata1 = PubInfo.getString(req.getParameter("ifdata"));
//        String ifdata2 = indexListService.getData(ifdata1).getIfdata();
//        int ifdata = Integer.parseInt(ifdata1);
//        String cname = PubInfo.getString(req.getParameter("cname"));
//        String procode = PubInfo.getString(req.getParameter("idcata"));
//        IndexList indexList = new IndexList();
//        indexList.setCode(code);
//        indexList.setCname(cname);
//        indexList.setProcode(procode);
//        indexList.setIfdata(ifdata1);
//        int int1 = indexListService.addMenu(indexList);
//        if (int1 == -1) {
//            data.setReturncode(501);
//            data.setReturndata("fail");
//            this.sendJson(data);
//            return;
//        }
//        data.setReturndata(indexList);
//        this.sendJson(data);
//        //this.getResponse().sendRedirect("indexlist.htm");
//


}
