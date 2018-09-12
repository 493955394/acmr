package com.acmr.dao.zhzs;

public interface IIndexTaskDao {
    public boolean hasTask(String indexcode,String ayearmon);
    public int create(String indexcode,String tcode,String ayearmon,String createtime);
}
