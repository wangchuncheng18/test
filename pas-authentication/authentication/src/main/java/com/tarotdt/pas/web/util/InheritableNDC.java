package com.tarotdt.pas.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.apache.log4j.NDC;

public class InheritableNDC {
  private static InheritableThreadLocal<List<Object>> itl = new InheritableThreadLocal<List<Object>>();

  public static void inheritNDC() {
    if (itl.get() != null) {
      for (Object s : (List<?>) itl.get()) {
        if (s.getClass().getName().contains("Stack")) {
          NDC.inherit((Stack<?>) s);
        } else {
          NDC.push(s.toString());
        }
      }
    }
  }

  public static void pushAll(Stack<?> stack) {
    if (stack != null) {
      push(stack);
    }
  }

  public static void push(Object chunk) {
    if (itl.get() == null) {
      itl.set(new ArrayList<Object>());
    }
    ((List<Object>) itl.get()).add(chunk);
  }

  public static void pop() {
    if (itl.get() == null) {
      itl.set(new ArrayList<Object>());
    }
    if (((List<?>) itl.get()).size() > 0) {
      ((List<?>) itl.get()).remove(((List<?>) itl.get()).size() - 1);
    }
  }
}
