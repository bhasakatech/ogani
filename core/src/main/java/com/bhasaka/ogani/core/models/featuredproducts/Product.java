package com.bhasaka.ogani.core.models.featuredproducts;

import java.util.List;

public class Product {

    private String title;
    private String price;
    private String image;
    private List<String> categories;

    public Product(String title, String price, String image, List<String> categories) {
        this.title = title;
        this.price = price;
        this.image = image;
        this.categories = categories;
    }

    public String getTitle() { return title; }
    public String getPrice() { return price; }
    public String getImage() { return image; }
    public List<String> getCategories() { return categories; }
}