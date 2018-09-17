package com.acmr.dao.oracle.zhzs;

import acmr.util.DataTable;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IWeightEditDao;
import com.acmr.model.zhzs.Data;

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
}
