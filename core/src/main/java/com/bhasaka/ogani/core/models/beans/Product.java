package com.bhasaka.ogani.core.models.beans;

import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

public class Product {

    @ValueMapValue
    private String title;
    @ValueMapValue
    private String price;
    @ValueMapValue
    private String image;
    @ValueMapValue
    private String description;
    @ValueMapValue
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