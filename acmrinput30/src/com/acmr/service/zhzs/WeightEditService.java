package com.acmr.service.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.WeightEditDao;
import com.acmr.model.zhzs.IndexMoudle;

import java.util.ArrayList;
import java.util.List;

public class WeightEditService {

 /*   public static void main(String[] args) {
        getMods("R0010",0);
    }*/
    /**
    * @Description:  根据传来的tcode和table的值去查询对应的模型节点列表，table:0表示index表，1表示任务表，2表示任务临时表,后两个等到时候再做
    * @Param: [tcode, table] tocode表示计划code/任务code
    * @return: void
    * @Author: lyh
    * @Date: 2018/9/5
    */
    public  List<IndexMoudle> getMods(String tcode, int table){
        List<IndexMoudle> mods=new ArrayList<>();
        if (table==0){
            List<DataTableRow> rows = WeightEditDao.Fator.getInstance().getIndexdatadao().getModsbyIcode(tcode).getRows();
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
}
