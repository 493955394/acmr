package com.acmr.dao.oracle.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IDataDao;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OraDataDaoImpl implements IDataDao {
    @Override
    public DataTable getData(String taskcode,String zbcode,String region,String time){
        String sql = "select * from tb_coindex_data where taskcode = ? and zbcode=? and region=? and time=?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{taskcode,zbcode,region,time});
    }
}