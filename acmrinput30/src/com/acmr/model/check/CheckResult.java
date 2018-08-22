package com.acmr.model.check;

import java.util.ArrayList;
import java.util.List;

public class CheckResult {

	public CheckResult(){
		checklist=new ArrayList<CheckItem>();
	}
	
	private String sort;
	
	private List<CheckItem> checklist;

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public List<CheckItem> getChecklist() {
		return checklist;
	}

    public void additem(CheckItem e){
    	checklist.add(e);
    }
	
	
}
