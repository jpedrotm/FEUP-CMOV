package com.springjpa.model;

import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "voucher")
public class Voucher implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    private VoucherType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    protected Voucher() {}


    public Voucher(VoucherType type, Customer customer) {
        this.type = type;
        this.customer = customer;
    }

    public long getId() {
        return id;
    }

    public VoucherType getType() {
        return type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public static VoucherType getVoucherTypeByString(String type) {
        return type.equals("FREE_COFFEE") ? Voucher.VoucherType.FREE_COFFEE : Voucher.VoucherType.FIVE_PERCENT_DISCOUNT;
    }

    @Override
    public String toString() {
        JSONObject response = new JSONObject();
        try {
            response.put("id",id);
            response.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    public enum VoucherType {
        FREE_COFFEE, FIVE_PERCENT_DISCOUNT
    }
}
