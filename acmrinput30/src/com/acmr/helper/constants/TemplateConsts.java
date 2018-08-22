package com.acmr.helper.constants;


/**
 * 模板中心常量类
 * @author caosl
 */
public class TemplateConsts {
	public static final int MEMCACHED_EXP_TIME = 60*60*1;
	public static int COL_WD_POSITION = 1;//列维度写到第二行
	public static int ROW_WD_POSITION = 0;//行维度写到第一列
	
	public static int WD_ERROR_Empty = 2;//不需要录数
	public static int WD_ERROR_regular = 1;//七维齐全
	public static int WD_ERROR_Error = 0;//缺失维度
	
	public enum PosititonType{//维度编码来源位置
		table,
		r,
		c,
		cell,
	}
	
	public enum HandleExcelType{
		frommod,
		fromreport,
	}
	// 操作对象类型
	public enum ObjectType {
		templatematch;
	}
	// 函数类型
	public enum FunType {
		getCodeOrTxt,//获取元数据编码或者字符串
		getExclusive,//获取排他的维度及优先级
		getPosition,//获取位置
		getCell,//获取单元格
		noFunction,//非函数
		excuteOnOnalysis, //模板解析可以执行的函数
		excuteOnDataload, //录数时后才能解析的函数
		range, //range函数
		filter, //filter函数
	}
	
	//维度类型
	public enum WdType{
		zb,fl,tt,reg,sj,ds,unit;
	}
	//字符串转维度类型
	public static WdType getWdTypeByStr(String wdStr){
		switch(wdStr.toUpperCase()){
		case "I": return WdType.zb;
		case "ZB": return WdType.zb;
		case "S": return WdType.fl;
		case "FL": return WdType.fl;
		case "T": return WdType.sj;
		case "SJ": return WdType.sj;
	 
		case "TT": return WdType.tt;
		case "SF": return WdType.tt;
		case "R": return WdType.reg;
		case "REG": return WdType.reg;
		case "DQ": return WdType.reg;
		case "DS": return WdType.ds;
		case "LY": return WdType.ds;
		case "DW": return WdType.unit;
		case "U": return WdType.unit;
		case "UNIT":return WdType.unit;
		default:return null;
		}
	}
	//所有函数
	public static String executableFun = "R[,C[,CELL[,DIMINFO[,"
			+ "GETCODE[,EXCLUSIVE[,LIST[,PROCODE[,"
			+ "IF[,SEPARATE[,"
			+ "INCLUDE[,EXCLUDE[";
	//非check函数
	public static String dataLoadExcFun = "FILENAME[,SHEETNAME[,NOWTIME[,FILTER[,GETFILTER[,SAMEPERIOD[,"
			+ "BEFORETIME[,RANGE[,COUNT[,SUM[,AVERAGE[,MAX[,MIN[";
	/**
	 * SheetpropertyType类型
	 */
	public enum SheetPropertyType {
		titlepos, //报表标题位置标记区---PositionInfo
		mainbarpos, //报表主栏位置标记区---PositionInfo
		guestbarpos, //报表宾栏位置标记区---PositionInfo
		unitpos, //报表单位位置标记区---PositionInfo
		calculatepos, //计算信息所在位置标记区---PositionInfo
		checkpos, //校验信息所在位置标记区---PositionInfo
		remarkpos, //注释信息所在位置标记区---PositionInfo
		headTxtpos,	//头部注释区域
		datapos, //数据区域
		
		
		calculateInfo, //计算信息
		checkinfo, //校验信息
		filename, //excel文件名称
		titleCellList, //标题单元格集合
		match, //匹配信息----MatchList
		completeWdMod, //维度是否是齐全的，默认为全的true
		dbCode, //模板年月季类型---PositionInfo
		modId, //模板中心用id
		wdList, //整表维度集合---WdItemList
		templateType, //模板类型3
		error, //错误 ---ErrorList
		createError, //创建过程报错信息
		errorWd,
		regularWd,
		emptyWd,
		rangeForHandle, //后台range----List<RangeInfoForHandle>
		filterForHandle, //后台filter---List<FilterInfoForHandle>
		
		hasColRange, //boolean
		hasRowRange, //boolean
		rowRangeWdList, //列range
		colRangeWdList, //行range
	}
	/**
	 * range类型
	 */
	public enum RangeType{
		zlRange,//主栏range
		blRange,//宾栏range
	}
	/**
	 * RowpropertyType类型
	 */
	public enum RowPropertyType {
		hight,	//高度像素
		wdList,	//行维度集
	}
	/**
	 * ColpropertyType类型
	 */
	public enum ColPropertyType {
		width,	//高度像素
		wdList,	//行维度集
	}
	/**
	 * CellpropertyType类型
	 */
	public enum CellPropertyType {
		celltype,	//cell类型
		WdList,	//对应WdList---WdItemList
		Error,	//对应错误信息---ExcelError
		hasFun, //是否含有公式 ：true or false
		funcStrOnload,	//录数时执行的公式
		CellStatus,	//单元格状态，报错单元格---对应CellStatus enum
		dataWdList,
		wdCheck,//是否含有维度类型0：维度不全1：维度全 2：不是数据格子
	}
	/**
	 * 单元格类型
	 */
	public enum CellType{
		text,
		data,
	}
	// 单元格状态
	public enum CellStatus{
		great,	//正常单元格
		error,	//报错单元格
		missingWd,	//缺失维度的--
		excuOnload,	//录数时调用函数
		match,	//匹配单元格
	}
	
	public static String[] executableFunList;//所有函数
	public static String[] dataLoadExcFunList;//数据录入时才能执行的函数
	static{
		executableFunList = executableFun.split(",");
		dataLoadExcFunList = dataLoadExcFun.split(",");
	}
	public static final String UNMATCH_ERROR_CODE = "222";
	public static final String UNMATCH_ERROR_MSG = "单元格不匹配";
	
	
	
	/**
	 * 维度属性前台展示类别
	 * 格子所在的位置
	 */
	public enum WdPropertyShowType{
		table,	//整表维度
		zhulan,	//返回列维度+整表
		binlan,	//返回行维度+整表
		dataCell, //行维度+列维度+整表维度
		nowd,	//不含有任何维度
	}
	/**
	 * 伪码集
	 */
	public enum DefaultUnknownWdCode{
		range, //range伪码
		unknownFunWdCode, //模板解析不能执行的伪码
	}
}
