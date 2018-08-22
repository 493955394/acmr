package com.acmr.dao.sysindex;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.sysindex.OraUniteTimeDaoImpl;
import com.acmr.dao.sqlserver.sysindex.SqlUniteTimeDaoImpl;

public class UniteTimeDao {
	public static class Fator {
		private static UniteTimeDao dao = new UniteTimeDao();

		public static UniteTimeDao getInstance() {
			return dao;
		}
	}

	IUniteTimeDao dao;

	private UniteTimeDao() {
		String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
		switch (dbsort) {
		case "oracle":
			dao = new OraUniteTimeDaoImpl();
			break;
		case "mysql":
			// iTemDao = new MysTestDaoImpl();
			break;
		case "sqlserver":
			dao = new SqlUniteTimeDaoImpl();
			break;
		default:
			dao = new OraUniteTimeDaoImpl();
		}
	}

	public IUniteTimeDao getDao() {
		return dao;
	}
}
