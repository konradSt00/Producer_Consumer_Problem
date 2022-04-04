package com.company;

import com.company.ActiveObject.Servant;
import com.company.ActiveObject.AOCreator;
import com.company.measurement.Calculating;
import com.company.measurement.Measure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        long avgResults[][] = new long[2][4];
        List<Long> NestedAvgAccess = new ArrayList<>(100);
        List<Long> AoAvgAccess = new ArrayList<>(100);
        List<Long> NestedInternalOp = new ArrayList<>(100);
        List<Long> AoInternalOp = new ArrayList<>(100);

        for(int l = 1; l < 3 ; l ++){
            NestedAvgAccess.clear();
            AoAvgAccess.clear();
            NestedInternalOp.clear();
            AoInternalOp.clear();
            Measure.numOfCons *= l;
            Measure.numOfProd *= l;
            System.out.println("Test " + Measure.numOfProd*2 + " threads. 10 seconds, calcRatio 1000");
            for(int i = 1; i <= 10000000; i *= 10){
                for (long[] row: avgResults)
                    Arrays.fill(row, 0);
                Calculating.nxE = i;
                for(int j = 0; j < 5; j ++){
                    Measure measure = new Measure(1, 10000, 1000);// czas skalowalne

                    long roundResults[][] = measure.measure();
                    avgResults[0][0] += roundResults[0][0];
                    avgResults[0][1] += roundResults[0][1];
                    avgResults[0][2] += roundResults[0][2];
                    avgResults[0][3] += roundResults[0][3];
                    avgResults[1][0] += roundResults[1][0];
                    avgResults[1][1] += roundResults[1][1];
                    avgResults[1][2] += roundResults[1][2];
                    avgResults[1][3] += roundResults[1][3];
                }
                avgResults[0][0] /= 5;
                avgResults[0][1] /= 5;
                avgResults[0][2] /= 5;
                avgResults[0][3] /= 5;

                System.out.println("Round: " + i);
                System.out.println("Nested: " + avgResults[0][0] * 10 + " : " + avgResults[0][1] );
                System.out.println("AO: " + avgResults[1][0] * 10 + " : " + avgResults[1][1] );
                System.out.println("");
                NestedAvgAccess.add(avgResults[0][0]);
                NestedInternalOp.add(avgResults[0][1]);
                AoInternalOp.add(avgResults[1][1]);
                AoAvgAccess.add(avgResults[1][0]);
            }
            System.out.println("NAA");
            for(long x : NestedAvgAccess){
                System.out.println(x);
            }
            System.out.println("AOAA");
            for(long x : AoAvgAccess){
                System.out.println(x);
            }
            System.out.println("NIO");
            for(long x : NestedInternalOp){
                System.out.println(x);
            }
            System.out.println("AOIO");
            for(long x : AoInternalOp){
                System.out.println(x);
            }
        }

        System.exit(0);

    }
}
