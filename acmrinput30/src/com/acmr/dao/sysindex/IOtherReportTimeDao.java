package com.acmr.dao.sysindex;

import com.acmr.model.sysindex.OtherReportTime;

public interface IOtherReportTimeDao {
	/**
	 * 新增时间规划
	 * @param SystemPlan 制度计划
	 * @return 成功条数
	 */
	public int insert(OtherReportTime otherReportTime);
	
	/**
	 * 根据id查找
	 * @param id
	 * @return
	 */
	public OtherReportTime getTimeById(String id);
	
	/**
	 * 根据planid删除
	 * @param planid
	 * @return
	 * @throws DaoException
	 */
	public int delete(String planid);

	/**
	 * 查询报表有没有被使用
	 * @param code
	 * @return
	 */
	public int countOtherTimeByReport(String code);

}
