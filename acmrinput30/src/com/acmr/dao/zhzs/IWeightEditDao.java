package com.acmr.dao.zhzs;

import acmr.util.DataTable;

public interface IWeightEditDao {
     public DataTable getModsbyIcode(String icode);
     public int weightset(String code, String weight);
}
