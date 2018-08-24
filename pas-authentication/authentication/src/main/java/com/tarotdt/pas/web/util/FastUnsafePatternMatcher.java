package com.tarotdt.pas.web.util;

import java.util.regex.Matcher;

public class FastUnsafePatternMatcher {
  Matcher m = null;

  public FastUnsafePatternMatcher(java.util.regex.Pattern compiled) {
    this.m = compiled.matcher("");
  }

  public FastUnsafePatternMatcher(String pattern) {
    this.m = java.util.regex.Pattern.compile(pattern).matcher("");
  }

  public boolean matches(String v) {
    this.m.reset(v);
    return this.m.matches();
  }

  public boolean find(String v) {
    this.m.reset(v);
    return this.m.find();
  }

  public Matcher matcher(String v) {
    this.m.reset(v);
    return this.m;
  }
}
