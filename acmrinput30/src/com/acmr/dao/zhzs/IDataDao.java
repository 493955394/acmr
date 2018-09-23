package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import com.acmr.model.zhzs.DataResult;

import java.util.List;


public interface IDataDao {
    public DataTable getData(boolean iftmp,String taskcode,String zbcode,String region,String time,String sessionid);
    public int addDataResult (boolean iftmp,List<DataResult> dataResults);
    public DataTable getSubMod(String code);
    public DataTable getModData(String code);
    public int subDataCheck(boolean iftmp,String taskcode,String modcode,String reg,String time,String sessionid);
    public int addZSData (boolean iftmp,DataResult dataResult);
    public String getDataResult(boolean iftmp,String taskcode,String modcode,String reg,String time,String sessionid);
    public int copyDataResult (String taskcode,String sessionid);
    public int resetPage(String taskcode,String sessionid);
    public int saveResult(String taskcode,String sessionid);
    public DataTable findOldTask(String icode,String ayearmon);
    public String findModCode(String taskcode,String orcode);
}
