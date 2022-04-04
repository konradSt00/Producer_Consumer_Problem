package com.company.ActiveObject;

import com.company.measurement.Calculating;

import java.util.Random;

public class Consumer extends Thread {

    private Proxy proxy;
    private int numOfProducts;
    private boolean exit = false;
    private long consumed = 0;
    private int id;
    private int lost;
    Random random;
    int internalCalc = 0;
    @Override
    public void run() {
        for(int i = 0 ; !exit ; i++){
            consume();
        }
    }

    public Consumer(Proxy proxy, int id) {
        this.id = id;
        this.proxy = proxy;
        random = new Random(id);
    }

    public void consume(){
        numOfProducts =  1 +  random.nextInt(proxy.getBufforCapacity()/2 - 1 );
        Future result = proxy.getObject(numOfProducts);

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

        //boolean res = false;
        //if(result.isAvailable()){
          //  res = result.getObject();
        if(result.isAvailable())
            consumed ++;
        //}else{
          //  lost++;
        //}

    }

    public void stopThread(){
        this.exit = true;
        //System.out.println("Consumer " + id + ": " +internalCalc);
    }
    public void printResults(){
        System.out.println("Consumer " + id + " consumed: " + consumed + " and lost: " + lost);
    }
    public long getConsumed(){return consumed;}
}
