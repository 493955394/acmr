package com.acmr.service.zhzs;

import acmr.cubequery.service.cubequery.entity.CubeNode;
import acmr.cubequery.service.cubequery.entity.CubeQueryData;
import acmr.cubequery.service.cubequery.entity.CubeWdCodes;
import acmr.math.CalculateExpression;
import acmr.math.entity.MathException;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.dao.zhzs.DataDao;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.dao.zhzs.IndexTaskDao;
import com.acmr.model.zhzs.DataResult;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.model.zhzs.TaskModule;
import com.acmr.model.zhzs.TaskZb;
import com.acmr.service.zbdata.OriginService;
import com.acmr.service.zbdata.RegdataService;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalTaskDataService {
    //这个表是启动任务时做计算的
    private CalculateExpression ce = new CalculateExpression();
    private Map<String,List<IndexMoudle>> modSubList = new HashMap<>();

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
        String icode = IndexTaskDao.Fator.getInstance().getIndexdatadao().getIcode(taskcode);
        String dbcode= IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        List<TaskZb> zbs = indexTaskService.findtaskzb(taskcode);
        //开始计算指数的值，包括乘上weight
        try {
            if(calculateZB(false,taskcode,time,regs,sessionid,zbs,icode,dbcode)){
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
    public boolean calculateZB(boolean iftmp,String taskcode, String time, String regs,String sessionid,List<TaskZb> zbs,String icode,String dbcode) throws MathException {
        String [] reg = regs.split(",");
        IndexTaskService indexTaskService = new IndexTaskService();
        OriginService originService = new OriginService();
        List<DataResult> newadd = new ArrayList<>();
        List<TaskModule> data = indexTaskService.getModuleFormula(taskcode,"0");//取的是指标的list
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
                            ArrayList<CubeQueryData> result = findZbData(zbs.get(k).getZbcode(), zbs.get(k).getDatasource(), zbs.get(k).getCompany(), reg[j],time,dbcode);
                            if (result.size() > 0) {
                                if (!result.get(0).getData().getStrdata().equals("")) {//如果有值的话
                                    //单位换算
                                    String funit = originService.getwdnode("zb", zbs.get(k).getZbcode(), dbcode).getUnitcode();
                                    BigDecimal rate = new BigDecimal(String.valueOf(originService.getRate(funit, zbs.get(k).getUnitcode(), time)));
                                    BigDecimal orval = (new BigDecimal(result.get(0).getData().getStrdata())).multiply(rate);
                                    BigDecimal val = orval.setScale(Integer.parseInt(data.get(i).getDacimal()), RoundingMode.CEILING);//截取小数点
                                    da.setData(val.toPlainString());
                                } else {
                                    da.setData("");
                                }
                            } else {//要是没有值
                                da.setData("");
                            }

                            newadd.add(da);
                        }
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
                    formula = specialMath(formula,iftmp,zbs,taskcode,time,reg[j],regs,sessionid,icode,dbcode);
                    if(formula.equals("false")){  //getvalue函数里没数
                        flag = true;
                        }
                        else{
                    for (int k = 0; k <zbs.size() ; k++) {
                        if(formula.contains(zbs.get(k).getProcode())){//要是存在这个code,就去取对应的zbcode
                            CubeWdCodes where = new CubeWdCodes();
                            where.Add("zb", zbs.get(k).getZbcode());
                            where.Add("ds", zbs.get(k).getDatasource());
                            where.Add("co", zbs.get(k).getCompany());
                            where.Add("reg", reg[j]);
                            where.Add("sj", time);
                            ArrayList<CubeQueryData> result = RegdataService.queryData(dbcode, where);
                            //替换公式中的值
                            if ((result.size() != 0 && result.get(0).getData().getStrdata().equals("")) || result.size() == 0) {
//                                formula = formula.replace("#"+zbs.get(k).getProcode()+"#","0");//换成0
                                flag = true;
                                break;}
                            else{
                                //单位换算
                                String funit = originService.getwdnode("zb", zbs.get(k).getZbcode(), dbcode).getUnitcode();
                                BigDecimal rate = new BigDecimal(String.valueOf(originService.getRate(funit, zbs.get(k).getUnitcode(), time)));
                                BigDecimal orval = (new BigDecimal(String.valueOf(result.get(0).getData().getStrdata()))).multiply(rate);
                                formula = formula.replace("#"+zbs.get(k).getProcode()+"#",String.valueOf(orval));//换成对应的value
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
        CalTaskDataService originDataService = new CalTaskDataService();
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
     * 校验时计算那个特殊的自定义函数getvalue
     */
    public String specialMath(String formulatext,boolean iftmp, List<TaskZb> zbs,String taskcode, String time,String thisreg,String regs,String sessionid,String icode,String dbcode){
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
            String result = getvalueMath(list.get(i),zbs,time,thisreg,regs,icode,dbcode);
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

    public String getvalueMath(String orgStr, List<TaskZb> zbs,String time,String thisreg,String regs,String icode,String dbcode) {
        String data = "false";//默认没有值
        String text = orgStr;
        OriginService originService = new OriginService();
       if(!text.contains(",")){//不包含逗号，说明是单个的zb,当前时间当前地区直接找值就可以
           for (int k = 0; k <zbs.size() ; k++) {
               if (text.contains(zbs.get(k).getProcode())) {//要是存在这个code,就去取对应的zbcode
                   String val = "";
                   ArrayList<CubeQueryData> result = findZbData(zbs.get(k).getZbcode(),zbs.get(k).getDatasource(),zbs.get(k).getCompany(),thisreg,time,dbcode);
                   if(result.size()>0){
                       if(!result.get(0).getData().getStrdata().equals("")){//如果有值的话
                           //单位换算
                           String funit=originService.getwdnode("zb",zbs.get(k).getZbcode(),dbcode).getUnitcode();
                           BigDecimal rate = new BigDecimal(String.valueOf(originService.getRate(funit,zbs.get(k).getUnitcode(),time)));
                           BigDecimal orval = (new BigDecimal(result.get(0).getData().getStrdata())).multiply(rate);
                           val = orval.toPlainString();
                       }
                   }
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
                       String val = "";
                       ArrayList<CubeQueryData> result = findZbData(zbs.get(k).getZbcode(),zbs.get(k).getDatasource(),zbs.get(k).getCompany(),thisreg,time,dbcode);
                       if(result.size()>0){
                           if(!result.get(0).getData().getStrdata().equals("")){//如果有值的话
                               //单位换算
                               String funit=originService.getwdnode("zb",zbs.get(k).getZbcode(),dbcode).getUnitcode();
                               BigDecimal rate = new BigDecimal(String.valueOf(originService.getRate(funit,zbs.get(k).getUnitcode(),time)));
                               BigDecimal orval = (new BigDecimal(result.get(0).getData().getStrdata())).multiply(rate);
                               val = orval.toPlainString();
                           }
                       }
                       if (!val.equals("")) {//如果有值的话,做替换
                           tmp +=","+val;
                       } else {//要是没有值
                          continue;
                       }
                   }
               }
               if(!tmp.equals("")) data = tmp.substring(1);//只要有一组数组有值，就算对
           }
           else {//要是既有指标又有时间，又分三种情况
               int index = text.indexOf(",");
               String modcode = text.substring(0, index);
               String wd = text.substring(index + 1);
               OriginService os = new OriginService();
               String zbcode = "";
               String ds = "";
               String co = "";
               String unitcode = "";
               for (int k = 0; k < zbs.size(); k++) {//先找到modcode对应的zbcode
                   if (modcode.contains(zbs.get(k).getProcode())) {//要是存在这个modcode,就去取对应的zbcode
                       zbcode = zbs.get(k).getZbcode();
                       ds = zbs.get(k).getDatasource();
                       co = zbs.get(k).getCompany();
                       unitcode = zbs.get(k).getUnitcode();
                   }
               }
               if (!zbcode.equals("")) {
               if (wd.equals("dq")) {//如果是所有地区，当期时间
                   String[] reg = regs.split(",");
                   String tmp = "";
                   String[] date = new String[]{time};
                   //要是存在这个code,就去取对应的zbcode
                       ArrayList<CubeQueryData> result = findZbData(zbcode, ds, co, reg, date, dbcode);
                       for (int i = 0; i < result.size(); i++) {
                           String val = "";
                           if (!result.get(i).getData().getStrdata().equals("")) {//如果有值的话
                               //单位换算
                               String funit = originService.getwdnode("zb", zbcode, dbcode).getUnitcode();
                               BigDecimal rate = new BigDecimal(String.valueOf(originService.getRate(funit, unitcode, time)));
                               BigDecimal orval = (new BigDecimal(result.get(i).getData().getStrdata())).multiply(rate);
                               val = orval.toPlainString();
                           }
                           if (!val.equals("")) {//如果有值的话,做替换
                               tmp += "," + val;
                           } else {//要是没有值
                               continue;
                           }
                   }
                   if (!tmp.equals("")) data = tmp.substring(1);//只要有一组数组有值，就算对
               } else if (wd.equals("begintime")) {//如果是计划起始时间,用当前地区
                   String tmp = "";
                   String begintime = IndexListDao.Fator.getInstance().getIndexdatadao().getByCode(icode).getRows().get(0).getString("startperiod");
                   List<CubeNode> sjs = os.getwdsubnodes("sj", begintime + "-", dbcode);
                   String[] date = new String[sjs.size()];
                   for (int i = 0; i <sjs.size() ; i++) {
                       date[i] = sjs.get(i).getCode();
                   }
                   ArrayList<CubeQueryData> result = findZbData(zbcode, ds, co, new String[]{thisreg}, date, dbcode);
                   for (int i = 0; i <result.size() ; i++) {
                       String val = "";
                       if (!result.get(i).getData().getStrdata().equals("")) {//如果有值的话
                           //单位换算
                           String funit = originService.getwdnode("zb", zbcode, dbcode).getUnitcode();
                           BigDecimal rate = new BigDecimal(String.valueOf(originService.getRate(funit, unitcode, time)));
                           BigDecimal orval = (new BigDecimal(result.get(i).getData().getStrdata())).multiply(rate);
                           val = orval.toPlainString();
                       }
                       if (!val.equals("")) {//如果有值的话,做替换
                           tmp += "," + val;
                       } else {//要是没有值
                           continue;
                       }
                   }
                   if (!tmp.equals("")) data = tmp.substring(1);//只要有一组数组有值，就算对
               } else {//其他情况直接丢去库里找
                   String tmp = "";
                   List<CubeNode> sjs = os.getwdsubnodes("sj", wd, dbcode);
                   String[] date = new String[sjs.size()];
                   for (int i = 0; i <sjs.size() ; i++) {
                       date[i] = sjs.get(i).getCode();
                   }
                   ArrayList<CubeQueryData> result = findZbData(zbcode, ds, co, new String[]{thisreg}, date, dbcode);

                   for (int i = 0; i <result.size() ; i++) {
                       String val = "";
                       if (!result.get(i).getData().getStrdata().equals("")) {//如果有值的话
                           //单位换算
                           String funit = originService.getwdnode("zb", zbcode, dbcode).getUnitcode();
                           BigDecimal rate = new BigDecimal(String.valueOf(originService.getRate(funit, unitcode, time)));
                           BigDecimal orval = (new BigDecimal(result.get(i).getData().getStrdata())).multiply(rate);
                           val = orval.toPlainString();
                       }
                       if (!val.equals("")) {//如果有值的话,做替换
                           tmp += "," + val;
                       } else {//要是没有值
                           continue;
                       }
                   }
                   if (!tmp.equals("")) data = tmp.substring(1);//只要有一组数组有值，就算对
               }
           }
           }
       }
        return data;
    }

    //查询zb值
    public ArrayList<CubeQueryData> findZbData(String zb, String ds, String co, String reg, String sj, String dbcode){
        CubeWdCodes where = new CubeWdCodes();
        where.Add("zb", zb);
        where.Add("ds", ds);
        where.Add("co", co);
        where.Add("reg", reg);
        where.Add("sj", sj);
        return RegdataService.queryData(dbcode, where);
    }

    public ArrayList<CubeQueryData> findZbData(String zb,String ds,String co,String[] regs,String[] sjs,String dbcode){
        CubeWdCodes where = new CubeWdCodes();
        where.Add("zb", zb);
        where.Add("ds", ds);
        where.Add("co", co);
        where.Add("reg", Arrays.asList(regs));
        where.Add("sj", Arrays.asList(sjs));
        return RegdataService.queryData(dbcode, where);
    }

}
