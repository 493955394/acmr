package com.acmr.model.templatecenter;

import java.util.List;


public class searchTrace {
	private String success;
	private List<TreeNode> results;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public List<TreeNode> getResults() {
		return results;
	}
	public void setResults(List<TreeNode> results) {
		this.results = results;
	}
}
