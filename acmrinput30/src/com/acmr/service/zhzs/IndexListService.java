package com.acmr.service.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.IndexEditDao;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.model.security.Department;
import com.acmr.model.security.User;
import com.acmr.model.zhzs.IndexList;
import com.acmr.model.zhzs.IndexMoudle;
import com.acmr.model.zhzs.IndexTask;
import com.acmr.service.security.DepartmentService;
import com.acmr.service.security.UserService;
import com.acmr.service.zbdata.UserDepService;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.text.ParseException;
import java.util.*;

public class IndexListService {
    String name = IndexListDao.Fator.getInstance().getIndexdatadao().getName();
    //通过usercode得到计划和目录列表

    public ArrayList<IndexList> getSublist(String code){
        User cu= UserService.getCurrentUser();
        //PubInfo.printStr("userid:"+cu.getUserid());
        String usercode=cu.getUserid();
        ArrayList<IndexList> indexLists=new ArrayList<>();
        List<DataTableRow> indexlist = IndexListDao.Fator.getInstance().getIndexdatadao().getSubLists(code,usercode).getRows();
        //我创建的指数
        if (code.equals("!1")){
            indexlist = IndexListDao.Fator.getInstance().getIndexdatadao().getSubLists("",usercode).getRows();
        }
        /*for (int i=0;i<indexlist.size();i++){
            PubInfo.printStr(indexlist.get(i).getRows().toString());
        }*/
        for(int i=0;i<indexlist.size();i++){
            IndexList index=new IndexList();
            index.setCode(indexlist.get(i).get("code").toString());
            index.setCname(indexlist.get(i).get("cname").toString());
            index.setCreateuser(indexlist.get(i).get("createuser").toString());
            index.setIfdata(indexlist.get(i).get("ifdata").toString());
            if (indexlist.get(i).get("state")!=null){
                index.setState(indexlist.get(i).get("state").toString());
            }
            index.setCreatetime(indexlist.get(i).getString("createtime"));
            if(indexlist.get(i).get("plantime")!=null){
                index.setPlantime(indexlist.get(i).getString("plantime"));
            }
            if(indexlist.get(i).get("updatetime")!=null){
                index.setUpdatetime(indexlist.get(i).getString("updatetime"));
            }
            if(indexlist.get(i).get("delayday")!=null){
                index.setDelayday(indexlist.get(i).get("delayday").toString());
            }
            if(indexlist.get(i).get("planperiod")!=null){
                index.setPlanperiod(indexlist.get(i).get("planperiod").toString());
            }
            if(indexlist.get(i).get("procode")!=null){
                index.setProcode(indexlist.get(i).get("procode").toString());
            }
            if(indexlist.get(i).get("sort")!=null){
                index.setSort(indexlist.get(i).get("sort").toString());
            }
            if(indexlist.get(i).get("startperiod")!=null){
                index.setStartperiod(indexlist.get(i).get("startperiod").toString());
            }
            //PubInfo.printStr("indexcode:"+index.getCode());
            indexLists.add(index);
        }

        //我共享的指数
        if (code.equals("!3")){
            PubInfo.printStr("我共享的");
            List<IndexList> list=new ArrayList<>();
            List<DataTableRow> rightrows= IndexListDao.Fator.getInstance().getIndexdatadao().getRightListByCreateUser(usercode).getRows();
            List<String> codes=new ArrayList<>();
            for (int i=0;i<rightrows.size();i++){
                String thiscode=rightrows.get(i).getString("indexcode");
                codes.add(thiscode);
            }
            for (int j=0;j<codes.size();j++){
                DataTableRow row=IndexListDao.Fator.getInstance().getIndexdatadao().getByCode(codes.get(j)).getRows().get(0);
                String icode=row.getString("code");
                String cname=row.getString("cname");
                String procode=row.getString("procode");
                String sort=row.getString("sort");
                String startperiod=row.getString("startperiod");
                String delayday=row.getString("delayday");
                String planperiod=row.getString("planperiod");
                String plantime=row.getString("plantime");
                String createuser=row.getString("createuser");
                String ifdata=row.getString("ifdata");
                String state=row.getString("state");
                IndexList indexList=new IndexList(icode,cname,procode,sort,startperiod,delayday,planperiod,plantime,createuser,ifdata,state);
                list.add(indexList);
            }
            indexLists= (ArrayList<IndexList>) list;
        }
        return indexLists;
    }

