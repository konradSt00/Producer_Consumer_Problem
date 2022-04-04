package com.company.measurement;

public class Calculating {
    public static int N = 1;
    public static int nxE = 1;
    public static int nxI = 1;
    public static void externalCalculating(){
        int k = 1;
        for(int i = 0; i < 2 * nxE; i ++){k*=2;}

    }
    public static void internalCalculating(){
        int k = 1;
        for(int i = 0; i < 200 * nxI; i ++){k*=2;}

    }
}
