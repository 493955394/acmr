package com.acmr.service.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.IIndexTaskDao;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.dao.zhzs.IndexTaskDao;
import com.acmr.model.zhzs.IndexList;
import com.acmr.model.zhzs.IndexTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreateTaskService {
    
    /** 
    * @Description: 获得数据库中要生成的新时间小于现在的时间的计划list
    * @Param: [] 
    * @return: void 
    * @Author: lyh
    * @Date: 2018/9/11 
    */ 
    public List<IndexList> getStartIndex() throws ParseException {
        List<IndexList> indexlist=new ArrayList<>();
        String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //PubInfo.printStr(date);
        DataTable table=IndexListDao.Fator.getInstance().getIndexdatadao().getStartLists(date);
        List<DataTableRow> list=table.getRows();
        for (int i=0;i<list.size();i++){
            PubInfo.printStr(list.get(i).getRows().toString());
            String code=list.get(i).getString("code");
            String cname=list.get(i).getString("cname");
            String procode=list.get(i).getString("procode");
            String sort=list.get(i).getString("sort");
            String startperiod=list.get(i).getString("startperiod");
            String delayday=list.get(i).getString("delayday");
            String planperiod=list.get(i).getString("planperiod");
            String plantime1=list.get(i).getString("plantime");
            Date plantime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(plantime1);
            String createuser=list.get(i).getString("createuser");
            String ifdata=list.get(i).getString("ifdata");
            String state=list.get(i).getString("state");
            IndexList indexList=new IndexList(code,cname,procode,sort,startperiod,delayday,planperiod,plantime1,createuser,ifdata,state);
            indexlist.add(indexList);
        }
        return indexlist;
    }

    /**
    * @Description: 返回指定code的计划应该生成的periods(期数)
    * @Param: [code]
    * @return: void
    * @Author: lyh
    * @Date: 2018/9/11
    */
    public List<String> getPeriods(IndexList index){
        List<String> periods=new ArrayList<>();
        String code=index.getCode();
        String sort=index.getSort();
        String startperiod=index.getStartperiod();
        //统计周期为年度
        if (sort.equals("y")){
            Calendar calendar=Calendar.getInstance();
            int now=calendar.get(Calendar.YEAR);
            int start= Integer.parseInt(startperiod);
            for (int i=start;i<now;i++){
                Boolean bool=IndexTaskDao.Fator.getInstance().getIndexdatadao().hasTask(code, String.valueOf(i));
                if (!bool){
                    periods.add(String.valueOf(i));
                }
            }
        }
        //统计周期为季度
        else if (sort.equals("q")){
            Calendar calendar=Calendar.getInstance();
            int syear= Integer.parseInt(startperiod.substring(0,4));
            //转为asc码，65，66，67，68，ABCD
            int sq=startperiod.charAt(4);
            int nyear=calendar.get(Calendar.YEAR);
            int nq=getQ(calendar.get(Calendar.MONTH));
            for (int i=syear;i<nyear;i++){
                //处理第一年
                if (i==syear){
                    for (int j=sq;j<69;j++){
                        String thisp=String.valueOf(i)+(char)j;
                        Boolean bool=IndexTaskDao.Fator.getInstance().getIndexdatadao().hasTask(code, thisp);
                        if (!bool){
                            periods.add(thisp);
                        }
                    }
                }
                //处理中间年
                else {
                    for (int j=65;j<69;j++){
                        String thisp=String.valueOf(i)+(char)j;
                        Boolean bool=IndexTaskDao.Fator.getInstance().getIndexdatadao().hasTask(code, thisp);
                        if (!bool){
                            periods.add(thisp);
                        }
                    }
                }
            }
            //处理最后一年
            for (int i=nq-1;i>64;i--){
                String thisp=String.valueOf(nyear)+(char)i;
                Boolean bool=IndexTaskDao.Fator.getInstance().getIndexdatadao().hasTask(code, thisp);
                if (!bool){
                    periods.add(thisp);
                }
            }


        }
        //统计周期为月度
        else if (sort.equals("m")){
            Calendar calendar=Calendar.getInstance();
            int syear= Integer.parseInt(startperiod.substring(0,4));
            int sm= Integer.parseInt(startperiod.substring(4));
            int nyear=calendar.get(Calendar.YEAR);
            int nm=calendar.get(Calendar.MONTH);
            for (int i=syear;i<=nyear;i++){
                //处理第一年
                if (i==syear){
                    for (int j=sm;j<13;j++){
                        String thisp;
                        if (j<10){
                            thisp=String.valueOf(i)+"0"+String.valueOf(j);
                        }
                        else {
                            thisp=String.valueOf(i)+String.valueOf(j);
                        }
                        Boolean bool=IndexTaskDao.Fator.getInstance().getIndexdatadao().hasTask(code, thisp);
                        if (!bool){
                            periods.add(thisp);
                        }
                    }
                }
                //处理最后一年
                if (i==nyear){
                    for (int j=nm;j>0;j--){
                        String thisp;
                        if (j<10){
                            thisp=String.valueOf(i)+"0"+String.valueOf(j);
                        }
                        else {
                            thisp=String.valueOf(i)+String.valueOf(j);
                        }
                        Boolean bool=IndexTaskDao.Fator.getInstance().getIndexdatadao().hasTask(code, thisp);
                        if (!bool){
                            periods.add(thisp);
                        }
                    }
                }
                //处理中间年
                else if (syear<i&&i<nyear){
                    for (int j=1;j<13;j++){
                        String thisp;
                        if (j<10){
                            thisp=String.valueOf(i)+"0"+String.valueOf(j);
                        }
                        else {
                            thisp=String.valueOf(i)+String.valueOf(j);
                        }
                        Boolean bool=IndexTaskDao.Fator.getInstance().getIndexdatadao().hasTask(code, thisp);
                        if (!bool){
                            periods.add(thisp);
                        }
                    }
                }
            }
        }
        PubInfo.printStr(periods.toString());
        return periods;
    }

    public char getQ(int mon){
        if (mon<4){
            return 'A';
        }
        else if (4<=mon&&mon<7){
            return 'B';
        }
        else if (7<=mon&&mon<10){
            return 'C';
        }
        else {
            return 'D';
        }
    }

    /**
    * @Description: 生成指定计划的任务
    * @Param: [index, periods]
    * @return: void
    * @Author: lyh
    * @Date: 2018/9/11
    */
    public void createTasks(IndexList index, List<String> periods){
        String indexcode=index.getCode();
        for (int i=0;i<periods.size();i++){
            String tcode= UUID.randomUUID().toString().replace("-", "").toLowerCase();
            String ayearmon=periods.get(i);
            String createtime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            IndexTaskDao.Fator.getInstance().getIndexdatadao().create(indexcode,tcode,ayearmon,createtime);
        }
    }

    /**
    * @Description: 生成所有的计划的所有的任务
    * @Param: []
    * @return: void
    * @Author: lyh
    * @Date: 2018/9/11
    */
    public void createAll() throws ParseException {
        List<IndexList> list=getStartIndex();
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                IndexList index=list.get(i);
                List<String> periods=getPeriods(index);
                createTasks(index,periods);
            }
        }
    }


/*    public static void main(String[] args) throws ParseException {
        createAll();
    }*/
}
