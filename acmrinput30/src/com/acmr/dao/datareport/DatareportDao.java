package com.acmr.dao.datareport;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.datareport.OraDatareportDaoImpl;
import com.acmr.dao.sqlserver.datareport.SqlDatareportDaoImpl;

public class DatareportDao {

	public static class Fator {
		public static DatareportDao dao = new DatareportDao();

		public static DatareportDao getInstance() {
			return dao;
		}
	}

	IDatareportDao dao;

	private DatareportDao() {
		String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
		switch (dbsort) {
		case "oracle":
			dao = new OraDatareportDaoImpl();
			break;
		case "mysql":
			break;
		case "sqlserver":
			dao = new SqlDatareportDaoImpl();
			break;
		default:
			dao = new OraDatareportDaoImpl();
		}
	}

	public IDatareportDao getDao() {
		return dao;
	}
}
