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
        OriginService os = new OriginService();
        List<String> fivetaskcode = pv.getAllTask(icode).subList(0,5);
        String taskcode = fivetaskcode.get(0);
        List<String> last5 = pv.getAllTime(icode).subList(0,5);
        Map<String,String> regsmap = pv.getRegList(icode);
        List<String> regcodes=new ArrayList<>(regsmap.keySet());
        List<Map<String,String>> regs=new ArrayList<>();
        for (int i=0;i<regcodes.size();i++){
            Map<String,String> m=new HashMap<>();
            m.put("code",regcodes.get(i));
            m.put("name",regsmap.get(regcodes.get(i)));
            regs.add(m);
        }
        String reg=regs.get(0).get("code");
        //String reg = pv.getRegions(taskcode).get(0);
        List<List<String>> showdatas = pv.getModTime(reg,fivetaskcode);//得到单地区data
        Map<String,Object> info=new HashMap<>();
        //展示的时间
        info.put("time",last5);
        //存在的地区并集,用于select
        info.put("options",regs);
        info.put("indexcode",icode);
        //info.put("show","ModTime");
        info.put("span","地区选择");
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata",showdatas).addObject("info",info);
   }




   public void reTable(){

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

            List<List<String>> showdatas = pv.getModTime(regcode,fivetaskcode);
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata",showdatas).addObject("last5",last5).addObject("" +
                    "",reginfo)
                    .addObject("show",change).addObject("indexcode",icode);
        }else{
            List<List<String>> showdatas = pv.getTimeMod(regcode, fivetaskcode,last5);
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
            List<List<String>> regdatas = pv.getRegTime(fivetaskcode,orcode);
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata",regdatas).addObject("last5",last5).addObject("show",change).addObject("modinfo",modinfo);
        }else{
            List<List<String>> regdatas = pv.getTimeReg(fivetaskcode,orcode,last5);
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
            List<List<String>> regdatas = pv.getRegMod(taskcode,lasttime);
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata",regdatas).addObject("show",change)
                    .addObject("regnames",regnames).addObject("ornames",ornames);
        }else{
            List<List<String>> regdatas = pv.getModReg(taskcode,lasttime);
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
            List<List<String>> datas = pv.getModTime(regcode,fivetaskcode);
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
            List<List<String>> datas = pv.getTimeMod(regcode,fivetaskcode,last5);
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
            List<List<String>> regdatas = pv.getRegTime(fivetaskcode,orcode);
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
            List<List<String>> regdatas = pv.getTimeReg(fivetaskcode,orcode,last5);
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
                String temp = getTimeFormat(times[i],sort);
                if(alltimelist.contains(temp)&&(!timelist.contains(temp))){//时间期里有，临时的时间list里没有
                    timelist.add(temp);
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
            String et = getTimeFormat(endtime,sort);
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
     * @Description: 返回ABCD对应的月份
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