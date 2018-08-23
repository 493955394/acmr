package com.acmr.service.zbdata;

import acmr.cubequery.service.CubeQuerySev;
import acmr.cubequery.service.cubequery.entity.*;
import acmr.util.PubInfo;

import java.util.*;

public class OriginService {

    /**
     * @Description: 获取维度列表,返回维度的wcode:CNAME的ArrayList
     * @Param: []
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/23
     */
    public  ArrayList<CubeWeidu> getwdlist() {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        ArrayList<CubeWeidu> wdlist = cube1.getWeiduList("cuscxnd");
        return wdlist;
    }
    /**
     * @Description: 获取指标子节点,根据wcode去查不同的表，返回code对应节点的子节点的的ArrayList
     * @Param: [wcode, code]
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/23
     */
    public  ArrayList<CubeNode> getwdsubnodes(String wcode,String code) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        ArrayList<CubeNode> nodes = cube1.getWeiSubNodes("cuscxnd", wcode, code);
        return nodes;
    }

    /**
     * @Description: 获取单个的点，同样通过wcode去查不同的表，返回该点
     * @Param: [wcode,code]
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/23
     */
    public  CubeNode getwdnode(String wcode,String code) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        CubeNode node = cube1.getWeiNode("cuscxnd", wcode, code);
        PubInfo.printStr(node.toString());
        return node;
    }
    /**
     * @Description: 返回有数的地区的编码（REGCODE，三位数）
     * @Param: [condition,wcode].wcode只能是sj或reg
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/23
     */

    public List<String> gethasdatawdlist(List<CubeWdValue> condition,String wcode) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        List<CubeWdValue> list1 = new ArrayList<CubeWdValue>();
        for(int i=0;i<condition.size();i++){
            list1.add(condition.get(i));
        }
        List<String> nodes=new ArrayList<String>();
        if(wcode=="sj"){
            nodes = cube1.getHasDataWdList("cuscxnd", list1, "sj");
        }else {
            nodes = cube1.getHasDataWdListreg("cuscxnd", list1, "reg");
        }
        return nodes;
    }
    /**
    * @Description:  根据指定的条件，查询底库中的值
    * @Param: [where] 传入指定的zb，ds，co，reg，sj
    * @return: java.util.ArrayList<acmr.cubequery.service.cubequery.entity.CubeQueryData>
    * @Author: lyh
    * @Date: 2018/8/23
    */
    public ArrayList<CubeQueryData> querydata(CubeWdCodes where){
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        ArrayList<CubeQueryData> result = cube1.getCubeData("cuscxnd", where);
        return result;
    }
    /**
    * @Description:  返回给定code对应的单位可以转换的单位列表
    * @Param: [code]
    * @return: java.util.List<acmr.cubequery.service.cubequery.entity.CubeUnit>
    * @Author: lyh
    * @Date: 2018/8/23
    */
    public List<CubeUnit> getUnitList(String code){
        List<CubeUnit> list1 = acmr.cubequery.service.CubeUnitManager.CubeUnitManagerFactor
                .getInstance("").getUnitZhuanhuanList(code);
        return list1;
    }
    /**
    * @Description: 返回从一个单位转换为另一个单位的rate
    * @Param: [funit, tunit, sj]
    * @return: double
    * @Author: lyh
    * @Date: 2018/8/23
    */
    public double getRate(String funit,String tunit,String sj){
        double rate = acmr.cubequery.service.CubeUnitManager.CubeUnitManagerFactor

                .getInstance("").getUnitRate(funit, tunit,sj);
        return rate;
    }

}
