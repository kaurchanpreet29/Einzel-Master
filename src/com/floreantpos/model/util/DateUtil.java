package com.floreantpos.model.util;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JComboBox;

public class DateUtil {
	public static Date startOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 6);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		return new Date(cal.getTimeInMillis());
	}

	public static Date endOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 5);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
		return new Date(cal.getTimeInMillis());
	}
	
	 public static void populateMonth(JComboBox month)
	  {
		 Calendar c = Calendar.getInstance();
			int mon = c.getTime().getMonth();
		 month.addItem("Januar");
		 month.addItem("Februar");
		 month.addItem("März");
		 month.addItem("April");
		 month.addItem("Mai");
		 month.addItem("Juni");
		 month.addItem("Juli");
		 month.addItem("August");
		 month.addItem("September");
		 month.addItem("Oktober");
		 month.addItem("November");
		 month.addItem("Dezember");
		 month.setSelectedIndex(mon);
	  }
	
	public static void populateYear(JComboBox year) {
		Calendar c = Calendar.getInstance();
		int ye = c.getTime().getYear() + 1900;
		int setDate = ye-5;

		while(ye+2!=setDate) {
			year.addItem(Integer.toString(setDate));
			setDate++;
		}
		year.setSelectedItem(ye + "");
	}
	
	
	public static int getMonth(String month) {
		if (month.compareTo("Januar") == 0)
			return 1;
		else if (month.compareTo("Februar") == 0)
			return 2;
		else if (month.compareTo("März") == 0)
			return 3;
		else if (month.compareTo("April") == 0)
			return 4;
		else if (month.compareTo("Mai") == 0)
			return 5;
		else if (month.compareTo("Juni") == 0)
			return 6;
		else if (month.compareTo("Juli") == 0)
			return 7;
		else if (month.compareTo("August") == 0)
			return 8;
		else if (month.compareTo("September") == 0)
			return 9;
		else if (month.compareTo("Oktober") == 0)
			return 10;
		else if (month.compareTo("November") == 0)
			return 11;
		else
			return 12;
	}

}
