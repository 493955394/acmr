package com.acmr.dao.grabdataindb;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.grabdataindb.OraGrabDataDaoImpl;
import com.acmr.dao.sqlserver.grabdataindb.SqlGrabDataDaoImpl;

public class GrabDataDao {
	public static class Fator {
		private static GrabDataDao graddao = new GrabDataDao();

		public static GrabDataDao getInstance() {
			return graddao;
		}
	}

	IGrabDataDao grabdatadao;

	private GrabDataDao() {
		String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
		switch (dbsort) {
		case "oracle":
			grabdatadao = new OraGrabDataDaoImpl();
			break;
		case "mysql":

			break;
		case "sqlserver":
			grabdatadao = new SqlGrabDataDaoImpl();
			break;
		default:
			grabdatadao = new OraGrabDataDaoImpl();
		}
	}

	public IGrabDataDao getGrabdatadao() {
		return grabdatadao;
	}
}
