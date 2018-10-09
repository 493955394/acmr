package com.acmr.service.zhzs;

import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.RightDao;
import com.acmr.model.security.User;
import com.acmr.service.security.DepartmentService;
import com.acmr.service.security.UserService;
import com.acmr.service.zbdata.UserDepService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RightControlService {
    /**
     * 通过indexcode查询right表中是否存在这个list,有的话返回这个list
     */
    public List<Map<String,String>> getRightList(String indexcode){
      User cu= UserService.getCurrentUser();
        String usercode = cu.getUserid();//获取当前用户的code
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        List<DataTableRow> datas = RightDao.Fator.getInstance().getIndexdatadao().getRightList(indexcode).getRows();
        for (int i = 0; i <datas.size() ; i++) {
            Map<String,String> arr = new HashMap<String,String>();
            //将他自己排除掉
           if(!(datas.get(i).getString("depusercode").equals(usercode)&&datas.get(i).getString("sort").equals("2"))){
                arr.put("indexcode",datas.get(i).getString("indexcode"));
                arr.put("depusercode",datas.get(i).getString("depusercode"));
                arr.put("sort",datas.get(i).getString("sort"));
                arr.put("right",datas.get(i).getString("right"));
                arr.put("createuser",datas.get(i).getString("createuser"));
                if(datas.get(i).getString("sort").equals("2")){
                    String depusername = UserDepService.getUserNameByCode(datas.get(i).getString("depusercode"));
                    arr.put("depusername",depusername);
                }
                else{
                    String depusername = DepartmentService.getDepartment(datas.get(i).getString("depusercode")).getCname();
                    arr.put("depusername",depusername);
                }
            }
            list.add(arr);
        }
        return list;
    }

    /**
     * 权限管理查询结果列表展示
     * @param keyword
     * @return
     */
    public static List<Map<String,String>> getSearchList(String keyword){
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        List<DataTableRow> depdatas = RightDao.Fator.getInstance().getIndexdatadao().searchDepName(keyword).getRows();
        for (int i = 0; i <depdatas.size() ; i++) {
            Map<String,String> arr = new HashMap<String,String>();
            arr.put("depusercode",depdatas.get(i).getString("code"));
            arr.put("depusername",depdatas.get(i).getString("cname"));
            arr.put("sort","1");
            list.add(arr);
        }
        List<DataTableRow> userdatas = RightDao.Fator.getInstance().getIndexdatadao().searchUserName(keyword).getRows();
        for (int i = 0; i <userdatas.size() ; i++) {
            Map<String,String> arr = new HashMap<String,String>();
            arr.put("depusercode",depdatas.get(i).getString("code"));
            arr.put("depusername",depdatas.get(i).getString("cname"));
            arr.put("sort","2");
            list.add(arr);
        }
        return list;
    }

    /**
     * 保存更改的权限管理，要注意createuser
     * @param icode
     * @param list
     * @return
     */
    public int  saveRightList(String icode,List<Map<String,String>> list){
        return RightDao.Fator.getInstance().getIndexdatadao().saveRightList(icode,list);
    }
}
