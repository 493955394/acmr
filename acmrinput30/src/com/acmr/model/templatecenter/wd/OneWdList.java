package com.acmr.model.templatecenter.wd;

import java.util.ArrayList;
import java.util.List;

public class OneWdList {
	private boolean isExclusive;//是否是排他
	private String exclusiveStr;//排他函数串
	private int priority;//排他优先级
	private List<WdItem> wd;//维度集合
	private String wdCode;//维度码
	public OneWdList(){
		this.isExclusive = false;
		this.wd = new ArrayList<WdItem>();
		this.exclusiveStr = "";
		this.priority = 0;
	}
	/**
	 * 全参数构造函数
	 */
	public OneWdList(boolean isExclusive,List<WdItem> wd,String exclusiveStr,int priority){
		this.isExclusive=isExclusive;
		this.wd=wd;
		this.exclusiveStr=exclusiveStr;
		this.priority=priority;
	}
	public String getWdCode() {
		//排他
		if(isExclusive){
			return wdCode;
		}
		//不是排他，拼接维度
		String sb = "";
		//拼接维度编码，用&连接
		if(!wd.isEmpty()){
			for(WdItem w:wd){
				sb+=w.getInputWdCode();
				sb+="&";
			}
		}
		if(sb.length()>0){
			return sb.substring(0, sb.length()-1);
		}else{
			return wdCode;
		}
	}
	public void setWdCode(String wdCode) {
		this.wdCode = wdCode;
	}
	public boolean isExclusive() {
		return isExclusive;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public void setExclusive(boolean isExclusive) {
		this.isExclusive = isExclusive;
	}
	public List<WdItem> getWd() {
		return wd;
	}
	public void setWd(List<WdItem> wd) {
		this.wd = wd;
	}
	public String getExclusiveStr() {
		return exclusiveStr;
	}
	public void setExclusiveStr(String exclusiveStr) {
		this.exclusiveStr = exclusiveStr;
	}
	/**
	 * 获取exclusive函数串
	 * @return 函数串
	 */
	public String showExclusive(){
		StringBuffer ex = new StringBuffer();
		ex.append("EXCLUSIVE[\"");
//		if(wd!=null&&wd.size()>0){
			for(WdItem w:wd){
				ex.append(w.getWdType().toString()+"_");
				ex.append(w.getCode()+",");
			}
//		}
		//删除最后一个，
		ex.deleteCharAt(ex.lastIndexOf(","));
		ex.append("\"");
		ex.append(",1]");
		exclusiveStr = ex.toString();
		return ex.toString();
	}
	 
	public static void main(String[] args) {
		String sb = "123456";
		//System.out.println(sb.substring(0, sb.length()-1));
	}
}
