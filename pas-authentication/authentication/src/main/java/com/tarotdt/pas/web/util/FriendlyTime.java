package com.tarotdt.pas.web.util;



import java.util.Date;

public class FriendlyTime {
  public static String friendlyDelayFromNow(long epoch) {
    return friendlyDelta(new Date(epoch), new Date());
  }
  
  public static String friendlyDelayFromNow(Date dateTime) { return friendlyDelta(dateTime, new Date()); }
  
  public static String friendlyDelta(Date dateTime, Date current)
  {
    StringBuffer sb = new StringBuffer();
    long diffInSeconds = (current.getTime() - dateTime.getTime()) / 1000L;
    
    long sec = diffInSeconds >= 60L ? diffInSeconds % 60L : diffInSeconds;
    long min = diffInSeconds /= 60L >= 60L ? diffInSeconds % 60L : diffInSeconds;
    long hrs = diffInSeconds /= 60L >= 24L ? diffInSeconds % 24L : diffInSeconds;
    long days = diffInSeconds /= 24L >= 30L ? diffInSeconds % 30L : diffInSeconds;
    long months = diffInSeconds /= 30L >= 12L ? diffInSeconds % 12L : diffInSeconds;
    long years = diffInSeconds /= 12L;
    
    if (years > 0L) {
      if (years == 1L) {
        sb.append("one year");
      } else {
        sb.append(years + " years");
      }
      if ((years <= 6L) && (months > 0L)) {
        if (months == 1L) {
          sb.append(" and one month");
        } else {
          sb.append(" and " + months + " months");
        }
      }
    } else if (months > 0L) {
      if (months == 1L) {
        sb.append("one month");
      } else {
        sb.append(months + " months");
      }
      if ((months <= 6L) && (days > 0L)) {
        if (days == 1L) {
          sb.append(" and a day");
        } else {
          sb.append(" and " + days + " days");
        }
      }
    } else if (days > 0L) {
      if (days == 1L) {
        sb.append("one day");
      } else {
        sb.append(days + " days");
      }
      if ((days <= 3L) && (hrs > 0L)) {
        if (hrs == 1L) {
          sb.append(" and one hour");
        } else {
          sb.append(" and " + hrs + " hours");
        }
      }
    } else if (hrs > 0L) {
      if (hrs == 1L) {
        sb.append("one hour");
      } else {
        sb.append(hrs + " hours");
      }
      if (min > 1L) {
        sb.append(" and " + min + " minutes");
      }
    } else if (min > 0L) {
      if (min == 1L) {
        sb.append("one minute");
      } else {
        sb.append(min + " minutes");
      }
      if (sec > 1L) {
        sb.append(" and " + sec + " seconds");
      }
    }
    else if (sec <= 1L) {
      sb.append("about a second");
    } else {
      sb.append("about " + sec + " seconds");
    }
    

    sb.append(" ago");
    











    return sb.toString();
  }
  
  public static String elapsedTime(long time) { String res = "";
    for (int i = 0; i < 3; i++) {
      if (time == 0L) {
        if (i >= 2) break;
        res = "0" + unit[i] + res; break;
      }
      

      long t = time % div[i];
      time /= div[i];
      res = "" + t + unit[i] + res;
    }
    
    return res; }
  
  private static int[] div = { 60, 60, 24, 7, 4, 12 };
  private static String[] unit = { "s", "m", "h", "w", "m", "y" };
}
