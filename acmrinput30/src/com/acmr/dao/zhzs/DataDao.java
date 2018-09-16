package com.acmr.dao.zhzs;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.zhzs.OraDataDaoImpl;
import com.acmr.dao.oracle.zhzs.OraIndexListDaoImpl;

public class DataDao {
    public static class Fator{
        private static DataDao dataDao = new DataDao();
        public static DataDao getInstance(){
            return dataDao;
        }

    }
    IDataDao iDataDao;
    private DataDao(){
        String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
        switch (dbsort) {
            case "oracle":
                iDataDao = new OraDataDaoImpl();
                break;
            case "mysql":

                break;
            default:
                iDataDao = new OraDataDaoImpl();
        }
    }
    public IDataDao getIndexdatadao() {
        return iDataDao;
    }

}
