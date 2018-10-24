package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.pub.PageBean;
import com.acmr.model.zhzs.IndexTask;
import com.acmr.service.zhzs.IndexTaskService;

import java.text.DecimalFormat;
import javax.servlet.http.HttpServletRequest;

import acmr.util.PubInfo;
import com.acmr.service.zbdata.OriginService;
import com.acmr.service.zhzs.IndexTaskService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import acmr.excel.pojo.Constants.XLSTYPE;
import acmr.excel.pojo.ExcelBook;
import acmr.excel.pojo.ExcelCell;
import acmr.excel.pojo.ExcelRow;
import acmr.excel.pojo.ExcelSheet;

import acmr.web.control.BaseAction;
import com.acmr.model.pub.JSONReturnData;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.acmr.helper.util.StringUtil.toStringWithZero;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class zstask extends BaseAction {

    public ModelAndView main() throws IOException {
        String icode = this.getRequest().getParameter("icode");
        String right=this.getRequest().getParameter("right");
        IndexTaskService task = new IndexTaskService();
        PageBean<IndexTask> page=new PageBean<>();
        List<IndexTask> alllist=task.getAllTask(icode);
        List<IndexTask> tasklist = task.getTaskByIcode(icode,page.getPageNum()-1,page.getPageSize());
        StringBuffer sb = new StringBuffer();
        sb.append(this.getRequest().getRequestURI());
        sb.append("?m=turn&icode="+icode);
        page.setData(tasklist);
        page.setUrl(sb.toString());
        page.setTotalRecorder(alllist.size());
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/taskindex").addObject("page",page).addObject("icode",icode).addObject("right",right);
    }

    /**
    * @Description: 翻页
    * @Param: []
    * @return: acmr.web.entity.ModelAndView
    * @Author: lyh
    * @Date: 2018/9/19
    */

    public ModelAndView turn() throws IOException {
        HttpServletRequest req = this.getRequest();
        String pjax = req.getHeader("X-PJAX");
        String icode=req.getParameter("icode");
        PageBean<IndexTask> page=new PageBean<>();
        IndexTaskService indexTaskService=new IndexTaskService();
        List<IndexTask> alllist=indexTaskService.getAllTask(icode);
        List<IndexTask> taskList=indexTaskService.getTaskByIcode(icode,page.getPageNum()-1,page.getPageSize());
        page.setData(taskList);
        page.setTotalRecorder(alllist.size());
        StringBuffer sb = new StringBuffer();
        sb.append(req.getRequestURL());
        sb.append("?m=turn&icode="+icode);
        page.setUrl(sb.toString());
        if (StringUtil.isEmpty(pjax)) {
            PubInfo.printStr("isempty");
            this.getResponse().sendRedirect(this.getContextPath()+"/zbdata/zstask.htm?icode="+icode+"&right=2");
        } else {
            PubInfo.printStr("pjax");
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/tasktable").addObject("page",page);
        }
        return null;
    }
    /**
    * @Description:  根据传来的session在数据临时表中找是否有记录过的
    * @Param: []
    * @return: void
    * @Author: lyh
    * @Date: 2018/9/13
    */
    public void findSession() throws IOException {
        PubInfo.printStr("===================findsession");
        HttpServletRequest req=this.getRequest();
        String sessionid=req.getSession().getId();
        PubInfo.printStr("id:"+sessionid);
        String taskcode=req.getParameter("taskcode");
        IndexTaskService indexTaskService=new IndexTaskService();
        this.sendJson(indexTaskService.findSession(sessionid,taskcode));
    }

    /**
     * 指数任务的查询
     * @return
     * @throws IOException
     */
    public ModelAndView findTask() throws IOException{
        HttpServletRequest req = this.getRequest();
        // 获取查询数据
        IndexTaskService indexTaskService =new IndexTaskService();
        String time = StringUtil.toLowerString(req.getParameter("time"));
        String icode = PubInfo.getString(req.getParameter("id"));
        List<IndexTask> indexTask = new ArrayList<>();
       List<IndexTask> indexTaskList =  new ArrayList<>();
        if(time.equals("")){
            indexTask = indexTaskService.getAllTask(icode);

        }
        else {
            indexTask = indexTaskService.findByTime(time,icode);
        }
        // 判断是否pjax 请求
        String pjax = req.getHeader("X-PJAX");
        PageBean<IndexTask> page=new PageBean<>();
        StringBuffer sb = new StringBuffer();
        sb.append(this.getRequest().getRequestURI());
        sb.append("?m=findTask&time="+time+"&id="+icode);
        if(time.equals("")){
            indexTaskList = indexTaskService.getTaskByIcode(icode,page.getPageNum()-1,page.getPageSize());
        }
        else {
            indexTaskList = indexTaskService.findByTime(time,icode,page.getPageNum() - 1,page.getPageSize());
        }
        page.setData(indexTaskList);
        page.setTotalRecorder(indexTask.size());
        page.setUrl(sb.toString());
        if (StringUtil.isEmpty(pjax)) {

            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/taskindex").addObject("page",page).addObject("icode",icode);
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/tasktable").addObject("page",page);
        }
    }

    /**
     * 删除任务
     * @throws IOException
     */
    public void delTask() throws IOException{
        HttpServletRequest req = this.getRequest();
        // 获取要删的数据
        JSONReturnData data = new JSONReturnData("");
        IndexTaskService indexTaskService =new IndexTaskService();
        String code = PubInfo.getString(req.getParameter("code"));
        int result = indexTaskService.delTask(code);
        if(result == 1){
            data.setReturncode(200);
            this.sendJson(data);
        }
        else {
            data.setReturncode(400);
            this.sendJson(data);
        }
    }
    /**
     * 文件上传
     * @author wf
     * @date
     * @param
     * @return
     */
    public void insertTaskData() {
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
                                data.setReturndata("指标或地区有误，请重新下载再进行数据修改上传");
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
                                            if(isDouble(value)||isInteger(value)){
                                                if(value.equals("  ")){
                                                    data.setReturncode(300);
                                                    data.setReturndata("数据不能为空");
                                                    this.sendJson(data);
                                                    return;
                                                }
                                                reganddata.add(value);
                                            }else{

                                                data.setReturncode(300);
                                                data.setReturndata("数据格式不正确");
                                                this.sendJson(data);
                                                return;
                                            }
                                            //System.out.println(value);
                                            /*if (StringUtil.isEmpty(value)) {
                                                continue;
                                            }*/
                                            //Pattern pattern = Pattern.compile("^[-//+]?//d+(//.//d*)?|//.//d+$");
                                            /*if (!mkey.containsValue(value)) {
                                                mkey.put(regscode.get(i), value);
                                            }*/


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
     * 判断value是否为double
     * @author wf
     * @date
     * @param
     * @return
     */
    public  boolean checkDouble(String str) {
        //Pattern p = Pattern.compile("^[-//+]?//d+(//.//d*)?|//.//d+$");
        Pattern p = Pattern.compile("^(([1-9][0-9]*)|((([1-9][0-9]*)|0)\\.[0-9][0-9]))$");

        return p.matcher(str).matches();
        //return false;
    }
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    /**
     * Double类型数据非科学计数法表示
     *
     * @param obj
     * @return 返回格式化后的Double数据类型
     */
    public static String formatDouble(Object obj) {
        DecimalFormat fmt = new DecimalFormat("###0.00");
        String str = String.valueOf(obj);
        if (obj instanceof Double) {
            return fmt.format(obj);
        } else if (str.matches("^[-\\+]?\\d+(\\.\\d+)?$")) {
            return fmt.format(Double.valueOf(str));
        } else {
            return toStringWithZero(obj);
        }
    }

    /**
     * 判断是否数值型
     *
     * @param string
     * @return
     */
    /*public static boolean isNumber(String string) {
        if (string == null || string.equals("")) {
            return false;
        }
        if (string.indexOf(".") == 0
                || string.indexOf(".") == string.length() - 1) {
            return false;
        }
        String validateStr = "0123456789.";
        for (int i = 0; i < string.length(); i++) {
            if (validateStr.indexOf(string.substring(i, i + 1)) == -1) {
                return false;
            }
        }
        return true;
    }*/

    /**
     * 判断是否整数型
     *
     * @param string
     * @return
     */
    public static boolean isInteger(String string) {
        if (string == null || string.equals("")) {
            return false;
        }
        String validateStr = "0123456789";
        for (int i = 0; i < string.length(); i++) {
            if (validateStr.indexOf(string.substring(i, i + 1)) == -1) {
                return false;
            }
        }
        return true;
    }


}
