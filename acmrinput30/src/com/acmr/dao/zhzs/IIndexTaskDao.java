package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;

import java.util.List;
import java.util.Map;

public interface IIndexTaskDao {
    public DataTable getTask(String tcode);
    public boolean hasTask(String indexcode,String ayearmon);
    public int create(String indexcode,String tcode,String ayearmon,String createtime);
    public DataTable getTaskListByPage(String icode,int page,int pagesize);
    public DataTable getAllTaskList(String icode);
    public boolean hasData(String sessionid,String taskcode);
    public DataTable findTask(String icode,String time);
    public DataTable findTaskByPage(String icode,String time,int page,int pagesize);
    public int delTask(String code);
    public DataTable getZBs(String taskcode);
    public String getData(String taskcode,String region,String zbcode,String ayearmon);
    public String getTmpData(String taskcode,String region,String zbcode,String ayearmon,String sessionid);
    public String getTime(String taskcode);
    public String getTaskcode(String icode,String ayearmoon);
    public String getzbcode(String ZBcode);
    public int ReData(String tcode,String sessionid);
    public DataTable getTaskZb(String taskcode);
    public DataTable getModuleData(String taskcode,String ifzs);
    public DataTable getTaskZBList(String taskcode);
    public int copyData(String taskcode,String sessionid);
    public int updateDataTmp(String taskcode,String ayearmon,String sessionid,List<String> regscode,List zbandreg);
    public DataTable getRootData(String taskcode);
    public DataTable getAllRootData(String taskcode);
    public DataTable getOrcode(String modcode);

    public String getIcode(String taskcode);
}
