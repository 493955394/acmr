package com.acmr.dao.oracle.sysindex;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import acmr.data.DataQuery;
import acmr.data.DataQuickQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.sysindex.IOtherReportTimeDao;
import com.acmr.model.sysindex.OtherReportTime;

public class OraOtherReportTimeDaoImpl implements IOtherReportTimeDao {

	@Override
	public int insert(OtherReportTime otherReportTime) {
		String sql = "insert into TB_TIMEPLAN_OTHERREPORTTIME values(SQ_TIMEPLAN_OTHERREPORTTIME.nextval,?,?)";
		List<Object> arr = new ArrayList<Object>();
		arr.add(otherReportTime.getTimePlanId());
		arr.add(otherReportTime.getReportCode());
		int count = 0;
		DataQuery dataQuery = null;
		try {
			dataQuery = AcmrInputDPFactor.getDataQuery();
			dataQuery.beginTranse();
			count = dataQuery.executeSql(sql, arr.toArray());
			dataQuery.commit();
		} catch (SQLException e) {
			if (null != dataQuery) {
				dataQuery.rollback();
			}
			e.printStackTrace();
		} finally {
			if (null != dataQuery) {
				dataQuery.releaseConnl();
			}
		}
		return count;
	}

	@Override
	public OtherReportTime getTimeById(String id) {
		String sql = "select * from TB_TIMEPLAN_OTHERREPORTTIME where TIMEPLANID = ?";
		List<Object> parm = new ArrayList<Object>();
		parm.add(id);
		DataQuickQuery quickQuery = AcmrInputDPFactor.getQuickQuery();
		DataTable dataTable = quickQuery.getDataTableSql(sql, parm.toArray());
		return dataToTimePlan(dataTable);
	}

	/**
	 * 数据转对象
	 * 
	 * @param dataTable
	 * @return
	 */
	private OtherReportTime dataToTimePlan(DataTable dataTable) {
		if (dataTable == null || dataTable.getRows().size() == 0) { // 如果没有查询到数据返回
			return null;
		}
		List<DataTableRow> dataTableRows = dataTable.getRows();
		for (DataTableRow row : dataTableRows) {
			OtherReportTime plan = new OtherReportTime();
			plan.setId(row.getString("ID"));
			plan.setReportCode(row.getString("REPORTCODE"));
			plan.setTimePlanId(row.getString("TIMEPLANID"));
			return plan;
		}
		return null;
	}

	@Override
	public int delete(String planid) {
		String sql = "delete TB_TIMEPLAN_OTHERREPORTTIME where TIMEPLANID = ?";
		List<Object> arr = new ArrayList<Object>();
		arr.add(planid);
		DataQuery dataQuery = null;
		int count = 0;
		try {
			dataQuery = AcmrInputDPFactor.getDataQuery();
			dataQuery.beginTranse();
			dataQuery.executeSql(sql, arr.toArray());
			dataQuery.commit();
		} catch (SQLException e) {
			if (null != dataQuery) {
				dataQuery.rollback();
			}
			e.printStackTrace();
		} finally {
			if (null != dataQuery) {
				dataQuery.releaseConnl();
			}
		}
		return count;
	}

	@Override
	public int countOtherTimeByReport(String code) {
		String sql = "select count(*) c from TB_TIMEPLAN_OTHERREPORTTIME where REPORTCODE = ?";
		List<Object> parm = new ArrayList<Object>();
		parm.add(code);
		DataQuickQuery quickQuery = AcmrInputDPFactor.getQuickQuery();
		DataTable dataTable = quickQuery.getDataTableSql(sql, parm.toArray());
		return dataTable.getRows().get(0).getint("c");
	}

}
