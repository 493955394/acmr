package com.acmr.service.zbdata;
import java.util.ArrayList;
import java.util.List;

import acmr.cubequery.service.CubeQuerySev;
import acmr.cubequery.service.cubequery.entity.CubeNode;
import acmr.cubequery.service.cubequery.entity.CubeWeidu;
import acmr.util.PubInfo;


public class RegdataService {
    //获取维度
    public static ArrayList<CubeWeidu> getwdlist( String dbcode) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        ArrayList<CubeWeidu> wdlist = cube1.getWeiduList(dbcode);

        return wdlist;
    }
    //获取地区的名字
    public static CubeNode getregnode(String dbcode,String  dqcode) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        CubeNode nodes = cube1.getWeiNode(dbcode, "reg", dqcode);
      return nodes;
      //  PubInfo.printStr(nodes.toString());
    }
    //获取地区下的子节点
    public static ArrayList<CubeNode> getregsubnodes(String dbcode,String dqcode) {
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        ArrayList<CubeNode> nodes = cube1.getWeiSubNodes(dbcode, "reg", dqcode);
       return nodes;
    }

}
