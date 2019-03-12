package com.acmr.service.zhzs;

import acmr.util.DataTableRow;
import com.acmr.dao.oracle.zhzs.OraSchemeDaoImpl;
import com.acmr.dao.zhzs.ISchemeDao;
import com.acmr.dao.zhzs.IndexEditDao;
import com.acmr.dao.zhzs.SchemeDao;
import com.acmr.dao.zhzs.WeightEditDao;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.model.zhzs.Scheme;

import java.util.*;

public class IndexSchemeService {
    private ISchemeDao ischemeDao=SchemeDao.Fator.getInstance().getIndexdatadao();
    /**
    * @Description: 根据指标code返回该指标所有的方案列表(去重)
    * @Param: [icode]
    * @return: java.util.List<com.acmr.model.zhzs.Scheme>
    * @Author: lyh
    * @Date: 2019/1/16
    */
    public List<Scheme> getSchemeByIcode(String icode) {
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


    /**
    * @Description:  根据指标code返回该指标所有的方案列表
    * @Param: [icode]
    * @return: java.util.List<com.acmr.model.zhzs.Scheme>
    * @Author: lyh
    * @Date: 2019/3/5
    */
    public List<Scheme> getSchemesByIcode(String icode) {
        List<Scheme> schemes=new ArrayList<>();
        List<DataTableRow> rows=ischemeDao.getSchemesByIcode(icode);
        if (rows.size()==0) return null;
        for (DataTableRow row:rows){
            String code=row.getString("code");
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
        return schemes;
    }

    public boolean checkSchemeState(String scode){
        int state=ischemeDao.getSchemeState(scode);
        if (state==1) return true;
        else return false;
    }

    public String getModSchemeWeight(String scode,String modcode){
        return ischemeDao.getModSchemeWeight(scode,modcode);
    }
    public String getModSchemeFormula(String scode,String modcode){
        return ischemeDao.getModSchemeFormula(scode,modcode);
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

    /**
     * 查询单个方案下某节点的公式
     * @param modcode
     * @param icode
     * @param scode
     * @return
     */
    public IndexMoudle getModData(String modcode, String icode, String scode){
        IndexMoudle mod=new IndexMoudle();
        List<DataTableRow> scheme = SchemeDao.Fator.getInstance().getIndexdatadao().getScMod(modcode,icode,scode).getRows();
        List<DataTableRow> info = IndexEditDao.Fator.getInstance().getIndexdatadao().getDataByCode(modcode).getRows();
        if(scheme.size()>0 && info.size()>0) {
            mod.setCname(info.get(0).getString("cname"));
            mod.setCode(modcode);
            mod.setDacimal(info.get(0).getString("dacimal"));
            mod.setFormula(scheme.get(0).getString("formula"));//替换公式和权重
            mod.setIfzb(info.get(0).getString("ifzb"));
            mod.setIfzs(info.get(0).getString("ifzs"));
            mod.setIndexcode(info.get(0).getString("indexcode"));
            mod.setProcode(info.get(0).getString("procode"));
            mod.setSortcode(info.get(0).getString("sortcode"));
            mod.setWeight(scheme.get(0).getString("weight"));//替换公式和权重
        }
        return  mod;
    }

    public int updtoModel(Scheme sc,String modcode,String scode){
        //检查这个模型所在的方案是不是处于启用状态
        String state = ischemeDao.getScMod(modcode,sc.getIndexcode(),scode).getRows().get(0).getString("state");
        return ischemeDao.updScheme(sc,modcode,scode,state);
    }
}
