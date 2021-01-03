package com.company;

public class Packet {
    public Object Information = null;
    public int IndexFor = 0;
    public long StartTime = -1;

    public Packet(int IndexFor, Object Information) {
        this.IndexFor = IndexFor;
        this.Information = Information;
    }
}
