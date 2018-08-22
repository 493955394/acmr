package com.acmr.model.datareport;

public class Datareport {

	private String id;
	private String name;
	private String type;
	private String href;
	private String updatetime;

	public Datareport() {
		// TODO Auto-generated constructor stub
	}

	public Datareport(String id, String name, String type, String href, String updatetime) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.href = href;
		this.updatetime = updatetime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

}
