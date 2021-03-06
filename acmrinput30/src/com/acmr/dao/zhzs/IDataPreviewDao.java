package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import com.acmr.model.zhzs.DataPreview;
import com.acmr.model.zhzs.Scheme;

import java.util.List;

/**
 * @author:djj
 * @date: 2018/12/5 10:49/**
 * @author:djj
 * @date: 2018/12/5 10:49
 */
public interface IDataPreviewDao {
    public DataTable getModuleData(String icode, String ifzs);
    public int addDataResult(List<DataPreview> dataResults);
    public DataTable getSubMod(String code);
    public int subDataCheck(String modcode, String reg, String time, String scode);
    public DataTable getData(String modcode,String region,String time,String scode);
    public int addZSData (DataPreview dataResult);
    public DataTable getRootData (String icode);
    public String findRegions(String icode);
 //   public DataTable getAllData(String icode);
}
