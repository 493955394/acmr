package com.acmr.web.jsp.zbdata;

import acmr.excel.ExcelException;
import acmr.excel.pojo.Constants.XLSTYPE;
import acmr.excel.pojo.ExcelBook;
import acmr.excel.pojo.ExcelCell;
import acmr.excel.pojo.ExcelRow;
import acmr.excel.pojo.ExcelSheet;
import acmr.util.PubInfo;

import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.service.zhzs.PastViewService;
import javax.servlet.http.HttpServletRequest;
//import java.util.List;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class pastviews extends BaseAction {

    PastViewService pv = new PastViewService();
    String code = this.getRequest().getParameter("id");
    /**
     * 地区选择最近五年默认值
     * @author wf
     * @date
     * @param
     * @return
     */
    public ModelAndView main(){
        //获取用户权限
        String right=this.getRequest().getParameter("right");

        List<String> fivetaskcode = pv.getAllTask(code).subList(0,5);
        //List<String> alltaskcode = pv.getAllTask(code);

        String taskcode = fivetaskcode.get(0);
        List<String> last5 = pv.getAllTime(code).subList(0,5);
        String reg = pv.getRegions(taskcode).get(0);
/*
        List<Map<String,String>> allfivemod = pv.getFiveMods(fivetaskcode);
        List<List<String>> moddatas = pv.getModData(reg,fivetaskcode,allfivemod,last5);*/
        List<List<String>> showdatas = pv.getModData(reg,fivetaskcode);

        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata",showdatas).addObject("last5",last5);
   }

    /**
     * 模型节点选择最近五年默认值
     * @author wf
     * @date
     * @param
     * @return
     */
    public ModelAndView RegDatas(){

        //获取用户权限
        String right=this.getRequest().getParameter("right");

        List<String> alltaskcode = pv.getAllTask(code);
        if(alltaskcode != null){
            String taskcode = alltaskcode.get(0);
            List<String> regs = pv.getRegions(taskcode);
            List<String> last5 = pv.getAllTime(code).subList(0,5);
            String mod = pv.getAllMods(alltaskcode).get(0).get("code");
            List<List<String>> regdatas = pv.getRegData(regs,alltaskcode,mod,last5);
        }
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews");
    }
    public void toModExcel() throws IOException {
        //接参
        List<String> fivetaskcode = pv.getAllTask(code).subList(0,5);
        String regcode = PubInfo.getString(this.getRequest().getParameter("regcode"));
        JSONReturnData data = new JSONReturnData("");
        if(regcode ==null){
            data.setReturncode(300);
            this.sendJson(data);
            return;
        } else {
            data.setReturncode(200);
        }
        List<String> last5 = pv.getAllTime(code).subList(0,5);
        List<List<String>> datas = pv.getModData(regcode,fivetaskcode);
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
        for (int k = 0; k < last5.size(); k++){
            int m =k+1;
            sheet1.addColumn();
            cell2 = cell1.clone();
            cell2.setCellValue(last5.get(k));
            dr1.set(m, cell2);
        }
        cell1.getCellstyle().getFont().setBoldweight((short) 10);
        //System.out.println(data1);
        for(int i=0;i<datas.size();i++){

            List<String> arr =datas.get(i);
            dr1 = sheet1.addRow();
            for(int j=0;j<arr.size();j++){
                cell2 = cell1.clone();
                cell2.setCellValue(arr.get(j));
                dr1.set(j, cell2);
            }
        }
        book.getSheets().add(sheet1);
        HttpServletResponse resp = this.getResponse();
        resp.reset();
        resp.setContentType("application/vnd.ms-excel; charset=UTF-8");
        resp.setHeader("Pragma", "public");
        resp.setHeader("Cache-Control", "max-age=30");
        String fileName="查看往期.xlsx";
        fileName=java.net.URLEncoder.encode(fileName, "UTF-8");
        resp.addHeader("Content-Disposition", "attachment; filename="+fileName);
        try {
            book.saveExcel(resp.getOutputStream(), XLSTYPE.XLSX);
        } catch (ExcelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    }