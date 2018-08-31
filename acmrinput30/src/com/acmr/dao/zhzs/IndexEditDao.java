package com.acmr.dao.zhzs;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.zhzs.OraIndexEditDaoImpl;

public class IndexEditDao {
    public static class Fator{
        private static IndexEditDao indexEditDao = new IndexEditDao();
        public static IndexEditDao getInstance(){
            return indexEditDao;
        }
    }

    IIndexEditDao iIndexEditDao;
    private IndexEditDao(){
        String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
        switch (dbsort) {
            case "oracle":
                iIndexEditDao = new OraIndexEditDaoImpl();
                break;
            case "mysql":

                break;
            default:
                iIndexEditDao = new OraIndexEditDaoImpl();
        }
    }
    public IIndexEditDao getIndexdatadao() {
        return iIndexEditDao;
    }
}
