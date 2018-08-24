package com.tarotdt.pas.web.util;

import java.util.Iterator;

public class LongList implements Iterable<Long> {
  private transient long[] data;
  private int size;

  public LongList(int initialCapacity) {
    this.data = new long[initialCapacity];
  }

  public LongList() {
    this(10);
  }

  public LongList(LongList other) {
    this.size = other.size();
    this.data = new long[other.capacity()];
    System.arraycopy(other.getAllData(), 0, this.data, 0, other.capacity());
  }

  private long[] getAllData() {
    return this.data;
  }

  public long[] getValidDataCopy() {
    long[] newData = new long[this.size];
    System.arraycopy(newData, 0, this.data, 0, this.size);
    return newData;
  }

  public long get(int index) {
    if (index >= this.size) {
      throw new IndexOutOfBoundsException("index=" + index + " size=" + this.size);
    }
    return this.data[index];
  }

  public long getUnsafe(int index) {
    return this.data[index];
  }

  public int size() {
    return this.size;
  }

  public boolean isEmpty() {
    return this.size == 0;
  }

  public int capacity() {
    return this.data.length;
  }

  public void setSize(int newSize) {
    if (newSize > capacity()) {
      throw new IndexOutOfBoundsException(
          "Trying to set size to " + newSize + " but capacity only " + capacity());
    }
    this.size = newSize;
  }

  public long set(int index, long element) {
    if (index >= this.size) {
      throw new IndexOutOfBoundsException("index=" + index + " size=" + this.size);
    }

    long previous = this.data[index];
    this.data[index] = element;
    return previous;
  }

  public void add(long o) {
    if (this.size >= this.data.length) {
      grow(this.size + 1);
    }
    this.data[(this.size++)] = o;
  }

  public void grow(int minCapacity) {
    int oldCapacity = this.data.length;
    if (minCapacity > oldCapacity) {
      long[] oldData = this.data;
      int newCapacity = oldCapacity * 3 / 2 + 1;
      if (newCapacity < minCapacity) {
        newCapacity = minCapacity;
      }
      this.data = new long[newCapacity];
      System.arraycopy(oldData, 0, this.data, 0, this.size);
    }
  }

  public void growExact(int capacity) {
    int oldCapacity = this.data.length;
    if (oldCapacity >= capacity) {
      return;
    }
    long[] oldData = this.data;
    this.data = new long[capacity];
    System.arraycopy(oldData, 0, this.data, 0, this.size);
  }

  public void clear() {
    this.size = 0;
  }

  public Iterator<Long> iterator() {
    return new Iterator<Long>() {
      int nextIndex = 0;

      public boolean hasNext() {
        return this.nextIndex < LongList.this.size();
      }

      public Long next() {
        return Long.valueOf(LongList.this.data[(this.nextIndex++)]);
      }

      public void remove() {
      }
    };
  }
}
