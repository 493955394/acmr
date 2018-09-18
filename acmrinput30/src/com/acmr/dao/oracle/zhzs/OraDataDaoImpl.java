package com.acmr.dao.oracle.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IDataDao;
import com.acmr.model.zhzs.DataResult;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OraDataDaoImpl implements IDataDao {
    @Override
    public DataTable getData(String taskcode,String zbcode,String region,String time,String sessionid){
        String sql = "select * from tb_coindex_data_tmp where taskcode =? and zbcode=? and region=? and time=? and sessionid=?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,zbcode,region,time});
    }
    @Override
    public int addDataResult (List<DataResult> dataResults){
        String sql1 = "insert into tb_coindex_data_result_tmp (taskcode,modcode,region,ayearmon,data,updatetime) values(?,?,?,?,?,?)";
        List<Object> params = new ArrayList<Object>();
        for (int i = 0; i <dataResults.size() ; i++) {
            params.add(dataResults.get(i).getTaskcode());
            params.add(dataResults.get(i).getModcode());
            params.add(dataResults.get(i).getRegion());
            params.add(dataResults.get(i).getAyearmon());
            params.add(dataResults.get(i).getData());
            params.add(new Timestamp(new Date().getTime()));
            AcmrInputDPFactor.getQuickQuery().executeSql(sql1, params.toArray());
        }
        return 1;
    }
}