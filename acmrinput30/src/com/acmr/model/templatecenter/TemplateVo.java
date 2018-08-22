/**
 * TemplateVo.java
 *北京华通人商用信息有限公司版权所有
 */
package com.acmr.model.templatecenter;

import java.util.Date;
/**
 * 批量上传模板前台vo
 * @author jinhr
 */
public class TemplateVo {
	private Integer fileNum; // 文件序号
	private String code;
	private String fileName; // 文件名称(文件唯一标识)
	private String sheetName; // sheet名称
	private String excelName; // excel名字
	private String size;
	private String flag;
	private Date createTime;// 创建时间
	private String createUserId;// 创建人
	private String memo;// 说明
	private String state;// 状态
	private String procode;// 父模板id
	private String templateType;// 类型
	private String isMod;// 是否模板,0模板分类,1模板
	private String templateSort;// 模板种类：1，excel模板2，拖拽模板3，标记定义模板
	private String templateCode;// 模板编码
	private byte[] standContent;// 标记大字段
	private byte[] excelContent;// excel大字段
	private String metaCodeIndex;// 维度集合字符串
	private String sufix;

	public Integer getFileNum() {
		return fileNum;
	}

	public void setFileNum(Integer fileNum) {
		this.fileNum = fileNum;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getProcode() {
		return procode;
	}

	public void setProcode(String procode) {
		this.procode = procode;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getIsMod() {
		return isMod;
	}

	public void setIsMod(String isMod) {
		this.isMod = isMod;
	}

	public String getTemplateSort() {
		return templateSort;
	}

	public void setTemplateSort(String templateSort) {
		this.templateSort = templateSort;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public byte[] getStandContent() {
		return standContent;
	}

	public void setStandContent(byte[] standContent) {
		this.standContent = standContent;
	}

	public byte[] getExcelContent() {
		return excelContent;
	}

	public void setExcelContent(byte[] excelContent) {
		this.excelContent = excelContent;
	}

	public String getMetaCodeIndex() {
		return metaCodeIndex;
	}

	public void setMetaCodeIndex(String metaCodeIndex) {
		this.metaCodeIndex = metaCodeIndex;
	}

	public String getSufix() {
		return sufix;
	}

	public void setSufix(String sufix) {
		this.sufix = sufix;
	}

	public String getExcelName() {
		return excelName;
	}

	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}

}
