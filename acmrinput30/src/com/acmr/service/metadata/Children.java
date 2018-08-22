package com.acmr.service.metadata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 元数据批量导入-树结构实体对象
 * 
 * @author chenyf
 *
 */
public class Children {

	private List<Node> list = new ArrayList<Node>();

	public int getSize() {
		return list.size();
	}

	public void addChild(Node node) {
		list.add(node);
	}

	public List<Node> getChilds() {
		return list;
	}

	// 拼接孩子节点的JSON字符串
	public String toString() {
		String result = "[";
		for (Iterator<Node> it = list.iterator(); it.hasNext();) {
			result += ((Node) it.next()).toString();
			result += ",";
		}
		result = result.substring(0, result.length() - 1);
		result += "]";
		return result;
	}
}
