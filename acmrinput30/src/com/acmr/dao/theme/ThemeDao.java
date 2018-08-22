package com.acmr.dao.theme;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.theme.OraThemeDaoImpl;
import com.acmr.dao.sqlserver.theme.SqlThemeDaoImpl;

public class ThemeDao {
	public static class Fator {
		private static ThemeDao dao = new ThemeDao();

		public static ThemeDao getInstance() {
			return dao;
		}
	}

	IThemeDao dao;

	private ThemeDao() {
		String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
		switch (dbsort) {
		case "oracle":
			dao = new OraThemeDaoImpl();
			break;
		case "mysql":
			//
			break;
		case "sqlserver":
			dao = new SqlThemeDaoImpl();
			break;
		default:
			dao = new OraThemeDaoImpl();
		}
	}

	public IThemeDao getDao() {
		return dao;
	}
}
