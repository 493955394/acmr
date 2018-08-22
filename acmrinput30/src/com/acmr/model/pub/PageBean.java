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
public class PageBean<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private int totalRecorder;// 总记录数
	private int pageSize = 10;// 每页的记录数
	private int pageNum = 1;// 当前第几页
	private List<T> data; // 数据
	private DataTable dataTable; // 查询出的数据对象
	private String url; // 翻页地址
	private int posePageCount = 3; // 分页显示前几页,默认显示前3页
	private static int[] availablepagesizes = new int[] { 10, 20, 50, 100 };

	public PageBean() {
		// 执行查询
		if (CurrentContext.getRequest() != null) {
			try {
				BeanUtils.populate(this, CurrentContext.getRequest().getParameterMap());
				boolean isavailablepagesize = false;
				for (int availablepagesize : availablepagesizes) {
					if (pageSize == availablepagesize) {
						isavailablepagesize = true;
						break;
					}
				}
				if (!isavailablepagesize) {
					pageSize = 10;
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	public int getPosePageCount() {
		return posePageCount;
	}

	public void setPosePageCount(int posePageCount) {
		this.posePageCount = posePageCount;
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
		return (this.totalRecorder - 1) / this.pageSize + 1;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data2) {
		this.data = data2;
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
		int indexpagesize = url.indexOf("pageSize");
		if (indexpagesize > 0) {
			int indexend = indexpagesize + "pageSize".length() + 1;
			while (true) {
				char c = url.charAt(indexend);
				if (c < '0' || c > '9') {
					break;
				}
				indexend++;
				if (indexend == url.length()) {
					break;
				}
			}
			url = url.substring(0, indexpagesize) + (indexend < url.length() ? url.substring(indexend) : "");
		}
		this.url = url;
	}

	@Override
	public String toString() {
		StringBuffer paginationHTML = new StringBuffer();
		int totalpage = getTotalPage();
		int i = 0;
		if (pageNum == 1) {
			paginationHTML
					.append("<li class=\"disabled\"><span>首页</span></li>");
			paginationHTML
					.append("<li class=\"disabled\"><span>上一页</span></li>");
		} else {
			paginationHTML.append("<li><a href='" + url
					+ "&pageNum=1&pageSize=" + pageSize + "'>首页</a></li>");
			if (pageNum >= 2) {
				paginationHTML.append("<li><a href='" + url + "&pageNum="
						+ (pageNum - 1) + "&pageSize=" + pageSize
						+ "'>上一页</a></li>");
			} else {
				paginationHTML.append("<li><a href='" + url
						+ "&pageNum=1&pageSize=" + pageSize + "'>上一页</a></li>");
			}
		}
		if (pageNum > posePageCount) {
			for (i = pageNum - posePageCount; i < pageNum; i++) {
				paginationHTML.append("<li><a href='" + url + "&pageNum=" + i
						+ "&pageSize=" + pageSize + "'>" + i + "</a></li>");
			}
		} else {
			for (i = 1; i < pageNum; i++) {
				paginationHTML.append("<li><a href='" + url + "&pageNum=" + i
						+ "&pageSize=" + pageSize + "'>" + i + "</a></li>");
			}
		}
		paginationHTML.append("<li class=\"active\"><span>" + pageNum
				+ "</span></li>");

		if (totalpage - pageNum > posePageCount) {
			for (i = pageNum + 1; i <= pageNum + posePageCount; i++) {
				paginationHTML.append("<li><a href='" + url + "&pageNum=" + i
						+ "&pageSize=" + pageSize + "'>" + i + "</a></li>");
			}
			;
		} else {
			for (i = pageNum + 1; i <= totalpage; i++) {
				paginationHTML.append("<li><a href='" + url + "&pageNum=" + i
						+ "&pageSize=" + pageSize + "'>" + i + "</a></li>");
			}
		}

		if (pageNum == totalpage) {
			paginationHTML
					.append("<li class=\"disabled\"><span>下一页</span></li>");
			paginationHTML.append("<li class=\"disabled\"><span>末页,共"
					+ totalpage + "页</span></li>");
		} else if (0 == totalpage) {
			paginationHTML
					.append("<li class=\"disabled\"><span>下一页</span></li>");
			paginationHTML.append("<li class=\"disabled\"><span>末页,共"
					+ totalpage + "页</span></li>");
		} else {
			if (pageNum + 2 <= totalpage) {
				paginationHTML.append("<li><a href='" + url + "&pageNum="
						+ (pageNum + 1) + "&pageSize=" + pageSize
						+ "'>下一页</a></li>");
			} else {
				paginationHTML.append("<li><a href='" + url + "&pageNum="
						+ totalpage + "&pageSize=" + pageSize
						+ "'>下一页</a></li>");
			}
			paginationHTML.append("<li><a href='" + url + "&pageNum="
					+ totalpage + "&pageSize=" + pageSize + "'>末页,共"
					+ totalpage + "页</a></li>");
		}
		if (totalRecorder > 10) {
			paginationHTML.append("<li><span>共"+totalRecorder+"条,每页条数:</span></li>");
			paginationHTML.append("<li");
			if (pageSize == 10) {
				paginationHTML.append(" class=\"active\"");
			}
			paginationHTML.append("><a href='" + url
					+ "&pageNum=1&pageSize=10'>10</a></li>");
			paginationHTML.append("<li");
			if (pageSize == 20) {
				paginationHTML.append(" class=\"active\"");
			}
			paginationHTML.append("><a href='" + url
					+ "&pageNum=1&pageSize=20'>20</a></li>");
			if (totalRecorder > 20) {
				paginationHTML.append("<li");
				if (pageSize == 50) {
					paginationHTML.append(" class=\"active\"");
				}
				paginationHTML.append("><a href='" + url
						+ "&pageNum=1&pageSize=50'>50</a></li>");
				if (totalRecorder > 50) {
					paginationHTML.append("<li");
					if (pageSize == 100) {
						paginationHTML.append(" class=\"active\"");
					}
					paginationHTML.append("><a href='" + url
							+ "&pageNum=1&pageSize=100'>100</a></li>");
				}
			}
		}
		return paginationHTML.toString();
	}
}
