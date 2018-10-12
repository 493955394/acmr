package com.acmr.web.jsp.zbdata;

import acmr.excel.pojo.Constants.XLSTYPE;
import acmr.excel.pojo.ExcelBook;
import acmr.excel.pojo.ExcelCell;
import acmr.excel.pojo.ExcelRow;
import acmr.excel.pojo.ExcelSheet;
import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.zhzs.IndexList;
import com.acmr.service.zbdata.OriginService;
import com.acmr.service.zhzs.IndexListService;
import com.acmr.service.zhzs.IndexTaskService;
import com.acmr.service.zhzs.PastViewService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class pastviews extends BaseAction {

    PastViewService pv = new PastViewService();
    /**
     * 地区选择最近五年默认值
     * @author wf
     * @date
     * @param
     * @return
     */
    public ModelAndView main(){
        String code = this.getRequest().getParameter("id");
        //获取用户权限
        String right=this.getRequest().getParameter("right");

        List<String> fivetaskcode = pv.getAllTask(code).subList(0,5);
        List<String> alltaskcode = pv.getAllTask(code);

        String taskcode = fivetaskcode.get(0);
        List<String> last5 = pv.getAllTime(code).subList(0,5);
        String reg = pv.getRegions(taskcode).get(0);
        List<Map> allmod = pv.getAllMods(alltaskcode);
        List<List<String>> moddatas = pv.getModData(reg,fivetaskcode,allmod,last5);

        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews").addObject("moddata",moddatas).addObject("last5",last5);
    }

    /**
     * 模型节点选择最近五年默认值
     * @author wf
     * @date
     * @param
     * @return
     */
    public ModelAndView RegDatas(){
        String code = this.getRequest().getParameter("id");
        //获取用户权限
        String right=this.getRequest().getParameter("right");

        List<String> alltaskcode = pv.getAllTask(code);
        if(alltaskcode != null){
            String taskcode = alltaskcode.get(0);
            List<String> regs = pv.getRegions(taskcode);
            List<String> last5 = pv.getAllTime(code).subList(0,5);
            String mod = pv.getAllMods(alltaskcode).get(0).get("code").toString();
            List<List<String>> regdatas = pv.getRegData(regs,alltaskcode,mod,last5);
        }
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews");
    }


    }