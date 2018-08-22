package com.acmr.dao.dataright;

import java.util.List;

import com.acmr.model.dataright.DataGroup;
import com.acmr.model.dataright.DataRight;
import com.acmr.model.dataright.RuleItem;
import com.acmr.model.security.User;

public interface IDataRightDao {

	public String addGroup(DataGroup dataGroup) ;
	public int updateGroup(DataGroup dataGroup);
	public int delGroup(String code);
	public boolean checkCanDel(String code);
	 public List<DataGroup> getGroupList();
	 public List<DataGroup> getGroupByName(String cname);
	public String addGroupRight(DataRight dataRight);
	public int updateGroupRight(DataRight dataRight);
	public int delGroupRight(String code);
	public String addRightRule(RuleItem ruleItem);
	public int updateRightRule(RuleItem ruleItem);
	public int delRightRule(String code);
	public int delRules(String rightcode);
	public List<RuleItem> getRuleList(User user);
	public List<RuleItem> getRuleByCode(String rightcode);
	public List<DataRight> getRightList();
	public List<DataRight> getRightList(String code);
	public List<DataRight> getRightList(String groupcode,String cname);
}
