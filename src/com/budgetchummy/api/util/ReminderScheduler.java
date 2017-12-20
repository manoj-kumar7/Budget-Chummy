package com.budgetchummy.api.util;

import org.quartz.*;
import org.quartz.ee.servlet.QuartzInitializerServlet;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.DateBuilder.*;
import java.util.*;
import javax.servlet.*;

import javax.servlet.http.HttpServletRequest;

public class ReminderScheduler {
	static private String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
	public static void scheduleReminder(HttpServletRequest request, int day, int month, int year, String timezone, long job_id) throws SchedulerException{
		
		try{

			ServletContext ctx = request.getServletContext();  
			
			StdSchedulerFactory factory = (StdSchedulerFactory) ctx.getAttribute(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
			
			Scheduler scheduler = factory.getScheduler();
			
			JobDetail job = JobBuilder.newJob(ReminderSchedulerJob.class).withIdentity("ReminderSchedulerClass", "group1")
							.usingJobData("job_id", job_id)
							.build();
	//
	//		Trigger t1 = TriggerBuilder.newTrigger().withIdentity("ReminderTrigger")
	//				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(60).repeatForever()).build();
	//		
	//		Scheduler sc = StdSchedulerFactory.getDefaultScheduler();
	//		
	//		sc.start();
	//		sc.scheduleJob(job, t1);
			String scheduleDate = "0 0 0 " + day + " " + months[month] + " ? " + year;
			Trigger trigger = TriggerBuilder.newTrigger()
					    .withIdentity("ReminderSchedulerTrigger", "group1")
					    .withSchedule(
					    		CronScheduleBuilder.cronSchedule(scheduleDate)
					    		.inTimeZone(TimeZone.getTimeZone(timezone))
					    )
					    .build();
	
			scheduler.scheduleJob(job, trigger);
			
//			Thread.sleep(240000);
			
//			sc.shutdown();
		} catch (Exception e) {
            e.printStackTrace();
        }

	}
}
