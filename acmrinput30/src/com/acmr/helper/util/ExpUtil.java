package com.acmr.helper.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.formula.functions.T;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 从map中获取对象公用类	
 * 
 * @author caosl
 */
public class ExpUtil {
	/**
	 * 获取map中的指定属性
	 * 
	 * @param ExpMap
	 *            属性map
	 * @param key
	 *            属性key
	 * @return key对应的map value值
	 * @throws ClassNotFoundException
	 */

	public static Object getMap(Class Clazz, String key, Map<String, String> map) {
		String s = map.get(key);
		//Object o = JSON.parseObject(s, Clazz);
		return JSON.parseObject(s, Clazz);
	}
	public static Map<String,String> setMapWithArrayList(Map<String, List> map) {
		Set<String> keySet = map.keySet();
		Map<String,String> tempMap = new HashMap<String,String>();
		for(String key : keySet){
			tempMap.put(key, JSONArray.toJSONString(map.get(key)));
		}
		return tempMap;
	}
	public static List getMapWithArrayList(Class Clazz, String key, Map<String, String> map) {
		List newPList1 = JSONArray.parseObject(map.get(key), List.class);
		List<Object> newList = new ArrayList<Object>();
		if(newPList1!=null&&!newPList1.isEmpty()){
			for(int i = 0;i<newPList1.size();i++){
				JSONObject jobj = (JSONObject)newPList1.get(i);
				Object newObj = JSONObject.toJavaObject(jobj, Clazz);
				newList.add(newObj);
			}
		}
		return newList;
	}
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
//	public enum testEnum{
//		test1,test2
//	}
//	public static void main(String[] args) {
//		TestP p1 = new TestP();
//		p1.setS1("s1");
//		p1.setS2("s2");
//		TestP p2 = new TestP();
//		p2.setS1("s1");
//		p2.setS2("s2");
//		List<TestP> pList = new ArrayList<TestP>();
//		pList.add(p1);
//		pList.add(p2);
//		Map<String,List> pMap = new HashMap<String,List>();
//		pMap.put("p1", pList);
//		Map<String,String> newPMap = setMapWithArrayList(pMap);
//		String s = JSON.toJSONString(newPMap);
//		//System.out.println(s);
//		List<TestP> l = (List<TestP>)getMapWithArrayList(TestP.class, "p1", newPMap);
//		//System.out.println(l.get(0).getS1());
//	}
	
	
	
	
	
	
	
	
	
	
}
