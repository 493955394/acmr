package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.zhzs.IndexList;
import com.acmr.service.zhzs.IndexListService;
import org.omg.CORBA.PUBLIC_MEMBER;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class indexlist extends BaseAction {
    public ModelAndView main() throws IOException {
        ArrayList<IndexList> indexlist= new IndexListService().getIndexList();
        return new ModelAndView("/WEB-INF/jsp/zhzs/indexlist").addObject("indexlist",indexlist);
    }

    public ModelAndView getIndexList() throws IOException {
        HttpServletRequest req = this.getRequest();
        String pjax = req.getHeader("X-PJAX");
        String code=req.getParameter("code");
        PubInfo.printStr("code"+code);
        ArrayList<IndexList> indexlist= new IndexListService().getSublist(code);
        if (indexlist.size()==0){
            indexlist.add(new IndexListService().getData(code));
        }
        if (StringUtil.isEmpty(pjax)) {
            PubInfo.printStr("isempty");
            this.getResponse().sendRedirect("/zbdata/indexlist.htm");
        } else {
            PubInfo.printStr("pjax");
            return new ModelAndView("/WEB-INF/jsp/zhzs/indextable").addObject("indexlist",indexlist);
        }
        return null;
    }

    /**
     *
     *
     * @author wf
     * @date
     * @param
     * @return
     */
    public void insert() throws IOException {
        IndexListService indexListService = new IndexListService();
        HttpServletRequest req = this.getRequest();
        String code = PubInfo.getString(req.getParameter("cocode"));
        String code1 = PubInfo.getString(req.getParameter("plancode"));
        JSONReturnData data = new JSONReturnData("");
        if (indexListService.getData(code).getCode()!=null){
            data.setReturncode(300);
            this.sendJson(data);
            return;
        }
        if (indexListService.getData(code1).getCode()!=null){
            data.setReturncode(300);
            this.sendJson(data);
            return;
        }
        String ifdata1 = PubInfo.getString(req.getParameter("ifdata"));
        int ifdata = Integer.parseInt(ifdata1);
        String cname = PubInfo.getString(req.getParameter("cocname"));
        String cname1 = PubInfo.getString(req.getParameter("plancname"));
        String procode = PubInfo.getString(req.getParameter("idcata"));
        String procode1 = PubInfo.getString(req.getParameter("idplan"));
        String sort = PubInfo.getString(req.getParameter("sort"));
        String createuser = "usercode01";
        String state = "0";
        IndexList indexList = new IndexList();
        indexList.setIfdata(ifdata1);
        indexList.setCreateuser(createuser);
        if(ifdata == 0){
            indexList.setCode(code);
            indexList.setCname(cname);
            indexList.setProcode(procode);
        }else {
            indexList.setCode(code1);
            indexList.setCname(cname1);
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

    }
    //复制到
    public void copy() throws IOException {
        IndexListService indexListService = new IndexListService();
        HttpServletRequest req = this.getRequest();
        String code = PubInfo.getString(req.getParameter("copycode"));
        //String ifdata1 = PubInfo.getString(req.getParameter("cifdata"));
        //int ifdata = Integer.parseInt(ifdata1);
        String ncode = PubInfo.getString(req.getParameter("plcode"));
        JSONReturnData data = new JSONReturnData("");
        if (indexListService.getData(ncode).getCode()!=null){
            data.setReturncode(300);
            this.sendJson(data);
            return;
        }
        String cname = PubInfo.getString(req.getParameter("zname"));
        String nprocode = PubInfo.getString(req.getParameter("newprocode"));
        IndexList indexList = new IndexList();
        indexList.setCode(ncode);
        indexList.setCname(cname);
        indexList.setProcode(nprocode);
        indexListService.updatePlan(indexList,code);
        /*if (int1 == -1) {
            data.setReturncode(501);
            data.setReturndata("fail");
            this.sendJson(data);
            return;
        }*/
        data.setReturndata(indexList);
        this.sendJson(data);
    }
    public void update() throws IOException{
        HttpServletRequest req = this.getRequest();
        //IndexListService indexListService = new IndexListService();
        JSONReturnData data = new JSONReturnData("");
        data.setReturncode(200);
        String code = PubInfo.getString(req.getParameter("editcode"));
        String procode = PubInfo.getString(req.getParameter("editprocode"));
        IndexList indexList = new IndexList();
        indexList.setCode(code);
        indexList.setProcode(procode);
        //IndexListService.updateCatePlan(code,procode);
        IndexListService.updateCatePlan(indexList);
        data.setReturndata("");
        this.sendJson(data);
    }
    public void delete() throws IOException {
        // 构造返回对象
        JSONReturnData data = new JSONReturnData("");
        data.setReturncode(200);
        String code = PubInfo.getString(this.getRequest().getParameter("id"));
        int int1 = IndexListService.delCataplan(code);
        data.setReturndata("");
        this.sendJson(data);
    }
    public void switchState() throws IOException {
        HttpServletRequest req = this.getRequest();
        String code = PubInfo.getString(req.getParameter("code"));
        JSONReturnData data = new JSONReturnData("");
        String state = PubInfo.getString(req.getParameter("state"));
        IndexList indexList = new IndexList();
        indexList.setCode(code);
        indexList.setState(state);
        IndexListService.updateCatePlan(indexList);
        data.setReturncode(200);
        this.sendJson(data);
    }
    public void checkCode() throws IOException {
        HttpServletRequest req = this.getRequest();
        IndexListService indexListService = new IndexListService();
        String code = PubInfo.getString(req.getParameter("cocode"));
        String code1 = PubInfo.getString(req.getParameter("plancode"));
        String ncode = PubInfo.getString(req.getParameter("plcode"));//复制到的code
        JSONReturnData data = new JSONReturnData("");
        List<String> list = new ArrayList();
        list.add(code);
        list.add(code1);
        list.add(ncode);
        for(int i = 0;i<list.size();i++){
            if (indexListService.getData(list.get(i)).getCode()!=null) {
                data.setReturncode(300);
            } else {
                data.setReturncode(200);
            }
        }


        this.sendJson(data);
    }
    /**
     * 综合指数查找
     * @return
     * @throws IOException
     */
    public ModelAndView find() throws IOException{
        HttpServletRequest req = this.getRequest();
        ArrayList<IndexList> indexlist= new ArrayList<IndexList>();
        // 获取查询数据
        String code = StringUtil.toLowerString(req.getParameter("code"));
        String cname = StringUtil.toLowerString(req.getParameter("cname"));
        // 判断是否pjax 请求
        String pjax = req.getHeader("X-PJAX");
        if (!StringUtil.isEmpty(code)) {
            indexlist= new IndexListService().found(0,code);
        }
        if (!StringUtil.isEmpty(cname)) {
            indexlist= new IndexListService().found(1,cname);
        }
        Map<String, String> codes = new HashMap<String, String>();
        codes.put("code", code);
        codes.put("cname", cname);
        if (StringUtil.isEmpty(pjax)) {
            return new ModelAndView("/WEB-INF/jsp/zhzs/indexlist").addObject("indexlist",indexlist).addObject("codes",codes);
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/indextable").addObject("indexlist",indexlist).addObject("codes",codes);
        }
    }



}

