package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import com.acmr.model.zhzs.Data;
import com.acmr.model.zhzs.DataResult;
import com.acmr.model.zhzs.right;

import java.util.List;
import java.util.Map;


public interface IRightDao {
    public DataTable getRightList(String indexcode);
    public DataTable searchDepName(String keyword);
    public DataTable searchUserName(String keyword);
    public int saveRightList(String icode,List<Map<String,String>> list);
}
