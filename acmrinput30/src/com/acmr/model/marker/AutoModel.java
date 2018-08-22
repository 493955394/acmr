package com.acmr.model.marker;

import acmr.util.IKeyible;

/**
 * 自动识别区对象
 * 
 * @author chenyf
 *
 */
public class AutoModel {
	private String posi;
	private String content;

	public AutoModel() {
		posi = "";
		content = "";
	}

	public AutoModel(String posi, String content) {
		this.posi = posi;
		this.content = content;
	}

	public String getPosi() {
		return posi;
	}

	public void setPosi(String posi) {
		this.posi = posi;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


}
