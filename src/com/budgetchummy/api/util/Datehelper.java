package com.budgetchummy.api.util;

import java.text.ParseException;
import java.text.DateFormat;
import java.util.*;
import java.text.SimpleDateFormat;

public class Datehelper {

	public static String utc = "UTC";
	public static String[] month_array = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	public static long convertTimeZone(long date, String toTZ)
	{
		Calendar fromTime = Calendar.getInstance();
	    fromTime.setTimeZone(TimeZone.getTimeZone(utc));
	    fromTime.setTimeInMillis(date);
	    Calendar toTime = new GregorianCalendar(TimeZone.getTimeZone(toTZ));
	    toTime.set(Calendar.DATE, fromTime.get(Calendar.DATE));
	    toTime.set(Calendar.MONTH, fromTime.get(Calendar.MONTH));
	    toTime.set(Calendar.YEAR, fromTime.get(Calendar.YEAR));
	    toTime.set(Calendar.HOUR_OF_DAY, fromTime.get(Calendar.HOUR_OF_DAY));
	    toTime.set(Calendar.MINUTE, fromTime.get(Calendar.MINUTE));
	    toTime.set(Calendar.SECOND, fromTime.get(Calendar.SECOND));
	    toTime.set(Calendar.MILLISECOND, fromTime.get(Calendar.MILLISECOND));
	    return toTime.getTimeInMillis();
	}
	public static long getServerTimeInEpoch()
	{
		return System.currentTimeMillis();
	}
	public static long subtractDays(long date, long no_of_days)
	{
		return date - (no_of_days * 86400000);
	}
	public static String epochToCustomFormat(long epoch)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(epoch);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		String monthString = month_array[month];
		int date = cal.get(Calendar.DATE);
		String customString = monthString + " " + date + ", " + year;
		return customString;
	}


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
