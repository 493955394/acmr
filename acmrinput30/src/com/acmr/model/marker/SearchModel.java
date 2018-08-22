package com.acmr.model.marker;

/**
 * 自动匹配对象
 * 
 * @author chenyf
 *
 */
public class SearchModel {
	private String item; // 维度
	private String range; // code
	private String posi; // 层
	private String main; // 主栏
	private String guest; // 宾栏

	public SearchModel() {
		// TODO Auto-generated constructor stub
	}

	public SearchModel(String item, String range, String posi, String main, String guest) {
		this.item = item;
		this.range = range;
		this.posi = posi;
		this.main = main;
		this.guest = guest;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getPosi() {
		return posi;
	}

	public void setPosi(String posi) {
		this.posi = posi;
	}

	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public String getGuest() {
		return guest;
	}

	public void setGuest(String guest) {
		this.guest = guest;
	}

}
