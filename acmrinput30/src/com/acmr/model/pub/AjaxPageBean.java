package com.acmr.model.pub;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import acmr.util.DataTable;
import acmr.web.core.CurrentContext;

/**
 * 分页
 * 
 * @author xufeng
 * 
 * @param <T>
 */
public class AjaxPageBean<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private int totalRecorder;// 总记录数
	private int pageSize = 10;// 每页的记录数
	private int pageNum = 1;// 当前第几页
	private List<T> data; // 数据
	private DataTable dataTable; // 查询出的数据对象
	private String url; // 翻页地址

	public AjaxPageBean() {
		// 执行查询
		if (CurrentContext.getRequest() != null) {
			try {
				BeanUtils.populate(this, CurrentContext.getRequest().getParameterMap());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	public AjaxPageBean(int pageNum) {
		this.pageNum = pageNum;
	}

	public AjaxPageBean(int pageNum, int pageSize) {
		this.pageSize = pageSize;
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getTotalPage() {
		return (int) Math.ceil((double) this.totalRecorder / this.pageSize);
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getTotalRecorder() {
		return totalRecorder;
	}

	public void setTotalRecorder(int totalRecorder) {
		this.totalRecorder = totalRecorder;
	}

	public DataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
