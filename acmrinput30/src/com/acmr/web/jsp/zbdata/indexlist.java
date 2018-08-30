package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.pub.PageBean;
import com.acmr.model.zhzs.IndexList;
import com.acmr.service.zhzs.IndexListService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class indexlist extends BaseAction {
    public ModelAndView main() throws IOException {
        String test="this is the listjps";
        ArrayList<IndexList> indexlist= new IndexListService().getIndexList();
        return new ModelAndView("/WEB-INF/jsp/zhzs/indexlist").addObject("test",test).addObject("indexlist",indexlist);
    }

    public void insert() throws IOException{
        HttpServletRequest req = this.getRequest();
        String ifdata = req.getParameter("ifdata");
        String code = req.getParameter("code");
        String cname = req.getParameter("cname");
        String idcata = req.getParameter("idcata");
        System.out.println(idcata);

        JSONReturnData data = new JSONReturnData(200);

        this.sendJson(data);
    }

    /**
     * 综合指数查找
     * @return
     * @throws IOException
     */
    public ModelAndView find() throws IOException{
        HttpServletRequest req = this.getRequest();
        ArrayList<IndexList> indexlist= new ArrayList<IndexList>();
        // 获取查询数据
        String code = StringUtil.toLowerString(req.getParameter("code"));
        String cname = StringUtil.toLowerString(req.getParameter("cname"));
        // 判断是否pjax 请求
        String pjax = req.getHeader("X-PJAX");
        if (!StringUtil.isEmpty(code)) {
            indexlist= new IndexListService().found(0,code);
        }
        if (!StringUtil.isEmpty(cname)) {
            indexlist= new IndexListService().found(1,cname);
        }
        Map<String, String> codes = new HashMap<String, String>();
        codes.put("code", code);
        codes.put("cname", cname);
        if (StringUtil.isEmpty(pjax)) {
            return new ModelAndView("/WEB-INF/jsp/zhzs/indexlist").addObject("indexlist",indexlist).addObject("codes",codes);
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/indextable").addObject("indexlist",indexlist).addObject("codes",codes);
        }
    }


}
