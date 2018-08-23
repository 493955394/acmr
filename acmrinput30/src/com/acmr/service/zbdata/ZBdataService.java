package com.acmr.service.zbdata;

import acmr.cubequery.service.cubequery.entity.CubeNode;

import java.util.ArrayList;

public class ZBdataService {
    OriginService origin=new OriginService();
    public ArrayList<CubeNode> getZB(){
        ArrayList<CubeNode> zb=origin.getwdsubnodes("zb","");
        return zb;
    }
}
