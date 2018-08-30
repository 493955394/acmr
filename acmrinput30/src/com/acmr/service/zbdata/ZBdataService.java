package com.acmr.service.zbdata;

import acmr.cubequery.service.cubequery.entity.CubeNode;
import acmr.cubequery.service.cubequery.entity.CubeWdValue;
import acmr.util.PubInfo;

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
    /**
     * @Description: 根据指定的节点，返回该节点下的所有叶子节点,用于查数，不是底层的点查不到数
     * @Param: [node,wdcode]wdcode指该节点的类型code，传"zb","co","ds"
     * @return: java.util.ArrayList<acmr.cubequery.service.cubequery.entity.CubeNode>
     * @Author: lyh
     * @Date: 2018/8/24
     */
    public  ArrayList<CubeNode> getBottomChilds(CubeNode node,String wdcode){
        ArrayList<CubeNode> leafs=new ArrayList<CubeNode>();
        hasSubNodes(leafs,node,wdcode);
        return leafs;
    }
    public void hasSubNodes(ArrayList<CubeNode> leafs,CubeNode node,String wdcode){
        ArrayList<CubeNode> subNodes=origin.getwdsubnodes(wdcode,node.getCode());
        if(subNodes.size()==0){
            leafs.add(node);
        }
        else {
            for(int i=0;i<subNodes.size();i++){
                hasSubNodes(leafs,subNodes.get(i),wdcode);
            }
        }

    }

    /**
    * @Description: 返回有数的ds和co,sj,reg
    * @Param: [zbcode,wcode] wcode只能是"co"或者"ds","sj","reg"，zbcode必须是最底层的指标的code，不能是指标分类的code
    * @return: java.util.List<acmr.cubequery.service.cubequery.entity.CubeNode>
    * @Author: lyh
    * @Date: 2018/8/24
    */
    public  List<String> getHasDataNodeO(String zbcode, String wcode){
        List<CubeWdValue> list=new ArrayList<>();
        list.add(new CubeWdValue("zb",zbcode));
        List<String> nodes=origin.gethasdatawdlist(list,wcode);
        return nodes;
    }



}
