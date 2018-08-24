package com.tarotdt.pas.web.util;

import java.util.List;

public class StringUtils {
  public static void split(String str, char separatorChar, boolean preserveAllTokens,
      List<String> list) {
    list.clear();
    if (str == null)
      return;
    int len = str.length();
    if (len == 0)
      return;
    int i = 0;
    int start = 0;
    boolean match = false;
    boolean lastMatch = false;
    while (i < len)
      if (str.charAt(i) == separatorChar) {
        if ((match) || (preserveAllTokens)) {
          list.add(str.substring(start, i));
          match = false;
          lastMatch = true;
        }
        i++;
        start = i;
      } else {
        lastMatch = false;

        match = true;
        i++;
      }
    if ((match) || ((preserveAllTokens) && (lastMatch))) {
      list.add(str.substring(start, i));
    }
  }

  public static int countChar(String haystack, char needle) {
    int count = 0;
    int len = haystack.length();
    for (int i = 0; i < len; i++) {
      if (haystack.charAt(i) == needle) {
        count++;
      }
    }
    return count;
  }

  public static String nullIfBlank(String in) {
    return org.apache.commons.lang.StringUtils.isBlank(in) ? null : in;
  }
}
