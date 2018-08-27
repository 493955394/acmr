package com.acmr.dao.zhzs;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.zhzs.OraIndexListDaoImpl;

public class IndexListDao {
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
