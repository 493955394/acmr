package com.acmr.model.zhzs;


import com.acmr.model.security.User;

public class IndexZb {

    private String code;
    /** 计划编码 */
    private String indexcode;
    /** 计划编码 */
    private String zbcode;
    /** 指标编码 */
    private String company;
    /** 主体 */
    private String datasource;
    /** 数据来源 */
    private String regions;
    /** 地区 */
    private String datatimes;
    /** 数据时间 */
    private String unitcode;
    /** 单位code */
    private String dacimal;
    /** 小数点位数 */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setIndexcode(String indexcode) {
        this.indexcode = indexcode;
    }

    public void setZbcode(String zbcode) {
        this.zbcode = zbcode;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public void setRegions(String regions) {
        this.regions = regions;
    }

    public void setDatatimes(String datatimes) {
        this.datatimes = datatimes;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    public void setDacimal(String dacimal) {
        this.dacimal = dacimal;
    }

    public String getIndexcode() {
        return indexcode;
    }

    public String getZbcode() {
        return zbcode;
    }

    public String getCompany() {
        return company;
    }

    public String getDatasource() {
        return datasource;
    }

    public String getRegions() {
        return regions;
    }

    public String getDatatimes() {
        return datatimes;
    }

    public String getUnitcode() {
        return unitcode;
    }

    public String getDacimal() {
        return dacimal;
    }
}

