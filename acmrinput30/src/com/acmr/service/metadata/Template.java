package com.acmr.service.metadata;

/**
 * 元数据-模板详情实体对象
 * 
 * @author chenyf
 *
 */
public class Template {
	private String code;
	private String name;
	private String type;
	private String state;

	public Template(String code, String name, String type, String state) {
		this.code = code;
		this.name = name;
		this.type = type;
		this.state = state;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		if ("D".equals(type)) {
			return "日";
		} else if ("W".equals(type)) {
			return "周";
		} else if ("X".equals(type)) {
			return "旬";
		} else if ("M".equals(type)) {
			return "月";
		} else if ("Q".equals(type)) {
			return "季";
		} else if ("Y".equals(type)) {
			return "年";
		}
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		if ("0".equals(state)) {
			return "停用";
		} else if ("1".equals(state)) {
			return "启用";
		} else if ("-1".equals(state)) {
			return "删除";
		}
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Template [code=" + code + ", name=" + name + ", type=" + type + ", state=" + state + "]";
	}

	public Template() {
		// TODO Auto-generated constructor stub
	}
}
