package com.acmr.dao.sysindex;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.oracle.sysindex.OraFlowIndexDaoImpl;
import com.acmr.dao.sqlserver.sysindex.SqlFlowIndexDaoImpl;

/**
 * 
 * 类名称: FlowIndexDaoImpl 
 * 描述(对这个类进行描述):工作流流程定义接口实现类 
 *
 */
public class FlowIndexDao {
    
	public static class Fator {
		private static FlowIndexDao dao = new FlowIndexDao();

		public static FlowIndexDao getInstance() {
			return dao;
		}
	}

	IFlowIndexDao dao;

	private FlowIndexDao() {
		String dbsort = AcmrInputDPFactor.getDataPool().getDbsort().toLowerCase();
		switch (dbsort) {
		case "oracle":
			dao = new OraFlowIndexDaoImpl();
			break;
		case "mysql":
			// iTemDao = new MysTestDaoImpl();
			break;
		case "sqlserver":
			dao = new SqlFlowIndexDaoImpl();
			break;
		default:
			dao = new OraFlowIndexDaoImpl();
		}
	}

	public IFlowIndexDao getDao() {
		return dao;
	}
}
