package com.acmr.service.zhzs;

import acmr.math.CalculateExpression;
import acmr.math.entity.MathException;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.DataDao;
import com.acmr.model.taskindex.TaskIndex;
import com.acmr.model.zhzs.Data;
import com.acmr.model.zhzs.DataResult;
import com.acmr.model.zhzs.TaskModule;
import com.acmr.model.zhzs.TaskZb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OriginDataService {
    private CalculateExpression ce = new CalculateExpression();
    /**
     * 查询对应的data值
     */
    public String getvalue(boolean iftmp,String taskcode,String zbcode,String region,String time,String sessionid){
        String value = "";
        DataTable data = DataDao.Fator.getInstance().getIndexdatadao().getData(iftmp,taskcode,zbcode,region,time,sessionid);
        if(data.getRows().size()!=0){
            value = data.getRows().get(0).getString("data");
        }
        return value;
    }
    /**
     * 将数据插入data_result_tmp
     */
    public int addDataresult(boolean iftmp,List<DataResult> dataResults){return DataDao.Fator.getInstance().getIndexdatadao().addDataResult(iftmp,dataResults);}
    public int addzsdata(boolean iftmp,DataResult dataResult){return DataDao.Fator.getInstance().getIndexdatadao().addZSData(iftmp,dataResult);}

    /**
     * 查询submod
     */
    public List<TaskModule> findSubMod(String code){
        List<TaskModule> taskModules = new ArrayList<>();
        List<DataTableRow> data =new ArrayList<>();
        data = DataDao.Fator.getInstance().getIndexdatadao().getSubMod(code).getRows();
        for (int i = 0; i <data.size() ; i++) {
            TaskModule taskModule = new TaskModule();
            taskModule.setCode(data.get(i).getString("code"));
            taskModule.setCname(data.get(i).getString("cname"));
            taskModule.setTaskcode(data.get(i).getString("taskcode"));
            taskModule.setProcode(data.get(i).getString("procode"));
            taskModule.setIfzs(data.get(i).getString("ifzs"));
            taskModule.setIfzb(data.get(i).getString("ifzb"));
            taskModule.setFormula(data.get(i).getString("formula"));
            taskModule.setWeight(data.get(i).getString("weight"));
            taskModule.setSortcode(data.get(i).getString("sortcode"));
            taskModule.setDacimal(data.get(i).getString("dacimal"));
            taskModules.add(taskModule);
        }
        return taskModules;
    }
    /**
     * 查询data_result_tmp中是否有值，返回1代表缺值
     */
    public int subDataCheck(boolean iftmp,List<TaskModule> taskModules,String reg,String time,String sessionid){
        int i = 0;
        for (int j = 0; j <taskModules.size() ; j++) {
            int temp = DataDao.Fator.getInstance().getIndexdatadao().subDataCheck(iftmp,taskModules.get(j).getTaskcode(),taskModules.get(j).getCode(),reg,time,sessionid);
            if(temp == 1 ){
                i = 1;//证明缺值
            }
        }
        return i;
    }
    /**
     * 查单个的module
     */
    public TaskModule getModData(String code){
        List<DataTableRow> data = DataDao.Fator.getInstance().getIndexdatadao().getModData(code).getRows();
        TaskModule taskModule = new TaskModule();
       if(data.size()>0){
            taskModule.setCode(data.get(0).getString("code"));
            taskModule.setCname(data.get(0).getString("cname"));
            taskModule.setTaskcode(data.get(0).getString("taskcode"));
            taskModule.setProcode(data.get(0).getString("procode"));
            taskModule.setIfzs(data.get(0).getString("ifzs"));
            taskModule.setIfzb(data.get(0).getString("ifzb"));
            taskModule.setFormula(data.get(0).getString("formula"));
            taskModule.setWeight(data.get(0).getString("weight"));
            taskModule.setSortcode(data.get(0).getString("sortcode"));
            taskModule.setDacimal(data.get(0).getString("dacimal"));
        }
        return taskModule;
    }
    /**
     * 查询指标的value
     */
    public String getzbvalue(boolean iftmp,String taskcode,String modcode,String reg,String time,String sessionid){return DataDao.Fator.getInstance().getIndexdatadao().getDataResult(iftmp,taskcode,modcode,reg,time,sessionid);}

    /**
     * 可以在model层调用的计算方法，在任务刚创建的时候就去计算
     * @param taskcode
     * @param time
     * @return
     */
    public boolean todocalculate(String taskcode,String time){
        IndexTaskService indexTaskService = new IndexTaskService();
        String regs = indexTaskService.findRegions(taskcode);
        String [] reg = regs.split(",");
        String sessionid="";
        //开始计算指数的值，包括乘上weight
        try {
            if(calculateZB(false,taskcode,time,regs,sessionid)){
                //指标已经算完
                List<TaskModule> zong = indexTaskService.findRoot(taskcode);
                for (int i = 0; i <zong.size() ; i++) {
                    for (int j = 0; j <reg.length ; j++) {//一个地区一个地区地算
                        calculateZS(false,zong.get(i).getCode(),taskcode,time,reg[j],sessionid);
                    }
                }
            }
        } catch (MathException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 计算的方法,只算指标
     */
    public boolean calculateZB(boolean iftmp,String taskcode, String time, String regs,String sessionid) throws MathException {
        String [] reg = regs.split(",");
        IndexTaskService indexTaskService = new IndexTaskService();
        OriginDataService originDataService = new OriginDataService();
        List<DataResult> newadd = new ArrayList<>();
        List<TaskModule> data = indexTaskService.getModuleFormula(taskcode,"0");//取的是指标的list
        List<TaskZb> zbs = indexTaskService.findtaskzb(taskcode);
        for (int i = 0; i <data.size() ; i++) {//开始循环
            if(data.get(i).getIfzb().equals("1")){//如果是直接用筛选的指标的话直接去查值
                //先去查tb_coindex_zb
                for (int j = 0; j <reg.length ; j++) {
                    DataResult da = new DataResult();
                    da.setAyearmon(time);
                    da.setRegion(reg[j]);
                    da.setTaskcode(taskcode);
                    da.setModcode(data.get(i).getCode());
                    da.setSessionid(sessionid);
                    for (int k = 0; k <zbs.size() ; k++) {
                        if(data.get(i).getFormula().contains(zbs.get(k).getProcode())){//要是存在这个code,就去取对应的zbcode
                            String val = originDataService.getvalue(iftmp,taskcode,zbs.get(k).getCode(),reg[j],time,sessionid);
                            da.setData(val);
                            newadd.add(da);}
                    }
                }
            }
            else if(data.get(i).getIfzb().equals("0")){//如果是自己编辑的公式
                //先处理公式

                for (int j = 0; j <reg.length ; j++) {//地区循环
                    String formula = data.get(i).getFormula();
                    DataResult da = new DataResult();
                    da.setAyearmon(time);
                    da.setRegion(reg[j]);
                    da.setTaskcode(taskcode);
                    da.setModcode(data.get(i).getCode());
                    da.setSessionid(sessionid);
                    for (int k = 0; k <zbs.size() ; k++) {
                        if(formula.contains(zbs.get(k).getProcode())){//要是存在这个code,就去取对应的zbcode
                            String tempval = originDataService.getvalue(iftmp,taskcode,zbs.get(k).getCode(),reg[j],time,sessionid);
                            //替换公式中的值
                            formula = formula.replace("#"+zbs.get(k).getProcode()+"#",tempval);//换成对应的value
                        }
                    }
                    //全部替换完成后开始做计算
                    String val = tocalculate(formula);
                    da.setData(val);
                    newadd.add(da);
                }
            }
        }
        int i=addDataresult(iftmp,newadd);
        if (i!=1){
            return false;
        }
        return true;
    }

    /**
     * 自定义公式计算函数
     */
    public String tocalculate(String formula){
        String result="";
        formula = formula.replace("random()","chance()");//不能用random这个函数名因为有个and会报错
        try {
            ce.setFunctionclass(new MathService());
            result = ce.Eval(formula);
            System.out.println(ce.Eval(formula));
        } catch (MathException e) {
            e.printStackTrace();
            System.out.println("error");
        }
        return result;
    }
    /**
     * 计算指数（乘上权重），递归
     */
    public  void calculateZS(boolean iftmp,String code,String taskcode, String time, String reg,String sessionid) throws MathException{
        DataResult zsdata = new DataResult();
        OriginDataService originDataService = new OriginDataService();
        List<TaskModule> subs = originDataService.findSubMod(code);
        int check = originDataService.subDataCheck(iftmp,subs,reg,time,sessionid);
        if(check ==1){//下一级的值不全
            for (int i = 0; i <subs.size() ; i++) {
                //先去检查数据库有没有值，有值就不算了
                List<TaskModule> tmp =new ArrayList<>();
                tmp.add(subs.get(i));
                if(originDataService.subDataCheck(iftmp,tmp,reg,time,sessionid)==1){
                    calculateZS(iftmp,subs.get(i).getCode(),taskcode,time,reg,sessionid);
                }
            }
            calculateZS(iftmp,code,taskcode,time,reg,sessionid);//计算一遍它的父级
        }
        else {//要是下一级的值是全的，就把值加到zsdatas中
            TaskModule temp = originDataService.getModData(code);
            zsdata.setAyearmon(time);
            zsdata.setRegion(reg);
            zsdata.setSessionid(sessionid);
            zsdata.setTaskcode(taskcode);
            zsdata.setTaskcode(temp.getTaskcode());
            zsdata.setModcode(temp.getCode());
            String formula = "";
            for (int i = 0; i < subs.size(); i++) {
                String data = originDataService.getzbvalue(iftmp,taskcode, subs.get(i).getCode(), reg, time, sessionid);
                formula += "+" + data + "*" + subs.get(i).getWeight();
            }
            zsdata.setData(tocalculate(formula.substring(1)));
            originDataService.addzsdata(iftmp,zsdata);
        }
    }
}
