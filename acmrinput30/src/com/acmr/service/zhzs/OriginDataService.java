package com.acmr.service.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.DataDao;
import com.acmr.model.taskindex.TaskIndex;
import com.acmr.model.zhzs.Data;
import com.acmr.model.zhzs.DataResult;
import com.acmr.model.zhzs.TaskModule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OriginDataService {
    /**
     * 查询对应的data值
     */
    public String getvalue(String taskcode,String zbcode,String region,String time,String sessionid){
        String value = "";
        DataTable data = DataDao.Fator.getInstance().getIndexdatadao().getData(taskcode,zbcode,region,time,sessionid);
        if(data.getRows().size()!=0){
            value = data.getRows().get(0).getString("data");
        }
        return value;
    }
    /**
     * 将数据插入data_result_tmp
     */
    public int addDataresult(List<DataResult> dataResults){return DataDao.Fator.getInstance().getIndexdatadao().addDataResult(dataResults);}
    public int addzsdata(DataResult dataResult){return DataDao.Fator.getInstance().getIndexdatadao().addZSData(dataResult);}

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
    public int subDataCheck(List<TaskModule> taskModules,String reg,String time,String sessionid){
        int i = 0;
        for (int j = 0; j <taskModules.size() ; j++) {
            int temp = DataDao.Fator.getInstance().getIndexdatadao().subDataCheck(taskModules.get(j).getTaskcode(),taskModules.get(j).getCode(),reg,time,sessionid);
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
    public String getzbvalue(String taskcode,String modcode,String reg,String time,String sessionid){return DataDao.Fator.getInstance().getIndexdatadao().getDataResult(taskcode,modcode,reg,time,sessionid);}
}
