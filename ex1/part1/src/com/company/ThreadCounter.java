package com.company;
import java.util.Random;

public class ThreadCounter extends Thread {

    private Barier counter;
    private final int index;
    private final int fullCount;

    public ThreadCounter(Barier barier, int index, int fullCount) {
        this.counter = barier;
        this.index = index;
        this.fullCount = fullCount;
    }

    public void run() {
          //  System.out.println("New thread start working, my number is " + String.valueOf(index));
            foo();
            counter.EnterBarrier(index);
            // System.out.println("Wait other threads, counter incremented");
            while ( !counter.IsTimeToBreakBarrier(index)) {

            }

            bar();
    }

    private void foo() {
       // System.out.println("Foo, thread :  " + String.valueOf(index));
    }

    private void bar() {
     //   System.out.println("Bar, thread :  " + String.valueOf(index));
    }
}
