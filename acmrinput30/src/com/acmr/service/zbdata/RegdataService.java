package com.acmr.service.zbdata;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import acmr.cubequery.service.CubeQuerySev;
import acmr.cubequery.service.cubequery.entity.*;

import acmr.util.PubInfo;


public class RegdataService {
    public static void main(String[]args){
        String dbcode = "cuscxnd";
        /*CubeWdCodes where = new CubeWdCodes();
        where.Add("zb","ffe001d3f4a67c752233a83f900af86a942359f2");
        where.Add("ds","A010100");
        where.Add("sj",
                Arrays.asList(new String[]{"2013","2014"}));
        where.Add("reg",
                Arrays.asList(
                        new String[]{"643"}
                ));
        where.Add("co","COG01");
        ArrayList<CubeQueryData> rs = querydata(dbcode,where);
        for (int i=0;i<rs.size();i++){
            PubInfo.printStr(rs.get(i).toString());
        }
        String dqcode = "GXQY000000";
       ArrayList<CubeNode> rs = getregsubnodes(dbcode,dqcode);
       for(int i=0;i<rs.size();i++) {
           PubInfo.printStr(rs.get(i).getCode());
       }*/
        ArrayList<CubeNode> rs = getregsubnodes(dbcode);

            PubInfo.printStr(rs.toString());
    }

    //获取维度
    public static ArrayList<CubeWeidu> getwdlist( String dbcode) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        ArrayList<CubeWeidu> wdlist = cube1.getWeiduList(dbcode);
        return wdlist;
    }
    //获取地区的名字
    public static CubeNode getregnode(String dbcode,String dqcode) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        CubeNode nodes = cube1.getWeiNode(dbcode, "reg", dqcode);
      return nodes;
    }
    //获取地区下的下一级节点
    public static ArrayList<CubeNode> getregsubnodes(String dbcode,String dqcode) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        ArrayList<CubeNode> nodes = cube1.getWeiSubNodes(dbcode, "reg", dqcode);
       return nodes;
    }

    //获取有数据的地区信息，给了指标信息
    public static List<String> gethasdatawdlist(String dbcode,List<CubeWdValue> list1) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        List<String> nodes = cube1.getHasDataWdListreg(dbcode, list1, "reg");
      return nodes;
    }
    //获取所有的东西,where传入zb、ds、co、reg、sj
    public static ArrayList<CubeQueryData> querydata(String dbcode,CubeWdCodes where) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        ArrayList<CubeQueryData> result = cube1.getCubeData(dbcode, where);
        return result;
    }
    //获取最顶层指标
    public static ArrayList<CubeNode> getregsubnodes(String dbcode) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        ArrayList<CubeNode> nodes = cube1.getWeiSubNodes(dbcode, "reg", "");
        return nodes;
    }

}
