package com.travelsky.autotest.autosky.utils;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil
{
  public static String dateToStr(Date date, String pattern)
  {
    return dateToStr(date, pattern, Locale.CHINA);
  }

  public static String dateToStr(Date date, String pattern, Locale locale)
  {
    if (pattern == null) {
      pattern = "yyyy-MM-dd HH:mm:ss.SSS";
    }
    DateFormat ymdhmsFormat = new SimpleDateFormat(pattern, locale);

    return ymdhmsFormat.format(date);
  }

  public static Date strToDate(String str, String pattern)
    throws ParseException
  {
    return strToDate(str, pattern, Locale.CHINA);
  }

  public static Date strToDate(String str, String pattern, Locale locale)
    throws ParseException
  {
    if (pattern == null) {
      pattern = "yyyy-MM-dd HH:mm:ss.SSS";
    }
    DateFormat ymdhmsFormat = new SimpleDateFormat(pattern, locale);
    return ymdhmsFormat.parse(str);
  }

  public static Date getToday()
  {
    Calendar ca = Calendar.getInstance();
    return ca.getTime();
  }

  public static Date mkDate(int year, int month, int date)
  {
    Calendar ca = Calendar.getInstance();
    ca.set(year, month - 1, date);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    sdf.format(ca.getTime());
    return ca.getTime();
  }

  public Date getGmtDate(Long time)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(time.longValue());
    int offset = calendar.get(15) / 3600000 + calendar.get(16) / 3600000;
    calendar.add(10, -offset);
    Date date = calendar.getTime();
    return date;
  }

  public static String getSpecifyDate(int interval, String format)
  {
    return getSpecifyDate(interval, format, Locale.CHINA);
  }

  public static String getSpecifyDate(int interval, String format, Locale locale)
  {
    Calendar cal = new GregorianCalendar();
    cal.add(5, interval);
    return dateToStr(cal.getTime(), format, locale);
  }

  public static String getSpecifyMonth(int interval, String format)
  {
    return getSpecifyMonth(interval, format, Locale.CHINA);
  }

  public static String getSpecifyMonth(int interval, String format, Locale locale)
  {
    Calendar cal = new GregorianCalendar();
    cal.add(2, interval);
    return dateToStr(cal.getTime(), format, locale);
  }

  public static String getSpecifyYear(int interval, String format)
  {
    return getSpecifyYear(interval, format, Locale.CHINA);
  }

  public static String getSpecifyYear(int interval, String format, Locale locale)
  {
    Calendar cal = new GregorianCalendar();
    cal.add(1, interval);
    return dateToStr(cal.getTime(), format, locale);
  }

  public static String getSpecifyDate(String date, int interval, String format)
  {
    return getSpecifyDate(date, interval, format, Locale.CHINA);
  }

  public static String getSpecifyDate(String date, int interval, String format, Locale locale) {
    Date d = null;
    try {
      d = strToDate(date, "yyyy-MM-dd");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Calendar cal = new GregorianCalendar();
    cal.setTime(d);
    cal.add(5, interval);
    return dateToStr(cal.getTime(), format, locale);
  }

  public static void main(String[] args) {
    try {
      System.out.println(strToDate("15OCT2013", "ddMMMyyy", Locale.ENGLISH));
      System.out.println(getToday());
      System.out.println(dateToStr(new Date(), "ddMMM", Locale.ENGLISH));
      System.out.println(dateToStr(new Date(), "yyyy/dd/MM"));
      System.out.println(getSpecifyDate(2, "ddMMM", Locale.ENGLISH));
      System.out.println(getSpecifyDate(-2, "ddMMM", Locale.ENGLISH));
      System.out.println(getSpecifyMonth(2, "ddMMM", Locale.ENGLISH));
      System.out.println(getSpecifyMonth(-2, "ddMMM", Locale.ENGLISH));
      System.out.println(getSpecifyYear(2, "ddMMMyyyy", Locale.ENGLISH));
      System.out.println(getSpecifyYear(-2, "ddMMMyyyy", Locale.ENGLISH));
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
  }
}