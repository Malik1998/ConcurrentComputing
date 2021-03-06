package com.company.good;

import com.company.Packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;


public class NaiveImpl {

    public NaiveImpl(int count, LinkedList<Packet>[] packets, AtomicInteger sum, int maxSum, long[] times ) {
        this.count = count;
        this.chain = new NaiveChainImpl[count];

        for(int i = 0; i < count; i++) {
            chain[i] = new NaiveChainImpl(i, this, packets, sum, maxSum, times);
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
