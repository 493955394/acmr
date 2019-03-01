package com.acmr.service.zhzs;

import acmr.cubequery.service.cubequery.entity.CubeNode;
import acmr.math.CalculateExpression;
import acmr.math.entity.MathException;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.DataDao;
import com.acmr.model.taskindex.TaskIndex;
import com.acmr.model.zhzs.Data;
import com.acmr.model.zhzs.DataResult;
import com.acmr.model.zhzs.TaskModule;
import com.acmr.model.zhzs.TaskZb;
import com.acmr.service.zbdata.OriginService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OriginDataService {
    //这个表是做计算的,和指数计算有关的功能都在这
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
     * 查询临时表submod
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
            taskModule.setOrcode(data.get(i).getString("orcode"));
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
            taskModule.setOrcode(data.get(0).getString("orcode"));
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
                            if(!val.equals("")){//如果有值的话
                                val = String.format("%."+data.get(i).getDacimal()+"f",Double.valueOf(val));//保留几位小数
                                da.setData(val);
                            }else{//要是没有值
                                da.setData("");
                            }

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
                    boolean flag = false;
                    for (int k = 0; k <zbs.size() ; k++) {
                        if(formula.contains(zbs.get(k).getProcode())){//要是存在这个code,就去取对应的zbcode
                            String tempval = originDataService.getvalue(iftmp,taskcode,zbs.get(k).getCode(),reg[j],time,sessionid);
                            //替换公式中的值
                            if(tempval.equals("")){
//                                formula = formula.replace("#"+zbs.get(k).getProcode()+"#","0");//换成0
                                flag = true;
                                continue;
                            }
                            else{
                                formula = formula.replace("#"+zbs.get(k).getProcode()+"#",tempval);//换成对应的value
                            }

                        }
                    }
                    if(flag){
                        da.setData("");//要是替换后有一个是null就不算了
                    }else {
                        //全部替换完成后开始做计算
                        String val = tocalculate(formula,data.get(i).getDacimal());
                        da.setData(val);
                    }
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
    public String tocalculate(String formula,String dacimal){
        String result="";
        formula = formula.replace("random()","chance()");//不能用random这个函数名因为有个and会报错
        try {
            ce.setFunctionclass(new MathService());
            result = ce.Eval(formula);
            result = String.format("%."+dacimal+"f",Double.valueOf(result));//保留几位小数
            System.out.println(ce.Eval(formula));
        } catch (MathException e) {
          //  e.printStackTrace();
            System.out.println("error");
            return result;
        }
        catch (NumberFormatException n) {
            //  e.printStackTrace();
            return result;
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
            boolean flag = false;
            for (int i = 0; i < subs.size(); i++) {
                String data = originDataService.getzbvalue(iftmp,taskcode, subs.get(i).getCode(), reg, time, sessionid);
                if(data.equals("")){
                    flag = true;
                    continue;
                }
                formula += "+" + data + "*" + subs.get(i).getWeight();
            }
            if(flag){
                zsdata.setData("");
            }else {
                zsdata.setData(tocalculate(formula.substring(1),temp.getDacimal()));
            }
            originDataService.addzsdata(iftmp,zsdata);
        }
    }

    /**
     * 重置的功能
     * @param taskcode
     * @return
     */
    public int resetPage(String taskcode,String sessionid){ return DataDao.Fator.getInstance().getIndexdatadao().resetPage(taskcode,sessionid);}

    /**
     * 重新计算，保存到临时表
     */
    public boolean recalculate(String taskcode,String time,String sessionid){
        IndexTaskService indexTaskService = new IndexTaskService();
        String regs = indexTaskService.findRegions(taskcode);
        String [] reg = regs.split(",");
        //开始计算指数的值，包括乘上weight
        try {
            if(calculateZB(true,taskcode,time,regs,sessionid)){
                //指标已经算完
                List<TaskModule> zong = indexTaskService.findRoot(taskcode);
                for (int i = 0; i <zong.size() ; i++) {
                    for (int j = 0; j <reg.length ; j++) {//一个地区一个地区地算
                        calculateZS(true,zong.get(i).getCode(),taskcode,time,reg[j],sessionid);
                    }
                }
            }
        } catch (MathException e) {
          //  e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 保存并重新计算，要是recalculate返回true就把临时表的数据覆盖正式表
     */
    public int savecalculateresult(String taskcode,String sessionid){
        return DataDao.Fator.getInstance().getIndexdatadao().saveResult(taskcode,sessionid);
    }

    //以下是页面回显

    /**
     * 计算结果的回显，测试指标树的列表展示
     */
    public List<Map> modelTree (String taskcode){
        IndexTaskService indexTaskService = new IndexTaskService();
        List<TaskModule> zong = indexTaskService.findRoot(taskcode);
        List<Map> list = new ArrayList<>();
        for (int i = 0; i <zong.size(); i++) {
            Map arr = new HashMap();
            arr.put("name",zong.get(i).getCname());
            arr.put("code",zong.get(i).getCode());
            arr.put("orcode",zong.get(i).getOrcode());
            arr.put("dotcount",zong.get(i).getDacimal());
            list.add(arr);
            list.addAll(modelList(taskcode,zong.get(i).getCode()));
        }
        return list;
    }

    public  List<Map> modelList(String taskcode,String modcode){//好像用不到taskcode，先留着
        List<Map> mode= new ArrayList<>();
        List<TaskModule> tmp = findSubMod(modcode);
        for (int i = 0; i <tmp.size() ; i++) {
            Map arr = new HashMap();
            arr.put("name",tmp.get(i).getCname());
            arr.put("code",tmp.get(i).getCode());
            arr.put("orcode",tmp.get(i).getOrcode());
            arr.put("dotcount",tmp.get(i).getDacimal());
            mode.add(arr);
            if(tmp.get(i).getIfzs().equals("1")){
                mode.addAll(modelList(taskcode,tmp.get(i).getCode()));
            }

        }
        return mode;
    }

    /**
     * 返回上期的modellist，如果有的话
     * @param taskcode
     * @return
     */
    public String findoldtask(String taskcode){
        IndexTaskService indexTaskService= new IndexTaskService();
        String icode = indexTaskService.findIcode(taskcode);
        String ayearmon = indexTaskService.getTime(taskcode);
       DataTable olddata = DataDao.Fator.getInstance().getIndexdatadao().findOldTask(icode,ayearmon);
       if(olddata.getRows().size()>0){
           String oldtcode = olddata.getRows().get(0).getString("code");
           return oldtcode;
       }
        return null;
    }

    /**
     * 通过taskcode和orcode去找是否有模型节点，有的话返回模型节点的code,用于查上期值
     */
    public String findoldmod(String taskcode,String orcode){return DataDao.Fator.getInstance().getIndexdatadao().findModCode(taskcode,orcode);}

    /**
     * 校验时计算那几个特殊的自定义函数
     */
    public String specialMath(String formulatext,String dbcode){
        //存在getvalue函数
        String regex = "getvalue\\((.*?)\\)";
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(formulatext);
        while (m.find()) {
            int i = 1;
            list.add(m.group(i));
            i++;
        }
        for (int i = 0; i <list.size() ; i++) {
            String result = getvalueMath(list.get(i),dbcode);
            if(result!="[]")
            {formulatext = formulatext.replace("getvalue("+list.get(i)+")",result);}
            else{break;}
        }
        return formulatext;
    }

    public String getvalueMath(String orgStr,String dbcode){
        String data = "";
        OriginService os = new OriginService();
        List<CubeNode> tmp = os.getwdsubnodes("sj",orgStr,dbcode);
        for (int i = 0; i <tmp.size() ; i++) {
            data +=","+tmp.get(i).getCode();
            data =  data.substring(1);
        }
        return data;
    }

    public static void main(String[] args) {
        String formulatext = "gettimevalue(test,l,a)";
        String regex = "gettimevalue\\((.*?)\\)";
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(formulatext);
        while (m.find()) {
            int i = 1;
            list.add(m.group(i));
            i++;
        }
        for (String str : list) {
            int index = str.indexOf(",");
            if(index<=0)break;
            String code = str.substring(0,index);
            String wd = str.substring(index+1);
            System.out.println(code);
            System.out.println(wd);

        }
        //存在allareavalue函数
        String regex1 = "allareavalue\\((.*?)\\)";
        List<String> list1 = new ArrayList<String>();
        Pattern pattern1 = Pattern.compile(regex1);
        Matcher m1 = pattern1.matcher(formulatext);
        while (m1.find()) {
            int i = 1;
            list1.add(m1.group(i));
            i++;
        }
        for (String str : list1) {
            int index = str.indexOf(",");
            if(index<=0)break;
            String code = str.substring(0,index);
            String wd = str.substring(index+1);
            System.out.println(code);
            System.out.println(wd);
        }
    }

}
