package com.acmr.service.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.DataDao;

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
}
