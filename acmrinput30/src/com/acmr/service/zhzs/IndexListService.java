package com.acmr.service.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.model.zhzs.IndexCategory;
import com.acmr.model.zhzs.IndexList;

import java.util.*;

public class IndexListService {
    String name = IndexListDao.Fator.getInstance().getIndexdatadao().getName();
    //通过usercode得到计划和目录列表

   // static List<DataTableRow> indexlist = IndexListDao.Fator.getInstance().getIndexdatadao().getByUser("usercode01").getRows();
    /**
    * @Description: 传usercode返回该用户创建的计划list
    * @Param: [] usercode先写死，后面改
    * @return: java.util.ArrayList<com.acmr.model.zhzs.IndexList>
    * @Author: lyh
    * @Date: 2018/8/28
    */
    public ArrayList<IndexList> getIndexList(){
        List<DataTableRow> indexlist = IndexListDao.Fator.getInstance().getIndexdatadao().getByUser("usercode01").getRows();
        ArrayList<IndexList> indexLists=new ArrayList<>();
        for(int i=0;i<indexlist.size();i++){
            IndexList index=new IndexList();
            index.setCode(indexlist.get(i).get("code").toString());
            index.setCname(indexlist.get(i).get("cname").toString());
            index.setCreateuser(indexlist.get(i).get("createuser").toString());
            index.setIfdata(indexlist.get(i).get("ifdata").toString());
            if (indexlist.get(i).get("state")!=null){
                index.setState(indexlist.get(i).get("state").toString());
            }
            index.setCreatetime((Date) indexlist.get(i).get("createtime"));
            if(indexlist.get(i).get("plantime")!=null){
                index.setPlantime((Date) indexlist.get(i).get("plantime"));
            }
            if(indexlist.get(i).get("updatetime")!=null){
                index.setUpdatetime((Date) indexlist.get(i).get("updatetime"));
            }
            if(indexlist.get(i).get("delayday")!=null){
                index.setDelayday(indexlist.get(i).get("delayday").toString());
            }
            if(indexlist.get(i).get("planperiod")!=null){
                index.setPlanperiod(indexlist.get(i).get("planperiod").toString());
            }
            if(indexlist.get(i).get("procode")!=null){
                index.setProcode(indexlist.get(i).get("procode").toString());
            }
            if(indexlist.get(i).get("sort")!=null){
                index.setSort(indexlist.get(i).get("sort").toString());
            }
            if(indexlist.get(i).get("startperiod")!=null){
                index.setStartperiod(indexlist.get(i).get("startperiod").toString());
            }
            indexLists.add(index);
        }
        return indexLists;
    }

    /**
     * 通过Code查询
     * @param code
     * @return
     */
    public IndexList getData(String code){
        DataTableRow data = IndexListDao.Fator.getInstance().getIndexdatadao().getByCode(code).getRows().get(0);
        IndexList index= new IndexList();
        index.setCode(data.getString("code"));
        index.setCname(data.getString("cname"));
        index.setCreateuser(data.getString("createuser"));
        index.setIfdata(data.getString("ifdata"));
        index.setState(data.getString("state"));
        index.setCreatetime(data.getDate("createtime"));
        index.setPlantime( data.getDate("plantime"));
        index.setUpdatetime( data.getDate("updatetime"));
        index.setDelayday(data.getString("delayday"));
        index.setPlanperiod(data.getString("planperiod"));
        index.setProcode(data.getString("procode"));
        index.setSort(data.getString("sort"));
        index.setStartperiod(data.getString("startperiod"));
        return index;
    }
    //查找功能
    public ArrayList<IndexList> found(int type,String code){
        ArrayList<IndexList> indexLists=new ArrayList<>();
        List<DataTableRow> data = new ArrayList<>();
       if(type==0){//0表示是通过code查的
            data = IndexListDao.Fator.getInstance().getIndexdatadao().getLikeCode(code).getRows();

       }else if(type==1){//1表示是通过cname查的
           data = IndexListDao.Fator.getInstance().getIndexdatadao().getLikeCname(code).getRows();
       }
        for(int i=0;i<data.size();i++){
            IndexList index= new IndexList();
            index.setCode(data.get(i).getString("code"));
            index.setCname(data.get(i).getString("cname"));
            index.setCreateuser(data.get(i).getString("createuser"));
            index.setIfdata(data.get(i).getString("ifdata"));
            index.setState(data.get(i).getString("state"));
            index.setCreatetime(data.get(i).getDate("createtime"));
            index.setPlantime( data.get(i).getDate("plantime"));
            index.setUpdatetime( data.get(i).getDate("updatetime"));
            index.setDelayday(data.get(i).getString("delayday"));
            index.setPlanperiod(data.get(i).getString("planperiod"));
            index.setProcode(data.get(i).getString("procode"));
            index.setSort(data.get(i).getString("sort"));
            index.setStartperiod(data.get(i).getString("startperiod"));
            indexLists.add(index);
        }
        return indexLists;
    }

    public int addCp(IndexList indexList) {
        return IndexListDao.Fator.getInstance().getIndexdatadao().addIndexlist(indexList);
    }
    public int updatePlan(IndexList indexList,String code) {
        return IndexListDao.Fator.getInstance().getIndexdatadao().addNplan(indexList,code);
    }
    public static int delCataplan(String code){
        return IndexListDao.Fator.getInstance().getIndexdatadao().delIndexcp(code);
    }
    public static int updateCate(String code,String procode){
        return IndexListDao.Fator.getInstance().getIndexdatadao().updateCategory(code,procode);
    }
    public static int updateCatePlan(IndexList indexList){
        return IndexListDao.Fator.getInstance().getIndexdatadao().updateCp(indexList);
    }





/*        Date test= (Date) indexlist.get(1).get("plantime");
        PubInfo.printStr("123"+test.toString());
        for (int i=0;i<indexlist.size();i++){
            PubInfo.printStr(indexlist.get(i).get("createtime").toString());
            if(indexlist.get(i).get("plantime")!=null){
                PubInfo.printStr(indexlist.get(i).get("plantime").toString());
            }
        }*/
/*        PubInfo.printStr(code.getRows().get(1).getRows().toString());
        PubInfo.printStr(code.getColumns().get(0).getColumnName());*/
    }

