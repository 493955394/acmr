package com.acmr.dao.oracle.zhzs;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IIndexListDao;

public class OraIndexListDaoImpl implements IIndexListDao {

    @Override
    public String getName() {
        String sql="select cname from tb_coindex_index where code = 'D002' ";
        return AcmrInputDPFactor.getQuickQuery().getDataScarSql(sql);
    }
}
