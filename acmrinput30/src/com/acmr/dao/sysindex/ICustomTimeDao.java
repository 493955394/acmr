package com.acmr.dao.sysindex;

import java.util.List;

import com.acmr.model.sysindex.CustomTime;

public interface ICustomTimeDao {
	/**
	 * 新增时间规划
	 * @param SystemPlan 制度计划
	 * @return 成功条数
	 */
	public int insert(CustomTime customTime);
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public List<CustomTime> getTimeById(String id);
	
	/**
	 * 根据planid删除
	 * @param planid
	 * @return
	 * @throws DaoException
	 */
	public int delete(String planid);
}
