package com.company;

import java.util.concurrent.locks.Lock;

public class SimpleReadWriterLock {
    volatile int readers;
    volatile boolean writer;
    ReadLock readLock;
    WriteLock writeLock;

    public SimpleReadWriterLock() {
        Guard guard = new Guard();
        readLock = new ReadLock(guard);
        writeLock = new WriteLock(guard);
    }
    public Lock ReadLock() {
        return ReadLock();
    }

    public Lock WriteLock() {
        return WriteLock();
    }
}
