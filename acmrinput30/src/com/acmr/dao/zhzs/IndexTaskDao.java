package com.acmr.dao.zhzs;


import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.zhzs.OraIndexTaskDaoImpl;

public class IndexTaskDao {
    public static class Fator{
        private static IndexTaskDao indexTaskDao = new IndexTaskDao();
        public static IndexTaskDao getInstance(){
            return indexTaskDao;
        }

    }
    IIndexTaskDao iIndexTaskDao;
    private IndexTaskDao(){
        String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
        switch (dbsort) {
            case "oracle":
                iIndexTaskDao = new OraIndexTaskDaoImpl();
                break;
            case "mysql":

                break;
            default:
                iIndexTaskDao = new OraIndexTaskDaoImpl();
        }
    }
    public IIndexTaskDao getIndexdatadao() {
        return iIndexTaskDao;
    }

}
