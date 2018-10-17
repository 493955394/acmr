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
import com.acmr.dao.zhzs.DataDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.service.zbdata.OriginService;
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
     * 最近五年默认值，单地区默认展示
     * @author wf
     * @date
     * @param
     * @return
     */
    public ModelAndView main(){
        //获取用户权限
        String icode = this.getRequest().getParameter("id");
        List<String> fivetaskcode = pv.getAllTask(icode).subList(0,5);
        List<String> last5 = pv.getAllTime(icode).subList(0,5);
        List<Map<String,String>> regs=pv.getRegList(icode);
        String reg=regs.get(0).get("code");
        List<List<String>> showdatas = pv.getModTime(reg,fivetaskcode,icode);//得到单地区data
        Map<String,Object> info=new HashMap<>();
        //展示的时间
        info.put("time",last5);
        //存在的地区并集,用于select
        info.put("options",regs);
        info.put("indexcode",icode);
        info.put("span","地区选择");
        info.put("spancode",null);
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata",showdatas).addObject("info",info);
   }




   /**
   * @Description: 根据传来的参数重新刷新数据表，以及下拉框
   * @Param: []
   * @return: acmr.web.entity.ModelAndView
   * @Author: lyh
   * @Date: 2018/10/17
   */
   public ModelAndView reTable() throws IOException {

       HttpServletRequest req=this.getRequest();
       //计划code
       String icode=req.getParameter("icode");
       //行和列
       String tablerow=req.getParameter("tableRow");
       String tablecol=req.getParameter("tableCol");
       //单个维度的code
       String spancode=req.getParameter("spancode");
       String time=req.getParameter("time");
       PastViewService pastViewService=new PastViewService();
       String pjax = req.getHeader("X-PJAX");
       Map<String,Object> info=new HashMap<>();
       List<List<String>> showdatas = new ArrayList<>();//data

       info.put("indexcode",icode);
       //span以及options
       String span;
       if (!(tablecol.equals("zb")||tablerow.equals("zb"))){
           span="指标选择";
           List<String> alltaskcode=pastViewService.getAllTask(icode);
           List<Map<String,String>> zbs=pastViewService.getModsList(alltaskcode);
           info.put("options",zbs);
       }
       else if (!(tablecol.equals("sj")||tablerow.equals("sj"))){
           span="时间选择";
       }
       else{
           span="地区选择";

       }
       info.put("span",span);




       if (StringUtil.isEmpty(pjax)) {
            this.getResponse().sendRedirect("/zbdata/pastviews.htm?id="+icode);
        }
        else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pasttable").addObject("info",info).addObject("showdata",showdatas);
        }
        return null;
   }
    /**
     * （模型节点选择最近五年默认值） 单地区传参展示
     * @author wf
     * @date
     * @param
     * @return
     */
    public ModelAndView regDatas() {

        OriginService os = new OriginService();
        //获取用户权限
       // String right = this.getRequest().getParameter("right");
        String regcode = this.getRequest().getParameter("code");
        String time = this.getRequest().getParameter("time");
        String icode = this.getRequest().getParameter("icode");
        String trow = this.getRequest().getParameter("tableRow");
        String tcol = this.getRequest().getParameter("tableCol");
        String change = "2";
        //时间 默认最近五期，最后修改时间传参维度
        List<String> fivetaskcode = pv.getAllTask(icode).subList(0, 5);
        List<String> last5 = pv.getAllTime(icode).subList(0, 5);
        String taskcode = fivetaskcode.get(0);
        List<String> regs = pv.getRegions(taskcode);
        List<Map<String, String>> reginfo = new ArrayList<>();
        for (int i = 0; i < regs.size(); i++) {
            Map<String, String> regmap = new HashMap<>();
            String regioncode = regs.get(i);
            String regname = os.getwdnode("reg", regioncode).getName();
            regmap.put("name", regname);
            regmap.put("regcode", regioncode);
            reginfo.add(regmap);
        }
        //得到模型节点name的list
        List<String> alltaskcode = pv.getAllTask(icode);
        List<Map<String,String>> allorcodes = pv.getModsList(alltaskcode);
        //用于模型下拉框选择展示
        List<String> modnames = new ArrayList<>();
        for(int i=0;i<allorcodes.size();i++) {
            String orname = allorcodes.get(i).get("name");
            modnames.add(orname);
        }
            //List<String> alltaskcode = pv.getAllTask(code);
        if(trow.equals("指标")&&tcol.equals("时间")){

            List<List<String>> showdatas = pv.getModTime(regcode,fivetaskcode,icode);
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata",showdatas).addObject("last5",last5).addObject("" +
                    "",reginfo)
                    .addObject("show",change).addObject("indexcode",icode);
        }else{
            List<List<String>> showdatas = pv.getTimeMod(regcode, fivetaskcode,last5,icode);
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata", showdatas).addObject("last5", last5).addObject("reginfo", reginfo)
                    .addObject("show", change).addObject("indexcode", icode).addObject("modnames",modnames);

        }

    }
    /**
     * 全部地区，单模型节点传参展示
     * @author wf
     * @date
     * @param
     * @return
     */
    public ModelAndView modDatas(){

        //获取用户权限
        String time = this.getRequest().getParameter("time");
        String icode = this.getRequest().getParameter("icode");
       // String right=this.getRequest().getParameter("right");
        String orcode=this.getRequest().getParameter("orcode");
        String trow = this.getRequest().getParameter("tableRow");
        String tcol = this.getRequest().getParameter("tableCol");
        OriginService originService=new OriginService();
        //List<String> fivemods = pv.findModByOrcode(code,orcode).subList(0,5);
        String change = "1";
        //时间 默认最近五年，最后修改时间传参维度
        List<String> fivetaskcode = pv.getAllTask(icode).subList(0,5);
        List<String> last5 = pv.getAllTime(icode).subList(0,5);
        List<String> alltaskcode = pv.getAllTask(icode);
        String taskcode = alltaskcode.get(0);
        List<String> regs = pv.getRegions(taskcode);
        List<String> regnames = new ArrayList<>();
        for(int m=0;m<regs.size();m++){
            String regioncode = regs.get(m);
            String regname = originService.getwdnode("reg",regioncode).getName();
            regnames.add(regname);
        }
        //List<String> modcodes = pv.findModByOrcode(alltaskcode,orcode);//所有年份的模型节点

        List<Map<String,String>> allorcodes = pv.getModsList(alltaskcode);
        //用于模型下拉框选择展示
        List<Map<String,String>> modinfo = new ArrayList<>();
        for(int i=0;i<allorcodes.size();i++) {
            Map<String,String> modmap = new HashMap<>();
            String code = allorcodes.get(i).get("orcode");
            String orname = allorcodes.get(i).get("name");
            modmap.put("name",orname);
            modmap.put("orcode",code);
            modinfo.add(modmap);

        }
        if(trow.equals("地区")&&tcol.equals("时间")){
            List<List<String>> regdatas = pv.getRegTime(fivetaskcode,orcode,icode);
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata",regdatas).addObject("last5",last5).addObject("show",change).addObject("modinfo",modinfo);
        }else{
            List<List<String>> regdatas = pv.getTimeReg(fivetaskcode,orcode,last5,icode);
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata",regdatas).addObject("last5",last5).addObject("show",change).addObject("modinfo",modinfo).addObject("regnames",regnames);
        }

    }
    /**
     * 全部地区，单模型节点传参展示
     * @author wf
     * @date
     * @param
     * @return
     */
    public ModelAndView timeDatas(){

        //获取用户权限
        OriginService os = new OriginService();
        //String right=this.getRequest().getParameter("right");
        String time = this.getRequest().getParameter("time");
        String icode = this.getRequest().getParameter("icode");
        String trow = this.getRequest().getParameter("tableRow");
        String tcol = this.getRequest().getParameter("tableCol");
        String change = "3";
        String lasttime = pv.getAllTime(icode).get(0);
        String taskcode = pv.getAllTask(icode).get(0);
        List<String> regs = pv.getRegions(taskcode);
        List<String> alltaskcode = pv.getAllTask(icode);
        List<Map<String,String>> allorcodes = pv.getModsList(alltaskcode);
        List<String> allMods = new ArrayList<>();
        for(int i=0;i<allorcodes.size();i++){
            String modcode = DataDao.Fator.getInstance().getIndexdatadao().findModCode(taskcode,allMods.get(i));
            allMods.add(modcode);
        }
        //模型节点name的list
        List<String> ornames = new ArrayList<>();
        for(int i=0;i<allorcodes.size();i++){
            String orname = allorcodes.get(i).get("name");
            ornames.add(orname);
        }
        //地区name的list
        List<String> regnames = new ArrayList<>();
        for(int j=0;j<regs.size();j++){
            String regioncode = regs.get(j);
            String regname = os.getwdnode("reg", regioncode).getName();
            regnames.add(regname);
        }
        if(trow.equals("地区")&&tcol.equals("指标")){
            List<List<String>> regdatas = pv.getRegMod(taskcode,lasttime,icode);
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata",regdatas).addObject("show",change)
                    .addObject("regnames",regnames).addObject("ornames",ornames);
        }else{
            List<List<String>> regdatas = pv.getModReg(taskcode,lasttime,icode);
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata",regdatas).addObject("show",change)
                    .addObject("regnames",regnames).addObject("ornames",ornames);
        }

    }

    /**
     * 单地区选择的数据下载
     * @author wf
     * @date
     * @param
     * @return
     */
    public void toRegExcel() throws IOException {
        //接参
        List<String> fivetaskcode = pv.getAllTask(code).subList(0,5);
        String regcode = PubInfo.getString(this.getRequest().getParameter("regcode"));
        String trow = PubInfo.getString(this.getRequest().getParameter("tableRow"));
        String tcol = PubInfo.getString(this.getRequest().getParameter("tableCol"));
        JSONReturnData data = new JSONReturnData("");
        if(regcode ==null){
            data.setReturncode(300);
            this.sendJson(data);
            return;
        } else {
            data.setReturncode(200);
        }
        //得到指标list
        List<String> alltaskcode = pv.getAllTask(code);
        List<Map<String,String>> allorcodes = pv.getModsList(alltaskcode);
        List<String> ornames = new ArrayList<>();
        for(int i=0;i<allorcodes.size();i++){
            String orname = allorcodes.get(i).get("name");
            ornames.add(orname);
        }
        List<String> last5 = pv.getAllTime(code).subList(0,5);
        ExcelBook book = new ExcelBook();
        ExcelSheet sheet1 = new ExcelSheet();
        sheet1.setName("sheet1");
        sheet1.addColumn();
        sheet1.addColumn();
        ExcelCell cell1 = new ExcelCell();
        if(trow.equals("指标")&&tcol.equals("时间")){
            List<List<String>> datas = pv.getModTime(regcode,fivetaskcode,code);
            ExcelRow dr1 = sheet1.addRow();
            ExcelCell cell2 = cell1.clone();
            cell2.setCellValue("指标");
            dr1.set(0, cell2);

            for (int k = 0; k < last5.size(); k++){
                int m =k+1;
                sheet1.addColumn();
                cell2 = cell1.clone();
                cell2.setCellValue(last5.get(k));
                dr1.set(m, cell2);
            }
            cell1.getCellstyle().getFont().setBoldweight((short) 10);
            for(int i=0;i<datas.size();i++){

                List<String> arr =datas.get(i);
                dr1 = sheet1.addRow();
                for(int j=0;j<arr.size();j++){
                    cell2 = cell1.clone();
                    cell2.setCellValue(arr.get(j));
                    dr1.set(j, cell2);
                }
            }
        }else{
            List<List<String>> datas = pv.getTimeMod(regcode,fivetaskcode,last5,code);
            ExcelRow dr1 = sheet1.addRow();
            ExcelCell cell2 = cell1.clone();
            cell2.setCellValue("时间");
            dr1.set(0, cell2);

            for (int k = 0; k < ornames.size(); k++){
                int m =k+1;
                sheet1.addColumn();
                cell2 = cell1.clone();
                cell2.setCellValue(ornames.get(k));
                dr1.set(m, cell2);
            }
            cell1.getCellstyle().getFont().setBoldweight((short) 10);
            for(int i=0;i<datas.size();i++){

                List<String> arr =datas.get(i);
                dr1 = sheet1.addRow();
                for(int j=0;j<arr.size();j++){
                    cell2 = cell1.clone();
                    cell2.setCellValue(arr.get(j));
                    dr1.set(j, cell2);
                }
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
    /**
     * 单模型节点选择的数据下载
     * @author wf
     * @date
     * @param
     * @return
     */
    public void toModExcel() throws IOException {
        //接参
        OriginService os = new OriginService();
        List<String> fivetaskcode = pv.getAllTask(code).subList(0,5);
        String orcode=PubInfo.getString(this.getRequest().getParameter("orcode"));
        //String regcode = PubInfo.getString(this.getRequest().getParameter("regcode"));
        String trow = PubInfo.getString(this.getRequest().getParameter("tableRow"));
        String tcol = PubInfo.getString(this.getRequest().getParameter("tableCol"));
        JSONReturnData data = new JSONReturnData("");
        if(orcode ==null){
            data.setReturncode(300);
            this.sendJson(data);
            return;
        } else {
            data.setReturncode(200);
        }
        /*List<String> alltaskcode = pv.getAllTask(code);
        List<Map<String,String>> allorcodes = pv.getModsList(alltaskcode);
        List<String> ornames = new ArrayList<>();
        for(int i=0;i<allorcodes.size();i++){
            String orname = allorcodes.get(i).get("name");
            ornames.add(orname);
        }*/
        //地区name的list
        String taskcode = pv.getAllTask(code).get(0);
        List<String> regs = pv.getRegions(taskcode);
        List<String> regnames = new ArrayList<>();
        for(int j=0;j<regs.size();j++){
            String regioncode = regs.get(j);
            String regname = os.getwdnode("reg", regioncode).getName();
            regnames.add(regname);
        }
        List<String> last5 = pv.getAllTime(code).subList(0,5);
        ExcelBook book = new ExcelBook();
        ExcelSheet sheet1 = new ExcelSheet();
        sheet1.setName("sheet1");
        sheet1.addColumn();
        sheet1.addColumn();
        ExcelCell cell1 = new ExcelCell();
        if(trow.equals("地区")&&tcol.equals("时间")){
            List<List<String>> regdatas = pv.getRegTime(fivetaskcode,orcode,code);
            ExcelRow dr1 = sheet1.addRow();
            ExcelCell cell2 = cell1.clone();
            cell2.setCellValue("地区");
            dr1.set(0, cell2);

            for (int k = 0; k < last5.size(); k++){
                int m =k+1;
                sheet1.addColumn();
                cell2 = cell1.clone();
                cell2.setCellValue(last5.get(k));
                dr1.set(m, cell2);
            }
            cell1.getCellstyle().getFont().setBoldweight((short) 10);
            for(int i=0;i<regdatas.size();i++){

                List<String> arr =regdatas.get(i);
                dr1 = sheet1.addRow();
                for(int j=0;j<arr.size();j++){
                    cell2 = cell1.clone();
                    cell2.setCellValue(arr.get(j));
                    dr1.set(j, cell2);
                }
            }
        }else{
            List<List<String>> regdatas = pv.getTimeReg(fivetaskcode,orcode,last5,code);
            ExcelRow dr1 = sheet1.addRow();
            ExcelCell cell2 = cell1.clone();
            cell2.setCellValue("时间");
            dr1.set(0, cell2);

            for (int k = 0; k < regnames.size(); k++){
                int m =k+1;
                sheet1.addColumn();
                cell2 = cell1.clone();
                cell2.setCellValue(regnames.get(k));
                dr1.set(m, cell2);
            }
            cell1.getCellstyle().getFont().setBoldweight((short) 10);
            for(int i=0;i<regdatas.size();i++){

                List<String> arr =regdatas.get(i);
                dr1 = sheet1.addRow();
                for(int j=0;j<arr.size();j++){
                    cell2 = cell1.clone();
                    cell2.setCellValue(arr.get(j));
                    dr1.set(j, cell2);
                }
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