package com.acmr.service.zhzs;

import acmr.cubequery.service.cubequery.entity.CubeUnit;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.IndexEditDao;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.service.zbdata.OriginService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexEditService {
/*
    public static void main(String[] args) {
        getSubMod("module1","R0010");
    }
*/

    /** 
    * @Description: 根据计划的code查询返回该计划下的筛选条件列表
    * @Param: [icode] 
    * @return: java.util.List<java.lang.String> 
    * @Author: lyh
    * @Date: 2018/8/30 
    */ 
    public  List<Map> getZBS(String icode){
        OriginService originService=new OriginService();
        List<Map> zbchoose=new ArrayList<>();
        List<DataTableRow> data = IndexEditDao.Fator.getInstance().getIndexdatadao().getZBSbyIndexCode(icode).getRows();
        PubInfo.printStr("======================data");
        for(int i=0;i<data.size();i++){
            PubInfo.printStr(data.get(i).getString("indexcode"));
            Map attr= new HashMap<String,String>();
            attr.put("code",data.get(i).getString("code"));
            attr.put("zbcode",data.get(i).getString("zbcode"));
            attr.put("dscode",data.get(i).getString("datasource"));
            attr.put("cocode",data.get(i).getString("company"));
            attr.put("unitcode",data.get(i).getString("unitcode"));
            attr.put("zbname",originService.getwdnode("zb",data.get(i).getString("zbcode")).getName());
            attr.put("dsname",originService.getwdnode("ds",data.get(i).getString("datasource")).getName());
            attr.put("coname",originService.getwdnode("co",data.get(i).getString("company")).getName());
            String unitname="";
            List<CubeUnit> units=originService.getUnitList(data.get(i).getString("unitcode"));
            String unitcode=data.get(i).getString("unitcode");
            for(int j=0;j<units.size();j++){
                String thiscode=units.get(j).getCode();
                if (thiscode.equals(unitcode)){
                    unitname=units.get(j).getName();
                }
            }
            attr.put("unitname",unitname);
            attr.put("regcode",data.get(i).getString("regions"));
            zbchoose.add(attr);
        }
        PubInfo.printStr(zbchoose.toString());

        return zbchoose;
    }

    /**
    * @Description: 根据给定模型节点的code和所属计划的icode返回submods
    * @Param: [code,icode]
    * @return: java.util.List<com.acmr.model.zhzs.IndexMoudle>
    * @Author: lyh
    * @Date: 2018/9/4
    */
    public  List<IndexMoudle> getSubMod(String code,String icode){
        List<IndexMoudle> submods=new ArrayList<>();
        List<DataTableRow> subs = IndexEditDao.Fator.getInstance().getIndexdatadao().getSubModsbyCode(code,icode).getRows();
        for (int i=0;i<subs.size();i++){
            PubInfo.printStr(subs.get(i).getRows().toString());
            IndexMoudle mod=new IndexMoudle();
            mod.setCname(subs.get(i).getString("cname"));
            mod.setCode(subs.get(i).getString("code"));
            mod.setDacimal(subs.get(i).getString("dacimal"));
            mod.setFormula(subs.get(i).getString("formula"));
            mod.setIfzb(subs.get(i).getString("ifzb"));
            mod.setIfzs(subs.get(i).getString("ifzs"));
            mod.setIndexcode(subs.get(i).getString("indexcode"));
            mod.setProcode(subs.get(i).getString("procode"));
            mod.setSortcode(subs.get(i).getString("sortcode"));
            mod.setWeight(subs.get(i).getString("weight"));
            submods.add(mod);
        }
        return submods;
    }
    public int deleteMod(String code){
        return IndexEditDao.Fator.getInstance().getIndexdatadao().deleteMod(code);
    }
    /**
     * 查找功能
     */
    //查找功能
    public ArrayList<IndexMoudle> found(int type,String code){
        ArrayList<IndexMoudle> indexMoudles=new ArrayList<IndexMoudle>();
        List<DataTableRow> data = new ArrayList<>();
        if(type==0){//0表示是通过code查的
            data = IndexEditDao.Fator.getInstance().getIndexdatadao().getLikeCode(code).getRows();

        }else if(type==1){//1表示是通过cname查的
            data = IndexEditDao.Fator.getInstance().getIndexdatadao().getLikeCname(code).getRows();
        }
        for(int i=0;i<data.size();i++){
            IndexMoudle index= new IndexMoudle();
            index.setCode(data.get(i).getString("code"));
            index.setCname(data.get(i).getString("cname"));
            index.setProcode(data.get(i).getString("procode"));
            index.setIndexcode(data.get(i).getString("indexcode"));
            index.setIfzs(data.get(i).getString("ifzs"));
            index.setDacimal(data.get(i).getString("dacimal"));
            index.setWeight( data.get(i).getString("weight"));
            index.setSortcode( data.get(i).getString("sortcode"));
            index.setIfzb(data.get(i).getString("ifzb"));
            index.setFormula(data.get(i).getString("formula"));
            indexMoudles.add(index);
        }
        return indexMoudles;
    }
    /**
     * 新增指数或者指标
     */
    public int addZStoModel(IndexMoudle indexMoudle){
        return IndexEditDao.Fator.getInstance().getIndexdatadao().addZS(indexMoudle);
    }
}
