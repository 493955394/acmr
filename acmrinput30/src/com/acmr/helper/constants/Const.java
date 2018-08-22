package com.acmr.helper.constants;

import com.acmr.helper.util.PropertiesReaderUtil;


/**
 * 常量
 * 
 */
public class Const {
	// 系统当前登录用户的键值，可用于从session中获取当前登录人信息，返回UserModel对象
	public static final String CURRENT_USER = "currentUser";

	// CCache缓存中的关键字
	public static final String CACHE_MENU_NAV = "cacheMenuNav"; // 缓存页面导航

	// 数据库连接池名称
	public static final String DB_INPUT = "input"; // 业务库
	public static final String DB_MOD = "mod"; // cube库
	public static final String DB_PUB = "pub"; // 发布库

	// 真假字符串
	public static final String BOOLEAN_TRUE_STRING = "1"; // 真
	public static final String BOOLEAN_FALSE_STRING = "0"; // 假
	// 通用状态说明
	public static final String COMMON_STATE_1_ = "-1"; // 删除状态
	public static final String COMMON_STATE_0 = "0"; // 失效状态
	public static final String COMMON_STATE_1 = "1"; // 有效状态

	public static final String COMMON_STATE_false = "0"; // 否
	public static final String COMMON_STATE_true = "1"; // 是

	// 制度类别代码
	public static final String SYSTEM_TYPE_0 = "0"; // 系统制度
	public static final String SYSTEM_TYPE_1 = "1"; // 部门制度
	public static final String SYSTEM_TYPE_2 = "2"; // 临时制度

	// 制度状态代码
	public static final String SYS_STATE_1_ = "-1"; // 无效（无模板）
	public static final String SYS_STATE_0 = "0"; // 有效没启用（有模板）
	public static final String SYS_STATE_1 = "1"; // 有效并启用

	// 任务类型
	public static final String TASK_TYPE_0 = "0"; // 系统任务
	public static final String TASK_TYPE_1 = "1"; // 临时任务

	// 数据校验
	public static final String TASK_STATE_01 = "1";// 已生成--待填报
	public static final String TASK_STATE_02 = "2";// 已经上传--草稿
	public static final String TASK_STATE_03 = "3";// 已提交--待审核
	public static final String TASK_STATE_04 = "4";// 已审核--已入库

	public static final String TASK_STATE_05 = "5";// 已推送
	public static final String TASK_STATE_06 = "6";// 已校验

	public static final String TASK_STATE_07 = "7"; // 入库中
	public static final String TASK_STATE_08 = "8"; // 有错误
	
	public static final String TASK_STATE_09 = "9";// 关闭

	// 制度期别类型代码
	public static final String SYSTEM_SORT_0 = "0"; // 一次性的
	public static final String SYSTEM_SORT_TMP = "TMP"; // 系统六维临时制度
	public static final String SYSTEM_SORT_D = "D"; // 日制度
	public static final String SYSTEM_SORT_W = "W"; // 周制度
	public static final String SYSTEM_SORT_X = "X"; // 旬制度
	public static final String SYSTEM_SORT_M = "M"; // 月制度
	public static final String SYSTEM_SORT_Q = "Q"; // 季制度
	public static final String SYSTEM_SORT_Y = "Y"; // 年制度

	// 模板类型代码
	public static final String TEMPLATE_TYPE_0 = "0"; // 临时模板
	public static final String TEMPLATE_TYPE_D = "D"; // 日模板
	public static final String TEMPLATE_TYPE_W = "W"; // 周模板
	public static final String TEMPLATE_TYPE_X = "X"; // 旬模板
	public static final String TEMPLATE_TYPE_M = "M"; // 月模板
	public static final String TEMPLATE_TYPE_Q = "Q"; // 季模板
	public static final String TEMPLATE_TYPE_Y = "Y"; // 年模板

	// 审批类型
	public static final String APPROVAL_TYPE_0 = "0"; // 多人审批单人通过
	public static final String APPROVAL_TYPE_1 = "1"; // 多人审批，多人通过

