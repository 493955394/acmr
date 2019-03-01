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
        String sql = "select count(*) from tb_coindex_scheme where indexcode=? and cname =?";
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
            String sql1 = "insert into tb_coindex_scheme (code,cname,indexcode,modcode,ifzb,formula,state,remark) values(?,?,?,?,?,?,?,?)";
            for (int i = 0; i < rows.size(); i++) {

                /*String code = scheme.getCode();
                String cname = scheme.getCname();
                String icode = scheme.getIndexcode();
                String modcode = rows.get(i).getString("code");
                String state = scheme.getState();
                String remark = scheme.getRemark();
                dataQuery.executeSql(sql1, new Object[]{code, cname, icode, modcode, state, remark});*/
                List<Object> params = new ArrayList<Object>();
                params.add(scheme.getCode());
                params.add(scheme.getCname());
                params.add(scheme.getIndexcode());
                params.add(rows.get(i).getString("code"));
                params.add(rows.get(i).getString("ifzb"));
                params.add(rows.get(i).getString("formula"));
                params.add(scheme.getState());
                params.add(scheme.getRemark());
                dataQuery.executeSql(sql1, params.toArray());

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
    @Override
    public int delSch(String code)  {
        StringBuffer sbf = new StringBuffer();
        List<Object> params = new ArrayList<Object>();
        sbf.append("delete from tb_coindex_scheme t where t.code = ? ");
        params.add(code);


        return AcmrInputDPFactor.getQuickQuery().executeSql(sbf.toString(), params.toArray());

    }
    @Override
    public int updateSch(Scheme scheme) {
        String sql1 = "";
        List<Object> parms = new ArrayList<Object>();
        if(scheme.getCode()!=null){
            sql1+=",code=?";
            parms.add(scheme.getCode());
        }
        if (scheme.getCname() != null) {
            sql1 += ",cname=?";
            parms.add(scheme.getCname());
        }
        if(scheme.getRemark()!=null){
            sql1+=",remark=?";
            parms.add(scheme.getRemark());
        }
        if(scheme.getState()!=null){
            sql1+=",state=?";
            parms.add(scheme.getState());
        }
        if (sql1.equals("")) {
            return 0;
        }
        sql1 = "update tb_coindex_scheme set " + sql1.substring(1) + " where code=?";
        parms.add(scheme.getCode());
        return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, parms.toArray());
    }

    @Override
    public List<DataTableRow> getSch(String icode,String code) {
        String sql="select * from tb_coindex_scheme where indexcode=? and code=?";
        List<DataTableRow> rows=AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{icode,code}).getRows();
        return rows;
    }
    @Override
    public int cloneSch(List<Scheme> scheme) {

        DataQuery dataQuery = null;
        try {
            dataQuery = AcmrInputDPFactor.getDataQuery();
            dataQuery.beginTranse();
            String sql1 = "insert into tb_coindex_scheme (code,cname,indexcode,modcode,state,ifzb,weight,formula,remark) values(?,?,?,?,?,?,?,?,?)";
            for (int i = 0; i < scheme.size(); i++) {
                List<Object> params = new ArrayList<Object>();
                params.add(scheme.get(0).getCode());
                params.add(scheme.get(0).getCname());
                params.add(scheme.get(0).getIndexcode());
                params.add(scheme.get(0).getModcode());
                params.add(scheme.get(0).getState());
                params.add(scheme.get(0).getIfzb());
                params.add(scheme.get(0).getWeight());
                params.add(scheme.get(0).getFormula());
                params.add(scheme.get(0).getRemark());
                dataQuery.executeSql(sql1, params.toArray());

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
