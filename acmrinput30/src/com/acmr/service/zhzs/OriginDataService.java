package com.acmr.service.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.DataDao;
import com.acmr.model.zhzs.Data;
import com.acmr.model.zhzs.DataResult;

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
}
