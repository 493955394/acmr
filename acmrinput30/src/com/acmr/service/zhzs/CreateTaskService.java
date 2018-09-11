package com.acmr.service.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.model.zhzs.IndexList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateTaskService {
    
    /** 
    * @Description: 获得数据库中要生成的新时间小于现在的时间的计划list
    * @Param: [] 
    * @return: void 
    * @Author: lyh
    * @Date: 2018/9/11 
    */ 
    public List<IndexList> getStartIndex() throws ParseException {
        List<IndexList> indexlist=new ArrayList<>();
        String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        PubInfo.printStr(date);
        DataTable table=IndexListDao.Fator.getInstance().getIndexdatadao().getStartLists(date);
        List<DataTableRow> list=table.getRows();
        for (int i=0;i<list.size();i++){
            PubInfo.printStr(list.get(i).getRows().toString());
            String code=list.get(i).getString("code");
            String cname=list.get(i).getString("cname");
            String procode=list.get(i).getString("procode");
            String sort=list.get(i).getString("sort");
            String startperiod=list.get(i).getString("startperiod");
            String delayday=list.get(i).getString("delayday");
            String planperiod=list.get(i).getString("planperiod");
            String plantime1=list.get(i).getString("plantime");
            Date plantime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(plantime1);
            String createuser=list.get(i).getString("createuser");
            IndexList indexList=new IndexList(code,cname,procode,sort,startperiod,delayday,planperiod,plantime,createuser);
            indexlist.add(indexList);
        }
        return indexlist;
    }

    /**
    * @Description: 返回指定code的计划应该存在的所有periods(期数)
    * @Param: [code]
    * @return: void
    * @Author: lyh
    * @Date: 2018/9/11
    */
    public List<String> getPeriods(String code){
        List<String> periods=new ArrayList<>();

        return periods;
    }



/*
    public static void main(String[] args) throws ParseException {
        getStartIndex();
    }*/
}
