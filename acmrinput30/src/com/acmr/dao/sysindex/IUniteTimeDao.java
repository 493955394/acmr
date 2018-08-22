package com.acmr.dao.sysindex;

import com.acmr.model.sysindex.UniteTime;

public interface IUniteTimeDao {
	/**
	 * 新增时间规划
	 * 
	 * @param SystemPlan
	 *            制度计划
	 * @return 成功条数
	 */
	public int insert(UniteTime unitetime);

	/**
	 * 根据id获取统一时间
	 * 
	 * @param id
	 * @return
	 */
	public UniteTime getUniteById(String id);

	/**
	 * 根据planid删除
	 * 
	 * @param planid
	 * @return
	 * @throws DaoException
	 */
	public int delete(String planid);
}
