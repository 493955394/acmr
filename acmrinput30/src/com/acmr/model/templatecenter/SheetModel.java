package com.acmr.model.templatecenter;

public class SheetModel {
	private String name;
	private int index;

	public SheetModel(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public SheetModel() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
