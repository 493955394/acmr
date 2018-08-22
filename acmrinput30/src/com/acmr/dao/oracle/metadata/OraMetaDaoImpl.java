package com.acmr.dao.oracle.metadata;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import acmr.util.DataTable;
import acmr.util.DataTableRow;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.metadata.IMetaDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.AjaxPageBean;
import com.acmr.service.metadata.DataDetails;
import com.acmr.service.metadata.MetaServiceManager;
import com.acmr.service.metadata.Template;

public class OraMetaDaoImpl implements IMetaDao {

	@Override
	public void getAllData(MetaServiceManager metaService, AjaxPageBean<DataDetails> page, String code) throws Exception {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		sb2.append("select vw_data.*,tb_class.cname as flname,tb_reg.cname as regname,tb_ds.cname as dsname from (");
		sb2.append("select CLASSCODE,REGCODE,DATASOURCE,max(updatetime) as updatetime,count(*) as count,max(AYEARMON) as maxnum,min(AYEARMON) as minnum from TB_BASIC_DATA where code =?  ");
		sb2.append("GROUP BY CLASSCODE,REGCODE,DATASOURCE");
		sb2.append(") vw_data");
		sb2.append(" left join TB_BASIC_CLASS tb_class on tb_class.code = vw_data.classcode");
		sb2.append(" left join TB_BASIC_REGION tb_reg on vw_data.regcode = tb_reg.code");
		sb2.append(" left join TB_BASIC_DATASOURCE tb_ds on vw_data.datasource = tb_ds.code");
		sb.append("SELECT * FROM ");
		sb.append("(");
		sb.append("SELECT A.*, ROWNUM RN ");
		sb.append("FROM (");
		sb.append(sb2.toString());
		sb.append(") A ");
		sb.append("WHERE ROWNUM <= ?");
		sb.append(") ");
		sb.append("WHERE RN >= ?");

		String queryCountSql = "select count(*) from (" + sb2.toString() + ")";
		Object[] parm1 = { code, page.getPageSize(), 1 };
		Object[] parm2 = { code };

		DataTable dataCount = metaService.queryDataSql(queryCountSql, parm2);

		int dataCounts = dataCount.getRows().get(0).getint(0);
		ArrayList<DataDetails> list = new ArrayList<DataDetails>();
		if (dataCounts != 0) {
			DataTable queryDataSql = metaService.queryDataSql(sb.toString(), parm1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < queryDataSql.getRows().size(); i++) {
				DataTableRow row = queryDataSql.getRows().get(i);
				DataDetails dd = new DataDetails();
				dd.setFl(row.getString("classcode"));
				dd.setFlName(row.getString("flname"));
				dd.setReg(row.getString("regcode"));
				dd.setRegName(row.getString("regname"));
				dd.setDatasource(row.getString("datasource"));
				dd.setDatasourceName(row.getString("dsname"));
				dd.setCount(row.getString("count"));
				dd.setEndTime(row.getString("maxnum"));
				dd.setBeginTime(row.getString("minnum"));
				java.util.Date date = row.getDate("updatetime");
				String updatetime = "";
				if (null != date) {
					updatetime = sdf.format(date);
				}
				dd.setUpdateTime(updatetime);
				list.add(dd);
			}
		}
		page.setData(list);
		page.setTotalRecorder(dataCounts);
	}

	@Override
	public void toDatadetails(MetaServiceManager metaService, AjaxPageBean<DataDetails> page, String code, String flcode, String regcode, String datasource, StringBuffer url) throws Exception {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		sb2.append("select vw_data.*,tb_class.cname as flname,tb_reg.cname as regname,tb_ds.cname as dsname from (");
		sb2.append("select CLASSCODE,REGCODE,DATASOURCE,max(updatetime) as updatetime,count(*) as count,max(AYEARMON) as maxnum,min(AYEARMON) as minnum from TB_BASIC_DATA where code =? ");
		List<Object> parm1 = new ArrayList<Object>();
		parm1.add(code);
		if (!StringUtil.isEmpty(flcode)) {
			sb2.append("and '&' || CLASSCODE || '&' like ?");
			url.append("&flcode=" + flcode);
			parm1.add("%&" + flcode + "&%");
		}
		if (!StringUtil.isEmpty(regcode)) {
			sb2.append("and regcode = ?");
			url.append("&regcode=" + regcode);
			parm1.add(regcode);
		}
		if (!StringUtil.isEmpty(datasource)) {
			sb2.append("and datasource = ?");
			url.append("&datasource=" + datasource);
			parm1.add(datasource);
		}
		sb2.append("GROUP BY CLASSCODE,REGCODE,DATASOURCE");
		sb2.append(") vw_data");
		sb2.append(" left join TB_BASIC_CLASS tb_class on tb_class.code = vw_data.classcode");
		sb2.append(" left join TB_BASIC_REGION tb_reg on vw_data.regcode = tb_reg.code");
		sb2.append(" left join TB_BASIC_DATASOURCE tb_ds on vw_data.datasource = tb_ds.code");
		sb.append("SELECT * FROM ");
		sb.append("(");
		sb.append("SELECT A.*, ROWNUM RN ");
		sb.append("FROM (");
		sb.append(sb2.toString());
		sb.append(") A ");
		sb.append("WHERE ROWNUM <= ?");
		sb.append(") ");
		sb.append("WHERE RN >= ?");

		String queryCountSql = "select count(*) from (" + sb2.toString() + ")";
		DataTable dataCount = metaService.queryDataSql(queryCountSql, parm1.toArray());
		int dataCounts = dataCount.getRows().get(0).getint(0);
		ArrayList<DataDetails> list = new ArrayList<DataDetails>();
		if (dataCounts != 0) {
			parm1.add(page.getPageNum() * page.getPageSize());
			parm1.add((page.getPageNum() - 1) * page.getPageSize() + 1);
			DataTable queryDataSql = metaService.queryDataSql(sb.toString(), parm1.toArray());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < queryDataSql.getRows().size(); i++) {
				DataTableRow row = queryDataSql.getRows().get(i);
				DataDetails dd = new DataDetails();
				dd.setFl(row.getString("classcode"));
				dd.setFlName(row.getString("flname"));
				dd.setReg(row.getString("regcode"));
				dd.setRegName(row.getString("regname"));
				dd.setDatasource(row.getString("datasource"));
				dd.setDatasourceName(row.getString("dsname"));
				dd.setCount(row.getString("count"));
				dd.setEndTime(row.getString("maxnum"));
				dd.setBeginTime(row.getString("minnum"));
				java.util.Date date = row.getDate("updatetime");
				String updatetime = "";
				if (null != date) {
					updatetime = sdf.format(date);
				}
				dd.setUpdateTime(updatetime);
				list.add(dd);
			}
		}

