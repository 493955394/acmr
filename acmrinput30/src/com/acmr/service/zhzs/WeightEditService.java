package com.acmr.service.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.IWeightEditDao;
import com.acmr.dao.zhzs.WeightEditDao;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.model.zhzs.TaskModule;
import com.sun.org.apache.regexp.internal.RE;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;

import java.util.ArrayList;
import java.util.List;

public class WeightEditService {

 /*   public static void main(String[] args) {
        getMods("R0010",0);
    }*/
    /**
    * @Description:  根据传来的tcode值去查询对应的模型节点列表
    * @Param: [code] tocode表示计划code
    * @return: void
    * @Author: lyh
    * @Date: 2018/9/5
    */
    public  List<IndexMoudle> getMods(String code){
        List<IndexMoudle> mods=new ArrayList<>();
        List<DataTableRow> rows = WeightEditDao.Fator.getInstance().getIndexdatadao().getModsbyIcode(code).getRows();
        for (int i=0;i<rows.size();i++){
            //PubInfo.printStr(rows.get(i).getRows().toString());
            IndexMoudle indexMoudle=new IndexMoudle();
            indexMoudle.setCname(rows.get(i).getString("cname"));
            indexMoudle.setCode(rows.get(i).getString("code"));
            indexMoudle.setDacimal(rows.get(i).getString("dacimal"));
            indexMoudle.setFormula(rows.get(i).getString("formula"));
            indexMoudle.setIfzb(rows.get(i).getString("ifzb"));
            indexMoudle.setIfzs(rows.get(i).getString("ifzs"));
            indexMoudle.setIndexcode(rows.get(i).getString("indexcode"));
            indexMoudle.setProcode(rows.get(i).getString("procode"));
            indexMoudle.setSortcode(rows.get(i).getString("sortcode"));
            indexMoudle.setWeight(rows.get(i).getString("weight"));
            mods.add(indexMoudle);
        }
        return mods;
    }

    /**
    * @Description: 设置模型节点权重值，code为该节点的code，weight为该节点要设置的权重的值
    * @Param: [code, weight]
    * @return: void
    * @Author: lyh
    * @Date: 2018/9/9
    */
    public void setWeight(String code,String weight){
        int i=WeightEditDao.Fator.getInstance().getIndexdatadao().weightset(code,weight);
    }

    /**
    * @Description: 返回该任务的所有模型节点（按顺序）
    * @Param: [taskcode]
    * @return: java.util.List<com.acmr.model.zhzs.TaskModule>
    * @Author: lyh
    * @Date: 2018/9/17
    */
    public List<TaskModule> getTMods(String taskcode){
        List<TaskModule> mods=new ArrayList<>();
        //先找根节点
        List<TaskModule> roots=new ArrayList<>();
        List<DataTableRow> rows= WeightEditDao.Fator.getInstance().getIndexdatadao().getTroos(taskcode).getRows();
        for (int i=0;i<rows.size();i++){
            String procode=rows.get(i).getString("procode");
            String code=rows.get(i).getString("code");
            String cname=rows.get(i).getString("cname");
            String sortcode=rows.get(i).getString("sortcode");
            String weight=rows.get(i).getString("weight");
            String ifzs=rows.get(i).getString("ifzs");
            TaskModule mod=new TaskModule(code,cname,taskcode,procode,sortcode,weight,ifzs);
            roots.add(mod);
        }
        mods.addAll(roots);
        for (int j=0;j<rows.size();j++){
            String code=rows.get(j).getString("code");
            List<TaskModule> allsubs=getAllSubTMods(code,taskcode);
            for (int m=0;m<allsubs.size();m++){
                mods.add(allsubs.get(m));
            }
        }

        return mods;
    }


    /**
    * @Description: 根据给定的模型节点code返回该节点下所有的模型节点，包括子节点的子节点
    * @Param: [code]
    * @return: java.util.List<com.acmr.model.zhzs.TaskModule>
    * @Author: lyh
    * @Date: 2018/9/17
    */
    public List<TaskModule> getAllSubTMods(String code,String taskcode){
        List<TaskModule> allsubs=new ArrayList<>();
        List<TaskModule> thissubs= getSubTMods(code,taskcode);
        allsubs.addAll(thissubs);
        for (int i=0;i<thissubs.size();i++){
            String thiscode=thissubs.get(i).getCode();
            if (getSubTMods(thiscode,taskcode).size()>0){
                allsubs.addAll(getAllSubTMods(thiscode,taskcode));
            }
        }
        return allsubs;
    }

    /** 
    * @Description:  根据给定模型节点的code返回submods
    * @Param: [code] 
    * @return: java.util.List<com.acmr.model.zhzs.TaskModule> 
    * @Author: lyh
    * @Date: 2018/9/17 
    */ 
    public List<TaskModule> getSubTMods(String code,String taskcode){
        List<TaskModule> subs=new ArrayList<>();
        List<DataTableRow> rows=WeightEditDao.Fator.getInstance().getIndexdatadao().getSubTmods(code).getRows();
        for (int i=0;i<rows.size();i++){
            String cname=rows.get(i).getString("cname");
            String mcode=rows.get(i).getString("code");
            String procode=rows.get(i).getString("procode");
            String sortcode=rows.get(i).getString("sortcode");
            String weight=rows.get(i).getString("weight");
            String ifzs=rows.get(i).getString("ifzs");
            TaskModule mod=new TaskModule(mcode,cname,taskcode,procode,sortcode,weight,ifzs);
            subs.add(mod);
        }
        return subs;
    }

    public List<TaskModule> getOrTMods(String taskcode){
        //正式表覆盖临时表
        WeightEditDao.Fator.getInstance().getIndexdatadao().ReWeight(taskcode);
        //再次从临时表中读书
        List<TaskModule> mods=getTMods(taskcode);
        return mods;
    }

    public int tWeightUpadte(String modcode,String weight){return WeightEditDao.Fator.getInstance().getIndexdatadao().tWeightUpd(modcode,weight);}
}
