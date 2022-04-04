package com.company.ActiveObject;

import com.company.measurement.Calculating;

import java.util.Random;

public class Producer extends Thread{
    private Proxy proxy;
    private int numOfProducts;
    private int id;
    private long produced = 0;
    private int lost = 0;
    Random random;
    public int internalCalc = 0;
    public Producer(Proxy proxy, int id) {
        this.id = id;
        this.proxy = proxy;
        random = new Random(id);
    }
    private boolean exit = false;
    @Override
    public void run() {
        for(int i = 0; !exit ; i ++){
            produce();
        }
    }

    public void produce(){
        numOfProducts = 1 +  random.nextInt(proxy.getBufforCapacity()/2 - 1 );
        Future result = proxy.pushObject(numOfProducts); // push by proxy

//        while(!result.isAvailable() && !exit){
//            for(int i = 0; i < Calculating.N && !result.isAvailable(); i ++){
//                Calculating.internalCalculating();
//                internalCalc ++;
//            }
//            //break;
//        }
        for(int i = 0; i < Calculating.N ; i ++){
            Calculating.internalCalculating();
            internalCalc ++;
        }
        while(!result.isAvailable() && !exit){
            Calculating.internalCalculating();
            internalCalc ++;
        }
        if(result.isAvailable())
            produced ++;

    }


    public void stopThread() {
        exit = true;
        //System.out.println("Producer " + id + ": " +internalCalc);

    }
    public void printResults(){
        System.out.println("Producer " + id + " produced: " + produced + " and lost: " + lost);
    }
    public long getProduced(){return produced;}



}
