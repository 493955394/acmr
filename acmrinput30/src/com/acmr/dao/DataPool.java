package com.acmr.dao;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.driver.OracleConnection;
import acmr.data.DataQueryPool;

public class DataPool extends DataQueryPool {
	
	public DataPool(String dirvername, String dbURl, String userName, String userPwd, String maxcount, String mincount, String usetimeout, String freetimeout) {
		super(dirvername, dbURl, userName, userPwd, maxcount, mincount, usetimeout, freetimeout);
		 
	}

	public DataPool(String pperfile) {
		super(pperfile);
		 
	}

	@Override
	protected void setProperty(Connection connl) {
		if (this.getDbsort().equalsIgnoreCase("oracle")) {
			OracleConnection connl1 = (OracleConnection) connl;
			try {
				connl1.setImplicitCachingEnabled(true);
				connl1.setStatementCacheSize(20);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(this.getDbsort().equalsIgnoreCase("mysql")){
			com.mysql.jdbc.Connection connl1=(com.mysql.jdbc.Connection) connl;
		    connl1.setUseOldAliasMetadataBehavior(true);
		}
	}

}
