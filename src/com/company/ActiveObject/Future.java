package com.company.ActiveObject;

public class Future {

    private boolean object = false;
    private boolean isAvailable = false;

    public synchronized boolean isAvailable(){return isAvailable;};
    public synchronized boolean getObject(){return object;}

    public synchronized void setObject(boolean object) {
        this.object = object;
    }

    public synchronized void setAvailable(boolean available) {
        isAvailable = available;
    }
}
