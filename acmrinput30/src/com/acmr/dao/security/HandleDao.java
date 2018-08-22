package com.acmr.dao.security;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.security.OraHandleDaoImpl;
import com.acmr.dao.sqlserver.security.SqlHandleDaoImpl;

public class HandleDao {
	public static class Fator {
		private static HandleDao handledao = new HandleDao();

		public static HandleDao getInstance() {
			return handledao;
		}
	}

	IhandleDao handledao;
	private HandleDao() {
		String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
		switch (dbsort) {
		case "oracle":
			handledao = new OraHandleDaoImpl();
			break;
		case "mysql":
			//Securitydao = new MysTestDaoImpl();
			break;
		case "sqlserver":
			handledao = new SqlHandleDaoImpl();
			break;
		default:
			handledao = new OraHandleDaoImpl();
		}
	}

	public IhandleDao getHandleDao() {
		return handledao;
	}
}
