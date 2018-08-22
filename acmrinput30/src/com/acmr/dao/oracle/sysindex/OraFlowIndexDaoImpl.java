package com.acmr.dao.oracle.sysindex;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import acmr.data.DataQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.sysindex.IFlowIndexDao;
import com.acmr.helper.constants.DataAuditConsts;
import com.acmr.model.sysindex.FlowIndex;
import com.acmr.model.sysindex.FlowNode;
import com.acmr.model.sysindex.FlowPerson;

public class OraFlowIndexDaoImpl implements IFlowIndexDao {

	@Override
	public String insertFlow(String flow, String flowName, String memo) {
		FlowIndex flowIndex = new FlowIndex();
		// 构造并插入流程定义数据
		flowIndex.setCname(flowName);
		flowIndex.setFlowdata(flow);
		flowIndex.setMemo("");
		flowIndex.setVersion(0);
		String flowcode = this.insertFlowIndex(flowIndex);

		// 构造并插入流程结点数据
		String[] nodes = flow.split(">");
		for (int j = 0; j < nodes.length; j++) {
			String node = nodes[j];
			FlowNode flowNode = new FlowNode();
			String gx = String.valueOf(node.charAt(0));
			String personTmps = node.substring(1, node.length());
			char currtype = personTmps.charAt(0);
			if (currtype == '^') {// 如果设置的为角色，则之间为或的关系
				gx = "|";
			}
			flowNode.setFlowcode(flowcode);
			flowNode.setType(gx);
			flowNode.setNodedata(personTmps);
			flowNode.setSort(String.valueOf(j + 1));
			flowNode.setVersion("0");
			String nodeid = this.insertFlowNode(flowNode);

			// 构造并插入结点下的审核人列表
			List<String> persons1 = new ArrayList<String>();
			List<String> persons2 = new ArrayList<String>();
			// 解析人员
			int begin = 1;
			int end = 1;

			for (int i = 1; i < personTmps.length(); i++) {
				char t = personTmps.charAt(i);
				String personcode;
				if (t == '@' || t == '^') {
					end = i;
					personcode = personTmps.substring(begin, end);
					begin = i + 1;
					end = i + 1;

					if (currtype == '@') {
						persons1.add(personcode);
					} else {
						persons2.add(personcode);
					}
					currtype = t;
				}
				if (i == personTmps.length() - 1) {
					// 搜到最后一个字符了
					end = personTmps.length();
					personcode = personTmps.substring(begin, end);
					begin = 1;
					end = 1;

					if (currtype == '@') {
						// 当前类型为人员
						persons1.add(personcode);
					} else {
						// 当前类型为角色
						persons2.add(personcode);
					}
				}
			}
			// 插入人员
			for (String person : persons1) {
				FlowPerson flowPerson = new FlowPerson();
				flowPerson.setNodeid(nodeid);
				flowPerson.setProleid(person);
				flowPerson.setType(DataAuditConsts.FLOW_NODE_PERSON_TYPEE_00);
				flowPerson.setVersion(0);
				this.insertFlowPerson(flowPerson);
			}
			for (String person : persons2) {
				FlowPerson flowPerson = new FlowPerson();
				flowPerson.setNodeid(nodeid);
				flowPerson.setProleid(person);
				flowPerson.setType(DataAuditConsts.FLOW_NODE_PERSON_TYPEE_01);
				flowPerson.setVersion(0);
				this.insertFlowPerson(flowPerson);
			}
		}

		return flowcode;
	}

	@Override
	public FlowIndex getFlowIndexByCode(String code) {
		String sql = "select flowcode,cname,flowdata from tb_flow_index where flowcode = ?";
		List<Object> obj = new ArrayList<Object>();
		obj.add(code);
		DataTable dataTable = AcmrInputDPFactor.getQuickQuery().getDataTableSql(sql, obj.toArray());
		if (dataTable == null || dataTable.getRows().size() == 0) {
			return null;
		}
		List<DataTableRow> dataTableRows = dataTable.getRows();
		DataTableRow row = dataTableRows.get(0);
		FlowIndex flowIndex = new FlowIndex();
		flowIndex.setFlowcode(row.getString("flowcode"));
		flowIndex.setCname(row.getString("cname"));
		flowIndex.setFlowdata(row.getString("flowdata"));
		return flowIndex;
	}

