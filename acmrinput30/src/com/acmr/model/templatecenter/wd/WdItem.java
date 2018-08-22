package com.acmr.model.templatecenter.wd;

import com.acmr.helper.constants.TemplateConsts.PosititonType;
import com.acmr.helper.constants.TemplateConsts.WdType;

/**
 * 维度
 * @author caosl
 * 判断逻辑
 * 1，是否是排他，如果是的话，根据排他权重直接获取维度编码放入code
 * 2，不含有排他，是否是公式，如果是公式，看是否需要筛选
 * 3，都不是，则就是维度码
 */
public class WdItem implements Cloneable{
	private PosititonType posType;
	//是否是公式
	private boolean hasExpression;
	//是否是filter
	private boolean hasFilter;
	//维度类型
	private WdType wdType;
	//维度码值(可能是表达式)
	private String code;
	//录数用的维度编码码值
	private String inputWdCode;
	public WdItem(){}
	/**
	 * 构造1
	 * @param isExpression 是否是公式
	 * @param hasFilter 是否是filter
	 * @param wdType 维度类型
	 * @param code 模板中的码值（有可能是公式）
	 * @param inputWdCode（实际使用维度编码--底库存在的）
	 * @param posType（位置类型）
	 */
	public WdItem(boolean isExpression,boolean hasFilter,
			WdType wdType, String code,String inputWdCode,PosititonType posType) {
		this.hasExpression=isExpression;
		this.hasFilter=hasFilter;
		this.wdType=wdType;
		this.code=code;
		this.inputWdCode=inputWdCode;
		this.posType=posType;
	}
	public String getInputWdCode() {
		return inputWdCode;
	}
	public void setInputWdCode(String inputWdCode) {
		this.inputWdCode = inputWdCode;
	}
	public PosititonType getPosType() {
		return posType;
	}
	public void setPosType(PosititonType posType) {
		this.posType = posType;
	}
	public boolean isHasFilter() {
		return hasFilter;
	}
	public void setHasFilter(boolean hasFilter) {
		this.hasFilter = hasFilter;
	}
	public boolean isHasExpression() {
		return hasExpression;
	}
	public void setHasExpression(boolean hasExpression) {
		this.hasExpression = hasExpression;
	}
	public WdType getWdType() {
		return wdType;
	}
	public void setWdType(WdType wdType) {
		this.wdType = wdType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
