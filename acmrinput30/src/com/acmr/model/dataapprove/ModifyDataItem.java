/**
 * ModifyDataItem.java
 *北京华通人商用信息有限公司版权所有
 */
package com.acmr.model.dataapprove;

/**
 * @author zhangqiang
 *
 */
public final class ModifyDataItem {
	private String cellpos;// 数据位置
	private String value;// 新的数据值
	private String datahander;// 数据处理方式
	private String annotation;// 注释
	private String ifforecast;// 是否预测值

	public ModifyDataItem(String cellpos, String value, String datahander,
			String annotation, String ifforecast) {
		this.cellpos = cellpos;
		this.value = value;
		this.datahander = datahander;
		this.annotation = annotation;
		this.ifforecast = ifforecast;
	}

	public String getCellpos() {
		return cellpos;
	}

	public String getValue() {
		return value;
	}

	public String getDatahander() {
		return datahander;
	}

	public String getAnnotation() {
		return annotation;
	}

	public String getIfforecast() {
		return ifforecast;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((annotation == null) ? 0 : annotation.hashCode());
		result = prime * result + ((cellpos == null) ? 0 : cellpos.hashCode());
		result = prime * result
				+ ((datahander == null) ? 0 : datahander.hashCode());
		result = prime * result
				+ ((ifforecast == null) ? 0 : ifforecast.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		ModifyDataItem other = (ModifyDataItem) obj;
		if (annotation == null) {
			if (other.annotation != null)
				return false;
		} else if (!annotation.equals(other.annotation))
			return false;
		if (cellpos == null) {
			if (other.cellpos != null)
				return false;
		} else if (!cellpos.equals(other.cellpos))
			return false;
		if (datahander == null) {
			if (other.datahander != null)
				return false;
		} else if (!datahander.equals(other.datahander))
			return false;
		if (ifforecast == null) {
			if (other.ifforecast != null)
				return false;
		} else if (!ifforecast.equals(other.ifforecast))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ModifyDataItem [cellpos=" + cellpos + ", value=" + value
				+ ", datahander=" + datahander + ", annotation=" + annotation
				+ ", ifforecast=" + ifforecast + "]";
	}

}