	// 模板下载类型
	public static final String MOD_DOWNLODA_TYPE_1 = "1"; // 不带代码标示
	public static final String MOD_DOWNLODA_TYPE_2 = "2"; // 带代码标示
	public static final String MOD_DOWNLODA_TYPE_3 = "3"; // 包含各个维度码表信息
	public static final String MOD_DOWNLODA_TYPE_4 = "4"; // 对照表
	// 数据处理方式
	public static final String IGNOREDATA = "0";
	public static final String COVERDBDATA = "1";
	public static final String DELETEDBDATA = "2";
	// 模板状态代码
	public static final String MOD_STATE_0 = "0";// 草稿状态
	public static final String MOD_STATE_1 = "1";// 使用状态

	// 校验状态编码
	public static final String CHECK_STATE_0 = "0";// 校验通过，即无错误
	public static final String CHECK_STATE_1 = "1";// 校验不通过，即有错误

	// 元数据管理
	public static final String[] DEFAULT_CODE = { "ZB"}; // 指标默认的代码
	public static final String ZB_CODE = "ZB"; // 指标默认的代码
	public static final String DEFAULT_UNITCODE = "UGB"; // 单位默认的代码
	public static final String IFDATA = "1";// 是否是指标 1表示指标
	public static final String ZB_FILE_NAME = "指标数据"; // 指标下载时的文件名
	public static final String COMPANY_FILE_NAME = "主体数据"; // 指标下载时的文件名
	public static final String ZBG_FILE_NAME = "指标分组数据";// 指标分组下载时的文件名
	public static final String REG_FILE_NAME = "地区数据";// 地区下载时的文件名
	public static final String DATASRC_FILE_NAME = "数据来源数据";// 数据来源下载时的文件名
	public static final String UNIT_FILE_NAME = "计量单位数据";// 计量单位下载时的文件名
	public static final String DATETYPE_FILE_NAME = "时间类型数据";// 时间类型下载时的文件名
	public static final String  FILE_NAME = "时间数据";// 月度时间下载时的文件名
 
	// 临时数据表样导出
	public static final String TASK_TEMP_NAME = "临时表样数据";

	// 模板单位放置位置常亮
	public static final String UNIT_POS_TYPE_01 = "1";// 默认维度后加（）
	public static final String UNIT_POS_TYPE_02 = "2";// 右上角
	public static final String UNIT_POS_TYPE_03 = "3";// 行
	public static final String UNIT_POS_TYPE_04 = "4";// 列

	// 高级查询默认码
	public static final String ADV_CODE = "dkyd";// 列
	public static final String ADVN_CODE = "dkndcx";// 列 新高级查询使用
	public static final String ADV_NUM = "4";// 列 新高级查询使用

	// 模板说明位置
	public static final String REMARK_POS_H = "h";// 头部备注
	public static final String REMARK_POS_E = "f";// 尾部备注

	// 模板单位放置位置
	public static final String UNIT_POS_CO = "common";// 放置在指标后
	public static final String UNIT_POS_Q = "q";// 放置在右上角
	public static final String UNIT_POS_R = "r";// 放置在一行
	public static final String UNIT_POS_C = "c";// 放置在一列

	// 模板拖拽模块代码
	public static final String MOD_MODE_ZL = "posiindex";// 主栏
	public static final String MOD_MODE_BL = "posiguest";// 宾栏
	public static final String MOD_MODE_RE = "posiremark";// 说明
	public static final String MOD_MODE_UNIT = "posiunit";// 统一单位设置
	public static final String MOD_MODE_FI = "posifilter";// 筛选条件

	// 返回的最大指标列数
	public static final int WD_MAX_SIZE = 500;

	public static final String CATALOG_TYPE_CATA = "t";
	public static final String CATALOG_TYPE_DATA = "c";

	// 数据库类型
	public static final String DB_CODE = "jjk";// 基础库

	// 数据变更修改类型
	public static final String CHANGE_DATA_NORM = "1";// 正常修改
	public static final String CHANGE_DATA_ERR = "2";// 错误修改_

	// 综合机关、制表机关
	// public static final String COMP_OFFICE = "福建调查总队";//重庆统计局
	// public static final String CREATE_OFFICE="福建调查总队";

