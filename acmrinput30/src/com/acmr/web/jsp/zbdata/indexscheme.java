package com.acmr.web.jsp.zbdata;

import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.zhzs.Scheme;
import com.acmr.service.zhzs.IndexSchemeService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class indexscheme extends BaseAction {
    private IndexSchemeService indexSchemeService=new IndexSchemeService();
    public ModelAndView getSchemeList() throws IOException {
        HttpServletRequest req = this.getRequest();
        String icode=req.getParameter("icode");
        String pjax = req.getHeader("X-PJAX");
        //获得计划所有的方案
        List<Scheme> schemes=indexSchemeService.getSchemesByIcode(icode);

        if (StringUtil.isEmpty(pjax)) {
            //PubInfo.printStr("isempty");
            this.getResponse().sendRedirect(this.getContextPath() + "/zbdata/zsjhedit.htm?id="+icode);
        } else {
            // PubInfo.printStr("pjax");
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/schemeTable").addObject("schemes",schemes);
        }
        return null;
    }
}
