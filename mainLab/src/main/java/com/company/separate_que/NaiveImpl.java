package com.company.separate_que;

import com.company.Packet;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class NaiveImpl {

    public NaiveImpl(int count, LinkedList<Packet>[] packets, AtomicInteger sum, int maxSum, long[] times ) {
        this.count = count;
        this.chain = new NaiveChainImpl[count];
        this.packets_que = new LinkedList[count];


        this.lock_on_que = new ReentrantLock[count];
        this.conditions = new Condition[count];
        for(int i = 0; i < count; i++) {
            this.lock_on_que[i] = new ReentrantLock();
            this.conditions[i] = this.lock_on_que[i].newCondition();
            this.packets_que[i] = new LinkedList<Packet>();
        }

        for(int i = 0; i < count; i++) {
            chain[i] = new NaiveChainImpl(i, lock_on_que[i], conditions[i],
                    lock_on_que[(i + 1) % count], conditions[(i + 1) % count],
                    packets, sum, maxSum, times, packets_que);
        }
    }

    public void Start() {
        for(int i = 0; i < count; i++) {
            chain[i].start();
        }

    }

    private int count;
    private LinkedList<Packet> packets[];
    private LinkedList<Packet> packets_que[];
    private NaiveChainImpl chain[];
    private ReentrantLock lock_on_que[];
    private Condition conditions[];
}
