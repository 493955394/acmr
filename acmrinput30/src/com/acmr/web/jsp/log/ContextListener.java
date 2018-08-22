package com.acmr.web.jsp.log;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.acmr.service.LogQueueRead;

public class ContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}


	public void contextInitialized(ServletContextEvent arg0) {
      Thread thread = new Thread(new LogQueueRead());  
      thread.setDaemon(true);
      thread.setPriority(Thread.MIN_PRIORITY);
      thread.start(); 
	} 

}
