package com.acmr.dao.oracle.zhzs;

import acmr.util.DataTable;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IIndexListDao;
import com.acmr.model.zhzs.IndexCategory;
import com.acmr.model.zhzs.IndexList;

import java.sql.Date;
import java.util.ArrayList;
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
    @Override
    public DataTable getLikeCode(String code) {
        String sql="select * from tb_coindex_index where code like ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[] {"%" + code + "%"});
    }
    @Override
    public DataTable getLikeCname(String cname) {
        String sql="select * from tb_coindex_index where cname like ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[] {"%" + cname + "%"});
    }
    @Override
    public int addIndexlist(IndexList indexList) {
        String sql1 = "insert into tb_coindex_index (code,cname,procode,ifdata,state,sort,startperiod,delayday,planperiod,plantime,createuser,createtime,updatetime) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        List<Object> parms = new ArrayList<Object>();
        parms.add(indexList.getCode());
        parms.add(indexList.getCname());
        parms.add(indexList.getProcode());
        parms.add(indexList.getIfdata());
        parms.add(indexList.getState());
        parms.add(indexList.getSort());
        parms.add(indexList.getStartperiod());
        parms.add(indexList.getDelayday());
        parms.add(indexList.getPlanperiod());
        parms.add(indexList.getPlantime());
        parms.add(indexList.getCreateuser());
        parms.add(indexList.getCreatetime());
        parms.add(indexList.getUpdatetime());
        return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, parms.toArray());
    }

}