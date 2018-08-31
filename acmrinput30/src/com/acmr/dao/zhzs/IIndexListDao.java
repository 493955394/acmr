package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import com.acmr.model.zhzs.IndexCategory;
import com.acmr.model.zhzs.IndexList;

public interface IIndexListDao {
    public String getName();
    public DataTable getByUser(String usercode);
    public DataTable getByCode(String code);
    public DataTable getLikeCode(String code);
    public DataTable getLikeCname(String cname);
    public int addIndexlist(IndexList indexList);
    public int addNplan(IndexList indexList,String code);
    public int delIndexcp(String code);
}
