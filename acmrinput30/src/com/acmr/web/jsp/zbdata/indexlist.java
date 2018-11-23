package com.acmr.web.jsp.zbdata;

import acmr.cubeinput.MetaTableException;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.pub.PageBean;
import com.acmr.model.pub.TreeNode;
import com.acmr.model.pub.ZTreeNode;
import com.acmr.model.security.User;
import com.acmr.model.zhzs.IndexList;
import com.acmr.service.security.DepartmentService;
import com.acmr.service.security.UserService;
import com.acmr.service.zhzs.CreateTaskService;
import com.acmr.service.zhzs.IndexListService;
import com.acmr.service.zhzs.RightControlService;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.text.ParseException;

import java.util.*;

public class indexlist extends BaseAction {
    private HttpServletRequest req;

    public ModelAndView main() throws IOException {

        User cu=UserService.getCurrentUser();
        //PubInfo.printStr("userid:"+cu.getUserid());
        String usercode=cu.getUserid();
        ArrayList<IndexList> allindexlist= new IndexListService().getIndexList();
        IndexListService indexListService=new IndexListService();
        String state="0";
        //检查是否有需要定位的
        String icode=this.getRequest().getParameter("icode");
        String path="";
        if (icode!=null){
            Boolean self=IndexListDao.Fator.getInstance().getIndexdatadao().hasIndex(icode,usercode);
            if (self){
                path=indexListService.getIndexPath(icode);
            }
        }

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
        return new ModelAndView("/WEB-INF/jsp/zhzs/indexlist").addObject("page",page).addObject("state",state).addObject("path",path);
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
            if (indexlist.get(i).getIfdata().equals("0")){
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
        }
        this.sendJson(list);

    }

