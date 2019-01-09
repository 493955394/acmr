package com.acmr.dao.oracle.zhzs;

import acmr.util.DataTableRow;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.ISchemeDao;

import java.util.List;

public class OraSchemeDaoImpl implements ISchemeDao {
    @Override
    public String getSelectedSchemeCode(String indexcode) {
        String sql="select code from tb_coindex_scheme where indexcode=? and state=1";
        List<DataTableRow> rows=AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{indexcode}).getRows();
        if (rows.size()!=0){
            return rows.get(0).getString("code");
        }
        else return null;
    }

    @Override
    public String getSchemeNameByCode(String code) {
        String sql="select cname from tb_coindex_scheme where code=?";
        List<DataTableRow> rows=AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{code}).getRows();
        if (rows.size()==0) return null;
        else return rows.get(0).getString("cname");
    }
}
