package com.acmr.model.calculate;

import com.acmr.model.range.Pinfo;

/**
 * 计算对象
 * 
 * @author chenyf
 *
 */
public class CalBean {
	private String name; // 名称
	private String oldName;//旧名称 编辑用
	private Pinfo posi; // 位置
	private String content; // 内容
	
	public String getOldName() {
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Pinfo getPosi() {
		return posi;
	}
	public void setPosi(Pinfo posi) {
		this.posi = posi;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
