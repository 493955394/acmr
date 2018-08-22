/**
 * ExcelData.java
 *北京华通人商用信息有限公司版权所有
 */
package com.acmr.model.dataapprove;

import java.util.Arrays;

/**
 * @author zhangqiang
 *
 */
public final class ExcelData {
	private byte[] data;
	private String filesuffix;
	public ExcelData(byte[] data, String filesuffix) {
		super();
		this.data = data;
		this.filesuffix = filesuffix;
	}
	public byte[] getData() {
		return data;
	}
	public String getFilesuffix() {
		return filesuffix;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		result = prime * result
				+ ((filesuffix == null) ? 0 : filesuffix.hashCode());
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
		ExcelData other = (ExcelData) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		if (filesuffix == null) {
			if (other.filesuffix != null)
				return false;
		} else if (!filesuffix.equals(other.filesuffix))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ExcelData [data=" + Arrays.toString(data) + ", filesuffix="
				+ filesuffix + "]";
	}
	

}
