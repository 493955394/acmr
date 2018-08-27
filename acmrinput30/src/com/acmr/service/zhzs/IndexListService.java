package com.acmr.service.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.model.zhzs.IndexList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IndexListService {
    String name = IndexListDao.Fator.getInstance().getIndexdatadao().getName();
    //通过usercode得到计划和目录列表
    static List<DataTableRow> indexlist = IndexListDao.Fator.getInstance().getIndexdatadao().getCodeByUser("usercode01").getRows();
    public ArrayList<IndexList> getIndexList(){
        ArrayList<IndexList> indexLists=new ArrayList<>();
        for(int i=0;i<indexlist.size();i++){
            IndexList index=new IndexList();
            index.setCode(indexlist.get(i).get("code").toString());
            index.setCname(indexlist.get(i).get("cname").toString());
            index.setCreateuser(indexlist.get(i).get("createuser").toString());
            index.setIfdata(indexlist.get(i).get("ifdata").toString());
            index.setState(indexlist.get(i).get("state").toString());
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


        public static void main(String[] args) {
        IndexListService indexListService=new IndexListService();
        ArrayList<IndexList> index=indexListService.getIndexList();
        for(int i=0;i<index.size();i++){
            PubInfo.printStr(index.get(i).getPlanperiod());
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
}
