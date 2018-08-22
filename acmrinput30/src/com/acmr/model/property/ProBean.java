package com.acmr.model.property;

import java.util.List;

import acmr.util.IKeyible;

public class ProBean  implements IKeyible {
	private String code;
	private List<Prop> list;
	@Override
	public String Key() {
		// TODO Auto-generated method stub
		return code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<Prop> getList() {
		return list;
	}
	public void setList(List<Prop> list) {
		this.list = list;
	}
}
