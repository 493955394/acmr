package com.acmr.dao.zhzs;

import acmr.util.DataTable;

public interface IIndexListDao {
    public String getName();
    public DataTable getByUser(String usercode);
    public DataTable getByCode(String code);
}
