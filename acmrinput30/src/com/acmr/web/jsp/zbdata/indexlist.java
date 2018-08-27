package com.acmr.web.jsp.zbdata;

import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;

import java.io.IOException;

public class indexlist extends BaseAction {
    public ModelAndView main() throws IOException {
        String test="this is the listjps";
        return new ModelAndView("/WEB-INF/jsp/zhzs/indexlist").addObject("test",test);
    }
}
