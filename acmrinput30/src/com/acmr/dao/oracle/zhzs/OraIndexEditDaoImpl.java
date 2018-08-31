package com.acmr.dao.oracle.zhzs;


import acmr.util.DataTable;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IIndexEditDao;

import java.util.List;

public class OraIndexEditDaoImpl implements IIndexEditDao {

    @Override
    public DataTable getZBSbyIndexCode(String icode) {
        String sql="select * from tb_coindex_zb where indexcode=?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[] {icode});
    }
}
