package com.acmr.service.zhzs;

import acmr.cubequery.service.cubequery.entity.CubeUnit;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.IndexEditDao;
import com.acmr.service.zbdata.OriginService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexEditService {

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
/*
    public static void main(String[] args) {
        getZBS("R0010");
    }*/
}
