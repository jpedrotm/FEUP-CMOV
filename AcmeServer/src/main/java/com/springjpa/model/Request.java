package com.springjpa.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "request")
public class Request implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "total_price")
    private double totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "request")
    private Set<RequestLine> requestLines = new HashSet<>();

    protected Request() { }

    public Request(Customer customer) {
        this.customer = customer;
    }

    public long getId() {
        return id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Set<RequestLine> getRequestLines() {
        return requestLines;
    }

    @Override
    public String toString() {
        JSONObject response = new JSONObject();
        JSONArray items = new JSONArray();
        try {
            response.put("id",id);
            response.put("total_price", totalPrice);

            for(RequestLine rLine: requestLines) {
                items.put(rLine.toString());
            }
            response.put("items",items);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response.toString();
    }
}

