package com.tarotdt.pas.web.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FastSafePatternMatcher {
  ThreadLocal<Matcher> m = new ThreadLocal<Matcher>();
  java.util.regex.Pattern compiled;

  public FastSafePatternMatcher(Pattern compiled) {
    this.compiled = compiled;
  }

  public boolean matches(String v) {
    Matcher matcher = (Matcher) this.m.get();
    if (matcher == null) {
      matcher = this.compiled.matcher(v);
      this.m.set(matcher);
      return matcher.matches();
    }
    matcher.reset(v);
    return matcher.matches();
  }

  public boolean find(String v) {
    Matcher matcher = (Matcher) this.m.get();
    if (matcher == null) {
      matcher = this.compiled.matcher(v);
      this.m.set(matcher);
      return matcher.find();
    }
    matcher.reset(v);
    return matcher.find();
  }

  public Matcher getMatcher(String v) {
    Matcher matcher = (Matcher) this.m.get();
    if (matcher == null) {
      matcher = this.compiled.matcher(v);
      this.m.set(matcher);
    } else {
      matcher.reset(v);
    }
    return matcher;
  }
}
