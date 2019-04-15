package com.acmr.dao.oracle.zhzs;

import acmr.data.DataQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.DataDao;
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
    public String getPastData(String taskcode,String modcode,String region,String ayearmoon){
        String sql = "select * from tb_coindex_data_result where taskcode =? and modcode=? and region=? and ayearmon=?";
        DataTable table = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,modcode,region,ayearmoon});
        if(table.getRows().size()>0){
            return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,modcode,region,ayearmoon}).getRows().get(0).getString("data");
        }else{
            return null;
        }

    }

    /**
     * 启动任务时要是没有数据就去数据库找数据并保存
     * @param dataResults
     * @return
     */
    @Override
    public int addMathData(List<DataResult> dataResults) {
        DataQuery dataQuery = null;
        try {
            dataQuery = AcmrInputDPFactor.getDataQuery();
            dataQuery.beginTranse();
        String sql="insert into tb_coindex_data (taskcode,zbcode,region,ayearmon,data) values(?,?,?,?,?)";
        for (int i = 0; i <dataResults.size() ; i++) {
            List<Object> params = new ArrayList<Object>();
            params.add(dataResults.get(i).getTaskcode());
            params.add(dataResults.get(i).getModcode());
            params.add(dataResults.get(i).getRegion());
            params.add(dataResults.get(i).getAyearmon());
            if(!dataResults.get(i).getData().equals("null"))
            {params.add(dataResults.get(i).getData());}
            else{
                params.add("");
            }
            dataQuery.executeSql(sql, params.toArray());
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

    @Override
    public DataTable getTaskZbData(String taskcode, String zbcode) {
        String sql = "select * from tb_coindex_task_zb where taskcode= ? and code= ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,zbcode});
    }

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
    public List<String> getAllTime(String procode) {
        List<String> times = new ArrayList<>();
        String sql="select d.ayearmon,d.zbcode from tb_coindex_task_zb t left join tb_coindex_data d on t.code = d.zbcode where t.procode = ? group by d.ayearmon,d.zbcode ";
        DataTable table=AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{procode});
        for (int i = 0; i <table.getRows().size(); i++) {
            times.add(table.getRows().get(i).getString("ayearmon")+":"+table.getRows().get(i).getString("zbcode"));
        }
      return times;
    }

    @Override
    public List<String> getAllZbcode(List<String> taskcodes,String procode) {
        List<String> zbcodes = new ArrayList<>();
        for (String tcode : taskcodes) {
            String sql="select code from tb_coindex_task_zb where taskcode = ? and procode=?";
            DataTable table=AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{tcode,procode});
            for (int i = 0; i <table.getRows().size(); i++) {
                zbcodes.add(table.getRows().get(i).getString("code"));
            }
        }
        return zbcodes;
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
            String sql = "select * from tb_coindex_task_module_tmp where procode=? order by sortcode";
            return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{code});
    }

    @Override
    public DataTable getSubMods(String code){
        String sql = "select * from tb_coindex_task_module where procode=? order by sortcode";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{code});
    }

    @Override
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
            if(AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,modcode,reg,time,sessionid}).getRows().size()>0){
                return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,modcode,reg,time,sessionid}).getRows().get(0).getString("data");
            }
            else{
                return "";
            }

        }
       else {
            String sql = "select * from tb_coindex_data_result where taskcode =? and modcode =? and region=? and ayearmon=? ";
            if(AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,modcode,reg,time}).getRows().size()>0){
                return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,modcode,reg,time}).getRows().get(0).getString("data");
            }
            else{
                return "";
            }
        }
    }

    @Override
    public int copyDataResult(String taskcode,String sessionid){
        DataQuery dataQuery=null;
        try {
            dataQuery = AcmrInputDPFactor.getDataQuery();
            dataQuery.beginTranse();
            //删除session之前的记录
            String sql="delete from tb_coindex_data_result_tmp where sessionid=? and taskcode=?";
            dataQuery.executeSql(sql,new Object[]{sessionid,taskcode});

            //从tb_coindex_data_result复制数据到tb_coindex_data_result_tmp
            String sql1="select * from tb_coindex_data_result where taskcode=?";
            DataTable table=dataQuery.getDataTableSql(sql1,new Object[]{taskcode});
            List<DataTableRow> rows=table.getRows();
            for (int i=0;i<rows.size();i++){
                String modcode=rows.get(i).getString("modcode");
                String region=rows.get(i).getString("region");
                String ayearmon=rows.get(i).getString("ayearmon");
                String data=rows.get(i).getString("data");
                Date updatetime = rows.get(i).getDate("updatetime");
                String sql2="insert into tb_coindex_data_result_tmp (taskcode,modcode,region,ayearmon,data,updatetime,sessionid) values(?,?,?,?,?,?,?)";
                dataQuery.executeSql(sql2,new Object[]{taskcode,modcode,region,ayearmon,data,updatetime,sessionid});
            }
            dataQuery.commit();

        }
        catch (SQLException e){
            if (dataQuery != null) {
                dataQuery.rollback();
                e.printStackTrace();
                return 1;
            }
        }
        finally {
            if (dataQuery != null) {
                dataQuery.releaseConnl();
            }
        }
        return 0;
    }

    @Override
    public int resetPage(String taskcode,String sessionid) {
        DataQuery dataQuery=null;
        try {
            dataQuery = AcmrInputDPFactor.getDataQuery();
            dataQuery.beginTranse();
            //删除结果临时表中session之前的记录
            String sql="delete from tb_coindex_data_result_tmp where sessionid=? and taskcode=?";
            dataQuery.executeSql(sql,new Object[]{sessionid,taskcode});

            //从tb_coindex_data_result复制数据到tb_coindex_data_result_tmp
            String sql1="select * from tb_coindex_data_result where taskcode=?";
            DataTable table=dataQuery.getDataTableSql(sql1,new Object[]{taskcode});
            List<DataTableRow> rows=table.getRows();
            for (int i=0;i<rows.size();i++){
                String modcode=rows.get(i).getString("modcode");
                String region=rows.get(i).getString("region");
                String ayearmon=rows.get(i).getString("ayearmon");
                String data=rows.get(i).getString("data");
                Date updatetime = rows.get(i).getDate("updatetime");
                String sql2="insert into tb_coindex_data_result_tmp (taskcode,modcode,region,ayearmon,data,updatetime,sessionid) values(?,?,?,?,?,?,?)";
                dataQuery.executeSql(sql2,new Object[]{taskcode,modcode,region,ayearmon,data,updatetime,sessionid});
            }

            //删除data临时表中session之前的记录
            String sqldata="delete from tb_coindex_data_tmp where sessionid=? and taskcode=?";
            dataQuery.executeSql(sqldata,new Object[]{sessionid,taskcode});

            //从tb_coindex_data复制数据到tb_coindex_data_tmp
            String sql3="select * from tb_coindex_data where taskcode=?";
            DataTable tabledata=dataQuery.getDataTableSql(sql3,new Object[]{taskcode});
            List<DataTableRow> rows1=tabledata.getRows();
            for (int i=0;i<rows1.size();i++){
                String zbcode=rows1.get(i).getString("zbcode");
                String region=rows1.get(i).getString("region");
                String ayearmon=rows1.get(i).getString("ayearmon");
                String data=rows1.get(i).getString("data");
                String sql4="insert into tb_coindex_data_tmp (taskcode,zbcode,region,ayearmon,data,sessionid) values(?,?,?,?,?,?)";
                dataQuery.executeSql(sql4,new Object[]{taskcode,zbcode,region,ayearmon,data,sessionid});
            }
            //删除tb_coindex_task_module_tmp中的记录
            String sqlmodel="delete from tb_coindex_task_module_tmp where taskcode=?";
            dataQuery.executeSql(sqlmodel,new Object[]{taskcode});
            //复制tb_coindex_task_module到tb_coindex_task_module_tmp
            String sql12="insert into tb_coindex_task_module_tmp (select * from tb_coindex_task_module where taskcode=?)";
            dataQuery.executeSql(sql12,new Object[]{taskcode});
            dataQuery.commit();

        }
        catch (SQLException e){
            if (dataQuery != null) {
                dataQuery.rollback();
                e.printStackTrace();
                return 1;
            }
        }
        finally {
            if (dataQuery != null) {
                dataQuery.releaseConnl();
            }
        }
        return 0;
    }

    @Override
    public int saveResult(String taskcode, String sessionid) {
        DataQuery dataQuery=null;
        try {
            dataQuery = AcmrInputDPFactor.getDataQuery();
            dataQuery.beginTranse();
            //删除tb_coindex_data_result表中之前的记录
            String sql="delete from tb_coindex_data_result where taskcode=?";
            dataQuery.executeSql(sql,new Object[]{taskcode});
            //从tb_coindex_data_result_tmp复制数据到tb_coindex_data_result
            String sql2="insert into tb_coindex_data_result (taskcode,modcode,region,ayearmon,data,updatetime)";
            sql2 = sql2+" select taskcode,modcode,region,ayearmon,data,updatetime from tb_coindex_data_result_tmp b where b.taskcode=? and b.sessionid=?";
            dataQuery.executeSql(sql2,new Object[]{taskcode,sessionid});

            //删除tb_coindex_data表中之前的记录
            String sqldata="delete from tb_coindex_data where taskcode=?";
            dataQuery.executeSql(sqldata,new Object[]{taskcode});
            //从tb_coindex_data_tmp复制数据到tb_coindex_data
            String sql3="insert into tb_coindex_data (taskcode,zbcode,region,ayearmon,data)";
            sql3 = sql3+" select taskcode,zbcode,region,ayearmon,data from tb_coindex_data_tmp b where b.taskcode=? and b.sessionid=?";
            dataQuery.executeSql(sql3,new Object[]{taskcode,sessionid});

            //删除tb_coindex_task_module中的记录
            String sqlmodel="delete from tb_coindex_task_module where taskcode=?";
            dataQuery.executeSql(sqlmodel,new Object[]{taskcode});
            //复制tb_coindex_task_module到tb_coindex_task_module_tmp
            String sql12="insert into tb_coindex_task_module (select * from tb_coindex_task_module_tmp where taskcode=?)";
            dataQuery.executeSql(sql12,new Object[]{taskcode});

            //更新tb_coindex_task的updatetime时间
            String sqltask="update tb_coindex_task set updatetime=? where code=?";
            List<Object> params = new ArrayList<Object>();
            params.add(new Timestamp(new Date().getTime()));
            params.add(taskcode);
            dataQuery.executeSql(sqltask, params.toArray());

            dataQuery.commit();

        }
        catch (SQLException e){
            if (dataQuery != null) {
                dataQuery.rollback();
                e.printStackTrace();
                return 1;
            }
        }
        finally {
            if (dataQuery != null) {
                dataQuery.releaseConnl();
            }
        }
        return 0;
    }

    /**
     * 获取上期的taskcode,如果有的话
     * @param icode
     * @param ayearmon
     * @return
     */
    @Override
    public DataTable findOldTask(String icode, String ayearmon) {
        try{
        String sql = "select * from tb_coindex_task where ayearmon =(select max(ayearmon) from tb_coindex_task where indexcode=? and ayearmon<?) and indexcode =?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{icode,ayearmon,icode});
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过taskcode和orcode查询对应的模型节点
     * @param taskcode
     * @param orcode
     * @return
     */
    @Override
    public String findModCode(String taskcode, String orcode) {
        String sql = "select code from tb_coindex_task_module_tmp where taskcode=? and orcode =?";
        DataTable table= AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,orcode});
        if(table.getRows().size()>0)
        return table.getRows().get(0).toString();
        return null;
    }
    @Override
    public String findModByOrode(String taskcode, String orcode) {
        String sql = "select code from tb_coindex_task_module where taskcode=? and orcode =?";
        DataTable table= AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,orcode});
        if(table.getRows().size()>0)
            return table.getRows().get(0).toString();
        return null;
    }

   /*public static void main(String[] args) throws NullPointerException {
            List<String> re=DataDao.Fator.getInstance().getIndexdatadao().getAllTime("1553852050584aft7bi570ij");
       System.out.println(re.toString());

    }*/



}