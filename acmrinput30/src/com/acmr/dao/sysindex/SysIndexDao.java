package com.acmr.dao.sysindex;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.sysindex.OraSysIndexDaoImpl;
import com.acmr.dao.sqlserver.sysindex.SqlSysIndexDaoImpl;

public class SysIndexDao {

	public static class Fator {
		private static SysIndexDao dao = new SysIndexDao();

		public static SysIndexDao getInstance() {
			return dao;
		}
	}

	ISysIndexDao dao;

	private SysIndexDao() {
		String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
		switch (dbsort) {
		case "oracle":
			dao = new OraSysIndexDaoImpl();
			break;
		case "mysql":
			// iTemDao = new MysTestDaoImpl();
			break;
		case "sqlserver":
			 dao = new SqlSysIndexDaoImpl();
			break;
		default:
			dao = new OraSysIndexDaoImpl();
		}
	}

	public ISysIndexDao getDao() {
		return dao;
	}
}
