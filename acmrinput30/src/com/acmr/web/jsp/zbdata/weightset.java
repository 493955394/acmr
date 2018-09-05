package com.acmr.web.jsp.zbdata;

import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.dao.zhzs.WeightEditDao;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.service.zhzs.WeightEditService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class weightset extends BaseAction {
    public ModelAndView main() {
        return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/weightset");
    }
    /**
    * @Description: 根据传来的indexcode取模型节点信息，并跳转页面
    * @Param: []
    * @return: void
    * @Author: lyh
    * @Date: 2018/9/5
    */
    public ModelAndView editweight(){
        HttpServletRequest req=this.getRequest();
        String icode=req.getParameter("icode");
        WeightEditService weightEditService=new WeightEditService();
        List<IndexMoudle> mods=weightEditService.getMods(icode,0);
        return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/weightset").addObject("indexcode",icode).addObject("mods",mods);
    }
}