    public List<IndexList> getSubCate(String pcode){
        User cu=UserService.getCurrentUser();
        //PubInfo.printStr("userid:"+cu.getUserid());
        String usercode=cu.getUserid();

        List<IndexList> indexLists=new ArrayList<>();
        List<DataTableRow> rows = IndexListDao.Fator.getInstance().getIndexdatadao().getSubLists(pcode,usercode).getRows();
        if (pcode.equals("!1")){
            rows = IndexListDao.Fator.getInstance().getIndexdatadao().getSubLists("",usercode).getRows();
        }
        for (int i=0;i<rows.size();i++){
            //只取目录，不取计划
            if (rows.get(i).getString("ifdata").equals("0")){
                String code=rows.get(i).getString("code");
                String cname=rows.get(i).getString("cname");
                String procode=rows.get(i).getString("procode");
                String sort=rows.get(i).getString("sort");
                String startperiod=rows.get(i).getString("startperiod");
                String delaydat=rows.get(i).getString("delayday");
                String planperiod=rows.get(i).getString("planperiod");
                String plantime=rows.get(i).getString("plantime");
                String createuser=rows.get(i).getString("createuser");
                String ifdata=rows.get(i).getString("ifdata");
                String state=rows.get(i).getString("state");
                IndexList indexList=new IndexList(code,cname,procode,sort,startperiod,delaydat,planperiod,plantime,createuser,ifdata,state);
                indexLists.add(indexList);
            }
        }
        return indexLists;
    }

    /**
    * @Description: 传usercode返回该用户创建的计划list
    * @Param: [] usercode先写死，后面改
    * @return: java.util.ArrayList<com.acmr.model.zhzs.IndexList>
    * @Author: lyh
    * @Date: 2018/8/28
    */
    public ArrayList<IndexList> getIndexList(){
        User cu=UserService.getCurrentUser();
        String usercode=cu.getUserid();

        List<DataTableRow> indexlist = IndexListDao.Fator.getInstance().getIndexdatadao().getByUser(usercode).getRows();
        ArrayList<IndexList> indexLists=new ArrayList<>();
        for(int i=0;i<indexlist.size();i++){
            IndexList index=new IndexList();
            index.setCode(indexlist.get(i).get("code").toString());
            index.setCname(indexlist.get(i).get("cname").toString());
            index.setCreateuser(indexlist.get(i).get("createuser").toString());
            index.setIfdata(indexlist.get(i).get("ifdata").toString());
            if (indexlist.get(i).get("state")!=null){
                index.setState(indexlist.get(i).get("state").toString());
            }
            index.setCreatetime(indexlist.get(i).getString("createtime"));
            if(indexlist.get(i).get("plantime")!=null){
                index.setPlantime(indexlist.get(i).getString("plantime"));
            }
            if(indexlist.get(i).get("updatetime")!=null){
                index.setUpdatetime( indexlist.get(i).getString("updatetime"));
            }
            if(indexlist.get(i).get("delayday")!=null){
                index.setDelayday(indexlist.get(i).get("delayday").toString());
            }
            if(indexlist.get(i).get("planperiod")!=null){
                index.setPlanperiod(indexlist.get(i).get("planperiod").toString());
            }
            if(indexlist.get(i).get("procode")!=null){
                index.setProcode(indexlist.get(i).get("procode").toString());
            }
            if(indexlist.get(i).get("sort")!=null){
                index.setSort(indexlist.get(i).get("sort").toString());
            }
            if(indexlist.get(i).get("startperiod")!=null){
                index.setStartperiod(indexlist.get(i).get("startperiod").toString());
            }
            indexLists.add(index);
        }
        return indexLists;
    }

