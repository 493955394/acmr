package com.acmr.service.zhzs;

import acmr.cubequery.service.cubequery.entity.CubeNode;
import acmr.cubequery.service.cubequery.entity.CubeQueryData;
import acmr.cubequery.service.cubequery.entity.CubeWdCodes;
import acmr.math.CalculateExpression;
import acmr.math.entity.MathException;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.dao.zhzs.DataPreviewDao;
import com.acmr.dao.zhzs.IDataPreviewDao;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.dao.zhzs.IndexTaskDao;
import com.acmr.model.zhzs.DataPreview;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.model.zhzs.Scheme;
import com.acmr.model.zhzs.TaskZb;
import com.acmr.service.zbdata.OriginService;
import com.acmr.service.zbdata.RegdataService;
import org.apache.commons.lang.StringUtils;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author:djj
 * @date: 2018/12/5 11:08/**
 * 启用前的预览结果
 * @author:djj
 * @date: 2018/12/5 11:08
 */
public class DataPreviewService {
    private CalculateExpression ce = new CalculateExpression();

    public boolean todocalculate(String icode,String time,String scode){
        String regs = findRegions(icode);
        String [] reg = regs.split(",");
        //开始计算指数的值，包括乘上weight
        try {
            if(calculateZB(time,regs,icode,scode)){
                //指标已经算完
                List<IndexMoudle> zong = findRoot(icode);
                for (int i = 0; i <zong.size() ; i++) {
                    for (int j = 0; j <reg.length ; j++) {//一个地区一个地区地算
                        calculateZS(zong.get(i).getCode(),time,reg[j],scode);
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
     * 自定义公式计算函数
     */
    public String tocalculate(String formula,String dacimal){
        String result="";
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
     * 计算的方法,只算指标
     */
    public boolean calculateZB(String time, String regs,String icode,String scode) throws MathException {
        String [] reg = regs.split(",");
        IndexEditService indexEditService = new IndexEditService();
        OriginService originService = new OriginService();
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        List<DataPreview> newadd = new ArrayList<>();
        List<IndexMoudle> data = getModuleFormula(icode,"0",scode);//取的是指标的list
        List<Map>  zbs = indexEditService.getZBS(icode);
        for (int i = 0; i <data.size() ; i++) {//开始循环
            if(data.get(i).getIfzb().equals("1")){//如果是直接用筛选的指标的话直接去查值
                //先去查tb_coindex_zb
                for (int j = 0; j <reg.length ; j++) {
                    DataPreview da = new DataPreview();
                    da.setAyearmon(time);
                    da.setRegion(reg[j]);
                    da.setIndexcode(icode);
                    da.setModcode(data.get(i).getCode());
                    da.setDacimal(data.get(i).getDacimal());
                    da.setScode(scode);
                    for (int k = 0; k <zbs.size() ; k++) {
                        if(data.get(i).getFormula().contains(zbs.get(k).get("code").toString())){//要是存在这个code,就去取对应的zbcode
                            ArrayList<CubeQueryData> result = findZbData(zbs.get(k).get("zbcode").toString(),zbs.get(k).get("dscode").toString(),zbs.get(k).get("cocode").toString(),reg[j],time,dbcode);
                            if(result.size()>0){
                                if(!result.get(0).getData().getStrdata().equals("")){//如果有值的话
                                    //单位换算
                                    String funit=originService.getwdnode("zb",zbs.get(k).get("zbcode").toString(),dbcode).getUnitcode();
                                    BigDecimal rate = new BigDecimal(originService.getRate(funit,zbs.get(k).get("unitcode").toString(),time));
                                    BigDecimal orval = (new BigDecimal(result.get(0).getData().getStrdata())).multiply(rate);
                                    BigDecimal val = orval.setScale(Integer.parseInt(data.get(i).getDacimal()),RoundingMode.CEILING);//截取小数点
                                    da.setData(val.toPlainString());
                                }
                                else {
                                    da.setData("");
                                }
                            }
                           else{//要是没有值
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
                    DataPreview da = new DataPreview();
                    da.setAyearmon(time);
                    da.setRegion(reg[j]);
                    da.setIndexcode(icode);
                    da.setModcode(data.get(i).getCode());
                    da.setDacimal(data.get(i).getDacimal());
                    da.setScode(scode);
                    boolean flag = false;
                    //先处理特殊的getvalue函数
                    formula = specialMath(formula,zbs,time,reg[j],regs,icode,dbcode);
                    if(formula.equals("false")){  //getvalue函数里没数
                        flag = true;
                    }
                    else {//要是数组函数没有错，开始算别的
                        for (int k = 0; k < zbs.size(); k++) {
                            if (formula.contains(zbs.get(k).get("code").toString())) {//要是存在这个code,就去取对应的zbcode
                                CubeWdCodes where = new CubeWdCodes();
                                where.Add("zb", zbs.get(k).get("zbcode").toString());
                                where.Add("ds", zbs.get(k).get("dscode").toString());
                                where.Add("co", zbs.get(k).get("cocode").toString());
                                where.Add("reg", reg[j]);
                                where.Add("sj", time);
                                ArrayList<CubeQueryData> result = RegdataService.queryData(dbcode, where);
                                //替换公式中的值
                                if ((result.size() != 0 && result.get(0).getData().getStrdata().equals("")) || result.size() == 0) {
//                                formula = formula.replace("#"+zbs.get(k).getProcode()+"#","0");//换成0
                                    flag = true;
                                    break;
                                } else {
                                    //单位换算
                                    String funit = originService.getwdnode("zb", zbs.get(k).get("zbcode").toString(), dbcode).getUnitcode();
                                    BigDecimal rate = new BigDecimal(originService.getRate(funit, zbs.get(k).get("unitcode").toString(), time));
                                    BigDecimal orval = (new BigDecimal(result.get(0).getData().getStrdata())).multiply(rate);
                                    formula = formula.replace("#" + zbs.get(k).get("code").toString() + "#", orval.toPlainString());//换成对应的value
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
        int i=addDataresult(newadd);
        if (i!=1){
            return false;
        }
        return true;
    }
    /**
     * 计算指数（乘上权重），递归
     */
    public  void calculateZS(String code, String time, String reg,String scode) throws MathException{
        DataPreview zsdata = new DataPreview();
        IndexEditService originDataService = new IndexEditService();
        List<IndexMoudle> subs = findSubMod(code,scode);
        int check = subDataCheck(subs,reg,time,scode);
        if(check ==1){//下一级的值不全
            for (int i = 0; i <subs.size() ; i++) {
                //先去检查数据库有没有值，有值就不算了
                List<IndexMoudle> tmp =new ArrayList<>();
                tmp.add(subs.get(i));
                if(subDataCheck(tmp,reg,time,scode)==1){
                    calculateZS(subs.get(i).getCode(),time,reg,scode);
                }
            }
            calculateZS(code,time,reg,scode);//计算一遍它的父级
        }
        else {//要是下一级的值是全的，就把值加到zsdatas中
            IndexMoudle temp = originDataService.getData(code);
            zsdata.setAyearmon(time);
            zsdata.setRegion(reg);
            zsdata.setIndexcode(temp.getIndexcode());
            zsdata.setModcode(temp.getCode());
            zsdata.setDacimal(temp.getDacimal());
            zsdata.setScode(scode);
            String formula = "";
            boolean flag = false;
            for (int i = 0; i < subs.size(); i++) {
                String data = getzbvalue(subs.get(i).getCode(),reg,time,scode);
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
            addzsdata(zsdata);
        }
    }

    /*
   获取对应计划的指数或者指标的list
    */
    public List<IndexMoudle> getModuleFormula(String icode,String ifzs,String scode){
        List<IndexMoudle> indexModules = new ArrayList<>();
        IndexSchemeService ss = new IndexSchemeService();
        List<DataTableRow> data = DataPreviewDao.Fator.getInstance().getIndexdatadao().getModuleData(icode,ifzs).getRows();
        for (int i = 0; i <data.size(); i++) {
            IndexMoudle iModule = new IndexMoudle();
            String modcode = data.get(i).getString("code");
            String indexcode = data.get(i).getString("indexcode");
            //找到方案对应的公式和权重
            IndexMoudle scheme= ss.getModData(modcode,indexcode,scode);
            iModule.setCode(modcode);
            iModule.setCname(data.get(i).getString("cname"));
            iModule.setIndexcode(indexcode);
            iModule.setProcode(data.get(i).getString("procode"));
            iModule.setIfzs(data.get(i).getString("ifzs"));
            iModule.setIfzb(scheme.getIfzb());//替换成方案对应的权重和公式，同下
            iModule.setFormula(scheme.getFormula());
            iModule.setWeight(scheme.getWeight());
            iModule.setSortcode(data.get(i).getString("sortcode"));
            iModule.setDacimal(data.get(i).getString("dacimal"));
            indexModules.add(iModule);
        }

        return indexModules;
    }

    /**
     * 查询MODULE表submod
     */
    public List<IndexMoudle> findSubMod(String code,String scode){
        List<IndexMoudle> taskModules = new ArrayList<>();
        List<DataTableRow> data =new ArrayList<>();
        IndexSchemeService ss = new IndexSchemeService();
        data = DataPreviewDao.Fator.getInstance().getIndexdatadao().getSubMod(code).getRows();
        for (int i = 0; i <data.size() ; i++) {
            IndexMoudle taskModule = new IndexMoudle();
            String modcode = data.get(i).getString("code");
            String indexcode = data.get(i).getString("indexcode");
            //找到方案对应的公式和权重
            IndexMoudle scheme= ss.getModData(modcode,indexcode,scode);
            taskModule.setCode(modcode);
            taskModule.setCname(data.get(i).getString("cname"));
            taskModule.setIndexcode(indexcode);
            taskModule.setProcode(data.get(i).getString("procode"));
            taskModule.setIfzs(data.get(i).getString("ifzs"));
            taskModule.setIfzb(scheme.getIfzb());
            taskModule.setFormula(scheme.getFormula());
            taskModule.setWeight(scheme.getWeight());
            taskModule.setSortcode(data.get(i).getString("sortcode"));
            taskModule.setDacimal(data.get(i).getString("dacimal"));
            taskModules.add(taskModule);
        }
        return taskModules;
    }

    /**
     * 查询data_result_tmp中是否有值，返回1代表缺值
     */
    public int subDataCheck(List<IndexMoudle> iModules, String reg, String time,String scode){
        int i = 0;
        for (int j = 0; j <iModules.size() ; j++) {
            int temp = DataPreviewDao.Fator.getInstance().getIndexdatadao().subDataCheck(iModules.get(j).getCode(),reg,time,scode);
            if(temp == 1 ){
                i = 1;//证明缺值
            }
        }
        return i;
    }

    /**
     * 将数据插入data_preview
     */
    public int addDataresult(List<DataPreview> dataResults){return DataPreviewDao.Fator.getInstance().getIndexdatadao().addDataResult(dataResults);}
    public int addzsdata(DataPreview dataResult){return DataPreviewDao.Fator.getInstance().getIndexdatadao().addZSData(dataResult);}

    /**
     * 查询对应的data值
     */
    public String getzbvalue(String modcode,String region,String time,String scode){
        String value = "";
        DataTable data = DataPreviewDao.Fator.getInstance().getIndexdatadao().getData(modcode,region,time,scode);
        if(data.getRows().size()!=0){
            value = data.getRows().get(0).getString("data");
        }
        return value;
    }
    /**
     * 通过icode 查询总指数
     */
    public List<IndexMoudle> findRoot(String icode){
        List<IndexMoudle> indexModules = new ArrayList<>();
        List<DataTableRow> data = DataPreviewDao.Fator.getInstance().getIndexdatadao().getRootData(icode).getRows();
        for (int i = 0; i <data.size(); i++) {
            IndexMoudle iModule = new IndexMoudle();
            iModule.setCode(data.get(i).getString("code"));
            iModule.setCname(data.get(i).getString("cname"));
            iModule.setIndexcode(data.get(i).getString("indexcode"));
            iModule.setProcode(data.get(i).getString("procode"));
            iModule.setIfzs(data.get(i).getString("ifzs"));
            iModule.setIfzb(data.get(i).getString("ifzb"));
            iModule.setFormula(data.get(i).getString("formula"));
            iModule.setWeight(data.get(i).getString("weight"));
            iModule.setSortcode(data.get(i).getString("sortcode"));
            iModule.setDacimal(data.get(i).getString("dacimal"));
            indexModules.add(iModule);
        }

        return indexModules;
    }
    public String findRegions(String icode){
        return DataPreviewDao.Fator.getInstance().getIndexdatadao().findRegions(icode);
    }
    public DataPreview getData(String modcode,String region,String time,String scode){
        List<DataTableRow> data =  DataPreviewDao.Fator.getInstance().getIndexdatadao().getData(modcode,region,time,scode).getRows();
        DataPreview row = new DataPreview();
       if(data.size()>0){
            row.setIndexcode(data.get(0).getString("indexcode"));
            row.setAyearmon(data.get(0).getString("ayearmon"));
            row.setDacimal(data.get(0).getString("dacimal"));
            row.setData(data.get(0).getString("data"));
            row.setModcode(data.get(0).getString("modcode"));
            row.setRegion(data.get(0).getString("region"));
        }
        return row;
    }

    /**
     * 校验时计算那个特殊的自定义函数getvalue
     */
    public String specialMath(String formulatext,List<Map> zbs,String time, String thisreg, String regs, String icode,String dbcode){
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

    public String getvalueMath(String orgStr, List<Map> zbs, String time,String thisreg,String regs,String icode,String dbcode) {
        String data = "false";//默认没有值
        String text = orgStr;
        OriginDataService originDataService = new OriginDataService();
      OriginService  originService = new OriginService();
        if(!text.contains(",")){//不包含逗号，说明是单个的zb,当前时间当前地区直接找值就可以
            for (int k = 0; k <zbs.size() ; k++) {
                if (text.contains(zbs.get(k).get("code").toString())) {//要是存在这个code,就去取对应的zbcode
                    String val = "";
                    ArrayList<CubeQueryData> result = findZbData(zbs.get(k).get("zbcode").toString(),zbs.get(k).get("dscode").toString(),zbs.get(k).get("cocode").toString(),thisreg,time,dbcode);
                    if(result.size()>0){
                        if(!result.get(0).getData().getStrdata().equals("")){//如果有值的话
                            //单位换算
                            String funit=originService.getwdnode("zb",zbs.get(k).get("zbcode").toString(),dbcode).getUnitcode();
                            BigDecimal rate = new BigDecimal(originService.getRate(funit,zbs.get(k).get("unitcode").toString(),time));
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
                    if (text.contains(zbs.get(k).get("code").toString())) {//要是存在这个code,就去取对应的zbcode
                        String val = "";
                        ArrayList<CubeQueryData> result = findZbData(zbs.get(k).get("zbcode").toString(),zbs.get(k).get("dscode").toString(),zbs.get(k).get("cocode").toString(),thisreg,time,dbcode);
                        if(result.size()>0){
                            if(!result.get(0).getData().getStrdata().equals("")){//如果有值的话
                                //单位换算
                                String funit=originService.getwdnode("zb",zbs.get(k).get("zbcode").toString(),dbcode).getUnitcode();
                                BigDecimal rate = new BigDecimal(originService.getRate(funit,zbs.get(k).get("unitcode").toString(),time));
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
                    if (modcode.contains(zbs.get(k).get("code").toString())) {//要是存在这个modcode,就去取对应的zbcode
                        zbcode = zbs.get(k).get("zbcode").toString();
                        ds = zbs.get(k).get("dscode").toString();
                        co = zbs.get(k).get("cocode").toString();
                        unitcode = zbs.get(k).get("unitcode").toString();
                    }
                }
                if (!zbcode.equals("")) {//要是存在这个code,就去取对应的zbcode
                if (wd.equals("dq")) {//如果是所有地区，当期时间
                    String[] reg = regs.split(",");
                    String tmp = "";
                    for (int k = 0; k < reg.length; k++) {
                        String val = "";
                        ArrayList<CubeQueryData> result = findZbData(zbcode, ds, co, reg[k], time, dbcode);
                        if (result.size() > 0) {
                            if (!result.get(0).getData().getStrdata().equals("")) {//如果有值的话
                                //单位换算
                                String funit = originService.getwdnode("zb", zbcode, dbcode).getUnitcode();
                                BigDecimal rate = new BigDecimal(originService.getRate(funit, unitcode, time));
                                BigDecimal orval = (new BigDecimal(result.get(0).getData().getStrdata())).multiply(rate);
                                val = orval.toPlainString();
                            }
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
                    for (int i = 0; i < sjs.size(); i++) {
                        String val = "";
                        ArrayList<CubeQueryData> result = findZbData(zbcode, ds, co, thisreg, sjs.get(i).getCode(), dbcode);
                        if (result.size() > 0) {
                            if (!result.get(0).getData().getStrdata().equals("")) {//如果有值的话
                                //单位换算
                                String funit = originService.getwdnode("zb", zbcode, dbcode).getUnitcode();
                                BigDecimal rate = new BigDecimal(originService.getRate(funit, unitcode, time));
                                BigDecimal orval = (new BigDecimal(result.get(0).getData().getStrdata())).multiply(rate);
                                val = orval.toPlainString();
                            }
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
                    for (int i = 0; i < sjs.size(); i++) {
                        String val = "";
                        ArrayList<CubeQueryData> result = findZbData(zbcode, ds, co, thisreg, sjs.get(i).getCode(), dbcode);
                        if (result.size() > 0) {
                            if (!result.get(0).getData().getStrdata().equals("")) {//如果有值的话
                                //单位换算
                                String funit = originService.getwdnode("zb", zbcode, dbcode).getUnitcode();
                                BigDecimal rate = new BigDecimal(originService.getRate(funit, unitcode, time));
                                BigDecimal orval = (new BigDecimal(result.get(0).getData().getStrdata())).multiply(rate);
                                val = orval.toPlainString();
                            }
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
    public ArrayList<CubeQueryData> findZbData(String zb,String ds,String co,String reg,String sj,String dbcode){
        CubeWdCodes where = new CubeWdCodes();
        where.Add("zb", zb);
        where.Add("ds", ds);
        where.Add("co", co);
        where.Add("reg", reg);
        where.Add("sj", sj);
        return RegdataService.queryData(dbcode, where);
    }
}
