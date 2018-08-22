package com.acmr.model.search;

import java.util.List;

public class searchList {

	
	private String success;
	private List<sItem> results;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public List<sItem> getResults() {
		return results;
	}
	public void setResults(List<sItem> results) {
		this.results = results;
	}
}
