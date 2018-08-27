package com.acmr.model.zhzs;


import com.acmr.model.security.User;

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
    private Date plantime;
    /** 将生成的时间 */
    private User createuser;
    /** 创建用户 */
    private Date createtime;
    /** 创建时间 */
    private Date updatetime;
    /** 更新时间 */

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

    public Date getPlantime() {
        return plantime;
    }

    public void setPlantime(Date plantime) {
        this.plantime = plantime;
    }

    public User getCreateuser() {
        return createuser;
    }

    public void setCreateuser(User createuser) {
        this.createuser = createuser;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}

