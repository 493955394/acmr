package com.acmr.model.property;

public class Prop {
	private String name;
	private String code;
	private String posi;
	private String pos;
	private String title;
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
    private boolean ifExclusive=false;

    private boolean ifDelCss=false;
	public boolean isIfDelCss() {
		return ifDelCss;
	}

	public void setIfDelCss(boolean ifDelCss) {
		this.ifDelCss = ifDelCss;
	}


	public boolean isIfExclusive() {
		return ifExclusive;
	}

	public void setIfExclusive(boolean ifExclusive) {
		this.ifExclusive = ifExclusive;
	}
	private boolean isf = false;
	public boolean isIfr() {
		return ifr;
	}

	public void setIfr(boolean ifr) {
		this.ifr = ifr;
	}

	public boolean isIfEdit() {
		return ifEdit;
	}

	public void setIfEdit(boolean ifEdit) {
		this.ifEdit = ifEdit;
	}

	private boolean isthis = false;
	private boolean ifr=false;
	private boolean ifEdit=false;

	public Prop() {
		// TODO Auto-generated constructor stub
	}

	public boolean getIsf() {
		return isf;
	}

	public void setIsf(boolean isf) {
		this.isf = isf;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getPosi() {
		return posi;
	}

	public void setPosi(String posi) {
		this.posi = posi;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean getIsthis() {
		return isthis;
	}

	public void setIsthis(boolean isthis) {
		this.isthis = isthis;
	}

}
