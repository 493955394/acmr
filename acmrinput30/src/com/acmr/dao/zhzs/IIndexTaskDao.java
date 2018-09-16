package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;

public interface IIndexTaskDao {
    public boolean hasTask(String indexcode,String ayearmon);
    public int create(String indexcode,String tcode,String ayearmon,String createtime);
    public DataTable getTaskList(String icode);
    public boolean hasData(String sessionid,String taskcode);
    public DataTable findTask(String icode,String time);
    public int delTask(String code);
    public DataTable getZBs(String taskcode);
    public String getData(String taskcode,String region,String zbcode,String ayearmon);
    public String getTime(String taskcode);
    public String getzbcode(String ZBcode);
    public int ReData(String tcode);
    public DataTable getTaskZb(String taskcode);
    public DataTable getModuleData(String taskcode);
    public String getIcode(String taskcode);
}
