package com.springjpa.controller;

import com.springjpa.utils.BCrypt;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springjpa.model.Customer;
import com.springjpa.repo.CustomerRepository;
import org.json.JSONObject;

import java.math.BigInteger;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {
	@Autowired
	CustomerRepository repository;

	@RequestMapping(value = "/save", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> process(@RequestBody String customer) throws JSONException {
        JSONObject cus = new JSONObject(customer);
        Customer newCustomer = new Customer(cus.getString("email"),cus.getString("name"), BCrypt.hashpw(cus.getString("password"), BCrypt.gensalt()), cus.getString("nif"), null, null) ;
        if(repository.save(newCustomer) != null) {
            return new ResponseEntity<>(newCustomer.toString(), HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

	}

    @RequestMapping(value = "/keyinfo", consumes = "application/json", produces = "application/json")
    public ResponseEntity addKeyInfoToCustomer(@RequestBody String request) throws JSONException {
        JSONObject cus = new JSONObject(request);
        Customer customer = repository.findOne(cus.getLong("customer_id"));
        String mod = cus.getString("mod");
        String exp = cus.getString("exp");
        System.out.println("MOD | EXP: " + mod  + "; " + exp);
        customer.setMod(mod);
        customer.setExp(exp);
        repository.save(customer);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping("/{id}")
    public String findById(@PathVariable("id") Long id){
        String result = repository.findOne(id).toString();
        return result;
    }

    @RequestMapping("/findbyemail/{email}")
    public ResponseEntity checkIfCustomerExists(@PathVariable("email") String email) {
        Customer result = repository.findByEmail(email);

        if(result != null) {
            return new ResponseEntity(HttpStatus.FOUND);
        }
        else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/login")
    public ResponseEntity<String> loginCustomer(@RequestBody String customer) throws JSONException {
        JSONObject fields = new JSONObject(customer);
        Customer result = repository.findByEmail(fields.getString("email"));

        if(result != null) {
            if(BCrypt.checkpw(fields.getString("password"), result.getPassword())) {
                return new ResponseEntity<>(result.toString(), HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }

    @RequestMapping("/vouchers/{id}")
    public ResponseEntity<String> getCustomerVouchers(@PathVariable("id") Long id) throws JSONException {
        Customer result = repository.findOne(id);

        if(result != null) {
            return new ResponseEntity<>(result.getVouchersJSON(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

