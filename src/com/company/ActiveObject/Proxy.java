package com.company.ActiveObject;

public class Proxy {

    private Servant servant;
    public Scheduler scheduler;

    public Proxy(Servant servant) {
        this.servant = servant;
        this.scheduler = new Scheduler();
        scheduler.start();
    }

    Future getObject(int products){
        Future future = new Future();
        GetMethodRequest getMethodRequest = new GetMethodRequest(future, products, servant);
        scheduler.enqueue(getMethodRequest);
        return future;
    }
    Future pushObject(int products){
        Future future = new Future();
        PushMethodRequest pushMethodRequest = new PushMethodRequest(future, products, servant);
        scheduler.enqueue(pushMethodRequest);
        return future;
    }
    public int getBufforCapacity(){
        return servant.getCapacity();
    }

}
