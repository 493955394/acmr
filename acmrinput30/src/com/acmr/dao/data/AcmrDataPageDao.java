package com.acmr.dao.data;

import java.util.List;

import acmr.util.DataTable;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.model.pub.PageBean;

public class AcmrDataPageDao<T> {

	public AcmrDataPageDao() {
		page = new PageBean<T>();
	}

	private PageBean<T> page;

	public PageBean<T> getPagebean() {
		return page;
	}

	@SuppressWarnings("unchecked")
	public PageBean<T> findPage(List<T> listdata) {
		if (listdata == null) {
			return page;
		}
		int count = listdata.size();
		if (count == 0) {
			return page;
		}
		page.setTotalRecorder(count);
		List<T> data = null;
		if (page.getPageNum() <1) {
			page.setPageNum(1);
		}
		if (page.getPageNum() > page.getTotalPage()) {
			page.setPageNum(page.getTotalPage());
		}
		int b1 = (page.getPageNum()-1) * page.getPageSize();
		int e1 = b1 + page.getPageSize();
		if (e1 >= listdata.size()) {
			e1 = listdata.size() - 1;
		}
		try {
			data = listdata.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		data.addAll(listdata.subList(b1, e1 + 1));
		page.setData(data);
		return page;
	}

	/**
	 * 分页查询
	 */
	public PageBean<T> findPage(String sql, String strorder, Object[] objs) {
		int count = getSqlCount(sql, objs);
		if (count == 0) {
			return page;
		}
		page.setTotalRecorder(count);
		DataTable dataTable = null;
		if (page.getPageNum() <1) {
			page.setPageNum(1);
		}
		if (page.getPageNum() > page.getTotalPage()) {
			page.setPageNum(page.getTotalPage());
		}
		dataTable = AcmrInputDPFactor.getQuickQuery().getDataPage(sql, strorder, objs, page.getPageNum()-1, page.getPageSize());
		if (dataTable == null) {
			return page;
		}
		page.setDataTable(dataTable);
		return page;
	}

	public static int getSqlCount(String sql, Object[] objs) {
		int result = 0;
		//StringBuffer sbf = new StringBuffer("select count(*) from (").append(sql).append(") d2");
		StringBuffer sbf = new StringBuffer("select count(1) from (").append(sql).append(") d2");
		String restr = AcmrInputDPFactor.getQuickQuery().getDataScarSql(sbf.toString(), objs);
		result = Integer.parseInt(restr);
		return result;
	}
}
