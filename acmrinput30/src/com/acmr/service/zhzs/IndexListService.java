package com.acmr.service.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.model.zhzs.IndexList;

import java.text.ParseException;
import java.util.*;

public class IndexListService {
    String name = IndexListDao.Fator.getInstance().getIndexdatadao().getName();
    //通过usercode得到计划和目录列表

    public ArrayList<IndexList> getSublist(String code){
        ArrayList<IndexList> indexLists=new ArrayList<>();
        List<DataTableRow> indexlist = IndexListDao.Fator.getInstance().getIndexdatadao().getSubLists(code,"usercode01").getRows();
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
        return indexLists;
    }

    public List<IndexList> getSubCate(String pcode){
        List<IndexList> indexLists=new ArrayList<>();
        List<DataTableRow> rows = IndexListDao.Fator.getInstance().getIndexdatadao().getSubLists(pcode,"usercode01").getRows();
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
        List<DataTableRow> indexlist = IndexListDao.Fator.getInstance().getIndexdatadao().getByUser("usercode01").getRows();
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
        DataTableRow data = IndexListDao.Fator.getInstance().getIndexdatadao().getByCode(code).getRows().get(0);
        IndexList index= new IndexList();
        index.setCode(data.getString("code"));
        index.setCname(data.getString("cname"));
        index.setCreateuser(data.getString("createuser"));
        index.setIfdata(data.getString("ifdata"));
        index.setState(data.getString("state"));
        index.setCreatetime(data.getString("createtime"));
        index.setPlantime( data.getString("plantime"));
        index.setUpdatetime( data.getString("updatetime"));
        index.setDelayday(data.getString("delayday"));
        index.setPlanperiod(data.getString("planperiod"));
        index.setProcode(data.getString("procode"));
        index.setSort(data.getString("sort"));
        index.setStartperiod(data.getString("startperiod"));
        return index;
    }

    //查找功能
    public ArrayList<IndexList> found(int type,String code){
        ArrayList<IndexList> indexLists=new ArrayList<>();
        List<DataTableRow> data = new ArrayList<>();
       if(type==0){//0表示是通过code查的
            data = IndexListDao.Fator.getInstance().getIndexdatadao().getLikeCode(code).getRows();

       }else if(type==1){//1表示是通过cname查的
           data = IndexListDao.Fator.getInstance().getIndexdatadao().getLikeCname(code).getRows();
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

    public int addCp(IndexList indexList) {
        return IndexListDao.Fator.getInstance().getIndexdatadao().addIndexlist(indexList);
    }
   /* public int updatePlan(IndexList indexList,String code) {
        return IndexListDao.Fator.getInstance().getIndexdatadao().addNplan(indexList,code);
    }*/
    public static int delCataplan(String code){
        return IndexListDao.Fator.getInstance().getIndexdatadao().delIndexcp(code);
    }
    public static int updateCate(String code,String procode){
        return IndexListDao.Fator.getInstance().getIndexdatadao().updateCategory(code,procode);
    }
    public static int updateCatePlan(IndexList indexList){
        return IndexListDao.Fator.getInstance().getIndexdatadao().updateCp(indexList);
    }
    //复制到
    public int addCopyplan(IndexList data1){
        return IndexListDao.Fator.getInstance().getIndexdatadao().addCopyplan(data1);
    }
    public int checkCode(String code){
        return IndexListDao.Fator.getInstance().getIndexdatadao().checkCode(code);
    }
    public int checkProcode(String procode){
        return IndexListDao.Fator.getInstance().getIndexdatadao().checkProcode(procode);
    }

    public List<IndexList> getIndexListByPage(String usercode,int page, int pagesize) throws ParseException {
        List<IndexList> list= new ArrayList<>();
        DataTable table=IndexListDao.Fator.getInstance().getIndexdatadao().getIndexListByPage(usercode,page,pagesize);
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

    public static void main(String[] args) throws ParseException {
        IndexListService indexListService=new IndexListService();
        indexListService.getIndexListByPage("usercode01",0,10);
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

