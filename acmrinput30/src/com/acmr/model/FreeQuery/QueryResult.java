package com.acmr.model.FreeQuery;

import java.util.List;

public class QueryResult {

	private List<FilterItem> filter;
	private QueryTable table;
	private String explain;

	public List<FilterItem> getFilter() {
		return filter;
	}

	public void setFilter(List<FilterItem> filter) {
		this.filter = filter;
	}

	public QueryTable getTable() {
		return table;
	}

	public void setTable(QueryTable table) {
		this.table = table;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}
}