    /**
     * 通过Code查询
     * @param code
     * @return
     */
    public IndexList getData(String code){

        List<DataTableRow> data = IndexListDao.Fator.getInstance().getIndexdatadao().getByCode(code).getRows();
        IndexList index= new IndexList();
        if(data.size()>0){
        index.setCode(data.get(0).getString("code"));
        index.setCname(data.get(0).getString("cname"));
        index.setCreateuser(data.get(0).getString("createuser"));
        index.setIfdata(data.get(0).getString("ifdata"));
        index.setState(data.get(0).getString("state"));
        index.setCreatetime(data.get(0).getString("createtime"));
        index.setPlantime( data.get(0).getString("plantime"));
        index.setUpdatetime( data.get(0).getString("updatetime"));
        index.setDelayday(data.get(0).getString("delayday"));
        index.setPlanperiod(data.get(0).getString("planperiod"));
        index.setProcode(data.get(0).getString("procode"));
        index.setSort(data.get(0).getString("sort"));
        index.setStartperiod(data.get(0).getString("startperiod"));}
        return index;
    }

    //查找功能
    public ArrayList<IndexList> found(int type,String code,String userid){
        ArrayList<IndexList> indexLists=new ArrayList<>();
        List<DataTableRow> data = new ArrayList<>();
       if(type==0){//0表示是通过code查的
            data = IndexListDao.Fator.getInstance().getIndexdatadao().getLikeCode(code,userid).getRows();

       }else if(type==1){//1表示是通过cname查的
           data = IndexListDao.Fator.getInstance().getIndexdatadao().getLikeCname(code,userid).getRows();
       }
        for(int i=0;i<data.size();i++){
            IndexList index= new IndexList();
            index.setCode(data.get(i).getString("code"));
            index.setCname(data.get(i).getString("cname"));
            index.setCreateuser(data.get(i).getString("createuser"));
            index.setIfdata(data.get(i).getString("ifdata"));
            index.setState(data.get(i).getString("state"));
            index.setCreatetime(data.get(i).getString("createtime"));
            index.setPlantime( data.get(i).getString("plantime"));
            index.setUpdatetime( data.get(i).getString("updatetime"));
            index.setDelayday(data.get(i).getString("delayday"));
            index.setPlanperiod(data.get(i).getString("planperiod"));
            index.setProcode(data.get(i).getString("procode"));
            index.setSort(data.get(i).getString("sort"));
            index.setStartperiod(data.get(i).getString("startperiod"));
            indexLists.add(index);
        }
        return indexLists;
    }

    /**
     * 查询的分页
     */
    public  List<IndexList> found(int type,String code,String userid,int page,int pagesize){
        List<IndexList> indexLists=new ArrayList<>();
        List<DataTableRow> data = new ArrayList<>();
        if(type==0){//0表示是通过code查的
            data = IndexListDao.Fator.getInstance().getIndexdatadao().getLikeCodeByPage(code,userid,page,pagesize).getRows();

        }else if(type==1){//1表示是通过cname查的
            data = IndexListDao.Fator.getInstance().getIndexdatadao().getLikeCnameByPage(code,userid,page,pagesize).getRows();
        }
        for(int i=0;i<data.size();i++){
            IndexList index= new IndexList();
            index.setCode(data.get(i).getString("code"));
            index.setCname(data.get(i).getString("cname"));
            index.setCreateuser(data.get(i).getString("createuser"));
            index.setIfdata(data.get(i).getString("ifdata"));
            index.setState(data.get(i).getString("state"));
            index.setCreatetime(data.get(i).getString("createtime"));
            index.setPlantime( data.get(i).getString("plantime"));
            index.setUpdatetime( data.get(i).getString("updatetime"));
            index.setDelayday(data.get(i).getString("delayday"));
            index.setPlanperiod(data.get(i).getString("planperiod"));
            index.setProcode(data.get(i).getString("procode"));
            index.setSort(data.get(i).getString("sort"));
            index.setStartperiod(data.get(i).getString("startperiod"));
            indexLists.add(index);
        }
        return indexLists;
    }
    //新增目录和计划
    public int addCp(IndexList indexList) {
        return IndexListDao.Fator.getInstance().getIndexdatadao().addIndexlist(indexList);
    }
   /* public int updatePlan(IndexList indexList,String code) {
        return IndexListDao.Fator.getInstance().getIndexdatadao().addNplan(indexList,code);
    }*/
    public static int delCataplan(String code){
        return IndexListDao.Fator.getInstance().getIndexdatadao().delIndexcp(code);
    }
    //编辑目录
    public static int updateCate(String code,String procode){
        return IndexListDao.Fator.getInstance().getIndexdatadao().updateCategory(code,procode);
    }
    public static int updateSwitch(IndexList indexList){
        return IndexListDao.Fator.getInstance().getIndexdatadao().updateCp(indexList);
    }
    //复制到
    public int addCopyplan(String cpcode,IndexList data1){
        return IndexListDao.Fator.getInstance().getIndexdatadao().addCopyplan(cpcode,data1);
    }
    public int checkCode(String code){
        return IndexListDao.Fator.getInstance().getIndexdatadao().checkCode(code);
    }
    public int checkProcode(String procode){
        return IndexListDao.Fator.getInstance().getIndexdatadao().checkProcode(procode);
    }


