package com.floreantpos.test;

import java.util.Calendar;
import java.util.Date;

import org.jdesktop.swingx.calendar.DateUtils;

public class DateTest {
	public static void main(String args[])
	{
		Date today_date = new Date();
		System.out.println("Today date: "+ today_date);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		System.out.println("Yesterday's date = "+ DateUtils.endOfDay(cal.getTime()));
		
	}
}
