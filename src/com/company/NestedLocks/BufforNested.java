package com.company.NestedLocks;

import com.company.measurement.Calculating;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BufforNested implements Buffor {

    int size = 0;
    int capacity;
    int[] products;
    int getIndex = 0, pushIndex = 0;

    private final Lock lock;
    private final Condition buffNotFull;
    private final Condition buffNotEmpty;
    private final Lock prodLock, consLock;
    public long[] consumersAccess, producersAccess;

    public BufforNested(int capacity) {
        lock = new ReentrantLock(true);
        prodLock = new ReentrantLock(true);
        consLock = new ReentrantLock(true);
        buffNotEmpty = lock.newCondition();
        buffNotFull = lock.newCondition();

        this.capacity = capacity;
        products = new int[capacity];
        this.producersAccess = new long[20];
        this.consumersAccess = new long[20];

        for(int i = 0 ; i < 20 ; i ++){
            this.producersAccess[i] = 0;
            this.consumersAccess[i] = 0;
        }
    }

    @Override
    public int[] get(Consumer consumer, int numOfProducts){
        int getProducts[];
        consLock.lock();
        lock.lock();
        try {
            while(size <= numOfProducts){
                try {
                    buffNotFull.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            consumersAccess[consumer.id] += 1;

            getProducts = getItems(numOfProducts);;

            buffNotEmpty.signal();
        }finally {
            lock.unlock();
            consLock.unlock();
        }

        return getProducts;
    }

    @Override
    public void push(Producent producent, int numOfProducts){
        prodLock.lock();
        lock.lock();
        try{
            while(this.size + numOfProducts >= this.capacity){
                try {
                    buffNotEmpty.await();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            pushItems(numOfProducts);

            producersAccess[producent.id] += 1;

            buffNotFull.signal();

        }
        finally {
            lock.unlock();
            prodLock.unlock();
        }
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public long getConsumersAccess() {
        long result = 0;
        for(long x: consumersAccess){
            result += x;
        }
        return result;
    }

    @Override
    public long getProducersAccess() {
        long result = 0;
        for(long x: producersAccess){
            result += x;
        }
        return result;
    }

    @Override
    public long[] getConsumersAccessArray() {
        return consumersAccess;
    }

    @Override
    public long[] getProducersAccessArray() {
        return producersAccess;
    }

    private void pushItems(int numOfProducts){
        Calculating.externalCalculating();
        for(int j = 0 ; j < numOfProducts; j++){
            size += 1;
            products[pushIndex] = 1;
            pushIndex = (pushIndex + 1) % capacity;
        }
    }
    private int[] getItems(int numOfProducts){
        Calculating.externalCalculating();
        int getProducts[] = new int[numOfProducts];
        for(int j = 0 ; j < numOfProducts; j++){
            size -= 1;
            getProducts[j] = products[getIndex];
            getIndex = (getIndex + 1) % capacity;

        }
        return getProducts;
    }


}
