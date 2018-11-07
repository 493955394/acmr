package com.acmr.dao.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.model.zhzs.IndexList;

import javax.xml.crypto.Data;

public interface IIndexListDao {
    public DataTable getStartLists(String date);
    public DataTable getSubLists(String code,String usercode);
    public String getName();
    public DataTable getByUser(String usercode);
    public DataTable getByCode(String code);
    public DataTable getLikeCode(String code,String userid);
    public DataTable getLikeCname(String cname,String userid);
    public DataTable getLikeCodeByPage(String code,String userid,int page,int pagesize);
    public DataTable getLikeCnameByPage(String cname,String userid,int page,int pagesize);
    public int addIndexlist(IndexList indexList);
    //public int addNplan(IndexList indexList,String code);
    public int delIndexcp(String code);
    public int updateCategory(String code,IndexList indexList);
    public int updateCp(IndexList indexList);
    public int checkCode(String code);
    public int checkProcode(String procode);
    public int addCopyplan(String cpcode,IndexList data1);
    public int addCopyShare(String cpcode,IndexList copydata);
    public DataTable getAllIndexListByPage(String usercode,int page, int pagesize);
    public DataTable getSubIndexListByPage(String usercode,String code,int page,int pagesize);
    public DataTable getRightListByCreateUser(String usercode);
    public DataTable getRightListByDepUserCode(String depusercode);
    public DataTable getZSMods(String icode);
    public DataTable getZBMods(String icode);
    public DataTable getSubMods(String icode,String pcode);
    public int delShare(String indexcode,String depusercode,String sort);
    public DataTable shareSelectList(int type,String keyword,String userid);
    public DataTable shareSelectListByPage(int type,String keyword,String userid,int page,int pagesize);
    public DataTable receiveSelectList(int type,String keyword,String depusercode,String sort);
    public int updateTime(String plantime,String planperiod,String icode);
    public int switchFormu(String ncode,String ocode);
    public Boolean hasIndex(String icode,String usercode);
    public String getDbcode(String icode);
    public int checkCname(String usercode,String cname);
}
