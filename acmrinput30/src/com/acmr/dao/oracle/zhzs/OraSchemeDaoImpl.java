package com.acmr.dao.oracle.zhzs;

import acmr.data.DataQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.ISchemeDao;
import com.acmr.model.zhzs.Scheme;

import java.sql.SQLException;
import java.util.ArrayList;
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

    @Override
    public List<DataTableRow> getSchemesByIcode(String icode) {
        String sql="select * from tb_coindex_scheme where indexcode=?";
        List<DataTableRow> rows=AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{icode}).getRows();
        return rows;
    }

    @Override
    public String getModSchemeWeight(String scode,String modcode) {
        String sql="select * from tb_coindex_scheme where code=? and modcode=?";
        List<DataTableRow> rows=AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{scode,modcode}).getRows();
        if (rows.size()==0) return "";
        else return rows.get(0).getString("weight");
    }
    @Override
    public int checkCname(String icode, String cname) {
        String sql = "select count(*) from tb_coindex_scheme where icode=? and cname =?";
        List<Object> params = new ArrayList<Object>();
        params.add(icode);
        params.add(cname);
        DataTable dt = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, params.toArray());
        List<DataTableRow> rows = dt.getRows();
        int getint = rows.get(0).getint(0);
        if (getint > 0) {
            return 0;
        }else {
            return 1;
        }
    }
    @Override
    public int insertSch(Scheme scheme, List<DataTableRow> rows) {

        DataQuery dataQuery = null;
        try {
            dataQuery = AcmrInputDPFactor.getDataQuery();
            dataQuery.beginTranse();
            for (int i = 0; i < rows.size(); i++) {
                String code = scheme.getCode();
                String cname = scheme.getCname();
                String icode = scheme.getIndexcode();
                String modcode = rows.get(i).getString("modcode");
                String state = scheme.getState();
                String remark = scheme.getRemark();
                String sql1 = "insert into tb_coindex_scheme (code,cname,indexcode,modcode,state.remark) values(?,?,?,?,?,?)";

                dataQuery.executeSql(sql1, new Object[]{code, cname, icode, modcode, state, remark});

                dataQuery.commit();
            }

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
