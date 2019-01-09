package com.acmr.dao.zhzs;


import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.zhzs.OraRightDaoImpl;
import com.acmr.dao.oracle.zhzs.OraSchemeDaoImpl;

public class SchemeDao {
    public static class Fator{
        private static SchemeDao schemeDao = new SchemeDao();
        public static SchemeDao getInstance(){
            return schemeDao;
        }

    }
    ISchemeDao ischemeDao;
    private SchemeDao(){
        String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
        switch (dbsort) {
            case "oracle":
                ischemeDao = new OraSchemeDaoImpl();
                break;
            case "mysql":

                break;
            default:
                ischemeDao = new OraSchemeDaoImpl();
        }
    }
    public ISchemeDao getIndexdatadao() {
        return ischemeDao;
    }
}