    /**
    * @Description: 获取用户创建的所有指数列表，带分页
    * @Param: [usercode, page, pagesize]
    * @return: java.util.List<com.acmr.model.zhzs.IndexList>
    * @Author: lyh
    * @Date: 2018/9/19
    */
    public List<IndexList> getIndexListByPage(String usercode,int page, int pagesize) throws ParseException {
        List<IndexList> list= new ArrayList<>();
        DataTable table=IndexListDao.Fator.getInstance().getIndexdatadao().getAllIndexListByPage(usercode,page,pagesize);
        List<DataTableRow> rows=table.getRows();
        for (int i=0;i<rows.size();i++){
            String code=rows.get(i).getString("code");
            String cname=rows.get(i).getString("cname");
            String procode=rows.get(i).getString("procode");
            String sort=rows.get(i).getString("sort");
            String startperiod=rows.get(i).getString("startperiod");
            String delayday=rows.get(i).getString("delayday");
            String planperiod=rows.get(i).getString("planperiod");
            String plantime=rows.get(i).getString("plantime");
            String createuser=rows.get(i).getString("createuser");
            String ifdata=rows.get(i).getString("ifdata");
            String state=rows.get(i).getString("state");
            IndexList indexList=new IndexList(code,cname,procode,sort,startperiod,delayday,planperiod,plantime,createuser,ifdata,state);
            list.add(indexList);
        }
        return list;
    }

    /**
    * @Description: 获取子计划列表，包括分页
    * @Param: [pcode, page, pagesize]
    * @return: java.util.List<com.acmr.model.zhzs.IndexList>
    * @Author: lyh
    * @Date: 2018/9/19
    */

    public List<IndexList> getSubIndexListByPage(String pcode,int page,int pagesize){

        User cu=UserService.getCurrentUser();
        String usercode=cu.getUserid();

        List<IndexList> list= new ArrayList<>();
        DataTable table=IndexListDao.Fator.getInstance().getIndexdatadao().getSubIndexListByPage(usercode,pcode,page,pagesize);
        List<DataTableRow> rows=table.getRows();
        for (int i=0;i<rows.size();i++){
            String code=rows.get(i).getString("code");
            String cname=rows.get(i).getString("cname");
            String procode=rows.get(i).getString("procode");
            String sort=rows.get(i).getString("sort");
            String startperiod=rows.get(i).getString("startperiod");
            String delayday=rows.get(i).getString("delayday");
            String planperiod=rows.get(i).getString("planperiod");
            String plantime=rows.get(i).getString("plantime");
            String createuser=rows.get(i).getString("createuser");
            String ifdata=rows.get(i).getString("ifdata");
            String state=rows.get(i).getString("state");
            IndexList indexList=new IndexList(code,cname,procode,sort,startperiod,delayday,planperiod,plantime,createuser,ifdata,state);
            list.add(indexList);
        }
        return list;
    }


