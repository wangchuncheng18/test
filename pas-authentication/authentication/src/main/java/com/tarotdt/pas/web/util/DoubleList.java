package com.tarotdt.pas.web.util;

import java.util.Iterator;

public class DoubleList
  implements Iterable<Double>
{
  private transient Double[] data;
  private int size;
  
  public DoubleList(int initialCapacity)
  {
    this.data = new Double[initialCapacity];
  }
  
  public DoubleList() {
    this(10);
  }
  
  public DoubleList(DoubleList other)
  {
    this.size = other.size();
    this.data = new Double[other.capacity()];
    System.arraycopy(other.getAllData(), 0, this.data, 0, other.capacity());
  }


  public Double[] getAllData()
  {
    return this.data;
  }

  public Double[] getValidDataCopy()
  {
    Double[] newData = new Double[this.size];
    System.arraycopy(newData, 0, this.data, 0, this.size);
    return newData;
  }
  
  public Double get(int index)
  {
    if (index >= this.size) {
      throw new IndexOutOfBoundsException("index=" + index + " size=" + this.size);
    }
    return this.data[index];
  }
  
  public Double getUnsafe(int index)
  {
    return this.data[index];
  }
  
  public int size() {
    return this.size;
  }
  
  public boolean isEmpty() { return this.size == 0; }
  
  public int capacity()
  {
    return this.data.length;
  }
  
  public void setSize(int newSize) {
    if (newSize > capacity()) {
      throw new IndexOutOfBoundsException("Trying to set size to " + newSize + " but capacity only " + capacity());
    }
    this.size = newSize;
  }
  

  public Double set(int index, Double element)
  {
    if (index >= this.size) {
      throw new IndexOutOfBoundsException("index=" + index + " size=" + this.size);
    }
    
    Double previous = this.data[index];
    this.data[index] = element;
    return previous;
  }
  
  public void add(Double o) {
    if (this.size >= this.data.length) {
      grow(this.size + 1);
    }
    this.data[(this.size++)] = o;
  }
  
  public void grow(int minCapacity)
  {
    int oldCapacity = this.data.length;
    if (minCapacity > oldCapacity) {
      Double[] oldData = this.data;
      int newCapacity = oldCapacity * 3 / 2 + 1;
      if (newCapacity < minCapacity) {
        newCapacity = minCapacity;
      }
      this.data = new Double[newCapacity];
      System.arraycopy(oldData, 0, this.data, 0, this.size);
    }
  }
  
  public void growExact(int capacity) {
    int oldCapacity = this.data.length;
    if (oldCapacity >= capacity) {
      return;
    }
    Double[] oldData = this.data;
    this.data = new Double[capacity];
    System.arraycopy(oldData, 0, this.data, 0, this.size);
  }
  
  public void clear() {
    this.size = 0;
  }
  
  public Iterator<Double> iterator() {
    return new Iterator<Double>() {
      int nextIndex = 0;
      
      public boolean hasNext() {
        return this.nextIndex < DoubleList.this.size();
      }
      
      public Double next()
      {
        return Double.valueOf(DoubleList.this.data[(this.nextIndex++)]);
      }
      
      public void remove() {}
    };
  }
}
