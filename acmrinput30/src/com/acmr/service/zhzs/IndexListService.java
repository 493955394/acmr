package com.acmr.service.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.IndexListDao;

import java.util.List;

public class IndexListService {
    String name = IndexListDao.Fator.getInstance().getIndexdatadao().getName();
    //通过usercode得到计划和目录列表
    List<DataTableRow> indexlist = IndexListDao.Fator.getInstance().getIndexdatadao().getCodeByUser("usercode01").getRows();

    public List<DataTableRow> getIndexlist() {
        return indexlist;
    }
    /*    public static void main(String[] args) {
        for (int i=0;i<indexlist.size();i++){
            PubInfo.printStr(indexlist.get(i).getRows().toString());
        }
*//*        PubInfo.printStr(code.getRows().get(1).getRows().toString());
        PubInfo.printStr(code.getColumns().get(0).getColumnName());*//*
    }*/
}
