package com.tarotdt.pas.web.util;

public class SingleCallAsserter { private boolean called;
  
  public void call(String message) { if (this.called) {
      throw new AssertionError("Method has already been called: " + (message == null ? "" : message));
    }
    this.called = true;
  }
}
