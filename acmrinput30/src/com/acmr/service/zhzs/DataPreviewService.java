package com.acmr.service.zhzs;

import acmr.cubequery.service.cubequery.entity.CubeQueryData;
import acmr.cubequery.service.cubequery.entity.CubeWdCodes;
import acmr.math.CalculateExpression;
import acmr.math.entity.MathException;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.dao.zhzs.DataPreviewDao;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.model.zhzs.DataPreview;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.service.zbdata.OriginService;
import com.acmr.service.zbdata.RegdataService;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:djj
 * @date: 2018/12/5 11:08/**
 * 启用前的预览结果
 * @author:djj
 * @date: 2018/12/5 11:08
 */
public class DataPreviewService {
    private CalculateExpression ce = new CalculateExpression();
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
     * 计算的方法,只算指标
     */
    public boolean calculateZB(String time, String regs,String icode) throws MathException {
        String [] reg = regs.split(",");
        IndexEditService indexEditService = new IndexEditService();
        OriginService originService = new OriginService();
        String dbcode = IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
        List<DataPreview> newadd = new ArrayList<>();
        List<IndexMoudle> data = getModuleFormula(icode,"0");//取的是指标的list
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
                    for (int k = 0; k <zbs.size() ; k++) {
                        if(data.get(i).getFormula().contains(zbs.get(k).get("code").toString())){//要是存在这个code,就去取对应的zbcode
                            CubeWdCodes where = new CubeWdCodes();
                            where.Add("zb", zbs.get(k).get("code").toString());
                            where.Add("ds", zbs.get(k).get("dscode").toString());
                            where.Add("co", zbs.get(k).get("cocode").toString());
                            where.Add("reg", reg[j]);
                            where.Add("sj", time);
                            ArrayList<CubeQueryData> result = RegdataService.queryData(dbcode, where);
                            if(!(result.size() ==0)){//如果有值的话
                                //单位换算
                                String funit=originService.getwdnode("zb",zbs.get(k).get("code").toString(),dbcode).getUnitcode();
                                BigDecimal rate = new BigDecimal(originService.getRate(funit,zbs.get(k).get("unitcode").toString(),time));
                                BigDecimal orval = (new BigDecimal(result.get(0).getData().getStrdata())).multiply(rate);
                                BigDecimal val = orval.setScale(Integer.parseInt(data.get(i).getDacimal()));//截取小数点
                                da.setData(val.toPlainString());
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
                    DataPreview da = new DataPreview();
                    da.setAyearmon(time);
                    da.setRegion(reg[j]);
                    da.setIndexcode(icode);
                    da.setModcode(data.get(i).getCode());
                    boolean flag = false;
                    for (int k = 0; k <zbs.size() ; k++) {
                        if(formula.contains(zbs.get(k).get("code").toString())){//要是存在这个code,就去取对应的zbcode
                            CubeWdCodes where = new CubeWdCodes();
                            where.Add("zb", zbs.get(k).get("code").toString());
                            where.Add("ds", zbs.get(k).get("dscode").toString());
                            where.Add("co", zbs.get(k).get("cocode").toString());
                            where.Add("reg", reg[j]);
                            where.Add("sj", time);
                            ArrayList<CubeQueryData> result = RegdataService.queryData(dbcode, where);
                            //替换公式中的值
                            if(result.size() ==0){
//                                formula = formula.replace("#"+zbs.get(k).getProcode()+"#","0");//换成0
                                flag = true;
                                break;
                            }
                            else{
                                //单位换算
                                String funit=originService.getwdnode("zb",zbs.get(k).get("code").toString(),dbcode).getUnitcode();
                                BigDecimal rate = new BigDecimal(originService.getRate(funit,zbs.get(k).get("unitcode").toString(),time));
                                BigDecimal orval = (new BigDecimal(result.get(0).getData().getStrdata())).multiply(rate);
                                formula = formula.replace("#"+zbs.get(k).get("code").toString()+"#",orval.toPlainString());//换成对应的value
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
    public  void calculateZS(String code, String time, String reg) throws MathException{
        DataPreview zsdata = new DataPreview();
        IndexEditService originDataService = new IndexEditService();
        List<IndexMoudle> subs = findSubMod(code);
        int check = subDataCheck(subs,reg,time);
        if(check ==1){//下一级的值不全
            for (int i = 0; i <subs.size() ; i++) {
                //先去检查数据库有没有值，有值就不算了
                List<IndexMoudle> tmp =new ArrayList<>();
                tmp.add(subs.get(i));
                if(subDataCheck(tmp,reg,time)==1){
                    calculateZS(subs.get(i).getCode(),time,reg);
                }
            }
            calculateZS(code,time,reg);//计算一遍它的父级
        }
        else {//要是下一级的值是全的，就把值加到zsdatas中
            IndexMoudle temp = originDataService.getData(code);
            zsdata.setAyearmon(time);
            zsdata.setRegion(reg);
            zsdata.setIndexcode(temp.getIndexcode());
            zsdata.setModcode(temp.getCode());
            String formula = "";
            boolean flag = false;
            for (int i = 0; i < subs.size(); i++) {
                String data = getzbvalue(subs.get(i).getCode(),reg,time);
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
   获取对应任务的指数或者指标的list
    */
    public List<IndexMoudle> getModuleFormula(String icode,String ifzs){
        List<IndexMoudle> indexModules = new ArrayList<>();
        List<DataTableRow> data = DataPreviewDao.Fator.getInstance().getIndexdatadao().getModuleData(icode,ifzs).getRows();
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

    /**
     * 查询MODULE表submod
     */
    public List<IndexMoudle> findSubMod(String code){
        List<IndexMoudle> taskModules = new ArrayList<>();
        List<DataTableRow> data =new ArrayList<>();
        data = DataPreviewDao.Fator.getInstance().getIndexdatadao().getSubMod(code).getRows();
        for (int i = 0; i <data.size() ; i++) {
            IndexMoudle taskModule = new IndexMoudle();
            taskModule.setCode(data.get(i).getString("code"));
            taskModule.setCname(data.get(i).getString("cname"));
            taskModule.setIndexcode(data.get(i).getString("taskcode"));
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
    public int subDataCheck(List<IndexMoudle> iModules, String reg, String time){
        int i = 0;
        for (int j = 0; j <iModules.size() ; j++) {
            int temp = DataPreviewDao.Fator.getInstance().getIndexdatadao().subDataCheck(iModules.get(j).getCode(),reg,time);
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
    public String getzbvalue(String modcode,String region,String time){
        String value = "";
        DataTable data = DataPreviewDao.Fator.getInstance().getIndexdatadao().getData(modcode,region,time);
        if(data.getRows().size()!=0){
            value = data.getRows().get(0).getString("data");
        }
        return value;
    }
}
