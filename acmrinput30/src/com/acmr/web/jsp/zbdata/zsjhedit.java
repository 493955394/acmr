package com.acmr.web.jsp.zbdata;

import acmr.cubeinput.MetaTableException;
import acmr.util.DataTable;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.range.Pinfo;
import com.acmr.model.zhzs.IndexList;
import com.acmr.service.zhzs.IndexListService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class zsjhedit extends BaseAction {
    //指数计划编辑页面
    private IndexListService indexService = new IndexListService();


    public ModelAndView editIndex(){
        String code = this.getRequest().getParameter("id");
        String proname =null;
        IndexListService indexListService=new IndexListService();
        IndexList list =indexListService.getData(code);
        String procode = list.getProcode();
        if(procode!= null && procode!=""){//处理为空的步骤
            IndexList list1 =indexListService.getData(procode);
             proname = list1.getCname();
        }
        ArrayList<IndexList> indexlist= new IndexListService().getIndexList();
        return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("proname",proname).addObject("list",list).addObject("indexlist",indexlist);
    }

//    public static void main(String[] args){
//        IndexListService indexListService=new IndexListService();
//        IndexList list =indexListService.getData("R001");
//        String procode = "0";
//        if(list.getProcode()!=null && list.getProcode()!=""){
//            procode = "1";
//        }
//        System.out.println(procode);
//    }
}
