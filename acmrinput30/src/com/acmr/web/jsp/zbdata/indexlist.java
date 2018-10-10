package com.acmr.web.jsp.zbdata;

import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.pub.PageBean;
import com.acmr.model.pub.TreeNode;
import com.acmr.model.pub.ZTreeNode;
import com.acmr.model.security.User;
import com.acmr.model.zhzs.IndexList;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.service.security.DepartmentService;
import com.acmr.service.security.UserService;
import com.acmr.service.zhzs.CreateTaskService;
import com.acmr.service.zhzs.IndexListService;
import com.acmr.service.zhzs.RightControlService;
import com.acmr.web.jsp.Index;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.omg.CORBA.portable.ValueOutputStream;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class indexlist extends BaseAction {
    public ModelAndView main() throws IOException {

        User cu=UserService.getCurrentUser();
        //PubInfo.printStr("userid:"+cu.getUserid());
        String usercode=cu.getUserid();
        ArrayList<IndexList> allindexlist= new IndexListService().getIndexList();
        IndexListService indexListService=new IndexListService();
        String state="0";
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
        return new ModelAndView("/WEB-INF/jsp/zhzs/indexlist").addObject("page",page).addObject("state",state);
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
        PubInfo.printStr("code:"+code);
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
          //  PubInfo.printStr("isempty");
            this.getResponse().sendRedirect("/zbdata/indexlist.htm");
        } else {
          //  PubInfo.printStr("pjax");
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
        String createtime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
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
        //String createtime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        IndexList data1 = indexListService.getData(cpcode);

        data1.setCode(code);
        data1.setCname(cname);
        data1.setProcode(nprocode);
        //data1.setCreatetime(createtime);
        indexListService.addCopyplan(cpcode,data1);
        /*if (int1 == -1) {
            data.setReturncode(501);
            data.setReturndata("fail");
            this.sendJson(data);
            return;
        }*/
        data.setReturndata(data1);
        this.sendJson(data);
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
        String oldcode = PubInfo.getString(req.getParameter("oldcode"));
        String oldname = PubInfo.getString(req.getParameter("oldname"));
        String code = PubInfo.getString(req.getParameter("editcode"));
        String name = PubInfo.getString(req.getParameter("editcname"));
        String procode = PubInfo.getString(req.getParameter("editprocode"));
        if (procode.equals("")) {
            data.setReturncode(300);
            this.sendJson(data);
            return;
        } else {
            data.setReturncode(200);
        }

        if(oldcode.equals(code) && oldname.equals(name)){
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
        }

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
    public ModelAndView find(){
        User cu=UserService.getCurrentUser();
        String usercode=cu.getUserid();
        HttpServletRequest req = this.getRequest();
        IndexListService indexListService=new IndexListService();
        ArrayList<IndexList> indexAllList= new ArrayList<IndexList>();
        List<IndexList> indexList= new ArrayList<IndexList>();
        // 获取查询数据
        String code = StringUtil.toLowerString(req.getParameter("code"));
        String cname = StringUtil.toLowerString(req.getParameter("cname"));
        // 判断是否pjax 请求
        String pjax = req.getHeader("X-PJAX");

        PageBean<IndexList> page = new PageBean<IndexList>();
        StringBuffer sb = new StringBuffer();
        sb.append(this.getRequest().getRequestURI());
        if (!StringUtil.isEmpty(code)) {
            indexAllList= new IndexListService().found(0,code,usercode);
            indexList= indexListService.found(0,code,usercode,page.getPageNum() - 1,page.getPageSize());
            sb.append("?m=find&code="+code);
        }
        if (!StringUtil.isEmpty(cname)) {
            indexAllList= new IndexListService().found(1,cname,usercode);
            indexList= indexListService.found(1,cname,usercode,page.getPageNum() - 1,page.getPageSize());
            sb.append("?m=find&cname="+cname);
        }
        page.setData(indexList);
        page.setTotalRecorder(indexAllList.size());
        page.setUrl(sb.toString());
        Map<String, String> codes = new HashMap<String, String>();
        codes.put("code", code);
        codes.put("cname", cname);
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
        public ModelAndView shareListFind(){
            User cu=UserService.getCurrentUser();
            String usercode=cu.getUserid();
            HttpServletRequest req = this.getRequest();
            IndexListService indexListService=new IndexListService();
            List<Map<String,String>> indexAllList= new ArrayList<Map<String,String>>();
            List<Map<String,String>> indexList= new ArrayList<Map<String,String>>();
            // 获取查询数据
            String code = StringUtil.toLowerString(req.getParameter("code"));
            String cname = StringUtil.toLowerString(req.getParameter("cname"));
            // 判断是否pjax 请求
            String pjax = req.getHeader("X-PJAX");

            PageBean<Map<String,String>> page = new PageBean<Map<String,String>>();
            StringBuffer sb = new StringBuffer();
            sb.append(this.getRequest().getRequestURI());
            if (!StringUtil.isEmpty(code)) {
                indexAllList= new IndexListService().shareSelect(0,code,usercode);
                indexList= indexListService.shareSelect(0,code,usercode,page.getPageNum() - 1,page.getPageSize());
                sb.append("?m=shareListFind&code="+code);
            }
            if (!StringUtil.isEmpty(cname)) {
                indexAllList= new IndexListService().shareSelect(1,cname,usercode);
                indexList= indexListService.shareSelect(1,cname,usercode,page.getPageNum() - 1,page.getPageSize());
                sb.append("?m=shareListFind&cname="+cname);
            }
            page.setData(indexList);
            page.setTotalRecorder(indexAllList.size());
            page.setUrl(sb.toString());
            Map<String, String> codes = new HashMap<String, String>();
            codes.put("code", code);
            codes.put("cname", cname);
            if (StringUtil.isEmpty(pjax)) {
                return new ModelAndView("/WEB-INF/jsp/zhzs/indexlist").addObject("page",page).addObject("codes",codes).addObject("state","2");
            } else {
                return new ModelAndView("/WEB-INF/jsp/zhzs/indextable").addObject("page",page).addObject("codes",codes).addObject("state","2");
            }
        }

    /**
     * 我收到的指数查找
     */
    public ModelAndView receiveListFind(){
        User cu=UserService.getCurrentUser();
        String usercode=cu.getUserid();
        HttpServletRequest req = this.getRequest();
        IndexListService indexListService=new IndexListService();
        List<Map<String,Object>> indexAllList= new ArrayList<Map<String,Object>>();
        // 获取查询数据
        String code = StringUtil.toLowerString(req.getParameter("code"));
        String cname = StringUtil.toLowerString(req.getParameter("cname"));
        // 判断是否pjax 请求
        String pjax = req.getHeader("X-PJAX");

        PageBean<Map<String,Object>> page = new PageBean<Map<String,Object>>();
        StringBuffer sb = new StringBuffer();
        sb.append(this.getRequest().getRequestURI());
        if (!StringUtil.isEmpty(code)) {
            indexAllList= indexListService.receiveSelect(0,code,usercode);
            sb.append("?m=receiveListFind&code="+code);
        }
        if (!StringUtil.isEmpty(cname)) {
            indexAllList= indexListService.receiveSelect(1,cname,usercode);
            sb.append("?m=receiveListFind&cname="+cname);
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
        Map<String, String> codes = new HashMap<String, String>();
        codes.put("code", code);
        codes.put("cname", cname);
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
            List<ZTreeNode> list=getDepUserTree(pcode);
            this.sendJson(list);
    }
    /**
     * 组织和用户合起来
     * @param pcode
     * @return
     */
    public static List<ZTreeNode> getDepUserTree(String pcode){
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
            list.add(new ZTreeNode(ifHasUsers.get(i).getUserid(),ifHasUsers.get(i).getDepcode(),ifHasUsers.get(i).getUsername(),false));
        }
        return list;
    }

    /**
     * 返回权限管理查询结果
     * @throws IOException
     */
    public void searchList() throws IOException{
        RightControlService rightControlService = new RightControlService();
        HttpServletRequest req=this.getRequest();
        String keyword=PubInfo.getString(req.getParameter("keyword"));
        List<Map<String,String>> datas = rightControlService.getSearchList(StringUtil.toLowerString(keyword));
        this.sendJson(datas);
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
}

