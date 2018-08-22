package com.acmr.service.metadata;

/**
 * 同义词po对象
 * @author chenyf
 *
 */
public class Synonyms {
	public String id;
	public String wdcode;
	public String sort;
	public String cname;
	public String code;
	public String dbcode;

	public Synonyms() {
		// TODO Auto-generated constructor stub
	}

	public Synonyms(String wdcode, String sort, String cname, String code, String dbcode) {
		this.wdcode = wdcode;
		this.sort = sort;
		this.cname = cname;
		this.code = code;
		this.dbcode = dbcode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWdcode() {
		return wdcode;
	}

	public void setWdcode(String wdcode) {
		this.wdcode = wdcode;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDbcode() {
		return dbcode;
	}

	public void setDbcode(String dbcode) {
		this.dbcode = dbcode;
	}

	@Override
	public String toString() {
		return "Synonyms [id=" + id + ", wdcode=" + wdcode + ", sort=" + sort + ", cname=" + cname + ", code=" + code + ", dbcode=" + dbcode + "]";
	}

}
