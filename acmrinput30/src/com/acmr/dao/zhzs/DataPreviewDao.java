package com.acmr.dao.zhzs;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.zhzs.OraDataPreviewDaoImpl;
import com.acmr.model.zhzs.DataPreview;

/**
 * @author:djj
 * @date: 2018/12/5 10:47/**
 * @author:djj
 * @date: 2018/12/5 10:47
 */
public class DataPreviewDao {
    public static class Fator{
        private static DataPreviewDao dataPreviewDao = new DataPreviewDao();
        public static DataPreviewDao getInstance(){
            return dataPreviewDao;
        }

    }
    IDataPreviewDao iDataPreviewDao;
    private DataPreviewDao(){
        String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
        switch (dbsort) {
            case "oracle":
                iDataPreviewDao = new OraDataPreviewDaoImpl();
                break;
            case "mysql":

                break;
            default:
                iDataPreviewDao = new OraDataPreviewDaoImpl();
        }
    }
    public IDataPreviewDao getIndexdatadao() {
        return iDataPreviewDao;
    }
}
