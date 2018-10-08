package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import com.acmr.model.zhzs.DataResult;
import com.acmr.model.zhzs.right;

import java.util.List;


public interface IRightDao {
    public DataTable getRightList(String indexcode);
}
