package com.acmr.dao.oracle.datareport;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import acmr.util.DataTable;
import acmr.util.DataTableRow;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.datareport.IDatareportDao;
import com.acmr.model.datareport.Datareport;

public class OraDatareportDaoImpl implements IDatareportDao {

	@Override
	public List<Datareport> find() {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ACMRNDATA_PUB_V2.TB_WEB_DATAREPORT t order by updatetime desc");
		DataTable dataTable = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql.toString());
		return dataToReport(dataTable);
	}

	@Override
	public int insert(Datareport report) {
		String sql = "insert into ACMRNDATA_PUB_V2.TB_WEB_DATAREPORT(id,name,type,href,updatetime) values(ACMRNDATA_PUB_V2.SEQ_DATAREPORT.nextval,?,?,?,?)";
		ArrayList<Object> parms = new ArrayList<Object>();
		parms.add(report.getName());
		parms.add(report.getType());
		parms.add(report.getHref());
		parms.add(new Timestamp(new Date().getTime()));
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql, parms.toArray());
	}

	@Override
	public int update(Datareport report) {
		String sql = "update ACMRNDATA_PUB_V2.TB_WEB_DATAREPORT set name=?,type=?,href=?,updatetime=? where id = ?";
		ArrayList<Object> parms = new ArrayList<Object>();
		parms.add(report.getName());
		parms.add(report.getType());
		parms.add(report.getHref());
		parms.add(new Timestamp(new Date().getTime()));
		parms.add(report.getId());
		return AcmrInputDPFactor.getQuickQuery().executeSql(sql, parms.toArray());
	}

	@Override
	public int delete(String id) {
		String sb = "delete from ACMRNDATA_PUB_V2.TB_WEB_DATAREPORT where id = ?";
		return AcmrInputDPFactor.getQuickQuery().executeSql(sb, new Object[] { id });
	}

	private List<Datareport> dataToReport(DataTable dataTable) {
		ArrayList<Datareport> list = new ArrayList<Datareport>();
		if (dataTable == null || dataTable.getRows().size() == 0) { // 如果没有查询到数据返回
			return list;
		}
		List<DataTableRow> dataTableRows = dataTable.getRows();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (DataTableRow row : dataTableRows) {
			Datareport report = new Datareport();
			report.setId(row.getString("id"));
			report.setName(row.getString("name"));
			report.setType(row.getString("type"));
			report.setHref(row.getString("href"));
			Date date = row.getDate("updatetime");
			if (date != null) {
				report.setUpdatetime(sdf.format(date));
			}
			list.add(report);
		}
		return list;
	}

	@Override
	public Datareport getById(String id) {
		String sql = "select * from ACMRNDATA_PUB_V2.TB_WEB_DATAREPORT where id = ?";
		DataTable dataTable = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, new Object[] { id });
		List<Datareport> list = dataToReport(dataTable);
		if (list.size() > 0) {
			return list.get(0);
		}
		return new Datareport();
	}
}
