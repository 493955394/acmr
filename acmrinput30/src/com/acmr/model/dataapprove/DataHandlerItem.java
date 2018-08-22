/**
 * DataHanlerItem.java
 *北京华通人商用信息有限公司版权所有
 */
package com.acmr.model.dataapprove;

/**
 * @author zhangqiang
 *
 */
public final class DataHandlerItem {
	private String cellpos;
	private String datahanler;

	public DataHandlerItem(String cellpos, String datahanler) {
		this.cellpos = cellpos;
		this.datahanler = datahanler;
	}

	public String getCellpos() {
		return cellpos;
	}

	public String getDatahanler() {
		return datahanler;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cellpos == null) ? 0 : cellpos.hashCode());
		result = prime * result
				+ ((datahanler == null) ? 0 : datahanler.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataHandlerItem other = (DataHandlerItem) obj;
		if (cellpos == null) {
			if (other.cellpos != null)
				return false;
		} else if (!cellpos.equals(other.cellpos))
			return false;
		if (datahanler == null) {
			if (other.datahanler != null)
				return false;
		} else if (!datahanler.equals(other.datahanler))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DataHanlerItem [cellpos=" + cellpos + ", datahanler="
				+ datahanler + "]";
	}
	

}
