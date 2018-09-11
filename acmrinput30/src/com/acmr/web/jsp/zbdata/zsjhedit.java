package com.acmr.web.jsp.zbdata;

import acmr.cubequery.service.cubequery.entity.CubeNode;
import acmr.cubequery.service.cubequery.entity.CubeQueryData;
import acmr.cubequery.service.cubequery.entity.CubeUnit;
import acmr.cubequery.service.cubequery.entity.CubeWdCodes;

import acmr.excel.ExcelException;
import acmr.excel.pojo.Constants.XLSTYPE;
import acmr.excel.pojo.ExcelBook;
import acmr.excel.pojo.ExcelCell;
import acmr.excel.pojo.ExcelRow;
import acmr.excel.pojo.ExcelSheet;
import acmr.util.PubInfo;

import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.pub.TreeNode;
import com.acmr.model.zhzs.IndexList;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.model.zhzs.IndexZb;
import com.acmr.service.zbdata.OriginService;
import com.acmr.service.zbdata.RegdataService;
import com.acmr.service.zbdata.ZBdataService;
import com.acmr.service.zhzs.IndexEditService;
import com.acmr.service.zhzs.IndexListService;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


public class zsjhedit extends BaseAction {
    //指数计划编辑页面
    /**
     * 获取service层对象（cube）
     *
     * @author chenyf
     */
    private List data1;
    private  String regname;
    private  List singledata1;
    private  String singleregname;
    private  String excelsj;

