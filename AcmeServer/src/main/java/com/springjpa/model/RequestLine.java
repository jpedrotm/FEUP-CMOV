package com.springjpa.model;

import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "request_line")
public class RequestLine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    protected RequestLine() {
    }

    public RequestLine(Request request,Item item, int quantity) {
        this.request = request;
        this.item = item;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public Item getItem() {
        return item;
    }

    public Request getRequest() {
        return request;
    }

    @Override
    public String toString() {
        JSONObject response = new JSONObject();
        try {
            response.put("item_name",item.getName());
            response.put("quantity", quantity);
            response.put("item_price", item.getPrice());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response.toString();
    }
}
