package com.acmr.web.jsp.zbdata;

import acmr.cubequery.service.cubequery.entity.*;

import acmr.excel.ExcelException;
import acmr.excel.pojo.Constants.XLSTYPE;
import acmr.excel.pojo.ExcelBook;
import acmr.excel.pojo.ExcelCell;
import acmr.excel.pojo.ExcelRow;
import acmr.excel.pojo.ExcelSheet;
import acmr.math.CalculateExpression;
import acmr.math.entity.MathException;
import acmr.util.PubInfo;

import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.dao.zhzs.IIndexListDao;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.pub.TreeNode;
import com.acmr.model.zhzs.*;
import com.acmr.service.zbdata.OriginService;
import com.acmr.service.zbdata.RegdataService;
import com.acmr.service.zbdata.ZBdataService;
import com.acmr.service.zhzs.DataPreviewService;
import com.acmr.service.zhzs.IndexEditService;
import com.acmr.service.zhzs.IndexListService;
import com.acmr.service.zhzs.MathService;
import com.alibaba.fastjson.JSONObject;
import com.sun.javafx.collections.MappingChange;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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


    private CalculateExpression ce = new CalculateExpression();
    public ModelAndView main(){
        /* 第一个分页显示*/
        String code = this.getRequest().getParameter("id");
        //获取用户权限
        String right=this.getRequest().getParameter("right");
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
        return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("proname",proname).addObject("list",list).addObject("indexlist",indexlist).addObject("zbs",zbs).addObject("regs",regs).addObject("right",right);
    }
    /**
     *
     * 获取树形结构
     *
     *
     * @throws IOException
     * @author chenyf
     */
    public void findRegTree() throws  IOException {
        String code = this.getRequest().getParameter("id");
        String icode = this.getRequest().getParameter("icode");
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        if (StringUtil.isEmpty(code)){
            ArrayList<CubeNode> nodes= RegdataService.getRegSubNodes(dbcode);
        }
        ArrayList<CubeNode> nodes= RegdataService.getRegSubNodes(dbcode,code);
        List<TreeNode> list = new ArrayList<TreeNode>();
        for (int i = 0; i <nodes.size() ; i++) {
            ArrayList<CubeNode> childnodes= RegdataService.getRegSubNodes(dbcode,nodes.get(i).getCode());
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
        String icode = this.getRequest().getParameter("icode");
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        if (StringUtil.isEmpty(procode)){
            ArrayList<CubeNode> nodes= RegdataService.getRegSubNodes(dbcode);
        }
        ArrayList<CubeNode> nodes= RegdataService.getRegSubNodes(dbcode,procode);
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
        OriginService originService = new OriginService();
        String code=req.getParameter("indexcode");
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(code);
        String reg = PubInfo.getString(req.getParameter("reg"));//地区
        regname = PubInfo.getString(req.getParameter("regname"));//地区名称
        String sj = PubInfo.getString(req.getParameter("sj"));//时间
        String zbcode = PubInfo.getString(req.getParameter("zb"));//zbcode
        String zbname = PubInfo.getString(req.getParameter("zbname"));//zbname
        String ds = PubInfo.getString(req.getParameter("ds"));//数据来源
        String co = PubInfo.getString(req.getParameter("co"));//主体
        String zbunit = PubInfo.getString(req.getParameter("zbunit"));//单位
        //String code=PubInfo.getString(req.getParameter("indexcode"));
        String[] regs = reg.split(",");
        String[] regnames = regname.split(",");
        String[] sjs = sj.split(",");
        String[] zbcodes = zbcode.split(",");
        String[] zbnames = zbname.split(",");
        String[] dss = ds.split(",");
        String[] cos = co.split(",");
        String[] units = zbunit.split(",");
        data1 = new ArrayList();
//        String checkresult = CheckResult(regs, sjs, zbcodes, dss, cos,dbcode);
        for (int i = 0; i < sjs.length; i++) {
            for (int j = 0; j < zbcodes.length; j++) {
                List<String> datas = new ArrayList<>();
                String funit = originService.getwdnode("zb", zbcodes[j],dbcode).getUnitcode();
                BigDecimal rate=new BigDecimal(originService.getRate(funit, units[j], sjs[i]));
                datas.add(sjs[i]);//获取时间
                datas.add(zbnames[j]);//获取指标
                for (int k = 0; k < regs.length; k++) {
                    CubeWdCodes where = new CubeWdCodes();
                    where.Add("zb", zbcodes[j]);
                    where.Add("ds", dss[j]);
                    where.Add("co", cos[j]);
                    where.Add("reg", regs[k]);
                    where.Add("sj", sjs[i]);
                    ArrayList<CubeQueryData> result = RegdataService.queryData(dbcode, where);
                    if(result.size()==0){
                        datas.add("");
                    }else{
                        for (int l = 0; l < result.size(); l++) {
                            if (!result.get(l).getData().toString().equals("")) {
                                BigDecimal resulttemp=(new BigDecimal(result.get(l).getData().getStrdata())).multiply(rate);
                                datas.add(resulttemp + "");
                            } else {
                                datas.add("");
                            }
                        }
                    }
                }
                data1.add(datas);
            }

        }
        /**
         * 检查数据是否完整
         */
        if (StringUtil.isEmpty(pjax)) {
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
            List<Map> region = regshow(code);
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("list",list).addObject("zbs",zbs).addObject("indexlist",indexlist).addObject("proname",proname).addObject("regs",region);
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/regSelect").addObject("regs",regnames).addObject("data",data1);
        }
    }

    /**
     * 先按指标、地区再按时间检查
     */
   /* public String CheckResult(String [] regs,String [] sjs,String [] zbcodes,String [] dss,String [] cos,String dbcode){
        String result="";
        for (int i = 0; i <regs.length ; i++) {
            String check = "0";
            for (int j = 0; j <zbcodes.length ; j++) {
                for (int k = 0; k <sjs.length ; k++) {
                    CubeWdCodes where = new CubeWdCodes();
                    where.Add("zb", zbcodes[j]);
                    where.Add("ds", dss[j]);
                    where.Add("co", cos[j]);
                    where.Add("reg", regs[i]);
                    where.Add("sj", sjs[k]);
                    ArrayList<CubeQueryData> result1 = RegdataService.queryData(dbcode,where);
                    if(result1.size()==0){
                        check ="1";
                    }else {
                        for (int l = 0; l <result1.size() ; l++) {
                            String result2 = result1.get(l).getData().toString();
                            if(result2 =="" ){
                                check ="1";
                            }
                        }
                    }
                }

            }
            result += check + ",";
        }
        result = result.substring(0,result.length()-1);
        return result;
    }*/
    /**
     * 查单个地区的情况
     */
    public ModelAndView getCheckSingle(){
        HttpServletRequest req = this.getRequest();
        String pjax = req.getHeader("X-PJAX");
        OriginService originService=new OriginService();
        String code=req.getParameter("indexcode");
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(code);
        String singlereg = PubInfo.getString(req.getParameter("reg"));//地区
        String excelsj = PubInfo.getString(req.getParameter("sj"));//时间
        String zbcode = PubInfo.getString(req.getParameter("zb"));//zbcode
        String zbname = PubInfo.getString(req.getParameter("zbname"));//zbname
        String ds = PubInfo.getString(req.getParameter("ds"));//数据来源
        String co = PubInfo.getString(req.getParameter("co"));//主体
        String zbunit = PubInfo.getString(req.getParameter("zbunit"));//单位
        String [] sjs = excelsj.split(",");
        String [] zbcodes = zbcode.split(",");
        String [] zbnames = zbname.split(",");
        String [] dss = ds.split(",");
        String [] cos = co.split(",");
        String [] units = zbunit.split(",");
        String regionname = originService.getwdnode("reg",singlereg,dbcode).getName();
        List singledata1 = new ArrayList();
        for (int i = 0; i <zbcodes.length ; i++) {
            List <String> datas=new ArrayList<>();
            datas.add(zbnames[i]);//获取指标名
            for (int j = 0; j <sjs.length ; j++) {

                CubeWdCodes where = new CubeWdCodes();
                String funit=originService.getwdnode("zb",zbcodes[i],dbcode).getUnitcode();
                BigDecimal rate=new BigDecimal(originService.getRate(funit,units[i],sjs[j]));
                where.Add("zb", zbcodes[i]);
                where.Add("ds", dss[i]);
                where.Add("co", cos[i]);
                where.Add("reg", singlereg);
                where.Add("sj", sjs[j]);
                ArrayList<CubeQueryData> result = RegdataService.queryData(dbcode,where);
                if(result.size()==0){
                    datas.add("");
                }else{
                    for (int k = 0; k <result.size() ; k++) {
                        if (!result.get(k).getData().toString().equals("") && result.size()!=0){
                            BigDecimal resulttemp=(new BigDecimal(result.get(k).getData().getStrdata())).multiply(rate);
                            datas.add(resulttemp+"") ;
                        }
                       else{
                            datas.add("");
                        }
                    }
                }
            }
            singledata1.add(datas);
        }
        if (StringUtil.isEmpty(pjax)) {
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
            List<Map> region = regshow(code);
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("list",list).addObject("zbs",zbs).addObject("indexlist",indexlist).addObject("proname",proname).addObject("regs",region);
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/regSelect").addObject("regname",regionname).addObject("singledata",singledata1).addObject("times",sjs);
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
            //String [] arr =data1.get(i).toString().substring(1,data1.get(i).toString().length()-1).split(",");
            /*String [] arr = (String[]) data1.get(i);
            String [] arr =new String[datas.size()];
            Object ob=data1.get(i);
            Object[] obs = (Object[]) ob;
            String ceshi = obs[2].toString();*/
            String arr =data1.get(i).toString().substring(1,data1.get(i).toString().length()-1);
            dr1 = sheet1.addRow();
           // String a2 = arr.replaceAll("0.0"," ");
            String [] a3 = arr.split(",");
            for(int j=0;j<a3.length;j++){
                /*int a = arr[j];
                if(a == 0.0){
                    arr[j].replaceAll("0.0", "-");
                }*/
                //a2[j].replaceAll("0.0", "***");
                cell2 = cell1.clone();
                cell2.setCellValue(a3[j]);
                dr1.set(j, cell2);
            }
        }
        for(int t=0;t<data1.size();t++){
            int n = t+2;
            int w = t+1;
            String arr =data1.get(t).toString().substring(1,data1.get(t).toString().length()-1);
            String [] a3 = arr.split(",");
            //if(w<data1.size() && n<=data1.size()){
            if(w<data1.size()){
                String arr1 = data1.get(w).toString().substring(1,data1.get(w).toString().length()-1);
                String [] a4 = arr1.split(",");
                if(a3[0].equals(a4[0])){
                    sheet1.MergedRegions(w,0,n,0);
                }
            }
        }
        book.getSheets().add(sheet1);
        HttpServletResponse resp = this.getResponse();
        resp.reset();
        resp.setContentType("application/vnd.ms-excel; charset=UTF-8");
        resp.setHeader("Pragma", "public");
        resp.setHeader("Cache-Control", "max-age=30");
        //String name = "计划数据";
        String fileName="指数计划.xlsx";
        fileName=java.net.URLEncoder.encode(fileName, "UTF-8");
        resp.addHeader("Content-Disposition", "attachment; filename="+fileName);
        //Export("application/ms-excel", "订单报表.xls");
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
        OriginService originService=new OriginService();
        String icode = this.getRequest().getParameter("indexcode");
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        String singlereg = PubInfo.getString(req.getParameter("reg"));//地区
        String excelsj = PubInfo.getString(req.getParameter("sj"));//时间
        String zbcode = PubInfo.getString(req.getParameter("zb"));//zbcode
        String zbname = PubInfo.getString(req.getParameter("zbname"));//zbname
        String ds = PubInfo.getString(req.getParameter("ds"));//数据来源
        String co = PubInfo.getString(req.getParameter("co"));//主体
        String zbunit = PubInfo.getString(req.getParameter("zbunit"));//单位
        String [] sjs = excelsj.split(",");
        String [] zbcodes = zbcode.split(",");
        String [] zbnames = zbname.split(",");
        String [] dss = ds.split(",");
        String [] cos = co.split(",");
        String [] units = zbunit.split(",");
        String name = originService.getwdnode("reg",singlereg,dbcode).getName();
        List singledata1 = new ArrayList();
        for (int i = 0; i <zbcodes.length ; i++) {
            List <String> datas=new ArrayList<>();
            datas.add(zbnames[i]);//获取指标名
            for (int j = 0; j <sjs.length ; j++) {

                CubeWdCodes where = new CubeWdCodes();
                String funit=originService.getwdnode("zb",zbcodes[i],dbcode).getUnitcode();
                double rate=originService.getRate(funit,units[i],sjs[j]);
                where.Add("zb", zbcodes[i]);
                where.Add("ds", dss[i]);
                where.Add("co", cos[i]);
                where.Add("reg", singlereg);
                where.Add("sj", sjs[j]);
                ArrayList<CubeQueryData> result = RegdataService.queryData(dbcode,where);
                if(result.size()==0){
                    datas.add("");
                }else{
                    for (int k = 0; k <result.size() ; k++) {
                        if (result.get(k).getData().toString() != ""){
                            double resulttemp = result.get(k).getData().getData()*rate;
                            datas.add(resulttemp+"") ;
                        }
                        else{
                            datas.add("");
                        }
                    }
                }
            }
            singledata1.add(datas);
        }

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
           // String [] arr =singledata1.get(i).toString().substring(1,singledata1.get(i).toString().length()-1).split(",");
            /*String [] arr = (String[]) data1.get(i);
            String [] arr =new String[datas.size()];
            Object ob=data1.get(i);
            Object[] obs = (Object[]) ob;
            String ceshi = obs[2].toString();*/
            dr1 = sheet1.addRow();
            String arr =singledata1.get(i).toString().substring(1,singledata1.get(i).toString().length()-1);
          //  String a2 = arr.replaceAll("0.0"," ");
            String [] a3 = arr.split(",");
            for(int j=0;j<a3.length;j++){
                cell2 = cell1.clone();
                cell2.setCellValue(a3[j]);
                dr1.set(j, cell2);
            }
        }
        book.getSheets().add(sheet1);
        HttpServletResponse resp = this.getResponse();
        resp.reset();
        resp.setContentType("application/vnd.ms-excel; charset=UTF-8");
        resp.setHeader("Pragma", "public");
        resp.setHeader("Cache-Control", "max-age=30");
        String fileName=name;
        fileName=java.net.URLEncoder.encode(fileName, "UTF-8");
        resp.addHeader("Content-Disposition", "attachment; filename="+fileName+".xlsx");
        //resp.addHeader("Content-Disposition", "attachment; filename=" + "singleindex.xlsx");
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
    public void findTreeNode() throws IOException {
        String id=this.getRequest().getParameter("id");
        String icode = this.getRequest().getParameter("icode");
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        ZBdataService zBdataService=new ZBdataService();
        List<TreeNode> list = new ArrayList<TreeNode>();
        List<CubeNode> zbs=new ArrayList<>();
        if(id==""){
            zbs=zBdataService.getZB(dbcode);
        }
        else {
            zbs=zBdataService.getSubZB(id,dbcode);
        }
        for (int i=0;i<zbs.size();i++){
            String code=zbs.get(i).getCode();
            String name=zbs.get(i).getName();
            TreeNode treeNode=new TreeNode();
            treeNode.setId(code);
            treeNode.setName(name);
            treeNode.setPId(id);
            if (zBdataService.getSubZB(code,dbcode).size()>0){
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
     * @Description: 返回指标搜索
     * @Param: []
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/30
     */
    public void ZbFind() throws IOException {
        String query=this.getRequest().getParameter("query");
        String icode = this.getRequest().getParameter("icode");
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        ZBdataService zBdataService=new ZBdataService();
        List<CubeNode> nodes=zBdataService.findZB(query,dbcode);
        List<CubeNode> zbs=new ArrayList<>();
        for (int i=0;i<nodes.size();i++){
            if(zBdataService.getSubZB(nodes.get(i).getCode(),dbcode).size()==0){
                zbs.add(nodes.get(i));
            }
        }
        this.sendJson(zbs);

    }
  /*  *//**
     * @Description: 返回指标的path
     * @Param: []
     * @return: void
     * @Author: lyh
     * @Date: 2018/9/2
     *//*
    public void getZBPath() throws IOException {
        ZBdataService zBdataService=new ZBdataService();
        String code=this.getRequest().getParameter("code");
        String icode = this.getRequest().getParameter("icode");
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        List<String> path=zBdataService.getZBPath(code,dbcode);
        this.sendJson(path);
    }*/

    /**
     * @Description: 返回有数的ds，co，以及指标对应的unit
     * @Param: []
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/30
     */
    public  void getDsCoUnit() throws IOException {
        String code=this.getRequest().getParameter("zbcode");
        String icode = this.getRequest().getParameter("icode");
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        // String code="888cab11761ccd4f77b12477c011e6e8188350f4";
        ZBdataService zBdataService=new ZBdataService();
        OriginService originService=new OriginService();
        List<String> dscodes=zBdataService.getHasDataNodeO(code,"ds",dbcode);
        List<Map> dss=new ArrayList<>();
        PutMap(dss,dscodes,"ds",dbcode);
        List<String> cocodes=zBdataService.getHasDataNodeO(code,"co",dbcode);
        List<Map> cos=new ArrayList<>();
        PutMap(cos,cocodes,"co",dbcode);
        CubeNode ZB=originService.getwdnode("zb",code,dbcode);
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
        obj.put("unitcode",unitcode);
        this.sendJson(obj);
    }
    public  void PutMap(List<Map> map, List<String> codes,String wcode,String dbcode){
        OriginService originService=new OriginService();

        for(int i=0;i<codes.size();i++){
            String code=codes.get(i);
            String name=originService.getwdnode(wcode,code,dbcode).getName();
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

    public ModelAndView getData(){
        HttpServletRequest req = this.getRequest();
        // 获取查询数据
        String zbcode = req.getParameter("zbcode");
        String dscode = req.getParameter("dscode");
        String cocode = req.getParameter("cocode");
        String unitcode = req.getParameter("unitcode");
        String indexcode = req.getParameter("indexcode");
        String sjselect=req.getParameter("sjselect");
        int pagesize= Integer.parseInt(req.getParameter("pagesize"));
        List<String> sjs= Arrays.asList(sjselect.split(","));
        // 判断是否pjax 请求
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(indexcode);
        String pjax = req.getHeader("X-PJAX");
        ZBdataService zBdataService=new ZBdataService();
        List<String> regs=zBdataService.getHasDataReg(zbcode,dscode,cocode,"reg",dbcode);
        int regsize=regs.size();
        OriginService originService=new OriginService();
        CubeWdCodes where = new CubeWdCodes();

        String funit=originService.getwdnode("zb",zbcode,dbcode).getUnitcode();
        List<BigDecimal> rates=new ArrayList<>();
        //sjs排序
        sjs=new IndexEditService().sjSort(sjs);
        for (int i=0;i<sjs.size();i++){
            String sj=sjs.get(i);
            BigDecimal rate=new BigDecimal(originService.getRate(funit,unitcode,sj));
            rates.add(rate);
        }
       // PubInfo.printStr(rates.toString());
        List<List<String>> rows=new ArrayList<>();
        //默认只加载pagesize条
        if (regs.size()<pagesize) pagesize=regs.size();
        for(int i=0;i<pagesize;i++){
            List<String> row=new ArrayList<>();
            if(originService.getwdnode("reg",regs.get(i),dbcode)!=null){
                row.add(originService.getwdnode("reg",regs.get(i),dbcode).getName());
                for(int j=0;j<sjs.size();j++){
                    where.Add("zb",zbcode);
                    where.Add("ds",dscode);
                    where.Add("co",cocode);
                    where.Add("sj",sjs.get(j));
                    where.Add("reg",regs.get(i));
                    String odata="";
                    if (originService.querydata(where,dbcode).size()>0){
                        odata=originService.querydata(where,dbcode).get(0).getData().getStrdata();
                    }
                    if(!odata.equals("")){
                        BigDecimal data=(new BigDecimal(odata)).multiply(rates.get(j));
                        row.add(data+"");
                    }
                    else row.add("");
                    where.Clear();
                }

                rows.add(row);
            }
        }
       // PubInfo.printStr(rows.toString());
        String nodata="";
        if (rows.size()==0){
            nodata="该筛选条件暂时无值";
        }
      //  PubInfo.printStr(nodata);

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
           // PubInfo.printStr("isempty");
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("zbs",zbs).addObject("list",list).addObject("proname",proname).addObject("indexlist",indexlist).addObject("regs",regoins);
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/ZBdataList").addObject("sjs",sjs).addObject("rows",rows).addObject("nodata",nodata).addObject("regsize",regsize);
        }
    }

    /**
    * @Description: 根据条件返回数据预览（带页码）
    * @Param: []
    * @return: void
    * @Author: lyh
    * @Date: 2018/11/7
    */
    public void getDataWithPage() throws IOException {
        HttpServletRequest req = this.getRequest();
        // 获取查询数据
        String zbcode = req.getParameter("zbcode");
        String dscode = req.getParameter("dscode");
        String cocode = req.getParameter("cocode");
        String unitcode = req.getParameter("unitcode");
        String indexcode = req.getParameter("indexcode");
        String sjselect=req.getParameter("sjselect");
        int pagesize= Integer.parseInt(req.getParameter("pagesize"));
        int pagenum= Integer.parseInt(req.getParameter("pagenum"));
        List<String> sjs= Arrays.asList(sjselect.split(","));
        // 判断是否pjax 请求
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(indexcode);
        String pjax = req.getHeader("X-PJAX");
        ZBdataService zBdataService=new ZBdataService();
        List<String> regs=zBdataService.getHasDataNodeO(zbcode,"reg",dbcode);
        OriginService originService=new OriginService();
        CubeWdCodes where = new CubeWdCodes();

        String funit=originService.getwdnode("zb",zbcode,dbcode).getUnitcode();
        List<BigDecimal> rates=new ArrayList<>();
        //sjs排序
        sjs=new IndexEditService().sjSort(sjs);
        for (int i=0;i<sjs.size();i++){
            String sj=sjs.get(i);
            BigDecimal rate=new BigDecimal(originService.getRate(funit,unitcode,sj));
            rates.add(rate);
        }
        // PubInfo.printStr(rates.toString());
        List<List<String>> rows=new ArrayList<>();
        int begin=pagenum*pagesize;
        int end=begin+pagesize;
        String nodata="";
        if (begin>regs.size()){
            nodata="全部数据已加载完毕";
            this.sendJson(nodata);
        }
        else {
            if (end>regs.size()) end=regs.size();
            for(int i=begin;i<end;i++){
                List<String> row=new ArrayList<>();
                if(originService.getwdnode("reg",regs.get(i),dbcode)!=null){
                    row.add(originService.getwdnode("reg",regs.get(i),dbcode).getName());
                    for(int j=0;j<sjs.size();j++){
                        where.Add("zb",zbcode);
                        where.Add("ds",dscode);
                        where.Add("co",cocode);
                        where.Add("sj",sjs.get(j));
                        where.Add("reg",regs.get(i));
                        String odata="";
                        if (originService.querydata(where,dbcode).size()>0){
                            odata=originService.querydata(where,dbcode).get(0).getData().getStrdata();
                        }
                        if(!odata.equals("")){
                            BigDecimal data=(new BigDecimal(odata)).multiply(rates.get(j));
                            row.add(data+"");
                        }
                        else row.add("");
                        where.Clear();
                    }
                    rows.add(row);
                }
            }
            this.sendJson(rows);
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
        String icode = this.getRequest().getParameter("icode");
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        ZBdataService zBdataService=new ZBdataService();
        List<String> path=zBdataService.getZBPath(code,dbcode);
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
        if(!code.equals("")){
            IndexMoudle self = indexEditService.getData(code);
            //如果是指标直接返回它自己
            if(self.getIfzs().equals("0")){
                mods.add(self);
            }
        }
        for (int i = 0; i <mods.size() ; i++) {
            if (mods.get(i).getIfzb().equals("0")){
                String formula = mods.get(i).getFormula();
                mods.get(i).setFormula(changeFormula(formula,icode,"CTN"));
            }else  if (mods.get(i).getIfzb().equals("1")){
                String formula = mods.get(i).getFormula();
                mods.get(i).setFormula(formulaShow(formula,icode));
            }
        }
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

        if(ifzs.equals("2")){//如果是总指数，默认权重是1
            indexMoudle.setWeight("1");
        }
        else{
            indexMoudle.setWeight("0");
        }
            if(ifzs.equals("1")||ifzs.equals("2")){
                ifzs = "1";//总指数或者次级指数
                ifzb ="";
                formula="";
            }
        if(indexEditService.checkCname(indexCode,name,ifzs)){
            data.setReturncode(301);
            this.sendJson(data); //要是cname已经存在
            return;
        }
            indexMoudle.setCode(code);
            indexMoudle.setCname(name);
            indexMoudle.setProcode(procodeId);
            indexMoudle.setIndexcode(indexCode);
            indexMoudle.setIfzs(ifzs);
            indexMoudle.setDacimal(dacimal);
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
     * 模型规划查找
     * @return
     * @throws IOException
     */
    public ModelAndView searchFind() throws IOException{
        HttpServletRequest req = this.getRequest();
        ArrayList<IndexMoudle> mods= new ArrayList<IndexMoudle>();
        // 获取查询数据
        IndexListService indexListService =new IndexListService();
        String zs_code = StringUtil.toLowerString(req.getParameter("zs_code"));
        String zs_cname = StringUtil.toLowerString(req.getParameter("zs_cname"));
        String id = req.getParameter("id");
        String icode = req.getParameter("icode");
        // 判断是否pjax 请求
        String pjax = req.getHeader("X-PJAX");
        List<String> treeList = new ArrayList<>() ;
        if (!StringUtil.isEmpty(id)) {//获取这棵树下的子节点的所有下节点的code
            List<IndexMoudle> temp = new IndexEditService().getAllMods(id,icode);
            treeList.add(id);
            for (int i = 0; i <temp.size() ; i++) {
                treeList.add(temp.get(i).getCode());
            }
        }
        if (!StringUtil.isEmpty(zs_code)) {
            if(treeList.size()>0){
                List<IndexMoudle> temp = new IndexEditService().found(0,zs_code,icode);
                for (int i = 0; i <temp.size() ; i++) {
                    if(treeList.contains(temp.get(i).getCode())){
                        mods.add(temp.get(i));
                    }
                }
            }else{
                mods= new IndexEditService().found(0,zs_code,icode);
            }
        }
        if (!StringUtil.isEmpty(zs_cname)) {
            if(treeList.size()>0){
                List<IndexMoudle> temp = new IndexEditService().found(1,zs_cname,icode);
                for (int i = 0; i <temp.size() ; i++) {
                    if(treeList.contains(temp.get(i).getCode())){
                        mods.add(temp.get(i));
                    }
                }
            }else{
             mods= new IndexEditService().found(1,zs_cname,icode);
            }
        }
        if (StringUtil.isEmpty(zs_cname) && StringUtil.isEmpty(zs_code)){

                List<IndexMoudle> tmp = new IndexEditService().getAllMods(id,icode);
            if (!StringUtil.isEmpty(id)){
                mods.add(new IndexEditService().getData(id));
            }
                for (int i = 0; i <tmp.size() ; i++) {
                        mods.add(tmp.get(i));
                }
        }
            Map<String, String> codes = new HashMap<String, String>();
        codes.put("zs_code", zs_code);
        codes.put("zs_cname", zs_cname);

        for (int i = 0; i <mods.size() ; i++) {
            if (mods.get(i).getIfzb().equals("0")){
                String formula = mods.get(i).getFormula();
                mods.get(i).setFormula(changeFormula(formula,icode,"CTN"));
            }else  if (mods.get(i).getIfzb().equals("1")){
                String formula = mods.get(i).getFormula();
                mods.get(i).setFormula(formulaShow(formula,icode));
            }
        }

        if (StringUtil.isEmpty(pjax)) {
            JSONObject zbs=getZBS(icode);

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
        str = str.replace("random()","chance()");//不能用random这个函数名因为有个and会报错
        try {
            ce.setFunctionclass(new MathService());
            System.out.println(ce.Eval(str));
        } catch (MathException e) {
            e.printStackTrace();
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
     * 纯指标替换中文
     */
    public String formulaShow(String str,String icode){
        IndexEditService indexEditService=new IndexEditService();
        List<Map> zbchoose=indexEditService.getZBS(icode);
        for (int i = 0; i <zbchoose.size() ; i++) {
            String temp = zbchoose.get(i).get("zbname").toString()+"("+zbchoose.get(i).get("dsname").toString()+","+zbchoose.get(i).get("unitname").toString()+")";
                str = str.replace(zbchoose.get(i).get("code").toString(),temp);//换成ZB表里的code
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
        zslist = indexEditService.getZSList(indexCode,code);
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
     * 编辑模型节点保存方法
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
        String ifzs = PubInfo.getString(req.getParameter("inputifzs"));
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
        if(indexEditService.checkCname(indexCode,name,ifzs,code)){//名称不重复校验
            data.setReturncode(301);
            this.sendJson(data); //要是cname已经存在
            return;
        }
        String oldpcode = indexEditService.getData(code).getProcode();
        if(!oldpcode.equals(procodeId)){//要是和原来的所属目录不一样
            String sortcode = indexEditService.getCurrentSort(procodeId,indexCode);//去查当前的排序防止变了procode
            indexMoudle.setSortcode(sortcode);
            //原先的procode要重新排
            List<IndexMoudle> subs=indexEditService.getSubMod(oldpcode,indexCode);
            List<String> codes=new ArrayList<>();
            for (int j=0;j<subs.size();j++){
                codes.add(subs.get(j).getCode());
            }
            indexEditService.resort(codes);
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
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        List<Map> zbchoose=indexEditService.getZBS(icode);
        if(zbchoose.size()>0){
            String region =  zbchoose.get(0).get("regcode").toString();
            if (region !=null && region != ""){
                String [] temp = region.split(",");
                for (int i = 0; i <temp.length ; i++) {
                    Map<String, String> items = new HashMap<String, String>();
                    items.put("regcode", temp[i]);
                    items.put("regcname", regdataService.getRegNode(dbcode,temp[i]).getCname());
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
        String index_cname = PubInfo.getString(req.getParameter("index_cname"));//cname
        JSONReturnData data = new JSONReturnData("");
        //先对名字做校验，要是已经存在就返回301
        String usercode = new IndexListService().getData(index_code).getCreateuser();
        int x = new IndexListService().checkCname(1,usercode,index_cname,index_code);
        if (x == 0 ) {
            data.setReturncode(301);
            this.sendJson(data);
            return;
        }

        String index_procode = PubInfo.getString(req.getParameter("index_procode"));//所属目录
        String startpeirod = PubInfo.getString(req.getParameter("startpeirod"));
        String delayday = PubInfo.getString(req.getParameter("delayday"));
        String remark = PubInfo.getString(req.getParameter("remark"));
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

        //前三个标签页的数据都能取到，分别存到zb和index表里
        if(index_procode.equals("!1")){
            index_procode="";
        }
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
        else if(sxcode=="" && (!reg.equals(""))){
            IndexZb zb = new IndexZb();
            zb.setIndexcode(index_code);
            zb.setRegions(reg);
            zbs.add(zb);
        }
        IndexList indexList = new IndexList();
        if(!startpeirod.equals("")) {
            //生成plantime，planperiod
            if (startpeirod.length() == 4) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(startpeirod) + 1);
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                // calendar.set(Calendar.MILLISECOND,0);
                Date time = calendar.getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String plantime = df.format(time);
                indexList.setPlantime(plantime);
                String planperiod = startpeirod;
                indexList.setPlanperiod(planperiod);

            } else if (startpeirod.length() == 5) {
                String q = startpeirod.substring(4);
                String year = startpeirod.substring(0, 4);
                Calendar calendar = Calendar.getInstance();
                if (q.equals("D")) {
                    calendar.set(Calendar.YEAR, Integer.parseInt(year) + 1);
                    calendar.set(Calendar.MONTH, 0);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Date time = calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime = df.format(time);
                    indexList.setPlantime(plantime);
                    String planperiod = startpeirod;
                    indexList.setPlanperiod(planperiod);
                } else if (q.equals("A")) {
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                    calendar.set(Calendar.MONTH, 3);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Date time = calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime = df.format(time);
                    indexList.setPlantime(plantime);
                    String planperiod = startpeirod;
                    indexList.setPlanperiod(planperiod);
                } else if (q.equals("B")) {
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                    calendar.set(Calendar.MONTH, 6);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Date time = calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime = df.format(time);
                    indexList.setPlantime(plantime);
                    String planperiod = startpeirod;
                    indexList.setPlanperiod(planperiod);
                } else {
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                    calendar.set(Calendar.MONTH, 9);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Date time = calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime = df.format(time);
                    indexList.setPlantime(plantime);
                    String planperiod = startpeirod;
                    indexList.setPlanperiod(planperiod);
                }
            } else {
                String year = startpeirod.substring(0, 4);
                String mon = startpeirod.substring(4);
                Calendar calendar = Calendar.getInstance();
                if (mon.equals("12")) {
                    calendar.set(Calendar.YEAR, Integer.parseInt(year) + 1);
                    calendar.set(Calendar.MONTH, 0);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Date time = calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime = df.format(time);
                    indexList.setPlantime(plantime);
                    String planperiod = startpeirod;
                    indexList.setPlanperiod(planperiod);
                } else {
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                    calendar.set(Calendar.MONTH, Integer.parseInt(mon));
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Date time = calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime = df.format(time);
                    indexList.setPlantime(plantime);
                    String planperiod = startpeirod;
                    indexList.setPlanperiod(planperiod);
                }
            }
            }


        //基本信息表的信息
        indexList.setCode(index_code);
        if(index_procode != null && index_procode !=""){
            indexList.setProcode(index_procode);
        }
        indexList.setCname(index_cname);
        indexList.setStartperiod(startpeirod);
        indexList.setDelayday(delayday);
        indexList.setRemark(remark);
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

    /**
     * 以下是指标筛选的时间搜索功能的时间校验和补全
     */
    //===============================start
    public void timeCheck() throws IOException {
        HttpServletRequest req = this.getRequest();
        String time = req.getParameter("timeinput");//得到时间搜索框中的时间
        String sort = req.getParameter("sort"); //看是年度的还是月度的还是季度的
        String icode = req.getParameter("icode"); //看是年度的还是月度的还是季度的
        String zbcode =  req.getParameter("zbcode"); //用于查最新一期数据的时间
        String cocode =  req.getParameter("cocode"); //用于查最新一期数据的时间
        String dscode =  req.getParameter("dscode"); //用于查最新一期数据的时间
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        JSONReturnData data = new JSONReturnData("");
        List<String> timelist = new ArrayList<>();
        IndexListService ls = new IndexListService();

        String[] times = time.split(",");
        for (int i = 0; i <times.length ; i++) {
            if(times[i].equals("")){continue;}
            else  if(checkLast(times[i])){//如果存在last这个字母
                try{
                    int num = Integer.parseInt(times[i].substring(times[i].indexOf("t")+1));
                    //找到最新一期的时间
                    OriginService os = new OriginService();
                    List<CubeWdValue> list1 = new ArrayList<CubeWdValue>();
                    if(!StringUtil.isEmpty(zbcode)){//要是有传指标
                        list1.add(new CubeWdValue("zb",
                                zbcode));
                        if(!StringUtil.isEmpty(cocode)){
                            list1.add(new CubeWdValue("co",
                                    cocode));
                        }
                        if(!StringUtil.isEmpty(cocode)){
                            list1.add(new CubeWdValue("ds",
                                    dscode));
                        }
                        List<String> ets = os.gethasdatawdlist(list1,"sj",dbcode);
                        if(ets.size()>0){
                            ets=new IndexEditService().sjSort(ets);
                            String et = ets.get(0);
                            List<String> temp = getNomalTimes(sort,et,num);
                            for(String arr : temp){
                                if(!timelist.contains(arr)){//没有才加上
                                    timelist.add(arr);
                                }
                            }
                        }

                    }
                    //处理last这种格式的
                }catch (NumberFormatException e){
                    data.setReturncode(300);
                    break;
                }
            }
            else if(checkstart(times[i])){//要是存在横杠
                String begintime = times[i].substring(0,times[i].indexOf("-"));
                String endtime = times[i].substring(times[i].indexOf("-")+1);
                List<String> tmp = getTime1(begintime,endtime,sort);
                if(tmp.size()<=0){//起止时间没有或者格式不对
                    data.setReturncode(300);
                    break;
                }
                else{
                    for(String arr : tmp){
                        if(!timelist.contains(arr)){//没有才加上
                            timelist.add(arr);
                        }
                    }
                }
            }
            else {//是直接的那种格式比如2012
                List<String> templist =new ArrayList<>();
                String bt = getTimeFormat(times[i],sort);
                String et = getEndTimeFormat(times[i],sort);
                int num = 0;
                if(sort.equals("y")) { //如果是年度
                    num = Integer.parseInt(et)-Integer.parseInt(bt)+1;
                }
                if(sort.equals("q")) { //如果是季度
                    num = (Integer.parseInt(et.substring(0,4))-Integer.parseInt(bt.substring(0,4)))*4+getQnum(et.substring(4,5),bt.substring(4,5));
                }
                if(sort.equals("m")) { //如果是月度
                    int mothnum = Integer.parseInt(et.substring(4,6))-Integer.parseInt(bt.substring(4,6))+1;//月份差
                    num = (Integer.parseInt(et.substring(0,4))-Integer.parseInt(bt.substring(0,4)))*12+mothnum;
                }
                if(num<1){
                    data.setReturncode(300);
                    break;
                }
                else {
                    templist = getNomalTimes(sort,et,num);
                }
                if(templist.size()<=0){//起止时间没有或者格式不对
                    data.setReturncode(300);
                    break;
                }
                else{
                for(String arr : templist){
                    if(!timelist.contains(arr)){//没有才加上
                        timelist.add(arr);
                    }
                } }
            }

        }
        if(data.getReturncode()==300){
            this.sendJson(data);
            return;
        }
        //要是能算出来，代表可以排序
        Collections.sort(timelist,Collections.reverseOrder());
        String result = StringUtils.join(timelist.toArray(), ",");
        data.setReturncode(200);
        data.setReturndata(result);
        this.sendJson(data);
    }

    /**
     * 按期数补齐,没有结束时间
     * @param sort
     * @param num
     * @return
     */
    public static List<String> getLastTimes(String sort,int num){
        List<String> list = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        if(sort.equals("y")) { //如果是年度
            for (int i = 0; i <num ; i++) {
                list.add(String.valueOf(now.get(Calendar.YEAR)-i));
            }
        }else  if(sort.equals("q")) { //如果是季度
            List<String> tmp = new ArrayList<>();
            for (int i = 0; i <num/4+2 ; i++) {//除去第一年，先补全再看num来截取个数
                String yd = String.valueOf(now.get(Calendar.YEAR)-i);
                for (int j = 68; j >=65 ; j--) {
                    tmp.add(yd+(char)Integer.parseInt(String.valueOf(j)));
                }
            }
            String nowtime = now.get(Calendar.YEAR)+getQ(now.get(Calendar.MONTH)+1);//获得的月份比当前少一，所以需要+1
            list.addAll(tmp.subList(tmp.indexOf(nowtime),tmp.indexOf(nowtime)+num));//截取集合
        }
        if(sort.equals("m")) { //如果是月度
            List<String> tmp = new ArrayList<>();
            for (int i = 0; i <num/12+2 ; i++) {//除去第一年，先补全再看num来截取个数
                String yd = String.valueOf(now.get(Calendar.YEAR)-i);
                for (int j = 12; j >=1 ; j--) {
                    if(j>=10){
                        tmp.add(yd+j);
                    }
                    else {
                        tmp.add(yd+"0"+j);
                    }
                }
            }
            String nowtime;
            int nowmonth = now.get(Calendar.MONTH)+1;
            if(nowmonth>=10){
                nowtime = now.get(Calendar.YEAR)+String.valueOf(nowmonth);
            }
             else {
                nowtime = now.get(Calendar.YEAR)+"0"+nowmonth;//低于10月份的需要特殊处理，补一个零
            }
            list.addAll(tmp.subList(tmp.indexOf(nowtime),tmp.indexOf(nowtime)+num));//截取集合
        }
            return list;
    }

    /**
     * 有结束时间的补齐
     * @param sort
     * @param
     * @return
     */
    public static List<String> getNomalTimes(String sort,String endtime,int num){
        List<String> list = new ArrayList<>();
        if(sort.equals("y")) { //如果是年度
            for (int i = 0; i <num ; i++) {
                list.add(String.valueOf(Integer.parseInt(endtime)-i));
            }
        }else  if(sort.equals("q")) { //如果是季度
            List<String> tmp = new ArrayList<>();
            for (int i = 0; i <num/4+2 ; i++) {//除去第一年，先补全再看num来截取个数
                String yd = String.valueOf(Integer.parseInt(endtime.substring(0,4))-i);
                for (int j = 68; j >=65 ; j--) {
                    tmp.add(yd+(char)Integer.parseInt(String.valueOf(j)));
                }
            }
            list.addAll(tmp.subList(tmp.indexOf(endtime),tmp.indexOf(endtime)+num));//截取集合
        }
        if(sort.equals("m")) { //如果是月度
            List<String> tmp = new ArrayList<>();
            for (int i = 0; i <num/12+2 ; i++) {//除去第一年，先补全再看num来截取个数
                String yd = String.valueOf(Integer.parseInt(endtime.substring(0,4))-i);
                for (int j = 12; j >=1 ; j--) {
                    if(j>=10){
                        tmp.add(yd+j);
                    }
                    else {
                        tmp.add(yd+"0"+j);
                    }
                }
            }
            list.addAll(tmp.subList(tmp.indexOf(endtime),tmp.indexOf(endtime)+num));//截取集合
        }
        return list;
    }
    /**
     * 处理从某一时间-某一时间的格式
     * @param begintime
     * @param endtime
     * @param sort
     * @param
     * @return
     */
    public List<String> getTime1(String begintime, String endtime,String sort) {
        List<String> list = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        int num = 0;//换算成期数
        if(endtime.equals("")) {//没有结束时间
            String bt = getTimeFormat(begintime,sort);
            if(sort.equals("y")) { //如果是年度
                num = now.get(Calendar.YEAR)-Integer.parseInt(bt)+1;
            }
            if(sort.equals("q")) { //如果是季度
                String month = getQ(now.get(Calendar.MONTH)+1);//当前的月份换算成A,B,C,D
                num = (now.get(Calendar.YEAR)-Integer.parseInt(bt.substring(0,4)))*4+getQnum(month,bt.substring(4,5));
            }
            if(sort.equals("m")) { //如果是月度
                int mothnum = now.get(Calendar.MONTH)+1-Integer.parseInt(bt.substring(4,6))+1;//月份差
                num = (now.get(Calendar.YEAR)-Integer.parseInt(bt.substring(0,4)))*12+mothnum;
            }
            if(num<1){
                return list;//要是期数小于1，返回空
            }
            else {
                list = getLastTimes(sort,num);
            }
        }
        else {//有结束时间
            String bt = getTimeFormat(begintime,sort);
            String et = getEndTimeFormat(endtime,sort);
            String nowtime = getEndTimeFormat(now.get(Calendar.YEAR)+getQ(now.get(Calendar.MONTH)+1),sort);
            if(nowtime.compareTo(et)<0){//要是结束时间大于当前时间，结束时间等于当前时间
                et = nowtime;
            }
            if(et.compareTo(bt)<0){//要是开始时间大于结束时间，返回空
                return list;
            }else {
                if(sort.equals("y")) { //如果是年度
                    num = Integer.parseInt(et)-Integer.parseInt(bt)+1;
                }
                if(sort.equals("q")) { //如果是季度
                    num = (Integer.parseInt(et.substring(0,4))-Integer.parseInt(bt.substring(0,4)))*4+getQnum(et.substring(4,5),bt.substring(4,5));
                }
                if(sort.equals("m")) { //如果是月度
                    int mothnum = Integer.parseInt(et.substring(4,6))-Integer.parseInt(bt.substring(4,6))+1;//月份差
                    num = (Integer.parseInt(et.substring(0,4))-Integer.parseInt(bt.substring(0,4)))*12+mothnum;
                }
                if(num<1){
                    return list;//要是期数小于1，返回空
                }
                else {
                    list = getNomalTimes(sort,et,num);
                }
            }
        }
        return list;
    }

    /**
     * 处理时间格式为对应的icode的时间格式
     * @param timeinput
     * @param sort
     * @return
     */
    public String getTimeFormat(String timeinput,String sort){
        String bt = "";
        if (sort.equals("y")) {//如果是年
            bt = timeinput.substring(0, 4);
        }
        else if (sort.equals("q")) {//如果是季度
            if (timeinput.length() == 4) {//输入的是年度
                bt = timeinput + "A";
            } else if (timeinput.length() == 5) {//输入的是季度
                bt = timeinput.toUpperCase();
            }
            else if (timeinput.length() == 6) {//输入的是月度
                bt = timeinput.substring(0,4)+getQ(Integer.parseInt(timeinput.substring(4,6)));
            }
        }
        else if(sort.equals("m")){//如果是月度
            if (timeinput.length() == 4) {//输入的是年度
                bt = timeinput + "01";
            } else if (timeinput.length() == 5) {//输入的是季度
                bt = timeinput.substring(0,4)+getM(timeinput.toUpperCase().substring(4,5));
            }
            else if (timeinput.length() == 6) {//输入的是月度
                bt = timeinput;
            }
        }
        return bt;
    }
    /**
     * 处理结束时间格式为对应的时间格式
     * @param timeinput
     * @param sort
     * @return
     */
    public String getEndTimeFormat(String timeinput,String sort){
        String bt = "";
        if (sort.equals("y")) {//如果是年
            bt = timeinput.substring(0, 4);
        }
        else if (sort.equals("q")) {//如果是季度
            if (timeinput.length() == 4) {//输入的是年度
                bt = timeinput + "D";
            } else if (timeinput.length() == 5) {//输入的是季度
                bt = timeinput.toUpperCase();
            }
            else if (timeinput.length() == 6) {//输入的是月度
                bt = timeinput.substring(0,4)+getQ(Integer.parseInt(timeinput.substring(4,6)));
            }
        }
        else if(sort.equals("m")){//如果是月度
            if (timeinput.length() == 4) {//输入的是年度
                bt = timeinput + "12";
            } else if (timeinput.length() == 5) {//输入的是季度
                bt = timeinput.substring(0,4)+getEndM(timeinput.toUpperCase().substring(4,5));
            }
            else if (timeinput.length() == 6) {//输入的是月度
                bt = timeinput;
            }
        }
        return bt;
    }
    public static int getQnum(String nowQ,String inputQ){
        int i = 0;
        char a = nowQ.charAt(0);
        char b = inputQ.charAt(0);
        i = (int)a-(int)b+1;
        return i;
    }

    /**
     * @Description: 返回月份对应的ABCD
     * @Param: [mon]
     * @return: String
     */
    public static String getQ(int mon){
        if (mon<4){
            return String.valueOf('A');
        }
        else if (4<=mon&&mon<7){
            return String.valueOf('B');
        }
        else if (7<=mon&&mon<10){
            return String.valueOf('C');
        }
        else {
            return String.valueOf('D');
        }
    }
    /**
     * @Description: 开始时间返回ABCD对应的月份
     * @Param: [mon]
     * @return: String
     */
    public static String getM(String q){
        if (q.equals("A")){
            return String.valueOf("01");
        }
        else if (q.equals("B")){
            return String.valueOf("04");
        }
        else if (q.equals("C")){
            return String.valueOf("07");
        }
        else {
            return String.valueOf("10");
        }
    }

    /**
     * @Description: 结束时间返回ABCD对应的月份
     * @Param: [mon]
     * @return: String
     */
    public static String getEndM(String q){
        if (q.equals("A")){
            return String.valueOf("03");
        }
        else if (q.equals("B")){
            return String.valueOf("06");
        }
        else if (q.equals("C")){
            return String.valueOf("09");
        }
        else {
            return String.valueOf("12");
        }
    }

    public static boolean checkLast(String str) {

        return   str.indexOf("last") >= 0;
    }
    public static boolean checkstart(String str) {

        return   str.indexOf("-") >= 0;
    }
    //===============================end

    /**
     * 新增的地区加搜索框
     */
    public void regFind() throws IOException {
        String query=this.getRequest().getParameter("query");
        String icode = this.getRequest().getParameter("icode");
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        RegdataService re=new RegdataService();
        List<CubeNode> nodes=re.findReg(query,dbcode);
        this.sendJson(nodes);
    }
    /**
     * 返回reg的path
     */
    public void getRegpath() throws IOException {
        String code=this.getRequest().getParameter("code");
        String icode = this.getRequest().getParameter("icode");
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        RegdataService re=new RegdataService();
        List<String> path=re.getRegPath(code,dbcode);
        this.sendJson(path);
    }

    //以下是预览结果
    /**
     * 预览前校验
     */
    public void checkPreview() throws IOException {
        String code = this.getRequest().getParameter("id");
        IndexListService indexListService=new IndexListService();
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
        JSONObject data=new JSONObject();
        data.put("return","");
        if (check){
            data.put("return","200");
        }
        else if (!checkmod){
            data.put("return",data.get("return")+"模型节点设置不符合规定，");
        }
        else if (!checkInfo){
            data.put("return",data.get("return")+"基本信息缺失，");
        }
        else if (!checkZbReg){
            data.put("return",data.get("return")+"指标/地区缺失，");
        }
        else if (!checkhasMod){
            data.put("return",data.get("return")+"不存在模型节点，");
        }
        /*JSONReturnData data = new JSONReturnData("");
        if (check){
            data.setReturncode(200);
        }
        else if (!checkmod){
            data.setReturncode(300);
        }
        else if (!checkInfo){

        }
        else if (!checkZbReg){

        }
        else if (!checkhasMod){
            data.setReturncode();
        }*/
        this.sendJson(data);
    }
    /**
     * 预览结果首页
     * @return
     */
    public ModelAndView previewIndex() throws MathException {
        String code = this.getRequest().getParameter("id");
        DataPreviewService dp = new DataPreviewService();
        //PubInfo.printStr(String.valueOf(check));
        List<String> times = new ArrayList<>();
        List<Map> regions = regshow(code);
        List<List<String>> predata = new ArrayList<>();
        IndexList indexinfo = new IndexListService().getData(code);
        String sort = indexinfo.getSort(); //看是年度的还是月度的还是季度的
        String starttime = indexinfo.getStartperiod();//起始时间
        Calendar now = Calendar.getInstance();
            int nums = 3;
            String bt = starttime;
            String et = getEndTimeFormat(now.get(Calendar.YEAR)+getQ(now.get(Calendar.MONTH)+1),sort);
            List<String> tmp = getTime1(bt,et,sort);
            int qishu = tmp.size();
            if(qishu>0){
                List<String> timelist = new ArrayList<>();
                if(qishu<=nums){//实际没有这么多期
                        timelist.addAll(tmp);
                }
                else{
                    for (int j = qishu-1; j >qishu-nums-1; j--) {
                            timelist.add(tmp.get(j));
                    }
                }
                //计算
                //要是能算出来，代表可以排序
                Collections.sort(timelist,Collections.reverseOrder());
                String result = StringUtils.join(timelist.toArray(), ",");
                for(String i :timelist){
                    dp.todocalculate(code,i);
                }
                //画表格
                predata = drawTable(code,result);
                times.addAll(timelist);
            }
            //处理last这种格式的

        return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/previewIndex").addObject("icode",code).addObject("times",times).addObject("regions",regions).addObject("predata",predata);
    }


    //画预览结果的表格
    public List<List<String>> drawTable(String icode,String times){
        List<List<String>> data = new ArrayList<>();
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        DataPreviewService dp =  new DataPreviewService();
        OriginService originService = new OriginService();
        String region = dp.findRegions(icode);
        String[] regions = region.split(",");
        String[] time = times.split(",");
       List<IndexMoudle> mods = new IndexEditService().getAllMods("",icode);
        List<Map> zbs = new IndexEditService().getZBS(icode);
        int zbsnum = zbs.size();//指标名的长度
        int modnum = mods.size();//模型节点的长度
        int num = modnum;
        if(zbsnum>modnum){
            num = zbsnum;
        }
            for (String i : regions) {
                for (int j = 0; j <num ; j++) {
                    List<String> rows = new ArrayList<>();
                    rows.add(new OriginService().getwdnode("reg",i,dbcode).getName());//地区名
                    if(j<zbsnum){//如果还在范围内
                        rows.add(zbs.get(j).get("zbname").toString());
                    }else {//范围外填充空
                        rows.add("");
                    }
                    if(j<modnum){//如果还在范围内
                        rows.add(mods.get(j).getCname());
                    }else {//范围外填充空
                        rows.add("");
                    }
                    for (String k : time) {
                        if(j<zbsnum){//如果还在范围内,去找它的值
                            CubeWdCodes where = new CubeWdCodes();
                            where.Add("zb", zbs.get(j).get("zbcode").toString());
                            where.Add("ds", zbs.get(j).get("dscode").toString());
                            where.Add("co", zbs.get(j).get("cocode").toString());
                            where.Add("reg", i);
                            where.Add("sj", k);
                            ArrayList<CubeQueryData> result = RegdataService.queryData(dbcode, where);
                            if(result.size()>0){
                                if(!result.get(0).getData().getStrdata().equals("")){
                                    String funit=originService.getwdnode("zb",zbs.get(j).get("zbcode").toString(),dbcode).getUnitcode();
                                    BigDecimal rate = new BigDecimal(originService.getRate(funit,zbs.get(j).get("unitcode").toString(),k));
                                    BigDecimal orval = (new BigDecimal(result.get(0).getData().getStrdata())).multiply(rate);
                                    rows.add(orval.toPlainString());
                                }
                                else {
                                    rows.add("");
                                }
                            }
                           else {
                                rows.add("");
                            }
                        }else {//范围外填充空
                            rows.add("");
                        }
                        if(j<modnum){//如果还在范围内
                            String val = dp.getData(mods.get(j).getCode(),i,k).getData();
                            if(val!=null&&!val.equals("")){
                              val=  String.format("%."+mods.get(j).getDacimal()+"f",Double.valueOf(val));//保留几位小数
                                rows.add(val);
                            }
                           else {
                                rows.add("");
                            }
                        }else {//范围外填充空
                            rows.add("");
                        }
                    }
                    data.add(rows);
                }
        }
        return  data;
    }
    //preview的时间检查
    public void previewTimeCheck() throws IOException {
        HttpServletRequest req = this.getRequest();
        String time = req.getParameter("timeinput");//得到时间搜索框中的时间
        String icode = req.getParameter("icode"); //看是年度的还是月度的还是季度的
        IndexList indexinfo = new IndexListService().getData(icode);
        String sort = indexinfo.getSort(); //看是年度的还是月度的还是季度的
        String starttime = indexinfo.getStartperiod();//起始时间
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        JSONReturnData data = new JSONReturnData("");
        List<String> timelist = new ArrayList<>();
        IndexListService ls = new IndexListService();
        Calendar now = Calendar.getInstance();
        String[] times = time.split(",");
        for (int i = 0; i <times.length ; i++) {
            if(times[i].equals("")){continue;}
            else  if(checkLast(times[i])){//如果存在last这个字母
                try{
                    int nums = Integer.parseInt(times[i].substring(times[i].indexOf("t")+1));
                    String bt = starttime;
                    String et = getEndTimeFormat(now.get(Calendar.YEAR)+getQ(now.get(Calendar.MONTH)+1),sort);
                    List<String> tmp = getTime1(bt,et,sort);
                    int qishu = tmp.size();
                    if(qishu<=0){//起止时间没有或者格式不对
                        data.setReturncode(300);
                        break;
                    }
                    else{
                        if(qishu<=nums){//实际没有这么多期
                            for(String arr : tmp){
                                if(!timelist.contains(arr)){//没有才加上
                                    timelist.add(arr);
                                }
                            }
                        }
                        else{
                            for (int j = qishu-1; j >qishu-nums-1; j--) {
                                if(!timelist.contains(tmp.get(j))){//没有才加上
                                    timelist.add(tmp.get(j));
                                }
                            }
                        }
                    }
                    //处理last这种格式的
                }catch (NumberFormatException e){
                    data.setReturncode(300);
                    break;
                }
            }
            else if(checkstart(times[i])){//要是存在横杠
                String begintime = times[i].substring(0,times[i].indexOf("-"));
                String endtime = times[i].substring(times[i].indexOf("-")+1);
                List<String> tmp = getTime1(begintime,endtime,sort);
                if(tmp.size()<=0){//起止时间没有或者格式不对
                    data.setReturncode(300);
                    break;
                }
                else{
                    for(String arr : tmp){
                        if(!timelist.contains(arr)){//没有才加上
                            timelist.add(arr);
                        }
                    }
                }
            }
            else {//是直接的那种格式比如2012
                List<String> templist =new ArrayList<>();
                String bt = getTimeFormat(times[i],sort);
                String et = getEndTimeFormat(times[i],sort);
                int num = 0;
                if(sort.equals("y")) { //如果是年度
                    num = Integer.parseInt(et)-Integer.parseInt(bt)+1;
                }
                if(sort.equals("q")) { //如果是季度
                    num = (Integer.parseInt(et.substring(0,4))-Integer.parseInt(bt.substring(0,4)))*4+getQnum(et.substring(4,5),bt.substring(4,5));
                }
                if(sort.equals("m")) { //如果是月度
                    int mothnum = Integer.parseInt(et.substring(4,6))-Integer.parseInt(bt.substring(4,6))+1;//月份差
                    num = (Integer.parseInt(et.substring(0,4))-Integer.parseInt(bt.substring(0,4)))*12+mothnum;
                }
                if(num<1){
                    data.setReturncode(300);
                    break;
                }
                else {
                    templist = getNomalTimes(sort,et,num);
                }
                if(templist.size()<=0){//起止时间没有或者格式不对
                    data.setReturncode(300);
                    break;
                }
                else{
                    for(String arr : templist){
                        if(!timelist.contains(arr)){//没有才加上
                            timelist.add(arr);
                        }
                    } }
            }

        }
        if(data.getReturncode()==300){
            this.sendJson(data);
            return;
        }
        //要是能算出来，代表可以排序
        Collections.sort(timelist,Collections.reverseOrder());
        String result = StringUtils.join(timelist.toArray(), ",");
        data.setReturncode(200);
        data.setReturndata(result);
        this.sendJson(data);
    }

    /**
     * 时间变后重新算
     * @return
     * @throws IOException
     */
    public ModelAndView preCaculate() throws IOException {
        HttpServletRequest req = this.getRequest();
        String pjax = req.getHeader("X-PJAX");
        String icode = req.getParameter("icode");
        String time = req.getParameter("time");
        String[] times = time.split(",");
        List<List<String>> predata = new ArrayList<>();
        if(!StringUtil.isEmpty(pjax)){
            DataPreviewService dp = new DataPreviewService();
                for(String i :times){
                    dp.todocalculate(icode,i);
                }
                //画表格
                predata = drawTable(icode,time);

        }
        if (StringUtil.isEmpty(pjax)) {
            this.getResponse().sendRedirect(this.getContextPath() + "/zbdata/zsjhedit.htm?m=previewIndex&id="+icode);
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/previewTable").addObject("predata",predata).addObject("times",times);
        }
        return null;
    }
}
