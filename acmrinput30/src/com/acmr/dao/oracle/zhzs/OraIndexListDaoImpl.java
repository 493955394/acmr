package com.acmr.dao.oracle.zhzs;

import acmr.util.DataTable;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IIndexListDao;
import com.acmr.model.zhzs.IndexList;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OraIndexListDaoImpl implements IIndexListDao {

    @Override
    public String getName() {
        String sql = "select cname from tb_coindex_index where code = 'D002' ";
        return AcmrInputDPFactor.getQuickQuery().getDataScarSql(sql);
    }

    @Override
    public DataTable getByUser(String usercode) {
        String sql = "select * from tb_coindex_index where createuser= ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{usercode});
    }

    @Override
    public DataTable getByCode(String code) {
        String sql = "select * from tb_coindex_index where code= ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{code});
    }

    @Override
    public DataTable getLikeCode(String code) {
        String sql = "select * from tb_coindex_index where lower(code) like ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{"%" + code + "%"});
    }

    @Override
    public DataTable getLikeCname(String cname) {
        String sql = "select * from tb_coindex_index where lower(cname) like ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{"%" + cname + "%"});
    }

    @Override
    public int addIndexlist(IndexList indexList) {
        String sql1 = "insert into tb_coindex_index (code,cname,procode,ifdata,state,sort,startperiod,delayday,planperiod,plantime,createuser,createtime,updatetime) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        List<Object> params = new ArrayList<Object>();
        params.add(indexList.getCode());
        params.add(indexList.getCname());
        params.add(indexList.getProcode());
        params.add(indexList.getIfdata());
        params.add(indexList.getState());
        params.add(indexList.getSort());
        params.add(indexList.getStartperiod());
        params.add(indexList.getDelayday());
        params.add(indexList.getPlanperiod());
        params.add(indexList.getPlantime());
        params.add(indexList.getCreateuser());
        params.add(indexList.getCreatetime());
        params.add(indexList.getUpdatetime());
        return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, params.toArray());
    }

    @Override
    //复制到
    public int addNplan(IndexList indexList, String code) {
        String sql1 = "update tb_coindex_index set code=? ,cname=? ,procode=? where code=?";
        List<Object> params = new ArrayList<Object>();
        params.add(indexList.getCode());
        params.add(indexList.getCname());
        params.add(indexList.getProcode());
        params.add(code);
        return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, params.toArray());
    }

    @Override
    public int updateCategory(String code,String procode) {
        String sql1 = "update tb_coindex_index set procode=? where code=?";
        List<Object> params = new ArrayList<Object>();
        params.add(procode);
        params.add(code);
        return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, params.toArray());
    }

    @Override
    public int delIndexcp(String code)  {
        StringBuffer sbf = new StringBuffer();
        List<Object> params = new ArrayList<Object>();
        //sbf.append("delete from tb_right_user_role t where t.rolecode = ? and t.userid in ( ");
        sbf.append("delete from tb_coindex_index t where t.code = ? ");
        params.add(code);
        /*for (int i = 0; i < userids.length; i++) {
            sbf.append("?");
            if (i != userids.length - 1) {
                sbf.append(",");
            }
            params.add(userids[i]);
        }
        sbf.append(")");*/

        return AcmrInputDPFactor.getQuickQuery().executeSql(sbf.toString(), params.toArray());

    }
    public int updateCp(IndexList indexList) {
        String sql1 = "";
        List<Object> parms = new ArrayList<Object>();
        if (indexList.getCname() != null) {
            sql1 += ",cname=?";
            parms.add(indexList.getCname());
        }
        if(indexList.getProcode()!=null){
            sql1+=",procode=?";
            parms.add(indexList.getProcode());
        }
        if(indexList.getState()!=null){
            sql1+=",state=?";
            parms.add(indexList.getState());
        }
        /*if(role.getRights()!=null){
            sql1+=",rights=?";
            StringBuffer sbf=new StringBuffer("");
            for(int i=0;i<role.getRights().size();i++){
                if(i==role.getRights().size()-1){
                    sbf.append(role.getRights().get(i));
                }else{
                    sbf.append(role.getRights().get(i)+",");
                }
            }
            parms.add(sbf.toString());
        }*/
        if (sql1.length() > 0) {
            sql1 += ",updatetime=?";
            parms.add(new Date(System.currentTimeMillis()));

        }
        if (sql1.equals("")) {
            return 0;
        }
        sql1 = "update tb_coindex_index set " + sql1.substring(1) + " where code=?";
        parms.add(indexList.getCode());
        return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, parms.toArray());
    }
}