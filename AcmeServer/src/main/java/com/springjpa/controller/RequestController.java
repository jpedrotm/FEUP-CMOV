package com.springjpa.controller;

import com.springjpa.model.*;
import com.springjpa.repo.*;
import com.springjpa.utils.Metadata;
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

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;

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

    @RequestMapping(value = "/save", produces = "application/json")
    public ResponseEntity<String> process(@RequestBody byte[] request) throws JSONException, UnsupportedEncodingException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        for(byte b: request) {
            System.out.println("BYTE: " + b);
        }
        String message = constructMessage(request);

        if(message != null) {
            JSONObject response = new JSONObject(message);

            Customer customer = customerRepository.findOne(response.getLong("customer_id"));
            Request newRequest = new Request(customer);
            requestRepository.save(newRequest);

            JSONArray items = response.getJSONArray("items");
            RequestLine rl;
            double finalPrice = 0.0;
            Item tmpItem;
            for(int i=0;i<items.length();i++) {
                tmpItem = itemRepository.findOne(items.getLong(i));
                if(tmpItem.getType().name().equals("COFFEE")) {
                    if(MetadataManager.getInstance().addUserCoffee(response.getLong("customer_id"),1)){
                        voucherRepository.save(new Voucher(Voucher.VoucherType.FREE_COFFEE,customer));
                    }
                }
                finalPrice += tmpItem.getPrice();
                rl = new RequestLine(newRequest, tmpItem, 1);
                requestLineRepository.save(rl);
            }

            if(MetadataManager.getInstance().addUserDiscount(response.getLong("customer_id"),finalPrice)){
                voucherRepository.save(new Voucher(Voucher.VoucherType.FIVE_PERCENT_DISCOUNT,customer));
            }

            JSONObject result = new JSONObject();
            newRequest.setTotalPrice(finalPrice);
            requestRepository.save(newRequest);

            Long voucher_id = response.getLong("voucher_id");
            if(voucher_id != -1) {
                Voucher v = voucherRepository.findOne(voucher_id);
                if(v.getType().equals(Voucher.VoucherType.FREE_COFFEE)) {
                    result.put("voucher_free_coffee: ", true);
                } else {
                    finalPrice = Math.round((finalPrice *0.95) * 100) / 100;
                }
                voucherRepository.delete(voucher_id);
            }

            result.put("price", finalPrice);
            result.put("request_id", newRequest.getId());
            return new ResponseEntity<>(result.toString(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public String constructMessage(byte[] array) throws JSONException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {

        int sign_size = 512/8;
        int mess_size = array.length - sign_size;

        ByteBuffer buffer = ByteBuffer.wrap(array);
        byte[] mess = new byte[mess_size];
        byte[] sign = new byte[sign_size];
        buffer.get(mess, 0, mess_size);             // extract the message containing nr. of types + type1 + type2 + ... (nr times)
        buffer.get(sign, 0, sign_size);             // extract the signature

        JSONObject response = new JSONObject();
        JSONArray items = new JSONArray();

        int customerId = (int) buffer.get(0);
        response.put("customer_id", customerId);

        Customer customer = customerRepository.findOne((long) customerId);
        BigInteger mod = new BigInteger(customer.getMod());
        BigInteger exp = new BigInteger(customer.getExp());

        System.out.println("MOD: " + mod + "EXP: " + exp);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");                // to build a key object we need a KeyFactory object
        RSAPublicKeySpec RSAPub = new RSAPublicKeySpec(mod, exp);             // the key raw values (as BigIntegers) are used to build an appropriate KeySpec
        PublicKey pKey = keyFactory.generatePublic(RSAPub);                   // the KeyFactory is used to build the key object from the key spec
        Signature sg1 = Signature.getInstance("SHA1WithRSA");
        sg1.initVerify(pKey);                                                 // using the rebuilt key, the signature is verified again

        sg1.update(mess);

        if(!sg1.verify(sign)) {
            return null;
        }

        int nrItems = (int) buffer.get(1);
        if(nrItems > 0) {
            ArrayList<Integer> itemsIds = new ArrayList<>();
            for(int i=0;i < nrItems;i++) {
                items.put((int) buffer.get(i + 2));
                itemsIds.add((int) buffer.get(i + 2));
            }
        }

        response.put("items", items);

        int hasVoucher = buffer.get(nrItems + 2);
        int voucherId = -1;
        if(hasVoucher != 0) {
            voucherId = buffer.get(nrItems + 3);
        }

        response.put("voucher_id", voucherId);

        return response.toString();
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