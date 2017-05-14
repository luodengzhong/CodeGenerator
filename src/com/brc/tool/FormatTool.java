 package com.brc.tool;
 
 import java.io.PrintStream;
 import java.text.DateFormat;
 import java.text.DecimalFormat;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import java.util.Date;
 
 public class FormatTool
 {
   public static final int H_M = 0;
   public static final int H_M_S = 1;
   public static final int Y_M_D = 2;
   public static final int Y_M_D_H_M = 3;
   public static final int Y_M_D_H_M_S = 4;
   public static final int MM = 5;
   public static final int yyyyMMdd = 6;
   public static final int MMdd = 7;
   public static final int MM_DD_YYYY = 8;
   public static final int DD_MM_YYYY = 9;
   private static SimpleDateFormat dateformat0 = new SimpleDateFormat("HH:mm");
   private static SimpleDateFormat dateformat1 = new SimpleDateFormat("HH:mm:ss");
   private static SimpleDateFormat dateformat2 = new SimpleDateFormat("yyyy-MM-dd");
   private static SimpleDateFormat dateformat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
   private static SimpleDateFormat dateformat4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   private static SimpleDateFormat dateformat5 = new SimpleDateFormat("MM");
   private static SimpleDateFormat dateformat6 = new SimpleDateFormat("yyyyMMdd");
   private static SimpleDateFormat dateformat7 = new SimpleDateFormat("MM-dd");
   private static SimpleDateFormat dateformat8 = new SimpleDateFormat("MM-dd-yyyy");
   private static SimpleDateFormat dateformat9 = new SimpleDateFormat("dd-MM-yyyy");
   public static final double RESERVEONE = 10.0D;
   public static final double RESERVETWO = 100.0D;
   public static final double RESERVETHREE = 1000.0D;
   public static final double RESERVEFOUR = 10000.0D;
   public static final String FORMATTWO = "###.00";
   public static final String FORMATTHREE = "##.000";
   public static final String FORMATFOUR = "##.0000";
   public static final String FORMAT_SPLIT = "#,###,###";
   public static final String FORMAT_TWO_SPLIT = "#,###,###.00";
 
   private static SimpleDateFormat getFormat(int num)
   {
     SimpleDateFormat dateformat = null;
     switch (num) {
     case 0:
       dateformat = dateformat0;
       break;
     case 1:
       dateformat = dateformat1;
       break;
     case 2:
       dateformat = dateformat2;
       break;
     case 3:
       dateformat = dateformat3;
       break;
     case 4:
       dateformat = dateformat4;
       break;
     case 5:
       dateformat = dateformat5;
       break;
     case 6:
       dateformat = dateformat6;
       break;
     case 7:
       dateformat = dateformat7;
       break;
     case 8:
       dateformat = dateformat8;
       break;
     case 9:
       dateformat = dateformat9;
       break;
     }
 
     return dateformat;
   }
 
   public static String numberFormat(String number, String splitor, int step)
   {
     number = number.toString();
     int len = number.length();
     if (len > step) {
       int l1 = len % step;
       int l2 = len / step;
       StringBuffer arr = new StringBuffer();
       String first = number.substring(0, l1);
       if (!"".equals(first)) {
         arr.append(first);
       }
       for (int i = 0; i < l2; i++) {
         arr.append(number.substring(l1 + i * step, step));
       }
       if (number.indexOf(splitor) == -1) {
         number = splitor;
       }
     }
     return number;
   }
 
   public static double numberFormat(double number, double reserveNumber)
   {
     int numberDTI = (int)reserveNumber;
     return Math.round(number * numberDTI) / reserveNumber;
   }
 
   public static String numberFormat(double number, String formatType)
   {
     DecimalFormat df = new DecimalFormat(formatType);
     return df.format(number);
   }
 
   public static long getFormatLong(int num, String stringDate)
     throws ParseException
   {
     return getFormatDate(num, stringDate).getTime();
   }
 
   public static Date getDateByYearAndMonthAndDay(int year, int month, int day)
   {
     Calendar calendar = Calendar.getInstance();
     calendar.set(1, year);
     calendar.set(2, month);
     calendar.set(5, day);
     return calendar.getTime();
   }
 
   public static Date getFormatDate(int num, String stringDate)
     throws ParseException
   {
     Date resultDate = null;
     SimpleDateFormat dateformat = getFormat(num);
     if (dateformat != null)
       resultDate = dateformat.parse(stringDate);
     return resultDate;
   }
 
   public static int getDepartOfDate(Date date, String type)
   {
     SimpleDateFormat sdf = new SimpleDateFormat(
       "yyyy/MM/dd/HH/mm/ss/w/W/D/E/d");
     String dateString = sdf.format(date);
     String[] args = dateString.split("/");
 
     if ("YEAR".equalsIgnoreCase(type)) {
       return Integer.parseInt(args[0]);
     }
     if ("MONTH".equalsIgnoreCase(type)) {
       return Integer.parseInt(args[1]);
     }
     if ("DAY".equalsIgnoreCase(type)) {
       return Integer.parseInt(args[2]);
     }
     if ("HOUR".equalsIgnoreCase(type)) {
       return Integer.parseInt(args[3]);
     }
     if ("MINUTE".equalsIgnoreCase(type)) {
       return Integer.parseInt(args[4]);
     }
     if ("SECOND".equalsIgnoreCase(type)) {
       return Integer.parseInt(args[5]);
     }
     if ("WEEKOFYEAR".equalsIgnoreCase(type)) {
       return Integer.parseInt(args[6]);
     }
     if ("WEEKOFMONTH".equalsIgnoreCase(type)) {
       return Integer.parseInt(args[7]);
     }
     if ("DAYOFYEAR".equalsIgnoreCase(type)) {
       return Integer.parseInt(args[8]);
     }
     return -1;
   }
 
   public static String getFormatString(int num, Date date)
   {
     if (date == null) {
       return "0000-00-00";
     }
     SimpleDateFormat dateformat = getFormat(num);
     return dateformat == null ? "0000-00-00" : dateformat.format(date);
   }
 
   public static long timediff(String type, Date begin, Date end)
   {
     long diff = end.getTime() - begin.getTime();
     if (type.equals("day")) {
       diff /= 86400000L;
     } else if (type.equals("hour")) {
       diff /= 3600000L;
     } else if (type.equals("minute")) {
       diff /= 60000L;
     } else if (type.equals("month")) {
       int startmonth = Integer.parseInt(getFormatString(5, begin));
       int endmonth = Integer.parseInt(getFormatString(5, end));
       diff = endmonth - startmonth;
     }
     return diff;
   }
 
   public static long timediff(String type, int num, String begin, String end)
     throws ParseException
   {
     Date sday = getFormatDate(num, begin);
     Date eday = getFormatDate(num, end);
     return timediff(type, sday, eday);
   }
 
   public static String formatString(int num, String oldformat, String strdate)
     throws ParseException
   {
     String datestring = "";
     if (!"".equals(strdate)) {
       DateFormat df1 = getFormat(num);
       DateFormat df2 = new SimpleDateFormat(oldformat);
       datestring = df1.format(df2.parse(strdate));
     }
     return datestring;
   }
 
   public static String getCurrentDate(int num)
   {
     Date date = new Date();
     DateFormat df = getFormat(num);
     String currentDate = df.format(date);
     return currentDate;
   }
 
   public static String getTomorrowDate(int num)
     throws ParseException
   {
     Calendar cal = Calendar.getInstance();
     Date date = new Date();
     SimpleDateFormat format = getFormat(num);
     date = format.parse(getCurrentDate(num));
     cal.setTime(date);
     cal.add(5, 1);
     return format.format(cal.getTime());
   }
 
   public static String getYesterday(int num)
     throws ParseException
   {
     Calendar cal = Calendar.getInstance();
     Date date = new Date();
     SimpleDateFormat format = getFormat(num);
     date = format.parse(getCurrentDate(num));
     cal.setTime(date);
     cal.add(5, -1);
     return format.format(cal.getTime());
   }
 
   public static String getNextYearDate(int num)
     throws ParseException
   {
     Calendar cal = Calendar.getInstance();
     Date date = new Date();
     SimpleDateFormat format = getFormat(num);
     date = format.parse(getCurrentDate(num));
     cal.setTime(date);
     cal.add(1, 1);
     cal.add(5, -1);
     return format.format(cal.getTime());
   }
 
   public static String getNotTodayDate(int num, int year, int month, int day)
     throws ParseException
   {
     Calendar cal = Calendar.getInstance();
     Date date = new Date();
     SimpleDateFormat format = getFormat(num);
     date = format.parse(getCurrentDate(num));
     cal.setTime(date);
     cal.add(1, year);
     cal.add(2, month);
     cal.add(5, day);
     return format.format(cal.getTime());
   }
 
   public static void main(String[] args)
     throws ParseException
   {
     System.out.println(getYesterday(2));
     System.out.println(getTomorrowDate(2));
     System.out.println(getNextYearDate(2));
   }
 }

