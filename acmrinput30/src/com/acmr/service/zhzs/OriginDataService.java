package com.acmr.service.zhzs;

import acmr.cubequery.service.cubequery.entity.CubeNode;
import acmr.cubequery.service.cubequery.entity.CubeQueryData;
import acmr.cubequery.service.cubequery.entity.CubeWdCodes;
import acmr.cubequery.service.cubequery.entity.CubeWdValue;
import acmr.math.CalculateExpression;
import acmr.math.entity.MathException;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.zhzs.DataDao;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.dao.zhzs.IndexTaskDao;
import com.acmr.model.taskindex.TaskIndex;
import com.acmr.model.zhzs.*;
import com.acmr.service.zbdata.OriginService;
import com.acmr.service.zbdata.RegdataService;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OriginDataService {
    //这个表是做计算的,和指数计算有关的功能都在这
    private CalculateExpression ce = new CalculateExpression();
    private List<String> tasktimes = new ArrayList<>();
    private List<String> task_codes = new ArrayList<>();
    /**
     * 查询对应的data值
     */
    public String getvalue(boolean iftmp,String taskcode,String procode,String region,String time,String sessionid){
        String value = "";
        DataTable data = DataDao.Fator.getInstance().getIndexdatadao().getData(iftmp,taskcode,procode,region,time,sessionid);
        if(data.getRows().size()!=0) {
            value = data.getRows().get(0).getString("data");
        }
        return value;
    }

    /**
     * 可以在model层调用的计算方法，在任务刚创建的时候就去计算
     * @param taskcode
     * @param time
     * @return
     */
    public boolean todocalculate(String[] taskcode,String[] time,String icode){
        IndexTaskService indexTaskService = new IndexTaskService();
        if(taskcode.length==0)return true;
        String regs = indexTaskService.findRegions(taskcode[0]);
        String [] reg = regs.split(",");
        String sessionid="";
        for (int i=0;i<taskcode.length;i++){
            task_codes.add(taskcode[i]);
        }
        //开始计算指数的值，包括乘上weight
        try {
            for (int y=0;y<taskcode.length;y++) {
                if (calculateZB(false,true, taskcode[y], time[y], regs, sessionid)) {
                    //指标已经算完
                    List<TaskModule> zong = indexTaskService.findRoot(taskcode[y]);
                    for (int i = 0; i < zong.size(); i++) {
                        for (int j = 0; j < reg.length; j++) {//一个地区一个地区地算
                            calculateZS(false, zong.get(i).getCode(), taskcode[y], time[y], reg[j], sessionid);
                        }
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
     * 启动时计算特殊函数处理时要先看是否有这个数据，没有要去底库存
     * @param iftmp
     * @param ifstart
     * @param sjs
     * @param icode
     * @param procode
     * @param tcode
     * @param region
     * @param time
     * @param sessionid
     * @return
     */
    public String getvalue(boolean iftmp,boolean ifstart,List<CubeNode> sjs,String icode ,String procode,String tcode,String region,String time,String sessionid){
        String value = "";
        DataTable data = DataDao.Fator.getInstance().getIndexdatadao().getData(iftmp,tcode,procode,region,time,sessionid);
        if(data.getRows().size()!=0) {
            value = data.getRows().get(0).getString("data");
        }else{
            if(ifstart){
                //校验所需要计算的时间是否在任务期内有，没有的话就存一下
                if(tasktimes.size()<=0){
                    List<String> datecode = DataDao.Fator.getInstance().getIndexdatadao().getAllTime(procode);
                    tasktimes.addAll(datecode);
                }
                List<String> less = new ArrayList<>();
                List<DataResult> savadata = new ArrayList<>();
                for (int i = 0; i <sjs.size() ; i++) {
                    if(!tasktimes.contains(sjs.get(i).getCode())){
                        less.add(sjs.get(i).getCode());
                        tasktimes.add(sjs.get(i).getCode());
                    }
                }
                for (int i = 0; i <less.size() ; i++) {
                    for (int k = 0; k <task_codes.size() ; k++) {
                        List<String> zbcodes = new ArrayList<>();
                        zbcodes.addAll(DataDao.Fator.getInstance().getIndexdatadao().getAllZbcode(task_codes.get(k)));
                        for(String zbcode:zbcodes) {
                            DataTable rows2 = DataDao.Fator.getInstance().getIndexdatadao().getTaskZbData(task_codes.get(k), zbcode);
                            String code = rows2.getRows().get(0).getString("code");
                            String jzbcode = rows2.getRows().get(0).getString("zbcode");
                            String company = rows2.getRows().get(0).getString("company");
                            String datasource = rows2.getRows().get(0).getString("datasource");
                            String regions = rows2.getRows().get(0).getString("regions");
                            String[] reg = regions.split(",");
                            String unitcode = rows2.getRows().get(0).getString("unitcode");
                            TaskZb taskZb = new TaskZb(code, task_codes.get(k), jzbcode, company, datasource, regions, unitcode);
                            List<Double> zbdatas = taskZb.getData(less.get(i), icode);
                            for (int j = 0; j < zbdatas.size(); j++) {
                                DataResult tmp = new DataResult();
                                if (task_codes.get(k).equals(tcode) && reg[j].equals(region) && less.get(i).equals(time)&&zbdatas.get(j)!=null) {
                                    value = String.valueOf(zbdatas.get(j));
                                }
                                tmp.setTaskcode(task_codes.get(k));
                                tmp.setModcode(zbcode);
                                tmp.setRegion(reg[j]);
                                tmp.setAyearmon(less.get(i));
                                tmp.setProcode(procode);
                                tmp.setData(String.valueOf(zbdatas.get(j)));
                                savadata.add(tmp);
                            }
                        }
                    }
                }
                //然后存入库
                if(savadata.size()>0)
                DataDao.Fator.getInstance().getIndexdatadao().addMathData(savadata);
            }
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
     * 计算的方法,只算指标
     */
    public boolean calculateZB(boolean iftmp,boolean ifstart,String taskcode, String time, String regs,String sessionid) throws MathException {
        String [] reg = regs.split(",");
        IndexTaskService indexTaskService = new IndexTaskService();
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
                            String val = getvalue(iftmp,taskcode,zbs.get(k).getProcode(),reg[j],time,sessionid);
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
                    //先处理特殊的getvalue函数
                    formula = specialMath(formula,iftmp,ifstart,zbs,taskcode,time,reg[j],regs,sessionid);
                    if(formula.equals("false")){  //getvalue函数里没数
                        flag = true;
                        }
                        else{
                    for (int k = 0; k <zbs.size() ; k++) {
                        if(formula.contains(zbs.get(k).getProcode())){//要是存在这个code,就去取对应的value
                            String tempval = getvalue(iftmp,taskcode,zbs.get(k).getProcode(),reg[j],time,sessionid);
                            //替换公式中的值
                            if(tempval.equals("")){
//                                formula = formula.replace("#"+zbs.get(k).getProcode()+"#","0");//换成0
                                flag = true;
                               break;
                            }
                            else{
                                formula = formula.replace("#"+zbs.get(k).getProcode()+"#",tempval);//换成对应的value
                            }

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
       // formula = formula.replace("random()","chance()");//不能用random这个函数名因为有个and会报错
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
        List<TaskModule> subs = findSubMod(code);
        int check = subDataCheck(iftmp,subs,reg,time,sessionid);
        if(check ==1){//下一级的值不全
            for (int i = 0; i <subs.size() ; i++) {
                //先去检查数据库有没有值，有值就不算了
                List<TaskModule> tmp =new ArrayList<>();
                tmp.add(subs.get(i));
                if(subDataCheck(iftmp,tmp,reg,time,sessionid)==1){
                    calculateZS(iftmp,subs.get(i).getCode(),taskcode,time,reg,sessionid);
                }
            }
            calculateZS(iftmp,code,taskcode,time,reg,sessionid);//计算一遍它的父级
        }
        else {//要是下一级的值是全的，就把值加到zsdatas中
            TaskModule temp = getModData(code);
            zsdata.setAyearmon(time);
            zsdata.setRegion(reg);
            zsdata.setSessionid(sessionid);
            zsdata.setTaskcode(taskcode);
            zsdata.setTaskcode(temp.getTaskcode());
            zsdata.setModcode(temp.getCode());
            String formula = "";
            boolean flag = false;
            for (int i = 0; i < subs.size(); i++) {
                String data = getzbvalue(iftmp,taskcode, subs.get(i).getCode(), reg, time, sessionid);
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
            addzsdata(iftmp,zsdata);
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
            if(calculateZB(true,false,taskcode,time,regs,sessionid)){
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
     * 校验时计算那个特殊的自定义函数getvalue
     */
    public String specialMath(String formulatext,boolean iftmp,boolean ifstart, List<TaskZb> zbs,String taskcode, String time,String thisreg,String regs,String sessionid){
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
        boolean flag = false;
        for (int i = 0; i <list.size() ; i++) {
            String result = getvalueMath(list.get(i),iftmp,ifstart,zbs,taskcode,time,thisreg,regs,sessionid);
            if(result.equals("false")){//是空数组，报错
                flag = true;
                break;
            }else {//不是空数组的话做替换
                formulatext = formulatext.replace("getvalue("+list.get(i)+")",result);
            }
        }
        if(flag)return "false";
        return formulatext;
    }

    public String getvalueMath(String orgStr,boolean iftmp,boolean ifstart, List<TaskZb> zbs,String taskcode, String time,String thisreg,String regs,String sessionid) {
        String data = "false";//默认没有值
        String text = orgStr;
        String[] reg = regs.split(",");
       if(!text.contains(",")){//不包含逗号，说明是单个的zb,当前时间当前地区直接找值就可以
           for (int k = 0; k <zbs.size() ; k++) {
               if (text.contains(zbs.get(k).getProcode())) {//要是存在这个code,就去取
                   String val = getvalue(iftmp, taskcode, zbs.get(k).getProcode(), thisreg, time, sessionid);
                   if (!val.equals("")) {//如果有值的话,做替换
                       data = val;
                   } else {//要是没有值
                       break;
                   }
               }
           }
       }else{ //有逗号，又分为几种情况
           if(StringUtils.countMatches(text,"#")>2){ //全都是指标的话
               String tmp = "";
               for (int k = 0; k <zbs.size() ; k++) {
                   if (text.contains(zbs.get(k).getProcode())) {//要是存在这个code,就去取对应的zbcode
                       String val = getvalue(iftmp, taskcode, zbs.get(k).getProcode(), thisreg, time, sessionid);
                       if (!val.equals("")) {//如果有值的话,做替换
                           tmp +=","+val;
                       } else {//要是没有值
                          continue;
                       }
                   }
               }
               if(!tmp.equals("")) data = tmp.substring(1);//只要有一组数组有值，就算对
           }
           else{//要是既有指标又有时间，又分三种情况
               int index = text.indexOf(",");
               String modcode = text.substring(0, index);
               String wd = text.substring(index + 1);
               String icode = IndexTaskDao.Fator.getInstance().getIndexdatadao().getIcode(taskcode);
               String dbcode= IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
               OriginService os = new OriginService();
               String zbcode="";
               String procode = "";
               for (int k = 0; k <zbs.size() ; k++) {//先找到modcode对应的zbcode
                   if (modcode.contains(zbs.get(k).getProcode())) {//要是存在这个modcode,就去取对应的zbcode
                       zbcode = zbs.get(k).getCode();
                       procode = zbs.get(k).getProcode();
                   }
               }
               if(wd.equals("dq")){//如果是所有地区，当期时间
                   String tmp = "";
                   for (int k = 0; k <reg.length ; k++) {
                       if (!zbcode.equals("")) {//要是存在这个code,就去取对应的zbcode
                           String val = getvalue(iftmp, taskcode, procode, reg[k], time, sessionid);
                           if (!val.equals("")) {//如果有值的话,做替换
                               tmp +=","+val;
                           } else {//要是没有值
                               continue;
                           }
                       }
                   }
                   if(!tmp.equals("")) data = tmp.substring(1);//只要有一组数组有值，就算对
               }else if(wd.equals("begintime")){//如果是计划起始时间,用当前地区
                   String tmp = "";
                   String begintime = IndexListDao.Fator.getInstance().getIndexdatadao().getByCode(icode).getRows().get(0).getString("startperiod");
                   List<CubeNode> sjs = os.getwdsubnodes("sj",begintime+"-"+time,dbcode);
                   for (int i = 0; i <sjs.size() ; i++) {
                           String val = getvalue(iftmp, taskcode, procode, thisreg,sjs.get(i).getCode(), sessionid);
                           if (!val.equals("")) {//如果有值的话,做替换
                               tmp +=","+val;
                           } else {//要是没有值
                               continue;
                           }
                       }
                   if(!tmp.equals("")) data = tmp.substring(1);//只要有一组数组有值，就算对
               }
               else {//其他情况直接丢去库里找
                   String tmp = "";
                   wd = os.getChangeTime(wd,time);
                   List<CubeNode> sjs = os.getwdsubnodes("sj",wd,dbcode);
                   for (int i = 0; i <sjs.size() ; i++) {//库里可能没有数据，需要先校验,没有就补全
                       String val = getvalue(iftmp,ifstart,sjs,icode,procode,taskcode,thisreg,sjs.get(i).getCode(), sessionid);
                       if (!val.equals("")) {//如果有值的话,做替换
                           tmp +=","+val;
                       } else {//要是没有值
                           continue;
                       }
                   }
                   if(!tmp.equals("")) data = tmp.substring(1);//只要有一组数组有值，就算对
               }
           }
       }
        return data;
    }


}
