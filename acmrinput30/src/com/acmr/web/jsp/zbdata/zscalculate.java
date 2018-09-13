package com.acmr.web.jsp.zbdata;

import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class zscalculate extends BaseAction {
    /**
    * @Description: 从data表中读数时返回页面
    * @Param: []
    * @return: acmr.web.entity.ModelAndView
    * @Author: lyh
    * @Date: 2018/9/13
    */
    public ModelAndView ZsCalculate(){
        List<String> data=new ArrayList<>();
        //String test="重新计算";
        HttpServletRequest req=this.getRequest();
        String taskcode=req.getParameter("taskcode");
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/zscalculate").addObject("data",data);
    }

    /**
    * @Description: 从tmp表中读数时返回页面
    * @Param: []
    * @return: acmr.web.entity.ModelAndView
    * @Author: lyh
    * @Date: 2018/9/13
    */
    public ModelAndView ReCalculate(){
        HttpServletRequest req=this.getRequest();
        String sessionid=req.getSession().getId();
        String taskcode=req.getParameter("taskcode");
        //取对应id的数据
        List<String> data=new ArrayList<>();
        //String test="继续上次计算";
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/zscalculate").addObject("data",data);

    }


    /**
    * @Description: istmp为true，表示从临时表中读数，false从data表中读数，返回rows，用于绘制原始数据表
    * @Param: [istmp,taskcode]
    * @return: java.util.List<java.util.List<java.lang.String>>
    * @Author: lyh
    * @Date: 2018/9/13
    */
    public List<List<String>> getOriginData(Boolean istmp,String taskcode){
        List<List<String>> rows=new ArrayList<>();
        //从临时表中取数
        if (istmp){

        }
        //从原始表中取数
        else {

        }
        return rows;
    }

}
