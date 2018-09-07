package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.service.zhzs.IndexEditService;
import com.acmr.service.zhzs.WeightEditService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
        List<IndexMoudle> re=weightEditService.getMods(icode,0);
        List<IndexMoudle> mods=new ArrayList<>();
        for (int j=0;j<re.size();j++){
            if (re.get(j).getProcode()==""){
                PubInfo.printStr("find1");
                mods.add(re.get(j));
                List<IndexMoudle> allsub=indexEditService.getAllMods(re.get(j).getCode(),icode);
                mods.addAll(allsub);
            }
        }
        PubInfo.printStr("==================addobj");
        for (int i=0;i<mods.size();i++){
            PubInfo.printStr(mods.get(i).getCode()+mods.get(i).getCname());
        }
        return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/weightset").addObject("indexcode",icode).addObject("mods",mods);
    }
}
