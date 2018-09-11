package com.acmr.service.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.model.zhzs.IndexList;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class CreateTaskService {
    
    /** 
    * @Description: 获得数据库中已启动的计划 
    * @Param: [] 
    * @return: void 
    * @Author: lyh
    * @Date: 2018/9/11 
    */ 
    public void getStartIndex(){
        List<IndexList> indexlist=new ArrayList<>();
        DataTable table=IndexListDao.Fator.getInstance().getIndexdatadao().getStartLists();
        List<DataTableRow> list=table.getRows();
        for (int i=0;i<list.size();i++){
            PubInfo.printStr(list.get(i).getRows().toString());
        }

    }
/*
    public static void main(String[] args) {
        getStartIndex();
    }*/
}
