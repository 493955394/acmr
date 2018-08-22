package com.acmr.web.jsp.log;

public interface LogConst {
	public static final String EXCSEC_DELETE="delete"; //操作：删除
	public static final String EXCSEC_BATCH_DELETE="batch_delete"; //操作：批量删除
	public static final String EXCSEC_UPDATE="update"; //操作：更新
	public static final String EXCSEC_INSERT="insert"; //操作：新增
	public static final String EXCSEC_FIND="find"; //操作：查找
	public static final String EXCSEC_BIND="bind"; //操作：绑定
	public static final String EXCSEC_UPLOAD="upload"; //操作：上传
	public static final String EXCSEC_DOWNLOAD="download"; //操作：下载
	public static final String EXCSEC_STATE_CHANGE="state_change"; //操作：状态改变（启用，停用）
	public static final String EXCSEC_MOVE="move"; //操作:移动
	public static final String EXCSEC_SUBMIT="submit"; //操作：提交
	public static final String EXCSEC_SAVEDBERR="savedb.err"; //保存到底库时出错
	public static final String EXCSEC_TMPSAVE="tmpsave";//暂存
	public static final String EXCSEC_SAVE="save";//保存
	//元数据管理额外功能
	public static final String EXC_QUERY_INFO="query_info"; //操作：查看指标信息
	public static final String EXC_QUERY="query"; //操作：查看节点下内容
	public static final String EXC_PLPP="plpp"; //操作：批量匹配详情
	public static final String EXC_ADV_QUERY="adv_query"; //操作：高级查询
	public static final String EXC_EXPORT1="export1"; //操作：导出当前checkbox下的所有数据
	public static final String EXC_EXPORT2="export2"; //操作：导出当前节点的下一层数据
	public static final String EXC_EXPORT3="export3"; //操作：导出搜索的数据
	public static final String EXC_EXPORT4="export4"; //操作：导出高级查询搜索的数据

	
	//审批管理操作
	public static final String EXC_PASSED="passed";//操作：通过
	public static final String EXC_REJECT="reject";//操作：驳回
	public static final String EXC_WITHDRAW="withdraw";//操作：撤回
	public static final String EXC_INSERTDB="insertdb";//操作：入底库
	
	public static final String COLUMN_PERSON="person"; //栏目：人员
	public static final String COLUMN_ROLE="role"; //栏目：角色
	public static final String COLUMN_DEPARTMENT="department"; //栏目：部门
	
	
	public static final String COLUMN_DATALOAD="dataload"; //栏目：数据加载
	
	
	public static final String COLUMN_APPROVE="approve";//栏目：数据审批



	public static final String COLUMN_ZBMGR="zbmgr"; //栏目：指标管理
	public static final String COLUMN_ZBGMGR="zbgmgr"; //栏目：指标分组管理
	public static final String COLUMN_REGMGR="regmgr"; //栏目：地区管理
	public static final String COLUMN_DATASRC="datasrc"; //栏目：数据来源管理
	public static final String COLUMN_DATETYPE="datetype"; //栏目：时间类型管理
	public static final String COLUMN_MDATE="sj"; //栏目：月度时间管理
	public static final String COLUMN_QDATE="qdate"; //栏目：季度时间管理
	public static final String COLUMN_YDATE="ydate"; //栏目：年度时间管理
	public static final String COLUMN_UNIT="unit"; //栏目：计量单位管理

	
	public static final String COLUMN_TEMPLATE="template"; //栏目：模板中心
	public static final String COLUMN_SYSTEM="system"; //栏目：制度管理
	public static final String COLUMN_TASK="task"; //栏目：任务管理
	public static final String COLUMN_QUERY="query"; //栏目：数据查询管理
	public static final String APPID="xtyinput";
	
}
