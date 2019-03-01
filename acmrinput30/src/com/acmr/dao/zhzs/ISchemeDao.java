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
    public int checkCname(String icode,String cname);
    public int insertSch(Scheme scheme, List<DataTableRow> rows);
    public int delSch(String icode);
}
