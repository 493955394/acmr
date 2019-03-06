package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.dao.zhzs.WeightEditDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.model.zhzs.Scheme;
import com.acmr.service.zhzs.IndexEditService;
import com.acmr.service.zhzs.IndexSchemeService;
import com.acmr.service.zhzs.WeightEditService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class weightset extends BaseAction {

    /**
    * @Description: 根据传来的indexcode取模型节点信息，并跳转页面,单个方案权重设置
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
                List<IndexMoudle> allsub=indexEditService.getAllMods(re.get(j).getCode(),icode,scode);
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
    * @Description: 根据计划code返回该计划的多个方案权重设置页面
    * @Param: []
    * @return: acmr.web.entity.ModelAndView
    * @Author: lyh
    * @Date: 2019/3/4
    */
    public ModelAndView editSchemesWeight(){
        HttpServletRequest req=this.getRequest();
        String icode=req.getParameter("icode");
        IndexSchemeService indexSchemeService=new IndexSchemeService();
        WeightEditService weightEditService=new WeightEditService();
        IndexEditService indexEditService=new IndexEditService();
        List<Scheme> schemes=indexSchemeService.getSchemeByIcode(icode);
        List<String> scodes=new ArrayList<>();
        List<String> snames=new ArrayList<>();
        for (Scheme scheme:schemes){
            scodes.add(scheme.getCode());
            snames.add(scheme.getCname());
        }
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

        return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/weightschemesset").addObject("indexcode",icode).addObject("scodes",scodes).addObject("mods",mods).addObject("snames",snames);
    }

    /**
    * @Description: 保存权重设置页面修改的所有weights到方案的对应权重中,单个方案
    * @Param: []
    * @return: void
    * @Author: lyh
    * @Date: 2018/9/9
    */
    public void setWeights(){
        HttpServletRequest req=this.getRequest();
        String cws=req.getParameter("cws");
        String scode=req.getParameter("scode");
        List<String> cw= Arrays.asList(cws.split(","));
        IndexSchemeService indexSchemeService=new IndexSchemeService();
        Map<String,String> weightmap=new HashMap<>();
        for (String s:cw){
            String[] array=s.split(":");
            weightmap.put(array[0],array[1]);
        }
        indexSchemeService.setSingleSchemeWeight(scode,weightmap);
        /*for (int i=0;i<cw.size();i++){
            String code=cw.get(i).split(":")[0];
            String weight=cw.get(i).split(":")[1];
            WeightEditService weightEditService=new WeightEditService();
            weightEditService.setWeight(code,weight);
        }
*/
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
