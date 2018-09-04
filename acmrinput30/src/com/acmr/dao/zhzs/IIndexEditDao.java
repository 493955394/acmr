package com.acmr.dao.zhzs;

import acmr.util.DataTable;


public interface IIndexEditDao {
    public DataTable getZBSbyIndexCode(String icode);
    public DataTable getSubModsbyCode(String code,String icode);
}
