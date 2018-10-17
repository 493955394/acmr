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
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.zhzs.IndexTask;
import com.acmr.service.zbdata.OriginService;
import com.acmr.service.zhzs.OriginDataService;
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
        String right=this.getRequest().getParameter("right");

        OriginService os = new OriginService();
        List<String> fivetaskcode = pv.getAllTask(icode).subList(0,5);
        List<String> alltaskcode = pv.getAllTask(icode);
        List<Map<String,String>> allmods = pv.getModsList(alltaskcode);
        List<String> orcodes = new ArrayList<>();
        List<String> ornames = new ArrayList<>();
        for(int i=0;i<allmods.size();i++){
            String code = allmods.get(i).get("orcode");
            String orname = allmods.get(i).get("name");
            orcodes.add(code);
            ornames.add(orname);
        }
        String change = "2";//1：数据地区+时间展示；2：数据指标+时间展示3：数据地区指标展示。

        String taskcode = fivetaskcode.get(0);
        List<String> alltime = pv.getAllTime(icode);
        /*if(alltime.size()>5){

        }*/
        List<String> last5 = pv.getAllTime(icode).subList(0,5);
        List<String> regs = pv.getRegions(taskcode);
        String reg = pv.getRegions(taskcode).get(0);
        List<Map<String,String>> reginfo = new ArrayList<>();
        for(int i=0;i<regs.size();i++) {
            Map<String,String> regmap = new HashMap<>();
            String regioncode = regs.get(i);
            String regname = os.getwdnode("reg", regioncode).getName();
            regmap.put("name",regname);
            regmap.put("regcode",regioncode);
            reginfo.add(regmap);

        }

        List<List<String>> showdatas = pv.getModData(reg,fivetaskcode);

        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata",showdatas).addObject("last5",last5).addObject("reginfo",reginfo)
                .addObject("orcode",orcodes).addObject("orname",ornames).addObject("show",change).addObject("indexcode",icode);
   }

    /**
     * 模型节点选择最近五年默认值,单地区传参展示
     * @author wf
     * @date
     * @param
     * @return
     */
    public ModelAndView regDatas() {

        OriginService os = new OriginService();
        //获取用户权限
        String right = this.getRequest().getParameter("right");
        String regcode = this.getRequest().getParameter("code");
        String time = this.getRequest().getParameter("time");
        String icode = this.getRequest().getParameter("icode");
        String trow = this.getRequest().getParameter("tableRow");
        String tcol = this.getRequest().getParameter("tableCol");
        String change = "2";
        //时间 默认最近五年，最后修改时间传参维度
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
        List<List<String>> showdatas = pv.getModData(regcode, fivetaskcode);
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata", showdatas).addObject("last5", last5).addObject("reginfo", reginfo)
                .addObject("show", change).addObject("indexcode", icode);
    }
            //List<String> alltaskcode = pv.getAllTask(code);
        /*if(trow.equals("指标")&&tcol.equals("时间")){

            List<List<String>> showdatas = pv.getModData(regcode,fivetaskcode);
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata",showdatas).addObject("last5",last5).addObject("reginfo",reginfo)
                    .addObject("show",change).addObject("indexcode",icode);
        }else{

        }


        }
*/
    /**
     * 全部地区，单模型节点传参展示
     * @author wf
     * @date
     * @param
     * @return
     */
    public ModelAndView modDatas(){

        //获取用户权限
        String icode = this.getRequest().getParameter("icode");
        String right=this.getRequest().getParameter("right");
        String orcode=this.getRequest().getParameter("orcode");

        //List<String> fivemods = pv.findModByOrcode(code,orcode).subList(0,5);
        String change = "1";
        //时间 默认最近五年，最后修改时间传参维度
        List<String> fivetaskcode = pv.getAllTask(icode).subList(0,5);
        List<String> last5 = pv.getAllTime(icode).subList(0,5);
        List<String> alltaskcode = pv.getAllTask(icode);
        String taskcode = alltaskcode.get(0);
        List<String> regs = pv.getRegions(taskcode);

        List<String> modcodes = pv.findModByOrcode(alltaskcode,orcode);//所有年份的模型节点

        List<Map<String,String>> allorcodes = pv.getModsList(alltaskcode);
        //用于模型下拉框选择展示
        List<Map<String,String>> modinfo = new ArrayList<>();
        for(int i=0;i<regs.size();i++) {
            Map<String,String> modmap = new HashMap<>();
            String code = allorcodes.get(i).get("orcode");
            String orname = allorcodes.get(i).get("name");
            modmap.put("name",orname);
            modmap.put("orcode",code);
            modinfo.add(modmap);

        }
        List<List<String>> regdatas = pv.getRegData(regs,fivetaskcode,modcodes,last5);
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata",regdatas).addObject("last5",last5).addObject("show",change).addObject("modinfo",modinfo);
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
        String right=this.getRequest().getParameter("right");
        String change = "3";
        String lasttime = pv.getAllTime(code).get(0);
        String taskcode = pv.getAllTask(code).get(0);
        List<String> regs = pv.getRegions(taskcode);
        List<String> alltaskcode = pv.getAllTask(code);
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
        List<List<String>> regdatas = pv.getTimeData(regs,taskcode,lasttime);
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("showdata",regdatas).addObject("show",change)
                .addObject("regnames",regnames).addObject("ornames",ornames);
    }
    //选择列为模型节点的数据下载
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