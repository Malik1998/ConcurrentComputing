package com.company;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    Lock lock;
    Lock lockForTransfer;
    Condition condition;
    volatile int preferredCount;
    volatile int sum;

    public Account() {
        lock = new ReentrantLock();
        lockForTransfer = new ReentrantLock();
        condition = lock.newCondition();
        sum = 0;
        preferredCount = 0;
    }

    public void withdrawPreferred(int k) {
        try{
            lock.lock();
            preferredCount++;
            while( sum < k) {
                condition.await();
            }
            sum -= k;
            preferredCount--;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void deposit(int k) {
        try{
            lock.lock();
            sum += k;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(int k) {
        try{
            lock.lock();
            while( sum < k || preferredCount != 0 ) {
                condition.await();
            }
            sum -= k;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    void transfer(int k, Account reserve) {

        try {
            lockForTransfer.lock();
            reserve.withdraw(k);
            deposit(k);
        }
        finally {
            lockForTransfer.unlock();
        }

    }
}
