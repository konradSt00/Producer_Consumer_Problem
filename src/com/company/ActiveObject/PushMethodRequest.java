package com.company.ActiveObject;

public class PushMethodRequest implements MethodRequest{
    private Future future;
    private int products;
    private Servant servant;

    public PushMethodRequest(Future future, int products, Servant servant) {
        this.future = future;
        this.products = products;
        this.servant = servant;
    }

    @Override
    public void call() {
        future.setObject(servant.push(products));
        future.setAvailable(true);
    }

    @Override
    public boolean guard() {
        return servant.getCapacity() >= servant.getSize() + products;
    }
}
