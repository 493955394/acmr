package com.acmr.model.pub;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户PO,TB_RIGHT_USER
 * @author xufeng
 *
 */
public class User implements Serializable {
	/**
	 * 
	 */
	private String userId;	// 用户编码
	private String userName;	// 用户名
	private String pwd;	// 密码
	private String cname;	// 姓名
	private String depcode = "";	// 部门编码
	private String deptname=""; //部门名称
	private String sex = "";	// 性别,0男，1女
	private String duty = "";	// 职务
	private String tel = "";	// 电话	
	private Date createTime;	// 创建时间
	private String state = "";	// 状态, 0失效，1有效，-1删除
	private String memo = "";	// 备注
	private String email = "";	// 邮件
	private String createUserId = "";	// 创建人	
	private String flowtype; // 审批类型 
	private String flowcode; // 审批code

	public String getFlowtype() {
		return flowtype;
	}
	public void setFlowtype(String flowtype) {
		this.flowtype = flowtype;
	}
	public String getFlowcode() {
		return flowcode;
	}
	public void setFlowcode(String flowcode) {
		this.flowcode = flowcode;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getDepcode() {
		return depcode;
	}
	public void setDepcode(String depcode) {
		this.depcode = depcode;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	
}
