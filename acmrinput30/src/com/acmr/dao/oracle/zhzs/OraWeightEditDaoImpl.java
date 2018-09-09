package com.acmr.dao.oracle.zhzs;

import acmr.util.DataTable;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IWeightEditDao;

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
}
