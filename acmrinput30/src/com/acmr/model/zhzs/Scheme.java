package com.acmr.model.zhzs;

public class Scheme {
    private String id;
    private String code;
    private String cname;
    private String indexcode;
    private String modcode;
    private String state;
    private String ifzb;
    private String weight;
    private String formula;
    private String remark;
    public Scheme(){

    }
    public Scheme(String id,String code,String cname,String indexcode,String modcode,String state,String ifzb,String weight,String formula,String remark){
        this.id=id;
        this.code=code;
        this.cname=cname;
        this.indexcode=indexcode;
        this.modcode=modcode;
        this.state=state;
        this.ifzb=ifzb;
        this.weight=weight;
        this.formula=formula;
        this.remark=remark;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getModcode() {
        return modcode;
    }

    public void setModcode(String modcode) {
        this.modcode = modcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIfzb() {
        return ifzb;
    }

    public void setIfzb(String ifzb) {
        this.ifzb = ifzb;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
