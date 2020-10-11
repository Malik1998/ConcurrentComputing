package com.company;

import java.util.Arrays;

public class ArrayBarrier implements Barier {

    volatile int[] values;
    int count;
    public ArrayBarrier(int count) {
        values = new int[count];
        this.count = count;
    }

    @Override
    public void EnterBarrier(int index) {
        if (index == 0) {
            values[index] = 1;
            return;
        }

        while(values[index - 1] != 1) {

        }

        values[index] = 1;
    }

    @Override
    public boolean IsTimeToBreakBarrier(int index) {
        //System.out.println(Arrays.toString(values));
        if( index + 1 != count) {
            if (values[index + 1] == 2) {
                values[index] = 2;
            }
        } else {
            values[index] = 2;
        }
        return values[index] == 2;
    }

    @Override
    public void Reset(int count) {
        this.count = count;
        values = new int[count];
    }
}
