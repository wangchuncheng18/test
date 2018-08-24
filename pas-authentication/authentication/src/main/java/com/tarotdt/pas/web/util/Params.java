package com.tarotdt.pas.web.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.*;

public class Params {
  KeyValue rootKV;
  protected Map<String, String> params;
  private Map<String, KeyValue> intermediate;

  public static class KeyValue {

    public void setValue(String value) {
      this.value = value;
    }

    public String key() {
      return key;
    }

    public String value() {
      return value;
    }

    public String toString() {
      return (new StringBuilder()).append("[KV k=").append(key).append(" v=").append(value)
          .append(" children=").append(getChildren().size()).append("]").toString();
    }

    public KeyValue getChild(String childKey) {
      for (Iterator<KeyValue> i$ = getChildren().iterator(); i$.hasNext();) {
        KeyValue kv = (KeyValue) i$.next();
        if (kv.key().equals(childKey))
          return kv;
      }

      return null;
    }

    public List<KeyValue> getChildren() {
      if (children == null)
        children = new ArrayList<KeyValue>();
      return children;
    }

    public List<String> childrenKeys(String prefix, boolean includeSelfKey) {
      List<String> o = new ArrayList<String>();
      String name;
      for (KeyValue child: getChildren()) {
        name = child.key;
        if (includeSelfKey)
          name = (new StringBuilder()).append(key).append(".").append(name).toString();
        if (prefix != null)
          name = (new StringBuilder()).append(prefix).append(".").append(name).toString();
      	o.add(name);
      }

      return o;
    }

    private String key;
    private String value;
    private List<KeyValue> children;

    public KeyValue() {
    }

    public KeyValue(String key) {
      this.key = key;
    }

    public KeyValue(String key, String value) {
      this.key = key;
      this.value = value;
    }
  }

  public Params() {
    rootKV = new KeyValue();
    params = new HashMap<String, String>();
    intermediate = new HashMap<String, KeyValue>();
  }

  @SuppressWarnings("rawtypes")
  public Params(Map<?, ?> input) {
    rootKV = new KeyValue();
    params = new HashMap<String, String>();
    intermediate = new HashMap<String, KeyValue>();
    for (Map.Entry ie: input.entrySet()) {
    	add((String) ie.getKey(), (String) ie.getValue());
    }
  }

  public Map<String, String> getAll() {
    return ImmutableMap.copyOf(params);
  }

  private void addRec(KeyValue prevKV, String chunks[], int curIdx, String value,
      String curPrefix) {
    String curChunk = chunks[curIdx];
    if (!curPrefix.isEmpty())
      curPrefix = (new StringBuilder()).append(curPrefix).append('.').toString();
    curPrefix = (new StringBuilder()).append(curPrefix).append(curChunk).toString();
    KeyValue kv = prevKV.getChild(curChunk);
    if (kv == null) {
      kv = new KeyValue(curChunk);
      intermediate.put(curPrefix, kv);
      prevKV.children.add(kv);
    }
    if (curIdx + 1 == chunks.length) {
      kv.setValue(value);
      params.put(curPrefix, value);
    } else {
      addRec(kv, chunks, curIdx + 1, value, curPrefix);
    }
  }

  public void add(String key, String value) {
    String chunks[] = key.split("\\.");
    addRec(rootKV, chunks, 0, value, "");
  }

  public void add(String key, long value) {
    add(key, Long.toString(value));
  }

  public void add(String key, double value) {
    add(key, Double.toString(value));
  }

  public Params with(String key, String value) {
    add(key, value);
    return this;
  }

  public Params with(String key, long value) {
    add(key, value);
    return this;
  }

  public Params with(String key, double value) {
    add(key, value);
    return this;
  }

  public boolean hasParam(String name) {
    return params.containsKey(name);
  }

  public String getParam(String name) {
    return (String) params.get(name);
  }

  public String getParam(String name, String defaultValue) {
    return getParam(name, defaultValue, false);
  }

  public String getParam(String name, String defaultValue, boolean translateEmptyToNull) {
    String s = (String) params.get(name);
    if (s == null)
      return defaultValue;
    if (translateEmptyToNull && s.isEmpty())
      return defaultValue;
    else
      return s;
  }

