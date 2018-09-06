package com.acmr.web.jsp.zbdata;

import acmr.cubequery.service.cubequery.entity.CubeNode;
import acmr.cubequery.service.cubequery.entity.CubeQueryData;
import acmr.cubequery.service.cubequery.entity.CubeUnit;
import acmr.cubequery.service.cubequery.entity.CubeWdCodes;
import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.pub.TreeNode;
import com.acmr.model.zhzs.IndexList;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.service.zbdata.OriginService;
import com.acmr.service.zbdata.RegdataService;
import com.acmr.service.zbdata.ZBdataService;
import com.acmr.service.zhzs.IndexEditService;
import com.acmr.service.zhzs.IndexListService;
import com.acmr.web.jsp.Index;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

public class zsjhedit extends BaseAction {
    //指数计划编辑页面
    /**
     * 获取service层对象（cube）
     *
     * @author chenyf
     */

    public ModelAndView editIndex(){
        /* 第一个分页显示*/
        String code = this.getRequest().getParameter("id");
        String proname =null;
        IndexListService indexListService=new IndexListService();
        IndexList list =indexListService.getData(code);
        String procode = list.getProcode();
        if(procode!= null && procode!=""){//处理为空的步骤
            IndexList list1 =indexListService.getData(procode);
             proname = list1.getCname();
        }
        ArrayList<IndexList> indexlist= new IndexListService().getIndexList();
        /* 第一个分页显示*/
        JSONObject zbs=getZBS(code);
        return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("proname",proname).addObject("list",list).addObject("indexlist",indexlist).addObject("zbs",zbs);
    }
    /**
     *
     * 获取树形结构
     *
     *
     * @throws IOException
     * @author chenyf
     */
    public void findZbTree() throws  IOException {
        String code = this.getRequest().getParameter("id");
        if (StringUtil.isEmpty(code)){
            ArrayList<CubeNode> nodes= RegdataService.getRegSubNodes("cuscxnd");
        }
        ArrayList<CubeNode> nodes= RegdataService.getRegSubNodes("cuscxnd",code);
        List<TreeNode> list = new ArrayList<TreeNode>();
        for (int i = 0; i <nodes.size() ; i++) {
            ArrayList<CubeNode> childnodes= RegdataService.getRegSubNodes("cuscxnd",nodes.get(i).getCode());
            TreeNode treeNode = new TreeNode();
            treeNode.setId(nodes.get(i).getCode());
            Boolean ifd ;
            if(childnodes.size()>0){
                ifd = true;
            }else {
                ifd = false;
            }
            treeNode.setIsParent(ifd);
            treeNode.setName(nodes.get(i).getCname());
            treeNode.setPid(code);
            list.add(treeNode);
        }
        this.sendJson(list);
    }

