package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import com.acmr.model.zhzs.IndexList;

import javax.xml.crypto.Data;

public interface IIndexListDao {
    public DataTable getSubLists(String code,String usercode);
    public String getName();
    public DataTable getByUser(String usercode);
    public DataTable getByCode(String code);
    public DataTable getLikeCode(String code);
    public DataTable getLikeCname(String cname);
    public int addIndexlist(IndexList indexList);
    public int addNplan(IndexList indexList,String code);
    public int delIndexcp(String code);
    public int updateCategory(String code,String procode);
    public int updateCp(IndexList indexList);
}
