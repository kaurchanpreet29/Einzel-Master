package com.floreantpos.util;

import java.util.Calendar;
import java.util.Date;

public class BusinessDateUtil {
  public static Date endOfBusinessDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    synchronized (calendar) {
      calendar.setTime(date);
      calendar.set(Calendar.DATE, calendar.getTime().getDate() + 1);
      calendar.set(Calendar.HOUR_OF_DAY, 3);
      calendar.set(Calendar.MILLISECOND, 999);
      calendar.set(Calendar.SECOND, 59);
      calendar.set(Calendar.MINUTE, 59);
      return calendar.getTime();
    }
  }

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

  public static Date startOfBusinessDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    synchronized (calendar) {
      calendar.setTime(date);
      if (date.getHours() >= 4) {
        calendar.set(Calendar.DATE, calendar.getTime().getDate());
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTime();
      } else {
        calendar.set(Calendar.DATE, calendar.getTime().getDate() - 1);
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTime();
      }
    }
  }
}
