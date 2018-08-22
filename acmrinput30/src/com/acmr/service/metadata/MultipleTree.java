package com.acmr.service.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 树形结构遍历
 * 
 * @author chenyf
 *
 */
public class MultipleTree {

	// 已遍历的行
	private HashMap<String, Node> ergNodeList = new HashMap<String, Node>();
	// 需要进行底库检查的procode
	private HashMap<String, String> needCheckProcode = new HashMap<String, String>();
	// 错误的行
	private List<String> errRow = new ArrayList<String>();

	public MultipleTree(List<Map<String, Object>> dataList) {
		// 节点列表（用于临时存储节点对象）
		HashMap<String, Node> nodeList = new HashMap<String, Node>();

		// 根节点
		List<Node> root = new ArrayList<Node>();

		// 根据结果集构造节点列表（存入散列表）
		for (Iterator<?> it = dataList.iterator(); it.hasNext();) {
			Map<?, ?> dataRecord = (Map<?, ?>) it.next();
			Node node = new Node();
			node.id = (String) dataRecord.get("code");
			node.text = (String) dataRecord.get("cname");
			node.parentId = (String) dataRecord.get("procode");
			node.orgCode = (String) dataRecord.get("orgCode");
			nodeList.put(node.id, node);
		}

		// 构造无序的多叉树
		Set<?> entrySet = nodeList.entrySet();
		for (Iterator<?> it = entrySet.iterator(); it.hasNext();) {
			@SuppressWarnings("rawtypes")
			Node node = (Node) ((Map.Entry) it.next()).getValue();
			if (node.parentId == null || node.parentId.length() == 0 || !nodeList.containsKey(node.parentId)) {
				root.add(node);
				if (node.parentId != null && node.parentId.length() != 0 && !nodeList.containsKey(node.parentId)) {
					// 表示可能是底库数据或者是根节点 （存下底库中的procode便于校验）
					needCheckProcode.put(node.orgCode, node.parentId);
				}
			} else {
				((Node) nodeList.get(node.parentId)).addChild(node);
			}
		}

		for (int i = 0; i < root.size(); i++) {
			Node node = root.get(i);
			buildTree(node);
			//System.out.println("根节点：" + node.id + "\t" + node.getChilds());
		}

		if (ergNodeList.size() != nodeList.size()) {
			//System.out.println("数据有错误！");
			// 构造无序的多叉树
			for (Iterator<?> it = entrySet.iterator(); it.hasNext();) {
				@SuppressWarnings("rawtypes")
				Node node = (Node) ((Map.Entry) it.next()).getValue();
				if (!ergNodeList.containsKey(node.id)) {
					errRow.add(node.orgCode);
				}
			}
			//System.out.println("错误的行：" + errRow);
		}

		// 输出无序的树形菜单的JSON字符串
		//System.out.println("需要在底库检查的code:" + needCheckProcode.toString());
	}

	private void buildTree(Node node) {
		if (node.isExc) {
			//System.out.println(node.id + "死循环了");
			return;
		}
		node.setExc(true);
		ergNodeList.put(node.id, node);
		List<Node> children = node.getChilds();
		if (children == null) {
			return;
		}
		for (Node child : children) {
			buildTree(child);
		}
	}

	public HashMap<String, Node> getErgNodeList() {
		return ergNodeList;
	}

	public void setErgNodeList(HashMap<String, Node> ergNodeList) {
		this.ergNodeList = ergNodeList;
	}

	public HashMap<String, String> getNeedCheckProcode() {
		return needCheckProcode;
	}

	public void setNeedCheckProcode(HashMap<String, String> needCheckProcode) {
		this.needCheckProcode = needCheckProcode;
	}

	public List<String> getErrRow() {
		return errRow;
	}

	public void setErrRow(List<String> errRow) {
		this.errRow = errRow;
	}

}
