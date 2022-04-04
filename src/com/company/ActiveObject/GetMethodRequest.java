package com.company.ActiveObject;

public class GetMethodRequest implements MethodRequest{
    private Future future;
    private int products;
    private Servant servant;

    public GetMethodRequest(Future future, int products, Servant servant) {
        this.future = future;
        this.products = products;
        this.servant = servant;
    }

    @Override
    public void call() {
        future.setObject(servant.get(products));
        future.setAvailable(true);
    }

    @Override
    public boolean guard() {
        return servant.getSize() >= products;
    }


}
