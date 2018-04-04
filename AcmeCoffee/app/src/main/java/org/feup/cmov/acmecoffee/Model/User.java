package org.feup.cmov.acmecoffee.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {
    private Long id;
    private String email;
    private String name;
    private String nif;
    private ArrayList<Voucher> vouchers = new ArrayList<>();

    private static User instance = null;
    protected User() {}

    public User(Long id, String email, String name, String nif, String vouchers) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.nif = nif;
        try {
            this.initializeVouchers(new JSONArray(vouchers));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeVouchers(JSONArray array) {
        try {
            JSONObject voucher;
            for(int i = 0;i < array.length();i++) {
                voucher = new JSONObject(array.getString(i));
                System.out.println(voucher.toString());
                this.vouchers.add(new Voucher(voucher.getLong("id"), voucher.getString("type")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static User getInstance() {
        if(instance == null) {
            instance = new User();
        }
        return instance;
    }

    public static void createUser(Long i, String e, String na, String n, String vouchers) {
        instance = new User(i, e, na, n, vouchers);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getNIF() {
        return nif;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNIF(String NIF) {
        this.nif = NIF;
    }

    public static void delete() {
        instance = null;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", nif='" + nif + '\'' +
                ", vouchers=" + vouchers +
                '}';
    }
}
