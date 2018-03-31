package org.feup.cmov.acmecoffee.Model;

import java.util.ArrayList;

public class User {
    private String email;
    private String name;
    private String password;
    private String nif;
    private ArrayList<Voucher> vouchers = new ArrayList<>();

    private static User instance = null;
    protected User() {}

    public User(String email, String name, String pass, String nif) {
        this.email = email;
        this.name = name;
        this.password = pass;
        this.nif = nif;
    }

    public static User getInstance() {
        if(instance == null) {
            instance = new User();
        }
        return instance;
    }

    public static void createUser(String e, String na, String p, String n) {
        instance = new User(e, na, p, n);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getNIF() {
        return nif;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNIF(String NIF) {
        this.nif = NIF;
    }
}
