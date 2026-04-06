package com.bhasaka.ogani.core.models.beans;

public class Product {

    private String title;
    private String price;
    private String image;
    private String description;
    private String[] category;

    public Product(String title, String price, String image,String description, String[] category) {
        this.title = title;
        this.price = price;
        this.image = image;
        this.description=description;
        this.category = category;
    }

    public String getTitle() { return title; }
    public String getPrice() { return price; }
    public String getImage() { return image; }
    public String getDescription(){return description;}
    public String[] getCategory() { return category; }
}