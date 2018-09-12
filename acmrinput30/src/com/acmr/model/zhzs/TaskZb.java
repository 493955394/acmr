package com.acmr.model.zhzs;

import acmr.cubequery.service.cubequery.entity.CubeWdCodes;
import acmr.util.PubInfo;
import com.acmr.service.zbdata.OriginService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskZb {
    private String code;
    /** 计划编码 */
    private String taskcode;
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

    public TaskZb(){
        PubInfo.printStr("无参构造方法");
    }
    public TaskZb(String code,String taskcode,String zbcode,String company,String datasource,String regions,String unitcode){
        this.code=code;
        this.taskcode=taskcode;
        this.zbcode=zbcode;
        this.company=company;
        this.datasource=datasource;
        this.regions=regions;
        this.unitcode=unitcode;

    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTaskcode() {
        return taskcode;
    }

    public void setTaskcode(String taskcode) {
        this.taskcode = taskcode;
    }

    public String getZbcode() {
        return zbcode;
    }

    public void setZbcode(String zbcode) {
        this.zbcode = zbcode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getRegions() {
        return regions;
    }

    public void setRegions(String regions) {
        this.regions = regions;
    }

    public String getDatatimes() {
        return datatimes;
    }

    public void setDatatimes(String datatimes) {
        this.datatimes = datatimes;
    }

    public String getUnitcode() {
        return unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    public String getDacimal() {
        return dacimal;
    }

    public void setDacimal(String dacimal) {
        this.dacimal = dacimal;
    }

    /**
    * @Description: 根据给的时间，查该指标的数，并按照regions中地区顺序返回list
    * @Param: [time]
    * @return: java.util.List<java.lang.Double>
    * @Author: lyh
    * @Date: 2018/9/12
    */
    public List<Double> getData(String time){
        List<Double> data=new ArrayList<>();
        OriginService originService=new OriginService();
        List<String> regs= Arrays.asList(this.regions.split(","));
        String funit=originService.getwdnode("zb",this.zbcode).getUnitcode();
        double rate=originService.getRate(funit,this.unitcode,time);
        for (int i=0;i<regs.size();i++){
            CubeWdCodes where = new CubeWdCodes();
            where.Add("reg",regs.get(i));
            where.Add("zb",this.zbcode);
            where.Add("ds",this.datasource);
            where.Add("co",this.company);
            where.Add("sj",time);
            if(originService.querydata(where).size()>0){
                double d=originService.querydata(where).get(0).getData().getData()*rate;
                data.add(d);
            }
            else {
                data.add(null);
            }
        }
        return data;
    }
}
