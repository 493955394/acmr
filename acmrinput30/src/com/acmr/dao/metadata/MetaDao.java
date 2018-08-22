package com.acmr.dao.metadata;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.metadata.OraMetaDaoImpl;
import com.acmr.dao.sqlserver.metadao.SqlMetaDaoImpl;

public class MetaDao {
	public static class Fator {
		private static MetaDao dao = new MetaDao();

		public static MetaDao getInstance() {
			return dao;
		}
	}

	IMetaDao dao;

	private MetaDao() {
		String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
		switch (dbsort) {
		case "oracle":
			dao = new OraMetaDaoImpl();
			break;
		case "mysql":
			break;
		case "sqlserver":
			dao = new SqlMetaDaoImpl();
			break;
		default:
			dao = new OraMetaDaoImpl();
		}
	}

	public IMetaDao getDao() {
		return dao;
	}

}
