package com.acmr.model.templatecenter;

import java.io.Serializable;
import java.util.Date;

public class TemplateIndex implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;// 模板编码
	private String code;// 模板编码
	private String cname;// 模板名称
	private Date createTime;// 创建时间
	private String createUserId;// 创建人
	private String memo;// 说明
	private String state;// 状态
	private String procode;// 父模板id
	private String templateType;// 类型
	private String isMod;// 是否模板,0模板分类,1模板
	private String templateSort;//模板种类：1，excel模板2，拖拽模板3，标记定义模板

	public String getTemplateSort() {
		return templateSort;
	}

	public void setTemplateSort(String templateSort) {
		this.templateSort = templateSort;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getProcode() {
		return procode;
	}

	public void setProcode(String procode) {
		this.procode = procode;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getIsMod() {
		return isMod;
	}

	public void setIsMod(String isMod) {
		this.isMod = isMod;
	}

}