		page.setData(list);
		page.setTotalRecorder(dataCounts);
		page.setUrl(url.toString());
	}

	/***
	 * 根据code查询模板中是否存在
	 * 
	 * @param code
	 * @author chenyf
	 */
	public boolean queryTemplate(String code) {
		String sql = "select count(*) from tb_template_content t where dbms_lob.instr(t.metacodeindex,?,1,1)>0";
		List<Object> params = new ArrayList<Object>();
		params.add(code + ",");
		DataTable dt = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, params.toArray());
		List<DataTableRow> rows = dt.getRows();
		int getint = rows.get(0).getint(0);
		if (getint > 0) {
			return true;
		}
		return false;
	}

	/***
	 * 根据code查询任务表中是否存在
	 * 
	 * @param code
	 * @author chenyf
	 */
	public boolean queryTask(String code) {
		String sql = "select count(*) from tb_task_excel t where dbms_lob.instr(t.metacodeindex,?,1,1)>0";
		List<Object> params = new ArrayList<Object>();
		params.add(code + ",");
		DataTable dt = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, params.toArray());
		List<DataTableRow> rows = dt.getRows();
		int getint = rows.get(0).getint(0);
		if (getint > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 查询临时表中是否存在
	 * 
	 * @author chenyf
	 */
	public boolean queryTmpTable(String code, String field) {
		String sql = "select count(*) from tb_input_tmp_data where " + field + " =?";
		List<Object> params = new ArrayList<Object>();
		params.add(code);
		DataTable dt = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, params.toArray());
		List<DataTableRow> rows = dt.getRows();
		int getint = rows.get(0).getint(0);
		if (getint > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 查询模板
	 * 
	 * @author chenyf
	 */
	@Override
	public List<Template> getTemplateData(String code, String type, AjaxPageBean<Template> page, String pre) {
		StringBuffer sb = new StringBuffer();
		if (StringUtil.isEmpty(code)) {
			return new ArrayList<Template>();
		}
		sb.append("SELECT * FROM ");
		sb.append("(");
		sb.append("SELECT A.*, ROWNUM RN ");
		sb.append("FROM (");
		sb.append("select i.templatetype,i.code,i.cname,i.state from tb_template_index i join tb_template_content c on ");
		sb.append("i.id = c.templatecode where dbms_lob.instr(c.metacodeindex,?,1,1)>0");
		if (!StringUtil.isEmpty(type)) {
			sb.append(" and templatetype =?");
		}
		sb.append(") A ");
		sb.append("WHERE ROWNUM <= ?");
		sb.append(") ");
		sb.append("WHERE RN >= ?");
		List<Object> params = new ArrayList<Object>();
		params.add(pre + code + ",");
		if (!StringUtil.isEmpty(type)) {
			params.add(type);
		}
		params.add(page.getPageNum() * page.getPageSize());
		params.add((page.getPageNum() - 1) * page.getPageSize() + 1);
		String sql = sb.toString();
		DataTable dataTable = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, params.toArray());
		return dataToTemplate(dataTable);
	}

	/**
	 * 数据转对象
	 * 
	 * @param dataTable
	 * @return
	 * @author chenyf
	 */
	private List<Template> dataToTemplate(DataTable dataTable) {
		List<Template> result = new ArrayList<Template>();
		if (dataTable == null || dataTable.getRows().isEmpty()) {
			return result;
		}

		List<DataTableRow> dataTableRows = dataTable.getRows();
		for (DataTableRow row : dataTableRows) {
			Template template = new Template();
			template.setCode(row.getString("code"));
			template.setName(row.getString("cname"));
			template.setType(row.getString("templatetype"));
			template.setState(row.getString("state"));
			result.add(template);
		}

		return result;
	}

	/**
	 * 查询模板有多少条记录
	 * 
	 * @author chenyf
	 */
	@Override
	public int queryTemplate(String code, String type, String pre) {
		StringBuffer sb = new StringBuffer();
		if (StringUtil.isEmpty(code)) {
			return 0;
		}
		sb.append("select count(*) from tb_template_index i join tb_template_content c on ");
		sb.append("i.id = c.templatecode where dbms_lob.instr(c.metacodeindex,?,1,1)>0");
		List<Object> params = new ArrayList<Object>();
		params.add(pre + code + ",");
		if (!StringUtil.isEmpty(type)) {
			sb.append(" and templatetype =?");
			params.add(type);
		}
		String sql = sb.toString();
		DataTable dataTable = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, params.toArray());
		return dataTable.getRows().get(0).getint(0);
	}

}
