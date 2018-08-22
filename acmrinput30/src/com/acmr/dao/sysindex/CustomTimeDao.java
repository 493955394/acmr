package com.acmr.dao.sysindex;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.sysindex.OraCustomTimeDaoImpl;
import com.acmr.dao.sqlserver.sysindex.SqlCustomTimeDaoImpl;

public class CustomTimeDao {
	public static class Fator {
		private static CustomTimeDao dao = new CustomTimeDao();

		public static CustomTimeDao getInstance() {
			return dao;
		}
	}

	ICustomTimeDao dao;

	private CustomTimeDao() {
		String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
		switch (dbsort) {
		case "oracle":
			dao = new OraCustomTimeDaoImpl();
			break;
		case "mysql":
			// iTemDao = new MysTestDaoImpl();
			break;
		case "sqlserver":
			dao = new SqlCustomTimeDaoImpl();
			break;
		default:
			dao = new OraCustomTimeDaoImpl();
		}
	}

	public ICustomTimeDao getDao() {
		return dao;
	}
}
