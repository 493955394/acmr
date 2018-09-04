package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import com.acmr.model.zhzs.IndexMoudle;


public interface IIndexEditDao {
    public DataTable getZBSbyIndexCode(String icode);
    public DataTable getSubModsbyCode(String code,String icode);
    public int addZS(IndexMoudle indexMoudle);
}
