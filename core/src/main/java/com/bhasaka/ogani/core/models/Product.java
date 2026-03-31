package com.bhasaka.ogani.core.models;

public class Product {

    
private String image;
    private String title;
    private String price;

    public Product(String image, String title, String price) {
        this.image = image;
        this.title = title;
        this.price = price;
    }

    public String getImage() { return image; }
    public String getTitle() { return title; }
    public String getPrice() { return price; }
}
