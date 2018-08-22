package com.acmr.dao.sqlserver.sysindex;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import acmr.data.DataQuery;
import acmr.data.DataQuickQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.sysindex.IUniteTimeDao;
import com.acmr.model.sysindex.UniteTime;

public class SqlUniteTimeDaoImpl implements IUniteTimeDao {

	@Override
	public int insert(UniteTime unitetime) {
		String sql = "insert into TB_TIMEPLAN_UNITETIME values(?,?,?,?,?,?,?,?,?)";
		List<Object> arr = new ArrayList<Object>();
		arr.add(unitetime.getTimeplanId());
		arr.add(unitetime.getMonth());
		arr.add(unitetime.getDay());
		arr.add(unitetime.getStartReportPeriod());
		arr.add(unitetime.getEndReportPeriod());
		arr.add(unitetime.getInputTimeSet());
		arr.add(unitetime.getInputTimeType());
		arr.add(unitetime.getExamineOrPushTime());
		arr.add(unitetime.getExamineOrPushTimeType());
		
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
	public UniteTime getUniteById(String id) {
		String sql = "select * from TB_TIMEPLAN_UNITETIME where TIMEPLANID = ?";
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
	private UniteTime dataToTimePlan(DataTable dataTable) {
		if (dataTable == null || dataTable.getRows().size() == 0) { // 如果没有查询到数据返回
			return null;
		}
		List<DataTableRow> dataTableRows = dataTable.getRows();
		for (DataTableRow row : dataTableRows) {
			UniteTime plan = new UniteTime();
			plan.setId(row.getString("ID"));
			plan.setTimeplanId(row.getString("TIMEPLANID"));
			plan.setMonth(row.getint("MONTH"));
			plan.setDay(row.getint("DAY"));
			plan.setStartReportPeriod(row.getString("STARTREPORTPERIOD"));
			plan.setEndReportPeriod(row.getString("ENDREPORTPERIOD"));
			plan.setInputTimeSet(row.getint("INPUTTIMESET"));
			plan.setInputTimeType(row.getString("INPUTTIMETYPE"));
			plan.setExamineOrPushTime(row.getint("EXAMINEORPUSHTIME"));
			plan.setExamineOrPushTimeType(row.getString("EXAMINEORPUSHTIMETYPE"));
			return plan;
		}
		return null;
	}

	@Override
	public int delete(String planid){
		String sql = "delete TB_TIMEPLAN_UNITETIME where TIMEPLANID = ?";
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
