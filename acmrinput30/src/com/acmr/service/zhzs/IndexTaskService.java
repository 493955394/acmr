package com.acmr.service.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.dao.zhzs.IndexTaskDao;
import com.acmr.model.zhzs.*;
import com.acmr.service.zbdata.OriginService;
import com.acmr.web.jsp.Index;
import acmr.util.ListHashMap;
import org.apache.poi.ss.formula.functions.T;

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
    public int updateData(String taskcode,String ayearmon,String sessionid,List<String> regscode,List zbandreg){
        return IndexTaskDao.Fator.getInstance().getIndexdatadao().updateDataTmp(taskcode,ayearmon,sessionid,regscode,zbandreg);
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
     * 任务列表展示（包括分页）
     */
    public ArrayList<IndexTask> getTaskByIcode(String icode,int page,int pagesize){
        ArrayList<IndexTask> indexTasks = new ArrayList<>();
        List<DataTableRow> data = IndexTaskDao.Fator.getInstance().getIndexdatadao().getTaskListByPage(icode,page,pagesize).getRows();
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
    * @Description: 返回任务的时间期
    * @Param: [tcode]
    * @return: java.lang.String
    * @Author: lyh
    * @Date: 2018/10/17
    */
    public String getTaskAyearmon(String tcode){
        DataTableRow row=IndexTaskDao.Fator.getInstance().getIndexdatadao().getTask(tcode).getRows().get(0);
        return row.getString("ayearmon");
    }

    /**
    * @Description: 获取全部任务列表，不包括分页
    * @Param: [icode]
    * @return: java.util.List<com.acmr.model.zhzs.IndexTask>
    * @Author: lyh
    * @Date: 2018/9/19
    */
    public List<IndexTask> getAllTask(String icode){
        ArrayList<IndexTask> indexTasks = new ArrayList<>();
        List<DataTableRow> data = IndexTaskDao.Fator.getInstance().getIndexdatadao().getAllTaskList(icode).getRows();
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
     * 任务列表按照任务时间期查询,加上分页
     */
    public ArrayList<IndexTask> findByTime(String time,String icode,int page,int pagesize){
        DataTable data = IndexTaskDao.Fator.getInstance().getIndexdatadao().findTaskByPage(icode,time,page,pagesize);
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
    * @Param: [code,icode]
    * @return: java.lang.String
    * @Author: lyh
    * @Date: 2018/9/14
    */
    public String getzbname(String ZBcode,String icode){
        String zbcode=IndexTaskDao.Fator.getInstance().getIndexdatadao().getzbcode(ZBcode);
        OriginService originService=new OriginService();
        String dbcode=IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        String zbname=originService.getwdnode("zb",zbcode,dbcode).getName();
        return zbname;
    }
    /*
    获取对应任务的指数或者指标的list
     */
    public List<TaskModule> getModuleFormula(String taskcode,String ifzs){
        List<TaskModule> taskModules = new ArrayList<>();
        List<DataTableRow> data = IndexTaskDao.Fator.getInstance().getIndexdatadao().getModuleData(taskcode,ifzs).getRows();
        for (int i = 0; i <data.size(); i++) {
            TaskModule taskModule = new TaskModule();
            taskModule.setCode(data.get(i).getString("code"));
            taskModule.setCname(data.get(i).getString("cname"));
            taskModule.setTaskcode(data.get(i).getString("taskcode"));
            taskModule.setProcode(data.get(i).getString("procode"));
            taskModule.setIfzs(data.get(i).getString("ifzs"));
            taskModule.setIfzb(data.get(i).getString("ifzb"));
            taskModule.setFormula(data.get(i).getString("formula"));
            taskModule.setWeight(data.get(i).getString("weight"));
            taskModule.setSortcode(data.get(i).getString("sortcode"));
            taskModule.setDacimal(data.get(i).getString("dacimal"));
            taskModule.setOrcode(data.get(i).getString("orcode"));
            taskModules.add(taskModule);
        }

        return taskModules;
    }
    /**
     * 获取地区
     * @param taskcode
     * @return
     */
    public String findRegions (String taskcode){
        String Regions="";
        List<DataTableRow> rows=IndexTaskDao.Fator.getInstance().getIndexdatadao().getZBs(taskcode).getRows();
        if(rows.get(0).getString("regions")!= "")
            Regions = rows.get(0).getString("regions");
        return Regions;
    }
    /**
     * 返回任务的zb的数据
     */
    public List<TaskZb> findtaskzb(String taskcode){
        List<TaskZb> taskZbs = new ArrayList<>();
        List<DataTableRow> data=IndexTaskDao.Fator.getInstance().getIndexdatadao().getTaskZBList(taskcode).getRows();
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
     * 返回临时表中总指数列表
     */
    public List<TaskModule> findRoot(String taskcode){
        List<TaskModule> taskModules = new ArrayList<>();
        List<DataTableRow> data = IndexTaskDao.Fator.getInstance().getIndexdatadao().getRootData(taskcode).getRows();
        for (int i = 0; i <data.size() ; i++) {
            TaskModule taskModule = new TaskModule();
            taskModule.setCode(data.get(i).getString("code"));
            taskModule.setCname(data.get(i).getString("cname"));
            taskModule.setTaskcode(data.get(i).getString("taskcode"));
            taskModule.setProcode(data.get(i).getString("procode"));
            taskModule.setIfzs(data.get(i).getString("ifzs"));
            taskModule.setIfzb(data.get(i).getString("ifzb"));
            taskModule.setFormula(data.get(i).getString("formula"));
            taskModule.setWeight(data.get(i).getString("weight"));
            taskModule.setSortcode(data.get(i).getString("sortcode"));
            taskModule.setDacimal(data.get(i).getString("dacimal"));
            taskModule.setOrcode(data.get(i).getString("orcode"));
            taskModules.add(taskModule);
        }
        return taskModules;
    }
    /**
     * 返回总指数列表
     * @author wf
     * @date
     * @param []
     * @return
     */
    public List<TaskModule> findModRoot(String taskcode){
        List<TaskModule> taskModules = new ArrayList<>();
        List<DataTableRow> data = IndexTaskDao.Fator.getInstance().getIndexdatadao().getAllRootData(taskcode).getRows();
        for (int i = 0; i <data.size() ; i++) {
            TaskModule taskModule = new TaskModule();
            taskModule.setCode(data.get(i).getString("code"));
            taskModule.setCname(data.get(i).getString("cname"));
            taskModule.setTaskcode(data.get(i).getString("taskcode"));
            taskModule.setProcode(data.get(i).getString("procode"));
            taskModule.setIfzs(data.get(i).getString("ifzs"));
            taskModule.setIfzb(data.get(i).getString("ifzb"));
            taskModule.setFormula(data.get(i).getString("formula"));
            taskModule.setWeight(data.get(i).getString("weight"));
            taskModule.setSortcode(data.get(i).getString("sortcode"));
            taskModule.setDacimal(data.get(i).getString("dacimal"));
            taskModule.setOrcode(data.get(i).getString("orcode"));
            taskModules.add(taskModule);
        }
        return taskModules;
    }

    /**
     * 查计划的icode,用于返回指数任务界面
     */
    public String findIcode(String taskcode){ return IndexTaskDao.Fator.getInstance().getIndexdatadao().getIcode(taskcode);}

    /*public static void main(String[] args) {
        IndexTaskService indexTask = new IndexTaskService();
        List<Map> test = indexTask.getModuleFormula("bb4016ee54d143cbb2d9f47d4b221e9b");
        for (int i = 0; i <test.size() ; i++) {
            System.out.println(test.get(i).toString()+"====test");
        }
    }*/
}