    /**
    * @Description: 返回该user分享的计划列表
    * @Param: [usercode]
    * @return: java.util.List<com.acmr.model.zhzs.IndexList>
    * @Author: lyh
    * @Date: 2018/9/20
    */
    public List<Map<String,String>> getSharedList(String usercode){
        List<DataTableRow> rightrows= IndexListDao.Fator.getInstance().getIndexdatadao().getRightListByCreateUser(usercode).getRows();
        List<Map<String,String>> list=new ArrayList<>();
        for (int i=0;i<rightrows.size();i++){
            String code=rightrows.get(i).getString("indexcode");
            String depusercode=rightrows.get(i).getString("depusercode");
            String sort=rightrows.get(i).getString("sort");
            String right=rightrows.get(i).getString("right");
            DataTableRow row=IndexListDao.Fator.getInstance().getIndexdatadao().getByCode(code).getRows().get(0);
            String cname=row.getString("cname");
            String timesort=row.getString("sort");
            Map m=new HashMap();
            m.put("code",code);
            m.put("cname",cname);
            m.put("timesort",timesort);
            m.put("depusercode",depusercode);
            m.put("sort",sort);
            m.put("right",right);
            //分享的是用戶
            if (rightrows.get(i).getString("sort").equals("2")){
                String depusername= UserDepService.getUserNameByCode(depusercode);
                m.put("depusername",depusername);
            }
            //分享的是组织
            else {
                String depusername= DepartmentService.getDepartment(depusercode).getCname();
                m.put("depusername",depusername);
            }
            list.add(m);
        }
        return list;
    }

    /**
    * @Description: 返回当前用户收到的指数信息，Map中包括指数计划对象，和其他信息（主要是String-String型的）
    * @Param: []
    * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    * @Author: lyh
    * @Date: 2018/9/21
    */
    public List<Map<String,Object>> getReceivedList(){
        List<Map<String,Object>> list=new ArrayList<>();
        //已经存在的icode
        List<String> icodes=new ArrayList<>();
        User cu=UserService.getCurrentUser();
        String usercode=cu.getUserid();
        //用户的部门层级列表
        List<Department> deps=UserDepService.getDepPath(usercode);
        List<String> depusercodes=new ArrayList<>();
        depusercodes.add(usercode);
        for (int j=0;j<deps.size();j++){
          //  PubInfo.printStr("deps:"+deps.get(j).getCode());
            depusercodes.add(deps.get(j).getCode());
        }
        //先获取直接分享给用户的指数列表
        //再获取分享给组织的计划（要过滤掉icodelist中已经有的）
        for (int n=0;n<depusercodes.size();n++){
            List<DataTableRow> rowu=IndexListDao.Fator.getInstance().getIndexdatadao().getRightListByDepUserCode(depusercodes.get(n)).getRows();
            for (int i=0;i<rowu.size();i++){
                HashMap<String,Object> m=new HashMap<>();
                String indexcode=rowu.get(i).getString("indexcode");
                //判断该index是否已经存在
                Boolean already=false;
                for (int j=0;j<icodes.size();j++){
                    if (icodes.get(j).equals(indexcode)){
                        already=true;
                        break;
                    }
                }
                if (!already){
                    m.put("indexcode",indexcode);
                    icodes.add(indexcode);
                    DataTableRow row=IndexListDao.Fator.getInstance().getIndexdatadao().getByCode(indexcode).getRows().get(0);
                    String cname=row.getString("cname");
                    String procode=row.getString("procode");
                    String sort=row.getString("sort");
                    String startperiod=row.getString("startperiod");
                    String delayday=row.getString("delayday");
                    String planperiod=row.getString("planperiod");
                    String plantime=row.getString("plantime");
                    String createuser=row.getString("createuser");
                    String createusername=UserDepService.getUserNameByCode(createuser);
                    m.put("createuser",createusername);
                    String ifdata=row.getString("ifdata");
                    String state=row.getString("state");
                    IndexList index=new IndexList(indexcode,cname,procode,sort,startperiod,delayday,planperiod,plantime,createuser,ifdata,state);
                    m.put("index",index);
                    String right=rowu.get(i).getString("right");
                    m.put("right",right);
                    list.add(m);
                }
            }
        }


        return list;
    }

