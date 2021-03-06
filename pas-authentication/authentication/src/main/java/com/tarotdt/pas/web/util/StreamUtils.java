package com.tarotdt.pas.web.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class StreamUtils {
  public static final String defaultEncoding = "UTF-8";

  public static BufferedReader readStream(InputStream input, String encoding)
      throws UnsupportedEncodingException {
    return new BufferedReader(new InputStreamReader(input, encoding));
  }

  public static BufferedReader readStream(InputStream input) {
    try {
      return readStream(input, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      assert false;
    }
    return null;
  }

  public static BufferedReader readFile(File f, String encoding)
      throws FileNotFoundException, UnsupportedEncodingException {
    FileInputStream fis = new FileInputStream(f.getPath());
    InputStreamReader in = new InputStreamReader(fis, encoding);

    return new BufferedReader(in);
  }

  public static BufferedReader readFile(File f) throws FileNotFoundException {
    try {
      return readFile(f, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      assert false;
    }
    return null;
  }

  public static BufferedOutputStream readFD(FileDescriptor fd, String encoding) {
    return new BufferedOutputStream(new FileOutputStream(FileDescriptor.in));
  }

  public static BufferedOutputStream readFD(FileDescriptor fd) {
    return readFD(fd, "UTF-8");
  }

  public static BufferedWriter writeToFile(File f, String encoding, boolean append)
      throws FileNotFoundException, UnsupportedEncodingException {
    return new BufferedWriter(
        new java.io.OutputStreamWriter(new FileOutputStream(f, append), encoding));
  }

  public static BufferedWriter writeToFile(File f, boolean append) throws FileNotFoundException {
    try {
      return writeToFile(f, "UTF-8", append);
    } catch (UnsupportedEncodingException e) {
      assert false;
    }
    return null;
  }

  public static void skip(InputStream i, long n) throws java.io.IOException {
    while (n > 0L) {
      long skipped = i.skip(n);
      if (skipped == 0L) {
        break;
      }
      n -= skipped;
    }
  }
}
