package com.acmr.service.zhzs;

import acmr.util.PubInfo;
import com.acmr.dao.zhzs.IndexListDao;

public class IndexListService {
    static String name = IndexListDao.Fator.getInstance().getIndexdatadao().getName();

    public static void main(String[] args) {
        PubInfo.printStr(name);
    }
}
