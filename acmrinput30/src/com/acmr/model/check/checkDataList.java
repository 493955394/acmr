package com.acmr.model.check;


import acmr.cubeinput.service.cubeinput.entity.CubeQueryData;
import acmr.util.IKeyible;

public class checkDataList implements IKeyible {
	private String celladdr;
	private String code;
	private CubeQueryData data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public CubeQueryData getData() {
		return data;
	}

	public void setData(CubeQueryData data) {
		this.data = data;
	}

	public String getCelladdr() {
		return celladdr;
	}

	public void setCelladdr(String celladdr) {
		this.celladdr = celladdr;
	}

	@Override
	public String Key() {
		return code;
	}

}
