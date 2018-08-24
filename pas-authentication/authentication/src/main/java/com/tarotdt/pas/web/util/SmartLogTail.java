package com.tarotdt.pas.web.util;

import java.util.List;

public class SmartLogTail {
  public static final int STATUS_DEBUG = 0;
  public static final int STATUS_INFO = 1;
  public static final int STATUS_WARNING = 2;

  public SmartLogTail() {
    this.lines = new java.util.ArrayList();
    this.status = new java.util.ArrayList();

    this.parseStatus = new ParseStatus();
  }

  public static final int STATUS_ERROR = 3;
  public List<String> lines;
  public List<Integer> status;
  private transient ParseStatus parseStatus;

  public List<String> getLines() {
    return this.lines;
  }

  public List<Integer> getStatus() {
    return this.status;
  }

  public void appendLine(String line) {
    this.lines.add(line);
    this.status.add(getSmartStatus(line, this.parseStatus));
  }

  static Integer getSmartStatus(String line, ParseStatus parseStatus) {
    int status = 1;

    if ((line.contains("Exception")) || (line.startsWith("\tat "))) {
      status = 3;
    }

    if (line.contains(" - ERROR")) {
      status = 3;
    }

    if (line.contains("- Error in ")) {
      status = 3;
    }
    if (line.contains("R code failed")) {
      status = 3;
    }
    if (line.contains("Execution halted")) {
      status = 3;
    }

    if ((line.contains("WARN  org.apache.hadoop.conf.Configuration "))
        && (line.contains("is deprecated"))) {
      status = 0;
    }

    if (line.contains("Recipe code failed")) {
      status = 3;
    }

    if (line.contains("Begin Python stack")) {
      parseStatus.inPythonStack = true;
    }
    if (line.contains("End Python stack")) {
      parseStatus.inPythonStack = false;
    }
    if (parseStatus.inPythonStack) {
      status = 3;
    }

    if (line.contains("Traceback ")) {
      status = 3;
    }
    if ((line.contains("NameError")) || (line.contains("TypeError"))) {
      status = 3;
    }

    if (line.contains("] [ERROR] [")) {
      status = 3;
    }
    if (line.contains("] [WARN] [")) {
      status = 2;
    }
    if (line.contains("] [DEBUG] [")) {
      status = 0;
    }

    if ((line.contains("[INFO] [pas.utils]")) && (line.contains(" DEBUG "))) {
      status = 0;
    }
    if ((line.contains("[INFO] [pas.utils]")) && (line.contains(" ERROR "))) {
      status = 3;
    }
    if ((line.contains("[INFO] [pas.utils]")) && (line.contains(" WARNING "))) {
      status = 2;
    }
    if ((line.contains("[INFO] [pas.utils]")) && (line.contains("- \tat "))) {
      status = 3;
    }

    return Integer.valueOf(status);
  }

  private static class ParseStatus {
    boolean inPythonStack;
  }
}
