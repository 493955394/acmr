package com.acmr.model.zhzs;

import acmr.util.PubInfo;
import com.acmr.service.zhzs.IndexEditService;

import java.util.ArrayList;
import java.util.List;

public class IndexMoudle {
    private String code;
    private  String cname;
    private String indexcode;//TB_COINDEX_INDEX的CODE
    private String procode;//指标树形级别
    private String ifzs;//节点类别，1表示指数0指标
    private String ifzb;//是否是指标，0公式，1指标
    private String formula;//公式，包含指标编号和公式，指标编号为TB_COINDEX_ZB的CODE
    private String sortcode;//上下移动排序，同级别的节点从0递增，0表示第一
    private String weight;//权重
    private String dacimal;//小数点位数

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getIndexcode() {
        return indexcode;
    }

    public void setIndexcode(String indexcode) {
        this.indexcode = indexcode;
    }

    public String getProcode() {
        return procode;
    }

    public void setProcode(String procode) {
        this.procode = procode;
    }

    public String getIfzs() {
        return ifzs;
    }

    public void setIfzs(String ifzs) {
        this.ifzs = ifzs;
    }

    public String getIfzb() {
        return ifzb;
    }

    public void setIfzb(String ifzb) {
        this.ifzb = ifzb;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getSortcode() {
        return sortcode;
    }

    public void setSortcode(String sortcode) {
        this.sortcode = sortcode;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDacimal() {
        return dacimal;
    }

    public void setDacimal(String dacimal) {
        this.dacimal = dacimal;
    }

    public boolean hasChild(){
        IndexEditService indexEditService=new IndexEditService();
        if (indexEditService.getSubMod(this.code,this.indexcode).size()>0){
            return true;
        }
        else return false;
    }

    public int ZBnums(){
        if (this.ifzs.equals("1")){
            //PubInfo.printStr("====================1");
            int nums=0;
            IndexEditService indexEditService=new IndexEditService();
            List<IndexMoudle> mods=indexEditService.getAllMods(this.code,this.indexcode);
            for (int i=0;i<mods.size();i++){
                if (mods.get(i).getIfzs().equals("0")){
                    nums=nums+1;
                }
            }/*
            if (mods.size()==0){
                nums=1;
            }*/
            PubInfo.printStr("nums:"+nums);
            return nums;
        }
        else {
            //PubInfo.printStr("========================0");
            return 1;
        }
    }

    public List<IndexMoudle> getChilds(){
        IndexEditService indexEditService=new IndexEditService();
        List<IndexMoudle> childs=indexEditService.getSubMod(this.code,this.indexcode);
        return childs;
    }
}
