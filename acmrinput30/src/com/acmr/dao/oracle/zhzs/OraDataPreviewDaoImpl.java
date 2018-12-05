package com.acmr.dao.oracle.zhzs;
import acmr.data.DataQuery;
import acmr.util.DataTable;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IDataPreviewDao;
import com.acmr.model.zhzs.DataPreview;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author:djj
 * @date: 2018/12/5 10:53/**
 * @author:djj
 * @date: 2018/12/5 10:53
 */
public class OraDataPreviewDaoImpl implements IDataPreviewDao {
    @Override
    public DataTable getModuleData(String icode, String ifzs){
        String sql = "select * from tb_coindex_module where indexcode = ? and ifzs=?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{icode,ifzs});
    }

    @Override
    public int addDataResult(List<DataPreview> dataResults) {
        if (dataResults.size() == 0) {
            return 0;
        }
            DataQuery dataQuery = null;
            try {
                dataQuery = AcmrInputDPFactor.getDataQuery();
                dataQuery.beginTranse();
                // 删除旧的

                String delold = "delete from tb_coindex_data_preview where indexcode =? ";
                dataQuery.executeSql(delold, new Object[] {dataResults.get(0).getIndexcode()});
                // 添加新的
                String sql1 = "insert into tb_coindex_data_preview (indexcode,modcode,region,ayearmon,data,updatetime) values(?,?,?,?,?,?)";
                for (int i = 0; i <dataResults.size() ; i++) {
                    List<Object> params = new ArrayList<Object>();
                    params.add(dataResults.get(i).getIndexcode());
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

    @Override
    public DataTable getSubMod(String code){
        String sql = "select * from tb_coindex_module where procode=? order by sortcode";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{code});
    }

    @Override
    public int subDataCheck(String modcode, String reg, String time) {
        String sql = "";
        DataTable table = new DataTable();
            sql = "select * from tb_coindex_data_preview where modcode=? and region=? and ayearmon=?";
            table = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{modcode,reg,time});
        if (table.getRows().size()>0){
            return 0;
        }
        else return 1;
    }

    @Override
    public DataTable getData(String modcode, String region, String time) {
        String sql = "select * from tb_coindex_data_preview where modcode=? and region=? and ayearmon=?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{modcode,region,time});
    }

    @Override
    public int addZSData( DataPreview dataResult) {
        String sql1 ="";
        sql1 = "insert into tb_coindex_data_preview (indexcode,modcode,region,ayearmon,data,updatetime) values(?,?,?,?,?,?)";

        List<Object> params = new ArrayList<Object>();
        params.add(dataResult.getIndexcode());
        params.add(dataResult.getModcode());
        params.add(dataResult.getRegion());
        params.add(dataResult.getAyearmon());
        params.add(dataResult.getData());
        params.add(new Timestamp(new Date().getTime()));

        return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, params.toArray());
    }

    @Override
    public DataTable getRootData(String icode) {
        String sql = "select * from tb_coindex_module where indexcode = ? and ifzs=1 and procode is null order by sortcode ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{icode});
    }

    @Override
    public String findRegions(String icode) {
        String sql = "select * from tb_coindex_zb where indexcode =?";
        DataTable table = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{icode});
        if(table.getRows().size()>0){
            return table.getRows().get(0).getString("regions");
        }
        else {
            return null;
        }
    }
}
