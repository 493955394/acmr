package com.acmr.dao.zhzs;

import acmr.util.DataTable;


public interface IDataDao {
    public DataTable getData(String taskcode,String zbcode,String region,String time);
}
