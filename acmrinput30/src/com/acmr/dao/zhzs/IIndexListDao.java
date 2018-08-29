package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import com.acmr.model.zhzs.IndexCategory;
import com.acmr.model.zhzs.IndexList;

public interface IIndexListDao {
    public String getName();
    public DataTable getByUser(String usercode);
    public DataTable getByCode(String code);
    public int addCatagory(IndexList indexList,String code,String cname,String procode,String ifdata1);
    public int addIndexlist(IndexList indexList);
}
