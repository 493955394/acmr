package com.acmr.dao.oracle.zhzs;

import acmr.data.DataQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.IIndexTaskDao;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

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

    @Override
    public int create(String indexcode, String tcode, String ayearmon, String createtime) {
        if (indexcode.length()==0){
            return 0;
        }
        DataQuery dataQuery = null;

        try{

            dataQuery = AcmrInputDPFactor.getDataQuery();
            dataQuery.beginTranse();

            //新增任务
            String sql="insert into tb_coindex_task (code,indexcode,ayearmon,createtime) values (?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
            dataQuery.executeSql(sql,new Object[]{tcode,indexcode,ayearmon,createtime});

            //复制模型tb_coindex_task_module,tb_coindex_task_module_tmp
            String sql1="select * from tb_coindex_module where indexcode=?";
            DataTable table=dataQuery.getDataTableSql(sql1,new Object[]{indexcode});
            List<DataTableRow> rows=table.getRows();
            for (int i=0;i<rows.size();i++){
                PubInfo.printStr(rows.get(i).getRows().toString());
                String code=UUID.randomUUID().toString().replace("-", "").toLowerCase();
                String cname=rows.get(i).getString("cname");
                String procode=rows.get(i).getString("procode");
                String ifzs=rows.get(i).getString("ifzs");
                String ifzb=rows.get(i).getString("ifzb");
                String formula=rows.get(i).getString("formula");
                String sortcode=rows.get(i).getString("sortcode");
                String weight=rows.get(i).getString("weight");
                String dacimal=rows.get(i).getString("dacimal");
                String sql2="insert into tb_coindex_task_module (code,cname,taskcode,procode,ifzs,ifzb,formula,sortcode,weight,dacimal) values(?,?,?,?,?,?,?,?,?,?)";
                String sql3="insert into tb_coindex_task_module_tmp (code,cname,taskcode,procode,ifzs,ifzb,formula,sortcode,weight,dacimal) values(?,?,?,?,?,?,?,?,?,?)";
                dataQuery.executeSql(sql2,new Object[]{code,cname,tcode,procode,ifzs,ifzb,formula,sortcode,weight,dacimal});
                dataQuery.executeSql(sql3,new Object[]{code,cname,tcode,procode,ifzs,ifzb,formula,sortcode,weight,dacimal});
            }

            //复制指标tb_coindex_task_zb
            String sql4="select * from tb_coindex_zb where indexcode=?";
            DataTable table1=dataQuery.getDataTableSql(sql4,new Object[]{indexcode});
            List<DataTableRow> rows1=table1.getRows();
            for (int j=0;j<rows1.size();j++){
                PubInfo.printStr(rows1.get(j).getRows().toString());
                String code=UUID.randomUUID().toString().replace("-", "").toLowerCase();
                String zbcode=rows1.get(j).getString("zbcode");
                String company=rows1.get(j).getString("company");
                String datasource=rows1.get(j).getString("datasource");
                String regions=rows1.get(j).getString("regions");
                String datatimes=rows1.get(j).getString("datatimes");
                String unitcode=rows1.get(j).getString("unitcode");
                String dacimal=rows1.get(j).getString("dacimal");
                String sql5="insert into tb_coindex_task_zb (code,taskcode,zbcode,company,datasource,regions,datatimes,unitcode,dacimal) values(?,?,?,?,?,?,?,?,?)";
                dataQuery.executeSql(sql5,new Object[]{code,tcode,zbcode,company,datasource,regions,datatimes,unitcode,dacimal});
            }

            dataQuery.commit();

        }
        catch (SQLException e){
            if (dataQuery != null) {
                dataQuery.rollback();
                e.printStackTrace();
                return 1;
            }
        }
        finally {
            if (dataQuery != null) {
                dataQuery.releaseConnl();
            }
        }

        return 0;

    }

/*    public static void main(String[] args) {
        OraIndexTaskDaoImpl oraIndexTaskDao=new OraIndexTaskDaoImpl();
        oraIndexTaskDao.hasTask("R001","2014");
    }*/
}
