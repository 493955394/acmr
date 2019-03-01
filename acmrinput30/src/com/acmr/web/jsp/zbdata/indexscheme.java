package com.acmr.web.jsp.zbdata;

import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.dao.zhzs.WeightEditDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.zhzs.Scheme;
import com.acmr.service.zhzs.IndexSchemeService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class indexscheme extends BaseAction {
    private IndexSchemeService indexSchemeService=new IndexSchemeService();
    /**
    * @Description: 返回计划方案列表
    * @Param: []
    * @return: acmr.web.entity.ModelAndView
    * @Author: lyh
    * @Date: 2019/1/16
    */
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
    /**
     * 方案新增
     * @author wf
     * @param
     * @return
     */
    public void addscheme() throws IOException {
        HttpServletRequest req = this.getRequest();
        String icode = PubInfo.getString(req.getParameter("indexcode"));
        //String indexcode = PubInfo.getString(req.getParameter("icode"));
        String scode = PubInfo.getString(req.getParameter("schemecode"));
        String cname = PubInfo.getString(req.getParameter("schemename"));
        String remark = PubInfo.getString(req.getParameter("showinfo"));
        JSONReturnData data = new JSONReturnData("");
        int y = indexSchemeService.checkSchname(icode,cname);
        if (y == 0) {
            data.setReturncode(300);
            data.setReturndata("该名称已存在");
            this.sendJson(data);
            return;
        }else {
            data.setReturncode(200);
        }
        List<DataTableRow> rows = WeightEditDao.Fator.getInstance().getIndexdatadao().getModsbyIcode(icode).getRows();
        String state = "0";
        Scheme scheme = new Scheme();
        scheme.setCode(scode);
        scheme.setCname(cname);
        scheme.setIndexcode(icode);
        scheme.setState(state);
        scheme.setRemark(remark);
        indexSchemeService.addSch(scheme,rows);

        data.setReturndata(scheme);
        this.sendJson(data);
    }
    /**
     * 方案编辑
     * @author wf
     * @param
     * @return
     */
    public void editscheme() throws IOException {
        HttpServletRequest req = this.getRequest();
        String icode = PubInfo.getString(req.getParameter("schediticode"));
        String scode = PubInfo.getString(req.getParameter("scheditcode"));
        String cname = PubInfo.getString(req.getParameter("scheditname"));
        String remark = PubInfo.getString(req.getParameter("remark"));
        JSONReturnData data = new JSONReturnData("");
        int y = indexSchemeService.checkSchname(icode,cname);
        if (y == 0) {
            data.setReturncode(300);
            data.setReturndata("该名称已存在");
            this.sendJson(data);
            return;
        }else {
            data.setReturncode(200);
        }
        Scheme scheme = new Scheme();
        scheme.setCode(scode);
        scheme.setCname(cname);
        scheme.setRemark(remark);
        indexSchemeService.editSch(scheme);

        data.setReturndata(scheme);
        this.sendJson(data);
    }
    /**
     * 方案克隆
     * @author wf
     * @param
     * @return
     */
    public void clonescheme() throws IOException {
        HttpServletRequest req = this.getRequest();
        String icode = PubInfo.getString(req.getParameter("schcloneicode"));
        String scode = PubInfo.getString(req.getParameter("schclonecode"));
        String cname = PubInfo.getString(req.getParameter("schclonename"));
        String remark = PubInfo.getString(req.getParameter("newremark"));
        JSONReturnData data = new JSONReturnData("");
        int y = indexSchemeService.checkSchname(icode,cname);
        if (y == 0) {
            data.setReturncode(300);
            data.setReturndata("该名称已存在");
            this.sendJson(data);
            return;
        }else {
            data.setReturncode(200);
        }
        /*List<Scheme> schemes = indexSchemeService.getSchs(icode,scode);
        String ostate =  schemes.get(0).getState();*/
        String state="0";
        Scheme scheme = new Scheme();
        scheme.setCode(scode);
        scheme.setCname(cname);
        scheme.setIndexcode(icode);
        scheme.setRemark(remark);
        scheme.setState(state);
        indexSchemeService.cloneSch(scheme);

        data.setReturndata(scheme);
        this.sendJson(data);
    }
    /**
     * 方案删除
     * @author wf
     * @param
     * @return
     */
    public void schdelete() throws IOException {
        JSONReturnData data = new JSONReturnData("");
        String code = PubInfo.getString(this.getRequest().getParameter("id"));
        indexSchemeService.delScheme(code);
        this.sendJson(data);
    }
}
