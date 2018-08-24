package com.tarotdt.pas.web.util;

import java.sql.SQLException;

public class ExceptionUtils {
  public static String getStackTrace(Throwable t) {
    java.io.StringWriter stringWriter = new java.io.StringWriter();
    java.io.PrintWriter printWriter = new java.io.PrintWriter(stringWriter, true);
    t.printStackTrace(printWriter);
    printWriter.flush();
    stringWriter.flush();
    return stringWriter.toString();
  }

  public static String formatMessage(Throwable t) {
    return t.getClass() + ": " + t.getMessage();
  }

  public static String getMessageWithCauses(Throwable t) {
    StringBuilder sb = new StringBuilder();
    if (t.getMessage() == null) {
      sb.append(t.getClass().getSimpleName());
    } else {
      if ((t instanceof NumberFormatException)) {
        sb.append(t.getClass().getSimpleName() + ": ");
      }
      sb.append(t.getMessage());
    }
    t = t.getCause();
    while (t != null) {
      sb.append(", caused by: " + t.getClass().getSimpleName() + ": " + t.getMessage());
      t = t.getCause();
    }
    return sb.toString();
  }

  public static String getMessageWithSQLNest(SQLException t) {
    StringBuilder sb = new StringBuilder();
    if (t.getMessage() == null) {
      sb.append(t.getClass().getSimpleName());
    } else {
      sb.append(t.getMessage());
    }
    t = t.getNextException();
    while (t != null) {
      sb.append(", caused by: " + t.getClass().getSimpleName() + ": " + t.getMessage());
      t = t.getNextException();
    }
    return sb.toString();
  }

  public static SQLException newSQLExceptionWithNestedMessages(SQLException e) {
    if (e.getNextException() == null) {
      return e;
    }
    return new SQLException(getMessageWithSQLNest(e));
  }
}
