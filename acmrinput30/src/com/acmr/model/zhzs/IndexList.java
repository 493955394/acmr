package com.acmr.model.zhzs;



import acmr.util.PubInfo;

import java.util.Date;

public class IndexList {
    private String code;
    /** 计划编码 */
    private String cname;
    /** 计划名称 */
    private String procode;
    /** 计划所属目录编码 */
    private String ifdata;
    /** 是目录/计划 */
    private String state;
    /** 计划状态 启用/停用 */
    private String sort;
    /** 统计周期 */
    private String startperiod;
    /** 起始数据期 */
    private String delayday;
    /** 自然日 */
    private String planperiod;
    /** 将生成的最新期数 */
    private String plantime;
    /** 将生成的时间 */
    private String createuser;
    /** 创建用户的code */
    private String createtime;
    /** 创建时间 */
    private String updatetime;
    /** 更新时间 */
    private String remark;
    /** 备注 */
    private String schemename;
    /** 方案名称 */
    private String schemecode;
    /** 方案编码 */

    public String getSchemename() {
        return schemename;
    }

    public void setSchemename(String schemename) {
        this.schemename = schemename;
    }

    public String getSchemecode() {
        return schemecode;
    }

    public void setSchemecode(String schemecode) {
        this.schemecode = schemecode;
    }


    public IndexList(){

    }
    public IndexList(String code,String cname,String procode,String sort,String startperiod,String delayday,String planperiod,String plantime,String createuser,String ifdata,String state){
        this.code=code;
        this.cname=cname;
        this.procode=procode;
        this.sort=sort;
        this.startperiod=startperiod;
        this.delayday=delayday;
        this.planperiod=planperiod;
        this.plantime=plantime;
        this.createuser=createuser;
        this.ifdata=ifdata;
        this.state=state;

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

    public String getProcode() {
        return procode;
    }

    public void setProcode(String procode) {
        this.procode = procode;
    }

    public String getIfdata() {
        return ifdata;
    }

    public void setIfdata(String ifdata) {
        this.ifdata = ifdata;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStartperiod() {
        return startperiod;
    }

    public void setStartperiod(String startperiod) {
        this.startperiod = startperiod;
    }

    public String getDelayday() {
        return delayday;
    }

    public void setDelayday(String delayday) {
        this.delayday = delayday;
    }

    public String getPlanperiod() {
        return planperiod;
    }

    public void setPlanperiod(String planperiod) {
        this.planperiod = planperiod;
    }

    public String getPlantime() {
        return plantime;
    }

    public void setPlantime(String plantime) {
        this.plantime = plantime;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

