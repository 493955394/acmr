package com.acmr.service.zbdata;



import java.util.ArrayList;
import java.util.List;

import acmr.util.ListHashMap;

import com.acmr.model.pub.ZTreeNode;
import com.acmr.model.security.Department;
import com.acmr.model.security.User;
import com.acmr.service.security.SecurityService;


public class UserDataService  {
    /**
     * 组织
     * @return
     */
    //得到父级组织

    public static List<ZTreeNode> getSubDepartments(String pcode) {

        return SecurityService.fator.getInstance().getsubDepartMent(pcode);
    }
    //查询单个组织信息（code为组织code）
    public static Department getDepartment(String code) {
        ListHashMap<Department> lists = SecurityService.fator.getInstance().getDeps();
        if (lists.containsKey(code)) {
            return lists.get(code);
        }
        return null;
    }

    //通过code得到name
    public static String getNameByCode(String code) {
        ListHashMap<Department> deps = SecurityService.fator.getInstance().getDeps();
        Department dep = deps.get(code);
        if(dep!=null){
            return dep.getCname();
        }else{
            return "";
        }
    }
    //通过name得到code
    public static String getCodeByName(String cname) {
        ListHashMap<Department> deps = SecurityService.fator.getInstance().getDeps();
        Department dep = deps.get(cname);
        if(dep!=null){
            return dep.getCode();
        }else{
            return "";
        }
    }
    /**
     * 用户
     * @return
     */
    //通过组织的code得到下边所有的用户
    public static List<User> getDepUsers(String depcode) {
        return SecurityService.fator.getInstance().getDepUsers(depcode);
    }

    public static User getUserInfo(String name) {
        return SecurityService.fator.getInstance().getUserInfo(name);
    }
    public static User getCurrentUser() {
        return SecurityService.fator.getInstance().getCurrentUser();
    }

}
