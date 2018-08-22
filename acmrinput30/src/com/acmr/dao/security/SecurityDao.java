package com.acmr.dao.security;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.security.oraSecurityDaoImpl;
import com.acmr.dao.sqlserver.security.SqlSecurityDaoImpl;

public class SecurityDao {
	public static class Fator {
		private static SecurityDao testdao = new SecurityDao();

		public static SecurityDao getInstance() {
			return testdao;
		}
	}

	ISecurityDao securitydao;
	private SecurityDao() {
		String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
		switch (dbsort) {
		case "oracle":
			securitydao = new oraSecurityDaoImpl();
			break;
		case "mysql":
			//Securitydao = new MysTestDaoImpl();
			break;
		case "sqlserver":
			securitydao = new SqlSecurityDaoImpl();
			break;
		default:
			securitydao = new oraSecurityDaoImpl();
		}
	}

	public ISecurityDao getSecurityDao() {
		return securitydao;
	}
}
