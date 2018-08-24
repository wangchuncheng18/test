package com.tarotdt.pas.web.util;

import java.util.HashSet;
import java.util.Set;

public class StringTransmogrifier {
  private Set<String> assigned = new HashSet();
  private String delimiter = "_";

  public StringTransmogrifier() {
  }

  public StringTransmogrifier(String delimiter) {
    this.delimiter = delimiter;
  }

  public void addAlreadyTransmogrified(String in) {
    if (this.assigned.contains(in)) {
      throw new IllegalArgumentException(
          "Input string " + in + " is already in the transmogrifier");
    }
    this.assigned.add(in);
  }

  public void addAlreadyTransmogrifiedAcceptDupes(String in) {
    this.assigned.add(in);
  }

  public String transmogrify(String input) {
    String cur = input;
    int i = 0;
    while (this.assigned.contains(cur)) {
      cur = input + this.delimiter + ++i;
    }
    this.assigned.add(cur);
    return cur;
  }
}
