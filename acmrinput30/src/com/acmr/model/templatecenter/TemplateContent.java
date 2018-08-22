package com.acmr.model.templatecenter;

public class TemplateContent {
	private String templateCode;// 模板编码
	private String fileSuffix;// 文件后缀
	private String metaCodeIndex;// 维度集合字符串
	private String excelObject;// exceljson对象
	private String objectForWeb;// 前后台交互用对象
	private String templateWithoutWD;// 不含维度标记的excel模板文件
	private String excelForWeb;// 前台展示用excelJSON
	private String templateSort;//  模板类型，1：设计视图；1以外的都是标记视图
	private String templateType;//  模板数据库类型：MQY

	public String getTemplateSort() {
		return templateSort;
	}

	public void setTemplateSort(String templateSort) {
		this.templateSort = templateSort;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public String getMetaCodeIndex() {
		return metaCodeIndex;
	}

	public void setMetaCodeIndex(String metaCodeIndex) {
		this.metaCodeIndex = metaCodeIndex;
	}

	public String getExcelObject() {
		return excelObject;
	}

	public void setExcelObject(String excelObject) {
		this.excelObject = excelObject;
	}

	public String getObjectForWeb() {
		return objectForWeb;
	}

	public void setObjectForWeb(String objectForWeb) {
		this.objectForWeb = objectForWeb;
	}

	public String getExcelForWeb() {
		return excelForWeb;
	}

	public void setExcelForWeb(String excelForWeb) {
		this.excelForWeb = excelForWeb;
	}

	public String getTemplateWithoutWD() {
		return templateWithoutWD;
	}

	public void setTemplateWithoutWD(String templateWithoutWD) {
		this.templateWithoutWD = templateWithoutWD;
	}

}
