package com.acmr.service.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.dao.zhzs.IndexTaskDao;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.model.zhzs.IndexTask;
import com.acmr.model.zhzs.TaskModule;
import com.acmr.model.zhzs.TaskZb;
import com.acmr.service.zbdata.OriginService;
import com.acmr.web.jsp.Index;
import acmr.util.ListHashMap;

import java.util.*;

public class IndexTaskService {
    public boolean findSession(String sessionid,String taskcode){
        Boolean bool= IndexTaskDao.Fator.getInstance().getIndexdatadao().hasData(sessionid,taskcode);
        return bool;
    }
    /**
     * 重新读取数据
     * @author wf
     * @date
     * @param
     * @return
     */
    public ArrayList<TaskZb> getTaskzb(String taskcode){
        ArrayList<TaskZb> taskZbs = new ArrayList<>();
        List<DataTableRow> data = IndexTaskDao.Fator.getInstance().getIndexdatadao().getTaskZb(taskcode).getRows();
        for (int i = 0; i <data.size() ; i++) {
            TaskZb taskZb = new TaskZb();
            taskZb.setCode(data.get(i).getString("code"));
            taskZb.setTaskcode(data.get(i).getString("taskcode"));
            taskZb.setZbcode(data.get(i).getString("zbcode"));
            taskZb.setCompany(data.get(i).getString("company"));
            taskZb.setDatasource(data.get(i).getString("datasource"));
            taskZb.setRegions(data.get(i).getString("regions"));
            taskZb.setDatatimes(data.get(i).getString("datatimes"));
            taskZb.setUnitcode(data.get(i).getString("unitcode"));
            taskZb.setDacimal(data.get(i).getString("dacimal"));
            taskZb.setProcode(data.get(i).getString("procode"));
            taskZbs.add(taskZb);
        }
        return taskZbs;
    }
    /**
     * 上传文件
     * @author wf
     * @date
     * @param
     * @return
     */
    public int updateData(String taskcode,String ayearmon,String sessionid,List<List<String>> zbandreg){
        return IndexTaskDao.Fator.getInstance().getIndexdatadao().updateDataTmp(taskcode,ayearmon,sessionid,zbandreg);
    }
    /*public List<String> origionZBcodes(String taskcode){
        List<String> ZBcodes=new ArrayList<>();
        List<DataTableRow> rows=IndexTaskDao.Fator.getInstance().getIndexdatadao().getZBs(taskcode).getRows();
        for (int i=0;i<rows.size();i++){
            ZBcodes.add(rows.get(i).getString("code"));
        }
        return ZBcodes;
    }*/
    /**
     * 任务列表展示
     */
    public ArrayList<IndexTask> getTaskByIcode(String icode){
        ArrayList<IndexTask> indexTasks = new ArrayList<>();
        List<DataTableRow> data = IndexTaskDao.Fator.getInstance().getIndexdatadao().getTaskList(icode).getRows();
        for (int i = 0; i <data.size() ; i++) {
            IndexTask task = new IndexTask();
            task.setCode(data.get(i).getString("code"));
            task.setAyearmon(data.get(i).getString("ayearmon"));
            task.setIndexcode(data.get(i).getString("indexcode"));
            task.setCreatetime(data.get(i).getDate("createtime"));
            task.setUpdatetime(data.get(i).getDate("updatetime"));
            indexTasks.add(task);
        }
        return indexTasks;
    }
    /**
     * 任务列表按照任务时间期查询
     */
    public ArrayList<IndexTask> findByTime(String time,String icode){
        DataTable data = IndexTaskDao.Fator.getInstance().getIndexdatadao().findTask(icode,time);
        ArrayList<IndexTask> tasklist = new ArrayList<IndexTask>();
        if(time.equals("")){
            return tasklist;
        }
        for (int i = 0; i <data.getRows().size() ; i++) {
            IndexTask task = new IndexTask();
                task.setCode(data.getRows().get(i).getString("code"));
                task.setAyearmon(data.getRows().get(i).getString("ayearmon"));
                task.setIndexcode(data.getRows().get(i).getString("indexcode"));
                task.setCreatetime(data.getRows().get(i).getDate("createtime"));
                task.setUpdatetime(data.getRows().get(i).getDate("updatetime"));
                tasklist.add(task);
        }
        return tasklist;
    }
    /**
     * 指数任务删除
     */
    public int delTask(String code){
        return IndexTaskDao.Fator.getInstance().getIndexdatadao().delTask(code);
    }
    
