package com.acmr.dao.sysindex;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.sysindex.OraTimePlanDaoImpl;
import com.acmr.dao.sqlserver.sysindex.SqlTimePlanDaoImpl;

public class TimePlanDao {
	public static class Fator {
		private static TimePlanDao dao = new TimePlanDao();

		public static TimePlanDao getInstance() {
			return dao;
		}
	}

	ITimePlanDao dao;

	private TimePlanDao() {
		String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
		switch (dbsort) {
		case "oracle":
			dao = new OraTimePlanDaoImpl();
			break;
		case "mysql":
			// iTemDao = new MysTestDaoImpl();
			break;
		case "sqlserver":
			dao = new SqlTimePlanDaoImpl();
			break;
		default:
			dao = new OraTimePlanDaoImpl();
		}
	}

	public ITimePlanDao getDao() {
		return dao;
	}
}
