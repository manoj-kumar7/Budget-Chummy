package com.budgetchummy.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.quartz.*;

import com.budgetchummy.api.util.ReminderScheduler;

@WebListener
public class SchedulerListener implements ServletContextListener {

    public SchedulerListener() {
    	
    }

    public void contextDestroyed(ServletContextEvent arg0)  { 
         
    }

    public void contextInitialized(ServletContextEvent arg0){ 
        try
        {
        	ReminderScheduler.scheduleReminder();
        }
    	catch(SchedulerException se)
        {
    		se.printStackTrace();
        }
    }
	
}
