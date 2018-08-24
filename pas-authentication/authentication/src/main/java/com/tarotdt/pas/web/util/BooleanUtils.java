package com.tarotdt.pas.web.util;



public class BooleanUtils {
  public static Boolean toBoolean(String b) {
    String lowerValue = b.toLowerCase();
    if (("yes".startsWith(lowerValue)) || ("true".startsWith(lowerValue)) || ("1".equals(lowerValue)))
    {

      return Boolean.valueOf(true);
    }
    if (("no".startsWith(lowerValue)) || ("false".startsWith(lowerValue)) || ("0".equals(lowerValue)))
    {

      return Boolean.valueOf(false);
    }
    return null;
  }
  
  public static Boolean toBoolean(String b, Boolean defaultValue) {
    Boolean res = toBoolean(b);
    if (res == null) {
      return defaultValue;
    }
    
    return res;
  }
  
  public static boolean toboolean(String b) {
    Boolean res = toBoolean(b);
    return (res != null) && (res.booleanValue());
  }
}
