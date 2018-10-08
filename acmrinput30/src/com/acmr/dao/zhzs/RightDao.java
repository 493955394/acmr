package com.acmr.dao.zhzs;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.zhzs.OraRightDaoImpl;

public class RightDao {
    public static class Fator{
        private static RightDao dataDao = new RightDao();
        public static RightDao getInstance(){
            return dataDao;
        }

    }
    IRightDao iRightDao;
    private RightDao(){
        String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
        switch (dbsort) {
            case "oracle":
                iRightDao = new OraRightDaoImpl();
                break;
            case "mysql":

                break;
            default:
                iRightDao = new OraRightDaoImpl();
        }
    }
    public IRightDao getIndexdatadao() {
        return iRightDao;
    }

}
