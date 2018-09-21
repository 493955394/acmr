package com.acmr.service.zbdata;

import acmr.util.DataTable;
import acmr.util.PubInfo;
import com.acmr.dao.security.SecurityDao;
import com.acmr.model.security.Department;
import com.acmr.model.security.User;
import com.acmr.service.security.DepartmentService;

import java.util.ArrayList;
import java.util.List;

public class UserDepService {

    /**
    * @Description: 根据usercode返回user的name
    * @Param: [usercode]
    * @return: java.lang.String
    * @Author: lyh
    * @Date: 2018/9/20
    */
    public static String getUserNameByCode(String usercode){
        DataTable table= SecurityDao.Fator.getInstance().getSecurityDao().getUser(usercode);
        String username=table.getRows().get(0).getString("cname");
        return username;
    }

    /**
    * @Description: 返回用户的部门list（按层级从低到高）
    * @Param: [usercode]
    * @return: java.util.List<com.acmr.model.security.Department>
    * @Author: lyh
    * @Date: 2018/9/21
    */
    public static List<Department> getDepPath(String usercode){
        List<Department> deps=new ArrayList<>();
        //该用户的直系depcode
        String depcode=SecurityDao.Fator.getInstance().getSecurityDao().getUser(usercode).getRows().get(0).getString("depcode");
        Department dep= DepartmentService.getDepartment(depcode);
        deps.add(dep);
        deps.addAll(getDeps(dep));
        return deps;
    }

    /**
    * @Description: 返回指定dep的上层depcodes列表
    * @Param: [depcode]
    * @return: java.util.List<java.lang.String>
    * @Author: lyh
    * @Date: 2018/9/21
    */
    public static List<Department> getDeps(Department dep){
        List<Department> deps=new ArrayList<>();
        String pcode=dep.getParent();
        /*if (pcode==""){
           // deps.add(dep);
        }*/
        if (pcode!=""){
            Department pdep=DepartmentService.getDepartment(pcode);
            deps.add(pdep);
            deps.addAll(getDeps(pdep));
        }
        return deps;
    }

    public static void main(String[] args) {
        List<Department> deps=getDepPath("usercode06");
        for (int i=0;i<deps.size();i++){
            PubInfo.printStr(deps.get(i).getCname());
        }

    }



}
