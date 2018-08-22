package com.acmr.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import acmr.cubequery.service.CubeQuerySev;
import acmr.cubequery.service.cubequery.entity.CubeNode;
import acmr.cubequery.service.cubequery.entity.CubeQueryData;
import acmr.cubequery.service.cubequery.entity.CubeUnit;
import acmr.cubequery.service.cubequery.entity.CubeWdCodes;
import acmr.cubequery.service.cubequery.entity.CubeWdValue;
import acmr.cubequery.service.cubequery.entity.CubeWeidu;
import acmr.util.PubInfo;

public class Test {

	public static void main(String args[]) {
		 //test_getwdlist();
		 //test_getwdsubnodes();
		 test_getwdnode();
		 test_gethasdatawdlist();
		 test_querydata();
		test_unitlist();
		PubInfo.printStr("OK");
	}

	public static void test_getwdlist() {
		CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
		ArrayList<CubeWeidu> wdlist = cube1.getWeiduList("cuscxnd");
		for (int i = 0; i < wdlist.size(); i++) {
			PubInfo.printStr(wdlist.get(i).getCode() + ":"
					+ wdlist.get(i).getName());
		}
	}

	public static void test_getwdsubnodes() {
		CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
		ArrayList<CubeNode> nodes = cube1.getWeiSubNodes("cuscxnd", "zb", "");
		for (int i = 0; i < nodes.size(); i++) {
			PubInfo.printStr(nodes.get(i).toString());
		}
	}

	public static void test_getwdnode() {
		CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
		CubeNode nodes = cube1.getWeiNode("cuscxnd", "reg", "643");
		PubInfo.printStr(nodes.toString());

	}

	public static void test_gethasdatawdlist() {
		CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
		List<CubeWdValue> list1 = new ArrayList<CubeWdValue>();
		list1.add(new CubeWdValue("zb",
				"ffe001d3f4a67c752233a83f900af86a942359f2"));
		list1.add(new CubeWdValue("co", "COG01"));
		List<String> nodes = cube1.getHasDataWdListreg("cuscxnd", list1, "reg");
		for (int i = 0; i < nodes.size(); i++) {
			PubInfo.printStr(nodes.get(i).toString());
		}
	}

	public static void test_querydata() {
		CubeQuerySev cube1 = CubeQuerySev.CCubeDaoFactor.getInstance();
		CubeWdCodes where = new CubeWdCodes();
		where.Add("zb", "ffe001d3f4a67c752233a83f900af86a942359f2");
		where.Add("ds", "A010100");
		where.Add("co", "COG01");
		where.Add(
				"reg",
				Arrays.asList(new String[] { "643", "642", "634", "626", "624",
						"608" }));
		where.Add(
				"sj",
				Arrays.asList(new String[] { "2011", "2012", "2013", "2014",
						"2015", "2016" }));
		ArrayList<CubeQueryData> result = cube1.getCubeData("cuscxnd", where);
		for (int i = 0; i < result.size(); i++) {
			PubInfo.printStr(result.get(i).toString());
		}

	}

	public static void test_unitlist() {
		List<CubeUnit> list1 = acmr.cubequery.service.CubeUnitManager.CubeUnitManagerFactor
				.getInstance("").getUnitZhuanhuanList("UGB002360");
		for (int i = 0; i < list1.size(); i++) {
			PubInfo.printStr(list1.get(i).getCode()+":"+ list1.get(i).getName());
		}

		double rate = acmr.cubequery.service.CubeUnitManager.CubeUnitManagerFactor

				.getInstance("").getUnitRate("UGB008830", "UGB002360","2017");
		PubInfo.printStr("" + rate);
	}
}
