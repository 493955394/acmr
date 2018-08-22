package com.acmr.dao.oracle.sysindex;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import acmr.data.DataQuery;
import acmr.data.DataQuickQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.sysindex.ICustomTimeDao;
import com.acmr.model.sysindex.CustomTime;

public class OraCustomTimeDaoImpl implements ICustomTimeDao {

	@Override
	public int insert(CustomTime customTime) {
		String sql = "insert into TB_TIMEPLAN_CUSTOMTIME values(SQ_TIMEPLAN_CUSTOMTIME.nextval,?,?,?,?,?,?,?,?)";
		List<Object> arr = new ArrayList<Object>();
		arr.add(customTime.getTimeplanId());
		arr.add(customTime.getReportPeriod());
		arr.add(new java.sql.Date(customTime.getTaskStartTime().getTime()));
		arr.add(customTime.getInputTimeSet());
		arr.add(customTime.getInputTimeType());
		arr.add(customTime.getExamineOrPushTime());
		arr.add(customTime.getExamineOrPushTimeType());
		arr.add(customTime.getIsNoReport());
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
	public List<CustomTime> getTimeById(String id) {
		String sql = "select * from TB_TIMEPLAN_CUSTOMTIME where TIMEPLANID = ?";
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
	private List<CustomTime> dataToTimePlan(DataTable dataTable) {
		ArrayList<CustomTime> list = new ArrayList<CustomTime>();
		if (dataTable == null || dataTable.getRows().size() == 0) { // 如果没有查询到数据返回
			return list;
		}
		List<DataTableRow> dataTableRows = dataTable.getRows();
		for (DataTableRow row : dataTableRows) {
			CustomTime plan = new CustomTime();
			plan.setId(row.getString("ID"));
			plan.setTimeplanId(row.getString("TIMEPLANID"));
			plan.setReportPeriod(row.getString("REPORTPERIOD"));
			plan.setTaskStartTime(row.getDate("TASKSTARTTIME"));
			plan.setInputTimeSet(row.getint("INPUTTIMESET"));
			plan.setInputTimeType(row.getString("INPUTTIMETYPE"));
			plan.setExamineOrPushTime(row.getint("EXAMINEORPUSHTIME"));
			plan.setExamineOrPushTimeType(row.getString("EXAMINEORPUSHTIMETYPE"));
			plan.setIsNoReport(row.getString("ISNOREPORT"));
			list.add(plan);
		}
		return list;
	}

	@Override
	public int delete(String planid) {
		String sql = "delete TB_TIMEPLAN_CUSTOMTIME where TIMEPLANID = ?";
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

}
