package com.acmr.web.jsp.zbdata;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import acmr.cubequery.service.cubequery.entity.CubeQueryData;
import acmr.cubequery.service.cubequery.entity.CubeWdCodes;
import acmr.util.PubInfo;
import com.acmr.service.zbdata.OriginService;
import com.acmr.service.zbdata.RegdataService;
import com.acmr.service.zhzs.CreateTaskService;
import com.acmr.service.zhzs.IndexTaskService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import acmr.cubeinput.MetaTableException;
import acmr.cubeinput.service.CubeUnitManager;
import acmr.cubeinput.service.cubeinput.entity.CubeUnit;
import acmr.cubeinput.service.metatable.entity.SqlWhere;
import acmr.excel.pojo.Constants.XLSTYPE;
import acmr.excel.pojo.ExcelBook;
import acmr.excel.pojo.ExcelCell;
import acmr.excel.pojo.ExcelRow;
import acmr.excel.pojo.ExcelSheet;
import acmr.util.DataTable;
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
import com.acmr.dao.zhzs.IndexTaskDao;
import com.acmr.dao.zhzs.WeightEditDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.zhzs.Data;
import com.acmr.model.zhzs.DataResult;
import com.acmr.model.zhzs.IndexZb;
import com.acmr.model.zhzs.TaskModule;
import com.acmr.service.zbdata.OriginService;
import com.acmr.service.zhzs.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class datahandle extends BaseAction {

    /**
     *  重新读取数据
     *
     * @author wf
     * @date
     * @param
     * @return
     */
    /*public ModelAndView reGetData(){
        HttpServletRequest req=this.getRequest();
        String sessionid=req.getSession().getId();
        IndexTaskService indexTaskService=new IndexTaskService();
        OriginService originService=new OriginService();
        String taskcode=req.getParameter("taskcode");
        List<String> origionZBcodes = indexTaskService.getZBcodes(taskcode);//取得指标code
        //String[] ZBcodes = origionZBcodes.toArray(new String[origionZBcodes.size()]);
        List<String> reg=indexTaskService.getTaskRegs(taskcode);//地区
        String[] regscode = reg.toArray(new String[reg.size()]);
        String sj=indexTaskService.getTime(taskcode);//时间ayearmon
        *//*String[] reg = regscode.split(",");
        String[] regnames = regname.split(",");
        String[] zbcodes = zbcode.split(",");
        String[] zbnames = zbname.split(",");
        *//*
        List<String> regs=new ArrayList<>();
        for (int i=0;i<reg.size();i++){
            regs.add(originService.getwdnode("reg",reg.get(i)).getName());
        }
        List<List<String>> data = new ArrayList<>();
        for(int j=0;j<regscode.length;j++){
            List<String> rows = new ArrayList<>();
            for(int m=0;m<origionZBcodes.size();m++){
                CubeWdCodes where = new CubeWdCodes();
                //String funit=originService.getwdnode("zb",zbcode).getUnitcode();
                String zbcode = origionZBcodes.get(m);
                rows.add(indexTaskService.getzbname(zbcode));
                String zbunit =indexTaskService.getTaskzb(taskcode).get(m).getUnitcode();
                //String[] units = zbunit.split(",");
                String co = indexTaskService.getTaskzb(taskcode).get(m).getCompany();//主体
                //String[] cos = co.split(",");
                String ds = indexTaskService.getTaskzb(taskcode).get(m).getDatasource();//数据来源
                //String[] dss = ds.split(",");
                //String funit=originService.getwdnode("zb",origionZBcodes.get(j)).getUnitcode();
                //double rate=originService.getRate(funit,indexTaskService.getTaskzb(taskcode).get(m).getUnitcode(),sj);
                where.Add("zb", zbcode);
                where.Add("ds", ds);
                where.Add("co", co);
                where.Add("reg",reg.get(j));
                where.Add("sj", sj);
                ArrayList<CubeQueryData> result = RegdataService.queryData("cuscxnd",where);
                *//*rows.add(sjs[i]);//获取时间
                datas.add(zbnames[j]);//获取指标*//*
                for (int k = 0; k <result.size() ; k++) {
                    if(result.get(k).getData().toString() != ""){
                        //double resulttemp = result.get(k).getData().getData()*rate;
                        double resulttemp = result.get(k).getData().getData();
                        rows.add(resulttemp+"");
                    }
                    else{
                        rows.add("");
                    }
                }
                data.add(rows);
            }
        }
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/zscalculate").addObject("data",data).addObject("regs",regs);

    }*/
    /**
     * 文件上传
     *
     * @author wf
     * @date
     * @param
     * @return
     */
    public void updateTaskData() {
        //CreateTaskService createTaskService = new CreateTaskService();
        IndexTaskService indexTaskService = new IndexTaskService();
        OriginService originService = new OriginService();
        HttpServletRequest req = this.getRequest();
        JSONReturnData data = new JSONReturnData("");
        String sessionid = req.getSession().getId();
        String taskcode = req.getParameter("taskcode");
        List<String> regscode = indexTaskService.getTaskRegs(taskcode);

        List<String> regs = new ArrayList<>();
        for (int i = 0; i < regscode.size(); i++) {
            regs.add(originService.getwdnode("reg", regscode.get(i)).getName());
        }
        List<String> ZBcodes = indexTaskService.getZBcodes(taskcode);
        String ayearmon = indexTaskService.getTime(taskcode);

        ServletFileUpload uploader = new ServletFileUpload(new DiskFileItemFactory());
        uploader.setHeaderEncoding("utf-8");
        try {
            ArrayList<FileItem> files = (ArrayList<FileItem>) uploader.parseRequest(this.getRequest());
            if (files.size() > 0) {
                FileItem file = files.get(0);
                String name = file.getName();
                if (file != null) {
                    try {
                        XLSTYPE xlstype = XLSTYPE.XLS;
                        if (name.endsWith("xlsx")) {
                            xlstype = XLSTYPE.XLSX;
                        }
                        ExcelBook book1 = new ExcelBook();
                        book1.LoadExcel(file.getInputStream(), xlstype);
                        ExcelSheet sheet = book1.getSheets().get(0);
                        if (sheet == null) {
                            data.setReturncode(500);
                            data.setReturndata("没有发现上传的文件");
                            this.sendJson(data);
                            return;
                        }
                        int rows = sheet.getRows().size();

                        // 数据量
                        int count = 0;
                        //对地区进行比对
                        for(int r=0;r<regs.size();r++){
                            //String reg = sheet.getRows().get(0).toString().substring(4,sheet.getRows().get(0).toString().length()-1);
                            int s= r+1;
                            String getreg =sheet.getRows().get(0).getCells().get(s).getText() + "";
                            String [] getregname = getreg.split(",");
                            String reg = regs.get(r);
                            String b = regs.get(0);
                            if(!getreg.equals(reg)){
                                data.setReturncode(300);
                                data.setReturndata("指标或地区有误，请比对下载进行数据修改");
                                this.sendJson(data);
                                return;
                            }
                        }
                        List<String> ZBname = new ArrayList<>();

                        //对指标进行比对
                        for(int z=0;z<ZBcodes.size();z++){
                            //String reg = sheet.getRows().get(0).toString().substring(4,sheet.getRows().get(0).toString().length()-1);
                            int n= z+1;
                            String getzb =sheet.getRows().get(n).getCells().get(0).getText() + "";
                            //String ceshi = sheet.getRows().get(2).getCells().get(0).getText() + "";
                            //String [] getregname = getreg.split(",");
                            String ZBcode = ZBcodes.get(z);
                            ZBname.add(indexTaskService.getzbname(ZBcode));
                            String zbname = ZBname.get(z);
                            if(!getzb.equals(zbname)){
                                data.setReturncode(300);
                                data.setReturndata("指标或地区有误，请比对下载进行数据修改");
                                this.sendJson(data);
                                return;
                            }
                        }
                        // 遍历数据并进行封装
                        List zbandreg = new ArrayList<>();
                        for (int j=0;j<ZBcodes.size();j++){
                            int k = j+1;
                            /*
                            ExcelCell zb = sheet.getRows().get(k).getCells().get(0);
                            String zbname = zb.getText() + "";
                            String [] getzbname = zbname.split(",");
                            if(!getzbname[k].equals(ZBname.get(j))){
                                data.setReturncode(300);
                                data.setReturndata("指标或地区有误，请比对下载进行数据修改");
                                this.sendJson(data);
                                return;
                            }*/
                            if (rows >= 1 && sheet.getRows().get(k) != null) {
                                ExcelRow Rows = sheet.getRows().get(k);
                                if (Rows != null) {
                                    //int cells = Rows.getCells().size();
                                    //Map<String, String> mkey = new HashMap<String, String>();
                                    List<String> reganddata = new ArrayList<>();
                                    reganddata.add(ZBcodes.get(j));
                                    for (int i = 0; i < regscode.size(); i++) {
                                        int m = i+1;
                                        ExcelCell cell = Rows.getCells().get(m);
                                        if (cell != null) {
                                            String value = cell.getText() + "";
                                            System.out.println(value);
                                            /*if (StringUtil.isEmpty(value)) {
                                                continue;
                                            }*/
                                            /*if (!mkey.containsValue(value)) {
                                                mkey.put(regscode.get(i), value);
                                            }*/
                                            if(value.equals("  ")){
                                                data.setReturncode(300);
                                                data.setReturndata("数据不能为空");
                                                this.sendJson(data);
                                                return;
                                            }
                                            reganddata.add(value);

                                        }
                                    }
                                    zbandreg.add(reganddata);
                                }

                            }
                        }
                        for (int m = 0; m < zbandreg.size(); m++) {
                            PubInfo.printStr(zbandreg.get(m).toString());
                        }

                        if (count >= 10000) {
                            data.setReturncode(400);
                            data.setReturndata("导入的数据不能超过10000行");
                            return;
                        }
                        // 入库
                        int uploaddata = indexTaskService.updateData(taskcode,ayearmon,sessionid,regscode,zbandreg);
                        if(uploaddata == 1){
                            data.setReturncode(500);
                            data.setReturndata("数据存入数据库失败");
                            return;
                        }
                        data.setParam1(count);
                        data.setReturncode(200);
                        data.setReturndata("数据文件上传成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                        data.setReturncode(500);
                        data.setReturndata("数据上传失败");
                    }
                }
            }
            this.sendJson(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 判断字段为必填项
     * @author wf
     * @date
     * @param
     * @return
     */
    public static boolean isMust(String code) {
        Map<String, String> map = getMust();
        String value = map.get(code);
        if (value != null) {
            return true;
        }
        return false;
    }
    /**
     * 必须填写项
     * @author wf
     * @date
     * @param
     * @return
     */
    static Map<String, String> getMust() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("code", "zbcode");// 指标名称
        hashMap.put("cname", "region");// 地区名称
        return hashMap;
    }

}