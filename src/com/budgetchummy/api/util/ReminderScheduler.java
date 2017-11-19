package com.budgetchummy.api.util;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class ReminderScheduler {
	public static void scheduleReminder() throws SchedulerException{
		
		JobDetail job = JobBuilder.newJob(ReminderSchedulerJob.class).build();

		Trigger t1 = TriggerBuilder.newTrigger().withIdentity("ReminderTrigger")
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(60).repeatForever()).build();
		
		Scheduler sc = StdSchedulerFactory.getDefaultScheduler();
		
		sc.start();
		sc.scheduleJob(job, t1);
	}
}
