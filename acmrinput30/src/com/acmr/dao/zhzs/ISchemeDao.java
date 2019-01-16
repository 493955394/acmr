package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;

import java.util.List;

public interface ISchemeDao {
    public String getSelectedSchemeCode(String indexcode);
    public String getSchemeNameByCode(String code);
    public List<DataTableRow> getSchemesByIcode(String icode);
}
