package com.acmr.service.zhzs;

import acmr.util.PubInfo;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TaskTimerService {

	Timer timer1;

	class RT extends TimerTask {
		public void run() {
			PubInfo.printStr("时间到，执行");
			Calendar calendar = Calendar.getInstance();
			Date time = calendar.getTime();
			PubInfo.printStr("time:"+time.toString());
			CreateTaskService createTaskService=new CreateTaskService();
			try {
				createTaskService.createAll();
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
	}


	public  TaskTimerService() {
		super();
		timer1 = new Timer();
		RT task =new RT();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.SECOND, 0);
		Date time = calendar.getTime();
		PubInfo.printStr(time.toString());
		int m=1*60*1000;
		timer1.scheduleAtFixedRate(task,time,m);
	}

	public static void main(String[] args) {
		new TaskTimerService();
	}
}
