package com.acmr.web.jsp.zbdata;

import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;

public class zsjhedit extends BaseAction {
    //指数计划编辑页面
    public ModelAndView editIndex(){
        String code ="R0010";
        return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("code",code);
    }
}
