package org.feup.cmov.acmecoffee.Logic;

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

    public static boolean validateFields(String email, String name, String pass, String confirmPass, String nif) {
        if (true) { // aqui é colocar a condição que verifica se o email ainda não existe noutra conta na base de dados
            if(validatePassword(pass,confirmPass) && validateNif(nif)) {
                return true;
            }
        }
        return false;
    }

    private static boolean validatePassword(String password, String confirmPassword) {
        return password.length() >= 6 && password.length() == confirmPassword.length() && password.equals(confirmPassword);
    }

    private static boolean validateNif(String nif) {
        return nif.length() == 9 && nif.matches("[0-9]+");
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
