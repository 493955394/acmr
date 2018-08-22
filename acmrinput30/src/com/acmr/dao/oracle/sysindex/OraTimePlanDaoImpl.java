package com.acmr.dao.oracle.sysindex;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import acmr.data.DataQuery;
import acmr.data.DataQuickQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.sysindex.ITimePlanDao;
import com.acmr.model.sysindex.TimePlan;

public class OraTimePlanDaoImpl implements ITimePlanDao {

	@Override
	public String insert(TimePlan timePlan) {
		String sql = "insert into tb_timeplan_index values(sq_timeplan_index.nextval,?,?,sysdate)";
		List<Object> arr = new ArrayList<Object>();
		arr.add(timePlan.getReportType());
		arr.add(timePlan.getPlanType());
		DataQuery dataQuery = null;
		String seq = null;
		try {
			dataQuery = AcmrInputDPFactor.getDataQuery();
			dataQuery.beginTranse();
			seq = dataQuery.insertRtSeq(sql, arr.toArray(), "id");
			dataQuery.commit();
		} catch (Exception e) {
			if (null != dataQuery) {
				dataQuery.rollback();
			}
			e.printStackTrace();
		} finally {
			if (null != dataQuery) {
				dataQuery.releaseConnl();
			}
		}
		return seq;
	}

	@Override
	public TimePlan queryPlanById(String id) {
		String sql = "select * from tb_timeplan_index where id =?";
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
	private TimePlan dataToTimePlan(DataTable dataTable) {
		if (dataTable == null || dataTable.getRows().size() == 0) { // 如果没有查询到数据返回
			return null;
		}
		List<DataTableRow> dataTableRows = dataTable.getRows();
		for (DataTableRow row : dataTableRows) {
			TimePlan plan = new TimePlan();
			plan.setId(row.getString("id"));
			plan.setReportType(row.getString("REPORTTYPE"));
			plan.setPlanType(row.getString("PLANTYPE"));
			plan.setCreateTime(row.getDate("CREATETIME"));
			return plan;
		}
		return null;
	}

	@Override
	public int delete(String planid) {
		String sql = "delete tb_timeplan_index where id = ?";
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
