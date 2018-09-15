package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.dao.zhzs.IndexTaskDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.service.zbdata.OriginService;
import com.acmr.service.zhzs.IndexTaskService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class zscalculate extends BaseAction {
    /**
    * @Description: 从data表中读数时返回页面
    * @Param: []
    * @return: acmr.web.entity.ModelAndView
    * @Author: lyh
    * @Date: 2018/9/13
    */
    public ModelAndView ZsCalculate(){
        //String test="重新计算";
        HttpServletRequest req=this.getRequest();
        IndexTaskService indexTaskService=new IndexTaskService();
        OriginService originService=new OriginService();
        String taskcode=req.getParameter("taskcode");
        String ayearmon=indexTaskService.getTime(taskcode);
        List<List<String>> data=getOriginData(false,taskcode,ayearmon);
        List<String> regscode=indexTaskService.getTaskRegs(taskcode);
        List<String> regs=new ArrayList<>();
        for (int i=0;i<regscode.size();i++){
            regs.add(originService.getwdnode("reg",regscode.get(i)).getName());
        }
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/zscalculate").addObject("data",data).addObject("regs",regs).addObject("taskcode",taskcode);
    }

    /**
    * @Description: 从tmp表中读数时返回页面
    * @Param: []
    * @return: acmr.web.entity.ModelAndView
    * @Author: lyh
    * @Date: 2018/9/13
    */
    public ModelAndView ReCalculate(){
        HttpServletRequest req=this.getRequest();
        String sessionid=req.getSession().getId();
        IndexTaskService indexTaskService=new IndexTaskService();
        OriginService originService=new OriginService();
        String taskcode=req.getParameter("taskcode");
        //取对应id的数据
        String ayearmon=indexTaskService.getTime(taskcode);
        List<List<String>> data=getOriginData(true,taskcode,ayearmon);
        List<String> regscode=indexTaskService.getTaskRegs(taskcode);
        List<String> regs=new ArrayList<>();
        for (int i=0;i<regscode.size();i++){
            regs.add(originService.getwdnode("reg",regscode.get(i)).getName());
        }
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/zscalculate").addObject("data",data).addObject("regs",regs).addObject("taskcode",taskcode);

    }

    public void ReData(){
        HttpServletRequest req = this.getRequest();
        IndexTaskService indexTaskService=new IndexTaskService();
        String taskcode=req.getParameter("taskcode");
        String ayearmon=indexTaskService.getTime(taskcode);
        IndexTaskDao.Fator.getInstance().getIndexdatadao().ReData(taskcode);
      /*  List<List<String>> data=getOriginData(true,taskcode,ayearmon);
        String pjax = req.getHeader("X-PJAX");
        if (StringUtil.isEmpty(pjax)) {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/dataTable").addObject("data",data);
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/zscalculate");
        }*/
    }


    /**
    * @Description: istmp为true，表示从临时表中读数，false从data表中读数，返回rows，用于绘制原始数据表
    * @Param: [istmp,taskcode]
    * @return: java.util.List<java.util.List<java.lang.String>>
    * @Author: lyh
    * @Date: 2018/9/13
    */
    public List<List<String>> getOriginData(Boolean istmp,String taskcode,String ayearmon){
        List<List<String>> rows=new ArrayList<>();
        IndexTaskService indexTaskService=new IndexTaskService();
        List<String> ZBcodes=indexTaskService.getZBcodes(taskcode);
        List<String> regs=indexTaskService.getTaskRegs(taskcode);
        //从临时表中取数
        if (istmp){

        }
        //从原始表中取数
        else {
            for (int i=0;i<ZBcodes.size();i++){
                List<String> row=new ArrayList<>();
                String ZBcode=ZBcodes.get(i);
                row.add(indexTaskService.getzbname(ZBcode));
                for (int j=0;j<regs.size();j++){
                    String data=indexTaskService.getData(taskcode,regs.get(j),ZBcodes.get(i),ayearmon);
                    row.add(data);
                }
                //PubInfo.printStr("row:"+row.toString());
                rows.add(row);
            }
            for (int m=0;m<rows.size();m++){
                PubInfo.printStr(rows.get(m).toString());
            }
        }
        return rows;
    }

}