	public static final String COMP_OFFICE = PropertiesReaderUtil.get("system.comp_office");// 重庆统计局
	public static final String CREATE_OFFICE = PropertiesReaderUtil.get("system.create_office");// 重庆统计局
	public static final String DOWNLOAD_TYPE = PropertiesReaderUtil.get("download_type");// 下载格式，excel2003或2007
	public static final String EXCEL_URL = PropertiesReaderUtil.get("excel_url");// excel工程地址
	public static final String BOOK_DOWNLOAD_EXCEL = PropertiesReaderUtil.get("book_download_excel");// 一键办公excel下载路径
	public static final String BOOK_DOWNLOAD_HTML = PropertiesReaderUtil.get("book_download_html");// 一键办公html下载路径
	public static final String BOOK_DOWNLOAD_PDF = PropertiesReaderUtil.get("book_download_pdf");// 一键办公pdf下载路径
	public static final String BOOK_PAGE_IMAGE = PropertiesReaderUtil.get("book_page_image");
	public static final String ZB_RANDOM_NUMBER = PropertiesReaderUtil.get("zb_random_number"); // 指标管理是否要随机码
	// 模板变量取值位置定义
	public static final String VAR_POSITION_BOOKTIME = "var_position_booktime";// 年鉴时间
	public static final String VAR_POSITION_FILTERTIME = "var_position_filtertime";// 筛选里面的时间
	public static final String VAR_POSITION_FILENAME = "var_position_filename";// 文件名中的时间
	public static final String VAR_POSITION_SHEETNAME = "var_position_sheetname";// sheet名里面的时间
	
	// 流程实例状态

		/** 流程运行 */
		public static final String FLOW_INSTANSE_STATE_00 = "0";
		/** 流程通过 */
		public static final String FLOW_INSTANSE_STATE_01 = "1";
		/** 流程驳回 */
		public static final String FLOW_INSTANSE_STATE_02 = "2";
		/** 流程挂起 */
		public static final String FLOW_INSTANSE_STATE_03 = "3";
		/** 流程撤回*/
		public static final String FLOW_INSTANCE_STATE_04 = "4";

		// 流程实例结点
		/** 起挂 */
		public static final String FLOW_NODE_INSTANSE_STATE_00 = "0";
		/** 运行 */
		public static final String FLOW_NODE_INSTANSE_STATE_01 = "1";
		/** 审核通过 */
		public static final String FLOW_NODE_INSTANSE_STATE_02 = "2";
		/** 审核驳回 */
		public static final String FLOW_NODE_INSTANSE_STATE_03 = "3";
		
		//审核通过
		public static final String FLOW_NODE_INSTANCE_PASSSED="6";

	// 流程实例结点项
	/** 起挂 */
	public static final String FLOW_NODE_INSTANSE_DATA_STATE_00 = "0";
	/**  运行*/
	public static final String FLOW_NODE_INSTANSE_DATA_STATE_01 = "1";
	/** 审核通过  */
	public static final String FLOW_NODE_INSTANSE_DATA_STATE_02 = "2";
	/** 审核驳回 */
	public static final String FLOW_NODE_INSTANSE_DATA_STATE_03 = "3";
	/** 其他人审核通过 */
	public static final String FLOW_NODE_INSTANSE_DATA_STATE_04 = "4";
	/** 其他人审核驳回 */
	public static final String FLOW_NODE_INSTANSE_DATA_STATE_05 = "5";
	
	//审核通过
	public static final String FLOW_NODE_INSTANCE_DATA_PASSED="6";
	
	public static final String WEBLOGURL=PropertiesReaderUtil.get("weblogurl");

	// 时间变量code定义
	public static final String SJ_VAR_CODE = "SJ_00_01";// 最近五年

	public static enum CheckStep {
		TemplateCheck, TableDataRepeatCheck, DBDataRepeatCheck, TimeTypeCheck, SwingCheck;
	}

	public static enum HandleType {
		// 表内重复
		delcurrentdata,
		// 底库重复1：单个使用新数2：全部使用新数3：单个使用底库4：全部使用底库
		useonecurrentdata, useallcurrentdata, useonedbdata, usealldbdata,
		// 时间类型、摆动
		forcepassall, forcepassone,
	}

	public static String getExcelProjectUrl() {
		return EXCEL_URL;
	}
	/**
	 * 自定义查询指标树生成状态
	 */
	public static int customQueryStatus = 0;
}
