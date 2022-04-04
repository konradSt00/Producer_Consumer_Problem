package com.company.ActiveObject;

import java.awt.event.MouseWheelEvent;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Scheduler extends Thread {

    private static int SIZE = 10000;
    public ActivationQueue<MethodRequest> pushMethodRequestActivationQueue;
    private ActivationQueue<MethodRequest> getMethodRequestActivationQueue;
    private boolean exit = false;
    private Lock lock;
    private Condition pushNA, getNA, emptyQueues;
    public Scheduler() {

        pushMethodRequestActivationQueue = new ActivationQueue<>(SIZE);
        getMethodRequestActivationQueue = new ActivationQueue<>(SIZE);

        lock = new ReentrantLock(true);
        pushNA = lock.newCondition();
        getNA = lock.newCondition();
        emptyQueues = lock.newCondition();
    }

    @Override
    public void run() {
        for(int i = 0; !exit ; i ++){
            dispatch(i);
        }
    }

    public void dispatch(int i){
        dispatch1();
//        if(i%2 == 0){
//            disp(pushMethodRequestActivationQueue, getMethodRequestActivationQueue, 1);
//        }
//        else{
//            disp(getMethodRequestActivationQueue, pushMethodRequestActivationQueue, 2);
//
//        }

    }

    public void enqueue(PushMethodRequest methodRequest){
        pushMethodRequestActivationQueue.enqueue(methodRequest);
        signalPushOperation();

    }
    public void enqueue(GetMethodRequest methodRequest){
        getMethodRequestActivationQueue.enqueue(methodRequest);
        signalGetOperation();

    }
    private void signalPushOperation(){
        lock.lock();
        getNA.signal();
        emptyQueues.signal();
        lock.unlock();

    }
    private void signalGetOperation(){

        lock.lock();
        pushNA.signal();
        emptyQueues.signal();
        lock.unlock();

    }
    public void stopThread(){this.exit = true;}

    private void disp(ActivationQueue<MethodRequest> firstAQ, ActivationQueue<MethodRequest> secondAQ, int flag){
        Condition firstCond = getNA;
        Condition secondCond = pushNA;
        if(flag == 1){
            firstCond = getNA;
            secondCond = pushNA;
        }
        lock.lock(); // to remove active waiting
        if(firstAQ.isEmpty()){
            if(secondAQ.isEmpty()){
                //BOTH EMPTY
                while(secondAQ.isEmpty()){
                    try {
                        emptyQueues.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                disp(firstAQ, secondAQ, flag);
            }else{
                //PUSH EMPTY, GET NOT EMPTY - check if GET isAvailable
                if(secondAQ.checkFirstMethod().guard()) // check first but not get
                    secondAQ.dequeue().call();
                else {
                    while(firstAQ.isEmpty()){
                        try {
                            firstCond.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    disp(firstAQ, secondAQ, flag);
                }
            }
        }else{
            //PUSH NOT EMPTY - check if PUSH isAvailable
            if(firstAQ.checkFirstMethod().guard()){
                //execute push op
                firstAQ.dequeue().call();
            }
            else{

                //PUSH NA - check GET
                while(secondAQ.isEmpty()){
                    //PUSH NA, GET EMPTY
                    try {
                        secondCond.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                secondAQ.dequeue().call();
            }
        }
        lock.unlock();
    }


    private void dispatch1(){
        lock.lock(); // to remove active waiting
        if(pushMethodRequestActivationQueue.isEmpty()){
            if(getMethodRequestActivationQueue.isEmpty()){
                //BOTH EMPTY
                while(getMethodRequestActivationQueue.isEmpty()){
                    try {
                        emptyQueues.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //dispatch();
            }else{
                //PUSH EMPTY, GET NOT EMPTY - check if GET isAvailable
                if(getMethodRequestActivationQueue.checkFirstMethod().guard()) // check first but not get
                    getMethodRequestActivationQueue.dequeue().call();
                else {
                    while(pushMethodRequestActivationQueue.isEmpty()){
                        try {
                            getNA.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //dispatch();
                }
            }
        }else{
            //PUSH NOT EMPTY - check if PUSH isAvailable
            if(pushMethodRequestActivationQueue.checkFirstMethod().guard()){
                //execute push op
                pushMethodRequestActivationQueue.dequeue().call();
            }
            else{

                //PUSH NA - check GET
                while(getMethodRequestActivationQueue.isEmpty()){
                    //PUSH NA, GET EMPTY
                    try {
                        pushNA.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                getMethodRequestActivationQueue.dequeue().call();
            }
        }
        lock.unlock();
    }


}
