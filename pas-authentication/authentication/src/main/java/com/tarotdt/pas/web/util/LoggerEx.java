package com.tarotdt.pas.web.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;

public class LoggerEx extends Logger {
  private final List<LoggerFilter> filters = new ArrayList<LoggerFilter>();
  private final Logger delegate;
  private static ThreadLocal<CallTime> tl = new ThreadLocal<>();

  public static LoggerEx getLogger(String loggerName) {
    return new LoggerEx(Logger.getLogger(loggerName));
  }

  public static LoggerEx getLogger(Class clazz) {
    return new LoggerEx(Logger.getLogger(clazz));
  }

  protected LoggerEx(Logger innerLogger) {
    super(innerLogger.getName());
    this.delegate = innerLogger;
  }

  public static void startCurrentCall() {
    if (tl.get() == null)
      tl.set(new CallTime());
    ((CallTime) tl.get()).start = System.currentTimeMillis();
  }

  public static long endCurrentCall() {
    if (tl.get() != null) {
      long start = ((CallTime) tl.get()).start;
      tl.remove();
      return System.currentTimeMillis() - start;
    }
    return 0L;
  }

  private static Object concatTime(Object message) {
    if ((tl.get() != null) && (((CallTime) tl.get()).start != 0L)) {
      String header = "[ct: " + (System.currentTimeMillis() - ((CallTime) tl.get()).start) + "]";
      return header + " " + message;
    }
    return message;
  }

  public LoggerEx addFilter(LoggerFilter filter) {
    this.filters.add(filter);
    return this;
  }

  public boolean accept(Object message) {
    for (LoggerFilter filter : this.filters) {
      if (!filter.accept(message)) {
        return false;
      }
    }
    return true;
  }

  public void addAppender(Appender newAppender) {
    this.delegate.addAppender(newAppender);
  }

  public void assertLog(boolean assertion, String msg) {
    this.delegate.assertLog(assertion, msg);
  }

  public void callAppenders(LoggingEvent arg0) {
    this.delegate.callAppenders(arg0);
  }

  public void debug(Object message, Throwable t) {
    if (accept(message)) {
      this.delegate.debug(concatTime(message), t);
    }
  }

  public void debug(Object message) {
    if (accept(message)) {
      this.delegate.debug(concatTime(message));
    }
  }

  public void debugV(String message, Object... format) {
    debug(String.format(message, format));
  }

  public void traceV(String message, Object... format) {
    trace(String.format(message, format));
  }

  public boolean equals(Object obj) {
    return this.delegate.equals(obj);
  }

  public void error(Object message, Throwable t) {
    if (accept(message)) {
      this.delegate.error(message, t);
    }
  }

  public void error(Object message) {
    if (accept(message)) {
      this.delegate.error(message);
    }
  }

  public void errorV(String message, Object... format) {
    error(String.format(message, format));
  }

  public void fatal(Object message, Throwable t) {
    if (accept(message)) {
      this.delegate.fatal(message, t);
    }
  }

  public void fatal(Object message) {
    if (accept(message)) {
      this.delegate.fatal(message);
    }
  }

  public boolean getAdditivity() {
    return this.delegate.getAdditivity();
  }

  public Enumeration<?> getAllAppenders() {
    return this.delegate.getAllAppenders();
  }

  public Appender getAppender(String name) {
    return this.delegate.getAppender(name);
  }

  public Priority getChainedPriority() {
    return this.delegate.getChainedPriority();
  }

  public Level getEffectiveLevel() {
    return this.delegate.getEffectiveLevel();
  }

  public LoggerRepository getHierarchy() {
    return this.delegate.getHierarchy();
  }

  public LoggerRepository getLoggerRepository() {
    return this.delegate.getLoggerRepository();
  }

  public ResourceBundle getResourceBundle() {
    return this.delegate.getResourceBundle();
  }

  public int hashCode() {
    return this.delegate.hashCode();
  }

  public void info(Object message, Throwable t) {
    if (accept(message)) {
      this.delegate.info(concatTime(message), t);
    }
  }

  public void info(Object message) {
    if (accept(message)) {
      this.delegate.info(concatTime(message));
    }
  }

  public void infoV(String message, Object... format) {
    info(String.format(message, format));
  }

  public boolean isAttached(Appender appender) {
    return this.delegate.isAttached(appender);
  }

  public boolean isDebugEnabled() {
    return this.delegate.isDebugEnabled();
  }

  public boolean isEnabledFor(Priority level) {
    return this.delegate.isEnabledFor(level);
  }

  public boolean isInfoEnabled() {
    return this.delegate.isInfoEnabled();
  }

  public boolean isTraceEnabled() {
    return this.delegate.isTraceEnabled();
  }

  public void l7dlog(Priority arg0, String arg1, Object[] arg2, Throwable arg3) {
    this.delegate.l7dlog(arg0, arg1, arg2, arg3);
  }

  public void l7dlog(Priority arg0, String arg1, Throwable arg2) {
    this.delegate.l7dlog(arg0, arg1, arg2);
  }

  public void log(Priority priority, Object message, Throwable t) {
    if (accept(message)) {
      this.delegate.log(priority, message, t);
    }
  }

  public void log(Priority priority, Object message) {
    if (accept(message)) {
      this.delegate.log(priority, message);
    }
  }

  public void log(String callerFQCN, Priority level, Object message, Throwable t) {
    if (accept(message)) {
      this.delegate.log(callerFQCN, level, message, t);
    }
  }

  public void removeAllAppenders() {
    this.delegate.removeAllAppenders();
  }

  public void removeAppender(Appender appender) {
    this.delegate.removeAppender(appender);
  }

  public void removeAppender(String name) {
    this.delegate.removeAppender(name);
  }

  public void setAdditivity(boolean additive) {
    this.delegate.setAdditivity(additive);
  }

  public void setLevel(Level level) {
    this.delegate.setLevel(level);
  }

  public void setPriority(Priority priority) {
    this.delegate.setPriority(priority);
  }

  public void setResourceBundle(ResourceBundle bundle) {
    this.delegate.setResourceBundle(bundle);
  }

  public String toString() {
    return this.delegate.toString();
  }

  public void trace(Object message, Throwable t) {
    if (accept(message)) {
      this.delegate.trace(message, t);
    }
  }

  public void trace(Object message) {
    if (accept(message)) {
      this.delegate.trace(message);
    }
  }

  public void warn(Object message, Throwable t) {
    if (accept(message)) {
      this.delegate.warn(message, t);
    }
  }

  public void warn(Object message) {
    if (accept(message)) {
      this.delegate.warn(message);
    }
  }

  public void warnV(String message, Object... format) {
    warn(String.format(message, format));
  }

  private static class CallTime {
    long start;
  }
}
