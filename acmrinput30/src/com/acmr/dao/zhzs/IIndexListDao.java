package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import com.acmr.model.zhzs.IndexCategory;

public interface IIndexListDao {
    public String getName();
    public DataTable getByUser(String usercode);
    public DataTable getByCode(String code);
    public int addCatagory(IndexCategory inCata);
}
