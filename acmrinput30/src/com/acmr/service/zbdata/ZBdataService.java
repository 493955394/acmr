package com.acmr.service.zbdata;

import acmr.cubequery.service.cubequery.entity.CubeNode;
import acmr.cubequery.service.cubequery.entity.CubeWdValue;
import java.util.ArrayList;
import java.util.List;

public class ZBdataService {
    OriginService origin=new OriginService();
    /**
    * @Description: 获取最顶层指标
    * @Param: []
    * @return: java.util.ArrayList<acmr.cubequery.service.cubequery.entity.CubeNode>
    * @Author: lyh
    * @Date: 2018/8/23
    */
    public ArrayList<CubeNode> getZB(){
        ArrayList<CubeNode> zb=origin.getwdsubnodes("zb","");
        return zb;
    }
    /** 
    * @Description: 获取指定code的指标的下层指标
    * @Param: [code] 
    * @return: java.util.ArrayList<acmr.cubequery.service.cubequery.entity.CubeNode> 
    * @Author: lyh
    * @Date: 2018/8/23 
    */ 
    public ArrayList<CubeNode> getSubZB(String code){
        ArrayList<CubeNode> subs=origin.getwdsubnodes("zb",code);
        return subs;
    }
    /**
    * @Description: 获取最顶层的数据来源
    * @Param: []
    * @return: java.util.ArrayList<acmr.cubequery.service.cubequery.entity.CubeNode>
    * @Author: lyh
    * @Date: 2018/8/23
    */
    public ArrayList<CubeNode> getDS(){
        ArrayList<CubeNode> ds=origin.getwdsubnodes("ds","");
        return ds;
    }
    /**
    * @Description: 获取code的指定数据来源的下层数据来源
    * @Param: [code]
    * @return: java.util.ArrayList<acmr.cubequery.service.cubequery.entity.CubeNode>
    * @Author: lyh
    * @Date: 2018/8/23
    */
    public ArrayList<CubeNode> getSubDS(String code){
        ArrayList<CubeNode> subs=origin.getwdsubnodes("ds",code);
        return subs;
    }
    /**
     * @Description: 获取最顶层的主体
     * @Param: []
     * @return: java.util.ArrayList<acmr.cubequery.service.cubequery.entity.CubeNode>
     * @Author: lyh
     * @Date: 2018/8/23
     */
    public ArrayList<CubeNode> getCO(){
        ArrayList<CubeNode> co=origin.getwdsubnodes("co","");
        return co;
    }
    /**
     * @Description: 获取code的指定主体的下层主体
     * @Param: [code]
     * @return: java.util.ArrayList<acmr.cubequery.service.cubequery.entity.CubeNode>
     * @Author: lyh
     * @Date: 2018/8/23
     */
    public ArrayList<CubeNode> getSubCO(String code){
        ArrayList<CubeNode> subs=origin.getwdsubnodes("co",code);
        return subs;
    }
    public void getHasDataDS(String zbcode){
        List<CubeWdValue> where=new ArrayList<>();
        where.add(new CubeWdValue("zb",zbcode));

    }

}
