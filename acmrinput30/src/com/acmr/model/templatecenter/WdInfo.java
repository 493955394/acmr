package com.acmr.model.templatecenter;

/**
 * 维度信息对象
 * 
 * @author chenyf
 *
 */
public class WdInfo {

	private String code; // 代码

	private String name; // 名称

	private String nickname; // 自定义名称

	private Boolean ifprocode = false;

	public WdInfo() {
		// TODO Auto-generated constructor stub
	}

	public WdInfo(String code, String name, String nickname, Boolean ifprocode) {
		this.code = code;
		this.name = name;
		this.nickname = nickname;
		this.ifprocode = ifprocode;
	}

	public Boolean getIfprocode() {
		return ifprocode;
	}

	public void setIfprocode(Boolean ifprocode) {
		this.ifprocode = ifprocode;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
