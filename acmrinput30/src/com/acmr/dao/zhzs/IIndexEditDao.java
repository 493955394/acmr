package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import com.acmr.model.zhzs.IndexList;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.model.zhzs.IndexZb;
import com.acmr.model.zhzs.Scheme;

import java.awt.*;
import java.util.ArrayList;


public interface IIndexEditDao {
    public DataTable getZBSbyIndexCode(String icode);
    public DataTable getSubModsbyCode(String code,String icode);
    public int deleteMod(String code);
    public void setSort(String code,int sort);
    public DataTable getLikeCode(String code,String icode);
    public DataTable getLikeCname(String cname,String icode);
    public int addZS(IndexMoudle indexMoudle, ArrayList<Scheme> scodes);
    public DataTable getDataByCode(String code);
    public DataTable getZSList(String icode);
    public DataTable getCurrentSort(String procode,String icode);
    public int updateModel(IndexMoudle indexMoudle);
    public boolean checkCode (String code);
    public boolean checkProcode(String procode,String indexcode);
    public boolean checkModule(String code,String icode);
    public int toSaveAll(String indexcode,IndexList indexList);
    public DataTable getZBData(String code);
    public boolean saveCheckCname(String icode,String cname,String ifzs);
    public boolean updCheckCname(String icode,String cname,String ifzs,String code);
    public int toSaveRange(String indexcode, ArrayList<IndexZb> indexzb, IndexList indexList);
}
