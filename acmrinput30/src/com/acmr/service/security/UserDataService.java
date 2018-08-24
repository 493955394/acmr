package com.acmr.service.security;


import acmr.util.DataTable;
import acmr.util.ListHashMap;
import com.acmr.dao.security.SecurityDao;
import com.acmr.model.pub.ZTreeNode;
import com.acmr.model.security.Department;
import com.acmr.model.security.User;


import java.util.List;


public class UserDataService {
    /**
     * 组织
     * @return
     */

    ListHashMap<Department> deps = SecurityService.fator.getInstance().getDeps();
    public ListHashMap<Department> getDeps(){
        return deps;
    }
    //查询单个组织信息
    public static Department getDepartment(String code) {
        UserDataService user = new UserDataService();
        ListHashMap<Department> deps = user.getDeps();
        if (deps.containsKey(code)) {
            return deps.get(code);
        }
        return null;
    }

    //通过组织code得到name
    public static String getNameByCode(String code) {
        UserDataService user = new UserDataService();
        ListHashMap<Department> deps = user.getDeps();
        Department dep = deps.get(code);
        if(dep!=null){
            return dep.getCname();
        }else{
            return "";
        }
    }
    //通过组织name得到code
    public static String getCodeByName(String cname) {
        UserDataService user = new UserDataService();
        ListHashMap<Department> deps = user.getDeps();
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
        String depcode;
        List<User> users = SecurityService.fator.getInstance().getDepUsers(depcode);
        public List<User> getDepUsers (String depcode) {
            return users;
        }
        // 通过用户名称得到用户信息
        String name;
        User UserInfo = SecurityService.fator.getInstance().getUserInfo(name);
        public User getUserInfo(String name) {
            return UserInfo;
        }
        /*public static getUserId getUserId(String name){
            DataTable dt1 = SecurityDao.Fator.getInstance().getSecurityDao().getUser(name);
            return dt1.getRows().get(0);
        }*/
        //User username = SecurityService.fator.getInstance().getUser(name);
        //获取创建人
        User Currentuser = SecurityService.fator.getInstance().getCurrentUser();
        public User getUser(){
            return Currentuser;
    }

}
