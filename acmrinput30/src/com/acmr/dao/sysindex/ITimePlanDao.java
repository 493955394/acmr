package com.acmr.dao.sysindex;

import com.acmr.model.sysindex.TimePlan;

public interface ITimePlanDao {
	/**
	 * 新增时间规划
	 * @param SystemPlan 制度计划
	 * @return 成功条数
	 */
	public String insert(TimePlan timePlan);
	/**
	 * 根据id查询制度计划
	 * @param id
	 * @return
	 */
	public TimePlan queryPlanById(String id);
	
	/**
	 * 根据planid删除
	 * @param planid
	 * @return
	 * @throws DaoException
	 */
	public int delete(String planid);

}
