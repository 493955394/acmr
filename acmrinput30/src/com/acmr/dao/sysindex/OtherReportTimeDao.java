package com.acmr.dao.sysindex;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.sysindex.OraOtherReportTimeDaoImpl;
import com.acmr.dao.sqlserver.sysindex.SqlOtherReportTimeDaoImpl;

public class OtherReportTimeDao {
	public static class Fator {
		private static OtherReportTimeDao dao = new OtherReportTimeDao();

		public static OtherReportTimeDao getInstance() {
			return dao;
		}
	}

	IOtherReportTimeDao dao;

	private OtherReportTimeDao() {
		String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
		switch (dbsort) {
		case "oracle":
			dao = new OraOtherReportTimeDaoImpl();
			break;
		case "mysql":
			// iTemDao = new MysTestDaoImpl();
			break;
		case "sqlserver":
			dao = new SqlOtherReportTimeDaoImpl();
			break;
		default:
			dao = new OraOtherReportTimeDaoImpl();
		}
	}

	public IOtherReportTimeDao getDao() {
		return dao;
	}
}
