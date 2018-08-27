package com.acmr.model.zhzs;

import java.util.Date;

public class IndexTask {
    private String code;
    /** 任务编码 */
    private String indexcode;
    /** 任务所属的计划编码 */
    private String  ayearmon;
    /** 任务时间期 */
    private Date createtime;
    /** 任务创建时间 */
    private Date updatetime;
    /** 任务更新时间 */

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIndexcode() {
        return indexcode;
    }

    public void setIndexcode(String indexcode) {
        this.indexcode = indexcode;
    }

    public String getAyearmon() {
        return ayearmon;
    }

    public void setAyearmon(String ayearmon) {
        this.ayearmon = ayearmon;
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
