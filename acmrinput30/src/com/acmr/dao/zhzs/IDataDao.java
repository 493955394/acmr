package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import com.acmr.model.zhzs.DataResult;

import java.util.List;


public interface IDataDao {
    public DataTable getData(String taskcode,String zbcode,String region,String time,String sessionid);
    public int addDataResult (List<DataResult> dataResults);
}
