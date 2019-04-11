package com.acmr.service.zbdata;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import acmr.cubequery.service.CubeQuerySev;
import acmr.cubequery.service.cubequery.entity.*;
import acmr.cubequery.service.cubequery.CubeSjUtil;
import acmr.util.PubInfo;


public class RegdataService {

    //获取维度
    public static ArrayList<CubeWeidu> getWDList( String dbcode) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        ArrayList<CubeWeidu> wdlist = cube1.getWeiduList(dbcode);
        return wdlist;
    }
    //获取地区的名字
    public CubeNode getRegNode(String dbcode,String dqcode) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        CubeNode nodes = cube1.getWeiNode(dbcode, "reg", dqcode);
      return nodes;
    }
    //获取地区下的下一级节点
    public static ArrayList<CubeNode> getRegSubNodes(String dbcode,String dqcode) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        ArrayList<CubeNode> nodes = cube1.getWeiSubNodes(dbcode, "reg", dqcode);
       return nodes;
    }

    //获取有数据的地区信息，给了指标信息
   /* public static List<String> getHasDataWDList(String dbcode,List<CubeWdValue> list1) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
       // List<String> nodes = cube1.getHasDataWdListreg(dbcode, list1, "reg");

      return nodes;
    }*/
    //获取所有的东西,where传入zb、ds、co、reg、sj
    public static ArrayList<CubeQueryData> queryData(String dbcode,CubeWdCodes where) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        ArrayList<CubeQueryData> result = cube1.getCubeData(dbcode, where);
        return result;
    }
    //获取最顶层指标
    public static ArrayList<CubeNode> getRegSubNodes(String dbcode) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        ArrayList<CubeNode> nodes = cube1.getWeiSubNodes(dbcode, "reg", "");
        return nodes;
    }
    //获取地区的树
    public List<CubeNode> findReg(String s,String dbcode){
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        List<CubeNode> nodes = cube1.FindWeiNode(dbcode,"reg",s);
        return nodes;
    }
    //返回地区树的path
    public List<String> getRegPath(String code,String dbcode){
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        String _path=cube1.getWeiTreePath(dbcode,"reg",code);
        List<String> path= Arrays.asList(_path.split("/"));
        return path;
    }

}
