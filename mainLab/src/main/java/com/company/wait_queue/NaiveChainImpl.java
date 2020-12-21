package com.company.wait_queue;
import com.company.Packet;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class NaiveChainImpl extends Thread {

    public NaiveChainImpl(int myIndex, Lock lock, Condition condition, Lock nextLock, Condition nextCondition, LinkedList<Packet>[] packets,
                          AtomicInteger sum, int maxSum) {
        this.myLock = lock;
        this.myCondition = condition;
        this.nextLock = nextLock;
        this.nextCondition = nextCondition;
        this.packets = packets;
        this.myIndex = myIndex;
        this.sum = sum;
        this.maxSum = maxSum;
    }

    public void run() {
        while(true) {
            myLock.lock();
            try {
                while (packets[myIndex].isEmpty()) {
                   myCondition.await();
                }
                sendPacket();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                myLock.unlock();
            }
        }
    }

    private void sendPacket() {
        Packet packet = packets[myIndex].getFirst();
        int to = packet.IndexFor;

        if (to != myIndex) {
            boolean locked = nextLock.tryLock();
            if(locked) {
                packets[(myIndex + 1) % packets.length].addLast(packet);
                packets[myIndex].removeFirst();
                nextCondition.signalAll();
                nextLock.unlock();
            }
        } else {
            packets[myIndex].removeFirst();
            synchronized (sum) {
                int curSum = sum.incrementAndGet();
                if (curSum == maxSum) {
                    sum.notifyAll();
                }
            }
        }
    }

    private LinkedList<Packet>[] packets;
    final Lock myLock;
    final Condition myCondition;
    final Lock nextLock;
    final Condition nextCondition;
    private int myIndex;
    private int maxSum;
    private AtomicInteger sum;
}
