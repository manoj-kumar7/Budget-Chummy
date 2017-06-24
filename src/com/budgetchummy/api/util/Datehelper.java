package com.budgetchummy.api.util;

import java.text.ParseException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Datehelper {

	public static long dateToEpoch(String date)
	{
		long epochtime=0;
		try 
		{
		    epochtime = new SimpleDateFormat("yyyy/MM/dd").parse(date).getTime();
		} 
		catch (ParseException e) 
		{
		    e.printStackTrace();
		}
	    return epochtime;
	}
	
	public static String addDaysToDate(String date,int days)
	{
		String new_date=null;
		try 
		{
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			Calendar c = Calendar.getInstance();
			Date date1=df.parse(date);
			c.setTime(date1); 
			c.add(Calendar.DATE, days); 
			new_date = df.format(c.getTime());
		} 
		catch (ParseException e) 
		{
	    	e.printStackTrace();
		}
		return new_date;
	}
	
	public static String endOfMonth(String start_date)
	{
		String end_date=null;
		try 
		{
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		    Date date = df.parse(start_date);
		    Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
			end_date = df.format(c.getTime());
		} 
		catch (ParseException e) 
		{
	    	e.printStackTrace();
		}
		return end_date;
	}
	
	public static String endOfYear(String start_date)
	{
		String end_date=null;
		try 
		{
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		    Date date = df.parse(start_date);
		    Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));
			end_date = df.format(c.getTime());
		} 
		catch (ParseException e) 
		{
	    	e.printStackTrace();
		}
		return end_date;
	}
	
	public static String epochToDate(long epoch)
	{
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date epochtime = new Date(epoch);
		String date = df.format(epochtime);
		return date;
	}


}
