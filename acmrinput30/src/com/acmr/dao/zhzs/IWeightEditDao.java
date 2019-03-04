package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import com.acmr.model.zhzs.Scheme;

import java.util.List;

public interface IWeightEditDao {
     public DataTable getModsbyIcode(String icode);
     public int weightset(String code, String weight);
     public int setWeightFormula(List<Scheme> scheme);
     public DataTable getTroos(String taskcode);
     public DataTable getSubTmods(String code);
     public int tWeightUpd (String code, String weight);
     public int ReWeight(String tcode);
}
