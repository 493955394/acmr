package com.acmr.dao.oracle.zhzs;

import acmr.data.DataQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IDataDao;
import com.acmr.dao.zhzs.IRightDao;
import com.acmr.model.zhzs.DataResult;
import com.acmr.model.zhzs.right;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OraRightDaoImpl implements IRightDao {

    @Override
    public DataTable getRightList(String indexcode) {
        String sql = "select * from tb_coindex_right where indexcode =?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{indexcode});
    }

    @Override
    public DataTable searchDepName(String keyword) {
        String sql = "select * from tb_right_department where lower(cname) like ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{"%" + keyword + "%"});
    }

    @Override
    public DataTable searchUserName(String keyword) {
        String sql = "select * from tb_right_user where lower(cname) like ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{"%" + keyword + "%"});
    }
}