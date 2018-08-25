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
     *
     *
     * @author wf
     * @date   2018/8/25
     * @param []
     * @return
     */
    UserDataService userdata = new UserDataService();

    public ListHashMap<Department> getDeps(){
        ListHashMap<Department> deps = SecurityService.fator.getInstance().getDeps();
        return deps;
    }
    //查询单个组织信息
    public  Department getDepartment(String code) {
        ListHashMap<Department> deps = userdata.getDeps();
        if (deps.containsKey(code)) {
            return deps.get(code);
        }
        return null;
    }

    //通过组织code得到name
    public String getNameByCode(String code) {
        ListHashMap<Department> deps = userdata.getDeps();
        Department dep = deps.get(code);
        if(dep!=null){
            return dep.getCname();
        }else{
            return "";
        }
    }
    //通过组织name得到code
    public String getCodeByName(String cname) {
        ListHashMap<Department> deps = userdata.getDeps();
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
        public List<User> getDepUsers (String depcode) {
            List<User> users = SecurityService.fator.getInstance().getDepUsers(depcode);
            return users;
        }
        // 通过用户名称得到用户信息
        public User getUserInfo(String name) {
            User UserInfo = SecurityService.fator.getInstance().getUserInfo(name);
            return UserInfo;
        }
        /*public Cname getUserId(String name){
            DataTable dt1 = SecurityDao.Fator.getInstance().getSecurityDao().getUser(name);
            return dt1.getRows().get(0);
        }*/

        //获取创建人

        public User getUser(){
            User Currentuser = SecurityService.fator.getInstance().getCurrentUser();
            return Currentuser;
    }

}
