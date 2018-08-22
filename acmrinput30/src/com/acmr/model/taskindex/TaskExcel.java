package com.acmr.model.taskindex;

public class TaskExcel {
	private String taskCode;// 任务编码
	private String updateTime;// 更新时间
//	private String excelData;// 数据文件
	private String dataFileSuffix;// 数据文件后缀
	private String templateFileSuffix;// 模板文件后缀
	private String metaCodeIndex;// 所有维度集合
	private String excelObject;// 后台excel对象，含数据
	private String templateWithWD;// 有维度标记的excel模板文件
	private String templateWithoutWD;// 不含维度标记的excel模板文件

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

//	public String getExcelData() {
//		return excelData;
//	}
//
//	public void setExcelData(String excelData) {
//		this.excelData = excelData;
//	}

	public String getDataFileSuffix() {
		return dataFileSuffix;
	}

	public void setDataFileSuffix(String dataFileSuffix) {
		this.dataFileSuffix = dataFileSuffix;
	}

	public String getTemplateFileSuffix() {
		return templateFileSuffix;
	}

	public void setTemplateFileSuffix(String templateFileSuffix) {
		this.templateFileSuffix = templateFileSuffix;
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

	public String getTemplateWithWD() {
		return templateWithWD;
	}

	public void setTemplateWithWD(String templateWithWD) {
		this.templateWithWD = templateWithWD;
	}

	public String getTemplateWithoutWD() {
		return templateWithoutWD;
	}

	public void setTemplateWithoutWD(String templateWithoutWD) {
		this.templateWithoutWD = templateWithoutWD;
	}

}
