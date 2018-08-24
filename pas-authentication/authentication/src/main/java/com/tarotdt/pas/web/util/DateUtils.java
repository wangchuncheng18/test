package com.tarotdt.pas.web.util;

import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;

public class DateUtils {
  public static final String ISO_FORMAT_LOCAL = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

  public static String isoFormatUTC(long ts) {
    return org.joda.time.format.ISODateTimeFormat.dateHourMinuteSecondMillis().withZone(DateTimeZone.UTC).print(ts) + "Z";
  }

  public static String formatUTC(long ts, String format) {
    return DateTimeFormat.forPattern(format).withZone(DateTimeZone.UTC).print(ts);
  }

  public static String isoFormatUTCNow() {
    return isoFormatUTC(System.currentTimeMillis());
  }

  public static String isoFormatLocal(long ts) {
    return DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").withZone(DateTimeZone.getDefault()).print(ts);
  }

  public static String isoFormatFileFriendlyLocal(long ts) {
    return DateTimeFormat.forPattern("yyyy-MM-dd-HH-mm-ss-SSS").withZone(DateTimeZone.getDefault()).print(ts);
  }

  public static String isoFormatLocalNow() {
    return isoFormatLocal(System.currentTimeMillis());
  }

  public static String isoFormatFileFriendlyLocalNow() {
    return isoFormatFileFriendlyLocal(System.currentTimeMillis());
  }

  public static int daysBetweenUTC(long start, long end) {
    return daysBetween(start, end, DateTimeZone.UTC);
  }

  public static int daysBetweenLocal(long start, long end) {
    return daysBetween(start, end, DateTimeZone.getDefault());
  }

  public static int daysBetween(long start, long end, DateTimeZone tz) {
    return Days.daysBetween(new org.joda.time.LocalDate(start, tz), new org.joda.time.LocalDate(end, tz)).getDays();
  }
}
