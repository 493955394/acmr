package com.acmr.dao.sysindex;

import java.util.List;

import com.acmr.model.pub.PageBean;
import com.acmr.model.pub.RightDepartment;
import com.acmr.model.pub.TreeNode;
import com.acmr.model.sysindex.OneYearReportStageVoList;
import com.acmr.model.sysindex.SystemIndex;
import com.acmr.model.sysindex.SystemPlan;
import com.acmr.model.templatecenter.TemplateIndex;

public interface ISysIndexDao {

	/**
	 * 通过部门编码获取制度树（展示的是制度树）
	 * 
	 * @param deptCode
	 *            部门编码
	 * @param proCode
	 *            制度编码
	 * @return 制度树集合
	 */
	public List<TreeNode> getSysIndexTreeByDeptCode(String deptCode, String proCode);

	/**
	 * 新增制度
	 * 
	 * @param systemIndex
	 *            制度对象
	 * @return 操作成功条数
	 */
	public int insert(SystemIndex systemIndex);

	/**
	 * 更新制度
	 * 
	 * @param systemIndex
	 *            制度对象
	 * @return 成功条数
	 */
	public int update(SystemIndex systemIndex);

	/**
	 * 删除制度
	 * 
	 * @param systemIndex
	 *            制度对象
	 * @return 成功条数
	 */
	public int delete(String code);

	/**
	 * 查找制度
	 * 
	 * @param SystemIndex
	 *            制度对象
	 * @return 制度分页对象
	 */
	public PageBean<SystemIndex> findSystemIndex(String isTable, String systemStageType, PageBean<SystemIndex> page, SystemIndex systemIndex,String type);

	/**
	 * 查找所有有效制度
	 */
	public List<SystemIndex> findAllSystemIndex();

	/**
	 * 通过code取得SystemIndex制度
	 * 
	 * @param code
	 *            制度编码
	 * @return 制度对象
	 */
	public SystemIndex getSystemIndex(String code);

	/**
	 * 根据制度编码获取其下级组织树节点
	 * 
	 * @param code
	 *            制度编码
	 * @param isTable
	 *            报表或分类
	 * @return 制度树节点
	 */
	public List<TreeNode> findSysTree(String code, String isTable);

	/**
	 * 根据制度编码获取其下级组织树节点
	 * 
	 * @param code
	 *            制度编码
	 * @return 制度树节点
	 */
	public List<TreeNode> findAllSystemTree(String code);

	/**
	 * 根据制度编码获取其下级组织树节点
	 * 
	 * @param code
	 *            制度编码
	 * @param sort
	 *            制度期别
	 * @return 制度树节点
	 */
	public List<TreeNode> findSysTableTree(String code, String sort);

	/**
	 * 获得有权限的制度树
	 * 
	 * @param code
	 * @param sort
	 * @return 制度树节点
	 */
	public List<TreeNode> findSysTabTree(String code, String sort);

	/**
	 * 启用或者停用一个制度
	 * 
	 * @param id
	 *            制度编码
	 * @return 操作成功数
	 */
	public int deleteOrUse(String id);

	/**
	 * 查找出所有的部门
	 * 
	 * @return 部门集合对象
	 */
	public List<RightDepartment> listAllDept();

	/**
	 * 查找出所有的部门
	 * 
	 * @param code
	 *            制度编码
	 * @return 部门集合对象
	 */
	public List<RightDepartment> listAllDept(String code);

	/**
	 * 通过制度编码查找制度下的模板
	 * 
	 * @param code
	 *            制度编码
	 * @return 制度集合
	 */
	public List<TemplateIndex> findTemplateListBySysCode(String code);

	/**
	 * 更新制度计划的方法
	 * 
	 * @param systemIndex
	 *            制度对象
	 * @throws DaoException
	 */
	public void updateSysPlan(SystemIndex systemIndex);

	/**
	 * 判断该节点是否为父节点
	 * 
	 * @param code
	 *            制度编码
	 * @return 是否为父节点
	 */
	public int isParent(String code);

	/**
	 * 根据code查找制度数量
	 * 
	 * @param code
	 *            制度编码
	 * @return 制度数量
	 */
	public int getCountByCode(String code);

	/**
	 * 根据制度编码查找制度数量
	 * 
	 * @param tableNum
	 *            制度编码
	 * @return 制度数量
	 */
	public int getCountByTableNum(String tableNum);

