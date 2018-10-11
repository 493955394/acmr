package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.zhzs.IndexList;
import com.acmr.service.zhzs.IndexListService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class indexplan extends BaseAction {
        /**
         * 收到的指数 复制到
         * @author wf
         * @date
         * @param
         * @return
         */
        public void sharecopy() throws IOException {
            IndexListService indexListService = new IndexListService();
            HttpServletRequest req = this.getRequest();
            String cpcode = PubInfo.getString(req.getParameter("cosharecode"));//原code
            //String ifdata1 = PubInfo.getString(req.getParameter("cifdata"));
            //int ifdata = Integer.parseInt(ifdata1);
            String code = PubInfo.getString(req.getParameter("putcode"));
            JSONReturnData data = new JSONReturnData("");
            int x = indexListService.checkCode(code);
            if (x == 0) {
                data.setReturncode(300);
                this.sendJson(data);
                return;
            } else {
                data.setReturncode(200);
            }
            String cname = PubInfo.getString(req.getParameter("putname"));
            String nprocode = PubInfo.getString(req.getParameter("shareprocode"));
            //String createtime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            IndexList copydata = indexListService.getData(cpcode);

            copydata.setCode(code);
            copydata.setCname(cname);
            copydata.setProcode(nprocode);
            //data1.setCreatetime(createtime);
            String startpeirod = copydata.getStartperiod();
            String delayday = copydata.getDelayday();
            //生成plantime，planperiod
            if (startpeirod.length()==4){
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(startpeirod)+1);
                calendar.set(Calendar.MONTH,0);
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                calendar.set(Calendar.HOUR_OF_DAY,0);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);
                // calendar.set(Calendar.MILLISECOND,0);
                Date time=calendar.getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String plantime=df.format(time);
                copydata.setPlantime(plantime);
                String planperiod=startpeirod;
                copydata.setPlanperiod(planperiod);

            }
            else if (startpeirod.length()==5){
                String q=startpeirod.substring(4);
                String year=startpeirod.substring(0,4);
                Calendar calendar=Calendar.getInstance();
                if (q.equals("D")){
                    calendar.set(Calendar.YEAR, Integer.parseInt(year)+1);
                    calendar.set(Calendar.MONTH,0);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    copydata.setPlantime(plantime);
                    String planperiod=startpeirod;
                    copydata.setPlanperiod(planperiod);
                }
                else if (q.equals("A")){
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                    calendar.set(Calendar.MONTH,3);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    copydata.setPlantime(plantime);
                    String planperiod=startpeirod;
                    copydata.setPlanperiod(planperiod);
                }
                else if (q.equals("B")){
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                    calendar.set(Calendar.MONTH,6);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    copydata.setPlantime(plantime);
                    String planperiod=startpeirod;
                    copydata.setPlanperiod(planperiod);
                }
                else {
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                    calendar.set(Calendar.MONTH,9);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    copydata.setPlantime(plantime);
                    String planperiod=startpeirod;
                    copydata.setPlanperiod(planperiod);
                }
            }
            else {
                String year=startpeirod.substring(0,4);
                String mon=startpeirod.substring(4);
                Calendar calendar=Calendar.getInstance();
                if (mon.equals("12")){
                    calendar.set(Calendar.YEAR, Integer.parseInt(year)+1);
                    calendar.set(Calendar.MONTH,0);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    copydata.setPlantime(plantime);
                    String planperiod=startpeirod;
                    copydata.setPlanperiod(planperiod);
                }
                else {
                    calendar.set(Calendar.YEAR, Integer.parseInt(year));
                    calendar.set(Calendar.MONTH,Integer.parseInt(mon));
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(delayday));
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    Date time=calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String plantime=df.format(time);
                    copydata.setPlantime(plantime);
                    String planperiod=startpeirod;
                    copydata.setPlanperiod(planperiod);
                }
            }
            indexListService.addCopyShare(cpcode,copydata);
        /*if (int1 == -1) {
            data.setReturncode(501);
            data.setReturndata("fail");
            this.sendJson(data);
            return;
        }*/
            data.setReturndata(copydata);
            this.sendJson(data);
        }
    }

