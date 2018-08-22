package com.acmr.dao.sysindex;

import com.acmr.model.sysindex.FlowIndex;

/**
 * 
 * 类名称: FlowIndexDao 
 * 描述(对这个类进行描述): 工作流流程定义持久化层接口
 *
 * @author  anzhao
 *
 */
public interface IFlowIndexDao {
    
	/**
	 * 方法描述：插入流程定义相关数据
	 * @param flow 流程定义字符串,如：|@001>&@002^003>|^004@005    
	 * 	>:指向下一结点；|:该结点为多人审核单人完成；&该结点为多人审核多人完成；@：为人员编码；^：为角色编码
	 * @param	flowName	流程名称
	 * @param	memo	备注
	 * @return	流程编码
	 * @throws DaoException
	 */
	public String insertFlow(String flow, String flowName, String memo) ;
	/**
	 * 方法描述：通过流程编码获取流程
	 * @param code
	 * @return
	 */
	public FlowIndex getFlowIndexByCode(String code);
	
	public String insertFlowIndex(FlowIndex flowIndex) ;
	public int delFlowIndex(String code) ;
}
