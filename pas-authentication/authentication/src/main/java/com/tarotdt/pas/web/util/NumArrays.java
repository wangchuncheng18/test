package com.tarotdt.pas.web.util;

import java.util.*;
import org.apache.commons.lang.mutable.MutableInt;

public class NumArrays {

  public NumArrays() {
  }

  public static int maxIndex(int array[]) {
    int maxVal = 0x80000000;
    int maxPos = -1;
    for (int i = 0; i < array.length; i++)
      if (array[i] > maxVal) {
        maxVal = array[i];
        maxPos = i;
      }

    return maxPos;
  }

  public static int maxIndex(long array[]) {
    long maxVal = 0xffffffff80000000L;
    int maxPos = -1;
    for (int i = 0; i < array.length; i++)
      if (array[i] > maxVal) {
        maxVal = array[i];
        maxPos = i;
      }

    return maxPos;
  }

  public static int maxIndex(double array[]) {
    double maxVal = -2147483648D;
    int maxPos = -1;
    for (int i = 0; i < array.length; i++)
      if (array[i] > maxVal) {
        maxVal = array[i];
        maxPos = i;
      }

    return maxPos;
  }

  public static int sum(int array[]) {
    int ret = 0;
    for (int i = 0; i < array.length; i++)
      ret += array[i];

    return ret;
  }

  public static long sum(long array[]) {
    long ret = 0L;
    for (int i = 0; i < array.length; i++)
      ret += array[i];

    return ret;
  }

  public static double sum(double array[]) {
    double ret = 0.0D;
    for (int i = 0; i < array.length; i++)
      ret += array[i];

    return ret;
  }

  public static long sumSquare(int array[]) {
    long ret = 0L;
    for (int i = 0; i < array.length; i++)
      ret += (long) array[i] * (long) array[i];

    return ret;
  }

  public static long sumSquare(long array[]) {
    long ret = 0L;
    for (int i = 0; i < array.length; i++)
      ret += array[i] * array[i];

    return ret;
  }

  public static double sumSquare(double array[]) {
    double ret = 0.0D;
    for (int i = 0; i < array.length; i++)
      ret += array[i] * array[i];

    return ret;
  }

  public static int[][] distinctValuesCounts(int array[]) {
    Map<Integer, MutableInt> map = new HashMap<Integer, MutableInt>();
    for (int i = 0; i < array.length; i++) {
      int x = array[i];
      MutableInt v = (MutableInt) map.get(Integer.valueOf(x));
      if (v == null) {
        v = new MutableInt(0);
        map.put(Integer.valueOf(x), v);
      }
      v.increment();
    }

    List<Integer> values = new ArrayList<Integer>();
    values.addAll(map.keySet());
    Collections.sort(values);
    int ret[][] = new int[values.size()][];
    for (int i = 0; i < values.size(); i++) {
      int v = ((Integer) values.get(i)).intValue();
      int count = ((MutableInt) map.get(Integer.valueOf(v))).intValue();
      ret[i] = (new int[] { v, count });
    }

    return ret;
  }
}
