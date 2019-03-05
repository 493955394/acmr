package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.model.zhzs.Scheme;

import java.util.List;

public interface ISchemeDao {
    public String getSelectedSchemeCode(String indexcode);
    public String getSchemeNameByCode(String code);
    public List<DataTableRow> getSchemesByIcode(String icode);
    public String getModSchemeWeight(String scode,String modcode);
    public String getModSchemeFormula(String scode,String modcode);
    public int checkCname(String icode,String cname);
    public int insertSch(Scheme scheme, List<DataTableRow> rows);
    public int delSch(String icode);
    public int updateSch(Scheme scheme);
    public List<DataTableRow> getSch(String icode,String code);
    public int cloneSch(List<Scheme> scheme);
    public int setWeight(String scode,String modcode,String weight);
    public DataTable getScMod(String modcode,String icode,String scode);
    public int updScheme(Scheme sc,String modcode,String scode );
    public boolean checkScheme (String icode);
    public DataTable getScodes (String icode);
}
