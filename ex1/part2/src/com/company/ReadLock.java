package com.company;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ReadLock implements Lock {

    private Guard guard;

    public ReadLock(Guard guard) {
        this.guard = guard;
    }

    @Override
    public void lock() {
        try {
            synchronized (guard) {
                while (guard.getIsWriter()) {
                    guard.wait();
                }
                guard.setReaders(guard.getReaders() + 1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlock() {
        synchronized (guard) {
            guard.setReaders(guard.getReaders() - 1);

            if (guard.getReaders() == 0) {
                guard.notifyAll();
            }
        }
    }

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
