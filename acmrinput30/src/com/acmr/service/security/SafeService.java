package com.acmr.service.security;

import java.io.IOException;

import acmr.util.ListHashMap;
import acmr.util.WebConfig;

import com.acmr.model.security.Menu;
import com.acmr.model.security.Right;
import com.acmr.model.security.User;

public class SafeService {

	public static int checkUserLogin(String name, String pwd) {
		return SecurityService.fator.getInstance().UserLogin(name, pwd);
	}

 
	public static boolean checkUserRight(String rights) throws IOException {
		 if(WebConfig.factor.getInstance().getPropertie("login.properties", "ssologin").equalsIgnoreCase("yes")){
			 return SecurityService.fator.getInstance().ssocheckUserRight(rights);
		 }
		return SecurityService.fator.getInstance().checkUserRight(rights);
	}

	public static int checkUserRightMethod(String rights) throws IOException {
		 if(WebConfig.factor.getInstance().getPropertie("login.properties", "ssologin").equalsIgnoreCase("yes")){
			 return SecurityService.fator.getInstance().ssocheckUserRightMethod(rights);
		 }
		return SecurityService.fator.getInstance().checkUserRightMethod(rights);
	}
	
	public static ListHashMap<Right> getUserRight(User user) {
		return SecurityService.fator.getInstance().getUserRight(user);
	}

	public static ListHashMap<Menu> getUserMenu(User user) {
		return SecurityService.fator.getInstance().getUserMenu(user);

	}

	public static ListHashMap<Menu> getUserMenu(ListHashMap<Right> rights) {
		return SecurityService.fator.getInstance().getUserMenu(rights);

	}

}
