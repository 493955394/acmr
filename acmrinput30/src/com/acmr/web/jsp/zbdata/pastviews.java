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
    public ModelAndView main(){
        String indexcode = this.getRequest().getParameter("id");
        //获取用户权限
        String right=this.getRequest().getParameter("right");

       // List<String> alltaskcode =
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/pastviews");
    }


    }