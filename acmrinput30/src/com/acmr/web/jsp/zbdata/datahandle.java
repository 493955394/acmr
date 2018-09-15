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
import acmr.web.control.BaseAction;
import acmr.web.core.CurrentContext;
import acmr.web.entity.ModelAndView;

import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.pub.PageBean;
import com.acmr.service.metadata.MetaDataExport;
import com.acmr.service.metadata.MetaService;
import com.acmr.service.metadata.MetaServiceManager;
public class datahandle extends BaseAction {

    /**
     *  重新读取数据
     *
     * @author wf
     * @date
     * @param
     * @return
     */
    public ModelAndView reGetData(){
        HttpServletRequest req=this.getRequest();
        String sessionid=req.getSession().getId();
        IndexTaskService indexTaskService=new IndexTaskService();
        OriginService originService=new OriginService();
        String taskcode=req.getParameter("taskcode");
        List<String> origionZBcodes = indexTaskService.getZBcodes(taskcode);//取得指标code
        String[] ZBcodes = origionZBcodes.toArray(new String[origionZBcodes.size()]);
        List<String> reg=indexTaskService.getTaskRegs(taskcode);//地区
        String[] regscode = reg.toArray(new String[reg.size()]);
        String sj=indexTaskService.getTime(taskcode);//时间ayearmon
        /*String[] reg = regscode.split(",");
        String[] regnames = regname.split(",");
        String[] zbcodes = zbcode.split(",");
        String[] zbnames = zbname.split(",");
        */
        List<String> regs=new ArrayList<>();
        for (int i=0;i<reg.size();i++){
            regs.add(originService.getwdnode("reg",reg.get(i)).getName());
        }
        List<List<String>> data = new ArrayList<>();
        for(int j=0;j<ZBcodes.length;j++){
            List<String> rows = new ArrayList<>();
            String zbcode = ZBcodes[j];
            rows.add(indexTaskService.getzbname(zbcode));
            for(int m=0;m<regscode.length;m++){
                CubeWdCodes where = new CubeWdCodes();
                String funit=originService.getwdnode("zb",zbcode).getUnitcode();
                String zbunit =indexTaskService.getTaskzb(taskcode).get(m).getUnitcode();
                String[] units = zbunit.split(",");
                String co = indexTaskService.getTaskzb(taskcode).get(m).getCompany();//主体
                String[] cos = co.split(",");
                String ds = indexTaskService.getTaskzb(taskcode).get(m).getDatasource();//数据来源
                String[] dss = ds.split(",");
                double rate=originService.getRate(funit,units[j],sj);
                where.Add("zb", ZBcodes[j]);
                where.Add("ds", dss[j]);
                where.Add("co", cos[j]);
                where.Add("reg", Arrays.asList(regscode));
                where.Add("sj", sj);
                ArrayList<CubeQueryData> result = RegdataService.queryData("cuscxnd",where);
                /*rows.add(sjs[i]);//获取时间
                datas.add(zbnames[j]);//获取指标*/
                for (int k = 0; k <result.size() ; k++) {
                    if(result.get(k).getData().toString() != ""){
                        double resulttemp = result.get(k).getData().getData()*rate;
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

    }
    /**
     * 文件上传
     *
     * @author wf
     * @date
     * @param
     * @return
     */
    public void importTaskData() {
        CreateTaskService createTaskService = new CreateTaskService();
        JSONReturnData data = new JSONReturnData("");
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
                        // 必填项
                        Map<Integer, String> mkey = new HashMap<Integer, String>();
                        // 数据量
                        int count = 0;
                        //获取地区
                        String reg = sheet.getRows().get(0).toString().substring(1,sheet.getRows().get(0).toString().length()-1);
                        String [] regname = reg.split(",");
                        List code = new ArrayList();
                        for (int i=0;i<regname.length-1;i++){
                            int j = i+1;
                            System.out.println(regname[j]);

                        }
                        //测试指标列
                        String [] zbname = sheet.getCols().get(1).toString().split(",");
                        for(int k=0;k<zbname.length-1;k++){
                            int l = k+1;
                            System.out.println(zbname[l]);
                        }
                        // 遍历标识列
                        if (rows >= 1 && sheet.getRows().get(1) != null) {
                            ExcelRow firstRow = sheet.getRows().get(1);
                            if (firstRow != null) {
                                int cells = firstRow.getCells().size();
                                for (int i = 0; i < cells; i++) {
                                    ExcelCell cell = firstRow.getCells().get(i);
                                    if (cell != null) {
                                        String value = cell.getText() + "";
                                        if (StringUtil.isEmpty(value)) {
                                            continue;
                                        }
                                        if (!mkey.containsValue(value)) {
                                            mkey.put(i, value);
                                        }
                                    }
                                }
                            }
                        }
                        boolean uState = false;
                        List<Map<String, Object>> update = new ArrayList<Map<String, Object>>(); // 更新
                        // 如果插入的数据量大于10000条，则提示用户数量超标
                        if (count >= 10000) {
                            data.setReturncode(400);
                            data.setReturndata("导入的数据不能超过10000行，目前有" + count + "行");
                            return;
                        }
                        // 入库
                        if (uState) {
                            //createTaskService.UpdateRows(update);
                        }
                        data.setParam1(count);
                        data.setReturncode(200);
                        data.setReturndata("数据文件上传成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                        data.setReturncode(500);
                        data.setReturndata("数据导入失败");
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