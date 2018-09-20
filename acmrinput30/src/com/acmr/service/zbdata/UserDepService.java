package com.acmr.service.zbdata;

import acmr.util.DataTable;
import acmr.util.PubInfo;
import com.acmr.dao.security.SecurityDao;

public class UserDepService {

    /**
    * @Description: 根据usercode返回user的name
    * @Param: [usercode]
    * @return: java.lang.String
    * @Author: lyh
    * @Date: 2018/9/20
    */
    public static String getUserNameByCode(String usercode){
        DataTable table= SecurityDao.Fator.getInstance().getSecurityDao().getUser(usercode);
        String username=table.getRows().get(0).getString("cname");
        return username;
    }



}
