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
import com.acmr.dao.zhzs.IndexTaskDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.zhzs.IndexList;
import com.acmr.model.zhzs.IndexTask;
import com.acmr.service.zbdata.OriginService;
import com.acmr.service.zhzs.IndexListService;
import com.acmr.service.zhzs.OriginDataService;

import com.acmr.service.zhzs.PastViewService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
//import java.util.List;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Pattern;

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
        List<String> fivetaskcode = pv.getAllTask(icode);
        Map<String,Object> info=new HashMap<>();
        info.put("indexcode",icode);
        List<List<String>> showdatas=new ArrayList<>();
        if (fivetaskcode.size()==0) info.put("tasknum","0");
        else {
            info.put("tasknum",fivetaskcode.size());
            if (fivetaskcode.size()>5) fivetaskcode=fivetaskcode.subList(0,5);
            List<String> last5 = pv.getAllTime(icode);
            if (last5.size()>5) last5=last5.subList(0,5);
            List<Map<String,String>> regs=pv.getRegList(icode);
            String reg=regs.get(0).get("code");
            showdatas = pv.getModTime(reg,fivetaskcode,icode);//得到单地区data
            //展示的时间
            info.put("head",last5);
            info.put("row","指标");
            //存在的地区并集,用于select
            info.put("options",regs);
            info.put("span","地区选择");
            info.put("spancode",reg);
        }
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
       if (spancode.equals("null")) spancode=null;
       List<String> times= Arrays.asList(time.split(","));
       if (time.equals("null")) time=null;
       PastViewService pastViewService=new PastViewService();
       String pjax = req.getHeader("X-PJAX");
       Map<String,Object> info=new HashMap<>();
       List<List<String>> showdatas = new ArrayList<>();//data
       List<String> alltaskcodes=pastViewService.getAllTask(icode);
       info.put("indexcode",icode);
       if (alltaskcodes.size()==0) info.put("tasknum","0");
       else {
           info.put("tasknum",alltaskcodes.size());
           String span;
           List<String> head=new ArrayList<>();
           List<String> taskcodes=new ArrayList<>();
           //根据传来的time算出应该展示的taskcodes
           if (time != null){
               for(int i=0;i<times.size();i++){
                   String taskcode = IndexTaskDao.Fator.getInstance().getIndexdatadao().getTaskcode(icode,times.get(i));
                   taskcodes.add(taskcode);
               }
           }else{
               taskcodes=alltaskcodes;
               if (taskcodes.size()>5) taskcodes=taskcodes.subList(0,5);
           }
           if (!(tablecol.equals("zb")||tablerow.equals("zb"))){
               span="指标选择";
               List<String> alltaskcode=pastViewService.getAllTask(icode);
               //返回所有指标
               List<Map<String,String>> zbs=pastViewService.getModsList(alltaskcode);
               info.put("options",zbs);
               if (tablecol.equals("sj")){
                   showdatas=pastViewService.getRegTime(taskcodes,spancode,icode);
                   info.put("row","地区");
                   //List<String> sjhead=pastViewService.getAllTime(icode).subList(0,5);
                   List<String> sjhead;
                   if (time==null){
                       sjhead=pastViewService.getAllTime(icode);
                       if (sjhead.size()>5) sjhead=sjhead.subList(0,5);
                   }
                   else {
                       sjhead=times;
                   }
                   head=sjhead;
                   //info.put("head",sjhead);
               }
               else {
                   showdatas=pastViewService.getTimeReg(taskcodes,spancode,icode);
                   info.put("row","时间");
                   List<String> reghead=new ArrayList<>();
                   List<Map<String,String>> regs=pastViewService.getRegList(icode);
                   for (int i=0;i<regs.size();i++){
                       String reg=regs.get(i).get("name");
                       reghead.add(reg);
                   }
                   head=reghead;
                   //info.put("head",reghead);
               }

           }
           else if (!(tablecol.equals("sj")||tablerow.equals("sj"))){
               span="时间选择";
               String taskcode;
               if (spancode==null){
                   taskcode=null;
               }
               else{
                   taskcode = IndexTaskDao.Fator.getInstance().getIndexdatadao().getTaskcode(icode,spancode);
               }

               //返回所有时间
               List<String> ayearmons=pastViewService.getAllTime(icode);
               List<Map<String,String>> sjs=new ArrayList<>();
               for (int i=0;i<ayearmons.size();i++){
                   Map m=new HashMap();
                   m.put("code",ayearmons.get(i));
                   m.put("name",ayearmons.get(i));
                   sjs.add(m);
               }
               info.put("options",sjs);

               if (tablecol.equals("zb")){
                   showdatas=pastViewService.getRegMod(taskcode,icode);
                   info.put("row","地区");
                   List<String> zbhead=new ArrayList<>();
                   List<Map<String,String>> zbmap=pastViewService.getModsList(pastViewService.getAllTask(icode));
                   for (int i=0;i<zbmap.size();i++){
                       String zbname=zbmap.get(i).get("name");
                       zbhead.add(zbname);
                   }
                   head=zbhead;
                   //info.put("head",zbhead);
               }
               else {
                   showdatas=pastViewService.getModReg(taskcode,icode);
                   info.put("row","指标");
                   List<String> regs=new ArrayList<>();
                   List<String> reghead=new ArrayList<>();
                   List<Map<String,String>> regmap=pastViewService.getRegList(icode);
                   for (int i=0;i<regmap.size();i++){
                       String regname=regmap.get(i).get("name");
                       reghead.add(regname);
                   }
                   head=reghead;
                   // info.put("head",reghead);

               }
           }
           else{
               span="地区选择";
               //返回所有地区
               List<Map<String,String>> regs=pastViewService.getRegList(icode);
               info.put("options",regs);
               if (tablecol.equals("sj")){
                   showdatas=pastViewService.getModTime(spancode,taskcodes,icode);
                   info.put("row","指标");
                   //时间先写死最近5期，然后根据time来变
                   List<String> sjhead;
                   if (time==null){
                       sjhead=pastViewService.getAllTime(icode);
                       if (sjhead.size()>5) sjhead=sjhead.subList(0,5);
                   }
                   else {
                       sjhead=times;
                   }
                   head=sjhead;
                   //info.put("head",timehead);
               }
               else {
                   showdatas=pastViewService.getTimeMod(spancode,taskcodes,icode);
                   info.put("row","时间");
                   List<String> zbhead=new ArrayList<>();
                   List<Map<String,String>> zbmap=pastViewService.getModsList(pastViewService.getAllTask(icode));
                   for (int i=0;i<zbmap.size();i++){
                       String zbname=zbmap.get(i).get("name");
                       zbhead.add(zbname);
                   }
                   head=zbhead;
                   //info.put("head",zbhead);
               }
           }
           info.put("span",span);
           info.put("head",head);

           //序列化（spancode==null）后默认选择第一个
           if (spancode==null){
               if (span=="地区选择"){
                   spancode=pastViewService.getRegList(icode).get(0).get("code");
               }
               else if (span=="指标选择"){
                   List<String> alltaskcode=pastViewService.getAllTask(icode);
                   spancode=pastViewService.getModsList(alltaskcode).get(0).get("code");
               }
               else if (span=="时间选择"){
                   spancode=pastViewService.getAllTime(icode).get(0);
               }
           }
           info.put("spancode",spancode);

       }



       if (StringUtil.isEmpty(pjax)) {
            this.getResponse().sendRedirect(this.getContextPath() + "/zbdata/pastviews.htm?id="+icode);

        }
        else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pasttable").addObject("info",info).addObject("showdata",showdatas);
        }
        return null;
   }

    /**
     * 数据下载
     * @author wf
     * @date
     * @param
     * @return
     */
    public void toExcel() throws IOException {
        String spancode = PubInfo.getString(this.getRequest().getParameter("spancode"));
        String tablerow = PubInfo.getString(this.getRequest().getParameter("tableRow"));
        String tablecol = PubInfo.getString(this.getRequest().getParameter("tableCol"));
        String time = PubInfo.getString(this.getRequest().getParameter("time"));
        String icode = PubInfo.getString(this.getRequest().getParameter("icode"));
        PastViewService pastViewService=new PastViewService();
        List<String> alltaskcodes=pastViewService.getAllTask(icode);
        List<String> allTime = pastViewService.getAllTime(icode);
        if (spancode.equals("null")) spancode=null;
        List<String> times= Arrays.asList(time.split(","));
        if (time.equals("null")) time=null;

        List<List<String>> showdatas = new ArrayList<>();//data
        List<String> taskcodes = new ArrayList<>();
        List<String> sjhead = new ArrayList<>();
        ExcelBook book = new ExcelBook();
        ExcelSheet sheet1 = new ExcelSheet();
        sheet1.setName("sheet1");
        sheet1.addColumn();
        sheet1.addColumn();
        ExcelCell cell1 = new ExcelCell();
        if (!(tablecol.equals("zb")||tablerow.equals("zb"))){
            /*指标*/
            List<String> alltaskcode=pastViewService.getAllTask(icode);
            if (time != null){
                for(int i=0;i<times.size();i++){
                    String taskcode = IndexTaskDao.Fator.getInstance().getIndexdatadao().getTaskcode(icode,times.get(i));
                    taskcodes.add(taskcode);
                }

            }else{
                taskcodes=alltaskcodes;
                if (taskcodes.size()>5) taskcodes=taskcodes.subList(0,5);
            }
            List<Map<String,String>> zbs=pastViewService.getModsList(alltaskcode);
            if (tablecol.equals("sj")){
                showdatas=pastViewService.getRegTime(taskcodes,spancode,icode);

                if (time==null)
                    sjhead=allTime;
                    if(sjhead.size()>5) sjhead=sjhead.subList(0,5);
                else {
                    sjhead=times;
                }

                ExcelRow dr1 = sheet1.addRow();
                ExcelCell cell2 = cell1.clone();
                cell2.setCellValue("地区");
                dr1.set(0, cell2);

                for (int k = 0; k < sjhead.size(); k++){
                    int m =k+1;
                    sheet1.addColumn();
                    cell2 = cell1.clone();
                    cell2.setCellValue(sjhead.get(k));
                    dr1.set(m, cell2);
                }
                cell1.getCellstyle().getFont().setBoldweight((short) 10);
                for(int i=0;i<showdatas.size();i++){

                    List<String> arr =showdatas.get(i);
                    dr1 = sheet1.addRow();
                    for(int j=0;j<arr.size();j++){
                        cell2 = cell1.clone();
                        cell2.setCellValue(arr.get(j));
                        dr1.set(j, cell2);
                    }
                }


            }
            else {
                showdatas=pastViewService.getTimeReg(taskcodes,spancode,icode);
                List<String> reghead=new ArrayList<>();
                List<Map<String,String>> regs=pastViewService.getRegList(icode);
                for (int i=0;i<regs.size();i++){
                    String reg=regs.get(i).get("name");
                    reghead.add(reg);
                }
                ExcelRow dr1 = sheet1.addRow();
                ExcelCell cell2 = cell1.clone();
                cell2.setCellValue("时间");
                dr1.set(0, cell2);

                for (int k = 0; k < reghead.size(); k++){
                    int m =k+1;
                    sheet1.addColumn();
                    cell2 = cell1.clone();
                    cell2.setCellValue(reghead.get(k));
                    dr1.set(m, cell2);
                }
                cell1.getCellstyle().getFont().setBoldweight((short) 10);
                for(int i=0;i<showdatas.size();i++){

                    List<String> arr =showdatas.get(i);
                    dr1 = sheet1.addRow();
                    for(int j=0;j<arr.size();j++){
                        cell2 = cell1.clone();
                        cell2.setCellValue(arr.get(j));
                        dr1.set(j, cell2);
                    }
                }
            }

        }
        else if (!(tablecol.equals("sj")||tablerow.equals("sj"))){
            /*时间*/
            String taskcode="";
            if (time==null&&spancode==null){
                taskcode=null;
            }else{
                taskcode = IndexTaskDao.Fator.getInstance().getIndexdatadao().getTaskcode(icode,spancode);
            }
            //返回所有时间
            List<String> ayearmons=pastViewService.getAllTime(icode);
            if (tablecol.equals("zb")){
                showdatas=pastViewService.getRegMod(taskcode,icode);
                List<String> zbhead=new ArrayList<>();
                List<Map<String,String>> zbmap=pastViewService.getModsList(pastViewService.getAllTask(icode));
                for (int i=0;i<zbmap.size();i++){
                    String zbname=zbmap.get(i).get("name");
                    zbhead.add(zbname);
                }
                ExcelRow dr1 = sheet1.addRow();
                ExcelCell cell2 = cell1.clone();
                cell2.setCellValue("地区");
                dr1.set(0, cell2);

                for (int k = 0; k < zbhead.size(); k++){
                    int m =k+1;
                    sheet1.addColumn();
                    cell2 = cell1.clone();
                    cell2.setCellValue(zbhead.get(k));
                    dr1.set(m, cell2);
                }
                cell1.getCellstyle().getFont().setBoldweight((short) 10);
                for(int i=0;i<showdatas.size();i++){

                    List<String> arr =showdatas.get(i);
                    dr1 = sheet1.addRow();
                    for(int j=0;j<arr.size();j++){
                        cell2 = cell1.clone();
                        cell2.setCellValue(arr.get(j));
                        dr1.set(j, cell2);
                    }
                }
            }
            else {
                showdatas=pastViewService.getModReg(taskcode,icode);
                List<String> regs=new ArrayList<>();
                List<String> reghead=new ArrayList<>();
                List<Map<String,String>> regmap=pastViewService.getRegList(icode);
                for (int i=0;i<regmap.size();i++){
                    String regname=regmap.get(i).get("name");
                    reghead.add(regname);
                }

                ExcelRow dr1 = sheet1.addRow();
                ExcelCell cell2 = cell1.clone();
                cell2.setCellValue("指标");
                dr1.set(0, cell2);

                for (int k = 0; k < reghead.size(); k++){
                    int m =k+1;
                    sheet1.addColumn();
                    cell2 = cell1.clone();
                    cell2.setCellValue(reghead.get(k));
                    dr1.set(m, cell2);
                }
                cell1.getCellstyle().getFont().setBoldweight((short) 10);
                for(int i=0;i<showdatas.size();i++){

                    List<String> arr =showdatas.get(i);
                    dr1 = sheet1.addRow();
                    for(int j=0;j<arr.size();j++){
                        cell2 = cell1.clone();
                        cell2.setCellValue(arr.get(j));
                        dr1.set(j, cell2);
                    }
                }


            }
        }
        else{
            /*地区*/
            if (time != null){
                for(int i=0;i<times.size();i++){
                   String taskcode = IndexTaskDao.Fator.getInstance().getIndexdatadao().getTaskcode(icode,times.get(i));
                    taskcodes.add(taskcode);
                }

            }else{
                taskcodes=alltaskcodes;
                if (taskcodes.size()>5) taskcodes=taskcodes.subList(0,5);

            }
            //返回所有地区
            List<Map<String,String>> regs=pastViewService.getRegList(icode);
            if (tablecol.equals("sj")){
                showdatas=pastViewService.getModTime(spancode,taskcodes,icode);
                //时间先写死最近5期，然后根据time来变

                if (time==null) {
                    sjhead = allTime;
                    if (sjhead.size() > 5){
                        sjhead = sjhead.subList(0,5);
                    }
                }else {
                    sjhead=times;
                }
                ExcelRow dr1 = sheet1.addRow();
                ExcelCell cell2 = cell1.clone();
                cell2.setCellValue("指标");
                dr1.set(0, cell2);

                for (int k = 0; k < sjhead.size(); k++){
                    int m =k+1;
                    sheet1.addColumn();
                    cell2 = cell1.clone();
                    cell2.setCellValue(sjhead.get(k));
                    dr1.set(m, cell2);
                }
                cell1.getCellstyle().getFont().setBoldweight((short) 10);
                for(int i=0;i<showdatas.size();i++){

                    List<String> arr =showdatas.get(i);
                    dr1 = sheet1.addRow();
                    for(int j=0;j<arr.size();j++){
                        cell2 = cell1.clone();
                        cell2.setCellValue(arr.get(j));
                        dr1.set(j, cell2);
                    }
                }

            }
            else {
                showdatas=pastViewService.getTimeMod(spancode,taskcodes,icode);
                List<String> zbhead=new ArrayList<>();
                List<Map<String,String>> zbmap=pastViewService.getModsList(pastViewService.getAllTask(icode));
                for (int i=0;i<zbmap.size();i++){
                    String zbname=zbmap.get(i).get("name");
                    zbhead.add(zbname);
                }
                ExcelRow dr1 = sheet1.addRow();
                ExcelCell cell2 = cell1.clone();
                cell2.setCellValue("时间");
                dr1.set(0, cell2);

                for (int k = 0; k < zbhead.size(); k++){
                    int m =k+1;
                    sheet1.addColumn();
                    cell2 = cell1.clone();
                    cell2.setCellValue(zbhead.get(k));
                    dr1.set(m, cell2);
                }
                cell1.getCellstyle().getFont().setBoldweight((short) 10);
                for(int i=0;i<showdatas.size();i++){

                    List<String> arr =showdatas.get(i);
                    dr1 = sheet1.addRow();
                    for(int j=0;j<arr.size();j++){
                        cell2 = cell1.clone();
                        cell2.setCellValue(arr.get(j));
                        dr1.set(j, cell2);
                    }
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
     * 时间校验,以及补齐
     */
    public void timeCheck() throws IOException {
        HttpServletRequest req = this.getRequest();
        String time = req.getParameter("timeinput");//得到时间搜索框中的时间
        String icode = req.getParameter("icode");//得到计划的icode
        JSONReturnData data = new JSONReturnData("");
        PastViewService pv = new PastViewService();
        List<String> alltimelist = pv.getAllTime(icode);//得到这个计划所有的任务期的时间
        List<String> timelist = new ArrayList<>();
        IndexListService ls = new IndexListService();
        String sort = ls.getData(icode).getSort();//看是年度的还是月度的还是季度的
        String[] times = time.split(",");
        for (int i = 0; i <times.length ; i++) {
            if(times[i].equals("")){continue;}
           else  if(checkLast(times[i])){//如果存在last这个字母
                try{
                    int num = Integer.parseInt(times[i].substring(times[i].indexOf("t")+1));
                    if(num>=alltimelist.size()){//要是最新期数大于等于总共的期数，返回总共的期数
                        timelist = alltimelist;
                    }
                    else {
                        for (int j = 0; j <num ; j++) {
                            if((!timelist.contains(alltimelist.get(j)))){
                            timelist.add(alltimelist.get(j));}
                        }
                    }
                }catch (NumberFormatException e){
                    data.setReturncode(300);
                    break;
                }
            }
            else if(checkstart(times[i])){//要是存在横杠
                String begintime = times[i].substring(0,times[i].indexOf("-"));
                String endtime = times[i].substring(times[i].indexOf("-")+1);
                List<String> tmp = getTime1(begintime,endtime,sort,alltimelist);
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
                String bt = getTimeFormat(times[i],sort);
                String et = getEndTimeFormat(times[i],sort);
                List<String> templist =new ArrayList<>();
                if(et.compareTo(bt)>=0){
                    for (String arr : alltimelist) {
                        if (arr.compareTo(bt) >= 0 && arr.compareTo(et)<=0) {
                            templist.add(arr);
                        }
                    }
                }
                for(String arr : templist){
                    if(!timelist.contains(arr)){//没有才加上
                        timelist.add(arr);
                    }
                }
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
     * 处理从某一时间-某一时间的格式
     * @param begintime
     * @param endtime
     * @param sort
     * @param alltimelist
     * @return
     */
   public List<String> getTime1(String begintime, String endtime,String sort,List<String> alltimelist) {
        List<String> list = new ArrayList<>();
        if(endtime.equals("")) {//没有结束时间
            String bt = getTimeFormat(begintime,sort);
            for (String i : alltimelist) {
                if (i.compareTo(bt) >= 0) {
                    list.add(i);
                }
            }
        }
        else {//有结束时间
            String bt = getTimeFormat(begintime,sort);
            String et = getEndTimeFormat(endtime,sort);
            if(et.compareTo(bt)>=0){
                for (String i : alltimelist) {
                    if (i.compareTo(bt) >= 0 && i.compareTo(et)<=0) {
                        list.add(i);
                    }
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
     * 处理结束时间格式为对应的icode的时间格式
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

   /* public static void main(String[] args) {
        String str ="last5";
        String endtime = "201301";
        List<String> list = new ArrayList<>();
        List<String> test = new ArrayList<>();
        list.add("2013B");
        list.add("2014A");
        list.add("2011D");
        Collections.sort(list,Collections.reverseOrder());
            System.out.println(str.substring(str.indexOf("t")+1));
    }*/
    }