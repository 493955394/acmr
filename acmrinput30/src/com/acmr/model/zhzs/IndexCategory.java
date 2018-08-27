package com.acmr.model.zhzs;

public class IndexCategory {
    private String code;
    /** 目录编码 */
    private String cname;
    /** 目录名称 */
    private String procode;
    /** 目录所属目录编码 */
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

    public String getProcode() {
        return procode;
    }

    public void setProcode(String procode) {
        this.procode = procode;
    }
}
