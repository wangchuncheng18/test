package com.tarotdt.pas.web.util;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexpFieldsMatcher {
  private List<String> regexpChunks = new ArrayList<String>();
  private Pattern compiled;
  private List<String> columnNames = new ArrayList<String>();

  public static final String HOSTNAME = "[A-z0-9\\.\\-_]*";
  public static final String IP = "[0-9abcdef:\\.\\[\\]]*";
  public static final String ESCAPED_URL = "[A-z0-9_\\-\\[\\]&@#%+()]*";
  public static final String QUOTTED_NO_ESCAPE = "\"([^\"]*)\"";
  public static final String INTEGER = "[-0-9]+";
  public static final String DOUBLE = "[-0-9\\.]+";
  private boolean autoAddSpace;

  public RegexpFieldsMatcher(boolean autoAddSpace) {
    this.autoAddSpace = autoAddSpace;
  }

  private String buildRegexp(int nbChunks) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < nbChunks; i++) {
      if ((this.autoAddSpace) && (i > 0))
        sb.append(' ');
      sb.append((String) this.regexpChunks.get(i));
    }
    return sb.toString();
  }

  public String regexp() {
    return buildRegexp(this.regexpChunks.size());
  }

  public void captureInt(String column) {
    capture(column, "[0-9]*");
  }

  public void capture(String column, String pattern) {
    this.regexpChunks.add("(" + pattern + ")");
    this.columnNames.add(column);
  }

  public void customCapture(String column, String patternWithCapture) {
    this.regexpChunks.add(patternWithCapture);
    this.columnNames.add(column);
  }

  public void space() {
    match(" ");
  }

  public void spaces() {
    match("\\s+");
  }

  public void match(String pattern) {
    this.regexpChunks.add(pattern);
  }

  public void captureQuottedWithoutEscape(String column) {
    this.regexpChunks.add(column);
    match("\"([^\"]*)\"");
  }

  public Pattern compile() {
    String regexp = buildRegexp(this.regexpChunks.size());
    System.out.println(regexp);
    this.compiled = Pattern.compile(regexp);
    return this.compiled;
  }

  public List<String> exec(String line) {
    Matcher m = this.compiled.matcher(line);
    boolean matches = m.matches();
    if (!matches) {
      return null;
    }
    List<String> out = new ArrayList<String>();

    for (int i = 1; i <= m.groupCount(); i++) {
      out.add(m.group(i));
    }
    return out;
  }

  public static class PartialMatch {
    private int matchedChunks;
    private int matchedChars;

    public int getTotalChars() {
      return this.totalChars;
    }

    public void setTotalChars(int totalChars) {
      this.totalChars = totalChars;
    }

    public PartialMatch withTotalChars(int totalChars) {
      this.totalChars = totalChars;
      return this;
    }

    public int getTotalChunks() {
      return this.totalChunks;
    }

    public void setTotalChunks(int totalChunks) {
      this.totalChunks = totalChunks;
    }

    public PartialMatch withTotalChunks(int totalChunks) {
      this.totalChunks = totalChunks;
      return this;
    }

    public int getMatchedChars() {
      return this.matchedChars;
    }

    public void setMatchedChars(int matchedChars) {
      this.matchedChars = matchedChars;
    }

    public PartialMatch withMatchedChars(int matchedChars) {
      this.matchedChars = matchedChars;
      return this;
    }

    public int getMatchedChunks() {
      return this.matchedChunks;
    }

    public void setMatchedChunks(int matchedChunks) {
      this.matchedChunks = matchedChunks;
    }

    public PartialMatch withMatchedChunks(int matchedChunks) {
      this.matchedChunks = matchedChunks;
      return this;
    }

    private int totalChunks;
    private int totalChars;
  }

  public PartialMatch partialExec(String line) {
    PartialMatch pm = new PartialMatch();
    pm.totalChars = line.length();
    pm.totalChunks = this.regexpChunks.size();
    for (int i = this.regexpChunks.size() - 1; i >= 0; i--) {
      String regexp = buildRegexp(i + 1);
      Matcher m = Pattern.compile(regexp).matcher(line);
      boolean found = m.find();
      if (found) {
        System.out.println("MATCHED " + m.group() + " WITH " + regexp);

        pm.matchedChars = m.end();
        pm.matchedChunks = (i + 1);
        return pm;
      }
    }

    return pm;
  }

  public List<String> getColumnNames() {
    return this.columnNames;
  }
}