    static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
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
        List<Map> regs = regshow(code);
        return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("proname",proname).addObject("list",list).addObject("indexlist",indexlist).addObject("zbs",zbs).addObject("regs",regs);
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
        regname = PubInfo.getString(req.getParameter("regname"));//地区名称
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
        data1 = new ArrayList();
        String checkresult =CheckResult(regs,sjs,zbcodes,dss,cos);
        for (int i = 0; i <sjs.length ; i++) {
            for (int j = 0; j <zbcodes.length ; j++) {
                List <String> datas=new ArrayList();
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
                /*for (int k = datas.size(); k <regs.length+2 ; k++) {//补齐单元格
                    datas.add("0.0");
                }*/
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
                    if(result2 == 0.0 ){
                        check ="1";
                    }
                }
            }
            result += check + ",";
        }
        result = result.substring(0,result.length()-1);
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
        excelsj = PubInfo.getString(req.getParameter("sj"));//时间
        String zbcode = PubInfo.getString(req.getParameter("zb"));//zbcode
        String zbname = PubInfo.getString(req.getParameter("zbname"));//zbname
        String ds = PubInfo.getString(req.getParameter("ds"));//数据来源
        String co = PubInfo.getString(req.getParameter("co"));//主体
        String zbunit = PubInfo.getString(req.getParameter("zbunit"));//单位
        String check = PubInfo.getString(req.getParameter("checkdata"));
        String [] sjs = excelsj.split(",");
        String [] zbcodes = zbcode.split(",");
        String [] zbnames = zbname.split(",");
        String [] dss = ds.split(",");
        String [] cos = co.split(",");
        String [] units = zbunit.split(",");
        String regname = originService.getwdnode("reg",reg).getName();
        singledata1 = new ArrayList();
        for (int i = 0; i <zbcodes.length ; i++) {
            List <String> datas=new ArrayList();
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
            singledata1.add(datas);
        }
        System.out.println(check);
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
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/regSelect").addObject("regname",regname).addObject("singledata",singledata1).addObject("times",sjs).addObject("check",check);
        }
    }
    /**
     * 数据下载
     *
     * @author wf
     * @date
     * @param
     * @return
     */
    /**
     * 全部地区数据下载
     *
     * @author wf
     * @date
     * @param
     * @return
     */
    public void toExcel() throws IOException {
        //接参
        HttpServletRequest req = this.getRequest();
        /*req.getAttribute("excelregs");
        req.getAttribute("exceldata");*/
        /*String regname = PubInfo.getString(req.getParameter("excelregs"));//地区名称
        String [] regnames = regname.split(",");
        String data1=PubInfo.getString(req.getParameter("exceldata"));
        String [] datas = regname.split(",");*/
        JSONReturnData data = new JSONReturnData("");
        if(regname == null || data1 ==null){
            data.setReturncode(300);
            this.sendJson(data);
            return;
        } else {
            data.setReturncode(200);
        }
        String [] regnames = regname.split(",");
        ExcelBook book = new ExcelBook();
        ExcelSheet sheet1 = new ExcelSheet();
        sheet1.setName("sheet1");
        sheet1.addColumn();
        sheet1.addColumn();
        ExcelCell cell1 = new ExcelCell();
        ExcelRow dr1 = sheet1.addRow();
        ExcelCell cell2 = cell1.clone();
        cell2.setCellValue("时间");
        dr1.set(0, cell2);
        cell2 = cell1.clone();
        cell2.setCellValue("指标");
        dr1.set(1, cell2);
        // for (int a=2;a<)
        for (int k = 0; k < regnames.length; k++){
            int m =k+2;
            sheet1.addColumn();
            cell2 = cell1.clone();
            cell2.setCellValue(regnames[k]);
            dr1.set(m, cell2);
        }
        cell1.getCellstyle().getFont().setBoldweight((short) 10);
        //System.out.println(data1);
        for(int i=0;i<data1.size();i++){
            String [] arr =data1.get(i).toString().substring(1,data1.get(i).toString().length()-1).split(",");
            /*String [] arr = (String[]) data1.get(i);
            String [] arr =new String[datas.size()];
            Object ob=data1.get(i);
            Object[] obs = (Object[]) ob;
            String ceshi = obs[2].toString();*/
            dr1 = sheet1.addRow();
            for(int j=0;j<arr.length;j++){
                cell2 = cell1.clone();
                cell2.setCellValue(arr[j]);
                dr1.set(j, cell2);
            }
        }
        book.getSheets().add(sheet1);
        HttpServletResponse resp = this.getResponse();
        resp.reset();
        resp.setContentType("application/vnd.ms-excel");
        resp.setHeader("Pragma", "public");
        resp.setHeader("Cache-Control", "max-age=30");
        resp.addHeader("Content-Disposition", "attachment; filename=" + "index.xlsx");
        try {
            book.saveExcel(resp.getOutputStream(), XLSTYPE.XLSX);
        } catch (ExcelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //单个地区的下载

    public void toExcelsinglereg() throws IOException {
        //接参
        HttpServletRequest req = this.getRequest();
        /*req.getAttribute("excelregs");
        req.getAttribute("exceldata");*/
        /*String regname = PubInfo.getString(req.getParameter("excelregs"));//地区名称
        String [] regnames = regname.split(",");
        String data1=PubInfo.getString(req.getParameter("exceldata"));
        String [] datas = regname.split(",");*/
        JSONReturnData data = new JSONReturnData("");
        if(singledata1 ==null){
            data.setReturncode(300);
            this.sendJson(data);
            return;
        } else {
            data.setReturncode(200);
            this.sendJson(data);
        }
        String [] sj = excelsj.split(",");
        ExcelBook book = new ExcelBook();
        ExcelSheet sheet1 = new ExcelSheet();
        sheet1.setName("sheet1");
        sheet1.addColumn();
        sheet1.addColumn();
        ExcelCell cell1 = new ExcelCell();
        ExcelRow dr1 = sheet1.addRow();
        ExcelCell cell2 = cell1.clone();
        cell2.setCellValue("指标");
        dr1.set(0, cell2);
        // for (int a=2;a<)
        for (int k = 0; k < sj.length; k++){
            int m =k+1;
            sheet1.addColumn();
            cell2 = cell1.clone();
            cell2.setCellValue(sj[k]);
            dr1.set(m, cell2);
        }
        cell1.getCellstyle().getFont().setBoldweight((short) 10);
        //System.out.println(data1);
        for(int i=0;i<singledata1.size();i++){
            String [] arr =singledata1.get(i).toString().substring(1,singledata1.get(i).toString().length()-1).split(",");
            /*String [] arr = (String[]) data1.get(i);
            String [] arr =new String[datas.size()];
            Object ob=data1.get(i);
            Object[] obs = (Object[]) ob;
            String ceshi = obs[2].toString();*/
            dr1 = sheet1.addRow();
            for(int j=0;j<arr.length;j++){
                cell2 = cell1.clone();
                cell2.setCellValue(arr[j]);
                dr1.set(j, cell2);
            }
        }
        book.getSheets().add(sheet1);
        HttpServletResponse resp = this.getResponse();
        resp.reset();
        resp.setContentType("application/vnd.ms-excel");
        resp.setHeader("Pragma", "public");
        resp.setHeader("Cache-Control", "max-age=30");
        resp.addHeader("Content-Disposition", "attachment; filename=" + "singleindex.xlsx");
        try {
            book.saveExcel(resp.getOutputStream(), XLSTYPE.XLSX);
        } catch (ExcelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            String procode = list.getProcode();
            String proname = null;
            if(procode!= null && procode!=""){//处理为空的步骤
                IndexList list1 =indexListService.getData(procode);
                proname = list1.getCname();
            }
            ArrayList<IndexList> indexlist= new IndexListService().getIndexList();

            List<Map> regoins = regshow(code);
            PubInfo.printStr("isempty");
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("zbs",zbs).addObject("list",list).addObject("proname",proname).addObject("indexlist",indexlist).addObject("regs",regoins);
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
            String procode = list.getProcode();
            String proname = null;
            if(procode!= null && procode!=""){//处理为空的步骤
                IndexList list1 =indexListService.getData(procode);
                proname = list1.getCname();
            }
            ArrayList<IndexList> indexlist= new IndexListService().getIndexList();
            List<Map> regs = regshow(icode);
            PubInfo.printStr("isempty");
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("zbs",zbs).addObject("list",list).addObject("proname",proname).addObject("indexlist",indexlist).addObject("regs",regs);
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
        IndexMoudle data = indexEditService.getData(procodeId);
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
        String formula = PubInfo.getString(req.getParameter("formula"));//判断是不是自定义，是指标还是公式
        String formulatext = PubInfo.getString(req.getParameter("formulatext"));
        String ifzb = "";
        if(ifzs.equals("1")){//选了次级指标
            String zs = PubInfo.getString(req.getParameter("cjzs"));//次级指数的所属节点类别
            procodeId = zs;
        }
        else if(ifzs.equals("0")){//要是选了指标
            String zb = PubInfo.getString(req.getParameter("zb_ifzs"));//指标的所属节点类别
            procodeId = zb;
        }
       if(formula.equals("userdefined")){
           ifzb = "0";//0是公式
       }else {
           ifzb = "1";//1是指标
       }
        String sortcode = indexEditService.getCurrentSort(procodeId,indexCode);
        String dacimal = PubInfo.getString(req.getParameter("dotcount"));
        if(checkCode(code)){
            data.setReturncode(501);
            this.sendJson(data); //要是code已经存在
            return;
        }
            if(ifzs.equals("1")||ifzs.equals("2")){
                ifzs = "1";//总指数或者次级指数
                ifzb ="";
                formula="";
            }
            indexMoudle.setCode(code);
            indexMoudle.setCname(name);
            indexMoudle.setProcode(procodeId);
            indexMoudle.setIndexcode(indexCode);
            indexMoudle.setIfzs(ifzs);
            indexMoudle.setDacimal(dacimal);
            indexMoudle.setWeight("0");
            indexMoudle.setSortcode(sortcode);
        if(ifzb.equals("1")){
            indexMoudle.setIfzb(ifzb);
            indexMoudle.setFormula(formula);
        }
           else if(ifzb.equals("0")){
                indexMoudle.setIfzb(ifzb);//是选的自定义公式，要做校验
           if(checkFormula(formulatext,indexCode)){
               formulatext = changeFormula(formulatext,indexCode,"NTC");
               indexMoudle.setFormula(formulatext);
           }else {
               data.setReturncode(300);
               this.sendJson(data);
               return;
           }
            }
        int back = indexEditService.addZStoModel(indexMoudle);
        if(back == 1){
            data.setReturncode(200);
            this.sendJson(data);
            return;
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
            String proname = null;
            String procode = list.getProcode();
            if(procode!= null && procode!=""){//处理为空的步骤
                IndexList list1 =indexListService.getData(procode);
                proname = list1.getCname();
            }
            ArrayList<IndexList> indexlist= new IndexListService().getIndexList();
            List<Map> regs = regshow(icode);
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("zbs",zbs).addObject("list",list).addObject("proname",proname).addObject("indexlist",indexlist).addObject("regs",regs);
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
    /**
     * 校验公式是否合理
     */
    public boolean checkFormula(String str,String icode){
        IndexEditService indexEditService=new IndexEditService();
        //String icode=this.getRequest().getParameter("icode");
        List<Map> zbchoose=indexEditService.getZBS(icode);
        for (int i = 0; i <zbchoose.size() ; i++) {
            String temp = "#"+zbchoose.get(i).get("zbname").toString()+"("+zbchoose.get(i).get("dsname").toString()+","+zbchoose.get(i).get("unitname").toString()+")#";
            str = str.replace(temp," 2.0 ");//随便给个数算
        }
        try {
            System.out.println(jse.eval(str));
        } catch (Exception t) {
            System.out.println("error");
            return false;
        }
        return true;
    }

    /**
     * code 和name互相转换
     * @param str
     * @param icode
     * @return
     */
    public String changeFormula(String str,String icode,String type){
        IndexEditService indexEditService=new IndexEditService();
        //String icode=this.getRequest().getParameter("icode");
        List<Map> zbchoose=indexEditService.getZBS(icode);
        for (int i = 0; i <zbchoose.size() ; i++) {
            String temp = "#"+zbchoose.get(i).get("zbname").toString()+"("+zbchoose.get(i).get("dsname").toString()+","+zbchoose.get(i).get("unitname").toString()+")#";
            if(type.equals("NTC")){
                str = str.replace(temp,"#"+zbchoose.get(i).get("code").toString()+"#");//换成ZB表里的code
            }
            else if(type.equals("CTN")){
                str = str.replace("#"+zbchoose.get(i).get("code").toString()+"#",temp);//换成回显的格式
            }
        }
        return str;
    }

    /**
     * 模型规划编辑页面
     * @return
     * @throws IOException
     */
    public ModelAndView toEditShow()throws IOException{
        HttpServletRequest req = this.getRequest();
        String code = req.getParameter("code");
        String indexCode = req.getParameter("indexCode");
        List<IndexMoudle> zslist = new ArrayList<IndexMoudle>();
        IndexEditService indexEditService = new IndexEditService();
        zslist = indexEditService.getZSList(indexCode);
        IndexMoudle getdata = indexEditService.getData(code);
       /* String procodeId =getdata.getProcode() ;
        String proname = indexEditService.getData(procodeId,indexCode).getCname();*/
        //要是是自定义公式，读取的时候要换成对应的名字
       if (getdata.getIfzb().equals("0")){
           String formula = getdata.getFormula();
           getdata.setFormula(changeFormula(formula,indexCode,"CTN"));
       }
        //筛选指标信息
        JSONObject zblist=getZBS(indexCode);
     //   Map<String, String> datas = new HashMap<String, String>();
      //  datas.put("proname", proname);
//        datas.put("procodeId", getdata.getProcode());
        return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/modelEdit").addObject("icode",indexCode).addObject("zslist",zslist).addObject("zblist",zblist).addObject("data",getdata);
    }
    /**
     * 新增模型节点保存方法
     */
    public void toUpdateZS() throws IOException{
        IndexMoudle indexMoudle = new IndexMoudle();
        IndexEditService indexEditService = new IndexEditService();
        JSONReturnData data = new JSONReturnData("");
        HttpServletRequest req = this.getRequest();
        String procodeId = PubInfo.getString(req.getParameter("zb_ifzs"));
        String indexCode = PubInfo.getString(req.getParameter("icode"));
        String code = PubInfo.getString(req.getParameter("ZS_code"));
        String name = PubInfo.getString(req.getParameter("ZS_cname"));
        String ifzs = PubInfo.getString(req.getParameter("ifzs"));
        String formula = PubInfo.getString(req.getParameter("formula"));//判断是不是自定义，是指标还是公式
        String formulatext = PubInfo.getString(req.getParameter("formulatext"));
        String ifzb = "";
        if(ifzs.equals("1")){//选了次级指标
            String zs = PubInfo.getString(req.getParameter("cjzs"));//次级指数的所属节点类别
            procodeId = zs;
        }
        if(formula.equals("userdefined")){
            ifzb = "0";//0是公式
        }else {
            ifzb = "1";//1是指标
        }
        String dacimal = PubInfo.getString(req.getParameter("dotcount"));
        if(ifzs.equals("1")||ifzs.equals("0")){
            indexMoudle.setProcode(procodeId);
        }
        if(ifzs.equals("1")||ifzs.equals("2")){
            ifzs = "1";//总指数或者次级指数
            ifzb ="";
            formula="";
        }
        indexMoudle.setCode(code);
        indexMoudle.setCname(name);
        indexMoudle.setIndexcode(indexCode);
        indexMoudle.setIfzs(ifzs);
        indexMoudle.setDacimal(dacimal);
        if(ifzb.equals("1")){
            indexMoudle.setIfzb(ifzb);
            indexMoudle.setFormula(formula);
        }
        else if(ifzb.equals("0")){
            indexMoudle.setIfzb(ifzb);//是选的自定义公式，要做校验
            if(checkFormula(formulatext,indexCode)){
                formulatext = changeFormula(formulatext,indexCode,"NTC");
                indexMoudle.setFormula(formulatext);
            }else {
                data.setReturncode(300);
                this.sendJson(data);
                return;
            }
        }
        int back = indexEditService.updateToModel(indexMoudle);
        if(back == 1 || back == 0){
            data.setReturncode(200);
            this.sendJson(data);
            return;
        }

    }


    /**
    * @Description:  判断要删除的筛选指标是否被模型引用
    * @Param: []
    * @return: boolean
    * @Author: lyh
    * @Date: 2018/9/10
    */
    public void checkModule() throws IOException {
        HttpServletRequest req=this.getRequest();
        String code=req.getParameter("code");
       // PubInfo.printStr("==================================code:");
       // PubInfo.printStr(code);
        IndexEditService indexEditService=new IndexEditService();
        Boolean bool=indexEditService.checkModule(code);
        this.sendJson(bool);
    }

   /* *
     * 保存之后地区回显
     * @param icode
     * @return
     * @throws IOException
     */
    public  List<Map> regshow(String icode){
        List<Map> regions = new ArrayList<Map>();
        RegdataService regdataService = new RegdataService();
        IndexEditService indexEditService=new IndexEditService();
        List<Map> zbchoose=indexEditService.getZBS(icode);
        if(zbchoose.size()>0){
            String region =  zbchoose.get(0).get("regcode").toString();
            if (region !=null && region != ""){
                String [] temp = region.split(",");
                for (int i = 0; i <temp.length ; i++) {
                    Map<String, String> items = new HashMap<String, String>();
                    items.put("regcode", temp[i]);
                    items.put("regcname", regdataService.getRegNode("cuscxnd",temp[i]).getCname());
                    regions.add(items);
                }
            }
        }
        return regions;
    }
    /**
     * 保存所有
     */
    public void toSaveAll() throws IOException{
        HttpServletRequest req = this.getRequest();
        IndexEditService indexEditService = new IndexEditService();
        //index表中的信息
        String index_code = PubInfo.getString(req.getParameter("index_code"));//code
        String index_cname = PubInfo.getString(req.getParameter("index_cname"));//code
        String index_procode = PubInfo.getString(req.getParameter("index_procode"));//所属目录
        String startpeirod = PubInfo.getString(req.getParameter("startpeirod"));
        String delayday = PubInfo.getString(req.getParameter("delayday"));
        //ZB表的信息
        String reg = PubInfo.getString(req.getParameter("regselect"));//地区
        String zbcode = PubInfo.getString(req.getParameter("zbcode"));//zbcode
        String ds = PubInfo.getString(req.getParameter("zbds"));//数据来源
        String co = PubInfo.getString(req.getParameter("zbco"));//主体
        String zbunit = PubInfo.getString(req.getParameter("zbunit"));//单位
        String sxcode = PubInfo.getString(req.getParameter("sxcode"));//ZB表的code
        String [] zbcodes = zbcode.split(",");
        String [] dss = ds.split(",");
        String [] cos = co.split(",");
        String [] units = zbunit.split(",");
        String [] sxcodes = sxcode.split(",");
        JSONReturnData data = new JSONReturnData("");
        //前三个标签页的数据都能取到，分别存到zb和index表里
        ArrayList<IndexZb> zbs = new ArrayList<IndexZb>();
        if(sxcode != ""){
            for (int i = 0; i <sxcodes.length ; i++) {
                IndexZb zb = new IndexZb();
                zb.setCode(sxcodes[i]);
                zb.setZbcode(zbcodes[i]);
                zb.setIndexcode(index_code);
                zb.setCompany(cos[i]);
                zb.setDatasource(dss[i]);
                zb.setRegions(reg);
                zb.setUnitcode(units[i]);
                zbs.add(zb);
            }
        }
        //基本信息表的信息
        IndexList indexList = new IndexList();
        indexList.setCode(index_code);
        if(index_procode != null && index_procode !=""){
            indexList.setProcode(index_procode);
        }
        indexList.setCname(index_cname);
        indexList.setStartperiod(startpeirod);
        indexList.setDelayday(delayday);
        int result = indexEditService.toSaveAll(index_code,zbs,indexList);
        if(result ==0){
            data.setReturncode(200);
            this.sendJson(data);
            return;
        }else if(result ==1){
            data.setReturncode(501);
            this.sendJson(data);
            return;
        }
    }

}
