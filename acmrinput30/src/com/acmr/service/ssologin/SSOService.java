package com.acmr.service.ssologin;

import java.util.HashMap;
import java.util.Map;

import acmr.util.WebConfig;
import acmr.util.returnData; 

import com.alibaba.fastjson.JSON;

public class SSOService {

	private HttpClient hpc;

	public SSOService() {
		hpc = new HttpClient();
	}

	public static SSOService getInstance() {
		return new SSOService();
	}

	public returnData checklogin(String aid) {
		String url1 =WebConfig.factor.getInstance().getPropertie("login.properties","ssologinurl")+"api/user/checklogin";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("aid", aid);
		return JSON.parseObject(hpc.getHttp(url1, maps), returnData.class);
	}

	public returnData getUserAid(String tid) {
		String url1 =WebConfig.factor.getInstance().getPropertie("login.properties","ssologinurl")+"api/user/getaid";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("tid", tid);
		return JSON.parseObject(hpc.getHttp(url1, maps), returnData.class);
	}

	public static void main(String[] args) {
		SSOService sso = new SSOService();
		returnData str1 = sso.getUserAid("b25eecb99d205713e189488c8857a9218e599042");
		System.out.print(str1);
	}
}
