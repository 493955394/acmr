package com.acmr.dao.zhzs;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.zhzs.OraWeightEditDaoImpl;

public class WeightEditDao {

    public static class Fator{
        private static WeightEditDao weightEditDao = new WeightEditDao();
        public static WeightEditDao getInstance(){
            return weightEditDao;
        }

    }
    IWeightEditDao iWeightEditDao;
    private WeightEditDao(){
        String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
        switch (dbsort) {
            case "oracle":
                iWeightEditDao = new OraWeightEditDaoImpl();
                break;
            case "mysql":

                break;
            default:
                iWeightEditDao = new OraWeightEditDaoImpl();
        }
    }
    public IWeightEditDao getIndexdatadao() {
        return iWeightEditDao;
    }
}
