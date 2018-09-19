package com.acmr.model.zhzs;


public class DataResult {
    private String id;
    private String taskcode;
    private String modcode;
    private String region;
    private String ayearmon;
    private String data;
    private String updatetime;
    private String sessionid;
    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getModcode() {
        return modcode;
    }

    public void setModcode(String modcode) {
        this.modcode = modcode;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUnitcode() {
        return unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    private String unitcode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskcode() {
        return taskcode;
    }

    public void setTaskcode(String taskcode) {
        this.taskcode = taskcode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAyearmon() {
        return ayearmon;
    }

    public void setAyearmon(String ayearmon) {
        this.ayearmon = ayearmon;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

