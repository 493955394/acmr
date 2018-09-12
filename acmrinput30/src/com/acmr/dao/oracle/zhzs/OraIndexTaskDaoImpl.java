package com.acmr.dao.oracle.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IIndexTaskDao;

import java.util.List;

public class OraIndexTaskDaoImpl implements IIndexTaskDao {
    @Override
    public  boolean hasTask(String indexcode, String ayearmon) {
        String sql="select * from tb_coindex_task where indexcode=? and ayearmon=?";
        DataTable table= AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{indexcode,ayearmon});
        List<DataTableRow> rows=table.getRows();
        if (rows.size()>0){
            return true;
        }
        else {
            return false;
        }
    }

/*    public static void main(String[] args) {
        OraIndexTaskDaoImpl oraIndexTaskDao=new OraIndexTaskDaoImpl();
        oraIndexTaskDao.hasTask("R001","2014");
    }*/
}
