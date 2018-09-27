package com.acmr.dao.oracle.zhzs;

import acmr.data.DataQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IWeightEditDao;
import com.acmr.model.zhzs.Data;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class OraWeightEditDaoImpl implements IWeightEditDao {
    @Override
    public DataTable getModsbyIcode(String icode) {
        String sql="select * from tb_coindex_module where indexcode=?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{icode});

    }

    @Override
    public int weightset(String code, String weight) {
        String sql="update tb_coindex_module set weight=? where code=?";
        return AcmrInputDPFactor.getQuickQuery().executeSql(sql,new Object[]{weight,code});
    }

    @Override
    public DataTable getTroos(String taskcode) {
        String sql="select * from tb_coindex_task_module_tmp where taskcode=? and procode is null order by sortcode";
        DataTable table=AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{taskcode});
        return table;
    }

    @Override
    public DataTable getSubTmods(String code) {
        String sql="select * from tb_coindex_task_module_tmp where procode=? order by sortcode";
        DataTable table=AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{code});
        return table;
    }

    @Override
    public int tWeightUpd (String code, String weight) {
        String sql="update tb_coindex_task_module_tmp set weight=? where code=?";
        return AcmrInputDPFactor.getQuickQuery().executeSql(sql,new Object[]{weight,code});
    }

    @Override
    public int ReWeight(String tcode) {
        DataQuery dataQuery = null;
        try {
            dataQuery = AcmrInputDPFactor.getDataQuery();
            dataQuery.beginTranse();
            //删除原有的
            String sql="delete from tb_coindex_task_module_tmp where taskcode=?";
            dataQuery.executeSql(sql,new Object[]{tcode});
            //复制模型
            String sql2 = "select * from tb_coindex_task_module where taskcode=?";
            DataTable table1=dataQuery.getDataTableSql(sql2,new Object[]{tcode});
            List<DataTableRow> rows1=table1.getRows();
            for(int i=0;i<rows1.size();i++){
                String code= rows1.get(i).getString("code");
                String cname=rows1.get(i).getString("cname");
                String procode = rows1.get(i).getString("procode");
                String ifzs = rows1.get(i).getString("ifzs");
                String ifzb = rows1.get(i).getString("ifzb");
                String formula = rows1.get(i).getString("formula");
                String sortcode = rows1.get(i).getString("sortcode");
                String weight = rows1.get(i).getString("weight");
                String dacimal = rows1.get(i).getString("dacimal");
                String orcode=rows1.get(i).getString("orcode");
                String sql3 = "insert into tb_coindex_task_module_tmp (code,cname,taskcode,procode,ifzs,ifzb,formula,sortcode,weight,dacimal,orcode) values(?,?,?,?,?,?,?,?,?,?,?)";
                dataQuery.executeSql(sql3,new Object[]{code,cname,tcode,procode,ifzs,ifzb,formula,sortcode,weight,dacimal,orcode});
            }
            dataQuery.commit();

        } catch (SQLException e) {
            if (dataQuery != null) {
                dataQuery.rollback();
                e.printStackTrace();
                return 1;
            }
        } finally {
            if (dataQuery != null) {
                dataQuery.releaseConnl();
            }
        }

        return 0;
    }
}
