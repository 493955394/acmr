package com.acmr.service.zhzs;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.IIndexTaskDao;
import com.acmr.dao.zhzs.IndexEditDao;
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
    * @Description: 返回指定code的计划应该生成的periods（期数），考虑delayday
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
                if (!bool&&dateBefore(index,String.valueOf(i))){
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
                        if (!bool&&dateBefore(index,thisp)){
                            periods.add(thisp);
                        }
                    }
                }
                //处理中间年
                else {
                    for (int j=65;j<69;j++){
                        String thisp=String.valueOf(i)+(char)j;
                        Boolean bool=IndexTaskDao.Fator.getInstance().getIndexdatadao().hasTask(code, thisp);
                        if (!bool&&dateBefore(index,thisp)){
                            periods.add(thisp);
                        }
                    }
                }
            }
            //处理最后一年
            for (int i=65;i<=nq;i++){
                String thisp=String.valueOf(nyear)+(char)i;
                Boolean bool=IndexTaskDao.Fator.getInstance().getIndexdatadao().hasTask(code, thisp);
                if (!bool&&dateBefore(index,thisp)){
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
                        if (!bool&&dateBefore(index,thisp)){
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
                        if (!bool&&dateBefore(index,thisp)){
                            periods.add(thisp);
                        }
                    }
                }
                //处理最后一年
                else if (i==nyear&&i!=syear){
                    for (int j=1;j<=nm;j++){
                        String thisp;
                        if (j<10){
                            thisp=String.valueOf(i)+"0"+String.valueOf(j);
                        }
                        else {
                            thisp=String.valueOf(i)+String.valueOf(j);
                        }
                        Boolean bool=IndexTaskDao.Fator.getInstance().getIndexdatadao().hasTask(code, thisp);
                        if (!bool&&dateBefore(index,thisp)){
                            periods.add(thisp);
                        }
                    }
                }
            }
        }
        PubInfo.printStr("periods:"+periods.toString());
        return periods;
    }
    /**
     * 范围确认计划起始时间补全
     */
    public List<String> getTimes(IndexList index){
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
                    periods.add(String.valueOf(i));
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
                            periods.add(thisp);
                    }
                }
                //处理中间年
                else {
                    for (int j=65;j<69;j++){
                        String thisp=String.valueOf(i)+(char)j;
                            periods.add(thisp);
                    }
                }
            }
            //处理最后一年
            for (int i=65;i<=nq;i++){
                String thisp=String.valueOf(nyear)+(char)i;
                    periods.add(thisp);
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
                            periods.add(thisp);
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
                            periods.add(thisp);
                    }
                }
                //处理最后一年
                else if (i==nyear&&i!=syear){
                    for (int j=1;j<=nm;j++){
                        String thisp;
                        if (j<10){
                            thisp=String.valueOf(i)+"0"+String.valueOf(j);
                        }
                        else {
                            thisp=String.valueOf(i)+String.valueOf(j);
                        }
                            periods.add(thisp);
                    }
                }
            }
        }
        PubInfo.printStr("periods:"+periods.toString());
        return periods;
    }



    /**
    * @Description: 判断这一期是否先于现在的时间
    * @Param: [index, period]
    * @return: java.lang.Boolean,true表示先于现在的时间，可以生成，false表示暂时不能生成
    * @Author: lyh
    * @Date: 2018/9/26
    */
    public Boolean dateBefore(IndexList index,String period){
        Boolean isBefore=false;
        String sort=index.getSort();
        String delayday=index.getDelayday();
        Calendar nowCal=Calendar.getInstance();
        if (sort.equals("y")){
            Calendar cal=getYearCalendar(period,delayday);
            isBefore=cal.before(nowCal);
        }
        else if (sort.equals("q")){
            Calendar cal=getQCalendar(period,delayday);
            isBefore=cal.before(nowCal);
        }
        else {
            Calendar cal=getMonCalendar(period,delayday);
            isBefore=cal.before(nowCal);
        }
        return isBefore;
    }

    /**
    * @Description: 返回年度的计划生成该期任务的Calendar
    * @Param: [period, delayday]
    * @return: java.util.Calendar
    * @Author: lyh
    * @Date: 2018/9/26
    */
    public Calendar getYearCalendar(String period,String delayday){
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(period)+1);
        cal.set(Calendar.MONTH,0);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);;
        return cal;
    }

    /**
    * @Description:  返回季度的计划生成该期任务的Calendar
    * @Param: [period, delayday]
    * @return: java.util.Calendar
    * @Author: lyh
    * @Date: 2018/9/26
    */
    public Calendar getQCalendar(String period,String delayday){
        Calendar cal=Calendar.getInstance();
        String year=period.substring(0,4);
        String q=period.substring(4);
        if (q.equals("D")){
              cal.set(Calendar.YEAR, Integer.parseInt(year)+1);
              cal.set(Calendar.MONTH,0);
              cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
              cal.set(Calendar.HOUR_OF_DAY,0);
              cal.set(Calendar.MINUTE,0);
              cal.set(Calendar.SECOND,0);
        }
        else if (q.equals("A")){
            cal.set(Calendar.YEAR, Integer.parseInt(year));
            cal.set(Calendar.MONTH,3);
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
            cal.set(Calendar.HOUR_OF_DAY,0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
        }
        else if (q.equals("B")){
            cal.set(Calendar.YEAR, Integer.parseInt(year));
            cal.set(Calendar.MONTH,6);
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
            cal.set(Calendar.HOUR_OF_DAY,0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
        }
        else {
            cal.set(Calendar.YEAR, Integer.parseInt(year));
            cal.set(Calendar.MONTH,9);
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
            cal.set(Calendar.HOUR_OF_DAY,0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
        }

        return cal;
    }

    /**
    * @Description:  返回月度的计划生成该期任务的Calendar
    * @Param: [period, delayday]
    * @return: java.util.Calendar
    * @Author: lyh
    * @Date: 2018/9/26
    */
    public Calendar getMonCalendar(String period,String delayday){
        Calendar cal=Calendar.getInstance();
        String year=period.substring(0,4);
        String mon=period.substring(4);
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(mon));
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        return cal;
    }

    /**
     * @Description: 更新计划的plantime，planperiod到最新
     * @Param: [index, periods]
     * @return: void
     * @Author: lyh
     * @Date: 2018/9/26
     */
    public void updateTime(IndexList index,List<String> periods){
        String sort=index.getSort();
        String delayday=index.getDelayday();
        String lastperiod=periods.get(periods.size()-1);
        //下一期period
        String period;
        Calendar cal;
        if (sort.equals("y")){
            period= String.valueOf(Integer.parseInt(lastperiod)+1);
            cal=getYearCalendar(period,delayday);
        }
        else if (sort.equals("q")){
            String year=lastperiod.substring(0,4);
            String q=lastperiod.substring(4);
            if (q.equals("D")){
                String nyear= String.valueOf(Integer.parseInt(year)+1);
                period=nyear+"A";
                cal=getQCalendar(period,delayday);
            }
            else {
                int sq=q.charAt(0)+1;
                period=year+(char)sq;
                cal=getQCalendar(period,delayday);

            }
        }
        else {
            String year=lastperiod.substring(0,4);
            int mon= Integer.parseInt(lastperiod.substring(4));
            if (mon!=12){
                if (mon<9){
                    period=year+"0"+String.valueOf(mon+1);
                }
                else {
                    period=year+String.valueOf(mon+1);
                }
            }
            else {
                String nyear= String.valueOf(Integer.parseInt(year)+1);
                period=nyear+"01";
            }
            cal=getMonCalendar(period,delayday);
        }

        Date time=cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String plantime=df.format(time);
        int i= IndexListDao.Fator.getInstance().getIndexdatadao().updateTime(plantime,period,index.getCode());

    }


    /**
    * @Description: 返回月份对应的ABCD
    * @Param: [mon]
    * @return: char
    * @Author: lyh
    * @Date: 2018/9/26
    */
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
             //生成完之后开始做计算，从data表和module_tmp表里取数
            OriginDataService originDataService = new OriginDataService();
            originDataService.todocalculate(tcode,ayearmon);

            //更新计划的plantime，planperiod
            updateTime(index,periods);
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
