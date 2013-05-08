package cs.montclair.softwareeng.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter implements Comparable<Counter> {
   private AtomicInteger value = new AtomicInteger();

   public void increment() {
      value.incrementAndGet();
   }

   public void increment(int value) {
      this.value.addAndGet(value);
   }

   public int getValue() {
      return value.intValue();
   }

   public boolean equals(Object o) {
      if(o instanceof Counter) {
         return getValue() == ((Counter) o).getValue();
      }

      return false;
   }

   public int compareTo(Counter o) {
      return getValue() - o.getValue();
   }

   @Override
   public String toString() {
      return String.valueOf(this.getValue());
   }
}