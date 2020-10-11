package com.company;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TTasCounter implements Barier {

    private volatile int count;
    private int fullCount;
    private AtomicBoolean isLocked;


    public TTasCounter(int fullCount) {
        count = 0;
        this.fullCount = fullCount;
        isLocked = new AtomicBoolean();
        isLocked.set(false);
    }

    public void Reset(int count) {
        fullCount  = count;
        this.count = 0;
        isLocked.set(false);
    }

    @Override
    public void EnterBarrier(int unUsed) {
        Increment();
    }

    @Override
    public boolean IsTimeToBreakBarrier(int unUsed) {
        return getCount() == fullCount;
    }

    private void Increment() {
        lock();
        incrementCounter();
        unlock();
    }

    private int getCount() {
        return count;
    }

    private void lock() {
        while( true ) {
            while( isLocked.get() ) {} // is still locked

            if (isLocked.compareAndSet(false, true)) {
                break;
            }

        }
    }

    private void unlock() {
        isLocked.set(false);
    }

    private void incrementCounter() {
        count++;
    }
}

