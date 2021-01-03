package com.company.separate_que;
import com.company.Packet;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class NaiveChainImpl extends Thread {

    public NaiveChainImpl(int myIndex, Lock lock, Condition condition, Lock nextLock, Condition nextCondition, LinkedList<Packet>[] packets,
                          AtomicInteger sum, int maxSum, long[] times, LinkedList<Packet>[] packets_que) {
        this.myLock = lock;
        this.myCondition = condition;
        this.nextLock = nextLock;
        this.nextCondition = nextCondition;
        this.packets = packets;
        this.myIndex = myIndex;
        this.sum = sum;
        this.maxSum = maxSum;
        this.times = times;
        this.packets_que = packets_que;
    }

    public void run() {
        while(true) {

            if(!packets[myIndex].isEmpty()) {
                sendPacket();
            } else {

                myLock.lock();
                try {
                    while (packets_que[myIndex].isEmpty()) {
                        myCondition.await();
                    }
                    packets[myIndex].addLast(packets_que[myIndex].getFirst());
                    packets_que[myIndex].removeFirst();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    myLock.unlock();
                }
            }
        }
    }

    private void sendPacket() {
        Packet packet = packets[myIndex].getFirst();
        int to = packet.IndexFor;

        if (to != myIndex) {
            boolean locked = nextLock.tryLock();
            if(locked) {
                if (packet.StartTime == -1) {
                    packet.StartTime = System.nanoTime();
                }
                packets_que[(myIndex + 1) % packets.length].addLast(packet);
                packets[myIndex].removeFirst();
                nextCondition.signalAll();
                nextLock.unlock();
            }
        } else {
            packets[myIndex].removeFirst();
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

    private LinkedList<Packet>[] packets;
    private LinkedList<Packet>[] packets_que;
    private long[] times;
    private long innerCount = 0;
    final Lock myLock;
    final Condition myCondition;
    final Lock nextLock;
    final Condition nextCondition;
    private int myIndex;
    private int maxSum;
    private AtomicInteger sum;
}
