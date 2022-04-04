package com.company.ActiveObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ActivationQueue<T>{

    private int size;
    private List<T> queue;
    public int pushIndex = 0, getIndex = 0;
    private Lock lock;
    public ActivationQueue(int size) {
        this.lock = new ReentrantLock(true);
        this.size = size;
        this.queue = new ArrayList<>(size);
    }
    // methods used in dispatch ( 1 thread only )
    public T checkFirstMethod(){return queue.get(getIndex);}

    public  T dequeue(){
        T method = queue.get(getIndex);
        getIndex = (getIndex + 1) % size;
        return method;
    }

    public  boolean isEmpty(){
        return getIndex == pushIndex;
    }

    // method  used by many threads
    public  void enqueue(T method){
        lock.lock();
        queue.add(pushIndex, method);
        pushIndex = (pushIndex + 1) % size;
        lock.unlock();
    }
}
