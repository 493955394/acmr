package com.acmr.dao.zhzs;

import acmr.util.DataTable;

public interface IIndexListDao {
    public String getName();
    public DataTable getCodeByUser(String usercode);
    public DataTable getByCode(String code);
}
