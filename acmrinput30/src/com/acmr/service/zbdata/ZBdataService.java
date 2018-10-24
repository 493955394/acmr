package com.acmr.service.zbdata;

import acmr.cubequery.service.CubeQuerySev;
import acmr.cubequery.service.cubequery.entity.CubeNode;
import acmr.cubequery.service.cubequery.entity.CubeWdValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZBdataService {
    OriginService origin=new OriginService();
    /**
    * @Description: 获取最顶层指标
    * @Param: [dbcode]
    * @return: java.util.ArrayList<acmr.cubequery.service.cubequery.entity.CubeNode>
    * @Author: lyh
    * @Date: 2018/8/23
    */
    public ArrayList<CubeNode> getZB(String dbcode){
        ArrayList<CubeNode> zb=origin.getwdsubnodes("zb","",dbcode);
        return zb;
    }
    /** 
    * @Description: 获取指定code的指标的下层指标
    * @Param: [code,dbcode]
    * @return: java.util.ArrayList<acmr.cubequery.service.cubequery.entity.CubeNode> 
    * @Author: lyh
    * @Date: 2018/8/23 
    */ 
    public ArrayList<CubeNode> getSubZB(String code,String dbcode){
        ArrayList<CubeNode> subs=origin.getwdsubnodes("zb",code,dbcode);
        return subs;
    }
    /**
    * @Description: 获取最顶层的数据来源
    * @Param: [dbcode]
    * @return: java.util.ArrayList<acmr.cubequery.service.cubequery.entity.CubeNode>
    * @Author: lyh
    * @Date: 2018/8/23
    */
    public ArrayList<CubeNode> getDS(String dbcode){
        ArrayList<CubeNode> ds=origin.getwdsubnodes("ds","",dbcode);
        return ds;
    }
    /**
    * @Description: 获取code的指定数据来源的下层数据来源
    * @Param: [code,dbcode]
    * @return: java.util.ArrayList<acmr.cubequery.service.cubequery.entity.CubeNode>
    * @Author: lyh
    * @Date: 2018/8/23
    */
    public ArrayList<CubeNode> getSubDS(String code,String dbcode){
        ArrayList<CubeNode> subs=origin.getwdsubnodes("ds",code,dbcode);
        return subs;
    }
    /**
     * @Description: 获取最顶层的主体
     * @Param: [dbcode]
     * @return: java.util.ArrayList<acmr.cubequery.service.cubequery.entity.CubeNode>
     * @Author: lyh
     * @Date: 2018/8/23
     */
    public ArrayList<CubeNode> getCO(String dbcode){
        ArrayList<CubeNode> co=origin.getwdsubnodes("co","",dbcode);
        return co;
    }
    /**
     * @Description: 获取code的指定主体的下层主体
     * @Param: [code,dbcode]
     * @return: java.util.ArrayList<acmr.cubequery.service.cubequery.entity.CubeNode>
     * @Author: lyh
     * @Date: 2018/8/23
     */
    public ArrayList<CubeNode> getSubCO(String code,String dbcode){
        ArrayList<CubeNode> subs=origin.getwdsubnodes("co",code,dbcode);
        return subs;
    }
    /**
     * @Description: 根据指定的节点，返回该节点下的所有叶子节点,用于查数，不是底层的点查不到数
     * @Param: [node,wdcode,dbcode]wdcode指该节点的类型code，传"zb","co","ds"
     * @return: java.util.ArrayList<acmr.cubequery.service.cubequery.entity.CubeNode>
     * @Author: lyh
     * @Date: 2018/8/24
     */
    public  ArrayList<CubeNode> getBottomChilds(CubeNode node,String wdcode,String dbcode){
        ArrayList<CubeNode> leafs=new ArrayList<CubeNode>();
        hasSubNodes(leafs,node,wdcode,dbcode);
        return leafs;
    }
    public void hasSubNodes(ArrayList<CubeNode> leafs,CubeNode node,String wdcode,String dbcode){
        ArrayList<CubeNode> subNodes=origin.getwdsubnodes(wdcode,node.getCode(),dbcode);
        if(subNodes.size()==0){
            leafs.add(node);
        }
        else {
            for(int i=0;i<subNodes.size();i++){
                hasSubNodes(leafs,subNodes.get(i),wdcode,dbcode);
            }
        }

    }

    /**
    * @Description: 返回有数的ds和co,sj,reg
    * @Param: [zbcode,wcode,dbcode] wcode只能是"co"或者"ds","sj","reg"，zbcode必须是最底层的指标的code，不能是指标分类的code
    * @return: java.util.List<acmr.cubequery.service.cubequery.entity.CubeNode>
    * @Author: lyh
    * @Date: 2018/8/24
    */
    public  List<String> getHasDataNodeO(String zbcode, String wcode,String dbcode){
        List<CubeWdValue> list=new ArrayList<>();
        list.add(new CubeWdValue("zb",zbcode));
        List<String> nodes=origin.gethasdatawdlist(list,wcode,dbcode);
        return nodes;
    }

    /**
    * @Description: 根据传入的s模糊查找指标
    * @Param: [s]
    * @return: java.util.List<acmr.cubequery.service.cubequery.entity.CubeNode>
    * @Author: lyh
    * @Date: 2018/9/2
    */
    public List<CubeNode> findZB(String s,String dbcode){
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        List<CubeNode> nodes = cube1.FindWeiNode(dbcode,"zb",s);
        return nodes;
    }
    /**
    * @Description: 根据给定的指标code，返回该指标的path
    * @Param: [code]
    * @return: java.util.List<java.lang.String>
    * @Author: lyh
    * @Date: 2018/9/2
    */
    public List<String> getZBPath(String code,String dbcode){
        CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
        String _path=cube1.getWeiTreePath(dbcode,"zb",code);
        List<String> path= Arrays.asList(_path.split("/"));
        return path;
    }
}
