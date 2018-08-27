package com.acmr.web.jsp.zbdata;

import acmr.cubeinput.MetaTableException;
import acmr.util.DataTable;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.helper.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class zsjhedit extends BaseAction {
    //指数计划编辑页面
    public ModelAndView editIndex(){
        String code = this.getRequest().getParameter("id");
        code ="coo1";
        return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("code",code);
    }
}
