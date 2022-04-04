package com.company.measurement;

import com.company.ActiveObject.AOCreator;
import com.company.ActiveObject.Servant;
import com.company.NestedLocks.NLCreator;

import java.util.Arrays;

public class XOpMeasure {
    private int time;
    int numOfTrials = 0;
    public static final int numOfProd = 5;
    public static final int numOfCons = 5;
    public static final int buffCapacity = 100;
    long resultsNL[];
    long resultsAO[];
    long results[][];
    public XOpMeasure(int numOfTrials, int time, int calcRatio) {
        this.numOfTrials = numOfTrials;
        resultsNL = new long[4];
        resultsAO = new long[4];
        results = new long[2][4];
        Arrays.fill(resultsAO, 0);
        Arrays.fill(resultsNL, 0);
        this.time = time;
        Calculating.N = calcRatio;
    }

    public long[][] measure(){
        long nestedResults[][] = new long[numOfTrials][4];
        long fourLocksResults[][] = new long[numOfTrials][4];

        for(int i = 0; i < numOfTrials ; i++){
            NLCreator creator = new NLCreator(numOfCons, numOfProd,buffCapacity, time);
            nestedResults[i] = creator.start(true);

        }
        for(int i = 0; i < numOfTrials ; i++){
            AOCreator aoCreator = new AOCreator(new Servant(buffCapacity), numOfCons, numOfProd, time);
            fourLocksResults[i] = aoCreator.start();
        }

        for(int i = 0; i < numOfTrials; i ++){
            for(int j = 0; j < 4 ; j++){
                resultsAO[j] += fourLocksResults[i][j];
                resultsNL[j] += nestedResults[i][j];
            }
        }
        for(int j = 0; j < 4 ; j++){
            resultsAO[j] /= numOfTrials;
            resultsNL[j] /= numOfTrials;
        }
        results[0] = resultsNL;
        results[1] = resultsAO;
        return results;
        //printResults(resultsAO, "Active Object");
        //printResults(resultsNL, "Nested Locks");

    }
    private void printResults(long results[], String type){
        System.out.println("");
        System.out.println(String.format("Average results for %d trials using %s:", numOfTrials, type));
        System.out.println(String.format("Real Time: %d, CPU time: %d", results[2], results[3]));
        System.out.println(String.format("Producers operations: %d, Consumers operations: %d", results[1], results[0]));
        System.out.println("");
    }

}