    /** 
    * @Description: 返回任务的地区list  
    * @Param: [taskcode] 
    * @return: java.util.List<java.lang.String> 
    * @Author: lyh
    * @Date: 2018/9/14 
    */ 
    public List<String> getTaskRegs(String taskcode){
        List<String> regs=new ArrayList<>();
        List<DataTableRow> rows=IndexTaskDao.Fator.getInstance().getIndexdatadao().getZBs(taskcode).getRows();
        String tregs=rows.get(0).getString("regions");
        regs= Arrays.asList(tregs.split(","));
        return regs;
    }
    
    /** 
    * @Description: 返回任务的指标code列表 
    * @Param: [taskcode] 
    * @return: java.util.List<java.lang.String> 
    * @Author: lyh
    * @Date: 2018/9/14 
    */ 
    public List<String> getZBcodes(String taskcode){
        List<String> ZBcodes=new ArrayList<>();
        List<DataTableRow> rows=IndexTaskDao.Fator.getInstance().getIndexdatadao().getZBs(taskcode).getRows();
        for (int i=0;i<rows.size();i++){
            ZBcodes.add(rows.get(i).getString("code"));
        }
        return ZBcodes;
    }

    /**
    * @Description: 从原始data表中返回数据
    * @Param: [taskcode, region, zbcode, ayearmon]
    * @return: java.lang.String
    * @Author: lyh
    * @Date: 2018/9/14
    */
    public String getData(Boolean istmp,String taskcode,String region,String zbcode,String ayearmon,String sessionid){
        if (istmp){
            return IndexTaskDao.Fator.getInstance().getIndexdatadao().getTmpData(taskcode,region,zbcode,ayearmon,sessionid);
        }
        else {
            return IndexTaskDao.Fator.getInstance().getIndexdatadao().getData(taskcode,region,zbcode,ayearmon);
        }
    }

    /**
    * @Description: 返回任务的时间期
    * @Param: [taskcode]
    * @return: java.lang.String
    * @Author: lyh
    * @Date: 2018/9/14
    */
    public String getTime(String taskcode){
        String ayearmon=IndexTaskDao.Fator.getInstance().getIndexdatadao().getTime(taskcode);
        return ayearmon;
    }

    /**
    * @Description: 获取指标的名称
    * @Param: [code]
    * @return: java.lang.String
    * @Author: lyh
    * @Date: 2018/9/14
    */
    public String getzbname(String ZBcode){
        String zbcode=IndexTaskDao.Fator.getInstance().getIndexdatadao().getzbcode(ZBcode);
        OriginService originService=new OriginService();
        String zbname=originService.getwdnode("zb",zbcode).getName();
        return zbname;
    }
    /*
    获取对应任务的所有list
     */
    public List<TaskModule> getModuleFormula(String taskcode,String ifzs){
        List<TaskModule> taskModules = new ArrayList<>();
        List<DataTableRow> data = IndexTaskDao.Fator.getInstance().getIndexdatadao().getModuleData(taskcode,ifzs).getRows();

        return taskModules;
    }
    /**
     * 获取地区
     * @param taskcode
     * @return
     */
    public String getRegions (String taskcode){
        String Regions="";
        List<DataTableRow> rows=IndexTaskDao.Fator.getInstance().getIndexdatadao().getZBs(taskcode).getRows();
        if(rows.get(0).getString("code")!= "")
            Regions = rows.get(0).getString("code");
        return Regions;
    }
    /**
     * 返回任务的icode
     */
    public String geticode(String taskcode){
        String icode=IndexTaskDao.Fator.getInstance().getIndexdatadao().getIcode(taskcode);
        return icode;
    }

    /*public static void main(String[] args) {
        IndexTaskService indexTask = new IndexTaskService();
        List<Map> test = indexTask.getModuleFormula("bb4016ee54d143cbb2d9f47d4b221e9b");
        for (int i = 0; i <test.size() ; i++) {
            System.out.println(test.get(i).toString()+"====test");
        }
    }*/
}

