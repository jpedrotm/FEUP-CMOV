package com.springjpa.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "customer", uniqueConstraints={@UniqueConstraint(columnNames = {"email"})})
public class Customer implements Serializable {

	private static final long serialVersionUID = -3009157732242241606L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "email")
	private String email;

	@Column(name = "name")
	private String name;

	@Column(name = "password")
	private String password;

	@Column(name = "nif")
	private String nif;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "customer")
    private Set<Voucher> vouchers = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "customer")
    private Set<Request> requests = new HashSet<>();

	protected Customer() {
	}

	public Customer(String email, String name, String password, String nif) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.nif = nif;
	}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<Voucher> getVouchers() {
        return vouchers;
    }

    public String getVouchersJSON() throws JSONException {
        JSONArray array = new JSONArray();

	    for(Voucher v: vouchers) {
	        array.put(v.toString());
        }
        JSONObject response = new JSONObject();
	    response.put("vouchers",array);
	    return response.toString();
    }

	@Override
	public String toString() {
        JSONObject response = new JSONObject();
        try {
            response.put("id",id);
            response.put("email", email);
            response.put("name", name);
            response.put("nif", nif);

            JSONArray array = new JSONArray();
            for(Voucher v: vouchers) {
                array.put(v.toString());
            }

            response.put("vouchers", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response.toString();
	}
}
