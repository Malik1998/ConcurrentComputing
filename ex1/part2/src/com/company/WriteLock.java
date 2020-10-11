package com.company;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class WriteLock implements Lock {


    private Guard guard;

    public WriteLock(Guard guard) {
        this.guard = guard;
    }

    @Override
    public void lock() {
        try {
            synchronized (guard) {
                if (guard.getIsWriter() || guard.getReaders() != 0) {
                    guard.wait();
                }
                guard.setIsWriter(true);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void unlock() {

        synchronized (guard) {
            guard.setIsWriter(false);
            guard.notifyAll();
        }
    }


    // UNUSED

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }



    @Override
    public Condition newCondition() {
        return null;
    }
}
