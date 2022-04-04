package com.company.ActiveObject;

import com.company.measurement.Calculating;

public class Servant {
    private int size = 0;
    private int capacity = 0;

    public Servant(int capacity) {
        this.capacity = capacity;
    }

    public boolean get(int numOfProd){
        Calculating.externalCalculating();
        size -= numOfProd;
        //System.out.println("Buff size: " + size);
        return true;
    }
    public boolean push(int numOfProd){
        Calculating.externalCalculating();
        size += numOfProd;
        return true;
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

}
