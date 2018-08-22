package com.acmr.model.pub;

import java.io.Serializable;

/**
 * 系统返回json数据定义.
 * 系统内返回json数据统一使用此类。
 *
 */
public class JSONReturnData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int returncode;	// 返回码. 200成功，500服务器错误，其它状态码各业务根据自身自定义, 599未登陆
	private Object returndata;	// 返回数据对象
	private Object param1;	// 备用参数1
	private Object param2;	// 备用参数2
	private Object param3;	// 备用参数3
	private Object param4;	// 备用参数4
	private Object param5;	// 备用参数5

	public JSONReturnData(int code1, String msg1) {
		this.returncode = code1;
		this.returndata = msg1;
	}

	public JSONReturnData(Object data1) {
		returncode = 200;
		returndata = data1;
	}

	public int getReturncode() {
		return returncode;
	}

	public void setReturncode(int returncode) {
		this.returncode = returncode;
	}

	public Object getReturndata() {
		return returndata;
	}

	public void setReturndata(Object returndata) {
		this.returndata = returndata;
	}

	public Object getParam1() {
		return param1;
	}

	public void setParam1(Object param1) {
		this.param1 = param1;
	}

	public Object getParam2() {
		return param2;
	}

	public void setParam2(Object param2) {
		this.param2 = param2;
	}

	public Object getParam3() {
		return param3;
	}

	public void setParam3(Object param3) {
		this.param3 = param3;
	}

	public Object getParam4() {
		return param4;
	}

	public void setParam4(Object param4) {
		this.param4 = param4;
	}

	public Object getParam5() {
		return param5;
	}

	public void setParam5(Object param5) {
		this.param5 = param5;
	}
	
}