    public Boolean checkModule(String icode){
        //校验模型节点下必须有指标，且权重不能为0
        List<DataTableRow> rows=IndexListDao.Fator.getInstance().getIndexdatadao().getZSMods(icode).getRows();
       // List<IndexMoudle> mods=new ArrayList<>();
        Boolean check=true;
        for (int i=0;i<rows.size();i++){
            String code=rows.get(i).getString("code");
            String cname=rows.get(i).getString("cname");
            String indexcode=rows.get(i).getString("indexcode");
            String procode=rows.get(i).getString("procode");
            String ifzs=rows.get(i).getString("ifzs");
            String ifzb=rows.get(i).getString("ifzb");
            String formula=rows.get(i).getString("formula");
            String sortcode=rows.get(i).getString("sortcode");
            String weight=rows.get(i).getString("weight");
            String dacimal=rows.get(i).getString("dacimal");
            IndexMoudle mod=new IndexMoudle(code,cname,indexcode,procode,ifzs,ifzb,formula,sortcode,weight,dacimal);
            if (mod.ZBnums()<1||mod.getWeight().equals("0")){
                check=false;
            }
          //  mods.add(mod);
        }
        //指标的权重也不能为0
        List<DataTableRow> rows1=IndexListDao.Fator.getInstance().getIndexdatadao().getZBMods(icode).getRows();
        for (int j=0;j<rows1.size();j++){
            if (rows1.get(j).getString("weight").equals("0")){
                check=false;
            }
        }
        return check;
    }

    /**
     * 计划分享的撤回
     * @param indexcode
     * @param depusercode
     * @param sort
     * @return
     */
    public int delShare(String indexcode,String depusercode,String sort){
        return IndexListDao.Fator.getInstance().getIndexdatadao().delShare(indexcode,depusercode,sort);
    }

    /**
     * 我共享的指数的查询搜索
     */
    public List<Map<String,String>> shareSelect(int type,String keyword,String userid){
       List<DataTableRow> datas = IndexListDao.Fator.getInstance().getIndexdatadao().shareSelectList(type,keyword,userid).getRows();
        List<Map<String,String>> list=new ArrayList<>();
        for (int i = 0; i <datas.size() ; i++) {
            Map<String,String> arr = new HashMap<String,String>();
            arr.put("code",datas.get(i).getString("indexcode"));
            arr.put("cname",datas.get(i).getString("cname"));
            arr.put("timesort",datas.get(i).getString("isort"));
            arr.put("depusercode",datas.get(i).getString("depusercode"));
            arr.put("sort",datas.get(i).getString("sort"));
            arr.put("right",datas.get(i).getString("right"));
            if(datas.get(i).getString("sort").equals("2")){
                arr.put("depusername",datas.get(i).getString("ucname"));
            }
            else {
                arr.put("depusername",datas.get(i).getString("dcname"));
            }
            list.add(arr);
        }
        return list;
    }
    //分页
    public List<Map<String,String>> shareSelect(int type,String keyword,String userid,int page,int pagesize){
        List<DataTableRow> datas = IndexListDao.Fator.getInstance().getIndexdatadao().shareSelectListByPage(type,keyword,userid,page,pagesize).getRows();
        List<Map<String,String>> list=new ArrayList<>();
        for (int i = 0; i <datas.size() ; i++) {
            Map<String,String> arr = new HashMap<String,String>();
            arr.put("code",datas.get(i).getString("indexcode"));
            arr.put("cname",datas.get(i).getString("cname"));
            arr.put("timesort",datas.get(i).getString("isort"));
            arr.put("depusercode",datas.get(i).getString("depusercode"));
            arr.put("sort",datas.get(i).getString("sort"));
            arr.put("right",datas.get(i).getString("right"));
            if(datas.get(i).getString("sort").equals("2")){
                arr.put("depusername",datas.get(i).getString("ucname"));
            }
            else {
                arr.put("depusername",datas.get(i).getString("dcname"));
            }
            list.add(arr);
        }
        return list;
    }

