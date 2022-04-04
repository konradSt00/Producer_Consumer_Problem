package com.company.ActiveObject;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AOCreator {
    private int numOfC, numOfP;
    private int time;
    Producer[] producents;
    Consumer[] consumers;
    Proxy buffor;
    Servant servant;
    private long nanoCpuTime = 0;
    private long realTime = 0;
    long[] consumersAccesses, producersAccesses;
    public AOCreator(Servant servant, int numOfC, int numOfP, int time) {
        this.numOfC = numOfC;
        this.numOfP = numOfP;
        producents = new Producer[numOfP];
        consumers  = new Consumer[numOfC];
        buffor = new Proxy(servant);
        this.servant = servant;
        this.time = time;
        consumersAccesses = new long[numOfC];
        producersAccesses = new long[numOfP];
    }


    public long[] start(){
        //Creating threads
        long results[] = new long[4];
        long start = System.currentTimeMillis();

        for(int i = 0; i < numOfP ; i ++){
            producents[i] = new Producer(buffor, i);
            producents[i].start();
        }
        for(int i = 0; i < numOfC ; i ++){
            consumers[i] = new Consumer(buffor, i);
            consumers[i].start();
        }

        //Wait TIME
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //End threads
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        for(int i = 0; i < numOfP ; i ++){
            nanoCpuTime += threadMXBean.getThreadCpuTime(producents[i].getId());
            producersAccesses[i] = producents[i].getProduced();
            producents[i].stopThread();

        }

        for(int i = 0; i < numOfC ; i ++){
            nanoCpuTime += threadMXBean.getThreadCpuTime(consumers[i].getId());
            consumersAccesses[i] = consumers[i].getConsumed();
            consumers[i].stopThread();
        }
        buffor.scheduler.stopThread();
        realTime = System.currentTimeMillis() - start;

//        results[0] = Arrays.stream(consumersAccesses).sum();
//        results[1] = Arrays.stream(producersAccesses).sum();
//        results[2] = realTime;
//        results[3] = (int)(nanoCpuTime/1E6);
        printResults();

        long averageConsEOp = Arrays.stream(consumersAccesses).sum()/numOfC;
        long averageProdsEOp = Arrays.stream(producersAccesses).sum()/numOfP;

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

        return results;
    }

    private void printResults(){



//        System.out.println(String.format("Real Time: %d, CPU time: %d", realTime, (int)(nanoCpuTime/1E6)));
//        System.out.println("Accesses to buffor:");
//        System.out.println("Consumers: " + Arrays.toString(consumersAccesses) + " = " + Arrays.stream(consumersAccesses).sum());
//        System.out.println("Producers: " + Arrays.toString(producersAccesses) + " = " + Arrays.stream(producersAccesses).sum());
//
//        System.out.println("Consumers average access: " + averageConsEOp);
//        System.out.println("Producers average access: " + averageProdsEOp);
//        System.out.println("Consumers average internal operation: " + averageConsIOp);
//        System.out.println("Consumers average internal operation: " + averageProdsIOp);



    }




}