  public String getParamOrEmpty(String name) {
    return getParam(name, "");
  }

  public String getMandParam(String name) {
    String s = (String) params.get(name);
    if (s == null)
      throw ErrorContext
          .iae((new StringBuilder()).append("Missing param '").append(name).append("'").toString());
    else
      return s;
  }

  public String getNonEmptyMandParam(String name) {
    String s = getMandParam(name);
    if (s.isEmpty())
      throw ErrorContext
          .iae((new StringBuilder()).append("Empty param '").append(name).append("'").toString());
    else
      return s;
  }

  public boolean getBoolParam(String name, boolean defaultValue) {
    String s = (String) params.get(name);
    if (s == null)
      return defaultValue;
    return s.equalsIgnoreCase("true") || s.startsWith("y") || s.startsWith("Y");
  }

  public int getIntParam(String name, Integer defaultValue) {
    String s = (String) params.get(name);
    if (s == null)
      return defaultValue.intValue();
    else
      return Integer.parseInt(s);
  }

  public int getIntParam(String name) {
    String s = getMandParam(name);
    return Integer.parseInt(s);
  }

  public short getShortParam(String name, Short defaultValue) {
    String s = (String) params.get(name);
    if (s == null)
      return defaultValue.shortValue();
    else
      return Short.parseShort(s);
  }

  public short getShortParam(String name) {
    String s = getMandParam(name);
    return Short.parseShort(s);
  }

  public int getUShortParam(String name, Integer defaultValue) {
    String s = (String) params.get(name);
    if (s == null)
      return defaultValue.intValue();
    int res = Integer.parseInt(s);
    if (!ushortBoundsCheck(res))
      throw new NumberFormatException(
          (new StringBuilder()).append(s).append(" is not an unsigned short.").toString());
    else
      return res;
  }

  public int getUShortParam(String name) {
    return getUShortParam(name, null);
  }

  private boolean ushortBoundsCheck(int i) {
    return i >= 0 && i <= 65535;
  }

  public long getLongParam(String name, long defaultValue) {
    String s = (String) params.get(name);
    if (s == null)
      return defaultValue;
    else
      return Long.parseLong(s);
  }

  public long getLongParam(String name) {
    String s = getMandParam(name);
    return Long.parseLong(s);
  }

  public double getDoubleParam(String name, long defaultValue) {
    String s = (String) params.get(name);
    if (s == null)
      return (double) defaultValue;
    else
      return Double.parseDouble(s);
  }

  public double getDoubleParam(String name) {
    String s = getMandParam(name);
    return Double.parseDouble(s);
  }

  public char getCharParam(String name) {
    getMandParam(name);
    return getCharParam(name, null);
  }

  public char getCharParam(String name, Character defaultValue) {
    String s = getParam(name, (new StringBuilder()).append("").append(defaultValue).toString());
    if (s.length() != 1) {
      if (s.startsWith("\\u"))
        try {
          int code = Integer.parseInt(s.substring(2), 16);
          return (char) code;
        } catch (Exception e) {
          throw exceptSingleChar(name);
        }
      if (s.startsWith("u") && s.length() == 5)
        try {
          int code = Integer.parseInt(s.substring(1), 16);
          return (char) code;
        } catch (Exception e) {
          throw exceptSingleChar(name);
        }
      else
        throw exceptSingleChar(name);
    } else {
      return s.charAt(0);
    }
  }

  private static IllegalArgumentException exceptSingleChar(String param) {
    return ErrorContext.iae((new StringBuilder()).append("Expected single char in param '")
        .append(param).append("'").toString());
  }

  public List<String> getCSVParamAsList(String name, String defaultValue, String separator) {
    List<String> out = new ArrayList<String>();
    String arr[] = getCSVParam(name, defaultValue, separator);
    int len = arr.length;
    for (int i = 0; i < len; i++) {
      String cell = arr[i];
      out.add(cell);
    }

    return out;
  }

  public static List<String> getFromCSVList(String value, String defaultValue, String separator) {
    if (value == null && defaultValue != null)
      value = defaultValue;
    if (value == null)
      return new ArrayList<String>();
    else
      return Lists.newArrayList(value.split(separator));
  }

