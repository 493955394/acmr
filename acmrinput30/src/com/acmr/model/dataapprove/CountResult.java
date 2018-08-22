package com.acmr.model.dataapprove;

import java.io.Serializable;
/**
 * 统计入库结果
 * @author anzhao
 *
 */
public class CountResult implements Serializable {
	private String islast="0";//是否是最后异一步
	private Integer countdb;//已入库
	private Integer countnodb;//未入库
	public CountResult(){
		
	}
	public CountResult(	String islast,Integer countdb,Integer countnodb){
		this.islast=islast;
		this.countdb=countdb;
		this.countnodb=countnodb;
	}
 
	
	public String getIslast() {
		return islast;
	}


	public void setIslast(String islast) {
		this.islast = islast;
	}


	public Integer getCountdb() {
		return countdb;
	}

	public void setCountdb(Integer countdb) {
		this.countdb = countdb;
	}

	public Integer getCountnodb() {
		return countnodb;
	}

	public void setCountnodb(Integer countnodb) {
		this.countnodb = countnodb;
	}
	
	
    
}
