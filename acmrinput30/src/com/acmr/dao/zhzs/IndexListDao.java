package com.acmr.dao.zhzs;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.zhzs.OraIndexListDaoImpl;
import com.acmr.model.zhzs.IndexCategory;
import com.acmr.web.jsp.Index;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class IndexListDao {
    public static class Fator{
        private static IndexListDao indexListDao = new IndexListDao();
        public static IndexListDao getInstance(){
            return indexListDao;
        }

    }
    IIndexListDao iIndexListDao;
    private IndexListDao(){
        String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
        switch (dbsort) {
            case "oracle":
                 iIndexListDao = new OraIndexListDaoImpl();
                break;
            case "mysql":

                break;
            default:
                iIndexListDao = new OraIndexListDaoImpl();
        }
    }
    public IIndexListDao getIndexdatadao() {
        return iIndexListDao;
    }

}
