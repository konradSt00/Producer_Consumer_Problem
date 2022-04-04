package com.company.NestedLocks;

public interface Buffor {
    int[] get(Consumer consumer, int numOfProducts);

    void push(Producent producent, int numOfProducts);

    int getCapacity();

    long getConsumersAccess();

    long getProducersAccess();

    long[] getConsumersAccessArray();

    long[] getProducersAccessArray();
}
