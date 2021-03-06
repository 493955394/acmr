package com.acmr.dao.oracle.zhzs;


import acmr.data.DataQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IIndexEditDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.zhzs.IndexList;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.model.zhzs.IndexZb;
import com.acmr.model.zhzs.Scheme;

import java.sql.Date;
import java.sql.SQLException;
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
    public int deleteMod(String code) {
        // return值暂时无用
        if (code == null) {
            return 1;
        }
        DataQuery dataQuery = null;
        try {

            dataQuery = AcmrInputDPFactor.getDataQuery();
            dataQuery.beginTranse();
            // 删除module表
            StringBuffer sbf = new StringBuffer();
            sbf.append("delete from tb_coindex_module where code=?");
            dataQuery.executeSql(sbf.toString(),new Object[]{code});
            //删除scheme表
            String sql1 = "delete from tb_coindex_scheme where modcode=?";
            dataQuery.executeSql(sql1,new Object[]{code});
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

    @Override
    public void setSort(String code, int sort) {
        String sql="update tb_coindex_module set sortcode=? where code=?";
        AcmrInputDPFactor.getQuickQuery().executeSql(sql,new Object[]{sort,code});
    }

    @Override
    public DataTable getLikeCode(String code,String icode) {
        String sql = "select * from tb_coindex_module where lower(code) like ? and indexcode=? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{"%" + code + "%",icode});
    }

    @Override
    public DataTable getLikeCname(String cname ,String icode) {
        String sql = "select * from tb_coindex_module where lower(cname) like ? and indexcode=?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{"%" + cname + "%",icode});
    }
    @Override
    public int addZS(IndexMoudle indexMoudle, ArrayList<Scheme> scodes){
        DataQuery dataQuery = null;
        try {

            dataQuery = AcmrInputDPFactor.getDataQuery();
            dataQuery.beginTranse();
            // 删除旧的

            String sql1 = "insert into tb_coindex_module (code,cname,procode,indexcode,ifzs,ifzb,formula,sortcode,weight,dacimal,copycode) values(?,?,?,?,?,?,?,?,?,?,?)";
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
            params.add(indexMoudle.getCode());
            dataQuery.executeSql(sql1, params.toArray());

            //如果已经有方案了，所有的方案都要新加这个模型节点
            if(scodes.size()>0){
                for (int i = 0; i <scodes.size() ; i++) {
                    String sql = "insert into tb_coindex_scheme (code,cname,indexcode,modcode,state,ifzb,weight,formula,remark) values(?,?,?,?,?,?,?,?,?)";
                    List<Object> arr = new ArrayList<Object>();
                    arr.add(scodes.get(i).getCode());
                    arr.add(scodes.get(i).getCname());
                    arr.add(indexMoudle.getIndexcode());
                    arr.add(indexMoudle.getCode());
                    arr.add(scodes.get(i).getState());
                    arr.add(indexMoudle.getIfzb());
                    arr.add(indexMoudle.getWeight());
                    arr.add(indexMoudle.getFormula());
                    arr.add(scodes.get(i).getRemark());
                    dataQuery.executeSql(sql, arr.toArray());
                }
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
    /*
     *通过code查信息
     */
    @Override
    public DataTable getDataByCode(String code) {
        String sql = "select * from tb_coindex_module where code = ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{code});
    }
    /**
     * 查询指数列表
     */
    @Override
    public DataTable getZSList(String icode){
        String sql = "select * from tb_coindex_module where ifzs = 1 and indexcode = ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{icode});
    }
    @Override
    public DataTable getCurrentSort(String procode,String icode){
        if(procode==""){
            String sql = "select max(sortcode) from tb_coindex_module where indexcode = ? and procode is null ";
            List<Object> params = new ArrayList<Object>();
            params.add(icode);
            return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, params.toArray());
        }else {
            String sql = "select max(sortcode) from tb_coindex_module where indexcode = ? and procode = ? ";
            List<Object> params = new ArrayList<Object>();
            params.add(icode);
            params.add(procode);
            return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, params.toArray());
        }
    }
    @Override
    public int updateModel(IndexMoudle indexMoudle){
        String sql1 = "";
        List<Object> parms = new ArrayList<Object>();
        if (indexMoudle.getCname() != null) {
            sql1 += ",cname=?";
            parms.add(indexMoudle.getCname());
        }
        if(indexMoudle.getProcode()!=null&& indexMoudle.getProcode()!=""){
            sql1+=",procode=?";
            parms.add(indexMoudle.getProcode());
        }
        if(indexMoudle.getIfzs()!=null){
            sql1+=",ifzs=?";
            parms.add(indexMoudle.getIfzs());
        }
        if(indexMoudle.getDacimal()!=null){
            sql1+=",dacimal=?";
            parms.add(indexMoudle.getDacimal());
        }
        if(indexMoudle.getSortcode()!=null){
            sql1+=",sortcode=?";
            parms.add(indexMoudle.getSortcode());
        }
        if (sql1.equals("")) {
            return 0;
        }
        sql1 = "update tb_coindex_module set " + sql1.substring(1) + " where code=? and indexcode=?";
        parms.add(indexMoudle.getCode());
        parms.add(indexMoudle.getIndexcode());
        return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, parms.toArray());
    }
    /**
     *
     * @param procode
     * @return
     */
    @Override
    public boolean checkProcode(String procode,String icode){
        if(procode==""){
            String sql = "select count(*) from tb_coindex_module where indexcode = ? and procode is null ";
            List<Object> params = new ArrayList<Object>();
            params.add(icode);
            DataTable dt = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, params.toArray());
            List<DataTableRow> rows = dt.getRows();
            int getint = rows.get(0).getint(0);
            if (getint > 0) {
                return true;
            }
            return false;
        }else {
        String sql = "select count(*) from tb_coindex_module where procode = ? and indexcode = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(procode);
        params.add(icode);
        DataTable dt = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, params.toArray());
        List<DataTableRow> rows = dt.getRows();
        int getint = rows.get(0).getint(0);
        if (getint > 0) {
            return true;
        }
        return false;
        }
    }

    @Override
    public boolean checkModule(String code,String icode) {
        String sql="select * from tb_coindex_scheme where formula like ? and indexcode=?";
        DataTable rows= AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[] {"%"+code+"%",icode});
        if (rows.getRows().size()>0){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean checkCode(String code){
        String sql = "select count(*) from tb_coindex_module where code = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(code);
        DataTable dt = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, params.toArray());
        List<DataTableRow> rows = dt.getRows();
        int getint = rows.get(0).getint(0);
        if (getint > 0) {
            return true;
        }
        return false;
    }
    @Override
    public int toSaveAll(String indexcode,IndexList indexList){
        // return值暂时无用
        if (indexcode == null) {
            return 0;
        }
        DataQuery dataQuery = null;
        try {

           dataQuery = AcmrInputDPFactor.getDataQuery();
            dataQuery.beginTranse();
            // 删除旧的

            /* String delold = "delete from tb_coindex_zb where indexcode = ?";
            dataQuery.executeSql(delold, new Object[] { indexcode });
            // 添加新的
            if(indexZb.size()>0){
            String sql = "insert into tb_coindex_zb (code,indexcode,zbcode,company,datasource,regions,unitcode) values(?,?,?,?,?,?,?)";
            for (int i = 0; i < indexZb.size(); i++) {
                ArrayList<Object> parms = new ArrayList<Object>();
                parms.add(indexZb.get(i).getCode());
                parms.add(indexZb.get(i).getIndexcode());
                parms.add(indexZb.get(i).getZbcode());
                parms.add(indexZb.get(i).getCompany());
                parms.add(indexZb.get(i).getDatasource());
                parms.add(indexZb.get(i).getRegions());
                parms.add(indexZb.get(i).getUnitcode());
                dataQuery.executeSql(sql, parms.toArray());
            }
            }*/
            //更新基本信息表
            String sql1 = "";
            List<Object> upd = new ArrayList<Object>();
            if(indexList.getProcode()!=null){
                sql1+=",procode=?";
                upd.add(indexList.getProcode());
            }
                sql1+=",cname=?";
                upd.add(indexList.getCname());
                sql1+=",delayday=?";
                upd.add(indexList.getDelayday());
            if(indexList.getPlantime()!=null){
                sql1+=",plantime=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
                upd.add(indexList.getPlantime());}
            if(indexList.getPlantime()!=null){
                sql1+=",planperiod=?";
                upd.add(indexList.getPlanperiod());}
                sql1+=",remark=?";
                upd.add(indexList.getRemark());
            if (sql1.equals("")) {
                return 0;
            }
            sql1 = "update tb_coindex_index set " + sql1.substring(1) + " where code=?";
            upd.add(indexList.getCode());
            dataQuery.executeSql(sql1, upd.toArray());
            dataQuery.commit();
        } catch (SQLException e) {
            if (dataQuery != null) {
                dataQuery.rollback();
                return 1;
            }
            e.printStackTrace();
        } finally {
            if (dataQuery != null) {
                dataQuery.releaseConnl();
            }
        }
        return 0;
    }
    @Override
    public DataTable getZBData(String code) {
        String sql = "select * from tb_coindex_zb where code = ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{code});
    }

    @Override
    public boolean saveCheckCname(String icode, String cname, String ifzs) {
        String sql = "select count(*) from tb_coindex_module where indexcode =? and cname=? and ifzs=?";
        List<Object> params = new ArrayList<Object>();
        params.add(icode);
        params.add(cname);
        params.add(ifzs);
        DataTable dt = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, params.toArray());
        List<DataTableRow> rows = dt.getRows();
        int getint = rows.get(0).getint(0);
        if (getint > 0) {
            return true;//说明有这个名字
        }
        return false;
    }

    @Override
    public boolean updCheckCname(String icode, String cname, String ifzs, String code) {
        String sql = "select count(*) from tb_coindex_module where indexcode =? and cname=? and ifzs=? and code!=?";
        List<Object> params = new ArrayList<Object>();
        params.add(icode);
        params.add(cname);
        params.add(ifzs);
        params.add(code);
        DataTable dt = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, params.toArray());
        List<DataTableRow> rows = dt.getRows();
        int getint = rows.get(0).getint(0);
        if (getint > 0) {
            return true;//说明有这个名字
        }
        return false;
    }

    @Override
    public int toSaveRange(String indexcode, ArrayList<IndexZb> indexZb, IndexList indexList) {
        // return值暂时无用
        if (indexcode == null) {
            return 0;
        }
        DataQuery dataQuery = null;
        try {

            dataQuery = AcmrInputDPFactor.getDataQuery();
            dataQuery.beginTranse();
            // 删除旧的

            String delold = "delete from tb_coindex_zb where indexcode = ?";
            dataQuery.executeSql(delold, new Object[] { indexcode });
            // 添加新的
            if(indexZb.size()>0){
                String sql = "insert into tb_coindex_zb (code,indexcode,zbcode,company,datasource,regions,unitcode) values(?,?,?,?,?,?,?)";
                for (int i = 0; i < indexZb.size(); i++) {
                    ArrayList<Object> parms = new ArrayList<Object>();
                    parms.add(indexZb.get(i).getCode());
                    parms.add(indexZb.get(i).getIndexcode());
                    parms.add(indexZb.get(i).getZbcode());
                    parms.add(indexZb.get(i).getCompany());
                    parms.add(indexZb.get(i).getDatasource());
                    parms.add(indexZb.get(i).getRegions());
                    parms.add(indexZb.get(i).getUnitcode());
                    dataQuery.executeSql(sql, parms.toArray());
                }
            }
            //更新基本信息表
            String sql1 = "";
            List<Object> upd = new ArrayList<Object>();
            sql1+=",startperiod=?";
            upd.add(indexList.getStartperiod());
            sql1+=",plantime=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
            upd.add(indexList.getPlantime());
            sql1+=",planperiod=?";
            upd.add(indexList.getPlanperiod());
            if (sql1.equals("")) {
                return 0;
            }
            sql1 = "update tb_coindex_index set " + sql1.substring(1) + " where code=?";
            upd.add(indexList.getCode());
            dataQuery.executeSql(sql1, upd.toArray());
            dataQuery.commit();
        } catch (SQLException e) {
            if (dataQuery != null) {
                dataQuery.rollback();
                return 1;
            }
            e.printStackTrace();
        } finally {
            if (dataQuery != null) {
                dataQuery.releaseConnl();
            }
        }
        return 0;
    }
}