    /**
     * @author djj
     * 查找下一级地区
     * @throws IOException
     */
    public void getResultLeft() throws  IOException{
        HttpServletRequest req = this.getRequest();
        String procode = PubInfo.getString(req.getParameter("procode"));
        if (StringUtil.isEmpty(procode)){
            ArrayList<CubeNode> nodes= RegdataService.getRegSubNodes("cuscxnd");
        }
        ArrayList<CubeNode> nodes= RegdataService.getRegSubNodes("cuscxnd",procode);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <nodes.size() ; i++) {
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put("code", nodes.get(i).getCode());
            obj.put("name", nodes.get(i).getCname());
            data.add(obj);
        }
        this.sendJson(data);
    }
    /**
     * 数据检查
     */
    public ModelAndView getCheckData() {
        HttpServletRequest req = this.getRequest();
        String pjax = req.getHeader("X-PJAX");
        OriginService originService=new OriginService();
        String reg = PubInfo.getString(req.getParameter("reg"));//地区
        String regname = PubInfo.getString(req.getParameter("regname"));//地区名称
        String sj = PubInfo.getString(req.getParameter("sj"));//时间
        String zbcode = PubInfo.getString(req.getParameter("zb"));//zbcode
        String zbname = PubInfo.getString(req.getParameter("zbname"));//zbname
        String ds = PubInfo.getString(req.getParameter("ds"));//数据来源
        String co = PubInfo.getString(req.getParameter("co"));//主体
        String zbunit = PubInfo.getString(req.getParameter("zbunit"));//单位
        //String code=PubInfo.getString(req.getParameter("indexcode"));
        String [] regs = reg.split(",");
        String [] regnames = regname.split(",");
        String [] sjs = sj.split(",");
        String [] zbcodes = zbcode.split(",");
        String [] zbnames = zbname.split(",");
        String [] dss = ds.split(",");
        String [] cos = co.split(",");
        String [] units = zbunit.split(",");
        List data1 = new ArrayList();
        String checkresult =CheckResult(regs,sjs,zbcodes,dss,cos);
        for (int i = 0; i <sjs.length ; i++) {
            for (int j = 0; j <zbcodes.length ; j++) {
                List datas=new ArrayList();
                CubeWdCodes where = new CubeWdCodes();
                String funit=originService.getwdnode("zb",zbcodes[j]).getUnitcode();
                double rate=originService.getRate(funit,units[j],sjs[i]);
                where.Add("zb", zbcodes[j]);
                where.Add("ds", dss[j]);
                where.Add("co", cos[j]);
                where.Add("reg", Arrays.asList(regs));
                where.Add("sj", sjs[i]);
                ArrayList<CubeQueryData> result = RegdataService.queryData("cuscxnd",where);
                datas.add(sjs[i]);//获取时间
                datas.add(zbnames[j]);//获取地区
                for (int k = 0; k <result.size() ; k++) {
                    double resulttemp = result.get(k).getData().getData()*rate;
                    datas.add(resulttemp+"") ;
                }
                data1.add(datas);
            }
        }
        /**
         * 检查数据是否完整
         */

        if (StringUtil.isEmpty(pjax)) {
            String code=req.getParameter("indexcode");
            JSONObject zbs=getZBS(code);
            IndexListService indexListService=new IndexListService();
            IndexList list =indexListService.getData(code);
            String proname =null;
            String procode = list.getProcode();
            if(procode!= null && procode!=""){//处理为空的步骤
                IndexList list1 =indexListService.getData(procode);
                proname = list1.getCname();
            }
            ArrayList<IndexList> indexlist= new IndexListService().getIndexList();
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("list",list).addObject("zbs",zbs).addObject("indexlist",indexlist).addObject("proname",proname);
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/regSelect").addObject("regs",regnames).addObject("data",data1).addObject("check",checkresult);
        }
    }

    /**
     * 先按指标、地区再按时间检查
     */
    public String CheckResult(String [] regs,String [] sjs,String [] zbcodes,String [] dss,String [] cos){
        String result="";
        for (int i = 0; i <regs.length ; i++) {
            String check = "0";
            for (int j = 0; j <zbcodes.length ; j++) {
                CubeWdCodes where = new CubeWdCodes();
                where.Add("zb", zbcodes[j]);
                where.Add("ds", dss[j]);
                where.Add("co", cos[j]);
                where.Add("reg", regs[i]);
                where.Add("sj", Arrays.asList(sjs));
                ArrayList<CubeQueryData> result1 = RegdataService.queryData("cuscxnd",where);
                for (int k = 0; k <result1.size() ; k++) {
                    double result2 = result1.get(k).getData().getData();
                    if(result2 == 0.0){
                        check ="1";
                    }
                }
            }
            result += check + ",";
        }
        return result;
    }
    /**
     * 查单个地区的情况
     */
    public ModelAndView getCheckSingle(){
        HttpServletRequest req = this.getRequest();
        String pjax = req.getHeader("X-PJAX");
        OriginService originService=new OriginService();
        String reg = PubInfo.getString(req.getParameter("reg"));//地区
        String sj = PubInfo.getString(req.getParameter("sj"));//时间
        String zbcode = PubInfo.getString(req.getParameter("zb"));//zbcode
        String zbname = PubInfo.getString(req.getParameter("zbname"));//zbname
        String ds = PubInfo.getString(req.getParameter("ds"));//数据来源
        String co = PubInfo.getString(req.getParameter("co"));//主体
        String zbunit = PubInfo.getString(req.getParameter("zbunit"));//单位
        String [] sjs = sj.split(",");
        String [] zbcodes = zbcode.split(",");
        String [] zbnames = zbname.split(",");
        String [] dss = ds.split(",");
        String [] cos = co.split(",");
        String [] units = zbunit.split(",");
        String regname = originService.getwdnode("reg",reg).getName();
        List data1 = new ArrayList();
        for (int i = 0; i <zbcodes.length ; i++) {
            List datas=new ArrayList();
            datas.add(zbnames[i]);//获取指标名
            for (int j = 0; j <sjs.length ; j++) {

                CubeWdCodes where = new CubeWdCodes();
                String funit=originService.getwdnode("zb",zbcodes[i]).getUnitcode();
                double rate=originService.getRate(funit,units[i],sjs[j]);
                where.Add("zb", zbcodes[i]);
                where.Add("ds", dss[i]);
                where.Add("co", cos[i]);
                where.Add("reg", reg);
                where.Add("sj", sjs[j]);
                ArrayList<CubeQueryData> result = RegdataService.queryData("cuscxnd",where);
                for (int k = 0; k <result.size() ; k++) {
                    double resulttemp = result.get(k).getData().getData()*rate;
                    datas.add(resulttemp+"") ;
                }
            }
            data1.add(datas);
        }
        if (StringUtil.isEmpty(pjax)) {
            String code=req.getParameter("indexcode");
            JSONObject zbs=getZBS(code);
            IndexListService indexListService=new IndexListService();
            IndexList list =indexListService.getData(code);
            String proname =null;
            String procode = list.getProcode();
            if(procode!= null && procode!=""){//处理为空的步骤
                IndexList list1 =indexListService.getData(procode);
                proname = list1.getCname();
            }
            ArrayList<IndexList> indexlist= new IndexListService().getIndexList();
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("list",list).addObject("zbs",zbs).addObject("indexlist",indexlist).addObject("proname",proname);
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/regSelect").addObject("regname",regname).addObject("singledata",data1).addObject("times",sjs);
        }
    }
    /**
     * @Description: 返回指标树
     * @Param: []
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/30
     */
    public void testTreeNode() throws IOException {
        String id=this.getRequest().getParameter("id");
        ZBdataService zBdataService=new ZBdataService();
        List<TreeNode> list = new ArrayList<TreeNode>();
        List<CubeNode> zbs=new ArrayList<>();
        if(id==""){
            zbs=zBdataService.getZB();
        }
        else {
            zbs=zBdataService.getSubZB(id);
        }
        for (int i=0;i<zbs.size();i++){
            String code=zbs.get(i).getCode();
            String name=zbs.get(i).getName();
            TreeNode treeNode=new TreeNode();
            treeNode.setId(code);
            treeNode.setName(name);
            treeNode.setPId(id);
            if (zBdataService.getSubZB(code).size()>0){
                treeNode.setIsParent(true);
            }
            else treeNode.setIsParent(false);
            list.add(treeNode);
        }
        for(int j=0;j<list.size();j++){
            PubInfo.printStr(list.get(j).getId());
        }
        this.sendJson(list);

    }

    /**
     * @Description: 返回搜索
     * @Param: []
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/30
     */
    public void testFind() throws IOException {
        System.out.println("=====================find");
        String query=this.getRequest().getParameter("query");
        ZBdataService zBdataService=new ZBdataService();
        List<CubeNode> nodes=zBdataService.findZB(query);
        List<CubeNode> zbs=new ArrayList<>();
        for (int i=0;i<nodes.size();i++){
            if(zBdataService.getSubZB(nodes.get(i).getCode()).size()==0){
                zbs.add(nodes.get(i));
            }
        }
        this.sendJson(zbs);

    }
    /**
     * @Description: 返回指标的path
     * @Param: []
     * @return: void
     * @Author: lyh
     * @Date: 2018/9/2
     */
    public void getZBPath() throws IOException {
        ZBdataService zBdataService=new ZBdataService();
        String code=this.getRequest().getParameter("code");
        List<String> path=zBdataService.getZBPath(code);
        this.sendJson(path);
    }

    /**
     * @Description: 返回有数的ds，co，以及指标对应的unit
     * @Param: []
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/30
     */
    public  void getDsCoUnit() throws IOException {
        String code=this.getRequest().getParameter("zbcode");
        // String code="888cab11761ccd4f77b12477c011e6e8188350f4";
        ZBdataService zBdataService=new ZBdataService();
        OriginService originService=new OriginService();
        List<String> dscodes=zBdataService.getHasDataNodeO(code,"ds");
        List<Map> dss=new ArrayList<>();
        PutMap(dss,dscodes,"ds");
        List<String> cocodes=zBdataService.getHasDataNodeO(code,"co");
        List<Map> cos=new ArrayList<>();
        PutMap(cos,cocodes,"co");
        CubeNode ZB=originService.getwdnode("zb",code);
        String unitcode=ZB.getUnitcode();
        String unitname=ZB.getUnitname();
        List<CubeUnit> unitlist=originService.getUnitList(unitcode);
        List<Map> units=new ArrayList<>();
        for(int i=0;i<unitlist.size();i++){
            Map jsonObj=new HashMap <String,String>();
            jsonObj.put(unitlist.get(i).getCode(),unitlist.get(i).getName());
            units.add(jsonObj);
        }

        JSONObject obj=new JSONObject();
        obj.put("ds",dss);
        obj.put("co",cos);
        obj.put("unit",units);
        this.sendJson(obj);
    }
    public  void PutMap(List<Map> map, List<String> codes,String wcode){
        OriginService originService=new OriginService();

        for(int i=0;i<codes.size();i++){
            String code=codes.get(i);
            String name=originService.getwdnode(wcode,code).getName();
            Map jsonObj=new HashMap <String,String>();
            jsonObj.put(code,name);
            map.add(jsonObj);
        }
    }

    /**
     * @Description: 返回该指标的已有筛选条件
     * @Param: []
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/30
     */
    public JSONObject getZBS(String icode){
        IndexEditService indexEditService=new IndexEditService();
        //String icode=this.getRequest().getParameter("icode");
        List<Map> zbchoose=indexEditService.getZBS(icode);
        JSONObject obj=new JSONObject();
        obj.put("zbchoose",zbchoose);
        return obj;
    }
    /**
     * @Description: 根据条件返回数据预览
     * @Param: []
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/31
     */

    public ModelAndView getDataTest(){
        HttpServletRequest req = this.getRequest();
        // 获取查询数据
        String zbcode = req.getParameter("zbcode");
        String dscode = req.getParameter("dscode");
        String cocode = req.getParameter("cocode");
        String unitcode = req.getParameter("unitcode");
        // 判断是否pjax 请求
        String pjax = req.getHeader("X-PJAX");
        ZBdataService zBdataService=new ZBdataService();
        List<String> sjs=zBdataService.getHasDataNodeO(zbcode,"sj");
        List<String> regs=zBdataService.getHasDataNodeO(zbcode,"reg");
        OriginService originService=new OriginService();
        CubeWdCodes where = new CubeWdCodes();
        /*
        where.Add("sj",sjs);
        where.Add("reg",regs);
        List<CubeQueryData> datas=originService.querydata(where);*/
        //PubInfo.printStr(datas.toString());
        String funit=originService.getwdnode("zb",zbcode).getUnitcode();
        List<Double> rates=new ArrayList<>();
        for (int i=0;i<sjs.size();i++){
            String sj=sjs.get(i);
            double rate=originService.getRate(funit,unitcode,sj);
            rates.add(rate);
        }
        PubInfo.printStr(rates.toString());
        List<List<String>> rows=new ArrayList<>();
        for(int i=0;i<regs.size();i++){
            List<String> row=new ArrayList<>();
            if(originService.getwdnode("reg",regs.get(i))!=null){
                row.add(originService.getwdnode("reg",regs.get(i)).getName());
                for(int j=0;j<sjs.size();j++){
                    where.Add("zb",zbcode);
                    where.Add("ds",dscode);
                    where.Add("co",cocode);
                    where.Add("sj",sjs.get(j));
                    where.Add("reg",regs.get(i));
                    double data=0;
                    if(originService.querydata(where).size()>0){
                        data=originService.querydata(where).get(0).getData().getData()*rates.get(j);
                    }
                    row.add(data+"");
                    where.Clear();
                }

                rows.add(row);
            }

        }
        PubInfo.printStr(rows.toString());
        String nodata="";
        if (rows.size()==0){
            nodata="该筛选条件暂时无值";
        }
        PubInfo.printStr(nodata);

        if (StringUtil.isEmpty(pjax)) {
            String code=req.getParameter("indexcode");
            JSONObject zbs=getZBS(code);
            IndexListService indexListService=new IndexListService();
            IndexList list =indexListService.getData(code);
            PubInfo.printStr("isempty");
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("zbs",zbs).addObject("list",list);
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/ZBdataList").addObject("sjs",sjs).addObject("rows",rows).addObject("nodata",nodata);
        }
    }
    
    /** 
    * @Description: 返回指标的path
    * @Param: [] 
    * @return: void 
    * @Author: lyh
    * @Date: 2018/9/3 
    */ 
    public void getZBpath() throws IOException {
        String code=this.getRequest().getParameter("code");
        ZBdataService zBdataService=new ZBdataService();
        List<String> path=zBdataService.getZBPath(code);
        this.sendJson(path);
    }

    /**
    * @Description: 返回模型树
    * @Param: []
    * @return: void
    * @Author: lyh
    * @Date: 2018/9/4
    */
    public void getModTree() throws IOException {
        HttpServletRequest req=this.getRequest();
        String icode=req.getParameter("icode");
        String code=req.getParameter("id");
        IndexEditService indexEditService=new IndexEditService();
        List<IndexMoudle> mods=indexEditService.getSubMod(code,icode);
        List<TreeNode> list = new ArrayList<TreeNode>();
        for (int i=0;i<mods.size();i++){
            TreeNode node=new TreeNode();
            node.setPId(mods.get(i).getProcode());
            node.setId(mods.get(i).getCode());
            node.setName(mods.get(i).getCname());
            if (mods.get(i).getIfzs().equals("1")){
                node.setIsParent(true);
            }
            else node.setIsParent(false);
            list.add(node);
        }
        this.sendJson(list);

    }
    
    /** 
    * @Description: 根据各种情况返回模型列表的list 
    * @Param: [] 
    * @return: void 
    * @Author: lyh
    * @Date: 2018/9/4 
    */ 
    public ModelAndView getModList(){
        HttpServletRequest req = this.getRequest();
        String pjax = req.getHeader("X-PJAX");
        String icode=req.getParameter("icode");
        String code=req.getParameter("code");
        IndexEditService indexEditService=new IndexEditService();
        List<IndexMoudle> mods=indexEditService.getSubMod(code,icode);
        if (StringUtil.isEmpty(pjax)) {
            JSONObject zbs=getZBS(icode);
            IndexListService indexListService=new IndexListService();
            IndexList list =indexListService.getData(icode);
            PubInfo.printStr("isempty");
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("zbs",zbs).addObject("list",list);
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/modTableList").addObject("mods",mods);
        }
    }
    
    /** 
    * @Description: 根据code删除模型节点
    * @Param: [] 
    * @return: void 
    * @Author: lyh
    * @Date: 2018/9/5 
    */ 
    public void deleteMod() throws IOException {
        HttpServletRequest req=this.getRequest();
        String code=req.getParameter("code");
        String icode=req.getParameter("indexcode");
        PubInfo.printStr(code);
        IndexEditService indexEditService=new IndexEditService();
        int m=0;
        if (indexEditService.getSubMod(code,icode).size()==0){
            m=indexEditService.deleteMod(code);
        }
        else {
            m=0;
        }
        this.sendJson(m);
    }

    /**
    * @Description: 根据传来的string里的code顺序，对应的节点重新排序
    * @Param: []
    * @return: void
    * @Author: lyh
    * @Date: 2018/9/5
    */
    public void resort(){
        HttpServletRequest req=this.getRequest();
        String codestring=req.getParameter("codes");
        PubInfo.printStr(codestring);
        List<String> codes=new ArrayList<>();
        codes= Arrays.asList(codestring.split(","));
        IndexEditService indexEditService=new IndexEditService();
        indexEditService.resort(codes);
    }
    /**
     * 模型规划新增节点
     */
    public void toAdd()throws IOException{
        HttpServletRequest req = this.getRequest();
        String procodeId = req.getParameter("procodeId");
        String procodeName = req.getParameter("procodeName");
        String indexCode = req.getParameter("indexCode");
        if(procodeId =="" || procodeId ==null){
            String rs ="1";
            this.sendJson(rs);
        }
        IndexEditService indexEditService=new IndexEditService();
        IndexMoudle data = indexEditService.getData(procodeId,indexCode);
        String result = data.getIfzs();
        this.sendJson(result);
    }
    /**
     * 模型规划新增节点展示页面
     */
    public ModelAndView toAddShow()throws IOException{
        HttpServletRequest req = this.getRequest();
        String procodeId = req.getParameter("procodeId");
        String procodeName = req.getParameter("procodeName");
        String indexCode = req.getParameter("indexCode");
        List<IndexMoudle> zslist = new ArrayList<IndexMoudle>();
        IndexEditService indexEditService = new IndexEditService();
        zslist = indexEditService.getZSList(indexCode);
        //筛选指标信息
        JSONObject zblist=getZBS(indexCode);
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("procodeId", procodeId);
        datas.put("procodeName", procodeName);
        datas.put("indexCode", indexCode);
        return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/toAddZB").addObject("datas",datas).addObject("zslist",zslist).addObject("zblist",zblist);
    }
    /**
     * 新增模型节点保存方法
     */
    public void toSaveZS() throws IOException{
        IndexMoudle indexMoudle = new IndexMoudle();
        IndexEditService indexEditService = new IndexEditService();
        JSONReturnData data = new JSONReturnData("");
        HttpServletRequest req = this.getRequest();
        String procodeId = PubInfo.getString(req.getParameter("procodeId"));
        String indexCode = PubInfo.getString(req.getParameter("icode"));
        String code = PubInfo.getString(req.getParameter("ZS_code"));
        String name = PubInfo.getString(req.getParameter("ZS_cname"));
        String ifzs = PubInfo.getString(req.getParameter("ifzs"));
        if(ifzs.equals("1")){//选了次级指标
            String zs = PubInfo.getString(req.getParameter("cjzs"));//次级指数的所属节点类别
            procodeId = zs;
        }
        else if(ifzs.equals("0")){//要是选了指标
            String zb = PubInfo.getString(req.getParameter("zb_ifzs"));//指标的所属节点类别
            procodeId = zb;
        }
        String ifzb = "1";//是指标
        String formula = "";
        String sortcode = indexEditService.getCurrentSort(procodeId,indexCode);
        String dacimal = PubInfo.getString(req.getParameter("dotcount"));
        if(checkCode(code)){
            data.setReturncode(501);
            this.sendJson(data); //要是code已经存在
        }
        else {
            if(ifzs != "0"){
                ifzs = "1";
            }
            indexMoudle.setCode(code);
            indexMoudle.setCname(name);
            indexMoudle.setProcode(procodeId);
            indexMoudle.setIndexcode(indexCode);
            indexMoudle.setIfzs(ifzs);
            indexMoudle.setDacimal(dacimal);
            indexMoudle.setWeight("0");
            indexMoudle.setSortcode(sortcode);
        if(ifzb.equals(1)){
            indexMoudle.setIfzb(ifzb);
            indexMoudle.setFormula(formula);
        }
        int back = indexEditService.addZStoModel(indexMoudle);
        if(back == 1){
            data.setReturncode(200);
            this.sendJson(data);
        }
        }
    }
    /**
     * 综合指数查找
     * @return
     * @throws IOException
     */
    public ModelAndView searchFind() throws IOException{
        HttpServletRequest req = this.getRequest();
        ArrayList<IndexMoudle> mods= new ArrayList<IndexMoudle>();
        // 获取查询数据
        String zs_code = StringUtil.toLowerString(req.getParameter("zs_code"));
        String zs_cname = StringUtil.toLowerString(req.getParameter("zs_cname"));
        String icode = req.getParameter("icode");
        // 判断是否pjax 请求
        String pjax = req.getHeader("X-PJAX");
        if (!StringUtil.isEmpty(zs_code)) {
            mods= new IndexEditService().found(0,zs_code);
        }
        if (!StringUtil.isEmpty(zs_cname)) {
            mods= new IndexEditService().found(1,zs_cname);
        }
        Map<String, String> codes = new HashMap<String, String>();
        codes.put("zs_code", zs_code);
        codes.put("zs_cname", zs_cname);
        if (StringUtil.isEmpty(pjax)) {
            JSONObject zbs=getZBS(icode);
            IndexListService indexListService=new IndexListService();
            IndexList list =indexListService.getData(icode);
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("zbs",zbs).addObject("list",list);
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/modTableList").addObject("mods",mods).addObject("codes",codes);
        }
    }

    /**
     * 检查code是否存在了，true是已存在
     * @param code
     * @return
     */
    public boolean checkCode(String code){
        IndexEditService indexListService=new IndexEditService();
        return indexListService.checkCode(code);
    }
}
