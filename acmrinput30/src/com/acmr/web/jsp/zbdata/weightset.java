package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.dao.zhzs.WeightEditDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.service.zhzs.IndexEditService;
import com.acmr.service.zhzs.IndexSchemeService;
import com.acmr.service.zhzs.WeightEditService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class weightset extends BaseAction {

    /**
    * @Description: 根据传来的indexcode取模型节点信息，并跳转页面
    * @Param: []
    * @return: void
    * @Author: lyh
    * @Date: 2018/9/5
    */
    public ModelAndView editSingleWeight(){
        HttpServletRequest req=this.getRequest();
        String icode=req.getParameter("icode");
        String scode=req.getParameter("scode");
        String sname=req.getParameter("sname");
        WeightEditService weightEditService=new WeightEditService();
        IndexEditService indexEditService=new IndexEditService();
        IndexSchemeService indexSchemeService=new IndexSchemeService();
        List<IndexMoudle> re=weightEditService.getMods(icode);
        List<IndexMoudle> mods=new ArrayList<>();
        for (int j=0;j<re.size();j++){
            if (re.get(j).getProcode()==""){
                //PubInfo.printStr("find1");
                mods.add(re.get(j));
                List<IndexMoudle> allsub=indexEditService.getAllMods(re.get(j).getCode(),icode);
                //要显示注解，所以要替换公式
                for (int i = 0; i <allsub.size() ; i++) {
                    if(allsub.get(i).getIfzs().equals("0")){//如果是指数的话
                        if(allsub.get(i).getIfzb().equals("0")){//自定义公式
                            allsub.get(i).setFormula(new zsjhedit().changeFormula(allsub.get(i).getFormula(),icode,"CTN"));
                        }
                        else {//直接选的指标当公式
                            allsub.get(i).setFormula(new zsjhedit().formulaShow(allsub.get(i).getFormula(),icode));
                        }
                    }
                }
                mods.addAll(allsub);
            }
        }
        //按照方案id重置weight
        for (IndexMoudle indexMoudle:mods){
            String newweight=indexSchemeService.getModSchemeWeight(scode,indexMoudle.getCode());
            indexMoudle.setWeight(newweight);
        }
        String pjax = req.getHeader("X-PJAX");
        if (StringUtil.isEmpty(pjax)) {
            PubInfo.printStr("====================================================empty");
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/weightset").addObject("indexcode",icode).addObject("mods",mods).addObject("schemecode",scode).addObject("schemename",sname);
        } else {
            PubInfo.printStr("====================================================pjax");
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/weighttable").addObject("indexcode",icode).addObject("mods",mods).addObject("schemecode",scode).addObject("schemename",sname);
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
