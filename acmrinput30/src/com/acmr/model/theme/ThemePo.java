package com.acmr.model.theme;

import java.util.Date;
import java.util.List;

public class ThemePo {
	private String code;
	private String cname;
	private String procode;
	private String href;
	private String target;
	private Date updatetime;
	private String ifclose;
	private String photo;
	private String exp;
	private String dbname;
	private String def;
	private String sort;
	private String visible;
	private List<ThemePo> childs;
	private String first;
	private String last;
	private String sortcode;

	public ThemePo() {
		// TODO Auto-generated constructor stub
	}

	public List<ThemePo> getChilds() {
		return childs;
	}

	public void setChilds(List<ThemePo> childs) {
		this.childs = childs;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getDef() {
		return def;
	}

	public void setDef(String def) {
		this.def = def;
	}

	public String getIfclose() {
		return ifclose;
	}

	public void setIfclose(String ifclose) {
		this.ifclose = ifclose;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
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

	public String getProcode() {
		return procode;
	}

	public void setProcode(String procode) {
		this.procode = procode;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getSortcode() {
		return sortcode;
	}

	public void setSortcode(String sortcode) {
		this.sortcode = sortcode;
	}

}
