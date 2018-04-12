package org.feup.cmov.acmecoffee.Model;

public class Item {
    private Long id;
    private String name;
    private double price;
    private ItemType type;

    public Item(Long id, String name, double price, String type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = getTypeFromString(type);
    }

    private ItemType getTypeFromString(String t) {
        switch(t) {
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public ItemType getType() {
        return type;
    }

    public void setPrice(double price) {
        this.price = price;
    }



    public enum ItemType {
        DRINKS, SNACKS, MEALS, COFFEE, CAKES
    }

}