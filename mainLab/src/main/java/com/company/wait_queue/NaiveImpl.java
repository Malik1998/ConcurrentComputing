package com.company.wait_queue;

import com.company.Packet;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class NaiveImpl {

    public NaiveImpl(int count, LinkedList<Packet>[] packets, AtomicInteger sum, int maxSum ) {
        this.count = count;
        this.chain = new NaiveChainImpl[count];

        this.locks = new ReentrantLock[count];
        this.conditions = new Condition[count];
        for(int i = 0; i < count; i++) {
            this.locks[i] = new ReentrantLock();
            this.conditions[i] = this.locks[i].newCondition();
        }

        for(int i = 0; i < count; i++) {
            chain[i] = new NaiveChainImpl(i, locks[i], conditions[i],
                    locks[(i + 1) % count], conditions[(i + 1) % count],
                    packets, sum, maxSum);
        }
    }

    public void Start() {
        for(int i = 0; i < count; i++) {
            chain[i].start();
        }

    }

    private int count;
    private LinkedList<Packet> packets[];
    private NaiveChainImpl chain[];
    private ReentrantLock locks[];
    private Condition conditions[];
}
