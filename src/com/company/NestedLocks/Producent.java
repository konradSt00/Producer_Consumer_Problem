package com.company.NestedLocks;

import com.company.measurement.Calculating;

import java.util.Random;

public class Producent extends Thread{
    public Buffor buffor;
    public int id;
    boolean exit = false;
    Random random;
    int internalCalc = 0;
    public Producent(Buffor buffor, int id) {
        this.buffor = buffor;
        this.id = id;
        random = new Random(2*id);
    }

    @Override
    public void run() {
        for(int i = 0; !exit ; i ++){
            produce();
        }
    }

    private void produce() {
        internalCalculations(Calculating.N);
        int numOfProd = 1 +  random.nextInt(buffor.getCapacity()/2 - 1 );
        buffor.push(this, numOfProd);
    }
    public void stopThread(){
        exit = true;
        //System.out.println("Producer " + id + ": " +internalCalc);

    }
    private void internalCalculations(int n){
        for(int i = 0 ; i < n ; i ++) {
            Calculating.internalCalculating();
            internalCalc ++;
        }
    }
}
