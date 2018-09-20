package com.acmr.dao.oracle.zhzs;

import acmr.data.DataQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IDataDao;
import com.acmr.model.zhzs.Data;
import com.acmr.model.zhzs.DataResult;


import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OraDataDaoImpl implements IDataDao {
    @Override
    public DataTable getData(boolean iftmp,String taskcode,String zbcode,String region,String time,String sessionid){
        if(!iftmp){
            String sql = "select * from tb_coindex_data where taskcode =? and zbcode=? and region=? and ayearmon=?";
            return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,zbcode,region,time});
        }else{
            String sql = "select * from tb_coindex_data_tmp where taskcode =? and zbcode=? and region=? and ayearmon=? and sessionid=?";
            return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,zbcode,region,time,sessionid});
        }
    }
    @Override
    public int addDataResult (boolean iftmp,List<DataResult> dataResults){
        if (dataResults.size() == 0) {
            return 0;
        }
        if(iftmp){
            DataQuery dataQuery = null;
            try {
                dataQuery = AcmrInputDPFactor.getDataQuery();
                dataQuery.beginTranse();
                // 删除旧的
                String sessionid = dataResults.get(0).getSessionid();
                String taskcode = dataResults.get(0).getTaskcode();
                String delold = "delete from tb_coindex_data_result_tmp where taskcode =? and sessionid=? ";
                dataQuery.executeSql(delold, new Object[] {taskcode,sessionid});
                // 添加新的
                String sql1 = "insert into tb_coindex_data_result_tmp (taskcode,modcode,region,ayearmon,data,updatetime,sessionid) values(?,?,?,?,?,?,?)";
                for (int i = 0; i <dataResults.size() ; i++) {
                    List<Object> params = new ArrayList<Object>();
                    params.add(dataResults.get(i).getTaskcode());
                    params.add(dataResults.get(i).getModcode());
                    params.add(dataResults.get(i).getRegion());
                    params.add(dataResults.get(i).getAyearmon());
                    params.add(dataResults.get(i).getData());
                    params.add(new Timestamp(new Date().getTime()));
                    params.add(dataResults.get(i).getSessionid());
                    dataQuery.executeSql(sql1, params.toArray());
                }
                dataQuery.commit();
            } catch (SQLException e) {
                if (dataQuery != null) {
                    dataQuery.rollback();
                    return 0;
                }
                e.printStackTrace();
            } finally {
                if (dataQuery != null) {
                    dataQuery.releaseConnl();
                }
            }
            return 1;
        }
        else{
            DataQuery dataQuery = null;
            try {
                dataQuery = AcmrInputDPFactor.getDataQuery();
                dataQuery.beginTranse();
                // 删除旧的
                String taskcode = dataResults.get(0).getTaskcode();
                String delold = "delete from tb_coindex_data_result where taskcode =?";
                dataQuery.executeSql(delold, new Object[] {taskcode});
                // 添加新的
                String sql1 = "insert into tb_coindex_data_result (taskcode,modcode,region,ayearmon,data,updatetime) values(?,?,?,?,?,?)";
                for (int i = 0; i <dataResults.size() ; i++) {
                    List<Object> params = new ArrayList<Object>();
                    params.add(dataResults.get(i).getTaskcode());
                    params.add(dataResults.get(i).getModcode());
                    params.add(dataResults.get(i).getRegion());
                    params.add(dataResults.get(i).getAyearmon());
                    params.add(dataResults.get(i).getData());
                    params.add(new Timestamp(new Date().getTime()));
                    dataQuery.executeSql(sql1, params.toArray());
                }
                dataQuery.commit();
            } catch (SQLException e) {
                if (dataQuery != null) {
                    dataQuery.rollback();
                    return 0;
                }
                e.printStackTrace();
            } finally {
                if (dataQuery != null) {
                    dataQuery.releaseConnl();
                }
            }
            return 1;

        }
    }
    @Override
    public DataTable getSubMod(String code){
            String sql = "select * from tb_coindex_task_module_tmp where procode=?";
            return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{code});
    }
    public DataTable getModData(String code){
        String sql = "select * from tb_coindex_task_module_tmp where code=?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{code});
    }

   @Override
    public int subDataCheck(boolean iftmp,String taskcode,String modcode,String reg,String time,String sessionid){//检查是否有数
        String sql = "";
       DataTable table = new DataTable();
        if(iftmp){
            sql = "select * from tb_coindex_data_result_tmp where taskcode=? and modcode=? and region=? and ayearmon=? and sessionid=?";
            table = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,modcode,reg,time,sessionid});}
        else {
            sql = "select * from tb_coindex_data_result where taskcode=? and modcode=? and region=? and ayearmon=?";
            table = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,modcode,reg,time});
   }

        if (table.getRows().size()>0){
            return 0;
        }
        else return 1;
    }
    @Override
    public int addZSData (boolean iftmp,DataResult dataResult){
        String sql1 ="";
        if(iftmp){
            sql1 = "insert into tb_coindex_data_result_tmp (taskcode,modcode,region,ayearmon,data,updatetime,sessionid) values(?,?,?,?,?,?,?)";
        }else{
            sql1 = "insert into tb_coindex_data_result (taskcode,modcode,region,ayearmon,data,updatetime) values(?,?,?,?,?,?)";
        }

        List<Object> params = new ArrayList<Object>();
            params.add(dataResult.getTaskcode());
            params.add(dataResult.getModcode());
            params.add(dataResult.getRegion());
            params.add(dataResult.getAyearmon());
            params.add(dataResult.getData());
            params.add(new Timestamp(new Date().getTime()));
            if(iftmp){
                params.add(dataResult.getSessionid());
            }
           return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, params.toArray());

    }
    public String getDataResult(boolean iftmp,String taskcode,String modcode,String reg,String time,String sessionid){
        if(iftmp){
            String sql = "select * from tb_coindex_data_result_tmp where taskcode =? and modcode =? and region=? and ayearmon=? and sessionid=?";
            return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,modcode,reg,time,sessionid}).getRows().get(0).getString("data");
        }
       else {
            String sql = "select * from tb_coindex_data_result where taskcode =? and modcode =? and region=? and ayearmon=? ";
            return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,modcode,reg,time}).getRows().get(0).getString("data");
        }
    }
}