    public void getTree() throws IOException {
        HttpServletRequest req=this.getRequest();
        String code=req.getParameter("id");
        String indexcode=PubInfo.getString(req.getParameter("indexcode"));
        IndexListService indexListService=new IndexListService();
        List<IndexList> indexlist=indexListService.getSubCate(code);
        List<TreeNode> list=new ArrayList<>();
        for (int i=0;i<indexlist.size();i++){
            if(!indexlist.get(i).getCode().equals(indexcode)){
                TreeNode node=new TreeNode();
                node.setPId(indexlist.get(i).getProcode());
                node.setId(indexlist.get(i).getCode());
                node.setName(indexlist.get(i).getCname());
                node.setIsParent(true);
                list.add(node);
            }
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
        //PubInfo.printStr("code:"+code);
        IndexListService indexListService=new IndexListService();
        //PubInfo.printStr("code"+code);
        List<IndexList> allindexlist=new ArrayList<>();
        //state=0,查看指数目录，=1，查看我收到的指数，=2，查看我共享的指数
        String state="0";


        if (code==null){
            allindexlist=indexListService.getIndexList();
        }
        else {
            allindexlist=indexListService.getSublist(code);
        }

        PageBean page=new PageBean<>();
        List<IndexList> indexlist=new ArrayList<>();
        //code为null表示查询所有的计划
        if (code==null){
            indexlist=indexListService.getIndexListByPage(usercode,page.getPageNum()-1,page.getPageSize());
            page.setData(indexlist);
        }
        else if (code.equals("!1")){
            indexlist=indexListService.getSubIndexListByPage("",page.getPageNum()-1,page.getPageSize());
            page.setData(indexlist);

        }
        //返回我收到的指数list
        else if (code.equals("!2")){
            List<Map<String,Object>> list=indexListService.getReceivedList();
            int b=(page.getPageNum()-1)*page.getPageSize()+1;
            int e=b+page.getPageSize();
            List<Map<String,Object>> rlist=new ArrayList<>();
            for (int i=0;i<list.size();i++){
                int j=i+1;
                if (j>=b&&j<e){
                    rlist.add(list.get(i));
                }
            }
            page.setData(rlist);
            state="1";
        }
        //返回我共享的指数list
        else if (code.equals("!3")){
            List<Map<String,String>> list=indexListService.getSharedList(usercode);
            int b=(page.getPageNum()-1)*page.getPageSize()+1;
            int e=b+page.getPageSize();
            List<Map<String,String>> plist=new ArrayList<>();
            for (int i=0;i<list.size();i++){
                int j=i+1;
                if (j>=b&&j<e){
                    plist.add(list.get(i));
                }
            }
            page.setData(plist);
            state="2";
        }
        else {
            indexlist=indexListService.getSubIndexListByPage(code,page.getPageNum()-1,page.getPageSize());
            /*if (allindexlist.size()==0){
                indexlist.add(indexListService.getData(code));
            }*/
            page.setData(indexlist);
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
        //我共享的指数
       /* if (code.equals("!3")){
            *//*List<IndexList> allsharedList=indexListService.getSharedList(usercode);
            indexlist.clear();
            int b=(page.getPageNum()-1)*page.getPageSize()+1;
            int e=b+page.getPageSize();
            for (int i=0;i<allsharedList.size();i++){
                int j=i+1;
                if (j>=b&&j<e){
                    indexlist.add(allsharedList.get(i));
                }
            }
            page.setData(indexlist);
            state="2";*//*
        }*/

        if (StringUtil.isEmpty(pjax)) {
            //PubInfo.printStr("isempty");
            this.getResponse().sendRedirect(this.getContextPath() + "/zbdata/indexlist.htm");

        } else {
           // PubInfo.printStr("pjax");
            return new ModelAndView("/WEB-INF/jsp/zhzs/indextable").addObject("page",page).addObject("state",state);
        }
        return null;
    }

    /**
     * 新增目录
     * @author wf
     * @date
     * @param
     * @return
     */
    public void insertcate() throws IOException {
        IndexListService indexListService = new IndexListService();
        HttpServletRequest req = this.getRequest();
        String code = PubInfo.getString(req.getParameter("cocode"));
        String cname = PubInfo.getString(req.getParameter("cocname"));
        User user = (User) this.getSession().getAttribute("loginuser");
        String createuser = user.getUserid();
        JSONReturnData data = new JSONReturnData("");
        int x = indexListService.checkCode(code);
        int y = indexListService.checkCname(0,createuser,cname);//目录的名称也不能重复
        if (x == 0) {
            data.setReturncode(300);
            data.setReturndata("该编码已存在");
            this.sendJson(data);
            return;
        }
        else if(y == 0){
            data.setReturncode(301);
            data.setReturndata("该名称已存在");
            this.sendJson(data);
            return;
        }
        else {
            data.setReturncode(200);
        }
        String ifdata1 = PubInfo.getString(req.getParameter("ifdata"));
        String procode = PubInfo.getString(req.getParameter("idcata"));
        if(procode.equals("!1")){
            procode="";
        }
        String proname = PubInfo.getString(req.getParameter("cataname"));
        String createtime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        IndexList indexList = new IndexList();
        indexList.setIfdata(ifdata1);
        indexList.setCreateuser(createuser);
        indexList.setCode(code);
        indexList.setCname(cname);
        indexList.setProcode(procode);
        indexList.setCreatetime(createtime);
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
    /**
     * 计划新增
     * @author wf
     * @date
     * @param
     * @return
     */
    public void insertplan() throws IOException {
        IndexListService indexListService = new IndexListService();
        HttpServletRequest req = this.getRequest();
        String code = PubInfo.getString(req.getParameter("plcode"));
        String cname = PubInfo.getString(req.getParameter("plancname"));
        User user = (User) this.getSession().getAttribute("loginuser");
        String createuser = user.getUserid();
        JSONReturnData data = new JSONReturnData("");
        int x = indexListService.checkCode(code);
        int y = indexListService.checkCname(1,createuser,cname);
        if (x == 0) {
            data.setReturncode(300);
            data.setReturndata("该编码已存在");
            this.sendJson(data);
            return;
        } else if(y == 0){
            data.setReturncode(301);
            data.setReturndata("该名称已存在");
            this.sendJson(data);
            return;
        }else {
            data.setReturncode(200);
        }
        String ifdata1 = PubInfo.getString(req.getParameter("ifdata"));

        String procode = PubInfo.getString(req.getParameter("idplan"));
        if(procode.equals("!1")){
            procode="";
        }
        String proname = PubInfo.getString(req.getParameter("proname"));
        String sort = PubInfo.getString(req.getParameter("sort"));
        String createtime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //String createuser = "usercode01";
        String state = "0";
        IndexList indexList = new IndexList();
        indexList.setCode(code);
        indexList.setIfdata(ifdata1);
        indexList.setCreateuser(createuser);
        indexList.setCname(cname);
        indexList.setProcode(procode);
        indexList.setSort(sort);
        indexList.setState(state);
        indexList.setCreatetime(createtime);
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
    /**
     * 复制到
     * @author wf
     * @date
     * @param
     * @return
     */
    public void copy() throws IOException {
        IndexListService indexListService = new IndexListService();
        HttpServletRequest req = this.getRequest();
        String cpcode = PubInfo.getString(req.getParameter("copycode"));
        //String ifdata1 = PubInfo.getString(req.getParameter("cifdata"));
        //int ifdata = Integer.parseInt(ifdata1);
        User cu=UserService.getCurrentUser();
        String usercode=cu.getUserid();
        String code = PubInfo.getString(req.getParameter("conewcode"));
        String cname = PubInfo.getString(req.getParameter("zname"));
        JSONReturnData data = new JSONReturnData("");
        int x = indexListService.checkCode(code);
        int y = indexListService.checkCname(1,usercode,cname);//0是目录，1是计划
        if (x == 0) {
            data.setReturncode(300);
            data.setReturndata("该编码已存在");
            this.sendJson(data);
            return;
        } else if(y == 0){
            data.setReturncode(301);
            data.setReturndata("该名称已存在");
            this.sendJson(data);
            return;
        }
         else {
            data.setReturncode(200);
        }
        String nprocode = PubInfo.getString(req.getParameter("newprocode"));
        //String createtime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String state = "0";
        DataTableRow row= IndexListDao.Fator.getInstance().getIndexdatadao().getByCode(cpcode).getRows().get(0);
        String start = row.getString("startperiod");
        String delay = row.getString("delayday");
        String getplan = row.getString("planperiod");
        if(start.equals("")||delay.equals("")||getplan.equals("")){
            IndexList data1 = indexListService.getData(cpcode);
            String createtime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            data1.setCode(code);
            data1.setCname(cname);
            data1.setProcode(nprocode);
            data1.setState(state);
            data1.setCreateuser(usercode);
            data1.setCreatetime(createtime);
            data1.setUpdatetime(createtime);
            indexListService.addCp(data1);
            data.setReturndata(data1);
            this.sendJson(data);
        } else {

            IndexList data1 = indexListService.getData(cpcode);
            data1.setCode(code);
            data1.setCname(cname);
            data1.setProcode(nprocode);
            data1.setState(state);
            data1.setCreateuser(usercode);
            String startpeirod = data1.getStartperiod();
            String delayday = data1.getDelayday();
            //生成plantime，planperiod
            if (startpeirod.length()==4){
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(startpeirod)+1);
                calendar.set(Calendar.MONTH,0);
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                calendar.set(Calendar.HOUR_OF_DAY,0);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);
                // calendar.set(Calendar.MILLISECOND,0);
                Date time=calendar.getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String plantime=df.format(time);
                data1.setPlantime(plantime);
                String planperiod=startpeirod;
                data1.setPlanperiod(planperiod);

            }
            else if (startpeirod.length()==5){
                String q=startpeirod.substring(4);
                String year=startpeirod.substring(0,4);
                Calendar calendar=Calendar.getInstance();
                if (q.equals("D")){
                    calendar.set(Calendar.YEAR, Integer.parseInt(year)+1);
                    calendar.set(Calendar.MONTH,0);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    data1.setPlantime(plantime);
                    String planperiod=startpeirod;
                    data1.setPlanperiod(planperiod);
                }
                else if (q.equals("A")){
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                    calendar.set(Calendar.MONTH,3);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    data1.setPlantime(plantime);
                    String planperiod=startpeirod;
                    data1.setPlanperiod(planperiod);
                }
                else if (q.equals("B")){
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                    calendar.set(Calendar.MONTH,6);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    data1.setPlantime(plantime);
                    String planperiod=startpeirod;
                    data1.setPlanperiod(planperiod);
                }
                else {
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                    calendar.set(Calendar.MONTH,9);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    data1.setPlantime(plantime);
                    String planperiod=startpeirod;
                    data1.setPlanperiod(planperiod);
                }
            }
            else {
                String year=startpeirod.substring(0,4);
                String mon=startpeirod.substring(4);
                Calendar calendar=Calendar.getInstance();
                if (mon.equals("12")){
                    calendar.set(Calendar.YEAR, Integer.parseInt(year)+1);
                    calendar.set(Calendar.MONTH,0);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    data1.setPlantime(plantime);
                    String planperiod=startpeirod;
                    data1.setPlanperiod(planperiod);
                }
                else {
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                    calendar.set(Calendar.MONTH,Integer.parseInt(mon));
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    data1.setPlantime(plantime);
                    String planperiod=startpeirod;
                    data1.setPlanperiod(planperiod);
                }
            }
            //data1.setCreatetime(createtime);
            indexListService.addCopyplan(cpcode,data1);
            indexListService.switchFormu(code,cpcode);
        /*if (int1 == -1) {
            data.setReturncode(501);
            data.setReturndata("fail");
            this.sendJson(data);
            return;
        }*/
            data.setReturndata(data1);
            this.sendJson(data);
        }


    }
    /**
     * 目录编辑
     * @author wf
     * @date
     * @param
     * @return
     */
    public void editCate() throws IOException{
        HttpServletRequest req = this.getRequest();
        //IndexListService indexListService = new IndexListService();
        JSONReturnData data = new JSONReturnData("");
        IndexListService indexListService = new IndexListService();
        data.setReturncode(200);
        String code = PubInfo.getString(req.getParameter("editcode"));
        String name = PubInfo.getString(req.getParameter("editcname"));
        String createuser = indexListService.getData(code).getCreateuser();
        int y = indexListService.checkCname(0,createuser,name,code);//目录的名称也不能重复
        if (y==0) {
            data.setReturncode(300);
            data.setReturndata("该名称已存在");
            this.sendJson(data);
            return;
        }
        String procode = PubInfo.getString(req.getParameter("editprocode"));
        if(procode.equals("!1")){
            procode="";
        }
        /*if (procode.equals("")) {
            data.setReturncode(300);
            this.sendJson(data);
            return;
        } else {
            data.setReturncode(200);
        }*/

        /*if(oldcode.equals(code) && oldname.equals(name)){
            IndexList indexList = indexListService.getData(oldcode);
            //IndexList indexList = new IndexList();
            //indexList.setCode(code);
            indexList.setProcode(procode);
            indexListService.updateCate(oldcode,indexList);
            }else if(oldcode.equals(code) && !oldname.equals(name)){
                IndexList indexList = indexListService.getData(oldcode);
                //indexList.setCode(code);
                indexList.setCname(name);
                indexList.setProcode(procode);
                indexListService.updateCate(oldcode,indexList);
                 }else{
                    IndexList indexList = indexListService.getData(oldcode);
                    indexList.setCode(code);
                    indexList.setCname(name);
                    indexList.setProcode(procode);
                    indexListService.addCp(indexList);
        }*/
        IndexList indexList = indexListService.getData(code);
        indexList.setProcode(procode);
        indexList.setCode(code);
        indexList.setCname(name);
        indexListService.updateCate(code,indexList);
        data.setReturndata("");
        this.sendJson(data);
    }
    /**
     * 目录、计划删除
     * @author wf
     * @date
     * @param
     * @return
     */
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

    /**
     * 停用启用
     * @author wf
     * @date
     * @param
     * @return
     */
    public void switchState() throws IOException {
        HttpServletRequest req = this.getRequest();
        String code = PubInfo.getString(req.getParameter("code"));
        JSONReturnData data = new JSONReturnData("");
        String state = PubInfo.getString(req.getParameter("state"));
        IndexListService indexListService=new IndexListService();
        IndexList indexList = new IndexList();
        indexList.setCode(code);
        indexList.setState(state);

        //停用
        if (state.equals("0")){
            IndexListService.updateSwitch(indexList);
            data.setReturncode(200);
            this.sendJson(data);
        }
        else {
            //校验部分
            Boolean check=false;
            //校验模型
            Boolean checkmod=indexListService.checkModule(code);
            //   PubInfo.printStr("checkmode"+checkmod);
            //校验是否已经通过编辑，基本信息完善
            Boolean checkInfo=indexListService.checkInfo(code);
            // PubInfo.printStr("checkInfo"+checkInfo);
            //校验是否有指标、地区
            Boolean checkZbReg=indexListService.checkZBandReg(code);
            //  PubInfo.printStr("checkZbReg"+checkZbReg);
            Boolean checkhasMod=indexListService.checkHasMod(code);

            check=checkInfo&&checkmod&&checkZbReg&&checkhasMod;
            //PubInfo.printStr(String.valueOf(check));

            //校验通过
            if (check){
                IndexListService.updateSwitch(indexList);
                data.setReturncode(200);
                //启用自动生成未生成的任务
                if (state.equals("1")){
                    IndexList index=indexListService.getData(code);
                    CreateTaskService createTaskService=new CreateTaskService();
                    List<String> periods=createTaskService.getPeriods(index);
                    createTaskService.createTasks(index,periods);
                }
                this.sendJson(data);
            }
            else {

                JSONObject obj=new JSONObject();
                obj.put("checkmod",checkmod);
                obj.put("checkInfo",checkInfo);
                obj.put("checkZbReg",checkZbReg);
                obj.put("checkhasMod",checkhasMod);
                this.sendJson(obj);
            }

        }



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
    public ModelAndView find() throws IOException {
        User cu=UserService.getCurrentUser();
        String usercode=cu.getUserid();
        HttpServletRequest req = this.getRequest();
        IndexListService indexListService=new IndexListService();
        ArrayList<IndexList> indexAllList= new ArrayList<IndexList>();
        List<IndexList> indexList= new ArrayList<IndexList>();
        // 获取查询数据
        String seltype = req.getParameter("seltype");
        String keyword =  StringUtil.toLowerString(req.getParameter("keyword"));
        Map<String, String> codes = new HashMap<String, String>();
        //为了回显
        if(seltype.equals("code")){
            codes.put("code", "1");
            codes.put("cname", "");
            codes.put("keyword",keyword);
        }
        else{
            codes.put("code", "");
            codes.put("cname", "1");
            codes.put("keyword",keyword);
        }
        String procode = req.getParameter("id");//看是否点击了树；
        // 判断是否pjax 请求
        String pjax = req.getHeader("X-PJAX");
        List<String> treeList=null;
        PageBean<IndexList> page = new PageBean<IndexList>();
        StringBuffer sb = new StringBuffer();
        sb.append(this.getRequest().getRequestURI());
        if (!StringUtil.isEmpty(procode)) {
                treeList = indexListService.getAllSubs(procode,usercode);
        }
        sb.append("?m=find&seltype="+seltype+"&keyword="+keyword+"&id="+procode);
        if (!StringUtil.isEmpty(keyword)) {
            if(treeList!=null){
                if(seltype.equals("code")){
                    indexList = new IndexListService().found(0,keyword,usercode);//code
                }
               else {
                    indexList = new IndexListService().found(1,keyword,usercode);
                }
                for (int i = 0; i <indexList.size() ; i++) {
                    if(treeList.contains(indexList.get(i).getCode())){
                        indexAllList.add(indexList.get(i));
                    }
                }
            }
            else {
                if(seltype.equals("code")){
                    indexAllList= new IndexListService().found(0,keyword,usercode);
                }
               else {
                    indexAllList= new IndexListService().found(1,keyword,usercode);
                }
            }
            //indexList= indexListService.found(0,code,usercode,page.getPageNum() - 1,page.getPageSize());
        }
        if(StringUtil.isEmpty(keyword)){//没有默认搜所有
            if(treeList!=null){
                indexList = new IndexListService().getIndexList();//所有
                for (int i = 0; i <indexList.size() ; i++) {
                    if(treeList.contains(indexList.get(i).getCode())){
                        indexAllList.add(indexList.get(i));
                    }
                }
            }
            else {
                indexAllList = new IndexListService().getIndexList();
            }
        }
        //分页
        int b=(page.getPageNum()-1)*page.getPageSize()+1;
        int e=b+page.getPageSize();
        List<IndexList> rlist=new ArrayList<>();
        for (int i=0;i<indexAllList.size();i++){
            int j=i+1;
            if (j>=b&&j<e){
                rlist.add(indexAllList.get(i));
            }
        }
        page.setData(rlist);
       // page.setData(indexList);
        page.setTotalRecorder(indexAllList.size());
        page.setUrl(sb.toString());
        if (StringUtil.isEmpty(pjax)) {
            return new ModelAndView("/WEB-INF/jsp/zhzs/indexlist").addObject("page",page).addObject("codes",codes).addObject("state","0");
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/indextable").addObject("page",page).addObject("codes",codes).addObject("state","0");
        }
    }

    /**
     * 分享的撤回功能
     */
    public void shareWithDraw() throws IOException {
        HttpServletRequest req = this.getRequest();
        JSONReturnData data = new JSONReturnData("");
        String indexcode = PubInfo.getString(req.getParameter("indexcode"));
        String depusercode = PubInfo.getString(req.getParameter("depusercode"));
        String sort = PubInfo.getSortString(req.getParameter("sort"));
        IndexListService indexListService = new IndexListService();
        if (indexListService.delShare(indexcode,depusercode,sort)==1){
         data.setReturncode(200);
         this.sendJson(data);
         return;
        }else {
            data.setReturncode(300);
            this.sendJson(data);
            return;
        }
    }

        /**
         * 我共享的指数查找
         */
        public ModelAndView shareListFind() throws IOException {
            User cu=UserService.getCurrentUser();
            String usercode=cu.getUserid();
            HttpServletRequest req = this.getRequest();
            IndexListService indexListService=new IndexListService();
            List<Map<String,String>> indexAllList= new ArrayList<Map<String,String>>();
            List<Map<String,String>> indexList= new ArrayList<Map<String,String>>();
            // 获取查询数据
            String seltype = req.getParameter("seltype");
            String keyword =  StringUtil.toLowerString(req.getParameter("keyword"));
            Map<String, String> codes = new HashMap<String, String>();
            //为了回显
            if(seltype.equals("code")){
                codes.put("code", "1");
                codes.put("cname", "");
                codes.put("keyword",keyword);
            }
            else{
                codes.put("code", "");
                codes.put("cname", "1");
                codes.put("keyword",keyword);
            }
            // 判断是否pjax 请求
            String pjax = req.getHeader("X-PJAX");

            PageBean<Map<String,String>> page = new PageBean<Map<String,String>>();
            StringBuffer sb = new StringBuffer();
            sb.append(this.getRequest().getRequestURI());
            sb.append("?m=receiveListFind&seltype="+seltype+"&keyword="+keyword);
                if(seltype.equals("code")){
                indexAllList= new IndexListService().shareSelect(0,keyword,usercode);//code
                indexList= indexListService.shareSelect(0,keyword,usercode,page.getPageNum() - 1,page.getPageSize());
                }
                else {
                indexAllList= new IndexListService().shareSelect(1,keyword,usercode);//cname
                indexList= indexListService.shareSelect(1,keyword,usercode,page.getPageNum() - 1,page.getPageSize());
                }
            page.setData(indexList);
            page.setTotalRecorder(indexAllList.size());
            page.setUrl(sb.toString());
            if (StringUtil.isEmpty(pjax)) {
                return new ModelAndView("/WEB-INF/jsp/zhzs/indexlist").addObject("page",page).addObject("codes",codes).addObject("state","2");
            } else {
                return new ModelAndView("/WEB-INF/jsp/zhzs/indextable").addObject("page",page).addObject("codes",codes).addObject("state","2");
            }
        }

    /**
     * 我收到的指数查找
     */
    public ModelAndView receiveListFind() throws IOException {
        User cu=UserService.getCurrentUser();
        String usercode=cu.getUserid();
        HttpServletRequest req = this.getRequest();
        IndexListService indexListService=new IndexListService();
        List<Map<String,Object>> indexAllList= new ArrayList<Map<String,Object>>();
        // 获取查询数据
        String seltype = req.getParameter("seltype");
        String keyword =  StringUtil.toLowerString(req.getParameter("keyword"));
        Map<String, String> codes = new HashMap<String, String>();
        //为了回显
        if(seltype.equals("code")){
            codes.put("code", "1");
            codes.put("cname", "");
            codes.put("keyword",keyword);
        }
        else{
            codes.put("code", "");
            codes.put("cname", "1");
            codes.put("keyword",keyword);
        }
        // 判断是否pjax 请求
        String pjax = req.getHeader("X-PJAX");

        PageBean<Map<String,Object>> page = new PageBean<Map<String,Object>>();
        StringBuffer sb = new StringBuffer();
        sb.append(this.getRequest().getRequestURI());
        sb.append("?m=receiveListFind&seltype="+seltype+"&keyword="+keyword);
        if (!StringUtil.isEmpty(keyword)) {
            if(seltype.equals("code")){
            indexAllList= indexListService.receiveSelect(0,keyword,usercode);}//code
            else {
                indexAllList= indexListService.receiveSelect(1,keyword,usercode);//cname
            }
        }
        if(StringUtil.isEmpty(keyword)){
            indexAllList=indexListService.getReceivedList();
        }
        int b=(page.getPageNum()-1)*page.getPageSize()+1;
        int e=b+page.getPageSize();
        List<Map<String,Object>> rlist=new ArrayList<>();
        for (int i=0;i<indexAllList.size();i++){
            int j=i+1;
            if (j>=b&&j<e){
                rlist.add(indexAllList.get(i));
            }
        }
        page.setData(rlist);
        page.setTotalRecorder(indexAllList.size());
        page.setUrl(sb.toString());
        if (StringUtil.isEmpty(pjax)) {
            return new ModelAndView("/WEB-INF/jsp/zhzs/indexlist").addObject("page",page).addObject("codes",codes).addObject("state","1");
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/indextable").addObject("page",page).addObject("codes",codes).addObject("state","1");
        }
    }

    //以下是权限管理-------start
    /**
     * 模态框查询之前的共享记录，并返回前台
     */
    public void rightManager() throws IOException{
        HttpServletRequest req=this.getRequest();
        String indexcode=PubInfo.getString(req.getParameter("indexcode"));
        RightControlService rightControlService = new RightControlService();
        List<Map<String,String>> list = rightControlService.getRightList(indexcode);
        this.sendJson(list);
    }
    /**
     * 组织用户树
     * @throws IOException
     */
    public void depUserTree() throws IOException{
            HttpServletRequest req=this.getRequest();
            String pcode=req.getParameter("id");
            String icode = req.getParameter("icode");
            IndexListService indexListService =new IndexListService();
            String createuser = indexListService.getData(icode).getCreateuser();
            List<ZTreeNode> list=getDepUserTree(pcode,createuser);
            this.sendJson(list);
    }
    /**
     * 组织和用户合起来
     * @param pcode
     * @return
     */
    public static List<ZTreeNode> getDepUserTree(String pcode,String createuser){
        //获取用户组织树
        List<ZTreeNode> deps = new DepartmentService().getSubDepartments(pcode);
        List<ZTreeNode> list = new ArrayList<ZTreeNode>();
        for (int i = 0; i <deps.size() ; i++) {
            if(deps.get(i).isIsParent()){
                list.add(deps.get(i));//如果在组织树下已经是父节点，就直接加进list
            }
            else{
               /* List<User> ifHasUsers = new UserService().getDepUsers(deps.get(i).getId());
                if(ifHasUsers.size()>0){
                    //如果在组织树下不是父节点，先看它下面是否有用户，有就是父节点
                    list.add(new ZTreeNode(deps.get(i).getId(),deps.get(i).getPId(),deps.get(i).getName(),true));
               }else {
                    list.add(deps.get(i));//没有就是叶子结点
                }*/
                list.add(new ZTreeNode(deps.get(i).getId(),deps.get(i).getPId(),deps.get(i).getName(),true));
            }
        }
        List<User> ifHasUsers = new UserService().getDepUsers(pcode);
        for (int i = 0; i <ifHasUsers.size() ; i++) {
            if(!ifHasUsers.get(i).getUserid().equals(createuser)){//创建的人不能分享给自己
                list.add(new ZTreeNode(ifHasUsers.get(i).getUserid(),ifHasUsers.get(i).getDepcode(),ifHasUsers.get(i).getUsername(),false));
            }
        }
        return list;
    }

    /**
     * 返回权限管理查询结果
     * @throws IOException
     */
    public void searchList() throws IOException{
        RightControlService rightControlService = new RightControlService();
        HttpServletRequest req = this.getRequest();
        String keyword = PubInfo.getString(req.getParameter("keyword"));
        String icode = PubInfo.getString(req.getParameter("icode"));
        String id = PubInfo.getString(req.getParameter("id"));
        String sort = PubInfo.getString(req.getParameter("sort"));
        IndexListService indexListService =new IndexListService();
        String createuser = indexListService.getData(icode).getCreateuser();//搜索结果排除计划创建人
        List<Map<String,String>> datas = new ArrayList<>();
        if (!StringUtil.isEmpty(id)) {
            List<Map<String,String>> temp = rightControlService.getSearchList(StringUtil.toLowerString(keyword),createuser);
            if(sort.equals("true")){ //要是树点击的是组织，就要去查
                List<String> treeDepList = new ArrayList<>();
                treeDepList.add(id);
                treeDepList.addAll(getAllDepSubs(id));
                List<String> treeUserList = getAllUserSubs(treeDepList,createuser);
                for (int i = 0; i <temp.size() ; i++) {
                    if(treeDepList.contains(temp.get(i).get("depusercode"))&&temp.get(i).get("sort").equals("1")){//code对上并且是组织
                        datas.add(temp.get(i));
                    }
                    if(treeUserList.contains(temp.get(i).get("depusercode"))&&temp.get(i).get("sort").equals("2")){//code对上并且是用户
                        datas.add(temp.get(i));
                    }
                }
            }
           else{
                for (int i = 0; i <temp.size() ; i++) {
                    if(id.equals(temp.get(i).get("depusercode"))&&temp.get(i).get("sort").equals("2")){//code对上并且是用户
                        datas.add(temp.get(i));
                    }
                }
            }

        }
        else{
            datas = rightControlService.getSearchList(StringUtil.toLowerString(keyword),createuser);
        }
        this.sendJson(datas);
    }

    /**
     * 给某组织的id获取该组织下的所有组织，包括它下级的下级
     */
    public static List<String> getAllDepSubs(String procode){
        List<String> lists = new ArrayList<>();
        List<ZTreeNode> temp = new DepartmentService().getSubDepartments(procode);
        for (int i = 0; i <temp.size() ; i++) {
            lists.add(temp.get(i).getId());
            if(temp.get(i).isIsParent()){
                lists.addAll(getAllDepSubs(temp.get(i).getId()));
            }
        }
        return lists;
    }

    public List<String> getAllUserSubs(List<String> procodes,String createuser){
        List<String> lists = new ArrayList<>();
        for (int i = 0; i <procodes.size() ; i++) {
            List<User> ifHasUsers = new UserService().getDepUsers(procodes.get(i));
            for (int j = 0; j <ifHasUsers.size() ; j++) {
                if(!ifHasUsers.get(j).getUserid().equals(createuser)){
                    lists.add(ifHasUsers.get(j).getUserid());
                }
            }
        }
        return lists;
    }

    /**
     * 权限管理的保存按钮
     */
    public void rightListSave() throws IOException {
        RightControlService rightControlService = new RightControlService();
        HttpServletRequest req=this.getRequest();
        String rightlists = PubInfo.getString(req.getParameter("rightlist"));
        String icode = PubInfo.getString(req.getParameter("icode"));
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        if(rightlists != ""){
            String[] rightlist = rightlists.split(";#");
            for (int i = 0; i <rightlist.length ; i++) {
                Map<String,String> arr = new HashMap<String, String>();
               String[] temp = rightlist[i].split(",");
               arr.put("indexcode",icode);
               arr.put("sort",temp[0]);
               arr.put("depusercode",temp[1]);
               arr.put("right",temp[2]);
               arr.put("createuser",temp[3]);
               list.add(arr);
            }
        }
        int back = rightControlService.saveRightList(icode,list);
        JSONReturnData data = new JSONReturnData("");
        if(back == 1){
            data.setReturncode(200);
            this.sendJson(data);
            return;
        }
        else{
            data.setReturncode(300);
            this.sendJson(data);
            return;
        }
    }
    //权限管理-------end
    
    /** 
    * @Description: 检查计划的模型是否有空目录 
    * @Param: [] 
    * @return: void 
    * @Author: lyh
    * @Date: 2018/10/17 
    */ 
    public void checkModuleCat() throws IOException {
        HttpServletRequest req=this.getRequest();
        String icode= req.getParameter("icode");
        IndexListService indexListService=new IndexListService();
        this.sendJson(indexListService.checkModuleCat(icode));
    }
    /**
     * 收到的指数 复制到
     * @author wf
     * @date
     * @param
     * @return
     */
    public void sharecopy() throws IOException {
        IndexListService indexListService = new IndexListService();
        User cu= UserService.getCurrentUser();
        String usercode=cu.getUserid();
        HttpServletRequest req = this.getRequest();
        String cpcode = PubInfo.getString(req.getParameter("cosharecode"));//原code
        //String ifdata1 = PubInfo.getString(req.getParameter("cifdata"));
        //int ifdata = Integer.parseInt(ifdata1);
        String code = PubInfo.getString(req.getParameter("newsharecode"));
        String cname = PubInfo.getString(req.getParameter("putname"));
        JSONReturnData data = new JSONReturnData("");
        int x = indexListService.checkCode(code);
        int y = indexListService.checkCname(1,usercode,cname);//0是目录，1是计划
        if (x == 0) {
            data.setReturncode(300);
            data.setReturndata("该编码已存在");
            this.sendJson(data);
            return;
        } else if(y == 0){
            data.setReturncode(301);
            data.setReturndata("该名称已存在");
            this.sendJson(data);
            return;
        }
        else {
            data.setReturncode(200);
        }
        String nprocode = PubInfo.getString(req.getParameter("shareprocode"));
        //String createtime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        DataTableRow row= IndexListDao.Fator.getInstance().getIndexdatadao().getByCode(cpcode).getRows().get(0);
        String start = row.getString("startperiod");
        String delay = row.getString("delayday");
        String getplan = row.getString("planperiod");
        String state = "0";
        if(start.equals("")||delay.equals("")||getplan.equals("")){
            IndexList data1 = indexListService.getData(cpcode);
            String createtime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            data1.setCode(code);
            data1.setCname(cname);
            data1.setProcode(nprocode);
            data1.setState(state);
            data1.setCreateuser(usercode);
            data1.setCreatetime(createtime);
            data1.setUpdatetime(createtime);
            indexListService.addCp(data1);
            data.setReturndata(data1);
            this.sendJson(data);
        } else {
            IndexList copydata = indexListService.getData(cpcode);
            copydata.setState(state);
            copydata.setCode(code);
            copydata.setCname(cname);
            copydata.setProcode(nprocode);
            copydata.setCreateuser(usercode);
            //data1.setCreatetime(createtime);
            String startpeirod = copydata.getStartperiod();
            String delayday = copydata.getDelayday();
            //生成plantime，planperiod
            if (startpeirod.length()==4){
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(startpeirod)+1);
                calendar.set(Calendar.MONTH,0);
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                calendar.set(Calendar.HOUR_OF_DAY,0);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);
                // calendar.set(Calendar.MILLISECOND,0);
                Date time=calendar.getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String plantime=df.format(time);
                copydata.setPlantime(plantime);
                String planperiod=startpeirod;
                copydata.setPlanperiod(planperiod);

            }
            else if (startpeirod.length()==5){
                String q=startpeirod.substring(4);
                String year=startpeirod.substring(0,4);
                Calendar calendar=Calendar.getInstance();
                if (q.equals("D")){
                    calendar.set(Calendar.YEAR, Integer.parseInt(year)+1);
                    calendar.set(Calendar.MONTH,0);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    copydata.setPlantime(plantime);
                    String planperiod=startpeirod;
                    copydata.setPlanperiod(planperiod);
                }
                else if (q.equals("A")){
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                    calendar.set(Calendar.MONTH,3);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    copydata.setPlantime(plantime);
                    String planperiod=startpeirod;
                    copydata.setPlanperiod(planperiod);
                }
                else if (q.equals("B")){
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                    calendar.set(Calendar.MONTH,6);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    copydata.setPlantime(plantime);
                    String planperiod=startpeirod;
                    copydata.setPlanperiod(planperiod);
                }
                else {
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                    calendar.set(Calendar.MONTH,9);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    copydata.setPlantime(plantime);
                    String planperiod=startpeirod;
                    copydata.setPlanperiod(planperiod);
                }
            }
            else {
                String year=startpeirod.substring(0,4);
                String mon=startpeirod.substring(4);
                Calendar calendar=Calendar.getInstance();
                if (mon.equals("12")){
                    calendar.set(Calendar.YEAR, Integer.parseInt(year)+1);
                    calendar.set(Calendar.MONTH,0);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    copydata.setPlantime(plantime);
                    String planperiod=startpeirod;
                    copydata.setPlanperiod(planperiod);
                }
                else {
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                    calendar.set(Calendar.MONTH,Integer.parseInt(mon));
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    copydata.setPlantime(plantime);
                    String planperiod=startpeirod;
                    copydata.setPlanperiod(planperiod);
                }
            }
            indexListService.addCopyShare(cpcode,copydata);
            indexListService.switchFormu(code,cpcode);
        /*if (int1 == -1) {
            data.setReturncode(501);
            data.setReturndata("fail");
            this.sendJson(data);
            return;
        }*/
            data.setReturndata(copydata);
            this.sendJson(data);
        }
        }


}