    /**
     * 我收到的指数的查询搜索
     */
    public List<Map<String,Object>> receiveSelect(int type,String keyword,String userid){
        List<Map<String,Object>> list=new ArrayList<>();
        //获取部门层级列表
        List<Department> deps=UserDepService.getDepPath(userid);
        List<String> depusercodes = new ArrayList<>();
        depusercodes.add(userid);
        for (int j=0;j<deps.size();j++){
            depusercodes.add(deps.get(j).getCode());
        }
        //已经存在的icode
        List<String> icodes=new ArrayList<>();
        //先获取直接分享给用户的指数列表
        //再获取分享给组织的计划（要过滤掉icodelist中已经有的）
        for (int n=0;n<depusercodes.size();n++){
            List<DataTableRow> row = new ArrayList<>();
            if(n==0){
                row = IndexListDao.Fator.getInstance().getIndexdatadao().receiveSelectList(type,keyword,depusercodes.get(n),"2").getRows();//第一个查用户，优先级最高
            }
            else{
                row = IndexListDao.Fator.getInstance().getIndexdatadao().receiveSelectList(type,keyword,depusercodes.get(n),"1").getRows();
            }
            for (int i=0;i<row.size();i++){
                Map <String,Object> m=new HashMap<>();
                String indexcode=row.get(i).getString("code");
                //判断该index是否已经存在
                Boolean already=false;
                for (int j=0;j<icodes.size();j++){
                    if (icodes.get(j).equals(indexcode)){
                        already=true;
                        break;
                    }
                }
                if (!already){
                    m.put("indexcode",indexcode);
                    icodes.add(indexcode);
                    String cname=row.get(i).getString("cname");
                    String procode=row.get(i).getString("procode");
                    String sort=row.get(i).getString("sort");
                    String startperiod=row.get(i).getString("startperiod");
                    String delayday=row.get(i).getString("delayday");
                    String planperiod=row.get(i).getString("planperiod");
                    String plantime=row.get(i).getString("plantime");
                    String createuser=row.get(i).getString("createuser");
                    m.put("createuser",row.get(i).getString("ucname"));
                    String ifdata=row.get(i).getString("ifdata");
                    String state=row.get(i).getString("state");
                    IndexList index=new IndexList(indexcode,cname,procode,sort,startperiod,delayday,planperiod,plantime,createuser,ifdata,state);
                    m.put("index",index);
                    String right=row.get(i).getString("right");
                    m.put("right",right);
                    list.add(m);
                }
            }
        }
        return list;
    }
   /* public static void main(String[] args) throws ParseException {
        IndexListService indexListService=new IndexListService();
        indexListService.shareSelect(0,"魏","admin");*/
    
    
    /** 
    * @Description: 校验该指数下的基本信息是否完善
    * @Param: [icode] 
    * @return: java.lang.Boolean 
    * @Author: lyh
    * @Date: 2018/9/26 
    */ 
    public Boolean checkInfo(String icode){
        Boolean check=false;
        DataTableRow row=IndexListDao.Fator.getInstance().getIndexdatadao().getByCode(icode).getRows().get(0);
        if (row.getString("plantime")!=""&&row.getString("planperiod")!=""){
            check=true;
        }
        return check;
    }

    /**
    * @Description: 校验该指数下是否已有指标和地区
    * @Param: [icode]
    * @return: java.lang.Boolean
    * @Author: lyh
    * @Date: 2018/9/26
    */
    public Boolean checkZBandReg(String icode){
        Boolean check=false;
        List<DataTableRow> zbrows= IndexEditDao.Fator.getInstance().getIndexdatadao().getZBSbyIndexCode(icode).getRows();
        if (zbrows.size()>0&&zbrows.get(0).getString("regions")!=""){
            check=true;
        }
        return check;
    }

    public static void main(String[] args) {
        IndexListService indexListService=new IndexListService();
        PubInfo.printStr(String.valueOf(indexListService.checkZBandReg("R0015")));
    }

/*        Date test= (Date) indexlist.get(1).get("plantime");
        PubInfo.printStr("123"+test.toString());
        for (int i=0;i<indexlist.size();i++){
            PubInfo.printStr(indexlist.get(i).get("createtime").toString());
            if(indexlist.get(i).get("plantime")!=null){
                PubInfo.printStr(indexlist.get(i).get("plantime").toString());
            }
        }*/
/*        PubInfo.printStr(code.getRows().get(1).getRows().toString());
        PubInfo.printStr(code.getColumns().get(0).getColumnName());*/


}

