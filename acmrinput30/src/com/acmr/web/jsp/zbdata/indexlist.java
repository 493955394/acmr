package com.acmr.web.jsp.zbdata;

import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.pub.PageBean;
import com.acmr.model.pub.TreeNode;
import com.acmr.model.security.User;
import com.acmr.model.zhzs.IndexList;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.service.security.UserService;
import com.acmr.service.zhzs.CreateTaskService;
import com.acmr.service.zhzs.IndexListService;
import org.omg.CORBA.PUBLIC_MEMBER;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class indexlist extends BaseAction {
    public ModelAndView main() throws IOException {

        User cu=UserService.getCurrentUser();
        //PubInfo.printStr("userid:"+cu.getUserid());
        String usercode=cu.getUserid();
        ArrayList<IndexList> allindexlist= new IndexListService().getIndexList();
        IndexListService indexListService=new IndexListService();
        PageBean<IndexList> page=new PageBean<>();
        StringBuffer sb = new StringBuffer();
        sb.append(this.getRequest().getRequestURI());
        sb.append("?m=getIndexList");
        try {
            List<IndexList> indexLists=indexListService.getIndexListByPage(usercode,page.getPageNum() - 1,page.getPageSize());
            page.setData(indexLists);
            page.setTotalRecorder(allindexlist.size());
            page.setUrl(sb.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new ModelAndView("/WEB-INF/jsp/zhzs/indexlist").addObject("page",page);
    }



    /**
    * @Description: 异步获取指数计划的目录结构树
    * @Param: []
    * @return: void
    * @Author: lyh
    * @Date: 2018/9/18
    */
    public void getListTree() throws IOException {
        HttpServletRequest req=this.getRequest();
        String code=req.getParameter("id");
        IndexListService indexListService=new IndexListService();
        ArrayList<IndexList> indexlist=indexListService.getSublist(code);
        List<TreeNode> list=new ArrayList<>();
        for (int i=0;i<indexlist.size();i++){
            TreeNode node=new TreeNode();
            node.setPId(indexlist.get(i).getProcode());
            node.setId(indexlist.get(i).getCode());
            node.setName(indexlist.get(i).getCname());
            if (indexlist.get(i).getIfdata().equals("0")){
                node.setIsParent(true);
            }
            else node.setIsParent(false);
            list.add(node);
        }
        this.sendJson(list);

    }

    public void getCateTree() throws IOException {
        HttpServletRequest req=this.getRequest();
        String code=req.getParameter("id");
        IndexListService indexListService=new IndexListService();
        List<IndexList> indexlist=indexListService.getSubCate(code);
        List<TreeNode> list=new ArrayList<>();
        for (int i=0;i<indexlist.size();i++){
            TreeNode node=new TreeNode();
            node.setPId(indexlist.get(i).getProcode());
            node.setId(indexlist.get(i).getCode());
            node.setName(indexlist.get(i).getCname());
            node.setIsParent(true);
            list.add(node);
        }
        this.sendJson(list);
    }


    /**
    * @Description: 点击树刷新列表+翻页
    * @Param: []
    * @return: acmr.web.entity.ModelAndView
    * @Author: lyh
    * @Date: 2018/9/18
    */
    public ModelAndView getIndexList() throws IOException, ParseException {
        User cu=UserService.getCurrentUser();
        //PubInfo.printStr("userid:"+cu.getUserid());
        String usercode=cu.getUserid();

        HttpServletRequest req = this.getRequest();
        String pjax = req.getHeader("X-PJAX");
        String code=req.getParameter("code");
        IndexListService indexListService=new IndexListService();
        //PubInfo.printStr("code"+code);
        List<IndexList> allindexlist=new ArrayList<>();
        if (code==null){
            allindexlist=indexListService.getIndexList();
        }
        else {
            allindexlist=indexListService.getSublist(code);
        }

        PageBean<IndexList> page=new PageBean<>();
        List<IndexList> indexlist=new ArrayList<>();
        //code为null表示查询所有的计划
        if (code==null){
            indexlist=indexListService.getIndexListByPage(usercode,page.getPageNum()-1,page.getPageSize());
        }
        else if (code=="#1"){
            indexlist=indexListService.getSubIndexListByPage("",page.getPageNum()-1,page.getPageSize());
        }
        else {
            indexlist=indexListService.getSubIndexListByPage(code,page.getPageNum()-1,page.getPageSize());
            if (allindexlist.size()==0){
                indexlist.add(indexListService.getData(code));
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append(req.getRequestURL());
        if (code!=null){
            sb.append("?m=getIndexList&code="+code);
        }
        else {
            sb.append("?m=getIndexList");
        }
        page.setTotalRecorder(allindexlist.size());
        page.setUrl(sb.toString());
        page.setData(indexlist);

        if (StringUtil.isEmpty(pjax)) {
            PubInfo.printStr("isempty");
            this.getResponse().sendRedirect("/zbdata/indexlist.htm");
        } else {
            PubInfo.printStr("pjax");
            return new ModelAndView("/WEB-INF/jsp/zhzs/indextable").addObject("page",page);
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
    public void insertcate() throws IOException {
        IndexListService indexListService = new IndexListService();
        HttpServletRequest req = this.getRequest();
        String code = PubInfo.getString(req.getParameter("cocode"));
        JSONReturnData data = new JSONReturnData("");
        int x = indexListService.checkCode(code);
        if (x == 0) {
            data.setReturncode(300);
            this.sendJson(data);
            return;
        } else {
            data.setReturncode(200);
        }
        String ifdata1 = PubInfo.getString(req.getParameter("ifdata"));
        String cname = PubInfo.getString(req.getParameter("cocname"));
        String procode = PubInfo.getString(req.getParameter("idcata"));
        User user = (User) this.getSession().getAttribute("loginuser");
        String createuser = user.getUserid();
        IndexList indexList = new IndexList();
        indexList.setIfdata(ifdata1);
        indexList.setCreateuser(createuser);
        indexList.setCode(code);
        indexList.setCname(cname);
        indexList.setProcode(procode);
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
    //计划新增
    public void insertplan() throws IOException {
        IndexListService indexListService = new IndexListService();
        HttpServletRequest req = this.getRequest();
        String code = PubInfo.getString(req.getParameter("plancode"));
        JSONReturnData data = new JSONReturnData("");
        int x = indexListService.checkCode(code);
        if (x == 0) {
            data.setReturncode(300);
            this.sendJson(data);
            return;
        } else {
            data.setReturncode(200);
        }
        String ifdata1 = PubInfo.getString(req.getParameter("ifdata"));
        String cname = PubInfo.getString(req.getParameter("plancname"));
        String procode = PubInfo.getString(req.getParameter("idplan"));
        String sort = PubInfo.getString(req.getParameter("sort"));
        //String createuser = "usercode01";
        User user = (User) this.getSession().getAttribute("loginuser");
        String createuser = user.getUserid();
        String state = "0";
        IndexList indexList = new IndexList();
        indexList.setCode(code);
        indexList.setIfdata(ifdata1);
        indexList.setCreateuser(createuser);
        indexList.setCname(cname);
        indexList.setProcode(procode);
        indexList.setSort(sort);
        indexList.setState(state);
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
        String cpcode = PubInfo.getString(req.getParameter("copycode"));
        //String ifdata1 = PubInfo.getString(req.getParameter("cifdata"));
        //int ifdata = Integer.parseInt(ifdata1);
        String code = PubInfo.getString(req.getParameter("plcode"));
        JSONReturnData data = new JSONReturnData("");
        int x = indexListService.checkCode(code);
        if (x == 0) {
            data.setReturncode(300);
            this.sendJson(data);
            return;
        } else {
            data.setReturncode(200);
        }
        String cname = PubInfo.getString(req.getParameter("zname"));
        String nprocode = PubInfo.getString(req.getParameter("newprocode"));
        IndexList data1 = indexListService.getData(cpcode);
        data1.setCode(code);
        data1.setCname(cname);
        data1.setProcode(nprocode);
        indexListService.addCopyplan(data1);
        /*if (int1 == -1) {
            data.setReturncode(501);
            data.setReturndata("fail");
            this.sendJson(data);
            return;
        }*/
        data.setReturndata(data1);
        this.sendJson(data);
    }
    //编辑
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
        IndexListService.updateCate(code,procode);
        data.setReturndata("");
        this.sendJson(data);
    }
    public void delete() throws IOException {
        // 构造返回对象
        JSONReturnData data = new JSONReturnData("");
        String code = PubInfo.getString(this.getRequest().getParameter("id"));
        IndexListService indexListService = new IndexListService();
        int y = indexListService.checkProcode(code);
        if (y == 0) {
            data.setReturncode(300);
            this.sendJson(data);
            return;
        } else {
            data.setReturncode(200);
        }
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
        //启用自动生成未生成的任务
        if (state.equals("1")){
            IndexListService indexListService=new IndexListService();
            IndexList index=indexListService.getData(code);
            CreateTaskService createTaskService=new CreateTaskService();
            List<String> periods=createTaskService.getPeriods(index);
            createTaskService.createTasks(index,periods);
        }
        this.sendJson(data);
    }
    /*//页面后台检查
    public void checkcoCode() throws IOException {
        HttpServletRequest req = this.getRequest();
        IndexListService indexListService = new IndexListService();
        String code = PubInfo.getString(req.getParameter("cocode"));
        *//*String code1 = PubInfo.getString(req.getParameter("plancode"));
        String ncode = PubInfo.getString(req.getParameter("plcode"));//复制到的code*//*
        JSONReturnData data = new JSONReturnData("");
        int x = indexListService.checkCode(code);
        if (x == 0) {
                data.setReturncode(300);
            } else {
                data.setReturncode(200);
            }
        this.sendJson(data);
    }
    public void checkplCode() throws IOException {
        HttpServletRequest req = this.getRequest();
        IndexListService indexListService = new IndexListService();
        //String code = PubInfo.getString(req.getParameter("cocode"));
        String code = PubInfo.getString(req.getParameter("plancode"));
        //String ncode = PubInfo.getString(req.getParameter("plcode"));//复制到的code
        JSONReturnData data = new JSONReturnData("");
        int x = indexListService.checkCode(code);
        if (x == 0) {
            data.setReturncode(300);
        } else {
            data.setReturncode(200);
        }
        this.sendJson(data);
    }
    public void checknCode() throws IOException {
        HttpServletRequest req = this.getRequest();
        IndexListService indexListService = new IndexListService();
        //String code = PubInfo.getString(req.getParameter("cocode"));
        //String code1 = PubInfo.getString(req.getParameter("plancode"));
        String code = PubInfo.getString(req.getParameter("plcode"));//复制到的code
        JSONReturnData data = new JSONReturnData("");
        int x = indexListService.checkCode(code);
        if (x == 0) {
            data.setReturncode(300);
        } else {
            data.setReturncode(200);
        }
        this.sendJson(data);
    }*/
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

