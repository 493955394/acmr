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
import java.util.Map;

public class OraRightDaoImpl implements IRightDao {

    @Override
    public DataTable getRightList(String indexcode) {
        String sql = "select * from tb_coindex_right where indexcode =?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{indexcode});
    }

    @Override
    public DataTable searchDepName(String keyword) {
        String sql = "select * from tb_right_department where lower(cname) like ? and ifclose='0' order by sortcode";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{"%" + keyword + "%"});
    }

    @Override
    public DataTable searchUserName(String keyword) {
        String sql = "select * from tb_right_user where lower(cname) like ? and ifclose='0'";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{"%" + keyword + "%"});
    }

    @Override
    public int saveRightList(String icode, List<Map<String, String>> list) {
        DataQuery dataQuery = null;
        try {
            dataQuery = AcmrInputDPFactor.getDataQuery();
            dataQuery.beginTranse();
            // 删除旧的
            String delold = "delete from tb_coindex_right where indexcode =?";
            dataQuery.executeSql(delold, new Object[] {icode});
            // 添加新的
            String sql1 = "insert into tb_coindex_right (indexcode,sort,depusercode,right,createuser) values(?,?,?,?,?)";
            for (int i = 0; i <list.size() ; i++) {
                List<Object> params = new ArrayList<Object>();
                params.add(list.get(i).get("indexcode"));
                params.add(list.get(i).get("sort"));
                params.add(list.get(i).get("depusercode"));
                params.add(list.get(i).get("right"));
                params.add(list.get(i).get("createuser"));
                dataQuery.executeSql(sql1, params.toArray());
            }
            dataQuery.commit();
        } catch (SQLException e) {
            if (dataQuery != null) {
                dataQuery.rollback();
                return 0;
            }
            e.printStackTrace();
        } finally {
            if (dataQuery != null) {
                dataQuery.releaseConnl();
            }
        }
        return 1;


    }
}