package com.springjpa.controller;

import com.springjpa.model.Item;
import com.springjpa.repo.ItemRepository;
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
@RequestMapping(value = "/item")
public class ItemController {

    @Autowired
    ItemRepository itemRepository;

    @RequestMapping(value = "/save", consumes = "application/json", produces = "application/json")
    public ResponseEntity process(@RequestBody String item) throws JSONException {
        JSONObject itm = new JSONObject(item);
        Item.ItemType type = Item.getItemTypeByString(itm.getString("type"));
        Item newItem = new Item(itm.getString("name"),itm.getString("price"), type);

        if(itemRepository.save(newItem) != null) {
            return new ResponseEntity(HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/{id}")
    public String findById(@PathVariable("id") Long id) {
        String result = itemRepository.findOne(id).toString();
        return result;
    }

    @RequestMapping("/delete/{id}")
    public ResponseEntity deleteVoucher(@PathVariable("id") Long id){
        itemRepository.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("/findall")
    public ResponseEntity<String> findAll(){
        JSONArray result = new JSONArray();

        for(Item item : itemRepository.findAll()){
            result.put(item.toString());
        }

        return new ResponseEntity<>(result.toString(),HttpStatus.OK);
    }
}
