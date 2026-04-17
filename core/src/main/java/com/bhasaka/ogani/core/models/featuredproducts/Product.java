package com.bhasaka.ogani.core.models.featuredproducts;

import java.util.List;

/**
 * POJO class representing a Product.
 * <p>
 * Holds product details such as title, price, image,
 * and associated category list.
 */
public class Product {

    private String title;
    private String price;
    private String image;
    private List<String> categories;

    /**
     * Constructor to initialize product details.
     *
     * @param title      product title
     * @param price      product price
     * @param image      product image path
     * @param categories list of category keys
     */
    public Product(String title, String price, String image, List<String> categories) {
        this.title = title;
        this.price = price;
        this.image = image;
        this.categories = categories;
    }

    /**
     * Returns product title.
     *
     * @return title of the product
     */
    public String getTitle() { return title; }

    /**
     * Returns product price.
     *
     * @return price of the product
     */
    public String getPrice() { return price; }

    /**
     * Returns product image.
     *
     * @return image path
     */
    public String getImage() { return image; }

    /**
     * Returns product categories.
     *
     * @return list of category keys
     */
    public List<String> getCategories() { return categories; }
}