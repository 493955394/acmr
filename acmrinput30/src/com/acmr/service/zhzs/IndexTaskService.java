package com.acmr.service.zhzs;

import com.acmr.dao.zhzs.IndexTaskDao;

public class IndexTaskService {
    public boolean findSession(String sessionid){
        Boolean bool= IndexTaskDao.Fator.getInstance().getIndexdatadao().hasData(sessionid);
        return bool;
    }
}