	/**
	 * 根据部门编码查询所有子部门编码
	 * 
	 * @param pDeptCode
	 *            部门编码
	 * @return 子部门编码
	 */
	public List<String> getCDeptCodeByPDeptCode(String pDeptCode);

	/**
	 * 是否是报表
	 * 
	 * @param sysCode
	 *            制度编码
	 * @return 是否为报表
	 */
	public boolean isTable(String sysCode);

	/**
	 * 获得父类树
	 * 
	 * @param code
	 *            制度编码
	 * @param deptCode
	 *            部门编码
	 * @return 制度树节点
	 */
	public List<TreeNode> getParentTree(String code, String deptCode);

	/**
	 * 获得父类树
	 * 
	 * @param code
	 *            制度编码
	 * @param deptCode
	 *            部门编码
	 * @return 制度树节点
	 */
	public List<TreeNode> getParentTreeFromSys(String code, String deptCode);

	/**
	 * 获得制度记录数
	 * 
	 * @param systemIndex
	 *            制度对象
	 * @return 制度记录数
	 */
	public int getPageNum(SystemIndex systemIndex);

	/**
	 * 查找制度计划
	 * 
	 * @param syscode
	 *            制度代码
	 * @return 制度计划对象
	 */
	public SystemPlan getSystemPlan(String syscode);

	/**
	 * 查找子制度集合
	 * 
	 * @param syscode
	 *            制度代码
	 * @return 子制度集合
	 */
	public List<SystemIndex> getChildren(String sysCode);

	/**
	 * 更新所有子制度部门
	 * 
	 * @param systemIndex
	 *            子制度
	 * @param deptCode
	 *            部门编码
	 */
	public void updateAllChildrenDeption(SystemIndex systemIndex, String deptCode);

	/**
	 * 获取所有子制度
	 * 
	 * @param code
	 *            制度编码
	 * @return 子制度集合
	 */
	public List<SystemIndex> getAllChildren(String code);

	/**
	 * 获取所有子制度
	 * 
	 * @param code
	 *            制度编码
	 * @return 子制度集合
	 */
	public List<SystemIndex> getAllChildrenIndex(String code);

	/**
	 * 批量删除制度
	 * 
	 * @param codes
	 *            制度编码
	 * @throws DaoException
	 * @return 成功数量
	 */
	public int deleteSysIndexByBatch(String codes);

	/**
	 * 批量删除报表
	 * 
	 * @param codes
	 *            制度编码
	 * @throws DaoException
	 * @return 成功数量
	 */
	public int deleteSysTableByBatch(String codes);

	/**
	 * 更新所有子制度的流程
	 * 
	 * @param codes
	 *            制度编码
	 * @param flowCode
	 *            流程编码
	 * @throws DaoException
	 * @return 成功数量
	 */
	public void updateAllChildFlow(String code, String flowCode);

	// /**
	// * 根据制度名字查询模板
	// * @param key 制度名称
	// * @return 模板集合
	// */
	// public List<SearchResultVo> findSystemByKeyWords(String key);
	/**
	 * 根据制度编码定位树
	 * 
	 * @param key
	 *            制度编码
	 * @return 定位后的制度树
	 */
	public List<TreeNode> getParentSystemTree(String id);

	/**
	 * 通过部门编码获取制度分类树
	 * 
	 * @param deptCode
	 *            部门编码
	 * @param proCode
	 *            制度编码
	 * @return 制度树集合
	 */
	public List<TreeNode> getSysTreeByDeptCode(String deptCode, String proCode);

	public List<SystemIndex> getCanCopySysList(String sort, String deptCode);

	/**
	 * 通过模板id获取模板文件后缀
	 * 
	 * @param templateId
	 * @return 模板文件后缀名
	 */
	public String getFileSuffix(String templateId);

	/**
	 * 通过id获取大字段
	 * 
	 * @param code
	 *            模板id
	 * @return 所查字段的byte
	 */
	public byte[] getBLOBBycode(String code, String field);

	public List<String> getYears(String sysCode);

	public OneYearReportStageVoList getOneYearPlan(String sysCode, String year);

	public List<TreeNode> getSysIndexTree(String deptCode, String proCode, String sysType);

	public List<TreeNode> findModTree(String id);
}
