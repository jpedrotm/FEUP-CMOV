package com.springjpa.model;

import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "item")
public class Item implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Enumerated(EnumType.STRING)
    private ItemType type;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "item")
    private Set<RequestLine> requestLines = new HashSet<>();

    protected Item() {}

    public Item(String name, Double price,ItemType type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public static ItemType getItemTypeByString(String type) {
        switch(type) {
            case "DRINKS":
                return ItemType.DRINKS;
            case "SNACKS":
                return ItemType.SNACKS;
            case "MEALS":
                return ItemType.MEALS;
            case "COFFEE":
                return ItemType.COFFEE;
            case "CAKES":
                return ItemType.CAKES;
                default:
                    return null;
        }
    }

    private static String getItemTypeInString(ItemType type) {
        switch(type) {
            case DRINKS:
                return "DRINKS";
            case SNACKS:
                return "SNACKS";
            case MEALS:
                return "MEALS";
            case COFFEE:
                return "COFFEE";
            case CAKES:
                return "CAKES";
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        JSONObject response = new JSONObject();
        try {
            response.put("id",id);
            response.put("name", name);
            response.put("price", price);
            response.put("type",getItemTypeInString(type));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    public enum ItemType {
        DRINKS, SNACKS, MEALS, COFFEE, CAKES
    }
}
