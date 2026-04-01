package com.bhasaka.ogani.core.models.productCarousel;

public class Product {

    private String title;
    private String category;
    private String image;
    private double originalPrice;
    private double currentPrice;
    private int discount;

    public Product(String title, String category, String image,
                   double originalPrice, double currentPrice, int discount) {
        this.title = title;
        this.category = category;
        this.image = image;
        this.originalPrice = originalPrice;
        this.currentPrice = currentPrice;
        this.discount = discount;
    }

    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getImage() { return image; }
    public double getOriginalPrice() { return originalPrice; }
    public double getCurrentPrice() { return currentPrice; }
    public int getDiscount() { return discount; }
}