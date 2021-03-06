package com.tarotdt.pas.web.util;

public class PrettyString {
  public static boolean isInteractif() {
    return System.console() != null;
  }

  public static boolean displayWithColor() {
    return (isInteractif()) || (System.getenv("CLICOLOR_FORCE") != null);
  }

  public static String quoted(Object msg) {
    return "\"" + msg.toString() + "\"";
  }

  public static String squoted(Object msg) {
    return "'" + msg.toString() + "'";
  }

  public static String pquoted(Object msg) {
    return "`" + msg.toString() + "'";
  }

  public static String nl(String... msgs) {
    StringBuilder sb = new StringBuilder();
    for (String msg : msgs) {
      sb.append(msg);
      sb.append(eol());
    }

    return sb.toString();
  }

  public static String eol() {
    return System.getProperty("line.separator");
  }

  protected static String cat(String concatenateStr, Object... msg) {
    StringBuilder b = new StringBuilder();
    if (msg.length > 0) {
      boolean first = true;
      for (int i = 0; i < msg.length; i++) {
        String m = msg[i].toString();
        if (!m.isEmpty()) {
          if (first) {
            first = false;
          } else {
            b.append(concatenateStr);
          }
          b.append(m);
        }
      }
    }
    return b.toString();
  }

  public static String scat(Object... msg) {
    return cat(" ", msg);
  }

  public static String nlcat(Object... msg) {
    return cat(eol(), msg);
  }

  public static String format(String str, String color) {
    if (displayWithColor()) {
      return "\033[" + color + "m" + str + "\033[0m";
    }

    return str;
  }

  public static String red(String str) {
    return format(str, "31");
  }
}
