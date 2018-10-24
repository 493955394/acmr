package com.acmr.service.zhzs;

import acmr.cubequery.service.cubequery.entity.CubeUnit;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.IndexEditDao;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.model.zhzs.IndexList;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.model.zhzs.IndexZb;
import com.acmr.service.zbdata.OriginService;
import com.acmr.web.jsp.Index;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalSimpleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexEditService {
/*    public static void main(String[] args) {
        List<String> codes=new ArrayList<>();
        codes.add("m5");
        codes.add("m2");
        codes.add("m3");
        codes.add("m4");
        codes.add("m1");
        resort(codes);
    }*/

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
        String dbcode= IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
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
            attr.put("zbname",originService.getwdnode("zb",data.get(i).getString("zbcode"),dbcode).getName());
            attr.put("dsname",originService.getwdnode("ds",data.get(i).getString("datasource"),dbcode).getName());
            attr.put("coname",originService.getwdnode("co",data.get(i).getString("company"),dbcode).getName());
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

    /**
     * 只返回code的列表
     */
    public List<String> getModCodeList(String icode){
        List<String> datas = new ArrayList<>();
        List<DataTableRow> data = IndexEditDao.Fator.getInstance().getIndexdatadao().getZBSbyIndexCode(icode).getRows();
        for (int i = 0; i <data.size() ; i++) {
            datas.add(data.get(i).getString("code"));
        }
        return datas;
    }

    /**
     * @Description: 根据给定的模型节点code和计划code返回该节点下所有的模型节点，包括子节点的子节点
     * @Param: [code, icode]
     * @return: java.util.List<com.acmr.model.zhzs.IndexMoudle>
     * @Author: lyh
     * @Date: 2018/9/6
     */
    public List<IndexMoudle> getAllMods(String code,String icode){
        List<IndexMoudle> allmods=new ArrayList<>();
        List<IndexMoudle> thissubs=getSubMod(code,icode);
        if (thissubs.size()>0){
            allmods.addAll(thissubs);
            for (int i=0;i<thissubs.size();i++){
                allmods.addAll(getAllMods(thissubs.get(i).getCode(),icode));
            }
        }
        return allmods;
    }


    /**
    * @Description: 根据给定模型节点的code和所属计划的icode返回submods
    * @Param: [code,icode]
    * @return: java.util.List<com.acmr.model.zhzs.IndexMoudle>
    * @Author: lyh
    * @Date: 2018/9/4
    */
    public  List<IndexMoudle> getSubMod(String code,String icode){
        List<IndexMoudle> submods=new ArrayList<>();
        List<DataTableRow> subs = IndexEditDao.Fator.getInstance().getIndexdatadao().getSubModsbyCode(code,icode).getRows();
        for (int i=0;i<subs.size();i++){
            PubInfo.printStr(subs.get(i).getRows().toString());
            IndexMoudle mod=new IndexMoudle();
            mod.setCname(subs.get(i).getString("cname"));
            mod.setCode(subs.get(i).getString("code"));
            mod.setDacimal(subs.get(i).getString("dacimal"));
            mod.setFormula(subs.get(i).getString("formula"));
            mod.setIfzb(subs.get(i).getString("ifzb"));
            mod.setIfzs(subs.get(i).getString("ifzs"));
            mod.setIndexcode(subs.get(i).getString("indexcode"));
            mod.setProcode(subs.get(i).getString("procode"));
            mod.setSortcode(subs.get(i).getString("sortcode"));
            mod.setWeight(subs.get(i).getString("weight"));
            submods.add(mod);
        }
        return submods;
    }

    /**
    * @Description: 根据指定的code删除节点
    * @Param: [code]
    * @return: int
    * @Author: lyh
    * @Date: 2018/9/5
    */
    public int deleteMod(String code){
        IndexMoudle indexMoudle=getData(code);
        String pcode=indexMoudle.getProcode();
        String icode=indexMoudle.getIndexcode();
        int i= IndexEditDao.Fator.getInstance().getIndexdatadao().deleteMod(code);
        List<IndexMoudle> subs=getSubMod(pcode,icode);
        List<String> codes=new ArrayList<>();
        for (int j=0;j<subs.size();j++){
            codes.add(subs.get(j).getCode());
        }
        resort(codes);
        return i;
    }

    public  void resort(List<String> codes){
        for (int i=0;i<codes.size();i++){
            IndexEditDao.Fator.getInstance().getIndexdatadao().setSort(codes.get(i),i);
        }
    }
    /**
     * 查找功能
     */
    public ArrayList<IndexMoudle> found(int type,String code,String icode){
        ArrayList<IndexMoudle> indexMoudles=new ArrayList<IndexMoudle>();
        List<DataTableRow> data = new ArrayList<>();
        if(type==0){//0表示是通过code查的
            data = IndexEditDao.Fator.getInstance().getIndexdatadao().getLikeCode(code,icode).getRows();

        }else if(type==1){//1表示是通过cname查的
            data = IndexEditDao.Fator.getInstance().getIndexdatadao().getLikeCname(code,icode).getRows();
        }
        for(int i=0;i<data.size();i++){
            IndexMoudle index= new IndexMoudle();
            index.setCode(data.get(i).getString("code"));
            index.setCname(data.get(i).getString("cname"));
            index.setProcode(data.get(i).getString("procode"));
            index.setIndexcode(data.get(i).getString("indexcode"));
            index.setIfzs(data.get(i).getString("ifzs"));
            index.setDacimal(data.get(i).getString("dacimal"));
            index.setWeight( data.get(i).getString("weight"));
            index.setSortcode( data.get(i).getString("sortcode"));
            index.setIfzb(data.get(i).getString("ifzb"));
            index.setFormula(data.get(i).getString("formula"));
            indexMoudles.add(index);
        }
        return indexMoudles;
    }
    /**
     * 新增指数或者指标
     */
    public int addZStoModel(IndexMoudle indexMoudle){
        return IndexEditDao.Fator.getInstance().getIndexdatadao().addZS(indexMoudle);
    }

    /**
     * 编辑模型节点更新
     * @param indexMoudle
     * @return
     */
    public static int updateToModel(IndexMoudle indexMoudle){
        return IndexEditDao.Fator.getInstance().getIndexdatadao().updateModel(indexMoudle);
    }
    /**
     * 查所选的树的节点信息
     */
    public IndexMoudle getData(String code){
        IndexMoudle indexMoudle = new IndexMoudle();
        DataTableRow data = IndexEditDao.Fator.getInstance().getIndexdatadao().getDataByCode(code).getRows().get(0);
        indexMoudle.setCode(data.getString("code"));
        indexMoudle.setCname(data.getString("cname"));
        indexMoudle.setProcode(data.getString("procode"));
        indexMoudle.setSortcode(data.getString("sortcode"));
        indexMoudle.setIndexcode(data.getString("indexcode"));
        indexMoudle.setIfzb(data.getString("ifzb"));
        indexMoudle.setIfzs(data.getString("ifzs"));
        indexMoudle.setFormula(data.getString("formula"));
        indexMoudle.setDacimal(data.getString("dacimal"));
        indexMoudle.setWeight(data.getString("weight"));
        return indexMoudle;
    }
    /**
     * 查询当前最大的sortcode是多少
     */
    public String getCurrentSort(String procode,String icode){
        //先检查这个procode下面有没有东西
        String result = "";
        boolean rs = IndexEditDao.Fator.getInstance().getIndexdatadao().checkProcode(procode,icode);
        if(rs){
            DataTableRow data = IndexEditDao.Fator.getInstance().getIndexdatadao().getCurrentSort(procode,icode).getRows().get(0);
            int i = data.getint(0);
             result = String.valueOf(i+1);
        }else{
            result = "0";
        }
        return result;//加1返回
    }

    /**
     * 查询zslist
     * @param
     */
    public ArrayList<IndexMoudle> getZSList(String icode){
        ArrayList<IndexMoudle> indexMoudles=new ArrayList<IndexMoudle>();
        List<DataTableRow> data = new ArrayList<>();
        data = IndexEditDao.Fator.getInstance().getIndexdatadao().getZSList(icode).getRows();
        for(int i=0;i<data.size();i++){
            IndexMoudle index= new IndexMoudle();
            index.setCode(data.get(i).getString("code"));
            index.setCname(data.get(i).getString("cname"));
            index.setProcode(data.get(i).getString("procode"));
            index.setIndexcode(data.get(i).getString("indexcode"));
            index.setIfzs(data.get(i).getString("ifzs"));
            index.setDacimal(data.get(i).getString("dacimal"));
            index.setWeight( data.get(i).getString("weight"));
            index.setSortcode( data.get(i).getString("sortcode"));
            index.setIfzb(data.get(i).getString("ifzb"));
            index.setFormula(data.get(i).getString("formula"));
            indexMoudles.add(index);
        }
        return indexMoudles;
    }
    /**
     * 编辑模型节点的时候要把它自己这个节点以及下边的子节点删掉，否则会有问题
     */
    public ArrayList<IndexMoudle> getZSList(String icode,String code){
        ArrayList<IndexMoudle> indexMoudles=new ArrayList<IndexMoudle>();
        List<DataTableRow> data = new ArrayList<>();
        data = IndexEditDao.Fator.getInstance().getIndexdatadao().getZSList(icode).getRows();
        List<IndexMoudle> subs = getAllMods(code,icode);
        List<String> subcodes = new ArrayList<String>();
        subcodes.add(code);//将自己以及下边的指数子节点全都加进来
        for (int i = 0; i <subs.size() ; i++) {
            if(subs.get(i).getIfzs().equals("1")){
                subcodes.add(subs.get(i).getCode());
            }
        }
        for(int i=0;i<data.size();i++){
            IndexMoudle index= new IndexMoudle();
            if(!subcodes.contains(data.get(i).getString("code"))) {//展示不包括它自己以及它下属的模型节点列表
                index.setCode(data.get(i).getString("code"));
                index.setCname(data.get(i).getString("cname"));
                index.setProcode(data.get(i).getString("procode"));
                index.setIndexcode(data.get(i).getString("indexcode"));
                index.setIfzs(data.get(i).getString("ifzs"));
                index.setDacimal(data.get(i).getString("dacimal"));
                index.setWeight(data.get(i).getString("weight"));
                index.setSortcode(data.get(i).getString("sortcode"));
                index.setIfzb(data.get(i).getString("ifzb"));
                index.setFormula(data.get(i).getString("formula"));
                indexMoudles.add(index);
            }
        }
        return indexMoudles;
    }
    /**
     * 检查code是否已经存在
     */
    public boolean checkCode(String code){
        return IndexEditDao.Fator.getInstance().getIndexdatadao().checkCode(code);
    }
    
    /** 
    * @Description: 检查module的公式中是否引用了code 
    * @Param: [code] 
    * @return: boolean 
    * @Author: lyh
    * @Date: 2018/9/10 
    */ 
    public boolean checkModule(String code){
        Boolean bool=IndexEditDao.Fator.getInstance().getIndexdatadao().checkModule(code);
        return bool;
    }
    public int toSaveAll(String indexcode, ArrayList<IndexZb> indexzb, IndexList indexList){
       return IndexEditDao.Fator.getInstance().getIndexdatadao().toSaveAll(indexcode,indexzb,indexList);
    }
    /**
     * 指标表查单个信息
     */
    public IndexZb getZBData(String code){
        IndexZb indexZb = new IndexZb();
        DataTableRow data = IndexEditDao.Fator.getInstance().getIndexdatadao().getZBData(code).getRows().get(0);
        indexZb.setCode(data.getString("code"));
        indexZb.setZbcode(data.getString("zbcode"));
        indexZb.setDatasource(data.getString("datasource"));
        indexZb.setIndexcode(data.getString("indexcode"));
        indexZb.setCompany(data.getString("company"));
        indexZb.setDatatimes(data.getString("datatimes"));
        indexZb.setUnitcode(data.getString("unitcode"));
        indexZb.setRegions(data.getString("regions"));
        indexZb.setDacimal(data.getString("dacimal"));
        return indexZb;
    }

}
