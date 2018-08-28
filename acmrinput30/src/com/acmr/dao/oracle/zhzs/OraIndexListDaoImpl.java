package com.acmr.dao.oracle.zhzs;

import acmr.util.DataTable;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IIndexListDao;

import java.util.List;

public class OraIndexListDaoImpl implements IIndexListDao {

    @Override
    public String getName() {
        String sql="select cname from tb_coindex_index where code = 'D002' ";
        return AcmrInputDPFactor.getQuickQuery().getDataScarSql(sql);
    }

    @Override
    public DataTable getByUser(String usercode) {
        String sql="select * from tb_coindex_index where createuser= ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[] {usercode});
    }
    @Override
    public DataTable getByCode(String code) {
        String sql="select * from tb_coindex_index where code= ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[] {code});
    }
}
