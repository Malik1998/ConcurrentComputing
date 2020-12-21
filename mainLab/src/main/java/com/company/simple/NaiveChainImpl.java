package com.company.simple;
import com.company.Packet;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class NaiveChainImpl extends Thread {

    public NaiveChainImpl(int myIndex, Object lock, LinkedList<Packet>[] packets,
                          AtomicInteger sum, int maxSum) {
        this.lock = lock;
        this.packets = packets;
        this.myIndex = myIndex;
        this.sum = sum;
        this.maxSum = maxSum;
    }

    public void run() {
        while( true ) {
            synchronized (lock) {
                if (!packets[myIndex].isEmpty()) {
                    sendPacket();
                }
            }
        }
    }

    private void sendPacket() {
        Packet packet = packets[myIndex].getFirst();
        int to = packet.IndexFor;
        packets[myIndex].removeFirst();
        if (to != myIndex) {
            packets[(myIndex + 1) % packets.length].addLast(packet);
        } else {
            synchronized (sum) {
                int curSum = sum.incrementAndGet();
                if (curSum == maxSum) {
                    sum.notifyAll();
                }
            }
        }
    }

    private LinkedList<Packet>[] packets;
    final Object lock;
    private int myIndex;
    private int maxSum;
    private AtomicInteger sum;
}
