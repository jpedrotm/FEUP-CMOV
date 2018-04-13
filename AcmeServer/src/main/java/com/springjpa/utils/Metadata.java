package com.springjpa.utils;

import java.io.Serializable;
import java.util.HashMap;

public class Metadata implements Serializable {
    private HashMap<Long, Integer> customerCoffees;
    private HashMap<Long, Double> customerMoney;

    public Metadata() {
        customerCoffees = new HashMap<>();
        customerMoney = new HashMap<>();
    }

    public boolean addUserCoffee(Long id, int amount) {
        if(customerCoffees.containsKey(id)) {
            customerCoffees.put(id, customerCoffees.get(id) + amount);
        } else {
            customerCoffees.put(id, amount);
        }

        return hasNewFreeCoffeeVoucher(id);
    }

    public boolean hasNewFreeCoffeeVoucher(Long id) {
        if(customerCoffees.get(id) == 3) {
            customerCoffees.put(id, 0);
            return true;
        } else {

            return false;
        }
    }

    public boolean addCustomerMoney(Long id, double money) {

        if(customerMoney.containsKey(id)) {
            customerMoney.put(id, customerMoney.get(id) + money);

        } else {
            customerMoney.put(id, money);
        }



        return hasNewDiscountVoucher(id);
    }

    public boolean hasNewDiscountVoucher(Long id) {
        if(customerMoney.get(id) >= 100.0) {
            customerMoney.put(id, customerMoney.get(id) - 100);
            return true;
        } else {
            return false;
        }
    }

    public int getUserCoffees(Long id) {
        return customerCoffees.get(id);
    }

    public HashMap<Long, Double> getCustomerMoney() {
        return customerMoney;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "customerCoffees=" + customerCoffees +
                '}';
    }
}
