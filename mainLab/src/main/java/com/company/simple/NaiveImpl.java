package com.company.simple;

import com.company.Packet;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;


public class NaiveImpl {

    public NaiveImpl(int count, LinkedList<Packet>[] packets, AtomicInteger sum, int maxSum ) {
        this.count = count;
        this.chain = new NaiveChainImpl[count];

        for(int i = 0; i < count; i++) {
            chain[i] = new NaiveChainImpl(i, this, packets, sum, maxSum);
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
}
