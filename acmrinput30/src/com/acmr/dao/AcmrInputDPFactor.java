package com.acmr.dao;

import acmr.data.DataQuery;
import acmr.data.DataQuickQuery;

public class AcmrInputDPFactor {
	private static DataPool dp = new DataPool("acmrinputdb.properties");
	private static DataQuickQuery dqq = new DataQuickQuery(dp);

	public static DataPool getDataPool() {
		return dp;
	}
	
	public static DataQuery getDataQuery(){
		return dp.getDataQuery();
	}
	public static DataQuickQuery getQuickQuery(){
		return dqq;
	}
}