  public static List<Integer> getFromCSVIntList(String value, String defaultValue,
      String separator) {
    if (value == null && defaultValue != null)
      value = defaultValue;
    if (value == null)
      return new ArrayList<Integer>();
    List<Integer> out = new ArrayList<Integer>();
    String arr[] = value.split(separator);
    for (int i = 0; i < arr.length; i++) {
      String x = arr[i];
      out.add(Integer.valueOf(Integer.parseInt(x)));
    }

    return out;
  }

  public List<String> getCSVParamAsList(String name, String defaultValue) {
    return getCSVParamAsList(name, defaultValue, ",");
  }

  public List<String> getCSVParamAsList(String name) {
    return getCSVParamAsList(name, null);
  }

  public String[] getCSVParam(String name, String defaultValue) {
    return getCSVParam(name, defaultValue, ",");
  }

  public String[] getCSVParam(String name, String defaultValue, String separator) {
    String v = getParam(name, defaultValue);
    if (v == null)
      return new String[0];
    else
      return v.split(separator);
  }

  public String[] getCSVParam(String name) {
    return getCSVParam(name, null);
  }

  public List<Integer> getCSVParamAsIntList(String name, String defaultValue) {
    String v = getParam(name, defaultValue);
    String chunks[] = v.split(",");
    List<Integer> out = new ArrayList<Integer>();
    for (int i = 0; i < chunks.length; i++) {
      String s = chunks[i];
      if (!s.isEmpty()) {
        out.add(Integer.valueOf(Integer.parseInt(s)));
      }
    }

    return out;
  }

  public KeyValue getAsKV(String name) {
    return (KeyValue) intermediate.get(name);
  }

  public String get(String prefix, String suffix) {
    return getParam((new StringBuilder()).append(prefix).append(".").append(suffix).toString());
  }

  public List<String> getChildrenAsIntList(String prefix) {
    KeyValue kv = (KeyValue) intermediate.get(prefix);
    if (kv == null)
      return null;
    List<String> validList = new ArrayList<String>();
    int i = 0;
    do {
      KeyValue child = kv.getChild((new StringBuilder()).append("").append(i).toString());
      if (child == null)
        break;
      validList.add((new StringBuilder()).append("").append(i).toString());
      i++;
    } while (true);
    for (KeyValue child: kv.getChildren()) {
      String childKey = child.key;
      if (!validList.contains(childKey)) {
        throw new IllegalArgumentException((new StringBuilder()).append("In prefix ").append(prefix)
            .append(", key ").append(childKey).append(" does not follow a valid list").toString());
      }
    }

    Collections.sort(validList, new Comparator<Object>() {

      public int compare(String o1, String o2) {
        int i1 = Integer.parseInt(o1);
        int i2 = Integer.parseInt(o2);
        return i1 <= i2 ? i1 >= i2 ? 0 : -1 : 1;
      }

      public int compare(Object obj, Object obj1) {
        return compare((String) obj, (String) obj1);
      }
    });
    return validList;
  }

  public List<String> childrenFullNames(String prefix) {
    KeyValue kv = (KeyValue) intermediate.get(prefix);
    if (kv == null)
      return null;
    List<String> o = new ArrayList<String>();
    for (KeyValue child: kv.getChildren()) {
      o.add((new StringBuilder()).append(prefix).append(".").append(child.key()).toString());
    }

    return o;
  }

  public static char getAsChar(String s) {
    if (s.length() != 1) {
      if (s.startsWith("\\u"))
        try {
          int code = Integer.parseInt(s.substring(2), 16);
          return (char) code;
        } catch (Exception e) {
          throw ErrorContext.iaef("Expect a single char but got '%s'", new Object[] { s });
        }
      if (s.startsWith("u") && s.length() == 5)
        try {
          int code = Integer.parseInt(s.substring(1), 16);
          return (char) code;
        } catch (Exception e) {
          throw ErrorContext.iaef("Expect a single char but got '%s'", new Object[] { s });
        }
      if (s.equals("\\t"))
        return '\t';
      else
        throw ErrorContext.iaef("Expect a single char but got '%s'", new Object[] { s });
    } else {
      return s.charAt(0);
    }
  }

}
