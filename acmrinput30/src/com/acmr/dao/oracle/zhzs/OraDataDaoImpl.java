package com.acmr.dao.oracle.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IDataDao;
import com.acmr.model.zhzs.Data;
import com.acmr.model.zhzs.DataResult;


import java.io.Serializable;
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
            params.add(dataResults.get(i).getSessionid());
            AcmrInputDPFactor.getQuickQuery().executeSql(sql1, params.toArray());
        }
        return 1;
    }
    @Override
    public DataTable getSubMod(String code){
        String sql = "select * from tb_coindex_data_result_tmp where procode=?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{code});
    }
    public DataTable getModData(String code){
        String sql = "select * from tb_coindex_task_module_tmp where code=?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{code});
    }

   @Override
    public int subDataCheck(String taskcode,String modcode,String reg,String time,String sessionid){//检查是否有数
        String sql = "select * from tb_coindex_data_result_tmp where taskcode=? and modcode=? and region=? and time=? and sessionid=?";
        DataTable table = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,modcode,reg,time,sessionid});
        if (table.getRows().size()>0){
            return 0;
        }
        else return 1;
    }
    @Override
    public int addZSData (DataResult dataResult){
        String sql1 = "insert into tb_coindex_data_result_tmp (taskcode,modcode,region,ayearmon,data,updatetime,sessionid) values(?,?,?,?,?,?,?)";
        List<Object> params = new ArrayList<Object>();
            params.add(dataResult.getTaskcode());
            params.add(dataResult.getModcode());
            params.add(dataResult.getRegion());
            params.add(dataResult.getAyearmon());
            params.add(dataResult.getData());
            params.add(new Timestamp(new Date().getTime()));
            params.add(dataResult.getSessionid());
            AcmrInputDPFactor.getQuickQuery().executeSql(sql1, params.toArray());
        return 1;
    }
    public String getDataResult(String taskcode,String modcode,String reg,String time,String sessionid){
        String sql = "select * from tb_coindex_data_result_tmp where taskcode =? and modecode =? and region=? sessionid=?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{modcode,sessionid}).getRows().get(0).getString("data");
    }
}