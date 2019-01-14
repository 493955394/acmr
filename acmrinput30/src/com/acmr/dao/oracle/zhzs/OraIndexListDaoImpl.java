package com.acmr.dao.oracle.zhzs;

import acmr.data.DataQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IIndexListDao;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.model.zhzs.Data;
import com.acmr.model.zhzs.IndexList;

//import java.sql.Date;
import javax.print.DocFlavor;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OraIndexListDaoImpl implements IIndexListDao {

    @Override
    public DataTable getStartLists(String date) {
        String sql = "select * from tb_coindex_index where plantime < to_date(?,'yyyy-mm-dd hh24:mi:ss') and state=1";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{date});
    }

    @Override
    public DataTable getSubLists(String code, String usercode) {
        if (code.equals("")) {
            String sql = "select * from tb_coindex_index where createuser= ? and procode is null order by createtime";
            return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{usercode});
        } else {
            String sql = "select * from tb_coindex_index where procode=? and createuser= ? order by createtime";
            return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{code, usercode});
        }
    }

    @Override
    public String getName() {
        String sql = "select cname from tb_coindex_index where code = 'D002' ";
        return AcmrInputDPFactor.getQuickQuery().getDataScarSql(sql);
    }

    @Override
    public DataTable getByUser(String usercode) {
        String sql = "select * from tb_coindex_index where createuser= ? order by createtime ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{usercode});
    }

    @Override
    public DataTable getByCode(String code) {
        String sql = "select * from tb_coindex_index where code= ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{code});
    }

    @Override
    public DataTable getLikeCode(String code,String userid) {
        String sql = "select * from tb_coindex_index where createuser =? and lower(code) like ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{userid,"%" + code + "%"});
    }

    @Override
    public DataTable getLikeCname(String cname,String userid) {
        String sql = "select * from tb_coindex_index where createuser =? and lower(cname) like ? ";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{userid,"%" + cname + "%"});
    }

    @Override
    public DataTable getLikeCodeByPage(String code,String userid,int page,int pagesize) {
        int b1 = page * pagesize + 1;
        int e1 = b1 + pagesize;
        String sql="select * from (select rownum no,d1.* from (select * from tb_coindex_index where createuser =? and lower(code) like ?) d1) where no>="+b1+" and no<"+ e1;
        return  AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{userid,"%" + code + "%"});
    }

    @Override
    public DataTable getLikeCnameByPage(String cname,String userid,int page,int pagesize) {
        int b1 = page * pagesize + 1;
        int e1 = b1 + pagesize;
        String sql="select * from (select rownum no,d1.* from (select * from tb_coindex_index where createuser=? and lower(cname) like ?) d1) where no>="+b1+" and no<"+ e1;
        return  AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{userid,"%" + cname + "%"});
    }

    @Override
    public int addIndexlist(IndexList indexList) {
        String sql1 = "insert into tb_coindex_index (code,cname,procode,ifdata,state,sort,startperiod,delayday,planperiod,plantime,createuser,createtime,updatetime) values(?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
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

    /**
     * 计划复制到
     * @author wf
     * @date
     * @param
     * @return
     */
    @Override
    public int addCopyplan(String cpcode,IndexList data1) {
        DataQuery dataQuery = null;
        try {
            dataQuery = AcmrInputDPFactor.getDataQuery();
            dataQuery.beginTranse();
            java.util.Date now = new java.util.Date();
            //复制基本信息表
            String sql1 = "insert into tb_coindex_index (code,cname,procode,ifdata,state,sort,startperiod,delayday,planperiod,plantime,createuser,createtime,updatetime,remark) values(?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'),?)";
            String icode = data1.getCode();
            String icname = data1.getCname();
            String iprocode = data1.getProcode();
            String iifdata = data1.getIfdata();
            String istate = data1.getState();
            String isort = data1.getSort();
            String istartpeiod = data1.getStartperiod();
            String idelayday = data1.getDelayday();
            String iplanperiod = data1.getPlanperiod();
            String iplantime = data1.getPlantime();
            //String time = data1.getCreatetime();
            String createtime = data1.getCreatetime().substring(0,data1.getCreatetime().length()-2);
            String createuser = data1.getCreateuser();
            String updatetime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String remark = data1.getRemark();

            /*params.add(new Timestamp(new Date().getTime()));
            params.add(taskcode);
            dataQuery.executeSql(sqltask, params.toArray());*/

            //Object up = new Timestamp(new Date().getTime());
            dataQuery.executeSql(sql1,new Object[]{icode,icname,iprocode,iifdata,istate,isort,istartpeiod,idelayday,iplanperiod,iplantime,createuser,createtime,updatetime,remark});

            //复制筛选条件
            String sql4 = "select * from tb_coindex_zb where indexcode=?";
            DataTable table2 = dataQuery.getDataTableSql(sql4,new Object[]{cpcode});
            List<DataTableRow> row2 = table2.getRows();
            for(int j=0;j<row2.size();j++){
                String code = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                //String incode1 = row2.get(j).getString("indexcode");
                String zbcode = row2.get(j).getString("zbcode");
                String company = row2.get(j).getString("company");
                String datasource = row2.get(j).getString("datasource");
                String regions = row2.get(j).getString("regions");
                String unitcode = row2.get(j).getString("unitcode");
                String dacimal = row2.get(j).getString("dacimal");
                String sql5 = "insert into tb_coindex_zb (code,indexcode,zbcode,company,datasource,regions,unitcode,dacimal) values(?,?,?,?,?,?,?,?)";
                dataQuery.executeSql(sql5,new Object[]{code,icode,zbcode,company,datasource,regions,unitcode,dacimal});
            }

            //复制方案
            String f_sql1 = "select * from tb_coindex_scheme where indexcode=?";
            DataTable f_table1 = dataQuery.getDataTableSql(sql4,new Object[]{cpcode});
            List<DataTableRow> f_row1 = f_table1.getRows();
            for(int j=0;j<f_row1.size();j++){
                String id = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                String code = f_row1.get(j).getString("code");

                String cname = f_row1.get(j).getString("cname");
                String modcode = f_row1.get(j).getString("modcode");
                String state = f_row1.get(j).getString("state");
                String ifzb = f_row1.get(j).getString("ifzb");
                String weight = f_row1.get(j).getString("weight");
                String formula = f_row1.get(j).getString("formula");
                String fremark = f_row1.get(j).getString("remark");
                String f_sql2 = "insert into tb_coindex_scheme (id,code,cname,indexcode,modcode,state,ifzb,weight,formula,remark) values(?,?,?,?,?,?,?,?,?,?)";
                dataQuery.executeSql(f_sql2,new Object[]{id,code,cname,icode,modcode,state,ifzb,weight,formula,fremark});
            }

            //复制模型
            String sql2 = "select * from tb_coindex_module where indexcode=?";
            DataTable table1=dataQuery.getDataTableSql(sql2,new Object[]{cpcode});
            List<DataTableRow> rows1=table1.getRows();
            for(int i=0;i<rows1.size();i++){
                //String code= UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,6);
                String code= UUID.randomUUID().toString().replace("-", "").toLowerCase();
                String cname=rows1.get(i).getString("cname");
                String procode = rows1.get(i).getString("procode");
                String ifzs = rows1.get(i).getString("ifzs");
                String ifzb = rows1.get(i).getString("ifzb");
                String formula = rows1.get(i).getString("formula");
                String sortcode = rows1.get(i).getString("sortcode");
                String weight = rows1.get(i).getString("weight");
                String dacimal = rows1.get(i).getString("dacimal");
                String copycode = rows1.get(i).getString("copycode");
                String sql3 = "insert into tb_coindex_module (code,cname,procode,indexcode,ifzs,ifzb,formula,sortcode,weight,dacimal,copycode) values(?,?,?,?,?,?,?,?,?,?,?)";
                dataQuery.executeSql(sql3,new Object[]{code,cname,procode,icode,ifzs,ifzb,formula,sortcode,weight,dacimal,copycode});
                String modcode =rows1.get(i).getString("code");
                String f_sql3="update tb_coindex_scheme set modcode=? where indexcode=? and modcode=?";
                dataQuery.executeSql(f_sql3,new Object[]{code,icode,modcode});
            }
            //修正tb_coindex_module的procode
            //String newcode = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,6);

            String sql9="select * from tb_coindex_module where indexcode=?";
            DataTable table3=dataQuery.getDataTableSql(sql9,new Object[]{icode});
            List<DataTableRow> rows3=table3.getRows();
            for (int r=0;r<rows3.size();r++){
                String orprocode=rows3.get(r).getString("procode");
                //String copycode=rows3.get(r).getString("copycode");
                String code = rows3.get(r).getString("code");

                if (orprocode!=""){
                    String sql10="select * from tb_coindex_module where copycode=? and indexcode=?";
                    String procode=dataQuery.getDataTableSql(sql10,new Object[]{orprocode,icode}).getRows().get(0).getString("code");
                    //更新这条module的procode
                    String sql11="update tb_coindex_module set procode=? where code=?";
                    dataQuery.executeSql(sql11,new Object[]{procode,code});
                }
            }

            //DataTableRow rows4 = rows3.get(6);


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
    /**
     * 修改计算公式
     * @author wf
     * @date
     * @param
     * @return
     */
    @Override
    public int switchFormu(String ncode,String ocode) {
        DataQuery dataQuery = null;
        try {
            dataQuery = AcmrInputDPFactor.getDataQuery();
            dataQuery.beginTranse();

            //排序所有的公式中的modulecode
            String sql1 = "select * from tb_coindex_zb where indexcode=? order by unitcode";
            DataTable table1 = dataQuery.getDataTableSql(sql1,new Object[]{ncode});
            DataTable table2 = dataQuery.getDataTableSql(sql1,new Object[]{ocode});
            List<DataTableRow> row1 = table1.getRows();
            List<DataTableRow> row2 = table2.getRows();
            //取出所有复制前的公式中的codelist
            List<String> zbcodelist = new ArrayList<>();
            for(int k=0;k<row2.size();k++) {
                String ozbcode = row1.get(k).getString("code");
                zbcodelist.add(ozbcode);
            }
            //取出所有的计算公式
            String sql2 = "select * from tb_coindex_module where indexcode=? order by sortcode";
            DataTable table3 = dataQuery.getDataTableSql(sql2,new Object[]{ncode});
            DataTable table4 = dataQuery.getDataTableSql(sql2,new Object[]{ocode});
            List<DataTableRow> row3 = table3.getRows();
            List<DataTableRow> row4 = table4.getRows();


            for(int j=0;j<row3.size();j++){
                String nformula = row3.get(j).getString("formula");
                String oformula = row4.get(j).getString("formula");
                String nmocode = row3.get(j).getString("code");
                String omocode = row4.get(j).getString("code");
                for(int k=0;k<row1.size();k++){

                    String nforcode = row1.get(k).getString("code");
                    String oforcode = row2.get(k).getString("code");

                    if(nformula.contains(oforcode)){
                        if(nformula.contains("#")){
                            nformula = nformula.replace("#"+oforcode+"#","#"+nforcode+"#");//换成新模型节点code
                            String sql3 = "update tb_coindex_module set formula=? where copycode=? and code=?";
                            //修改方案公式
                            String f_sql4="update tb_coindex_scheme set formula=? where indexcode=? and formula=?";
                            dataQuery.executeSql(f_sql4,new Object[]{nformula,ncode,oformula});
                            dataQuery.executeSql(sql3,new Object[]{nformula,omocode,nmocode});
                        }else{
                            nformula = nformula.replace(oforcode,nforcode);//换成新模型节点code
                            String sql3 = "update tb_coindex_module set formula=? where copycode=? and code=?";
                            //修改方案公式
                            String f_sql4="update tb_coindex_scheme set formula=? where indexcode=? and formula=?";
                            dataQuery.executeSql(f_sql4,new Object[]{nformula,ncode,oformula});
                            dataQuery.executeSql(sql3,new Object[]{nformula,omocode,nmocode});
                        }

                    }

                }

            }
            //修正复制节点的copycode
            for (int m=0;m<row3.size();m++){
                String code = row3.get(m).getString("code");
                String sql11="update tb_coindex_module set copycode=? where code=?";
                dataQuery.executeSql(sql11,new Object[]{code,code});
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
    public Boolean hasIndex(String icode, String usercode) {
        String sql="select * from tb_coindex_index where code=? and createuser=?";
        DataTable table=AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{icode,usercode});
        return table.getRows().size()!=0;
    }

    @Override
    public String getDbcode(String icode) {
        String sql = "select b.dbcode from tb_coindex_index a left join tb_coindex_database b on a.sort = b.sort where a.code=?";
        DataTable table= AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{icode});
            return table.getRows().get(0).toString();
    }

    @Override
    public int saveCheckCname(int ifdata,String usercode, String cname) {
        String sql = "select count(*) from tb_coindex_index where ifdata=? and cname =? and createuser=?";
        List<Object> params = new ArrayList<Object>();
        params.add(ifdata);
        params.add(cname);
        params.add(usercode);
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
    public int updCheckCname(int ifdata,String usercode, String cname,String icode) {
        String sql = "select count(*) from tb_coindex_index where ifdata=? and cname =? and createuser=? and code!=?";
        List<Object> params = new ArrayList<Object>();
        params.add(ifdata);
        params.add(cname);
        params.add(usercode);
        params.add(icode);
        DataTable dt = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, params.toArray());
        List<DataTableRow> rows = dt.getRows();
        int getint = rows.get(0).getint(0);
        if (getint > 0) {
            return 0;
        }else {
            return 1;
        }
    }
        /*}
        String sql1 = "insert into tb_coindex_index (code,cname,procode,ifdata,state,sort,startperiod,delayday,planperiod,plantime,createuser,createtime,updatetime) values(?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?)";
        List<Object> params = new ArrayList<Object>();
        params.add(data1.getCode());
        params.add(data1.getCname());
        params.add(data1.getProcode());
        params.add(data1.getIfdata());
        params.add(data1.getState());
        params.add(data1.getSort());
        params.add(data1.getStartperiod());
        params.add(data1.getDelayday());
        params.add(data1.getPlanperiod());
        params.add(data1.getPlantime());
        params.add(data1.getCreateuser());
        params.add(data1.getCreatetime());
        params.add(new Timestamp(new java.util.Date().getTime()));
        return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, params.toArray());
    }*/

    /**
     * 收到的指数 复制到
     * @author wf
     * @date
     * @param
     * @return
     */
    @Override
    public int addCopyShare(String cpcode,IndexList copydata) {
        DataQuery dataQuery = null;
        try {
            dataQuery = AcmrInputDPFactor.getDataQuery();
            dataQuery.beginTranse();
            java.util.Date now = new java.util.Date();
            //复制基本信息表
            String sql1 = "insert into tb_coindex_index (code,cname,procode,ifdata,state,sort,startperiod,delayday,planperiod,plantime,createuser,createtime,updatetime,remark) values(?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'),?)";
            String icode = copydata.getCode();
            String icname = copydata.getCname();
            String iprocode = copydata.getProcode();
            String iifdata = copydata.getIfdata();
            String istate = copydata.getState();
            String isort = copydata.getSort();
            String istartpeiod = copydata.getStartperiod();
            String idelayday = copydata.getDelayday();
            String iplanperiod = copydata.getPlanperiod();
            String iplantime = copydata.getPlantime();
            String createtime = copydata.getCreatetime().substring(0,copydata.getCreatetime().length()-2);
            String createuser = copydata.getCreateuser();
            //java.sql.Timestamp updatetime = new java.sql.Timestamp(now.getTime());
            //Object up = new Timestamp(new Date().getTime());
            String updatetime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String remark = copydata.getRemark();

            dataQuery.executeSql(sql1,new Object[]{icode,icname,iprocode,iifdata,istate,isort,istartpeiod,idelayday,iplanperiod,iplantime,createuser,createtime,updatetime,remark});

            //复制模型
            String sql2 = "select * from tb_coindex_module where indexcode=?";
            DataTable table1=dataQuery.getDataTableSql(sql2,new Object[]{cpcode});
            List<DataTableRow> rows1=table1.getRows();
            for(int i=0;i<rows1.size();i++){
                String code= UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,6);
                String cname=rows1.get(i).getString("cname");
                String procode = rows1.get(i).getString("procode");
                //String indexcode = rows1.get(i).getString("indexcode");
                String ifzs = rows1.get(i).getString("ifzs");
                String ifzb = rows1.get(i).getString("ifzb");
                String formula = rows1.get(i).getString("formula");
                String sortcode = rows1.get(i).getString("sortcode");
                String weight = rows1.get(i).getString("weight");
                String dacimal = rows1.get(i).getString("dacimal");
                String copycode = rows1.get(i).getString("copycode");
                String sql3 = "insert into tb_coindex_module (code,cname,procode,indexcode,ifzs,ifzb,formula,sortcode,weight,dacimal,copycode) values(?,?,?,?,?,?,?,?,?,?,?)";
                dataQuery.executeSql(sql3,new Object[]{code,cname,procode,icode,ifzs,ifzb,formula,sortcode,weight,dacimal,copycode});
            }

            //复制方案
            String f_sql1 = "select * from tb_coindex_scheme where indexcode=?";
            DataTable f_table1 = dataQuery.getDataTableSql(f_sql1,new Object[]{cpcode});
            List<DataTableRow> f_row1 = f_table1.getRows();
            for(int j=0;j<f_row1.size();j++){
                String id = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                String code = f_row1.get(j).getString("code");

                String cname = f_row1.get(j).getString("cname");
                String modcode = f_row1.get(j).getString("modcode");
                String state = f_row1.get(j).getString("state");
                String ifzb = f_row1.get(j).getString("ifzb");
                String weight = f_row1.get(j).getString("weight");
                String formula = f_row1.get(j).getString("formula");
                String fremark = f_row1.get(j).getString("remark");
                String f_sql2 = "insert into tb_coindex_scheme (id,code,cname,indexcode,modcode,state,ifzb,weight,formula,remark) values(?,?,?,?,?,?,?,?,?,?)";
                dataQuery.executeSql(f_sql2,new Object[]{id,code,cname,icode,modcode,state,ifzb,weight,formula,fremark});
            }

            //复制筛选条件
            String sql4 = "select * from tb_coindex_zb where indexcode=?";
            DataTable table2 = dataQuery.getDataTableSql(sql4,new Object[]{cpcode});
            List<DataTableRow> row2 = table2.getRows();
            for(int j=0;j<row2.size();j++){
                String code = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                //String incode1 = row2.get(j).getString("indexcode");
                String zbcode = row2.get(j).getString("zbcode");
                String company = row2.get(j).getString("company");
                String datasource = row2.get(j).getString("datasource");
                String regions = row2.get(j).getString("regions");
                String unitcode = row2.get(j).getString("unitcode");
                String dacimal = row2.get(j).getString("dacimal");
                String sql5 = "insert into tb_coindex_zb (code,indexcode,zbcode,company,datasource,regions,unitcode,dacimal) values(?,?,?,?,?,?,?,?)";
                dataQuery.executeSql(sql5,new Object[]{code,icode,zbcode,company,datasource,regions,unitcode,dacimal});
                String modcode =rows1.get(j).getString("code");
                String f_sql3="update tb_coindex_scheme set modcode=? where indexcode=? and modcode=?";
                dataQuery.executeSql(f_sql3,new Object[]{code,icode,modcode});
            }
            //修正tb_coindex_module的procode
           // String newcode = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,6);
            String sql9="select * from tb_coindex_module where indexcode=?";
            DataTable table3=dataQuery.getDataTableSql(sql9,new Object[]{icode});
            List<DataTableRow> rows3=table3.getRows();
            for (int r=0;r<rows3.size();r++){
                String orprocode=rows3.get(r).getString("procode");
                //String copycode=rows3.get(r).getString("copycode");
                String code = rows3.get(r).getString("code");

                if (orprocode!=""){
                    String sql10="select * from tb_coindex_module where copycode=? and indexcode=?";
                    String procode=dataQuery.getDataTableSql(sql10,new Object[]{orprocode,icode}).getRows().get(0).getString("code");
                    //更新这条module的procode
                    String sql11="update tb_coindex_module set procode=? where code=?";
                    dataQuery.executeSql(sql11,new Object[]{procode,code});
                }
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
    //检查
    @Override
    public int checkCode(String code){
        String sql = "select count(*) from tb_coindex_index where code = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(code);
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
    public int checkProcode(String procode){
        String sql = "select count(*) from tb_coindex_index where procode = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(procode);
        DataTable dt = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, params.toArray());
        List<DataTableRow> rows = dt.getRows();
        int getint = rows.get(0).getint(0);
        if (getint > 0) {
            return 0;
        }else {
            return 1;
        }

    }
    //编辑目录
    @Override
    public int updateCategory(String code,IndexList indexList) {
        String sql1 = "update tb_coindex_index set cname=?,procode=?,updatetime=? where code=?";
        List<Object> params = new ArrayList<Object>();
        params.add(indexList.getCname());
        params.add(indexList.getProcode());
        params.add(new Timestamp(new java.util.Date().getTime()));
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
    //启用停用
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
            parms.add(new Timestamp(new Date().getTime()));

        }
        if (sql1.equals("")) {
            return 0;
        }
        sql1 = "update tb_coindex_index set " + sql1.substring(1) + " where code=?";
        parms.add(indexList.getCode());
        return AcmrInputDPFactor.getQuickQuery().executeSql(sql1, parms.toArray());
    }

    @Override
    public DataTable getAllIndexListByPage(String usercode, int page, int pagesize) {
        int b1 = page * pagesize + 1;
        int e1 = b1 + pagesize;
        String sql="select * from (select rownum no,d1.* from (select * from tb_coindex_index where createuser=?  order by createtime) d1) where no>="+b1+" and no<"+ e1;
        DataTable table=AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{usercode});
        return table;
    }

    @Override
    public DataTable getSubIndexListByPage(String usercode,String code, int page, int pagesize) {
        int b1 = page * pagesize + 1;
        int e1 = b1 + pagesize;
        DataTable table;
        String sql;
        if (code.equals("")){
            sql="select * from (select rownum no,d1.* from (select * from tb_coindex_index where procode is null and createuser=? order by createtime) d1) where no>="+b1+" and no<"+ e1;
            table=AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{usercode});
        }
        else {
            sql="select * from (select rownum no,d1.* from (select * from tb_coindex_index where procode=? and createuser=? order by createtime) d1) where no>="+b1+" and no<"+ e1;
            table=AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{code,usercode});
        }
        return table;
    }

    @Override
    public DataTable getRightListByCreateUser(String usercode) {
        String sql="select * from tb_coindex_right where createuser=?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{usercode});
    }

    @Override
    public DataTable getRightListByDepUserCode(String depusercode) {
        String sql="select * from tb_coindex_right where depusercode=?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{depusercode});
    }

    @Override
    public DataTable getZSMods(String icode) {
        String sql="select * from tb_coindex_module where indexcode=? and ifzs=1";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{icode});
    }

    public DataTable getZBMods(String icode){
        String sql="select * from tb_coindex_module where indexcode=? and ifzs=0";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{icode});
    }

    @Override
    public DataTable getSubMods(String icode,String pcode) {
        String sql="select * from tb_coindex_module where indexcode=? and procode=?";
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{icode,pcode});
    }

    @Override
    public int delShare(String indexcode, String depusercode,String sort) {
        String sql = "delete from tb_coindex_right t where t.indexcode = ? and t.depusercode =? and t.sort=?";
        return AcmrInputDPFactor.getQuickQuery().executeSql(sql,new Object[]{indexcode,depusercode,sort});
    }

    @Override
    public DataTable shareSelectList(int type, String keyword,String userid) {
        //type==0是code,表示关键字是被分享人；1是cname，表示计划名称
        String sql = "select r.*,i.cname,i.sort as isort,u.cname as ucname,d.cname as dcname from tb_coindex_right r ";
        sql +="left join tb_coindex_index i on r.indexcode = i.code ";
        sql +="left join tb_right_user u on r.depusercode = u.userid ";
        sql +="left join tb_right_department d on r.depusercode = d.code ";
            if(type ==0){
                if(keyword.equals("")){
                    sql +="where r.createuser = ?";
                    return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{userid});
                }
                else {
                    sql +="where r.createuser = ? and (lower(u.cname) like ? or lower(d.cname) like ?)";
                    return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{userid,"%"+keyword+"%","%"+keyword+"%"});
                }
            }
            else {
                if(keyword.equals("")){
                sql +="where r.createuser = ?";
                return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{userid});
                }
                else {
                    sql +="where r.createuser = ? and lower(i.cname) like ?";
                    return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{userid,"%"+keyword+"%"});
                }
            }
    }

    @Override
    public DataTable shareSelectListByPage(int type, String keyword,String userid,int page,int pagesize) {
        //type==0是code,表示关键字是被分享人；1是cname，表示计划名称
        int b1 = page * pagesize + 1;
        int e1 = b1 + pagesize;
       String sql = "select r.*,i.cname,i.sort as isort,u.cname as ucname,d.cname as dcname from tb_coindex_right r ";
        sql +="left join tb_coindex_index i on r.indexcode = i.code ";
        sql +="left join tb_right_user u on r.depusercode = u.userid ";
        sql +="left join tb_right_department d on r.depusercode = d.code ";
        if(type ==0){
            if(keyword.equals("")){
                sql +="where r.createuser = ?";
                sql="select * from (select rownum no,d1.* from ("+sql+") d1) where no>="+b1+" and no<"+ e1;
                return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{userid});
            }
            else {
                sql +="where r.createuser = ? and (lower(u.cname) like ? or lower(d.cname) like ?)";
                sql="select * from (select rownum no,d1.* from ("+sql+") d1) where no>="+b1+" and no<"+ e1;
                return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql,new Object[]{userid,"%"+keyword+"%","%"+keyword+"%"});
            }
        }
        else {
            if(keyword.equals("")) {
                sql += "where r.createuser = ?";
                sql = "select * from (select rownum no,d1.* from (" + sql + ") d1) where no>=" + b1 + " and no<" + e1;
                return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{userid});
            }
            else {
                sql += "where r.createuser = ? and lower(i.cname) like ?";
                sql = "select * from (select rownum no,d1.* from (" + sql + ") d1) where no>=" + b1 + " and no<" + e1;
                return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{userid, "%" + keyword + "%"});
            }
        }

    }

    @Override
    public DataTable receiveSelectList(int type, String keyword, String depusercode,String sort) {
        //type==0是code,表示关键字是分享人；1是cname，表示计划名称
        //sort=2代表用户，sort=1代表组织
        String sql = "select r.right,i.*,u.cname as ucname from tb_coindex_right r ";
        sql += "left join tb_coindex_index i on r.indexcode = i.code ";
        sql += "left join tb_right_user u on r.createuser = u.userid ";
        if (type == 0) {
            sql += "where r.depusercode=? and r.sort=? and lower(u.cname) like ? ";
        } else if (type == 1) {
            sql += "where  r.depusercode=? and r.sort=? and lower(i.cname) like ?";
        }
        return AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[]{depusercode, sort, "%" + keyword + "%"});
    }
    public int updateTime(String plantime, String planperiod, String icode) {
        String sql="update tb_coindex_index set plantime=to_date(?,'yyyy-mm-dd hh24:mi:ss'),planperiod=? where code=?";
        AcmrInputDPFactor.getQuickQuery().executeSql(sql,new Object[]{plantime,planperiod,icode});
        return 0;
    }

   /* public static void main(String[] args) {

        List<DataTableRow> datas = IndexListDao.Fator.getInstance().getIndexdatadao().receiveSelectList(1,"李","04","1").getRows();
        System.out.println(datas.get(0).getString("ucname"));
    }*/
}