package com.company;

public class Guard {
    private int readers;
    private Object reader;
    private Object writer;

    public boolean getIsWriter() {
        return isWriter;
    }

    public void setIsWriter(boolean isWriter) {
        this.isWriter = isWriter;
    }

    boolean isWriter;

    public Guard() {
        readers = 0;
        isWriter = false;
        writer = new Object();
        reader = new Object();
    }


    public int getReaders() {
        return readers;
    }

    public void setReaders(int readers) {
        this.readers = readers;
    }
}
