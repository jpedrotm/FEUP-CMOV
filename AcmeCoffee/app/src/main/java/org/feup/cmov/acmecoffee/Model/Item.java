package org.feup.cmov.acmecoffee.Model;

public class Item {
    private String name;
    private String price;
    private itemType type;
    private String image; //tenho de ver melhor isto da imagem mas podemos ter uma para cada tipo

    public Item(String name, String price, itemType type, String image) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public itemType getType() {
        return type;
    }

    public String getImage() {
        return image;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public enum itemType {
        DRINKS, SNACKS, MEALS, COFFEES, CAKES
    }

}