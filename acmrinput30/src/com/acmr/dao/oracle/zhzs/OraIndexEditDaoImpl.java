package com.acmr.dao.oracle.zhzs;


import acmr.util.DataTable;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IIndexEditDao;
import com.acmr.model.zhzs.IndexMoudle;

import java.util.ArrayList;
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
    @Override
    public DataTable getLikeCode(String code) {
        String sql = "select * from tb_coindex_module where lower(code) like ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{"%" + code + "%"});
    }

    @Override
    public DataTable getLikeCname(String cname) {
        String sql = "select * from tb_coindex_module where lower(cname) like ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{"%" + cname + "%"});
    }
    public int addZS(IndexMoudle indexMoudle){
        String sql1 = "insert into tb_coindex_module (code,cname,procode,indexcode,ifzs,ifzb,formula,sortcode,weight,dacimal) values(?,?,?,?,?,?,?,?,?,?)";
        List<Object> params = new ArrayList<Object>();
        params.add(indexMoudle.getCode());
        params.add(indexMoudle.getCname());
        params.add(indexMoudle.getProcode());
        params.add(indexMoudle.getIndexcode());
        params.add(indexMoudle.getIfzs());
        params.add(indexMoudle.getIfzb());
        params.add(indexMoudle.getFormula());
        params.add(indexMoudle.getSortcode());
        params.add(indexMoudle.getWeight());
        params.add(indexMoudle.getDacimal());
        return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, params.toArray());
    }
}
