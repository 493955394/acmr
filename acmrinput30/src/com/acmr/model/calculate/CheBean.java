package com.acmr.model.calculate;

/**
 * 校验对象
 * 
 * @author chenyf
 *
 */
public class CheBean {
	private String name; // 名称
	private String oldName;//旧名称 编辑用
	private String type; // 错误类型
	private String info; // 提示
	private String content; // 校验方法

	public String getOldName() {
		return oldName;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
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
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
