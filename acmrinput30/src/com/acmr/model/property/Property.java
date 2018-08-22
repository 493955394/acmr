package com.acmr.model.property;

import java.util.ArrayList;
import java.util.List;

import acmr.cubeinput.service.cubeinput.entity.CubeWeidu;
import acmr.util.ListHashMap;

/**
 * property对象
 * 
 * @author : chenyf
 */
public class Property {
	
   private ListHashMap<ProBean> list;
	
	public ListHashMap<ProBean> getList() {
	return list;
}

public void setList(ListHashMap<ProBean> list) {
	this.list = list;
}

	public Property(ListHashMap<CubeWeidu> wdlist) {
		list=new ListHashMap<ProBean>();
		for(int i=0;i<wdlist.size();i++){
			ProBean pro=new ProBean();
			pro.setCode(wdlist.get(i).getCode());
			pro.setList(new ArrayList<Prop>());
			list.add(pro);
		}
	}

	public List<Prop> getList(String type){
		
		return list.get(type).getList();
	}
}
