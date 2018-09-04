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

    @Override
    public DataTable getSubModsbyCode(String code,String icode) {
        if (code==""){
            String sql="select * from tb_coindex_module where indexcode=? and procode is null order by sortcode";
            return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{icode});
        }
        else {
            String sql="select * from tb_coindex_module where indexcode=? and procode=? order by sortcode";
            return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[] {icode,code});
        }
    }
}
