package com.acmr.service.zhzs;

import acmr.util.DataTableRow;
import com.acmr.dao.oracle.zhzs.OraSchemeDaoImpl;
import com.acmr.dao.zhzs.ISchemeDao;
import com.acmr.dao.zhzs.SchemeDao;
import com.acmr.dao.zhzs.WeightEditDao;
import com.acmr.model.zhzs.Scheme;

import java.util.*;

public class IndexSchemeService {
    private ISchemeDao ischemeDao=SchemeDao.Fator.getInstance().getIndexdatadao();
    /**
    * @Description: 根据指标code返回该指标所有的方案列表(去重)，并局部刷新table
    * @Param: [icode]
    * @return: java.util.List<com.acmr.model.zhzs.Scheme>
    * @Author: lyh
    * @Date: 2019/1/16
    */
    public List<Scheme> getSchemesByIcode(String icode) {
        List<Scheme> schemes=new ArrayList<>();
        List<DataTableRow> rows=ischemeDao.getSchemesByIcode(icode);
        if (rows.size()==0) return null;
        Map<String,Integer> map=new HashMap<>();
        for (DataTableRow row:rows){
            String code=row.getString("code");
            if (!map.keySet().contains(code)){
                map.put(code,1);
                String id=row.getString("id");
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
    public int editSch(Scheme scheme){
        return ischemeDao.updateSch(scheme);
    }
    public int delScheme(String code){
        return ischemeDao.delSch(code);
    }
    public List<Scheme> getSchs(String icode,String schcode) {
        List<Scheme> schemes=new ArrayList<>();
        List<DataTableRow> rows=ischemeDao.getSch(icode,schcode);
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
    public int cloneSch(Scheme scheme){

        String icode = scheme.getIndexcode();
        String schcode = scheme.getCode();
        List<DataTableRow> rows=ischemeDao.getSch(icode,schcode);
        String schemecode= UUID.randomUUID().toString().replace("-", "").toLowerCase();
        List<Scheme> schemes=new ArrayList<>();
        for (DataTableRow row:rows){
            String id=row.getString("id");
            String code =schemecode;
            String cname=scheme.getCname();
            String indexcode=icode;
            String modcode=row.getString("modcode");
            String state=scheme.getState();
            String ifzb=row.getString("ifzb");
            String weight=row.getString("weight");
            String formula=row.getString("formula");
            String remark=scheme.getRemark();
            Scheme scheme1=new Scheme(id,code,cname,indexcode,modcode,state,ifzb,weight,formula,remark);
            schemes.add(scheme1);
        }
        return ischemeDao.cloneSch(schemes);
    }
    //所有方案选用后设置其他为未选用
    public List<Scheme> setOnlyStart(String icode,String schcode) {
        List<Scheme> schemes=new ArrayList<>();
        List<DataTableRow> rows=ischemeDao.getSch(icode,schcode);

        for (DataTableRow row:rows){
            if (!row.getString("code").equals(schcode)){
                String id=row.getString("id");
                String code=row.getString("code");
                String cname=row.getString("cname");
                String indexcode=icode;
                String modcode=row.getString("modcode");
                String state="0";
                String ifzb=row.getString("ifzb");
                String weight=row.getString("weight");
                String formula=row.getString("formula");
                String remark=row.getString("remark");
                Scheme scheme=new Scheme(id,code,cname,indexcode,modcode,state,ifzb,weight,formula,remark);
                schemes.add(scheme);
            }

        }
        return schemes;
    }
    public int copyWeightFormula(String icode,String schcode){
        List<Scheme> scheme =getSchs(icode,schcode);
        return WeightEditDao.Fator.getInstance().getIndexdatadao().setWeightFormula(scheme);
    }
    public int updateState(List<Scheme> schemes){
        return ischemeDao.cloneSch(schemes);
    }

    public void setSingleSchemeWeight(String scode, Map<String,String> weightmap){
        for (Map.Entry<String,String> entry:weightmap.entrySet()){
            String modcode=entry.getKey();
            String weight=entry.getValue();
            ischemeDao.setWeight(scode,modcode,weight);
        }
    }
}