	@Override
	public String insertFlowIndex(FlowIndex flowIndex) {
		StringBuffer sql = new StringBuffer();
		sql.append("insert into tb_flow_index ");
		sql.append("(flowcode,cname,flowdata,createtime,memo,version)");
		sql.append("values( sq_flow_index.nextval,?,?,sysdate,?,?)");
		Object[] prams = new Object[4];
		prams[0] = flowIndex.getCname();
		prams[1] = flowIndex.getFlowdata();
		prams[2] = flowIndex.getMemo();
		prams[3] = flowIndex.getVersion();
		String result = "";
		DataQuery dataQuery = null;
		try {
			dataQuery = AcmrInputDPFactor.getDataQuery();
			dataQuery.beginTranse();
			result = dataQuery.insertRtSeq(sql.toString(), prams, "flowcode");
			dataQuery.commit();
		} catch (SQLException e) {
			if (null != dataQuery) {
				dataQuery.rollback();
			}
			e.printStackTrace();
		} finally {
			if (null != dataQuery) {
				dataQuery.releaseConnl();
			}
		}
		return result;
	}

	@Override
	public int delFlowIndex(String code) {
		StringBuffer sql = new StringBuffer();
		sql.append("delete from tb_flow_index where flowcode=?");
		Object[] prams = new Object[1];
		prams[0] = code;

		DataQuery dataQuery = null;
		int result = 0;
		try {
			dataQuery = AcmrInputDPFactor.getDataQuery();
			dataQuery.beginTranse();
			result = dataQuery.executeSql(sql.toString(), prams);
			dataQuery.commit();
		} catch (SQLException e) {
			if (null != dataQuery) {
				dataQuery.rollback();
			}
			e.printStackTrace();
		} finally {
			if (null != dataQuery) {
				dataQuery.releaseConnl();
			}
		}
		return result;
	}

	/**
	 * 方法说明：插入流程节点
	 *
	 * @param nodeid
	 *            结点Id
	 * @param flowcode
	 *            流程Id 以上传过来的值
	 * @param nodedata
	 *            结点内容 存的是@001&^002、@001&@001、^002@^003数组
	 * @param sort
	 *            排序吗 按照截取的顺序进行排放
	 * @param type
	 *            审核类型
	 */
	private String insertFlowNode(FlowNode flowNode) {
		StringBuffer sql = new StringBuffer();
		sql.append("insert into tb_flow_node(nodeid,flowcode,nodedata,sort,type,version)");
		sql.append(" values(sq_flow_node.nextval,?,?,?,?,?)");

		Object[] prams = new Object[5];
		prams[0] = flowNode.getFlowcode();
		prams[1] = flowNode.getNodedata();
		prams[2] = flowNode.getSort();
		prams[3] = flowNode.getType();
		prams[4] = flowNode.getVersion();
		String result = null;
		DataQuery dataQuery = null;
		try {
			dataQuery = AcmrInputDPFactor.getDataQuery();
			dataQuery.beginTranse();
			result = dataQuery.insertRtSeq(sql.toString(), prams, "nodeid");
			dataQuery.commit();
		} catch (SQLException e) {
			if (null != dataQuery) {
				dataQuery.rollback();
			}
			e.printStackTrace();
		} finally {
			if (null != dataQuery) {
				dataQuery.releaseConnl();
			}
		}
		return result;
	}

	/**
	 * 方法描述：插入流程定义审批人员
	 * 
	 * @param flowperson
	 * @param id
	 *            业务Id
	 * @param nodeid
	 *            流程结点结点Id
	 * @param type
	 *            流程人员类型 @或者^代表人还是角色
	 * @param proleid
	 *            审核人Id @或者^以后的值--001等
	 * 
	 */
	private String insertFlowPerson(FlowPerson flowPerson) {
		StringBuffer sql = new StringBuffer();
		sql.append("insert into tb_flow_person (id,nodeid,type,proleid,version)");
		sql.append(" values(sq_flow_person.nextval,?,?,?,?)");
		Object[] prams = new Object[4];
		prams[0] = flowPerson.getNodeid();
		prams[1] = flowPerson.getType();
		prams[2] = flowPerson.getProleid();
		prams[3] = flowPerson.getVersion();
		String result = null;
		DataQuery dataQuery = null;
		try {
			dataQuery = AcmrInputDPFactor.getDataQuery();
			dataQuery.beginTranse();
			result = dataQuery.insertRtSeq(sql.toString(), prams, "id");
			dataQuery.commit();
		} catch (SQLException e) {
			if (null != dataQuery) {
				dataQuery.rollback();
			}
			e.printStackTrace();
		} finally {
			if (null != dataQuery) {
				dataQuery.releaseConnl();
			}
		}
		return result;
	}
}
