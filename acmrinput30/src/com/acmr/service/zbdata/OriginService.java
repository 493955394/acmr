package com.acmr.service.zbdata;

import acmr.cubequery.service.CubeQuerySev;
import acmr.cubequery.service.cubequery.CubeSjUtil;
import acmr.cubequery.service.cubequery.entity.*;


import java.util.*;

public class OriginService {

    /**
     * @Description: 获取维度列表,返回维度的wcode:CNAME的ArrayList
     * @Param: [dbcode]
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/23
     */
    public  ArrayList<CubeWeidu> getwdlist(String dbcode) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        ArrayList<CubeWeidu> wdlist = cube1.getWeiduList(dbcode);
        return wdlist;
    }
    /**
     * @Description: 获取指标子节点,根据wcode去查不同的表，返回code对应节点的子节点的的ArrayList
     * @Param: [wcode, code,dbcode]
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/23
     */
    public  ArrayList<CubeNode> getwdsubnodes(String wcode,String code,String dbcode) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        ArrayList<CubeNode> nodes = cube1.getWeiSubNodes(dbcode, wcode, code);
        return nodes;
    }

    /**
     * @Description: 获取单个的点，同样通过wcode去查不同的表，返回该点
     * @Param: [wcode,code,dbcode]
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/23
     */
    public  CubeNode getwdnode(String wcode,String code,String dbcode) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        CubeNode node = cube1.getWeiNode(dbcode, wcode, code);
        return node;
    }
    /**
     * @Description: 返回有数的地区的编码（REGCODE，三位数）、时间、数据来源、主体
     * @Param: [condition,wcode,dbcode].wcode只能是sj或reg,ds,co，condition例子：where.Add("zb", "ffe001d3f4a67c752233a83f900af86a942359f2");
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/23
     */
    public List<String> gethasdatawdlist(List<CubeWdValue> condition,String wcode,String dbcode) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        List<CubeWdValue> list1 = new ArrayList<CubeWdValue>();
        for(int i=0;i<condition.size();i++){
            list1.add(condition.get(i));
        }
        List<String> nodes=new ArrayList<String>();
        List<CubeWdDataSize> tmp= cube1.getHasDataWdListreg(dbcode,list1,wcode);
        for (int i = 0; i <tmp.size() ; i++) {
            nodes.add(tmp.get(i).getCode());
        }
        return nodes;
    }

    /**
    * @Description:  根据指定的条件，查询底库中的值
    * @Param: [where,dbcode] 传入指定的zb，ds，co，reg，sj
    * @return: java.util.ArrayList<acmr.cubequery.service.cubequery.entity.CubeQueryData>
    * @Author: lyh
    * @Date: 2018/8/23
    */
    public ArrayList<CubeQueryData> querydata(CubeWdCodes where,String dbcode){
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        ArrayList<CubeQueryData> result = cube1.getCubeData(dbcode, where);
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

    /**
     * 计算转换格式
     */
    public String getChangeTime (String code,String nowtime){
        String time = code;
        CubeSjUtil cube1 = new CubeSjUtil();
        if(code.contains("lastnum")){
            String i = "-"+time.replace("lastnum","").trim();
            time = cube1.getCodeAdd(nowtime,Integer.parseInt(i));
        }
        else if(code.contains("last")){
            String i = "-"+time.replace("last","").trim();
            time = cube1.getCodeAdd(nowtime,Integer.parseInt(i))+"-"+cube1.getCodeAdd(nowtime,-1);
        }
        return time;
    }
}
