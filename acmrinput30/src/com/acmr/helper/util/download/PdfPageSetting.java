package com.acmr.helper.util.download;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;


public class PdfPageSetting {
	private float marginLeft = 36;
	private float marginRight = 36;
	private float marginTop = 36;
	private float marginBottom = 36;
	private Rectangle pageSize = PageSize.A4;
	public float getMarginLeft() {
		return marginLeft;
	}
	public void setMarginLeft(float marginLeft) {
		this.marginLeft = marginLeft;
	}
	public float getMarginRight() {
		return marginRight;
	}
	public void setMarginRight(float marginRight) {
		this.marginRight = marginRight;
	}
	public float getMarginTop() {
		return marginTop;
	}
	public void setMarginTop(float marginTop) {
		this.marginTop = marginTop;
	}
	public float getMarginBottom() {
		return marginBottom;
	}
	public void setMarginBottom(float marginBottom) {
		this.marginBottom = marginBottom;
	}
	public Rectangle getPageSize() {
		return pageSize;
	}
	public void setPageSize(Rectangle pageSize) {
		this.pageSize = pageSize;
	}
	
}
