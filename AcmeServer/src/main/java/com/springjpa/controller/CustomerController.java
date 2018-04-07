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

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {
	@Autowired
	CustomerRepository repository;

	@RequestMapping(value = "/save", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> process(@RequestBody String customer) throws JSONException {
        JSONObject cus = new JSONObject(customer);
        Customer newCustomer = new Customer(cus.getString("email"),cus.getString("name"), cus.getString("password"), cus.getString("nif"));
        if(repository.save(newCustomer) != null) {
            return new ResponseEntity<>(newCustomer.toString(), HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

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

    @RequestMapping("/vouchers/{email}")
    public ResponseEntity<String> getCustomerVouchers(@PathVariable("email") String email) throws JSONException {
        Customer result = repository.findByEmail(email);

        if(result != null) {
            return new ResponseEntity<>(result.getVouchersJSON(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

