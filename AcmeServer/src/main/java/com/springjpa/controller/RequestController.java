package com.springjpa.controller;

import com.springjpa.model.*;
import com.springjpa.repo.*;
import com.springjpa.utils.MetadataManager;
import org.json.JSONArray;
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
@RequestMapping(value = "/order")
public class RequestController {
    @Autowired
    RequestRepository requestRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RequestLineRepository requestLineRepository;

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    ItemRepository itemRepository;

    @RequestMapping(value = "/save", consumes = "application/json", produces = "application/json")
    public ResponseEntity process(@RequestBody String request) throws JSONException {
        /*{
            "customer_id":"",
            "items": [
                        {
                            "item_id": "",
                            "quantity": ""
                        }
                    ]
        }*/
        JSONObject response = new JSONObject(request);

        Customer customer = customerRepository.findOne(response.getLong("customer_id"));
        Request newRequest = new Request(customer);
        requestRepository.save(newRequest);

        JSONArray items = response.getJSONArray("items");
        RequestLine rl;
        double finalPrice = 0.0;
        JSONObject tmpItemJSON;
        Item tmpItem;
        for(int i=0;i<items.length();i++) {
            tmpItemJSON = items.getJSONObject(i);
            tmpItem = itemRepository.findOne(tmpItemJSON.getLong("item_id"));
            finalPrice += tmpItem.getPrice();
            rl = new RequestLine(newRequest, tmpItem, tmpItemJSON.getInt("quantity"));
            requestLineRepository.save(rl);
        }

        newRequest.setTotalPrice(finalPrice);
        requestRepository.save(newRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping("/{id}")
    public String findById(@PathVariable("id") Long id) {
        String result = requestRepository.findOne(id).toString();
        return result;
    }

    @RequestMapping("/delete/{id}")
    public ResponseEntity deleteVoucher(@PathVariable("id") Long id){
        requestRepository.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}