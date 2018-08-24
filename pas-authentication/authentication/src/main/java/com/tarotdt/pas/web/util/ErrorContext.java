package com.tarotdt.pas.web.util;

import java.io.IOException;
import java.util.*;
import org.apache.log4j.NDC;

public class ErrorContext {
  static ThreadLocal<List<String>> chunks = new ThreadLocal<List<String>>();

  public static ACNDC pushWithNDC(String chunk) {
    push(chunk);
    NDC.push(chunk);
    return new ACNDC();
  }

  public static void popWithNDC() {
    pop();
    NDC.pop();
  }

  public static void push(String chunk) {
    if (chunks.get() == null)
      chunks.set(new ArrayList<String>());
    ((List<String>) chunks.get()).add(chunk);
  }

  public static void pop() {
    if (chunks.get() == null)
      chunks.set(new ArrayList<String>());
    if (((List<String>) chunks.get()).size() > 0) {
      ((List<String>) chunks.get()).remove(((List<String>) chunks.get()).size() - 1);
    }
  }

  public static void clear() {
    if (chunks.get() != null)
      ((List<String>) chunks.get()).clear();
  }

  public static String format() {
    if (chunks.get() == null)
      chunks.set(new ArrayList<String>());
    List<String> lchunks = (List<String>) chunks.get();

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < lchunks.size(); i++) {
      sb.append("in " + (String) lchunks.get(i));
      sb.append(": ");
    }
    return sb.toString();
  }

  public static void checkNotEmpty(String obj, String details) {
    if (obj == null) {
      throw iae("Unexpected null value for " + details);
    }
    if (obj.length() == 0) {
      throw iae("Unexpected empty value for " + details);
    }
  }

  public static void checkNotEmpty(Collection<?> col, String message) {
    if ((col == null) || (col.size() == 0)) {
      throw iae(message);
    }
  }

  public static void checkNotEmpty(int[] arr, String message) {
    if ((arr == null) || (arr.length == 0)) {
      throw iae(message);
    }
  }

  public static void checkNotEmpty(double[] arr, String message) {
    if ((arr == null) || (arr.length == 0)) {
      throw iae(message);
    }
  }

  public static void checkNotEmpty(float[] arr, String message) {
    if ((arr == null) || (arr.length == 0)) {
      throw iae(message);
    }
  }

  public static <T> T checkNotNull(T obj) {
    if (obj == null) {
      throw iae("Unexpected null");
    }
    return obj;
  }

  public static <T> T checkNotNull(T obj, String message) {
    if (obj == null) {
      throw iae(message);
    }
    return obj;
  }

  public static void check(boolean x, String message) {
    if (!x) {
      throw iae(message);
    }
  }

  public static IllegalArgumentException iae(String message) {
    return new IllegalArgumentException(format() + message);
  }

  public static IllegalArgumentException iae(String message, Exception cause) {
    return new IllegalArgumentException(format() + message, cause);
  }

  public static IllegalArgumentException iaef(String message, Object... format) {
    return new IllegalArgumentException(format() + String.format(message, format));
  }

  // public static IllegalConfigurationException icef(String message, Object...
  // format) {
  // return new IllegalConfigurationException(format() + String.format(message,
  // format));
  // }

  public static IOException ioException(String message, Object... format) {
    return new IOException(format() + String.format(message, format));
  }

  public static class ACNDC implements AutoCloseable {
    public void close() {
    }
  }
}
