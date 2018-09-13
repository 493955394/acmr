package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class zscalculate extends BaseAction {
    public ModelAndView main() throws IOException {
        List<String> data=new ArrayList<>();
        String test="重新计算";
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/zscalculate").addObject("data",data).addObject("test",test);
    }

    public ModelAndView ReCalculate(){
        HttpServletRequest req=this.getRequest();
        String sessionid=req.getSession().getId();
        //取对应id的数据
        List<String> data=new ArrayList<>();
        String test="继续上次计算";
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/zscalculate").addObject("data",data).addObject("test",test);

    }
}
