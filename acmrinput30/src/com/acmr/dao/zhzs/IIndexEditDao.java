package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import com.acmr.model.zhzs.IndexMoudle;


public interface IIndexEditDao {
    public DataTable getZBSbyIndexCode(String icode);
    public DataTable getSubModsbyCode(String code,String icode);
    public int deleteMod(String code);
    public void setSort(String code,int sort);
    public DataTable getLikeCode(String code);
    public DataTable getLikeCname(String cname);
    public int addZS(IndexMoudle indexMoudle);
    public DataTable getDataByCode(String code,String icode);
}
