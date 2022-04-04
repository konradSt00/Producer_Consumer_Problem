package com.company.NestedLocks;

import com.company.measurement.Calculating;

import java.util.Random;

public class Consumer extends Thread{

    public Buffor buffor;
    int id = 0;
    boolean exit = false;
    Random random;
    int internalCalc = 0;
    public Consumer(Buffor buffor, int id) {
        this.buffor = buffor;
        this.id = id;
        random = new Random(id); // seed = id
    }

    @Override
    public void run() {
        for(int i = 0; !exit ; i ++){
            consume();

        }
    }
    public void consume()  {
        internalCalculations(Calculating.N);
        int numOfProd = 1 +  random.nextInt(buffor.getCapacity()/2 - 1 );
        buffor.get(this, numOfProd);

    }
    public void stopThread(){
        exit = true;
        //System.out.println("Consumer " + id + ": " +internalCalc);

    }

    private void internalCalculations(int n){
        for(int i = 0 ; i < n ; i ++) {
            Calculating.internalCalculating();
            internalCalc ++;
        }
    }
}
