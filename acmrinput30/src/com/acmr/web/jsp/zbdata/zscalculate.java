package com.acmr.web.jsp.zbdata;

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
import com.acmr.dao.zhzs.DataDao;
import com.acmr.dao.zhzs.IndexTaskDao;
import com.acmr.dao.zhzs.WeightEditDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.zhzs.*;
import com.acmr.service.zbdata.OriginService;
import com.acmr.service.zhzs.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class zscalculate extends BaseAction {

    private List<List<String>> data1;
    private List<List<String>> datatmp;
    private List<String> regs;
    private List<String> regstmp;

    /**
     * @Description: 从data表中读数时返回页面
     * @Param: []
     * @return: acmr.web.entity.ModelAndView
     * @Author: lyh
     * @Date: 2018/9/13
     */
    public ModelAndView ZsCalculate() {
        //String test="重新计算";
        HttpServletRequest req = this.getRequest();
        IndexTaskService indexTaskService = new IndexTaskService();
       // boolean istmp = false;
        OriginService originService = new OriginService();
        String taskcode = req.getParameter("taskcode");
        //把data_tmp表中的数据覆盖了
        String sessionid=req.getSession().getId();
        IndexTaskDao.Fator.getInstance().getIndexdatadao().copyData(taskcode,sessionid);
        DataDao.Fator.getInstance().getIndexdatadao().copyDataResult(taskcode,sessionid);
        String ayearmon = indexTaskService.getTime(taskcode);
        data1 = getOriginData(false, taskcode, ayearmon,null);
        List<String> regscode = indexTaskService.getTaskRegs(taskcode);
        regs = new ArrayList<>();
        for (int i = 0; i < regscode.size(); i++) {
            regs.add(originService.getwdnode("reg", regscode.get(i)).getName());
        }

        WeightEditService weightEditService=new WeightEditService();
        List<TaskModule> mods=weightEditService.getTMods(taskcode);
        for (int i=0;i<mods.size();i++){
            PubInfo.printStr(mods.get(i).getCname());
        }
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/zscalculate").addObject("data", data1).addObject("regs", regs).addObject("taskcode", taskcode).addObject("istmp", false).addObject("mods",mods);
    }

    /**
     * @Description: 从tmp表中读数时返回页面
     * @Param: []
     * @return: acmr.web.entity.ModelAndView
     * @Author: lyh
     * @Date: 2018/9/13
     */
    public ModelAndView ReCalculate() {
        HttpServletRequest req = this.getRequest();
        String sessionid = req.getSession().getId();
        IndexTaskService indexTaskService = new IndexTaskService();
        //boolean istmp = true;
        OriginService originService = new OriginService();
        String taskcode = req.getParameter("taskcode");
        //取对应id的数据
        String ayearmon = indexTaskService.getTime(taskcode);
        datatmp = getOriginData(true,taskcode,ayearmon,sessionid);
        List<String> regscode = indexTaskService.getTaskRegs(taskcode);
        regstmp = new ArrayList<>();
        for (int i = 0; i < regscode.size(); i++) {
            regs.add(originService.getwdnode("reg", regscode.get(i)).getName());
        }

        WeightEditService weightEditService=new WeightEditService();
        List<TaskModule> mods=weightEditService.getTMods(taskcode);
        for (int i=0;i<mods.size();i++){
            PubInfo.printStr(mods.get(i).getCname());
        }
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/zscalculate").addObject("data", datatmp).addObject("regs", regstmp).addObject("taskcode", taskcode).addObject("istmp", true).addObject("mods",mods);

    }

    /**
    * @Description: 重新读取数据，插入数据库，并局部刷新原始数据
    * @Param: []
    * @return: acmr.web.entity.ModelAndView
    * @Author: lyh
    * @Date: 2018/9/17
    */
    public ModelAndView ReData() {
        HttpServletRequest req = this.getRequest();
        IndexTaskService indexTaskService = new IndexTaskService();
        OriginService originService = new OriginService();
        String taskcode = req.getParameter("taskcode");
        String sessionid = req.getSession().getId();
        String ayearmon = indexTaskService.getTime(taskcode);
        //插入数据库data_tmp表
        IndexTaskDao.Fator.getInstance().getIndexdatadao().ReData(taskcode, sessionid);
        List<List<String>> data=getOriginData(true,taskcode,ayearmon,sessionid);
        String pjax = req.getHeader("X-PJAX");
        List<String> regscode = indexTaskService.getTaskRegs(taskcode);
        List<String> regs = new ArrayList<>();
        for (int i = 0; i < regscode.size(); i++) {
            regs.add(originService.getwdnode("reg", regscode.get(i)).getName());
        }
        if (StringUtil.isEmpty(pjax)) {
            PubInfo.printStr("===================================emptyredata");
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/zscalculate").addObject("regs", regs).addObject("data",data).addObject("taskcode",taskcode);
        } else {
            PubInfo.printStr("=====================================pjaxredata");
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/dataTable").addObject("regs", regs).addObject("data",data).addObject("taskcode",taskcode);

        }

    }
    /**
     * @Description: istmp为true，表示从临时表中读数，false从data表中读数，返回rows，用于绘制原始数据表
     * @Param: [istmp, taskcode]
     * @return: java.util.List<java.util.List < java.lang.String>>
     * @Author: lyh
     * @Date: 2018/9/13
     */
    public List<List<String>> getOriginData (Boolean istmp, String taskcode, String ayearmon,String sessionid) {
        List<List<String>> rows = new ArrayList<>();
        IndexTaskService indexTaskService = new IndexTaskService();
        List<String> ZBcodes = indexTaskService.getZBcodes(taskcode);
        List<String> regs = indexTaskService.getTaskRegs(taskcode);
        for (int i = 0; i < ZBcodes.size(); i++) {
            List<String> row = new ArrayList<>();
            String ZBcode = ZBcodes.get(i);
            row.add(indexTaskService.getzbname(ZBcode));
            for (int j = 0; j < regs.size(); j++) {
                String data = indexTaskService.getData(istmp,taskcode, regs.get(j), ZBcodes.get(i), ayearmon,sessionid);
                row.add(data);
            }
            //PubInfo.printStr("row:"+row.toString());
            rows.add(row);
        }
        for (int m = 0; m < rows.size(); m++) {
            PubInfo.printStr(rows.get(m).toString());
        }
        return rows;
    }
    


    /**
     * 数据下载
     *
     * @param
     * @return
     * @author wf
     * @date
     */
    public void toExcel() throws IOException {
        //接参
        //HttpServletRequest req = this.getRequest();
        /*String regname = PubInfo.getString(req.getParameter("excelregs"));//地区名称
        String [] regnames = regname.split(",");
        String data1=PubInfo.getString(req.getParameter("exceldata"));
        String [] datas = regname.split(",");*/
        String istmp = PubInfo.getString(this.getRequest().getParameter("istmp"));//判断读取数据的data表
        ExcelBook book = new ExcelBook();
        if (istmp.equals("true")) {
            JSONReturnData data = new JSONReturnData("");
            if (regstmp == null || datatmp == null) {
                data.setReturncode(300);
                this.sendJson(data);
                return;
            } else {
                data.setReturncode(200);
            }
            ExcelSheet sheet1 = new ExcelSheet();
            sheet1.setName("sheet1");
            sheet1.addColumn();
            ExcelCell cell1 = new ExcelCell();
            ExcelRow dr1 = sheet1.addRow();
            ExcelCell cell2 = cell1.clone();
            cell2.setCellValue("指标");
            dr1.set(0, cell2);
            // for (int a=2;a<)
            for (int k = 0; k < regstmp.size(); k++) {
                int m = k + 1;
                sheet1.addColumn();
                cell2 = cell1.clone();
                cell2.setCellValue(regstmp.get(k));
                dr1.set(m, cell2);
            }
            cell1.getCellstyle().getFont().setBoldweight((short) 10);
            //System.out.println(data1);
            for (int i = 0; i < datatmp.size(); i++) {
                String arr = data1.get(i).toString().substring(1, data1.get(i).toString().length() - 1);
                //String arr =datatmp.get(i).toString();
                dr1 = sheet1.addRow();
                String a2 = arr.replaceAll("null"," ").replaceAll("0"," ");
                String[] a3 = a2.split(",");
                for (int j = 0; j < a3.length; j++) {
                    cell2 = cell1.clone();
                    cell2.setCellValue(a3[j]);
                    dr1.set(j, cell2);
                }
            }
            book.getSheets().add(sheet1);
        } else {
            JSONReturnData data = new JSONReturnData("");
            if (regs == null || data1 == null) {
                data.setReturncode(300);
                this.sendJson(data);
                return;
            } else {
                data.setReturncode(200);
            }
            ExcelSheet sheet1 = new ExcelSheet();
            sheet1.setName("sheet1");
            sheet1.addColumn();
            ExcelCell cell1 = new ExcelCell();
            ExcelRow dr1 = sheet1.addRow();
            ExcelCell cell2 = cell1.clone();
            cell2.setCellValue("指标");
            dr1.set(0, cell2);
            // for (int a=2;a<)
            for (int k = 0; k < regs.size(); k++) {
                int m = k + 1;
                sheet1.addColumn();
                cell2 = cell1.clone();
                cell2.setCellValue(regs.get(k));
                dr1.set(m, cell2);
            }
            cell1.getCellstyle().getFont().setBoldweight((short) 10);
            for (int i = 0; i < data1.size(); i++) {
                String arr = data1.get(i).toString().substring(1, data1.get(i).toString().length() - 1);
                //String arr =data1.get(i).toString();
                dr1 = sheet1.addRow();
                String a2 = arr.replaceAll("null"," ").replaceAll("0"," ");
                String[] a3 = a2.split(",");
                for (int j = 0; j < a3.length; j++) {
                    cell2 = cell1.clone();
                    cell2.setCellValue(a3[j]);
                    dr1.set(j, cell2);
                }
            }
            book.getSheets().add(sheet1);
        }
        HttpServletResponse resp = this.getResponse();
        resp.reset();
        resp.setContentType("application/vnd.ms-excel; charset=UTF-8");
        resp.setHeader("Pragma", "public");
        resp.setHeader("Cache-Control", "max-age=30");
        String fileName = "任务数据.xlsx";
        fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        resp.addHeader("Content-Disposition", "attachment; filename=" + fileName);
        //Export("application/ms-excel", "订单报表.xls");
        try {
            book.saveExcel(resp.getOutputStream(), XLSTYPE.XLSX);
        } catch (ExcelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 重新计算页面
     */
    public ModelAndView docalculate(){
        HttpServletRequest req = this.getRequest();
        // 获取查询数据
        String sessionid = req.getSession().getId();
        IndexTaskService indexTaskService =new IndexTaskService();
        OriginDataService originDataService = new OriginDataService();
        String taskcode = PubInfo.getString(req.getParameter("taskcode"));
        String cws=req.getParameter("cws");//获取当前的权重
        PubInfo.printStr(cws);
        List<String> cw= Arrays.asList(cws.split(","));
        for (int i=0;i<cw.size();i++){
            String code=cw.get(i).split(":")[0];
            String weight=cw.get(i).split(":")[1];
            WeightEditService weightEditService=new WeightEditService();
            weightEditService.tWeightUpadte(code,weight);
        }
        // 判断是否pjax 请求
        String pjax = req.getHeader("X-PJAX");
        //通过taskcode去查值
        String ayearmon = indexTaskService.findAyearmon(taskcode);
        //从临时数据表读取数据做计算
        String regs = indexTaskService.findRegions(taskcode);
        String [] reg = regs.split(",");
            //开始计算指数的值，包括乘上weight
        try {
            if(originDataService.calculateZB(true,taskcode,ayearmon,regs,sessionid)){
                //指标已经算完
                List<TaskModule> zong = indexTaskService.findRoot(taskcode);
                for (int i = 0; i <zong.size() ; i++) {
                    for (int j = 0; j <reg.length ; j++) {//一个地区一个地区地算
                        originDataService.calculateZS(true,zong.get(i).getCode(),taskcode,ayearmon,reg[j],sessionid);
                    }
                }
            }
        } catch (MathException e) {
            e.printStackTrace();
        }

        if (StringUtil.isEmpty(pjax)) {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/zscalculate");
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/zscalculate");
        }
    }
}
