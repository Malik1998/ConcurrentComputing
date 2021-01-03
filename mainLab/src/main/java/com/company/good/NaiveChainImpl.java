package com.company.good;
import com.company.Packet;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class NaiveChainImpl extends Thread {

    public NaiveChainImpl(int myIndex, Object lock, LinkedList<Packet>[] packets,
                          AtomicInteger sum, int maxSum, long[] times) {
        this.lock = lock;
        this.packets = packets;
        this.myIndex = myIndex;
        this.sum = sum;
        this.maxSum = maxSum;
        this.times = times;
    }

    public void run() {
        while( true ) {
            synchronized (lock) {
                try {
                    while (packets[myIndex].isEmpty()) {
                        lock.wait();
                    }
                    sendPacket();
                    lock.notifyAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendPacket() {
        Packet packet = packets[myIndex].getFirst();
        int to = packet.IndexFor;
        packets[myIndex].removeFirst();
        if (to != myIndex) {
            if (packet.StartTime == -1) {
                packet.StartTime = System.nanoTime();
            }
            packets[(myIndex + 1) % packets.length].addLast(packet);
        } else {
            innerCount += 1;
            times[myIndex] = (times[myIndex] * (innerCount - 1) + System.nanoTime() - packet.StartTime) / innerCount;
            synchronized (sum) {
                int curSum = sum.incrementAndGet();
                if (curSum == maxSum) {
                    sum.notifyAll();
                }
            }
        }
    }

    long[] times;
    private long innerCount = 0;
    private LinkedList<Packet>[] packets;
    final Object lock;
    private int myIndex;
    private int maxSum;
    private AtomicInteger sum;
}
