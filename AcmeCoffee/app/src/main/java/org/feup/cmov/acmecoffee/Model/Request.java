package org.feup.cmov.acmecoffee.Model;

/**
 * Created by tomas on 12/04/2018.
 */

public class Request {
    private long id;
    private double totalPrice;

    public Request(long id, double totalPrice){
        this.id = id;
        this.totalPrice = totalPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
