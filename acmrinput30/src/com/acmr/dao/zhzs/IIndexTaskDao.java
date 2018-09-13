package com.acmr.dao.zhzs;

import acmr.util.DataTable;

public interface IIndexTaskDao {
    public boolean hasTask(String indexcode,String ayearmon);
    public int create(String indexcode,String tcode,String ayearmon,String createtime);
    public DataTable getTaskList(String icode);
    public boolean hasData(String sessionid,String taskcode);
    public DataTable findTask(String time,String icode);
    public int delTask(String code);
}
