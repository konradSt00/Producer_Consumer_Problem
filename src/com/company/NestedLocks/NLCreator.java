package com.company.NestedLocks;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;

public class NLCreator {
    private int time;
    private int numOfC, numOfP, bufforCapacity;
    Producent[] producents;
    Consumer[] consumers;
    Buffor buffor;
    private long nanoCpuTime = 0;
    private long realTime = 0;
    public NLCreator(int numOfC, int numOfP, int bufforCapacity, int time) {
        this.numOfC = numOfC;
        this.numOfP = numOfP;
        this.bufforCapacity = bufforCapacity;
        producents = new Producent[numOfP];
        consumers  = new Consumer[numOfC];
        this.time = time;
    }

    public long[] start(boolean nestedLocks )  {
        buffor = new BufforNested(bufforCapacity);

        long start = System.currentTimeMillis();
        for(int i = 0; i < numOfP ; i ++){
            producents[i] = new Producent(buffor, i);
            producents[i].start();
        }
        for(int i = 0; i < numOfC ; i ++){
            consumers[i] = new Consumer(buffor, i);
            consumers[i].start();
        }

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        for(int i = 0; i < numOfP; i ++){
            nanoCpuTime += threadMXBean.getThreadCpuTime(producents[i].getId());
            producents[i].stopThread();

        }
        for(int i = 0; i < numOfC ; i ++){
            nanoCpuTime += threadMXBean.getThreadCpuTime(consumers[i].getId());
            consumers[i].stopThread();
        }
        realTime = System.currentTimeMillis() - start;

        long[] results = new long[4];
        long averageConsEOp = Arrays.stream(buffor.getConsumersAccessArray()).sum()/numOfC;
        long averageProdsEOp = Arrays.stream(buffor.getProducersAccessArray()).sum()/numOfP;

        long averageConsIOp = Arrays.stream(consumers)
                .mapToLong(consumer -> consumer.internalCalc)
                .sum()/numOfC;

        long averageProdsIOp = Arrays.stream(producents)
                .mapToLong(producer -> producer.internalCalc)
                .sum()/numOfP;

        results[0] = averageConsEOp;
        results[1] = averageConsIOp;
        results[2] = averageProdsEOp;
        results[3] = averageProdsIOp;
//        result[0] = buffor.getConsumersAccess();
//        result[1] = buffor.getProducersAccess();
//        result[2] = realTime;
//        result[3] = (int)(nanoCpuTime/1E6);
        //printResults();

        return results;
    }

    private void printResults(){
        System.out.println(String.format("Real Time: %d, CPU time: %d", realTime, (int)(nanoCpuTime/1E6)));
        System.out.println("Accesses to buffor:");
        System.out.println("Consumers: " + Arrays.toString(buffor.getConsumersAccessArray()) + " = " + buffor.getConsumersAccess());
        System.out.println("Producers: " + Arrays.toString(buffor.getProducersAccessArray()) + " = " + buffor.getProducersAccess());
    }

}
