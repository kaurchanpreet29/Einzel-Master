package com.floreantpos.ui.util;

import java.util.Calendar;
import java.util.Date;

public class BusinessDateUtil { 

  public static Date startOfOfficialDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    synchronized (calendar) {
      calendar.setTime(date);
      calendar.set(Calendar.DATE, calendar.getTime().getDate());
      calendar.set(Calendar.HOUR_OF_DAY, 6);
      calendar.set(Calendar.MILLISECOND, 999);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MINUTE, 0);
      return calendar.getTime();
    }
  }

 /* public static Date startOfDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    synchronized (calendar) {
      calendar.setTime(date);
      calendar.set(Calendar.DATE, calendar.getTime().getDate());
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MINUTE, 0);
      return calendar.getTime();
    }
  }*/

  public static Date endOfOfficialDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    synchronized (calendar) {
      calendar.setTime(date);
      calendar.set(Calendar.DATE, calendar.getTime().getDate() + 1);
      calendar.set(Calendar.HOUR_OF_DAY, 5);
      calendar.set(Calendar.MILLISECOND, 0);
      calendar.set(Calendar.SECOND, 59);
      calendar.set(Calendar.MINUTE, 59);
      return calendar.getTime();
    }
  }

 /* public static Date endOfDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    synchronized (calendar) {
      calendar.setTime(date);
      calendar.set(Calendar.DATE, calendar.getTime().getDate());
      calendar.set(Calendar.HOUR_OF_DAY, 23);
      calendar.set(Calendar.MILLISECOND, 0);
      calendar.set(Calendar.SECOND, 59);
      calendar.set(Calendar.MINUTE, 59);
      return calendar.getTime();
    }
  }*/
}
