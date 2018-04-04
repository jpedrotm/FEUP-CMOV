package com.springjpa.controller;

import com.springjpa.model.Customer;
import com.springjpa.model.Voucher;
import com.springjpa.repo.CustomerRepository;
import com.springjpa.repo.VoucherRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/voucher")
public class VoucherController {

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    CustomerRepository customerRepository;

    @RequestMapping(value = "/save", consumes = "application/json", produces = "application/json")
    public ResponseEntity process(@RequestBody String voucher) throws JSONException {
        JSONObject v = new JSONObject(voucher);
        Customer cus = customerRepository.findOne(v.getLong("customer_id"));
        Voucher.VoucherType type = Voucher.getVoucherTypeByString(v.getString("type"));
        Voucher newVoucher = new Voucher(type,cus);
        cus.getVouchers().add(newVoucher);
        if(voucherRepository.save(newVoucher) != null && customerRepository.save(cus) != null) {
            return new ResponseEntity(HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/delete/{id}")
    public ResponseEntity deleteVoucher(@PathVariable("id") Long id){
        voucherRepository.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
