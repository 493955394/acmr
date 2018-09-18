package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.dao.zhzs.WeightEditDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.service.zhzs.IndexEditService;
import com.acmr.service.zhzs.WeightEditService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class weightset extends BaseAction {

   /* public static void main(String[] args) {
        editweight();
    }*/
    /*public ModelAndView main() {
        return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/weightset");
    }*/
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
        IndexEditService indexEditService=new IndexEditService();
        List<IndexMoudle> re=weightEditService.getMods(icode);
        List<IndexMoudle> mods=new ArrayList<>();
        for (int j=0;j<re.size();j++){
            if (re.get(j).getProcode()==""){
                //PubInfo.printStr("find1");
                mods.add(re.get(j));
                List<IndexMoudle> allsub=indexEditService.getAllMods(re.get(j).getCode(),icode);
                mods.addAll(allsub);
            }
        }
        //PubInfo.printStr("==================addobj");
        /*for (int i=0;i<mods.size();i++){
            PubInfo.printStr(mods.get(i).getCode()+mods.get(i).getCname());
        }*/
        String pjax = req.getHeader("X-PJAX");
        if (StringUtil.isEmpty(pjax)) {
            PubInfo.printStr("====================================================empty");
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/weightset").addObject("indexcode",icode).addObject("mods",mods);
        } else {
            PubInfo.printStr("====================================================pjax");
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/weighttable").addObject("indexcode",icode).addObject("mods",mods);
        }
    }

    /**
    * @Description: 保存权重设置页面修改的所有weights
    * @Param: []
    * @return: void
    * @Author: lyh
    * @Date: 2018/9/9
    */
    public void setWeights(){
        HttpServletRequest req=this.getRequest();
        String cws=req.getParameter("cws");
        PubInfo.printStr(cws);
        List<String> cw= Arrays.asList(cws.split(","));
        for (int i=0;i<cw.size();i++){
            String code=cw.get(i).split(":")[0];
            String weight=cw.get(i).split(":")[1];
            WeightEditService weightEditService=new WeightEditService();
            weightEditService.setWeight(code,weight);
        }

    }

/*    public ModelAndView getWeightTable(){
        HttpServletRequest req = this.getRequest();
        String pjax = req.getHeader("X-PJAX");
        if (StringUtil.isEmpty(pjax)) {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/weightset");
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/weighttable");
        }
    }*/
}
