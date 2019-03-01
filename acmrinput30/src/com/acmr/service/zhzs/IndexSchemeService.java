package com.acmr.service.zhzs;

import acmr.util.DataTableRow;
import com.acmr.dao.oracle.zhzs.OraSchemeDaoImpl;
import com.acmr.dao.zhzs.ISchemeDao;
import com.acmr.dao.zhzs.SchemeDao;
import com.acmr.model.zhzs.Scheme;

import java.util.ArrayList;
import java.util.List;

public class IndexSchemeService {
    private ISchemeDao ischemeDao=SchemeDao.Fator.getInstance().getIndexdatadao();
    /**
    * @Description: 根据指标code返回该指标所有的方案列表，并局部刷新table
    * @Param: [icode]
    * @return: java.util.List<com.acmr.model.zhzs.Scheme>
    * @Author: lyh
    * @Date: 2019/1/16
    */
    public List<Scheme> getSchemesByIcode(String icode) {
        List<Scheme> schemes=new ArrayList<>();
        List<DataTableRow> rows=ischemeDao.getSchemesByIcode(icode);
        if (rows.size()==0) return null;
        for (DataTableRow row:rows){
            String id=row.getString("id");
            String code=row.getString("code");
            String cname=row.getString("cname");
            String indexcode=icode;
            String modcode=row.getString("modcode");
            String state=row.getString("state");
            String ifzb=row.getString("ifzb");
            String weight=row.getString("weight");
            String formula=row.getString("formula");
            String remark=row.getString("remark");
            Scheme scheme=new Scheme(id,code,cname,indexcode,modcode,state,ifzb,weight,formula,remark);
            schemes.add(scheme);
        }
        return schemes;
    }

    public String getModSchemeWeight(String scode,String modcode){
        return ischemeDao.getModSchemeWeight(scode,modcode);
    }
    public int checkSchname(String icode,String cname){
        return ischemeDao.checkCname(icode,cname);
    }
    public int addSch(Scheme scheme,List<DataTableRow> rows){
        return ischemeDao.insertSch(scheme,rows);
    }
}
