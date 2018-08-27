package com.acmr.dao.zhzs;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.zhzs.OraIndexListDaoImpl;
import com.acmr.web.jsp.Index;

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
